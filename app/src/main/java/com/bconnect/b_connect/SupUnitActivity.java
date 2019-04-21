package com.bconnect.b_connect;

import android.annotation.SuppressLint;
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
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class SupUnitActivity extends AppCompatActivity {

    private String TAG = "SR: " + OrderActivity.class.getSimpleName() + ": ";
    private int sentActive, is_reused;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String empsercode, supername;

    private ProgressDialog pDialog;


    TextView pharm_name, pharm_code, pharm_add, pharm_phone_line, pharm_phone_mobile, pharm_doc_name;
    TextView uni_dit_uni_no, uni_dit_uni_total_val, banknameTit;
    TextInputLayout cheDateT;

    String emp_name, pharm_nameS, BranchCodeS, invoice_noS;
    String banknameCode, paytypeCode, P_Code, doc_nameS;
    String rec_Date_dateAF, cheDate_dateAF, PharCode, real_Date, real_Date2, pharm_phone_lineS, pharm_phone_mobileS, PhoneS;


    EditText nc_rep_code, rec_Date, cheDate, chenam, con_note;

    EditText total_value;


    Intent intent;


    int paytypeSp_position;

    SimpleDateFormat rec_Date_SDF, cheDate_SDF;

    Button nc_sendbut, nc_rep_code_vil_but;

    Switch sup_sw_rec_code_reuse;

    private static String paytypeUrl, banknameUrl;

    Spinner paytypeSp, banknameSp;


    List<HashMap<String, String>> paytypeList, banknameList;
    private SimpleAdapter paytypeSAdapter, banknameSAdapter;

    Calendar rec_Date_myCalendar, cheDate_myCalendar, real_Date_myCalendar;

    NestedScrollView scrollView;

    SharedPreferencesSR SPsr = new SharedPreferencesSR();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_unit);


        basic();
        control();


        new SupUnitActivity.GetClientData_Master_Code().execute();
        new SupUnitActivity.GetClient_by_DCode().execute();


    }


    public void basic() {

        scrollView = (NestedScrollView) findViewById(R.id.SupUni_NS);

        pharm_name = (TextView) findViewById(R.id.pharm_name);
        pharm_code = (TextView) findViewById(R.id.pharm_code);
        pharm_add = (TextView) findViewById(R.id.pharm_add);
        pharm_phone_line = (TextView) findViewById(R.id.pharm_phone_line);
        pharm_phone_mobile = (TextView) findViewById(R.id.pharm_phone_mobile);
        pharm_doc_name = (TextView) findViewById(R.id.pharm_doc_name);
        banknameTit = (TextView) findViewById(R.id.banknameTit);
        uni_dit_uni_no = (TextView) findViewById(R.id.uni_dit_uni_no);
        uni_dit_uni_total_val = (TextView) findViewById(R.id.uni_dit_uni_total_val);

        cheDateT = (TextInputLayout) findViewById(R.id.cheDateT);


        nc_sendbut = (Button) findViewById(R.id.nc_sendbut);
        nc_rep_code_vil_but = (Button) findViewById(R.id.nc_rep_code_vil_but);

        sup_sw_rec_code_reuse = (Switch) findViewById(R.id.sup_sw_rec_code_reuse);


        total_value = (EditText) findViewById(R.id.total_value);
        nc_rep_code = (EditText) findViewById(R.id.nc_rep_code);
        rec_Date = (EditText) findViewById(R.id.rec_Date);
        cheDate = (EditText) findViewById(R.id.cheDate);
        chenam = (EditText) findViewById(R.id.chenam);
        con_note = (EditText) findViewById(R.id.con_note);


        intent = getIntent();

        PharCode = "";
        PharCode = intent.getExtras().getString("PharCode");

        Log.e(TAG, "PharCode PharCode: " + PharCode);

        paytypeUrl = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/Android.asmx/GetPayWay";
        banknameUrl = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/Android.asmx/GetBank";


        paytypeSp = (Spinner) findViewById(R.id.paytypeSp);
        banknameSp = (Spinner) findViewById(R.id.banknameSp);


        paytypeList = new ArrayList<HashMap<String, String>>();
        banknameList = new ArrayList<HashMap<String, String>>();

        paytypeSAdapter = new SimpleAdapter(this, paytypeList, R.layout.pay_way_spinner_item,
                new String[]{"name"}, new int[]{R.id.tvName});
        banknameSAdapter = new SimpleAdapter(this, banknameList, R.layout.bank_name_spinner_item,
                new String[]{"name"}, new int[]{R.id.tvName});


        real_Date_myCalendar = Calendar.getInstance();
        rec_Date_myCalendar = Calendar.getInstance();
        cheDate_myCalendar = Calendar.getInstance();

        SimpleDateFormat real_DateSDF2 = new SimpleDateFormat("yyyy/dd/MM");
        SimpleDateFormat real_DateSDF = new SimpleDateFormat("yyyy/MM/dd");
        real_Date = real_DateSDF2.format(real_Date_myCalendar.getTime()).toString();
        real_Date2 = real_DateSDF.format(real_Date_myCalendar.getTime()).toString();
        rec_Date_updateData();
        cheDate_updateData();

        sentActive = 0;

        pref = getApplicationContext().getSharedPreferences("MySr", MODE_PRIVATE);
        editor = pref.edit();


        empsercode = "";
        empsercode = SPsr.getSP(getApplicationContext(), "empsercode");
        emp_name = "";
        emp_name = SPsr.getSP(getApplicationContext(), "empsername");
        P_Code = "";
        doc_nameS = "";


    }

    @SuppressLint("ClickableViewAccessibility")
    public void control() {

        total_value.setEnabled(false);

        sup_sw_rec_code_reuse.setChecked(false);


        sup_sw_rec_code_reuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sup_sw_rec_code_reuse.isChecked()) {
                    is_reused = 1;
                    sup_sw_rec_code_reuse.setTextColor(Color.BLUE);
                } else {
                    is_reused = 0;
                    sup_sw_rec_code_reuse.setTextColor(Color.RED);


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
                new DatePickerDialog(SupUnitActivity.this, rec_Date_datepicker,
                        rec_Date_myCalendar.get(Calendar.YEAR),
                        rec_Date_myCalendar.get(Calendar.MONTH),
                        rec_Date_myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
        cheDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SupUnitActivity.this, cheDate_datepicker,
                        cheDate_myCalendar.get(Calendar.YEAR),
                        cheDate_myCalendar.get(Calendar.MONTH),
                        cheDate_myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });


        paytypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {

                HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getAdapter().getItem(position);
                paytypeCode = (String) obj.get("code");
                paytypeSp_position = position;
                if (position == 0) {
                    Toast.makeText(SupUnitActivity.this, "تحصيل بشيك ", Toast.LENGTH_SHORT).show();

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
                if (position == 1) {


                    Toast.makeText(SupUnitActivity.this, "تحصيل نقدي", Toast.LENGTH_SHORT).show();

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
                if (position == 2) {
                    Toast.makeText(SupUnitActivity.this, "تحصيل بحوالة بنكية", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(SupUnitActivity.this, "تذكر ان تختار نوع التحصيل ", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(SupUnitActivity.this, "تذكر ان تختار نوع التحصيل ", Toast.LENGTH_SHORT).show();

            }
        });

        nc_rep_code_vil_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SupUnitActivity.GetReciept_ver().execute();
            }
        });


        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


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

                AlertDialog.Builder builder1 = new AlertDialog.Builder(SupUnitActivity.this);
                if (sentActive == 0) {

                    builder1.setMessage("سيتم الان التحقق من رقم الإيصال: "
                            + nc_rep_code.getText().toString() + "\n" +
                            "المبلغ: "
                            + total_value.getText().toString() + "\n"

                            + "\nتأكيد التحصيل؟");

                }
                if (sentActive == 1) {

                    builder1.setMessage(
                            "المبلغ: "
                                    + total_value.getText().toString() + "\n"

                                    + "\nتأكيد التحصيل؟");

                }
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "تأكيد",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                if (sentActive == 0) {

                                    new SupUnitActivity.GetReciept_AND_Save_Unit_Another_Fees().execute();
                                }
                                if (sentActive == 1) {
                                    new SupUnitActivity.Save_Unit_Another_Fees().execute();

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
        });


    }


    private void rec_Date_updateData() {

        try {

            rec_Date_SDF = new SimpleDateFormat("yyyy/MM/dd");
            rec_Date_dateAF = rec_Date_SDF.format(rec_Date_myCalendar.getTime()).toString();
            SimpleDateFormat rec_Date_SDF2 = new SimpleDateFormat("yyyy/MM/dd");
            String rec_Date_dateAF2 = rec_Date_SDF2.format(rec_Date_myCalendar.getTime()).toString();
            rec_Date.setText(rec_Date_dateAF2);

        } catch (Exception e) {
            //The handling for the code
        }

    }

    private void cheDate_updateData() {

        try {

            cheDate_SDF = new SimpleDateFormat("dd/MM/yyyy");
            cheDate_dateAF = cheDate_SDF.format(cheDate_myCalendar.getTime()).toString();
            SimpleDateFormat cheDate_SDF2 = new SimpleDateFormat("yyyy/MM/dd");
            String cheDate_dateAF2 = cheDate_SDF2.format(cheDate_myCalendar.getTime()).toString();
            cheDate.setText(cheDate_dateAF2);
        } catch (Exception e) {
            //The handling for the code
        }

    }


    private class GetReciept_ver extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SupUnitActivity.this);
            pDialog.setMessage("يتم الان التحقق من رقم الإيصال");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                WebServiceCall webc = new WebServiceCall(SupUnitActivity.this);
                if (nc_rep_code.getText().toString().length() >= 3) {
                    webc.GetReciept_ver(nc_rep_code.getText().toString());
                } else {
                    WebServiceResult.ErrorID = 010;
                }

            } catch (Exception ex) {
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


            try {

                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }


                if (WebServiceResult.ErrorID == 4) {
                    nc_rep_code_vil_but.setBackgroundColor(Color.GREEN);
                    nc_rep_code_vil_but.setText("تم التحقق");
                    sentActive = 1;
                    Toast.makeText(getApplicationContext(),
                            "تم التحقق من صلاحية رقم الإيصال , يمكن الاستمرار الان",
                            Toast.LENGTH_LONG)
                            .show();
                }
                if (WebServiceResult.ErrorID == 0) {
                    Toast.makeText(getApplicationContext(),
                            "هذا الرقم موجود بالفعل , بالرجاء المراجعة ثم التحقق من جديد",
                            Toast.LENGTH_LONG)
                            .show();
                    sentActive = 0;

                }
                if (WebServiceResult.ErrorID == 010) {
                    Toast.makeText(getApplicationContext(),
                            "خطأ في التحقق من رقم الإيصال , الحقل فارغ او اقل من المطلوب",
                            Toast.LENGTH_LONG)
                            .show();
                    sentActive = 0;

                }

            } catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }


        }

    }

    private class GetReciept_AND_Save_Unit_Another_Fees extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SupUnitActivity.this);
            pDialog.setMessage("يتم الان التحقق من رقم الإيصال");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                WebServiceCall webc = new WebServiceCall(SupUnitActivity.this);
                if (nc_rep_code.getText().toString().length() >= 3) {
                    webc.GetReciept_ver(nc_rep_code.getText().toString());
                } else {
                    WebServiceResult.ErrorID = 010;
                }


            } catch (Exception ex) {
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


            try {

                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }


                if (WebServiceResult.ErrorID == 4) {
                    nc_rep_code_vil_but.setBackgroundColor(Color.GREEN);
                    nc_rep_code_vil_but.setText("تم التحقق");
                    sentActive = 1;
                    Toast.makeText(getApplicationContext(),
                            "تم التحقق , رقم الإيصال صحيح",
                            Toast.LENGTH_LONG)
                            .show();
                    new SupUnitActivity.Save_Unit_Another_Fees().execute();


                }
                if (WebServiceResult.ErrorID == 0) {
                    Toast.makeText(getApplicationContext(),
                            "رقم الإيصال غير صحيح , بالرجاء المراجعة ثم التحقق من جديد",
                            Toast.LENGTH_LONG)
                            .show();
                    sentActive = 0;
                    nc_rep_code.requestFocus();
                    scrollView.scrollTo(0, scrollView.getBottom() + 2 / 2);

                }
                if (WebServiceResult.ErrorID == 010) {
                    Toast.makeText(getApplicationContext(),
                            "خطأ في التحقق من رقم الإيصال , الحقل فارغ او اقل من المطلوب",
                            Toast.LENGTH_LONG)
                            .show();
                    sentActive = 0;

                }

            } catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }


        }

    }

    private class GetClientData_Master_Code extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SupUnitActivity.this);
            pDialog.setMessage("إنتظر قليلا...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                WebServiceCall webc = new WebServiceCall(SupUnitActivity.this);
                webc.GetClientData_Master_Code(PharCode);
                webc.Get_invoice_data(PharCode, empsercode);


            } catch (Exception ex) {
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


            try {


                BranchCodeS = "";
                BranchCodeS = WebServiceClientData.motaheda_branch.toString();

                Log.e(TAG, "P_Code P_Code P_Code: " + P_Code.toString());

                doc_nameS = WebServiceClientData.manager_name.toString();
                pharm_nameS = "";
                pharm_nameS = WebServiceClientData.phar_name.toString();
                pharm_name.setText("اسم الصيدلية:" + " " + WebServiceClientData.phar_name.toString());
                pharm_code.setText("كود الصيدلية:" + " " + WebServiceClientData.motaheda_code.toString());
                pharm_add.setText("عنوان الصيدلية:" + " " + WebServiceClientData.address.toString());
                pharm_phone_lineS = "";
                pharm_phone_lineS = WebServiceClientData.tel1.toString();
                if (pharm_phone_lineS.length() > 1) {
                    pharm_phone_line.setText("رقم الهاتف الارضي:" + " " + pharm_phone_lineS);
                }
                if (pharm_phone_lineS.length() < 1) {
                    pharm_phone_line.setText("رقم الهاتف الارضي:" + " ");
                }
                pharm_phone_mobileS = "";
                pharm_phone_mobileS = WebServiceClientData.tel1.toString();
                if (pharm_phone_mobileS.length() > 1) {

                    pharm_phone_mobile.setText("رقم الهاتف الارضي:" + " " + pharm_phone_mobileS);
                }
                if (pharm_phone_lineS.length() < 1) {
                    pharm_phone_mobile.setText("رقم الهاتف الارضي:" + " ");
                }
                pharm_phone_mobile.setText("رقم الهاتف المحمول:" + " " + WebServiceClientData.mob1.toString());
                pharm_doc_name.setText("اسم الصيدلي:" + " " + WebServiceClientData.manager_name.toString());

                uni_dit_uni_no.setText("اجمالي عدد الوحدات:" + " " + WebServiceUniData.uni_num.toString());
                uni_dit_uni_total_val.setText("اجمالي قيمة الوحدات:" + " " + WebServiceUniData.adds_total_price.toString());
                total_value.setText(WebServiceUniData.adds_total_price.toString());
                invoice_noS = "";
                invoice_noS = WebServiceUniData.invoice_no.toString();

                // updatePharmCreatData(WebServiceClientData.date_created.toString());
                //nc_running_date.setText(WebServiceClientData.date_created.toString());


            } catch (Exception ex) {


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

            try {

                WebServiceListHttpHolder sh3 = new WebServiceListHttpHolder();
                WebServiceListHttpHolder sh4 = new WebServiceListHttpHolder();

                String jsonStr3 = sh3.makeServiceCall(paytypeUrl);
                String jsonStr4 = sh4.makeServiceCall(banknameUrl);


                if (jsonStr3 != null) {
                    try {
                        JSONObject jsonObj3 = new JSONObject(jsonStr3);
                        JSONArray jsonCall3 = jsonObj3.getJSONArray("Table");
                        JSONObject jsonObj4 = new JSONObject(jsonStr4);
                        JSONArray jsonCall4 = jsonObj4.getJSONArray("Table");


                        paytypeList.clear();
                        banknameList.clear();


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
                    SPsr.removeAllSP(SupUnitActivity.this);
                    finish();
                    Intent intent = new Intent(SupUnitActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            } catch (Exception ex) {
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


            try {

                // Dismiss the progress dialog
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }

                paytypeSp.setAdapter(paytypeSAdapter);
                paytypeSp.post(new Runnable() {
                    @Override
                    public void run() {
                        paytypeSp.setSelection(1);
                    }
                });
                banknameSp.setAdapter(banknameSAdapter);

            } catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }


        }

    }

    private class Save_Unit_Another_Fees extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SupUnitActivity.this);
            pDialog.setMessage("جاري الان تحصيل الوحدة الإضافية");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                WebServiceCall webc = new WebServiceCall(SupUnitActivity.this);

                Log.e(TAG, "PharCode PharCode: " + PharCode);


                if (PharCode.length() > 1) {

                    Log.e(TAG, "PharCode PharCode: " + PharCode);

                    if (paytypeSp_position == 0 || paytypeSp_position == 2) {
                        webc.save_pahr_another_fees_add(PharCode, empsercode, total_value.getText().toString(), rec_Date_dateAF, nc_rep_code.getText().toString(), con_note.getText().toString(), paytypeCode, chenam.getText().toString(), cheDate_dateAF, "", invoice_noS);

                    }
                    if (paytypeSp_position == 1) {
                        webc.save_pahr_another_fees_add(PharCode, empsercode, total_value.getText().toString(), rec_Date_dateAF, nc_rep_code.getText().toString(), con_note.getText().toString(), paytypeCode, "", "", "", invoice_noS);

                    }
                    if (WebServiceResult.ErrorID == 0) {
                        PhoneS = pharm_phone_mobileS.toString();

                        webc.Update_uni_payed(PharCode, invoice_noS, empsercode);
                        webc.Insert_Emp_Daily_Report(nc_rep_code.getText().toString(), PharCode, doc_nameS.toString(), con_note.getText().toString(), pharm_nameS, PhoneS, BranchCodeS);
                        if (is_reused == 0) {
                            webc.Insert_Reciepts_Data(nc_rep_code.getText().toString(), rec_Date.getText().toString());
                        }

                    }

                }
            } catch (Exception ex) {
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


            try {

                // Dismiss the progress dialog
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }

                if (WebServiceResult.ErrorID == 0) {

                    Toast.makeText(getApplicationContext(),
                            "تمت العملية بنجاح",
                            Toast.LENGTH_LONG)
                             .show();

                    finish();

                }
                if (WebServiceResult.ErrorID == 1) {

                    Toast.makeText(getApplicationContext(),
                            "حدث خطأ",
                            Toast.LENGTH_LONG)
                            .show();
                    Log.e(TAG, "Save_Unit_Another_Fees: " + WebServiceResult.ErrorMessage.toString());

                    finish();

                }


                Log.e(TAG, "supername supername: " + supername);

            } catch (Exception ex) {


                Log.e(TAG, "New_Client onPostExecute Error: " + ex.getMessage());

            }


        }

    }


    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nc_rep_code.getWindowToken(), 0);
    }
}
