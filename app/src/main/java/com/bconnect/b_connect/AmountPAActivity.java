package com.bconnect.b_connect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AmountPAActivity extends AppCompatActivity {

    private String TAG = "SR: "+AmountPAActivity.class.getSimpleName()+": ";
    private int sentActive,is_ch  ;
    double Total_Value;
    BigDecimal Total_Valueresult;
    Integer TotalClient;

    String  empsercode;
 
    private ProgressDialog pDialog;


    TextView pharm_name,pharm_code,pharm_add,pharm_phone_line,pharm_phone_mobile, pharm_doc_name;
    TextView uni_dit_uni_no,uni_dit_uni_total_val,banknameTit;


    String   emp_name,pharm_nameS,BranchCodeS,invoice_noS;
    String   banknameCode,banknameS,paytypeCode,P_Code ,doc_nameS;
    String   rec_Date_dateAF,cheDate_dateAF,PharCode,real_Date,real_Date2,pharm_phone_lineS,pharm_phone_mobileS,PhoneS;


    EditText cheDate,chenam,sernum;

    Intent intent;


    int  paytypeSp_position;

    SimpleDateFormat rec_Date_SDF,cheDate_SDF;

    Button apa_sendbut_ch,apa_sendbut_mouny;


    private static String  banknameUrl;

    Spinner  banknameSp;


    List<HashMap<String, String>>   banknameList;
    private SimpleAdapter   banknameSAdapter ;

    Calendar rec_Date_myCalendar,cheDate_myCalendar,real_Date_myCalendar;



    private ListAdapter branchadapter;

    ArrayList<HashMap<String, String>> AmountListToActivity;

    ListView lv;

    Switch switch_pay_type;


    NestedScrollView scrollView;
    LinearLayout LL_ch,LL_mouny;

    SharedPreferencesSR SPsr=new SharedPreferencesSR();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount_pa);


        basic();
        control();

        new AmountPAActivity.GetBaseData().execute();


    }
    public  void basic(){


        pharm_name=(TextView)findViewById(R.id.pharm_name);
        pharm_code=(TextView)findViewById(R.id.pharm_code);
        pharm_add=(TextView)findViewById(R.id.pharm_add);
        pharm_phone_line=(TextView)findViewById(R.id.pharm_phone_line);
        pharm_phone_mobile=(TextView)findViewById(R.id.pharm_phone_mobile);
        pharm_doc_name=(TextView)findViewById(R.id.pharm_doc_name);
        banknameTit=(TextView)findViewById(R.id.banknameTit);
        uni_dit_uni_no=(TextView)findViewById(R.id.uni_dit_uni_no);
        uni_dit_uni_total_val=(TextView)findViewById(R.id.uni_dit_uni_total_val);

        cheDate=(EditText) findViewById(R.id.cheDate);
        chenam=(EditText) findViewById(R.id.chenam);
        sernum=(EditText) findViewById(R.id.sernum);

        apa_sendbut_ch=(Button) findViewById(R.id.apa_sendbut_ch);
        apa_sendbut_mouny=(Button) findViewById(R.id.apa_sendbut_mouny);


        intent = getIntent();

        PharCode="";
        PharCode = intent.getExtras().getString("PharCode");

        Log.e(TAG, "PharCode PharCode: " + PharCode);

         banknameUrl = "http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/GetBank";


         banknameSp = (Spinner) findViewById(R.id.banknameSp);

         banknameList = new ArrayList<HashMap<String, String>>();


        banknameSAdapter = new SimpleAdapter(this, banknameList, R.layout.bank_name_spinner_item,
                new String[] { "name" }, new int[] { R.id.tvName });


        real_Date_myCalendar = Calendar.getInstance();
        rec_Date_myCalendar = Calendar.getInstance();
        cheDate_myCalendar = Calendar.getInstance();

        SimpleDateFormat real_DateSDF2 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat real_DateSDF = new SimpleDateFormat("dd/MM/yyyy");
        real_Date = real_DateSDF2.format( real_Date_myCalendar.getTime()).toString();
        real_Date2= real_DateSDF.format( real_Date_myCalendar.getTime()).toString();
        cheDate_updateData();

        sentActive=0;

        is_ch=1;


        empsercode="";
        empsercode=SPsr.getSP(getApplicationContext(),"empsercode");
        emp_name="";
        emp_name=SPsr.getSP(getApplicationContext(),"empsername");
        P_Code="";
        doc_nameS="";


        TotalClient=0;


        Bundle bundle= getIntent().getExtras();
        lv = (ListView) findViewById(R.id.branch_list);
        scrollView= (NestedScrollView) findViewById(R.id.apa_NS);
        LL_ch= (LinearLayout) findViewById(R.id.LL_ch);
        LL_mouny= (LinearLayout) findViewById(R.id.LL_mouny);
        switch_pay_type= (Switch) findViewById(R.id.switch_pay_type);


        try{
            AmountListToActivity = ( ArrayList<HashMap<String, String>>) bundle.getSerializable("AmountListToActivity");
            branchadapter = new SimpleAdapter(
                    AmountPAActivity.this, AmountListToActivity,
                    R.layout.amount_pa_list_item, new String[]{"phar_name","mot_code","amt","desc_ar" },
                    new int[]{R.id.client_name ,R.id.client_code,R.id.client_value, R.id.client_service
                            });

            lv.setAdapter(null);

            lv.setAdapter(branchadapter);


                for(int i=0;i<AmountListToActivity.size();i++) {

                    TotalClient=i+1;

                    if(AmountListToActivity.get(i).get("amt").toString().length()>0) {

                        //Total_ValueBig += new BigDecimal(AmountListToActivity.get(i).get("amt").toString());

                        //BigDecimal result = Total_ValueBig.add(new BigDecimal(AmountListToActivity.get(i).get("amt").toString()));
                        Total_Value += Double.parseDouble(AmountListToActivity.get(i).get("amt").toString());

                    }
                }

             Total_Valueresult = new BigDecimal(Total_Value);

             Log.e(TAG, "Total_Value: " +     withSuffix(Total_Valueresult.doubleValue()).toString());


        }catch (Exception ex){
            super.onBackPressed();
        }


    }
    public  void control() {
        ViewCompat.setNestedScrollingEnabled(lv, true);

        switch_pay_type.setChecked(true);
        LL_mouny.setVisibility(View.GONE);
        LL_ch.setVisibility(View.VISIBLE);
        is_ch = 1;

        switch_pay_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switch_pay_type.isChecked()) {
                    LL_ch.setVisibility(View.VISIBLE);
                    LL_mouny.setVisibility(View.GONE);
                    switch_pay_type.setText("حوالة");
                    is_ch = 1;

                } else if (!switch_pay_type.isChecked()) {
                    LL_ch.setVisibility(View.GONE);
                    LL_mouny.setVisibility(View.VISIBLE);
                    switch_pay_type.setText("نقدي");
                    is_ch = 0;
                }
            }
        });




        final DatePickerDialog.OnDateSetListener cheDate_datepicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                cheDate_myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                cheDate_myCalendar.set(Calendar.MONTH, monthOfYear);
                cheDate_myCalendar.set(Calendar.YEAR, year);

                cheDate_updateData();

            }

        };



        cheDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AmountPAActivity.this, cheDate_datepicker,
                        cheDate_myCalendar.get(Calendar.YEAR),
                        cheDate_myCalendar.get(Calendar.MONTH),
                        cheDate_myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        banknameSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {

                HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getAdapter().getItem(position);
                banknameCode = (String) obj.get("code");
                banknameS=(String) obj.get("name");


            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                 Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "تذكر ان تختار نوع التحصيل" , Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                closeKeyboard();
                return false;

            }
        });



        //////////////////////////////////////////// send data ////////////////////////////////////////////


        apa_sendbut_ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (is_ch == 1) {
                    if (chenam.getText().length() > 3) {
                        sentActive = 1;
                    } else if (chenam.getText().length() <= 3) {
                        sentActive = 0;
                        chenam.requestFocus();
                        scrollView.scrollTo(0, scrollView.getBottom()+2/2);

                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), " رقم الحوالة/الشيك اقل من المطلوب" , Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }
                } else if (is_ch == 0) {
                    if (sernum.getText().length() > 3) {
                        sentActive = 1;
                    } else if (sernum.getText().length() <= 3) {
                        sentActive = 0;
                        sernum.requestFocus();
                        scrollView.scrollTo(0, scrollView.getBottom()+2/2);

                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), " رقم الإذن اقل من المطلوب" , Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }



                if(sentActive==1) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(AmountPAActivity.this);


                builder1.setMessage(
                        "اجمالي عدد المبالغ: "
                                +String.valueOf(TotalClient)+"\n"+
                        "المبلغ الاجمالي: "
                                    +String.valueOf(Total_Valueresult)+"\n"
                                    +"رقم الحوالة/الشيك: "
                                    +chenam.getText().toString()+"\n"
                                    +"اسم البنك: "
                                    +banknameS.toString()+"\n"
                                + "\nتأكيد التسديد؟");


                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "تأكيد",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                if (sentActive == 1) {
                                    new AmountPAActivity.Update_Fees_ch().execute();

                                }


                            }
                        });

                builder1.setNegativeButton(
                        "لا",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

              }


            }
        });
        apa_sendbut_mouny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (is_ch == 1) {
                    if (chenam.getText().length() > 3) {
                        sentActive = 1;
                    } else if (chenam.getText().length() <= 3) {
                        sentActive = 0;
                        chenam.requestFocus();
                        scrollView.scrollTo(0, scrollView.getBottom()+2/2);

                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), " رقم الحوالة/الشيك اقل من المطلوب" , Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }
                } else if (is_ch == 0) {
                    if (sernum.getText().length() > 3) {
                        sentActive = 1;
                    } else if (sernum.getText().length() <= 3) {
                        sentActive = 0;
                        sernum.requestFocus();
                        scrollView.scrollTo(0, scrollView.getBottom()+2/2);

                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), " رقم الإذن اقل من المطلوب" , Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }



                if(sentActive==1) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(AmountPAActivity.this);


                builder1.setMessage(
                        "اجمالي عدد المبالغ: "
                                +String.valueOf(TotalClient)+"\n"+
                                "المبلغ الاجمالي: "
                                +String.valueOf(Total_Valueresult)+"\n"
                                    +"رقم الإذن: "
                                    +sernum.getText().toString()+"\n"

                                + "\nتأكيد التسديد؟");


                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "تأكيد",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                    new AmountPAActivity.Update_Fees_mouny().execute();


                            }
                        });

                builder1.setNegativeButton(
                        "لا",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

                }
            }

        });




    }

    public static String withSuffix(double count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f %c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp-1));
    }
    private class GetBaseData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AmountPAActivity.this);
            pDialog.setMessage("إنتظر قليلا...");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {

                 WebServiceListHttpHolder sh4 = new WebServiceListHttpHolder();

                 String jsonStr4 = sh4.makeServiceCall(banknameUrl);



                if (jsonStr4 != null) {
                    try {

                        JSONObject jsonObj4 = new JSONObject(jsonStr4);
                        JSONArray jsonCall4 = jsonObj4.getJSONArray("Table");



                         banknameList.clear();


                        for (int iiii = 0; iiii < jsonCall4.length(); iiii++) {
                            JSONObject c4 = jsonCall4.getJSONObject(iiii);

                            String code = c4.getString("code");
                            String name = c4.getString("name");

                            HashMap<String, String> contact = new HashMap<>();

                            contact.clear();
                            contact.put("code", code);
                            contact.put("name", name);

                            banknameList.add(contact);
                        }

                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                    }
                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                    SPsr.removeAllSP(AmountPAActivity.this);
                    finish();
                    Intent intent = new Intent(AmountPAActivity.this, LoginActivity.class); intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
            catch(Exception ex)
            {
                Toast.makeText(getApplicationContext(),
                        "تعذر الإتصال بالإنترنت, راجع مزود الإنترنت",
                        Toast.LENGTH_LONG)
                        .show();
                Log.e(TAG, "getAllClient Error: " + ex.getMessage());


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            try{

                // Dismiss the progress dialog
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }


                banknameSp.setAdapter(banknameSAdapter);

            }
            catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }





        }

    }

    private class Update_Fees_ch extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AmountPAActivity.this);
            pDialog.setMessage("جاري الان التسديد");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {

                WebServiceCall webc =new WebServiceCall(AmountPAActivity.this);


                if(AmountListToActivity.size()>0){




                    for(int i=0;i<AmountListToActivity.size();i++) {
                        Log.e(TAG, "table_name: " + AmountListToActivity.get(i).get("table_name").toString());


                        webc.update_fees_for_AP("1",AmountListToActivity.get(i).get("table_name").toString(),"86",
                                cheDate_dateAF,chenam.getText().toString(),banknameCode,AmountListToActivity.get(i).get("serial").toString(),AmountListToActivity.get(i).get("reciept_serial").toString());



                    }


                }
            }
            catch(Exception ex)
            {
                Toast.makeText(getApplicationContext(),
                        "تعذر الإتصال بالإنترنت, راجع مزود الإنترنت",
                        Toast.LENGTH_LONG)
                        .show();
                Log.e(TAG, "getAllClient Error: " + ex.getMessage());


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            try{

                // Dismiss the progress dialog
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }

                if(WebServiceResult.ErrorID==0){

                    Toast.makeText(getApplicationContext(),
                            "تمت العملية بنجاح",
                            Toast.LENGTH_LONG)
                            .show();

                    AmountPAActivity.super.onBackPressed();

                }
                if(WebServiceResult.ErrorID==1){

                    Toast.makeText(getApplicationContext(),
                            "حدث خطأ",
                            Toast.LENGTH_LONG)
                            .show();
                    Log.e(TAG, "Update_Fees_ch: " + WebServiceResult.ErrorMessage.toString());

                    AmountPAActivity.super.onBackPressed();


                }

            }
            catch (Exception ex) {


                Log.e(TAG, "Update_Fees_ch: " + ex.getMessage());

            }





        }

    }


    private class Update_Fees_mouny extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AmountPAActivity.this);
            pDialog.setMessage("جاري الان التسديد");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {

                WebServiceCall webc =new WebServiceCall(AmountPAActivity.this);

                if(AmountListToActivity.size()>0){




                    for(int i=0;i<AmountListToActivity.size();i++) {
                        Log.e(TAG, "table_name: " + AmountListToActivity.get(i).get("table_name").toString());


                        webc.update_fees_for_AP("1",AmountListToActivity.get(i).get("table_name").toString(),"58",cheDate_dateAF,sernum.getText().toString(),"0",AmountListToActivity.get(i).get("serial").toString(),AmountListToActivity.get(i).get("reciept_serial").toString());



                    }


                }

            }
            catch(Exception ex)
            {
                Toast.makeText(getApplicationContext(),
                        "تعذر الإتصال بالإنترنت, راجع مزود الإنترنت",
                        Toast.LENGTH_LONG)
                        .show();
                Log.e(TAG, "Update_Fees_mouny: " + ex.getMessage());


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            try{

                // Dismiss the progress dialog
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }

                if(WebServiceResult.ErrorID==0){

                    Toast.makeText(getApplicationContext(),
                            "تمت العملية بنجاح",
                            Toast.LENGTH_LONG)
                            .show();

                    AmountPAActivity.super.onBackPressed();

                }
                if(WebServiceResult.ErrorID==1){

                    Toast.makeText(getApplicationContext(),
                            "حدث خطأ",
                            Toast.LENGTH_LONG)
                            .show();
                    Log.e(TAG, "Update_Fees_mouny: " + WebServiceResult.ErrorMessage.toString());

                    AmountPAActivity.super.onBackPressed();
                }

            }
            catch (Exception ex) {
                Log.e(TAG, "Update_Fees_mouny: " + ex.getMessage());
            }





        }

    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(cheDate.getWindowToken(), 0);
    }
    private void  cheDate_updateData() {

        try {

            cheDate_SDF = new SimpleDateFormat("dd/MM/yyyy");
            cheDate_dateAF = cheDate_SDF.format( cheDate_myCalendar.getTime()).toString();
            SimpleDateFormat cheDate_SDF2 = new SimpleDateFormat("dd/MM/yyyy");
            String cheDate_dateAF2 = cheDate_SDF2.format( cheDate_myCalendar.getTime()).toString();
            cheDate.setText(cheDate_dateAF2);
        }
        catch (Exception e) {
            //The handling for the code
        }

    }

}
