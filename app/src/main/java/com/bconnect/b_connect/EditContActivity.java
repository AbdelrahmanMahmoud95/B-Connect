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
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class EditContActivity extends AppCompatActivity {


    SharedPreferencesSR SPsr = new SharedPreferencesSR();


    private String TAG = "SR: " + ClientByBranchActivity.class.getSimpleName() + ": ";
    private int sentActive, is_dit_ok, is_run_ok, is_pay_ok, is_edit_con, is_runing, is_run_date_ok;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String asm, ser, empsercode, supername;


    //Definitions
    private ProgressDialog pDialog;
    TextView pharm_name, pharm_code, pharm_add, pharm_phone_line, pharm_phone_mobile, pharm_doc_name;
    TextView con_month_val, con_val, con_remaning_val, con_down_val, con_type, banknameTit, cheDateTit, nc_emp_by_super_tit, textView4;
    TextView textView5, textView8;
    TextInputLayout nc_con_valueT, nc_join_valueT, nc_pay_downT, nc_pay_upT, nc_running_valueT, nc_rest_valueT, cheDateT;


    EditText nc_running_date, nc_rep_code, rec_Date, cheDate, chenam, Mster_name, con_note;
    EditText nc_con_value, nc_join_value, nc_running_value, nc_pay_up, nc_pay_down, nc_rest_value;

    String MotahedaCodeToActivity, nc_running_dateAF, rec_Date_dateAF, cheDate_dateAF, PharCode, real_Date, real_Date2, pharm_phone_lineS, pharm_phone_mobileS, PhoneS;
    String contractTypeCode, phar_typeCode, banknameCode, paytypeCode, P_Code, doc_nameS, nc_con_value_S, nc_join_value_S;
    String emp_name, remaining, pharm_nameS, BranchCodeS, phar_nameToActivity;


    int pay_down_INT, des_INT, join_value_INT, nc_rest_value_INT, nc_pay_up_INT, paytypeSp_position, phar_typePosition;

    SimpleDateFormat nc_running_SDF, rec_Date_SDF, cheDate_SDF;

    Button nc_sendbut, nc_rep_code_vil_but;

    Switch emp_by_super_switch, ec_con_type_sw, nc_running_value_sw;

    Intent intent;

    private static String ContractTypeUrl, PharTypeUrl, paytypeUrl, banknameUrl, emp_by_superUrl;

    Spinner nc_con_type, nc_phar_type, paytypeSp, banknameSp, nc_emp_by_super;


    List<HashMap<String, String>> ContractTypeList, PharmTypeList, paytypeList, banknameList, emp_by_super_list;
    private SimpleAdapter ContractTypeSAdapter, PharmTypeSAdapter, paytypeSAdapter, banknameSAdapter, emp_by_super_SAdapter;

    Calendar nc_running_myCalendar, rec_Date_myCalendar, cheDate_myCalendar, real_Date_myCalendar, real_Date_Calendar_Max, real_Date_Calendar_Min;

    Date minDate, maxDate;

    NestedScrollView scrollView;

    Snackbar snackbar, snackbarRun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cont);

        basic();
        control();

        new EditContActivity.GetClient_by_DCode().execute();

    }


    public void basic() {

        scrollView = (NestedScrollView) findViewById(R.id.eCon_NestedScrollView);

        pharm_name = (TextView) findViewById(R.id.pharm_name);
        pharm_code = (TextView) findViewById(R.id.pharm_code);
        pharm_add = (TextView) findViewById(R.id.pharm_add);
        pharm_phone_line = (TextView) findViewById(R.id.pharm_phone_line);
        pharm_phone_mobile = (TextView) findViewById(R.id.pharm_phone_mobile);
        pharm_doc_name = (TextView) findViewById(R.id.pharm_doc_name);
        con_month_val = (TextView) findViewById(R.id.con_month_val);
        con_val = (TextView) findViewById(R.id.con_val);
        //con_remaning_val=(TextView)findViewById(R.id.con_remaning_val);
        con_down_val = (TextView) findViewById(R.id.con_down_val);
        con_type = (TextView) findViewById(R.id.con_type);
        banknameTit = (TextView) findViewById(R.id.banknameTit);
        // nc_emp_by_super_tit=(TextView)findViewById(R.id.nc_emp_by_super_tit);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView8 = (TextView) findViewById(R.id.textView8);

        nc_con_valueT = (TextInputLayout) findViewById(R.id.nc_con_valueT);
        nc_join_valueT = (TextInputLayout) findViewById(R.id.nc_join_valueT);
        nc_pay_downT = (TextInputLayout) findViewById(R.id.nc_pay_downT);
        nc_pay_upT = (TextInputLayout) findViewById(R.id.nc_pay_upT);
        nc_running_valueT = (TextInputLayout) findViewById(R.id.nc_running_valueT);
        nc_rest_valueT = (TextInputLayout) findViewById(R.id.nc_rest_valueT);
        cheDateT = (TextInputLayout) findViewById(R.id.cheDateT);

        nc_running_date = (EditText) findViewById(R.id.nc_running_date);
        nc_rep_code = (EditText) findViewById(R.id.nc_rep_code);
        rec_Date = (EditText) findViewById(R.id.rec_Date);
        cheDate = (EditText) findViewById(R.id.cheDate);
        chenam = (EditText) findViewById(R.id.chenam);
        Mster_name = (EditText) findViewById(R.id.Mster_name);
        con_note = (EditText) findViewById(R.id.con_note);
        nc_con_value = (EditText) findViewById(R.id.nc_con_value);
        nc_join_value = (EditText) findViewById(R.id.nc_join_value);
        nc_pay_up = (EditText) findViewById(R.id.nc_pay_up);
        nc_pay_down = (EditText) findViewById(R.id.nc_pay_down);
        // nc_des=(EditText) findViewById(R.id.nc_des);
        nc_rest_value = (EditText) findViewById(R.id.nc_rest_value);
        nc_running_value = (EditText) findViewById(R.id.nc_running_value);


        nc_sendbut = (Button) findViewById(R.id.nc_sendbut);
        nc_rep_code_vil_but = (Button) findViewById(R.id.nc_rep_code_vil_but);

        // emp_by_super_switch=(Switch) findViewById(R.id.emp_by_super_switch);
        ec_con_type_sw = (Switch) findViewById(R.id.ec_con_type_sw);
        nc_running_value_sw = (Switch) findViewById(R.id.nc_running_value_sw);

        nc_con_type = (Spinner) findViewById(R.id.nc_con_type);
        nc_phar_type = (Spinner) findViewById(R.id.nc_phar_type);
        paytypeSp = (Spinner) findViewById(R.id.paytypeSp);
        banknameSp = (Spinner) findViewById(R.id.banknameSp);
        // nc_emp_by_super= (Spinner) findViewById(R.id.nc_emp_by_super);


        nc_join_value.setEnabled(false);
        try {

            intent = getIntent();
            MotahedaCodeToActivity = intent.getExtras().getString("MotahedaCodeToActivity");
            P_Code = "";
            P_Code = intent.getExtras().getString("PharCode");

        } catch (Exception e) {

            super.onBackPressed();
        }


        Log.e("SR", "P_Code :" + P_Code);
        Log.e("SR", "MotahedaCodeToActivity :" + MotahedaCodeToActivity);


        sentActive = 0;

        pref = getApplicationContext().getSharedPreferences("MySr", MODE_PRIVATE);
        editor = pref.edit();

        snackbar = Snackbar.make(findViewById(android.R.id.content), "يجب ان لا تكون الدفعة المقدمة اكبر من رسوم الإنضمام", Snackbar.LENGTH_LONG);
        snackbarRun = Snackbar.make(findViewById(android.R.id.content), "مصاريف التشغيل فارغة او اقل من المطلوب", Snackbar.LENGTH_LONG);


        ContractTypeList = new ArrayList<HashMap<String, String>>();
        PharmTypeList = new ArrayList<HashMap<String, String>>();
        paytypeList = new ArrayList<HashMap<String, String>>();
        banknameList = new ArrayList<HashMap<String, String>>();
        emp_by_super_list = new ArrayList<HashMap<String, String>>();

        // ContractTypeAdapter=new SimpleAdapter(EditContActivity.this, android.R.layout.simple_spinner_dropdown_item, branchlist);
        ContractTypeSAdapter = new SimpleAdapter(this, ContractTypeList, R.layout.cont_type_spinner_item,
                new String[]{"name"}, new int[]{R.id.tvName});
        PharmTypeSAdapter = new SimpleAdapter(this, PharmTypeList, R.layout.pharm_type_spinner_item,
                new String[]{"name"}, new int[]{R.id.tvName});
        paytypeSAdapter = new SimpleAdapter(this, paytypeList, R.layout.pay_way_spinner_item,
                new String[]{"name"}, new int[]{R.id.tvName});
        banknameSAdapter = new SimpleAdapter(this, banknameList, R.layout.bank_name_spinner_item,
                new String[]{"name"}, new int[]{R.id.tvName});
        emp_by_super_SAdapter = new SimpleAdapter(this, emp_by_super_list, R.layout.emp_by_sup_spinner_item,
                new String[]{"name"}, new int[]{R.id.tvName});

        ContractTypeUrl = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/Android.asmx/GetContractType";
        PharTypeUrl = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/Android.asmx/GetPharType";
        paytypeUrl = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/Android.asmx/GetPayWay";
        banknameUrl = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/Android.asmx/GetBank";
        emp_by_superUrl = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/Android.asmx/Get_Emp_by_Super?emp_code=";


        empsercode = "";
        empsercode = SPsr.getSP(getApplicationContext(), "empsercode");
        emp_name = "";
        emp_name = SPsr.getSP(getApplicationContext(), "empsername");

        doc_nameS = "";

        nc_pay_down.setEnabled(false);


        is_dit_ok = 0;
        is_edit_con = 0;
        is_runing = 1;


        nc_running_myCalendar = Calendar.getInstance();
        rec_Date_myCalendar = Calendar.getInstance();
        cheDate_myCalendar = Calendar.getInstance();
        real_Date_myCalendar = Calendar.getInstance();
        real_Date_Calendar_Min = Calendar.getInstance();
        real_Date_Calendar_Max = Calendar.getInstance();


        SimpleDateFormat real_DateSDF2 = new SimpleDateFormat("yyyy/dd/MM");
        nc_running_dateAF = real_DateSDF2.format(nc_running_myCalendar.getTime()).toString();
        SimpleDateFormat real_DateSDF = new SimpleDateFormat("yyyy/MM/dd");
        real_Date = real_DateSDF2.format(real_Date_myCalendar.getTime()).toString();
        real_Date2 = real_DateSDF.format(real_Date_myCalendar.getTime()).toString();
        nc_running_updateData();
        rec_Date_updateData();
        cheDate_updateData();

        real_Date_Calendar_Min.add(Calendar.DAY_OF_MONTH, (-1));
        real_Date_Calendar_Max.add(Calendar.MONTH, (6));

        Log.e("SR", "max :" + real_Date_Calendar_Max.getTime().toString());

        minDate = real_Date_Calendar_Min.getTime();
        maxDate = real_Date_Calendar_Max.getTime();


    }

    public void control() {


        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (is_edit_con == 1) {
                    if (nc_pay_up_INT > (join_value_INT - pay_down_INT)) {


                        //   Toast.makeText(NewConActivity.this, "يجب ان لا تكون الدفعة المقدمة اكبر من رسوم الإنضمام" , Toast.LENGTH_SHORT).show();

                        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();

                        nc_pay_up.setTextColor(Color.RED);

                        is_pay_ok = 0;

                    }
                    if (phar_typePosition == 1) {
                        if (Mster_name.getText().length() > 1) {
                            is_dit_ok = 1;
                        }
                        if (Mster_name.getText().length() < 1) {
                            is_dit_ok = 0;
                        }
                    }
                    if (phar_typePosition != 1) {
                        is_dit_ok = 1;
                    }

                } else {
                    snackbar.dismiss();
                    is_pay_ok = 1;
                    is_dit_ok = 1;

                }
                if (is_runing == 1) {
                    if (nc_running_value.getText().length() <= 1) {

                        snackbarRun.setDuration(Snackbar.LENGTH_INDEFINITE);
                        snackbarRun.show();
                        nc_running_value.setTextColor(Color.RED);
                        is_run_ok = 0;
                    }
                } else {
                    snackbarRun.dismiss();
                    is_run_ok = 1;
                }

                if (dateMaxMin(nc_running_myCalendar.getTime(), minDate, maxDate) == true) {


                    nc_running_updateData();
                    nc_running_date.setTextColor(Color.BLACK);
                    is_run_date_ok = 1;

                } else {
                    nc_running_date.setTextColor(Color.RED);
                    is_run_date_ok = 0;
                }

                closeKeyboard();
                return false;

            }
        });


        if (empsercode != null || empsercode != "" || empsercode.length() > 0) {
            emp_by_superUrl = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/Android.asmx/Get_Emp_by_Super?emp_code=" + empsercode.toString();

        }
//        if(emp_by_super_switch.isChecked()){
//            nc_emp_by_super.setEnabled(true);
//            nc_emp_by_super.setVisibility(View.VISIBLE);
//            nc_emp_by_super_tit.setEnabled(true);
//            nc_emp_by_super_tit.setVisibility(View.VISIBLE);
//        }
//        if(!emp_by_super_switch.isChecked()) {
//            nc_emp_by_super.setEnabled(false);
//            nc_emp_by_super.setVisibility(View.GONE);
//            nc_emp_by_super_tit.setEnabled(false);
//            nc_emp_by_super_tit.setVisibility(View.GONE);
//        }
//
//        emp_by_super_switch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(emp_by_super_switch.isChecked()){
//                    nc_emp_by_super.setEnabled(true);
//                    nc_emp_by_super.setVisibility(View.VISIBLE);
//                    nc_emp_by_super_tit.setEnabled(true);
//                    nc_emp_by_super_tit.setVisibility(View.VISIBLE);
//                }
//                else {
//                    nc_emp_by_super.setEnabled(false);
//                    nc_emp_by_super.setVisibility(View.GONE);
//                    nc_emp_by_super_tit.setEnabled(false);
//                    nc_emp_by_super_tit.setVisibility(View.GONE);
//                }
//            }
//        });


        if (ec_con_type_sw.isChecked()) {
            textView4.setVisibility(View.VISIBLE);
            nc_con_type.setVisibility(View.VISIBLE);
            textView8.setVisibility(View.VISIBLE);
            nc_phar_type.setVisibility(View.VISIBLE);
            textView5.setVisibility(View.VISIBLE);
            nc_con_valueT.setVisibility(View.VISIBLE);
            nc_con_value.setVisibility(View.VISIBLE);
            nc_join_valueT.setVisibility(View.VISIBLE);
            nc_join_value.setVisibility(View.VISIBLE);
            nc_pay_downT.setVisibility(View.VISIBLE);
            nc_pay_down.setVisibility(View.VISIBLE);
            nc_pay_upT.setVisibility(View.VISIBLE);
            nc_pay_up.setVisibility(View.VISIBLE);
            nc_rest_value.setVisibility(View.VISIBLE);
            nc_rest_valueT.setVisibility(View.VISIBLE);
            is_edit_con = 1;


        } else {
            textView4.setVisibility(View.GONE);
            nc_con_type.setVisibility(View.GONE);
            textView8.setVisibility(View.GONE);
            nc_phar_type.setVisibility(View.GONE);
            textView5.setVisibility(View.GONE);
            nc_con_valueT.setVisibility(View.GONE);
            nc_con_value.setVisibility(View.GONE);
            nc_join_valueT.setVisibility(View.GONE);
            nc_join_value.setVisibility(View.GONE);
            nc_pay_downT.setVisibility(View.GONE);
            nc_pay_down.setVisibility(View.GONE);
            nc_pay_upT.setVisibility(View.GONE);
            nc_pay_up.setVisibility(View.GONE);
            Mster_name.setVisibility(View.GONE);
            nc_rest_value.setVisibility(View.GONE);
            nc_rest_valueT.setVisibility(View.GONE);
            is_edit_con = 0;
            snackbar.dismiss();

        }
        ec_con_type_sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ec_con_type_sw.isChecked()) {
                    textView4.setVisibility(View.VISIBLE);
                    nc_con_type.setVisibility(View.VISIBLE);
                    textView8.setVisibility(View.VISIBLE);
                    nc_phar_type.setVisibility(View.VISIBLE);
                    textView5.setVisibility(View.VISIBLE);
                    nc_con_valueT.setVisibility(View.VISIBLE);
                    nc_con_value.setVisibility(View.VISIBLE);
                    nc_join_valueT.setVisibility(View.VISIBLE);
                    nc_join_value.setVisibility(View.VISIBLE);
                    nc_pay_downT.setVisibility(View.VISIBLE);
                    nc_pay_down.setVisibility(View.VISIBLE);
                    nc_pay_upT.setVisibility(View.VISIBLE);
                    nc_pay_up.setVisibility(View.VISIBLE);
                    nc_rest_value.setVisibility(View.VISIBLE);
                    nc_rest_valueT.setVisibility(View.VISIBLE);
                    is_edit_con = 1;

                } else {
                    textView4.setVisibility(View.GONE);
                    nc_con_type.setVisibility(View.GONE);
                    textView8.setVisibility(View.GONE);
                    nc_phar_type.setVisibility(View.GONE);
                    textView5.setVisibility(View.GONE);
                    nc_con_valueT.setVisibility(View.GONE);
                    nc_con_value.setVisibility(View.GONE);
                    nc_join_valueT.setVisibility(View.GONE);
                    nc_join_value.setVisibility(View.GONE);
                    nc_pay_downT.setVisibility(View.GONE);
                    nc_pay_down.setVisibility(View.GONE);
                    nc_pay_upT.setVisibility(View.GONE);
                    nc_pay_up.setVisibility(View.GONE);
                    Mster_name.setVisibility(View.GONE);
                    nc_rest_value.setVisibility(View.GONE);
                    nc_rest_valueT.setVisibility(View.GONE);
                    is_edit_con = 0;

                }
            }
        });
        if (nc_running_value_sw.isChecked()) {
            nc_running_valueT.setVisibility(View.VISIBLE);
            nc_running_value.setVisibility(View.VISIBLE);
            snackbarRun.setDuration(Snackbar.LENGTH_INDEFINITE);
            snackbarRun.show();
            is_runing = 1;
        } else {
            nc_running_valueT.setVisibility(View.GONE);
            nc_running_value.setVisibility(View.GONE);
            snackbarRun.dismiss();
            is_runing = 0;

        }
        nc_running_value_sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nc_running_value_sw.isChecked()) {
                    nc_running_valueT.setVisibility(View.VISIBLE);
                    nc_running_value.setVisibility(View.VISIBLE);

                    is_runing = 1;
                } else {
                    nc_running_valueT.setVisibility(View.GONE);
                    nc_running_value.setVisibility(View.GONE);
                    is_runing = 0;

                }

            }
        });

        nc_con_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {

                HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getAdapter().getItem(position);
                String contractTypeName = (String) obj.get("name");
                contractTypeCode = (String) obj.get("code");
                if (contractTypeCode.length() != 0) {
                    new EditContActivity.GetContractTypeData().execute();
                    Toast.makeText(EditContActivity.this, "نوع العقد: " + contractTypeName, Toast.LENGTH_SHORT).show();
                    nc_phar_type.setEnabled(true);
                    is_pay_ok = 1;

                } else {
                    Toast.makeText(EditContActivity.this, "تذكر ان تختار نوع العقد", Toast.LENGTH_SHORT).show();

                    nc_con_value.setText("0");
                    nc_join_value.setText("0");
                    nc_pay_up.setText("0");
                    nc_phar_type.setEnabled(false);
                    is_pay_ok = 0;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(EditContActivity.this, "تذكر ان تختار نوع العقد", Toast.LENGTH_SHORT).show();
                is_pay_ok = 0;

            }
        });

        final DatePickerDialog.OnDateSetListener nc_running_datepicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                nc_running_myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                nc_running_myCalendar.set(Calendar.MONTH, monthOfYear);
                nc_running_myCalendar.set(Calendar.YEAR, year);

                if (dateMaxMin(nc_running_myCalendar.getTime(), minDate, maxDate) == true) {


                    nc_running_updateData();
                    nc_running_date.setTextColor(Color.BLACK);
                    is_run_date_ok = 1;

                } else {
                    nc_running_date.setTextColor(Color.RED);
                    Toast.makeText(EditContActivity.this, "مسموح بإختيار التاريخ خلال 6 اشهر من اليوم ", Toast.LENGTH_SHORT).show();
                    is_run_date_ok = 0;
                }


            }

        };
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

        nc_running_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditContActivity.this, nc_running_datepicker,
                        nc_running_myCalendar.get(Calendar.YEAR),
                        nc_running_myCalendar.get(Calendar.MONTH),
                        nc_running_myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
        rec_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditContActivity.this, rec_Date_datepicker,
                        rec_Date_myCalendar.get(Calendar.YEAR),
                        rec_Date_myCalendar.get(Calendar.MONTH),
                        rec_Date_myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
        cheDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditContActivity.this, cheDate_datepicker,
                        cheDate_myCalendar.get(Calendar.YEAR),
                        cheDate_myCalendar.get(Calendar.MONTH),
                        cheDate_myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });


        nc_phar_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {

                HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getAdapter().getItem(position);
                phar_typeCode = (String) obj.get("code");
                phar_typePosition = position;
                if (position == 1) {
                    Mster_name.setEnabled(true);
                    Mster_name.setVisibility(View.VISIBLE);
                    is_dit_ok = 0;
                    if (nc_join_value.getText().toString().length() > 1) {
                        join_value_INT = Integer.parseInt(nc_join_value.getText().toString());
                        if (join_value_INT > 2700) {
//                        int new_join_value=join_value_INT-300;
                            nc_join_value.setText("2700");
                            Toast.makeText(EditContActivity.this, "تم تغير رسوم الانظمام الى: " + "2700", Toast.LENGTH_SHORT).show();
                            int nc_pay_down_INT = 0;
                            join_value_INT = 0;
                            if (nc_pay_down.getText().toString().length() > 0) {
                                nc_pay_down_INT = Integer.parseInt(nc_pay_down.getText().toString());

                            }
                            if (nc_join_value.getText().toString().length() > 0) {
                                join_value_INT = Integer.parseInt(nc_join_value.getText().toString());

                            }
                            nc_pay_up.setText(String.valueOf(join_value_INT - nc_pay_down_INT));
                        }
                    } else {
                        Toast.makeText(EditContActivity.this, "برجاء مراجعة الرسوم", Toast.LENGTH_SHORT).show();

                    }
                    Toast.makeText(EditContActivity.this, "قم الان بختيار الصيدلية الرئيسية (Master)", Toast.LENGTH_SHORT).show();


                }
                if (position != 1) {
                    Mster_name.setEnabled(false);
                    Mster_name.setVisibility(View.GONE);
                    is_dit_ok = 1;
                    if (nc_join_value.getText().toString().length() > 1) {
                        join_value_INT = Integer.parseInt(nc_join_value.getText().toString());
                        if (join_value_INT <= 2700) {
//                        int new_join_value=join_value_INT-300;
                            nc_join_value.setText("3000");
                            Toast.makeText(EditContActivity.this, "تم تعديل رسوم الانظمام الى: " + "3000", Toast.LENGTH_SHORT).show();
                            int nc_pay_down_INT = 0;
                            join_value_INT = 0;
                            if (nc_pay_down.getText().toString().length() > 0) {
                                nc_pay_down_INT = Integer.parseInt(nc_pay_down.getText().toString());

                            }
                            if (nc_join_value.getText().toString().length() > 0) {
                                join_value_INT = Integer.parseInt(nc_join_value.getText().toString());

                            }
                            nc_pay_up.setText(String.valueOf(join_value_INT - nc_pay_down_INT));
                        }
                    } else {
                        Toast.makeText(EditContActivity.this, "برجاء مراجعة الرسوم", Toast.LENGTH_SHORT).show();

                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(EditContActivity.this, "تذكر ان تختار نوع الصيدلية ", Toast.LENGTH_SHORT).show();

            }
        });


        paytypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {

                HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getAdapter().getItem(position);
                paytypeCode = (String) obj.get("code");
                paytypeSp_position = position;
                if (position == 0) {
                    Toast.makeText(EditContActivity.this, "تسديد بشيك ", Toast.LENGTH_SHORT).show();

                    banknameTit.setEnabled(true);
                    banknameTit.setVisibility(View.VISIBLE);

                    banknameSp.setEnabled(true);
                    banknameSp.setVisibility(View.VISIBLE);

                    chenam.setEnabled(true);
                    chenam.setVisibility(View.VISIBLE);

                    cheDate.setEnabled(true);
                    cheDate.setVisibility(View.VISIBLE);
                    cheDateT.setVisibility(View.VISIBLE);

                }
                if (position == 1) {


                    Toast.makeText(EditContActivity.this, "تسديد نقدي", Toast.LENGTH_SHORT).show();

                    banknameTit.setEnabled(false);
                    banknameTit.setVisibility(View.GONE);
                    banknameSp.setEnabled(false);
                    banknameSp.setVisibility(View.GONE);
                    chenam.setEnabled(false);
                    chenam.setVisibility(View.GONE);
                    cheDate.setEnabled(false);
                    cheDateT.setVisibility(View.GONE);
                }
                if (position == 2) {
                    Toast.makeText(EditContActivity.this, "تسديد بحوالة بنكية", Toast.LENGTH_SHORT).show();

                    banknameTit.setEnabled(true);
                    banknameTit.setVisibility(View.VISIBLE);

                    banknameSp.setEnabled(true);
                    banknameSp.setVisibility(View.VISIBLE);

                    chenam.setEnabled(true);
                    chenam.setVisibility(View.VISIBLE);

                    cheDate.setEnabled(true);
                    cheDate.setVisibility(View.VISIBLE);
                    cheDateT.setVisibility(View.VISIBLE);


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(EditContActivity.this, "تذكر ان تختار نوع التسديد ", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(EditContActivity.this, "تذكر ان تختار نوع التسديد ", Toast.LENGTH_SHORT).show();

            }
        });
        nc_pay_up.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if (nc_pay_up.getText().toString().length() == 1 || nc_pay_up.getText().length() == 0) {
                        nc_pay_up.setText("0");
                        is_pay_ok = 0;
                    }
                } else {
                    if (nc_pay_up.getText().toString().length() == 1) {
                        nc_pay_up.setText("");
                        is_pay_ok = 0;

                    }
                }
                // TODO: the editText has just been left
            }
        });


        nc_pay_up.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (nc_join_value.getText().toString().length() <= 1) {

                    Toast.makeText(EditContActivity.this, "الاشتراك الشهري فارغ او قيمه 0", Toast.LENGTH_SHORT).show();

                }
                if (s.toString().length() >= 1) {
                    join_value_INT = Integer.parseInt(nc_join_value.getText().toString());
                    pay_down_INT = Integer.parseInt(nc_pay_down.getText().toString());
                    nc_pay_up_INT = Integer.parseInt(s.toString());

                    if (nc_pay_up_INT > (join_value_INT - pay_down_INT)) {


                        //   Toast.makeText(NewConActivity.this, "يجب ان لا تكون الدفعة المقدمة اكبر من رسوم الإنضمام" , Toast.LENGTH_SHORT).show();

                        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();

                        nc_pay_up.setTextColor(Color.RED);

                        is_pay_ok = 0;

                    } else {
                        is_pay_ok = 1;

                        snackbar.dismiss();

                        nc_pay_up.setTextColor(Color.BLACK);

                        if (s.toString().length() == 0) {
                            nc_pay_up_INT = 0;
                        } else {
                            nc_pay_up_INT = Integer.parseInt(s.toString());

                        }
                        if (nc_pay_down.getText().toString().length() == 0) {
                            pay_down_INT = 0;
                        } else {
                            pay_down_INT = Integer.parseInt(nc_pay_down.getText().toString());

                        }


                        if (nc_join_value.getText().toString().length() == 0) {
                            join_value_INT = 0;
                        } else {
                            join_value_INT = Integer.parseInt(nc_join_value.getText().toString());
                            ;

                        }
                        nc_rest_value_INT = join_value_INT - nc_pay_up_INT - pay_down_INT;
                        String nc_rest_value_S = String.valueOf(nc_rest_value_INT);
                        nc_rest_value.setText(nc_rest_value_S);
                        nc_running_value.setText(s.toString());

                    }


                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        nc_running_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (nc_running_value.getText().length() <= 1) {

                    snackbarRun.setDuration(Snackbar.LENGTH_INDEFINITE);
                    snackbarRun.show();
                    nc_running_value.setTextColor(Color.RED);
                    is_run_ok = 0;
                } else {
                    is_run_ok = 1;
                    snackbarRun.dismiss();
                    nc_running_value.setTextColor(Color.BLACK);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Mster_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditContActivity.this, BranchsActivity.class);
                i.putExtra("ToActivity", "22");
                i.putExtra("BranchCodeToActivity", BranchCodeS);
                i.putExtra("activation_flag", "a");
                startActivityForResult(i, 1);
            }
        });

//
//        nc_des.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                if(s.toString().length()==0){
//                    des_INT=0;
//                }
//                else {
//                    des_INT=Integer.parseInt(s.toString());
//
//                }
//                if(nc_pay_down.getText().toString().length()==0){
//                    pay_down_INT=0;
//                }
//                else {
//                    pay_down_INT=Integer.parseInt(nc_pay_down.getText().toString());
//
//                }
//                if(nc_pay_up.getText().toString().length()==0){
//                    nc_pay_up_INT =0;
//
//                }
//                else {
//                    nc_pay_up_INT=Integer.parseInt(nc_pay_up.getText().toString());;
//
//                }
//                if(nc_join_value.getText().toString().length()==0){
//                    join_value_INT=0;
//                }
//                else {
//                    join_value_INT=Integer.parseInt(nc_join_value.getText().toString());;
//
//                }
//                nc_rest_value_INT=join_value_INT-pay_down_INT-nc_pay_up_INT-des_INT;
//                String nc_rest_value_S=String.valueOf(nc_rest_value_INT);
//                nc_rest_value.setText(nc_rest_value_S);
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });


        //SR: Start send data


        nc_rep_code_vil_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new EditContActivity.GetReciept_ver().execute();
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
                sentActive = 0;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        nc_sendbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (is_dit_ok == 0) {
                    Toast.makeText(getApplicationContext(),
                            "قم بالتحقق من اسم الصيدلية الرئيسية",
                            Toast.LENGTH_LONG)
                            .show();
                    Mster_name.requestFocus();
                    scrollView.scrollTo(0, scrollView.getBottom());
                }

                if (is_pay_ok == 0 && is_edit_con == 1) {
                    Toast.makeText(getApplicationContext(),
                            "قم بالتحقق من رسوم الصيدلية",
                            Toast.LENGTH_LONG)
                            .show();
                    nc_pay_up.requestFocus();

                    scrollView.scrollTo(0, scrollView.getBottom() / 2);
                }
                if (is_run_ok == 0 && is_runing == 1) {
                    Toast.makeText(getApplicationContext(),
                            "قم بالتحقق مصاريف التشغيل",
                            Toast.LENGTH_LONG)
                            .show();
                    nc_pay_up.requestFocus();

                    scrollView.scrollTo(0, scrollView.getBottom() / 2);
                }

                if (is_run_date_ok == 0) {


                    nc_running_date.setTextColor(Color.RED);
                    Toast.makeText(EditContActivity.this, "مسموح بإختيار التاريخ خلال 6 اشهر من اليوم ", Toast.LENGTH_SHORT).show();
                    is_run_date_ok = 0;
                    nc_rep_code.requestFocus();

                    scrollView.scrollTo(0, scrollView.getBottom() / 2);

                }
                if (is_pay_ok == 1 && is_run_date_ok == 1 && is_run_ok == 1 && is_dit_ok == 1) {


                    Log.e(TAG, "paytypeSp_position:" + String.valueOf(paytypeSp_position));
                    Log.e(TAG, "is_runing:" + String.valueOf(is_runing));
                    Log.e(TAG, "is_edit_con:" + String.valueOf(is_edit_con));


                    AlertDialog.Builder builder1 = new AlertDialog.Builder(EditContActivity.this);
                    if (sentActive == 0) {

                        builder1.setMessage("سيتم الان التحقق من الإيصال ثم حفظ العقد");

                    }
                    if (sentActive == 1) {

                        builder1.setMessage("سيتم الان حفظ العقد");

                    }
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "تأكيد",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    if (sentActive == 0) {

                                        new EditContActivity.GetReciept_AN_Save_cover_page().execute();
                                    }
                                    if (sentActive == 1) {
                                        new EditContActivity.ReactiveClient().execute();

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

    private void nc_running_updateData() {

        try {

            nc_running_SDF = new SimpleDateFormat("yyyy/dd/MM");
            nc_running_dateAF = nc_running_SDF.format(nc_running_myCalendar.getTime()).toString();
            SimpleDateFormat nc_running_SDF2 = new SimpleDateFormat("yyyy/MM/dd");
            String nc_running_dateAF2 = nc_running_SDF2.format(nc_running_myCalendar.getTime()).toString();
            nc_running_date.setText(nc_running_dateAF2);

        } catch (Exception e) {
            //The handling for the code
        }

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

            cheDate_SDF = new SimpleDateFormat("yyyy/MM/dd");
            cheDate_dateAF = cheDate_SDF.format(cheDate_myCalendar.getTime()).toString();
            SimpleDateFormat cheDate_SDF2 = new SimpleDateFormat("yyyy/MM/dd");
            String cheDate_dateAF2 = cheDate_SDF2.format(cheDate_myCalendar.getTime()).toString();
            cheDate.setText(cheDate_dateAF2);
        } catch (Exception e) {
            //The handling for the code
        }

    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nc_con_value.getWindowToken(), 0);
    }

    public Boolean dateMaxMin(Date x, Date min, Date max) {

        if (x.after(min) && x.before(max)) {
            return true;
        } else {
            return false;
        }


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                phar_nameToActivity = data.getStringExtra("phar_nameToActivity");
                Mster_name.setText(phar_nameToActivity.toString());
            }
        }
    }

    private class GetClient_by_DCode extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(EditContActivity.this);
            pDialog.setMessage("إنتظر قليلا...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                WebServiceCall webc = new WebServiceCall(EditContActivity.this);
                webc.GetClientData(MotahedaCodeToActivity);
                webc.GetClientDataMore(P_Code);
                webc.GetContractTypeDataByCode(WebServiceClientDataMore.cont_kind.toString());
                WebServiceListHttpHolder sh = new WebServiceListHttpHolder();
                WebServiceListHttpHolder sh2 = new WebServiceListHttpHolder();
                WebServiceListHttpHolder sh3 = new WebServiceListHttpHolder();
                WebServiceListHttpHolder sh4 = new WebServiceListHttpHolder();
                WebServiceListHttpHolder sh5 = new WebServiceListHttpHolder();
                String jsonStr = sh.makeServiceCall(ContractTypeUrl);
                String jsonStr2 = sh2.makeServiceCall(PharTypeUrl);
                String jsonStr3 = sh3.makeServiceCall(paytypeUrl);
                String jsonStr4 = sh4.makeServiceCall(banknameUrl);
                String jsonStr5 = sh5.makeServiceCall(emp_by_superUrl);


                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        JSONArray jsonCall = jsonObj.getJSONArray("Table");
                        JSONObject jsonObj2 = new JSONObject(jsonStr2);
                        JSONArray jsonCall2 = jsonObj2.getJSONArray("Table");
                        JSONObject jsonObj3 = new JSONObject(jsonStr3);
                        JSONArray jsonCall3 = jsonObj3.getJSONArray("Table");
                        JSONObject jsonObj4 = new JSONObject(jsonStr4);
                        JSONArray jsonCall4 = jsonObj4.getJSONArray("Table");
                        JSONObject jsonObj5 = new JSONObject(jsonStr5);
                        JSONArray jsonCall5 = jsonObj5.getJSONArray("Table");


                        PharmTypeList.clear();
                        paytypeList.clear();
                        banknameList.clear();


                        for (int i = 0; i < jsonCall.length(); i++) {
                            JSONObject c = jsonCall.getJSONObject(i);


                            String code = c.getString("code");
                            String name = c.getString("name");

                            HashMap<String, String> contact = new HashMap<>();

                            contact.clear();
                            contact.put("code", code);
                            contact.put("name", name);

                            ContractTypeList.add(contact);

                        }

                        for (int ii = 0; ii < jsonCall2.length(); ii++) {
                            JSONObject c2 = jsonCall2.getJSONObject(ii);

                            String code = c2.getString("code");
                            String name = c2.getString("name");

                            HashMap<String, String> contact = new HashMap<>();

                            contact.clear();
                            contact.put("code", code);
                            contact.put("name", name);

                            PharmTypeList.add(contact);
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
                        for (int i = 0; i < jsonCall5.length(); i++) {
                            JSONObject c = jsonCall5.getJSONObject(i);


                            String code = c.getString("code");
                            String name = c.getString("name");

                            HashMap<String, String> contact = new HashMap<>();

                            contact.clear();
                            contact.put("code", code);
                            contact.put("name", name);

                            emp_by_super_list.add(contact);

                        }


                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                    }
                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                    SPsr.removeAllSP(EditContActivity.this);
                    finish();
                    Intent intent = new Intent(EditContActivity.this, LoginActivity.class);
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
                BranchCodeS = "";
                BranchCodeS = WebServiceClientData.motaheda_branch.toString();
                P_Code = "";
                P_Code = WebServiceClientData.code.toString();
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
                if (pharm_phone_lineS.length() == 0) {
                    pharm_phone_line.setText("رقم الهاتف الارضي:" + " ");
                }
                pharm_phone_mobileS = "";
                pharm_phone_mobileS = WebServiceClientData.tel1.toString();
                if (pharm_phone_mobileS.length() > 1) {

                    pharm_phone_mobile.setText("رقم الهاتف الارضي:" + " " + pharm_phone_mobileS);
                }
                if (pharm_phone_lineS.length() == 0) {
                    pharm_phone_mobile.setText("رقم الهاتف الارضي:" + " ");
                }
                pharm_phone_mobile.setText("رقم الهاتف المحمول:" + " " + WebServiceClientData.mob1.toString());
                pharm_doc_name.setText("اسم الصيدلي:" + " " + WebServiceClientData.manager_name.toString());
                //  con_month_val.setText("الاشتراك الشهري:"+" "+WebServiceClientDataMore.monthly_fee.toString());
                con_down_val.setText("الدفعة السابقة:" + " " + WebServiceClientDataMore.down_pay.toString());
                nc_pay_down.setText(WebServiceClientDataMore.down_pay.toString());


                //con_remaning_val.setText("الدفعة المقدمة:"+" "+WebServiceClientDataMore.remaining.toString());
                remaining = "";
                remaining = WebServiceClientDataMore.remaining.toString();

                con_val.setText("رسوم الإنضمام السابقة:" + " " + WebServiceClientDataMore.prog_price.toString());
                con_type.setText("نوع العقد:" + " " + WebServiceContTypeData.name.toString());

                nc_con_type.setAdapter(ContractTypeSAdapter);
                nc_phar_type.setAdapter(PharmTypeSAdapter);
                paytypeSp.setAdapter(paytypeSAdapter);
                paytypeSp.post(new Runnable() {
                    @Override
                    public void run() {
                        paytypeSp.setSelection(1);
                    }
                });
                banknameSp.setAdapter(banknameSAdapter);
                nc_emp_by_super.setAdapter(emp_by_super_SAdapter);


                // updatePharmCreatData(WebServiceClientData.date_created.toString());
                //nc_running_date.setText(WebServiceClientData.date_created.toString());


            } catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }


        }

    }

    private class GetContractTypeData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(EditContActivity.this);
            pDialog.setMessage("إنتظر قليلا...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                WebServiceCall webc = new WebServiceCall(EditContActivity.this);
                webc.GetContractTypeDataByCode(contractTypeCode.toString());


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


                nc_con_value_S = "";
                nc_con_value_S = WebServiceContTypeData.down_pay.toString();
                nc_join_value_S = "";
                nc_join_value_S = WebServiceContTypeData.price.toString();
                nc_con_value.setText(nc_con_value_S.replace(".0000", ""));
                nc_join_value.setText(nc_join_value_S.replace(".0000", ""));

                int nc_pay_down_INT = 0;
                join_value_INT = 0;
                if (nc_pay_down.getText().toString().length() > 0) {
                    nc_pay_down_INT = Integer.parseInt(nc_pay_down.getText().toString());

                }
                if (nc_join_value.getText().toString().length() > 0) {
                    join_value_INT = Integer.parseInt(nc_join_value.getText().toString());

                }
                nc_pay_up.setText(String.valueOf(join_value_INT - nc_pay_down_INT));


            } catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }


        }

    }

    private class GetReciept_ver extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(EditContActivity.this);
            pDialog.setMessage("يتم الان التحقق من رقم الإيصال");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                WebServiceCall webc = new WebServiceCall(EditContActivity.this);
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

    private class GetReciept_AN_Save_cover_page extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(EditContActivity.this);
            pDialog.setMessage("يتم الان التحقق من رقم الإيصال");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                WebServiceCall webc = new WebServiceCall(EditContActivity.this);
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
                    new EditContActivity.ReactiveClient().execute();


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

    private class ReactiveClient extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(EditContActivity.this);
            pDialog.setMessage("جاري الان ارسال العقد ...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                WebServiceCall webc = new WebServiceCall(EditContActivity.this);
                WebServiceSuperName webServiceSuperName = new WebServiceSuperName();
                webServiceSuperName.ErrorMessage = "";
                webc.GetSuper(empsercode);
                supername = webServiceSuperName.ErrorMessage.toString();

                if (is_edit_con == 1 || is_runing == 1) {

                    if (paytypeSp_position == 0 || paytypeSp_position == 2) {

                        if (is_edit_con == 1 && is_runing == 0) {
                            webc.Insert_Contract_Fess(P_Code, empsercode, nc_rep_code.getText().toString(), nc_pay_up.getText().toString(), paytypeCode, con_note.getText().toString(), chenam.getText().toString(), cheDate_dateAF, "", "downpay");
                            if (supername.length() > 1) {

                                webc.Update_cover_page_reactivate(P_Code, empsercode, doc_nameS, emp_name, supername, con_note.getText().toString(), nc_con_value.getText().toString(), contractTypeCode, nc_pay_up.getText().toString(), nc_rest_value.getText().toString(), nc_running_dateAF, real_Date, paytypeCode, nc_join_value.getText().toString(), banknameCode);
                            } else {
                                webc.Update_cover_page_reactivate(P_Code, empsercode, doc_nameS, emp_name, emp_name, con_note.getText().toString(), nc_con_value.getText().toString(), contractTypeCode, nc_pay_up.getText().toString(), nc_rest_value.getText().toString(), nc_running_dateAF, real_Date, paytypeCode, nc_join_value.getText().toString(), banknameCode);

                            }
                            webc.Insert_Reactivation_Phar(nc_rep_code.getText().toString(), real_Date, nc_pay_up.getText().toString(), contractTypeCode, P_Code);

                            if (phar_typePosition == 1) {
                                webc.Insert_Master_Phar_Mapping(P_Code, pharm_nameS, Mster_name.getText().toString());
                            }
                        }
                        if (is_edit_con == 0 && is_runing == 1) {
                            webc.Insert_another_fees(P_Code, "220", empsercode, nc_running_value.getText().toString(), rec_Date_dateAF, nc_rep_code.getText().toString(), con_note.getText().toString(), paytypeCode, chenam.getText().toString(), cheDate_dateAF, banknameCode);
                            webc.Update_client_activation_and_approve_sent_flag(P_Code, "c", "a", "b");

                        }
                        if (is_edit_con == 1 && is_runing == 1) {
                            webc.Insert_Contract_Fess(P_Code, empsercode, nc_rep_code.getText().toString(), nc_pay_up.getText().toString(), paytypeCode, con_note.getText().toString(), chenam.getText().toString(), cheDate_dateAF, "", "downpay");
                            if (supername.length() > 1) {

                                webc.Update_cover_page_reactivate(P_Code, empsercode, doc_nameS, emp_name, supername, con_note.getText().toString(), nc_con_value.getText().toString(), contractTypeCode, nc_pay_up.getText().toString(), nc_rest_value.getText().toString(), nc_running_dateAF, real_Date, paytypeCode, nc_join_value.getText().toString(), banknameCode);
                            } else {
                                webc.Update_cover_page_reactivate(P_Code, empsercode, doc_nameS, emp_name, emp_name, con_note.getText().toString(), nc_con_value.getText().toString(), contractTypeCode, nc_pay_up.getText().toString(), nc_rest_value.getText().toString(), nc_running_dateAF, real_Date, paytypeCode, nc_join_value.getText().toString(), banknameCode);

                            }
                            webc.Insert_Reactivation_Phar(nc_rep_code.getText().toString(), real_Date, nc_pay_up.getText().toString(), contractTypeCode, P_Code);

                            if (phar_typePosition == 1) {
                                webc.Insert_Master_Phar_Mapping(P_Code, pharm_nameS, Mster_name.getText().toString());
                            }
                            webc.Insert_another_fees(P_Code, "220", empsercode, nc_running_value.getText().toString(), rec_Date_dateAF, nc_rep_code.getText().toString(), con_note.getText().toString(), paytypeCode, chenam.getText().toString(), cheDate_dateAF, banknameCode);
                        }

                    }
                    if (paytypeSp_position == 1) {

                        if (is_edit_con == 1 && is_runing == 0) {
                            webc.Insert_Contract_Fess(P_Code, empsercode, nc_rep_code.getText().toString(), nc_pay_up.getText().toString(), paytypeCode, con_note.getText().toString(), "", "", "", "downpay");
                            if (supername.length() > 1) {

                                webc.Update_cover_page_reactivate(P_Code, empsercode, doc_nameS, emp_name, supername, con_note.getText().toString(), nc_con_value.getText().toString(), contractTypeCode, nc_pay_up.getText().toString(), nc_rest_value.getText().toString(), nc_running_dateAF, real_Date, paytypeCode, nc_join_value.getText().toString(), banknameCode);
                            } else {
                                webc.Update_cover_page_reactivate(P_Code, empsercode, doc_nameS, emp_name, emp_name, con_note.getText().toString(), nc_con_value.getText().toString(), contractTypeCode, nc_pay_up.getText().toString(), nc_rest_value.getText().toString(), nc_running_dateAF, real_Date, paytypeCode, nc_join_value.getText().toString(), banknameCode);

                            }
                            webc.Insert_Reactivation_Phar(nc_rep_code.getText().toString(), real_Date, nc_pay_up.getText().toString(), contractTypeCode, P_Code);

                            if (phar_typePosition == 1) {
                                webc.Insert_Master_Phar_Mapping(P_Code, pharm_nameS, Mster_name.getText().toString());
                            }
                        }
                        if (is_edit_con == 0 && is_runing == 1) {
                            webc.Insert_another_fees(P_Code, "220", empsercode, nc_running_value.getText().toString(), rec_Date_dateAF, nc_rep_code.getText().toString(), con_note.getText().toString(), paytypeCode, "", "", banknameCode);
                            webc.Update_client_activation_and_approve_sent_flag(P_Code, "c", "a", "b");

                        }
                        if (is_edit_con == 1 && is_runing == 1) {
                            webc.Insert_Contract_Fess(P_Code, empsercode, nc_rep_code.getText().toString(), nc_pay_up.getText().toString(), paytypeCode, con_note.getText().toString(), "", "", "", "downpay");
                            if (supername.length() > 1) {

                                webc.Update_cover_page_reactivate(P_Code, empsercode, doc_nameS, emp_name, supername, con_note.getText().toString(), nc_con_value.getText().toString(), contractTypeCode, nc_pay_up.getText().toString(), nc_rest_value.getText().toString(), nc_running_dateAF, real_Date, paytypeCode, nc_join_value.getText().toString(), banknameCode);
                            } else {
                                webc.Update_cover_page_reactivate(P_Code, empsercode, doc_nameS, emp_name, emp_name, con_note.getText().toString(), nc_con_value.getText().toString(), contractTypeCode, nc_pay_up.getText().toString(), nc_rest_value.getText().toString(), nc_running_dateAF, real_Date, paytypeCode, nc_join_value.getText().toString(), banknameCode);

                            }
                            webc.Insert_Reactivation_Phar(nc_rep_code.getText().toString(), real_Date, nc_pay_up.getText().toString(), contractTypeCode, P_Code);

                            if (phar_typePosition == 1) {
                                webc.Insert_Master_Phar_Mapping(P_Code, pharm_nameS, Mster_name.getText().toString());
                            }
                            webc.Insert_another_fees(P_Code, "220", empsercode, nc_running_value.getText().toString(), rec_Date_dateAF, nc_rep_code.getText().toString(), con_note.getText().toString(), paytypeCode, "", "", banknameCode);

                        }

                    }


                    webc.Insert_Activate_Data(P_Code);

                    if (pharm_phone_mobileS.length() == 0) {

                        PhoneS = pharm_phone_lineS;
                    }
                    if (pharm_phone_lineS.length() == 0) {
                        PhoneS = pharm_phone_mobileS;
                    }

                    webc.Insert_Emp_Daily_Report(empsercode, P_Code, doc_nameS, con_note.getText().toString(), pharm_nameS, PhoneS, BranchCodeS);
                    webc.Insert_Reciepts_Data(nc_rep_code.getText().toString(), rec_Date.getText().toString());


                    webc.Update_client_stage(P_Code, "b");
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
                Toast.makeText(getApplicationContext(),
                        "تمت العملية بنجاح",
                        Toast.LENGTH_LONG)
                        .show();

                EditContActivity.super.onBackPressed();

                Log.e(TAG, "supername supername: " + supername);

            } catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }


        }


    }


    @Override
    public void onBackPressed() {


        finish();

        super.onBackPressed();
    }

}
