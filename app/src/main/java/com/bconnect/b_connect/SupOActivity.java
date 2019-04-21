package com.bconnect.b_connect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bconnect.b_connect.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SupOActivity extends AppCompatActivity {

    private String TAG = "SR: "+OrderActivity.class.getSimpleName()+": ";
    private int sentActive ;

    SharedPreferences pref ;
    SharedPreferences.Editor editor;
    String  empsercode;

    private ProgressDialog pDialog;


    TextView pharm_name,pharm_code,pharm_add,pharm_phone_line,pharm_phone_mobile, pharm_doc_name;
    TextView  banknameTit,pharm_month_val,servtypeTit;
    TextInputLayout cheDateT;

    String  emp_name,pharm_nameS,BranchCodeS;
    String   banknameCode,servtypeCode,paytypeCode,P_Code ,doc_nameS;
    String   rec_Date_dateAF,cheDate_dateAF,real_Date,real_Date2,pharm_phone_lineS,pharm_phone_mobileS,PhoneS,yearS,MonthS;


    EditText nc_rep_code,rec_Date,cheDate,chenam,con_note;

    EditText total_value;



    Intent intent;




    int  paytypeSp_position,is_Month,is_reused,is_Total_V;

    SimpleDateFormat rec_Date_SDF,cheDate_SDF;

    Button nc_sendbut,nc_rep_code_vil_but;

    private static String paytypeUrl,banknameUrl,servtypeUrl;

    Spinner paytypeSp,banknameSp,servtypeSp;


    List<HashMap<String, String>> paytypeList,banknameList,servtypeSpList;
    private SimpleAdapter  paytypeSAdapter,banknameSAdapter,servtypeSpAdapter ;

    Calendar rec_Date_myCalendar,cheDate_myCalendar,real_Date_myCalendar;




    String mot_code,month_target,month_done,pharm_month_valS,empnameS,empcodeS,SearchToS,recS;


    Switch sup_sw_month_value,sup_sw_rec_code_reuse;

    NestedScrollView scrollView;
    SharedPreferencesSR SPsr=new SharedPreferencesSR();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_o);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        basic();
        control();


        new SupOActivity.GetClientData_Master_Code().execute();
        new SupOActivity.GetClient_by_DCode().execute();


    }


    public  void basic(){

        scrollView= (NestedScrollView) findViewById(R.id.SupO_NS);


        pharm_name=(TextView)findViewById(R.id.pharm_name);
        pharm_code=(TextView)findViewById(R.id.pharm_code);
        pharm_add=(TextView)findViewById(R.id.pharm_add);
        pharm_phone_line=(TextView)findViewById(R.id.pharm_phone_line);
        pharm_phone_mobile=(TextView)findViewById(R.id.pharm_phone_mobile);
        pharm_doc_name=(TextView)findViewById(R.id.pharm_doc_name);
        banknameTit=(TextView)findViewById(R.id.banknameTit);
        pharm_month_val=(TextView)findViewById(R.id.pharm_month_val);
        servtypeTit=(TextView)findViewById(R.id.servtypeTit);

        cheDateT=(TextInputLayout) findViewById(R.id.cheDateT);


        nc_sendbut=(Button) findViewById(R.id.nc_sendbut);
        nc_rep_code_vil_but=(Button) findViewById(R.id.nc_rep_code_vil_but);


        sup_sw_month_value=(Switch) findViewById(R.id.sup_sw_month_value);
        sup_sw_rec_code_reuse=(Switch) findViewById(R.id.sup_sw_rec_code_reuse);



        total_value=(EditText) findViewById(R.id.total_value);
        nc_rep_code=(EditText) findViewById(R.id.nc_rep_code);
        rec_Date=(EditText) findViewById(R.id.rec_Date);
        cheDate=(EditText) findViewById(R.id.cheDate);
        chenam=(EditText) findViewById(R.id.chenam);
        con_note=(EditText) findViewById(R.id.con_note);

        nc_rep_code_vil_but.setBackgroundColor(Color.RED);
        nc_rep_code_vil_but.setText("التحقق من صلاحية رقم الإيصال");
        sentActive=0;


        sentActive=0;
        is_Total_V=0;

        sup_sw_rec_code_reuse.setChecked(false);



        pref= getApplicationContext().getSharedPreferences("MySr", MODE_PRIVATE);
        editor = pref.edit();

        empsercode="";
        empsercode=SPsr.getSP(getApplicationContext(),"empsercode");
        emp_name="";
        emp_name=SPsr.getSP(getApplicationContext(),"empsername");
        P_Code="";
        doc_nameS="";


        intent = getIntent();
        P_Code  = intent.getExtras().getString("phar_code");
        mot_code = intent.getExtras().getString("mot_code");
        month_done  = intent.getExtras().getString("month_done");
        month_target = intent.getExtras().getString("month_target");


        Log.e("SR", "phar_code :" + P_Code);
        Log.e("SR", "mot_code :" + mot_code);
        Log.e("SR", "month_done :" + month_done);
        Log.e("SR", "month_target :" + month_target);


        SimpleDateFormat spf=new SimpleDateFormat("MM-yyyy");
        Date newDate= null;
        try {
            newDate = spf.parse(month_target);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("yyyy");
        yearS = spf.format(newDate);


        Log.e("SR", "yearS :" + yearS);

        SimpleDateFormat spf2=new SimpleDateFormat("MM-yyyy");
        Date newDate2= null;
        try {
            newDate2 = spf2.parse(month_target);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf2= new SimpleDateFormat("MM");
        MonthS = spf2.format(newDate2);

        Log.e("SR", "MonthS :" + MonthS);


        paytypeUrl = "http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/GetPayWay";
        banknameUrl = "http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/GetBank";
        servtypeUrl = "http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/Get_other_def";


        paytypeSp = (Spinner) findViewById(R.id.paytypeSp);
        banknameSp = (Spinner) findViewById(R.id.banknameSp);
        servtypeSp = (Spinner) findViewById(R.id.servtypeSp);

        paytypeList = new ArrayList<HashMap<String, String>>();
        banknameList = new ArrayList<HashMap<String, String>>();
        servtypeSpList = new ArrayList<HashMap<String, String>>();

        paytypeSAdapter = new SimpleAdapter(this, paytypeList, R.layout.pay_way_spinner_item,
                new String[] { "name" }, new int[] { R.id.tvName });
        banknameSAdapter = new SimpleAdapter(this, banknameList, R.layout.bank_name_spinner_item,
                new String[] { "name" }, new int[] { R.id.tvName });
        servtypeSpAdapter = new SimpleAdapter(this, servtypeSpList, R.layout.sr_spinner_item,
                new String[] { "name" }, new int[] { R.id.tvName });


        real_Date_myCalendar = Calendar.getInstance();
        rec_Date_myCalendar = Calendar.getInstance();
        cheDate_myCalendar = Calendar.getInstance();

        SimpleDateFormat real_DateSDF2 = new SimpleDateFormat("yyyy/dd/MM");
        SimpleDateFormat real_DateSDF = new SimpleDateFormat("yyyy/MM/dd");
        real_Date = real_DateSDF2.format( real_Date_myCalendar.getTime()).toString();
        real_Date2= real_DateSDF.format( real_Date_myCalendar.getTime()).toString();
        rec_Date_updateData();
        cheDate_updateData();

    }

    public  void control(){

        total_value.setEnabled(false);

        is_Month=0;
        is_reused=0;
        if(total_value.getText().toString().length()<1){
            is_Total_V=0;
        }

        if(sup_sw_month_value.isChecked()){
            is_Month=1;
            total_value.setEnabled(false);
            total_value.setText(pharm_month_valS);
            servtypeSp.setVisibility(View.GONE);
            servtypeTit.setVisibility(View.GONE);

        }else {
            is_Month=0;
            total_value.setEnabled(true);
            total_value.setText("");
            servtypeSp.setVisibility(View.VISIBLE);
            servtypeTit.setVisibility(View.VISIBLE);
        }

        if(sup_sw_rec_code_reuse.isChecked()){
            is_reused=1;

        }else {
            is_reused=0;

        }

        total_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()<1){
                    is_Total_V=0;
                }
                if(s.toString().length()>1){
                    is_Total_V=1;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        sup_sw_month_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sup_sw_month_value.isChecked()){
                    is_Month=1;
                    total_value.setEnabled(false);
                    total_value.setText(pharm_month_valS);
                    servtypeSp.setEnabled(false);
                    servtypeSp.setVisibility(View.GONE);
                    servtypeTit.setVisibility(View.GONE);

                }else {

                    is_Month=0;
                    total_value.setEnabled(true);
                    total_value.setText("");
                    servtypeSp.setEnabled(true);
                    servtypeSp.setVisibility(View.VISIBLE);
                    servtypeTit.setVisibility(View.VISIBLE);
                }
            }
        });
        sup_sw_rec_code_reuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sup_sw_rec_code_reuse.isChecked()){
                    is_reused=1;

                }else {
                    is_reused=0;

                }
            }
        });



        final DatePickerDialog.OnDateSetListener rec_Date_datepicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                rec_Date_myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                rec_Date_myCalendar.set(Calendar.MONTH, monthOfYear);
                rec_Date_myCalendar.set(Calendar.YEAR, year);

                rec_Date_updateData();

            }

        };

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


        rec_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SupOActivity.this, rec_Date_datepicker,
                        rec_Date_myCalendar.get(Calendar.YEAR),
                        rec_Date_myCalendar.get(Calendar.MONTH),
                        rec_Date_myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
        cheDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SupOActivity.this, cheDate_datepicker,
                        cheDate_myCalendar.get(Calendar.YEAR),
                        cheDate_myCalendar.get(Calendar.MONTH),
                        cheDate_myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });


        servtypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {

                HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getAdapter().getItem(position);
                servtypeCode = (String) obj.get("code");
                String servtypeName = (String) obj.get("name");
                Toast.makeText(SupOActivity.this,  "الخدمة: " +servtypeName, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(SupOActivity.this, "تذكر ان تختار نوع التحصيل " , Toast.LENGTH_SHORT).show();

            }
        });
        paytypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {

                HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getAdapter().getItem(position);
                paytypeCode = (String) obj.get("code");
                paytypeSp_position=position;
                if(position==0){
                    Toast.makeText(SupOActivity.this, "تحصيل بشيك " , Toast.LENGTH_SHORT).show();

                    banknameTit.setEnabled(true);
                    banknameTit.setVisibility(View.VISIBLE);



                    banknameSp.setEnabled(true);
                    banknameSp.setVisibility(View.VISIBLE);

                    chenam.setEnabled(true);
                    chenam.setVisibility(View.VISIBLE);

                    cheDate.setEnabled(true);
                    cheDate.setVisibility(View.VISIBLE);

                    cheDateT.setEnabled(true);
                    cheDateT.setVisibility(View.VISIBLE);
                    //cheDateTit.setVisibility(View.VISIBLE);

                }
                if(position==1){



                    Toast.makeText(SupOActivity.this, "تحصيل نقدي" , Toast.LENGTH_SHORT).show();

                    banknameTit.setEnabled(false);
                    banknameTit.setVisibility(View.GONE);
                    banknameSp.setEnabled(false);
                    banknameSp.setVisibility(View.GONE);
                    chenam.setEnabled(false);
                    chenam.setVisibility(View.GONE);
                    cheDate.setEnabled(false);
                    cheDate.setVisibility(View.GONE);

                    cheDateT.setEnabled(false);
                    cheDateT.setVisibility(View.GONE);
                    // cheDateTit.setVisibility(View.GONE);
                }
                if(position==2){
                    Toast.makeText(SupOActivity.this, "تحصيل بحوالة بنكية" , Toast.LENGTH_SHORT).show();

                    banknameTit.setEnabled(true);
                    banknameTit.setVisibility(View.VISIBLE);

                    banknameSp.setEnabled(true);
                    banknameSp.setVisibility(View.VISIBLE);

                    chenam.setEnabled(true);
                    chenam.setVisibility(View.VISIBLE);

                    cheDate.setEnabled(true);
                    cheDate.setVisibility(View.VISIBLE);

                    cheDateT.setEnabled(true);
                    cheDateT.setVisibility(View.VISIBLE);
                    //  cheDateTit.setVisibility(View.VISIBLE);

                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(SupOActivity.this, "تذكر ان تختار نوع التحصيل " , Toast.LENGTH_SHORT).show();

            }
        });

        banknameSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {

                HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getAdapter().getItem(position);
                banknameCode = (String) obj.get("code");

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(SupOActivity.this, "تذكر ان تختار نوع التحصيل " , Toast.LENGTH_SHORT).show();

            }
        });


        nc_rep_code_vil_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SupOActivity.GetReciept_ver().execute();
            }
        });
        nc_rep_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nc_rep_code_vil_but.setBackgroundColor(Color.RED);
                nc_rep_code_vil_but.setText("التحقق من صلاحية رقم الإيصال");
                sentActive=0;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(total_value.getText().toString().length()<1){
                    is_Total_V=0;
                }
                if(total_value.getText().toString().length()>1){
                    is_Total_V=1;
                }

                closeKeyboard();
                return false;

            }
        });



        //SR: Start send data

        nc_sendbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                StringBuilder t = new StringBuilder();
//                for(int l=(uni_num_INT); l<(uni_num_sall_INT+uni_num_INT); l++){
//                     t.append(l);
//                    uni_note.setText(t);
//                }
                if(is_Total_V==0) {
                    Toast.makeText(getApplicationContext(),
                            "اجمالي المبلغ فارغ او اقل من المطلوب",
                            Toast.LENGTH_LONG)
                            .show();
                }
                if(is_Total_V==1){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SupOActivity.this);
                    if(sentActive==0) {

                        builder1.setMessage("سيتم الان التحقق من رقم الإيصال: "
                                +nc_rep_code.getText().toString()+"\n"+
                                "المبلغ: "
                                +total_value.getText().toString()+"\n"

                                +"\nتأكيد التحصيل؟");

                    }
                    if(sentActive==1) {

                        builder1.setMessage(
                                "المبلغ: "
                                        +total_value.getText().toString()+"\n"

                                        +"\nتأكيد التحصيل؟");

                    }
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "تأكيد",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    if(sentActive==0) {

                                        new SupOActivity.GetReciept_AND_Save_Sup_Fees().execute();
                                    }
                                    if(sentActive==1) {
                                        new SupOActivity.Save_Sup_Fees().execute();

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



    }


    private void  rec_Date_updateData() {

        try {

            rec_Date_SDF = new SimpleDateFormat("yyyy/MM/dd");
            rec_Date_dateAF = rec_Date_SDF.format( rec_Date_myCalendar.getTime()).toString();
            SimpleDateFormat rec_Date_SDF2 = new SimpleDateFormat("yyyy/MM/dd");
            String rec_Date_dateAF2 = rec_Date_SDF2.format( rec_Date_myCalendar.getTime()).toString();
            rec_Date.setText(rec_Date_dateAF2);

        }
        catch (Exception e) {
            //The handling for the code
        }

    }
    private void  cheDate_updateData() {

        try {

            cheDate_SDF = new SimpleDateFormat("yyyy/MM/dd");
            cheDate_dateAF = cheDate_SDF.format( cheDate_myCalendar.getTime()).toString();
            SimpleDateFormat cheDate_SDF2 = new SimpleDateFormat("yyyy/MM/dd");
            String cheDate_dateAF2 = cheDate_SDF2.format( cheDate_myCalendar.getTime()).toString();
            cheDate.setText(cheDate_dateAF2);
        }
        catch (Exception e) {
            //The handling for the code
        }

    }


    private class GetReciept_ver extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SupOActivity.this);
            pDialog.setMessage("يتم الان التحقق من رقم الإيصال");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {
                WebServiceCall webc =new WebServiceCall(SupOActivity.this);
                if(nc_rep_code.getText().toString().length()>=3) {
                    webc.GetReciept_ver(nc_rep_code.getText().toString());
                }
                else{
                    WebServiceResult.ErrorID=010;
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

                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }


                if(WebServiceResult.ErrorID==4){
                    nc_rep_code_vil_but.setBackgroundColor(Color.GREEN);
                    nc_rep_code_vil_but.setText("تم التحقق");
                    sentActive=1;
                    Toast.makeText(getApplicationContext(),
                            "تم التحقق من صلاحية رقم الإيصال , يمكن الاستمرار الان",
                            Toast.LENGTH_LONG)
                            .show();
                }
                if(WebServiceResult.ErrorID==0){
                    Toast.makeText(getApplicationContext(),
                            "هذا الرقم موجود بالفعل , بالرجاء المراجعة ثم التحقق من جديد",
                            Toast.LENGTH_LONG)
                            .show();
                    nc_rep_code_vil_but.setBackgroundColor(Color.RED);
                    nc_rep_code_vil_but.setText("التحقق من صلاحية رقم الإيصال");
                    sentActive=0;

                }
                if(WebServiceResult.ErrorID==010){
                    Toast.makeText(getApplicationContext(),
                            "خطأ في التحقق من رقم الإيصال , الحقل فارغ او اقل من المطلوب",
                            Toast.LENGTH_LONG)
                            .show();
                    nc_rep_code_vil_but.setBackgroundColor(Color.RED);
                    nc_rep_code_vil_but.setText("التحقق من صلاحية رقم الإيصال");
                    sentActive=0;
                }

            }
            catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }





        }

    }
    private class GetReciept_AND_Save_Sup_Fees extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SupOActivity.this);
            pDialog.setMessage("يتم الان التحقق من رقم الإيصال");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {
                WebServiceCall webc =new WebServiceCall(SupOActivity.this);
                if(nc_rep_code.getText().toString().length()>=3) {
                    webc.GetReciept_ver(nc_rep_code.getText().toString());
                }
                else{
                    WebServiceResult.ErrorID=010;
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

                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }


                if(WebServiceResult.ErrorID==4){
                    nc_rep_code_vil_but.setBackgroundColor(Color.GREEN);
                    nc_rep_code_vil_but.setText("تم التحقق");
                    sentActive=1;
                    Toast.makeText(getApplicationContext(),
                            "تم التحقق , رقم الإيصال صحيح",
                            Toast.LENGTH_LONG)
                            .show();
                    new SupOActivity.Save_Sup_Fees().execute();


                }
                if(WebServiceResult.ErrorID==0){
                    Toast.makeText(getApplicationContext(),
                            "رقم الإيصال غير صحيح , بالرجاء المراجعة ثم التحقق من جديد",
                            Toast.LENGTH_LONG)
                            .show();
                    sentActive=0;
                    nc_rep_code.requestFocus();
                    scrollView.scrollTo(0, scrollView.getBottom()+2/2);

                }
                if(WebServiceResult.ErrorID==010){
                    Toast.makeText(getApplicationContext(),
                            "خطأ في التحقق من رقم الإيصال , الحقل فارغ او اقل من المطلوب",
                            Toast.LENGTH_LONG)
                            .show();
                    sentActive=0;

                }

            }
            catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }





        }

    }

    private class GetClientData_Master_Code extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SupOActivity.this);
            pDialog.setMessage("إنتظر قليلا...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {
                Log.e(TAG, "P_Code P_Code: " + P_Code);


                WebServiceCall webc =new WebServiceCall(SupOActivity.this);
                webc.GetClientData_Master_Code(P_Code);
                webc.GetClientDataMore(P_Code);


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


                BranchCodeS="";
                BranchCodeS=WebServiceClientData.motaheda_branch.toString();

                Log.e(TAG, "P_Code P_Code P_Code: " + P_Code.toString());

                doc_nameS=WebServiceClientData.manager_name.toString();
                pharm_nameS="";
                pharm_nameS=WebServiceClientData.phar_name.toString();
                pharm_name.setText("اسم الصيدلية:"+" "+WebServiceClientData.phar_name.toString());
                pharm_code.setText("كود الصيدلية:"+" "+WebServiceClientData.motaheda_code.toString());
                pharm_add.setText("عنوان الصيدلية:"+" "+WebServiceClientData.address.toString());
                pharm_phone_lineS="";
                pharm_phone_lineS=  WebServiceClientData.tel1.toString();
                if(pharm_phone_lineS.length()>1){
                    pharm_phone_line.setText("رقم الهاتف الارضي:"+" "+pharm_phone_lineS);
                }
                if(pharm_phone_lineS.length()<1){
                    pharm_phone_line.setText("رقم الهاتف الارضي:"+" ");
                }
                pharm_phone_mobileS="";
                pharm_phone_mobileS=  WebServiceClientData.tel1.toString();
                if(pharm_phone_mobileS.length()>1){

                    pharm_phone_mobile.setText("رقم الهاتف الارضي:"+" "+pharm_phone_mobileS);
                }
                if(pharm_phone_lineS.length()<1){
                    pharm_phone_mobile.setText("رقم الهاتف الارضي:"+" ");
                }
                pharm_phone_mobile.setText("رقم الهاتف المحمول:"+" "+WebServiceClientData.mob1.toString());
                pharm_doc_name.setText("اسم الصيدلي:"+" "+WebServiceClientData.manager_name.toString());
                pharm_month_valS="";
                pharm_month_valS=WebServiceClientDataMore.monthly_fee.toString();
                pharm_month_val.setText("قيمة الإشتراك:"+" "+WebServiceClientDataMore.monthly_fee.toString());



                // updatePharmCreatData(WebServiceClientData.date_created.toString());
                //nc_running_date.setText(WebServiceClientData.date_created.toString());


            }
            catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }





        }

    }

    private class GetClient_by_DCode extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog


        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {

                WebServiceListHttpHolder sh1 = new WebServiceListHttpHolder();
                WebServiceListHttpHolder sh3 = new WebServiceListHttpHolder();
                WebServiceListHttpHolder sh4 = new WebServiceListHttpHolder();

                String jsonStr1 = sh3.makeServiceCall(servtypeUrl);
                String jsonStr3 = sh3.makeServiceCall(paytypeUrl);
                String jsonStr4 = sh4.makeServiceCall(banknameUrl);



                if (jsonStr3 != null) {
                    try {
                        JSONObject jsonObj1 = new JSONObject(jsonStr1);
                        JSONArray jsonCall1 = jsonObj1.getJSONArray("Table");
                        JSONObject jsonObj3 = new JSONObject(jsonStr3);
                        JSONArray jsonCall3 = jsonObj3.getJSONArray("Table");
                        JSONObject jsonObj4 = new JSONObject(jsonStr4);
                        JSONArray jsonCall4 = jsonObj4.getJSONArray("Table");


                        servtypeSpList.clear();
                        paytypeList.clear();
                        banknameList.clear();


                        for (int i = 0; i < jsonCall1.length(); i++) {
                            JSONObject c1 = jsonCall1.getJSONObject(i);

                            String code = c1.getString("code");
                            String name = c1.getString("name");

                            HashMap<String, String> contact = new HashMap<>();

                            contact.clear();
                            contact.put("code", code);
                            contact.put("name", name);

                            servtypeSpList.add(contact);
                        }
                        for (int iii = 0; iii < jsonCall3.length(); iii++) {
                            JSONObject c3 = jsonCall3.getJSONObject(iii);

                            String code = c3.getString("code");
                            String name = c3.getString("name");

                            HashMap<String, String> contact = new HashMap<>();

                            contact.clear();
                            contact.put("code", code);
                            contact.put("name", name);

                            paytypeList.add(contact);
                        }
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
                    SPsr.removeAllSP(SupOActivity.this);
                    finish();
                    Intent intent = new Intent(SupOActivity.this, LoginActivity.class); intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

                servtypeSp.setAdapter(servtypeSpAdapter);
                paytypeSp.setAdapter(paytypeSAdapter);
                paytypeSp.post(new Runnable() {
                    @Override
                    public void run() {
                        paytypeSp.setSelection(1);
                    }
                });
                banknameSp.setAdapter(banknameSAdapter);

            }
            catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }





        }

    }

    private class Save_Sup_Fees extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SupOActivity.this);
            pDialog.setMessage("جاري الان تحصيل العميل, انتظر قليلا...");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {

                WebServiceCall webc =new WebServiceCall(SupOActivity.this);

                Log.e(TAG, "P_Code:" + P_Code);


                if(P_Code.length()>1){

                    Log.e(TAG, "P_Code P_Code: " + P_Code);

                    PhoneS=pharm_phone_mobileS.toString();

                    //   webc.Update_uni_payed(P_Code,invoice_noS,empsercode);

                    if(is_reused==0){
                        webc.Insert_Reciepts_Data(nc_rep_code.getText().toString(),rec_Date.getText().toString());
                    }



                    if(is_Month==0){
                        if(paytypeSp_position==0||paytypeSp_position==2){
                             webc.Insert_another_fees(P_Code,servtypeCode,empsercode,total_value.getText().toString(),rec_Date_dateAF,nc_rep_code.getText().toString(),con_note.getText().toString(),paytypeCode,chenam.getText().toString(),cheDate_dateAF,banknameCode);

                        }
                        if(paytypeSp_position==1){


                            webc.Insert_another_fees(P_Code,servtypeCode,empsercode,total_value.getText().toString(),rec_Date_dateAF,nc_rep_code.getText().toString(),con_note.getText().toString(),paytypeCode,"","",banknameCode);

                        }
                    }
                    if(is_Month==1){
                        if(paytypeSp_position==0||paytypeSp_position==2) {

                            if(total_value.getText().toString().length()>1) {

                                webc.save_monthly_fees(P_Code, empsercode, nc_rep_code.getText().toString(), total_value.getText().toString(), paytypeCode,yearS,MonthS,banknameCode,"",con_note.getText().toString(),chenam.getText().toString(),cheDate_dateAF,"");

                            }
                        }
                        if(paytypeSp_position==1) {

                            if(total_value.getText().toString().length()>1) {

                                webc.save_monthly_fees(P_Code, empsercode, nc_rep_code.getText().toString(), total_value.getText().toString(), paytypeCode,yearS,MonthS,banknameCode,"",con_note.getText().toString(),"","","");

                            }
                        }
                    }
                    if(WebServiceResult.ErrorID==0){
                        webc.Insert_Emp_Daily_Report(nc_rep_code.getText().toString(),P_Code,doc_nameS.toString(),con_note.getText().toString(),pharm_nameS,PhoneS,BranchCodeS);

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



                    if(is_reused==1){
                        Toast.makeText(getApplicationContext(),
                                "تمت العملية بنجاح ويمكنك استخدام رقم الإيصال مرة اخرى",
                                Toast.LENGTH_LONG)
                                .show();
                        finish();
                        startActivity(getIntent());

                    }
                    if(is_reused==0){
                        Toast.makeText(getApplicationContext(),
                                "تمت العملية ولا يمكنك استخدام رقم الإيصال مرة اخرى",
                                Toast.LENGTH_LONG)
                                .show();
                        finish();
                        startActivity(getIntent());
                    }


                }
                if(WebServiceResult.ErrorID==1){

                    Toast.makeText(getApplicationContext(),
                            "حدث خطأ",
                            Toast.LENGTH_LONG)
                            .show();
                    Log.e(TAG, "Save_Sup_Fees:" + WebServiceResult.ErrorMessage.toString());
                    finish();

                }




            }
            catch (Exception ex) {


                Log.e(TAG, "Save_Sup_Fees: " + ex.getMessage());

            }





        }

    }





    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nc_rep_code.getWindowToken(), 0);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //if (drawer.isDrawerOpen(GravityCompat.START)) {
        // drawer.closeDrawer(GravityCompat.START);
        // } else {
        super.onBackPressed();
        // }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }
}
