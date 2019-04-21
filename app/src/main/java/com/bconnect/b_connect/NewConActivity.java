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

public class NewConActivity extends AppCompatActivity {


    SharedPreferencesSR SPsr = new SharedPreferencesSR();


    private String TAG = "SR: " + NewConActivity.class.getSimpleName() + ": ";
    private int sentActive, is_dit_ok, is_pay_ok, is_run_date_ok;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String asm, ser, empsercode, supername;


    //Definitions
    private ProgressDialog pDialog;
    TextView con_type, banknameTit, nc_emp_by_super_tit;

    EditText nc_motaheda_code, nc_running_date, nc_rep_code, rec_Date, cheDate, chenam, Mster_name, con_note;
    EditText nc_con_value, nc_join_value, nc_pay_up, nc_des, nc_rest_value;
    EditText nc_ph_name, nc_ph_add, nc_ph_ph1, nc_ph_ph2, nc_ph_doc_name, total_value;


    String MotahedaCodeToActivity, Motaheda_codeS, nc_running_dateAF, rec_Date_dateAF, cheDate_dateAF, PharCode, real_Date, real_Date2, pharm_phone_lineS, pharm_phone_mobileS, PhoneS;
    String contractTypeCode, phar_typeCode, banknameCode, paytypeCode, P_Code, doc_nameS, nc_con_value_S, nc_join_value_S;
    String emp_name, remaining, pharm_nameS, BranchCodeS, phar_nameToActivity;


    int pay_down_INT, des_INT, join_value_INT, nc_rest_value_INT, nc_pay_up_INT, paytypeSp_position, phar_typePosition, Real_month_INT, Real_month_INT_AF, Real_year_INT, Real_year_INT_AF;

    SimpleDateFormat nc_running_SDF, rec_Date_SDF, cheDate_SDF;

    Button motaheda_code_vil_btn, nc_sendbut, nc_rep_code_vil_but;

    Switch nc_sw_new_code;

    boolean is_new_code;

    Intent intent;

    private static String ContractTypeUrl, PharTypeUrl, paytypeUrl, banknameUrl, emp_by_superUrl;

    Spinner nc_con_type, nc_phar_type, paytypeSp, banknameSp, nc_emp_by_super;


    List<HashMap<String, String>> ContractTypeList, PharmTypeList, paytypeList, banknameList, emp_by_super_list;
    private SimpleAdapter ContractTypeSAdapter, PharmTypeSAdapter, paytypeSAdapter, banknameSAdapter, emp_by_super_SAdapter;

    Calendar nc_running_myCalendar, rec_Date_myCalendar, cheDate_myCalendar, real_Date_myCalendar, real_Date_Calendar_Max, real_Date_Calendar_Min;

    Date minDate, maxDate;
    int i = 0;

    Snackbar snackbar, snackbar_dit, snackbar_pay;
    NestedScrollView scrollView;

    TextInputLayout nc_desT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_con);

        basic();

        new NewConActivity.GetClient_by_DCode().execute();

        control();
    }


    public void basic() {

        scrollView = (NestedScrollView) findViewById(R.id.nCon_NestedScrollView);

        con_type = (TextView) findViewById(R.id.con_type);
        banknameTit = (TextView) findViewById(R.id.banknameTit);
//        cheDateTit=(TextView)findViewById(R.id.cheDateTit);
        //nc_emp_by_super_tit=(TextView)findViewById(R.id.nc_emp_by_super_tit);

        nc_motaheda_code = (EditText) findViewById(R.id.nc_motaheda_code);
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
//        nc_pay_down=(EditText) findViewById(R.id.nc_pay_down);
        nc_des = (EditText) findViewById(R.id.nc_des);
        nc_rest_value = (EditText) findViewById(R.id.nc_rest_value);
        total_value = (EditText) findViewById(R.id.total_value);
        nc_ph_name = (EditText) findViewById(R.id.nc_ph_name);
        nc_ph_add = (EditText) findViewById(R.id.nc_ph_add);
        nc_ph_ph1 = (EditText) findViewById(R.id.nc_ph_ph1);
        nc_ph_ph2 = (EditText) findViewById(R.id.nc_ph_ph2);
        nc_ph_doc_name = (EditText) findViewById(R.id.nc_ph_doc_name);

        nc_desT = (TextInputLayout) findViewById(R.id.nc_desT);


        nc_sendbut = (Button) findViewById(R.id.nc_sendbut);
        nc_rep_code_vil_but = (Button) findViewById(R.id.nc_rep_code_vil_but);
        motaheda_code_vil_btn = (Button) findViewById(R.id.motaheda_code_vil_btn);
//        emp_by_super_switch=(Switch) findViewById(R.id.emp_by_super_switch);
        nc_sw_new_code = (Switch) findViewById(R.id.nc_sw_new_code);

        nc_con_type = (Spinner) findViewById(R.id.nc_con_type);
        nc_phar_type = (Spinner) findViewById(R.id.nc_phar_type);
        paytypeSp = (Spinner) findViewById(R.id.paytypeSp);
        banknameSp = (Spinner) findViewById(R.id.banknameSp);
        //nc_emp_by_super= (Spinner) findViewById(R.id.nc_emp_by_super);

        nc_join_value.setEnabled(false);


        ContractTypeList = new ArrayList<HashMap<String, String>>();
        PharmTypeList = new ArrayList<HashMap<String, String>>();
        paytypeList = new ArrayList<HashMap<String, String>>();
        banknameList = new ArrayList<HashMap<String, String>>();
        emp_by_super_list = new ArrayList<HashMap<String, String>>();

        // ContractTypeAdapter=new SimpleAdapter(NewConActivity.this, android.R.layout.simple_spinner_dropdown_item, branchlist);
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

        try {

            intent = getIntent();
            BranchCodeS = intent.getExtras().getString("BranchCodeToActivity");
            MotahedaCodeToActivity = intent.getExtras().getString("MotahedaCodeToActivity");
            PharCode = intent.getExtras().getString("PharCode");

        } catch (Exception e) {

            super.onBackPressed();
        }

        //  PharCode = intent.getExtras().getString("PharCode");


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


        sentActive = 0;
        is_new_code = true;


        pref = getApplicationContext().getSharedPreferences("MySr", MODE_PRIVATE);
        editor = pref.edit();


        empsercode = "";
        emp_name = "";

        empsercode = SPsr.getSP(getApplicationContext(), "empsercode");
        emp_name = SPsr.getSP(getApplicationContext(), "empsername");

        P_Code = "";
        doc_nameS = "";
        is_dit_ok = 0;
        is_pay_ok = 0;
        snackbar = Snackbar.make(findViewById(android.R.id.content), "يجب ان لا تكون الدفعة المقدمة اكبر من رسوم الإنضمام", Snackbar.LENGTH_LONG);
        snackbar_dit = Snackbar.make(findViewById(android.R.id.content), "بيانات الصيدلية الاساسية غير صحيحة", Snackbar.LENGTH_LONG);
        snackbar_pay = Snackbar.make(findViewById(android.R.id.content), "رسوم الصيدلية غير صحيحة", Snackbar.LENGTH_LONG);


        Log.e(TAG, "SP.empsercode: " + empsercode);
        Log.e(TAG, "SP.emp_name: " + emp_name);


    }

    public void control() {


        if (empsercode != null || empsercode != "" || empsercode.length() > 0) {
            emp_by_superUrl = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/Android.asmx/Get_Emp_by_Super?emp_code=" + empsercode.toString();

        }

        if (is_dit_ok == 0) {
            snackbar_dit.setDuration(Snackbar.LENGTH_INDEFINITE);
            snackbar_dit.show();
            scrollView.getParent().requestChildFocus(scrollView, scrollView);
            scrollView.setNestedScrollingEnabled(false);
            scrollView.requestDisallowInterceptTouchEvent(true);


        }

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (nc_ph_name.getText().toString().length() > 3 && nc_ph_add.getText().toString().length() > 3 && nc_ph_doc_name.getText().toString().length() > 3 && nc_ph_ph1.getText().toString().length() >= 8 &&
                        nc_ph_ph2.getText().toString().startsWith("01") && nc_ph_ph2.getText().toString().length() == 11) {

                    is_dit_ok = 1;

                    if (phar_typePosition != 1) {


                        is_dit_ok = 1;

                    }

                } else {
                    is_dit_ok = 0;


                }
                if (nc_pay_up.getText().toString().length() >= 2) {

                    nc_pay_up_INT = Integer.parseInt(nc_pay_up.getText().toString());

                    if (nc_pay_up_INT < join_value_INT) {
                        nc_pay_up.setTextColor(Color.BLACK);
                        is_pay_ok = 1;
                    }
                } else {
                    nc_pay_up.setTextColor(Color.RED);
                    is_pay_ok = 0;
                }
                if (phar_typePosition == 1) {
                    if (Mster_name.getText().length() < 1) {
                        is_dit_ok = 0;
                    }
                }

                if (is_dit_ok == 0) {
                    snackbar_dit.setDuration(Snackbar.LENGTH_INDEFINITE);
                    snackbar_dit.show();


                }
                if (is_dit_ok == 1) {


                    snackbar_dit.dismiss();

                }

                if (dateMaxMin(nc_running_myCalendar.getTime(), minDate, maxDate) == true) {


                    nc_running_updateData();
                    nc_running_date.setTextColor(Color.BLACK);
                    is_run_date_ok = 1;

                } else {
                    nc_running_date.setTextColor(Color.RED);
                    is_run_date_ok = 0;
                }
                if (nc_des.getText().length() > 1 && nc_pay_up.getText().length() > 0) {
                    if ((join_value_INT - nc_pay_up_INT) > des_INT && des_INT != 0) {
                        is_pay_ok = 1;
                        nc_des.setTextColor(Color.BLACK);
                        nc_desT.setErrorEnabled(false);
                    } else {
                        nc_des.setTextColor(Color.RED);
                        nc_desT.setErrorEnabled(true);
                        nc_desT.setError("الخصم اكبر من المسموح");
                        is_pay_ok = 0;
                    }
                }


                closeKeyboard();
                return false;

            }
        });

        Mster_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewConActivity.this, BranchsActivity.class);
                i.putExtra("ToActivity", "22");
                i.putExtra("BranchCodeToActivity", BranchCodeS);
                i.putExtra("activation_flag", "a");
                startActivityForResult(i, 1);
            }
        });

        nc_ph_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (nc_ph_name.getText().toString().length() < 3) {

                        Toast.makeText(NewConActivity.this, "اسم الصيدلية اقل من 3 احرف او فارغ", Toast.LENGTH_SHORT).show();
                        nc_ph_name.setTextColor(Color.RED);
                        is_dit_ok = 0;
                    } else {
                        nc_ph_name.setTextColor(Color.BLACK);

                        is_dit_ok = 1;
                    }
                }
                // TODO: the editText has just been left
            }
        });
        nc_ph_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (nc_ph_name.getText().toString().length() < 3) {

                    nc_ph_name.setTextColor(Color.RED);
                    is_dit_ok = 0;
                } else {
                    nc_ph_name.setTextColor(Color.BLACK);
                    is_dit_ok = 1;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        nc_ph_add.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (nc_ph_add.getText().toString().length() < 3) {

                        Toast.makeText(NewConActivity.this, "عنوان الصيدلية اقل من 3 احرف او فارغ", Toast.LENGTH_SHORT).show();
                        nc_ph_add.setTextColor(Color.RED);
                        is_dit_ok = 0;
                    } else {
                        nc_ph_add.setTextColor(Color.BLACK);

                        is_dit_ok = 1;
                    }
                }
                // TODO: the editText has just been left
            }
        });
        nc_ph_add.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (nc_ph_add.getText().toString().length() < 3) {

                    nc_ph_add.setTextColor(Color.RED);
                    is_dit_ok = 0;
                } else {
                    nc_ph_add.setTextColor(Color.BLACK);

                    is_dit_ok = 1;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        nc_ph_doc_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (nc_ph_doc_name.getText().toString().length() < 3) {

                        Toast.makeText(NewConActivity.this, "اسم الصيدلي اقل من 3 احرف او فارغ", Toast.LENGTH_SHORT).show();
                        nc_ph_doc_name.setTextColor(Color.RED);
                        is_dit_ok = 0;
                    } else {
                        nc_ph_doc_name.setTextColor(Color.BLACK);

                        is_dit_ok = 1;
                    }
                }
                // TODO: the editText has just been left
            }
        });
        nc_ph_doc_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (nc_ph_doc_name.getText().toString().length() < 3) {

                    nc_ph_doc_name.setTextColor(Color.RED);
                    is_dit_ok = 0;
                } else {
                    nc_ph_doc_name.setTextColor(Color.BLACK);

                    is_dit_ok = 1;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        nc_ph_ph1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() < 8) {

                    nc_ph_ph1.setTextColor(Color.RED);
                    is_dit_ok = 0;
                } else {
                    nc_ph_ph1.setTextColor(Color.BLACK);
                    is_dit_ok = 1;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        nc_ph_ph1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (nc_ph_ph1.getText().toString().length() < 8) {

                        Toast.makeText(NewConActivity.this, "يجب ان لا يقل رقم الهاتف الارضي عن 8 ارقام", Toast.LENGTH_SHORT).show();
                        nc_ph_ph1.setTextColor(Color.RED);
                        is_dit_ok = 0;
                    } else {
                        nc_ph_ph1.setTextColor(Color.BLACK);

                        is_dit_ok = 1;
                    }
                }
                // TODO: the editText has just been left
            }
        });


        nc_ph_ph2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!charSequence.toString().toString().startsWith("01")) {
                    nc_ph_ph2.setTextColor(Color.RED);
                    is_dit_ok = 0;

                }
                if (charSequence.toString().length() < 11) {

                    nc_ph_ph2.setTextColor(Color.RED);
                    is_dit_ok = 0;
                }
                if (charSequence.toString().length() == 11) {
                    if (!charSequence.toString().toString().startsWith("01")) {
                        nc_ph_ph2.setTextColor(Color.RED);
                        is_dit_ok = 0;
                    } else {
                        nc_ph_ph2.setTextColor(Color.BLACK);
                        is_dit_ok = 1;
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        nc_ph_ph2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if (!nc_ph_ph2.getText().toString().startsWith("01")) {
                        Toast.makeText(NewConActivity.this, "يجب يبدأ رقم الهاتف المحمول ب 01", Toast.LENGTH_SHORT).show();

                    }

                    if (nc_ph_ph2.getText().toString().length() < 11) {

                        Toast.makeText(NewConActivity.this, "يجب ان لا يقل رقم الهاتف المحمول عن 11 رقم", Toast.LENGTH_SHORT).show();
                        nc_ph_ph2.setTextColor(Color.RED);
                        is_dit_ok = 0;
                    }
                    if (nc_ph_ph2.getText().toString().length() >= 11) {
                        nc_ph_ph2.setTextColor(Color.BLACK);

                        is_dit_ok = 1;
                    }
                }
                // TODO: the editText has just been left
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


        nc_des.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if (nc_des.getText().length() == 0) {
                        nc_des.setText("0");
                    }
                } else {
                    if (nc_des.getText().toString().length() == 1) {
                        nc_des.setText("");
                    }
                }
                // TODO: the editText has just been left
            }
        });


        nc_rest_value.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if (nc_rest_value.getText().length() == 0) {

                        nc_des.setText("0");

                    }

                } else {
                    if (nc_rest_value.getText().toString().length() == 1) {
                        nc_rest_value.setText("");
                    }
                }
                // TODO: the editText has just been left
            }
        });

        if (nc_sw_new_code.isChecked()) {
            nc_motaheda_code.setEnabled(false);
            nc_motaheda_code.setVisibility(View.GONE);
            motaheda_code_vil_btn.setEnabled(false);
            motaheda_code_vil_btn.setVisibility(View.GONE);
            is_new_code = true;
        }
        if (!nc_sw_new_code.isChecked()) {
            nc_motaheda_code.setEnabled(true);
            nc_motaheda_code.setVisibility(View.VISIBLE);
            motaheda_code_vil_btn.setEnabled(true);
            motaheda_code_vil_btn.setVisibility(View.VISIBLE);
            is_new_code = false;
        }


        nc_sw_new_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nc_sw_new_code.isChecked()) {
                    nc_motaheda_code.setEnabled(false);
                    nc_motaheda_code.setVisibility(View.GONE);
                    motaheda_code_vil_btn.setEnabled(false);
                    motaheda_code_vil_btn.setVisibility(View.GONE);
                    is_new_code = true;
                }
                if (!nc_sw_new_code.isChecked()) {
                    nc_motaheda_code.setEnabled(true);
                    nc_motaheda_code.setVisibility(View.VISIBLE);
                    motaheda_code_vil_btn.setEnabled(true);
                    motaheda_code_vil_btn.setVisibility(View.VISIBLE);
                    is_new_code = false;
                }
            }
        });

        motaheda_code_vil_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NewConActivity.motaheda_code_vali().execute();
            }
        });
        nc_con_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {

                HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getAdapter().getItem(position);
                String contractTypeName = (String) obj.get("name");
                contractTypeCode = (String) obj.get("code");
                if (contractTypeCode.length() != 0) {
                    new NewConActivity.GetContractTypeData().execute();
                    Toast.makeText(NewConActivity.this, "نوع العقد: " + contractTypeName, Toast.LENGTH_SHORT).show();
                    nc_phar_type.setEnabled(true);
                    nc_con_value.setEnabled(true);
                    nc_join_value.setEnabled(true);

                } else {
                    Toast.makeText(NewConActivity.this, "تذكر ان تختار نوع العقد", Toast.LENGTH_SHORT).show();
                    nc_con_value.setEnabled(false);
                    nc_con_value.setText("0");
                    nc_join_value.setEnabled(false);
                    nc_join_value.setText("0");
                    nc_pay_up.setText("0");
                    nc_des.setText("0");
                    nc_rest_value.setText("0");
                    nc_phar_type.setEnabled(false);
                    is_pay_ok = 0;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(NewConActivity.this, "تذكر ان تختار نوع العقد", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(NewConActivity.this, "مسموح بإختيار التاريخ خلال 6 اشهر من اليوم ", Toast.LENGTH_SHORT).show();
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
                new DatePickerDialog(NewConActivity.this, nc_running_datepicker,
                        nc_running_myCalendar.get(Calendar.YEAR),
                        nc_running_myCalendar.get(Calendar.MONTH),
                        nc_running_myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
        rec_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewConActivity.this, rec_Date_datepicker,
                        rec_Date_myCalendar.get(Calendar.YEAR),
                        rec_Date_myCalendar.get(Calendar.MONTH),
                        rec_Date_myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
        cheDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewConActivity.this, cheDate_datepicker,
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
                    if (nc_join_value.getText().toString().length() > 1) {
                        join_value_INT = Integer.parseInt(nc_join_value.getText().toString());
                        if (join_value_INT > 2700) {
//                        int new_join_value=join_value_INT-300;
                            nc_join_value.setText("2700");
                            Toast.makeText(NewConActivity.this, "تم تغير رسوم الانظمام الى: " + "2700", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(NewConActivity.this, "برجاء مراجعة الرسوم", Toast.LENGTH_SHORT).show();

                    }
                    Toast.makeText(NewConActivity.this, "قم الان بختيار الصيدلية الرئيسية (Master)", Toast.LENGTH_SHORT).show();


                }
                if (position != 1) {
                    Mster_name.setEnabled(false);
                    Mster_name.setVisibility(View.GONE);

                    if (nc_join_value.getText().toString().length() > 1) {
                        join_value_INT = Integer.parseInt(nc_join_value.getText().toString());
                        if (join_value_INT <= 2700) {
//                        int new_join_value=join_value_INT-300;
                            nc_join_value.setText("3000");
                            Toast.makeText(NewConActivity.this, "تم تعديل رسوم الانظمام الى: " + "3000", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(NewConActivity.this, "برجاء مراجعة الرسوم", Toast.LENGTH_SHORT).show();

                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(NewConActivity.this, "تذكر ان تختار نوع الصيدلية ", Toast.LENGTH_SHORT).show();

            }
        });


        paytypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {

                HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getAdapter().getItem(position);
                paytypeCode = (String) obj.get("code");
                paytypeSp_position = position;
                if (position == 0) {
                    Toast.makeText(NewConActivity.this, "تحصيل بشيك ", Toast.LENGTH_SHORT).show();

                    banknameTit.setEnabled(true);
                    banknameTit.setVisibility(View.VISIBLE);

                    banknameSp.setEnabled(true);
                    banknameSp.setVisibility(View.VISIBLE);

                    chenam.setEnabled(true);
                    chenam.setVisibility(View.VISIBLE);

                    cheDate.setEnabled(true);
                    cheDate.setVisibility(View.VISIBLE);
                    //cheDateTit.setVisibility(View.VISIBLE);

                }
                if (position == 1) {


                    Toast.makeText(NewConActivity.this, "تحصيل نقدي", Toast.LENGTH_SHORT).show();

                    banknameTit.setEnabled(false);
                    banknameTit.setVisibility(View.GONE);
                    banknameSp.setEnabled(false);
                    banknameSp.setVisibility(View.GONE);
                    chenam.setEnabled(false);
                    chenam.setVisibility(View.GONE);
                    cheDate.setEnabled(false);
                    cheDate.setVisibility(View.GONE);
                    // cheDateTit.setVisibility(View.GONE);
                }
                if (position == 2) {
                    Toast.makeText(NewConActivity.this, "تحصيل بحوالة بنكية", Toast.LENGTH_SHORT).show();

                    banknameTit.setEnabled(true);
                    banknameTit.setVisibility(View.VISIBLE);

                    banknameSp.setEnabled(true);
                    banknameSp.setVisibility(View.VISIBLE);

                    chenam.setEnabled(true);
                    chenam.setVisibility(View.VISIBLE);

                    cheDate.setEnabled(true);
                    cheDate.setVisibility(View.VISIBLE);
                    //  cheDateTit.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(NewConActivity.this, "تذكر ان تختار نوع التحصيل ", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(NewConActivity.this, "تذكر ان تختار نوع التحصيل ", Toast.LENGTH_SHORT).show();

            }
        });


        nc_pay_up.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                i += 1;
                if (nc_join_value.getText().toString().length() <= 1) {

                    Toast.makeText(NewConActivity.this, "الاشتراك الشهري فارغ او قيمه 0", Toast.LENGTH_SHORT).show();

                }
                if (s.toString().length() >= 1) {
                    if (nc_join_value.getText().toString().length() >= 1) {

                        join_value_INT = Integer.parseInt(nc_join_value.getText().toString());
                    }
                    nc_pay_up_INT = Integer.parseInt(s.toString());

                    if (nc_pay_up_INT > join_value_INT) {


                        //   Toast.makeText(NewConActivity.this, "يجب ان لا تكون الدفعة المقدمة اكبر من رسوم الإنضمام" , Toast.LENGTH_SHORT).show();

                        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();

                        nc_pay_up.setTextColor(Color.RED);
                        is_pay_ok = 0;
                    } else {
                        snackbar.dismiss();

                        nc_pay_up.setTextColor(Color.BLACK);
                        is_pay_ok = 1;

                        if (s.toString().length() == 0) {
                            nc_pay_up_INT = 0;
                        } else {
                            nc_pay_up_INT = Integer.parseInt(s.toString());

                        }

                        if (nc_des.getText().toString().length() == 0) {
                            des_INT = 0;
                        } else {
                            des_INT = Integer.parseInt(nc_des.getText().toString());
                            ;

                        }
                        if (nc_join_value.getText().toString().length() == 0) {
                            join_value_INT = 0;
                        } else {
                            join_value_INT = Integer.parseInt(nc_join_value.getText().toString());
                            ;

                        }
                        nc_rest_value_INT = join_value_INT - nc_pay_up_INT - des_INT;
                        String nc_rest_value_S = String.valueOf(nc_rest_value_INT);
                        if (nc_des.getText().length() > 0 && nc_pay_up.getText().length() > 0) {


                            if ((join_value_INT == nc_pay_up_INT)) {
                                is_pay_ok = 1;
                                nc_des.setText("0");
                                nc_des.setTextColor(Color.BLACK);
                                if (nc_rest_value_INT <= 0) {
                                    nc_rest_value.setText("0");
                                } else {
                                    nc_rest_value.setText(nc_rest_value_S);

                                }
                                nc_desT.setErrorEnabled(false);
                                nc_des.setEnabled(false);
                            } else if ((join_value_INT != nc_pay_up_INT)) {
                                nc_des.setEnabled(true);

                                if ((join_value_INT - nc_pay_up_INT) > des_INT) {
                                    is_pay_ok = 1;
                                    nc_des.setTextColor(Color.BLACK);
                                    if (nc_rest_value_INT <= 0) {
                                        nc_rest_value.setText("0");
                                    } else {
                                        nc_rest_value.setText(nc_rest_value_S);

                                    }
                                    nc_desT.setErrorEnabled(false);
                                } else {
                                    nc_des.setTextColor(Color.RED);
                                    nc_desT.setErrorEnabled(true);
                                    nc_desT.setError("الخصم اكبر من المسموح");
                                    is_pay_ok = 0;
                                }
                            }
                        }
                        total_value.setText(s.toString());
                    }


                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        nc_des.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() == 0) {
                    des_INT = 0;
                } else {
                    des_INT = Integer.parseInt(s.toString());

                }

                if (nc_pay_up.getText().toString().length() == 0) {
                    nc_pay_up_INT = 0;

                } else {
                    nc_pay_up_INT = Integer.parseInt(nc_pay_up.getText().toString());
                    ;

                }
                if (nc_join_value.getText().toString().length() == 0) {
                    join_value_INT = 0;
                } else {
                    join_value_INT = Integer.parseInt(nc_join_value.getText().toString());
                    ;

                }
                nc_rest_value_INT = join_value_INT - nc_pay_up_INT - des_INT;
                String nc_rest_value_S = String.valueOf(nc_rest_value_INT);
                if (nc_des.getText().length() > 1 && nc_pay_up.getText().length() > 0) {

                    if ((join_value_INT == nc_pay_up_INT)) {
                        is_pay_ok = 1;
                        nc_des.setTextColor(Color.BLACK);
                        nc_desT.setErrorEnabled(false);
                    } else if ((join_value_INT != nc_pay_up_INT)) {
                        if ((join_value_INT - nc_pay_up_INT) > des_INT && des_INT != 0) {
                            if (nc_rest_value_INT <= 0) {
                                nc_rest_value.setText("0");
                            } else {
                                nc_rest_value.setText(nc_rest_value_S);

                            }
                            is_pay_ok = 1;
                            nc_des.setTextColor(Color.BLACK);
                            nc_desT.setErrorEnabled(false);
                        } else {
                            nc_des.setTextColor(Color.RED);
                            nc_desT.setErrorEnabled(true);
                            nc_desT.setError("الخصم اكبر من المسموح");
                            is_pay_ok = 0;
                        }
                    }

                }
                total_value.setText(String.valueOf(nc_pay_up_INT));

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

//        nc_rest_value.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//
//                if(s.toString().length()==0){
//                    nc_rest_value_INT =0;
//
//                }
//                else {
//                    nc_rest_value_INT=Integer.parseInt(s.toString());
//                    if(nc_rest_value_INT<=0){
//                        nc_rest_value.setText("0");
//                    }
//                }
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

                new NewConActivity.GetReciept_ver().execute();


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

//                if(sentActive==0) {
//                    Toast.makeText(getApplicationContext(),
//                            "قم بالتحقق من صلاحية رقم الإيصال اولا",
//                            Toast.LENGTH_LONG)
//                            .show();
//                }

                if (is_run_date_ok == 0) {


                    nc_running_date.setTextColor(Color.RED);
                    Toast.makeText(NewConActivity.this, "مسموح بإختيار التاريخ خلال 6 اشهر من اليوم ", Toast.LENGTH_SHORT).show();
                    is_run_date_ok = 0;
                    scrollView.scrollTo(0, scrollView.getBottom() / 2);

                }

                if (is_dit_ok == 0) {
                    Toast.makeText(getApplicationContext(),
                            "قم بالتحقق من بيانات الصيدلية الاساسية",
                            Toast.LENGTH_LONG)
                            .show();
                    scrollView.getParent().requestChildFocus(scrollView, scrollView);
                    scrollView.fullScroll(View.FOCUS_UP);
                }
                if (is_pay_ok == 0) {
                    Toast.makeText(getApplicationContext(),
                            "قم بالتحقق من رسوم الصيدلية",
                            Toast.LENGTH_LONG)
                            .show();
                    nc_pay_up.requestFocus();

                    scrollView.scrollTo(0, scrollView.getBottom() / 2);
                }
                if (is_new_code == false) {
                    Toast.makeText(getApplicationContext(),
                            "قم بالتحقق من كود الصيدلية",
                            Toast.LENGTH_LONG)
                            .show();
                    nc_pay_up.requestFocus();

                    scrollView.scrollTo(0, scrollView.getBottom() / 2);
                }

                if (is_dit_ok == 1 && is_pay_ok == 1 && is_new_code == true && is_run_date_ok == 1) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(NewConActivity.this);
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

                                        new NewConActivity.GetReciept_AN_Save_cover_page().execute();
                                    }
                                    if (sentActive == 1 && is_new_code == true) {
                                        new NewConActivity.Save_cover_page().execute();

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
        imm.hideSoftInputFromWindow(nc_ph_name.getWindowToken(), 0);
    }

    private void openKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY); // show
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

    ///////////////////////////////////////////////////////////////////////////////

    private class GetClient_by_DCode extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(NewConActivity.this);
            pDialog.setMessage("إنتظر قليلا...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                WebServiceCall webc = new WebServiceCall(NewConActivity.this);

                //webc.GetClientData(WebServiceCoverData.motaheda_code.toString());
                // webc.GetClientDataMore(WebServiceClientData.code.toString());
                //webc.GetContractTypeDataByCode(WebServiceClientDataMore.cont_kind.toString());
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

                        String ContractTypecode = "";
                        String ContractTypename = "";

                        HashMap<String, String> ContractTypehash = new HashMap<>();

                        ContractTypehash.clear();
                        ContractTypehash.put("code", ContractTypecode);
                        ContractTypehash.put("name", ContractTypename);

                        ContractTypeList.add(ContractTypehash);

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
                    SPsr.removeAllSP(NewConActivity.this);
                    finish();
                    Intent intent = new Intent(NewConActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
            pDialog = new ProgressDialog(NewConActivity.this);
            pDialog.setMessage("إنتظر قليلا...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                WebServiceCall webc = new WebServiceCall(NewConActivity.this);
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
            pDialog = new ProgressDialog(NewConActivity.this);
            pDialog.setMessage("يتم الان التحقق من رقم الإيصال");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                WebServiceResult.ErrorID = 000;

                WebServiceCall webc = new WebServiceCall(NewConActivity.this);
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
            pDialog = new ProgressDialog(NewConActivity.this);
            pDialog.setMessage("يتم الان التحقق من رقم الإيصال");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            WebServiceResult.ErrorID = 000;
            try {
                WebServiceCall webc = new WebServiceCall(NewConActivity.this);
                if (nc_rep_code.getText().toString().length() >= 3) {
                    webc.GetReciept_ver(nc_rep_code.getText().toString());
                }
                if (nc_rep_code.getText().toString().length() < 3) {
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
                    new NewConActivity.Save_cover_page().execute();


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
                            "خطأ في التحقق من رقم الإيصال, الحقل فارغ او اقل من المطلوب",
                            Toast.LENGTH_LONG)
                            .show();
                    sentActive = 0;

                }
                if (WebServiceResult.ErrorID == 000) {
                    Toast.makeText(getApplicationContext(),
                            "خطأ في التحقق من رقم الإيصال , راجع مزود خدمة الإنترنت",
                            Toast.LENGTH_LONG)
                            .show();
                    sentActive = 0;

                }

            } catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }


        }

    }


    private class motaheda_code_vali extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(NewConActivity.this);
            pDialog.setMessage("يتم الان التحقق من رقم الإيصال");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                WebServiceResult.ErrorID = 000;
                WebServiceCall webc = new WebServiceCall(NewConActivity.this);
                if (nc_motaheda_code.getText().toString().length() >= 3) {
                    webc.motaheda_code_vali(nc_motaheda_code.getText().toString());
                } else if (nc_motaheda_code.getText().toString().length() < 3) {
                    WebServiceResult.ErrorID = 111;
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


                if (WebServiceResult.ErrorID == 44) {
                    motaheda_code_vil_btn.setBackgroundColor(Color.GREEN);
                    motaheda_code_vil_btn.setText("تم التحقق من كود الصيدلية");
                    is_new_code = true;
                    Toast.makeText(getApplicationContext(),
                            "هذا الكود غير مسجل لدينا , يمكنك الضغط على انشاء كود جديد و عمل عقد جديد للصيدلية",
                            Toast.LENGTH_LONG)
                            .show();
                } else if (WebServiceResult.ErrorID == 01) {
                    Toast.makeText(getApplicationContext(),
                            "هذا الصيدلية فعالة, لا يمكن انشاء عقد جديد لها",
                            Toast.LENGTH_LONG)
                            .show();
                    is_new_code = false;

                } else if (WebServiceResult.ErrorID == 02) {
                    Toast.makeText(getApplicationContext(),
                            "هذا الصيدلية متوقفة , يمكنك اعادة تفعيلها",
                            Toast.LENGTH_LONG)
                            .show();
                    is_new_code = false;

                } else if (WebServiceResult.ErrorID == 111) {
                    Toast.makeText(getApplicationContext(),
                            "هذا الرقم اقل من المطلوب او الحقل فارغ , بالرجاء المراجعة ثم تحقق من جديد",
                            Toast.LENGTH_LONG)
                            .show();
                    is_new_code = false;

                }

            } catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }


        }

    }


    private class Save_cover_page extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(NewConActivity.this);
            pDialog.setMessage("جاري الان ارسال العقد ...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {


                WebServiceCoverData.ErrorID = 007;

                WebServiceCall webc = new WebServiceCall(NewConActivity.this);
                WebServiceSuperName webServiceSuperName = new WebServiceSuperName();
                webServiceSuperName.ErrorMessage = "";
                webc.GetSuper(empsercode);
                supername = webServiceSuperName.ErrorMessage.toString();
                webc.GetMotaheda_New_Code();
                Motaheda_codeS = WebServiceMotaheda_N_Code.motaheda_code.toString();
                Log.e(TAG, "Motaheda_codeS Motaheda_codeS: " + Motaheda_codeS);
                Log.e(TAG, "empsercode empsercode: " + empsercode);
                Log.e(TAG, "nc_rest_value: " + nc_rest_value.getText().toString());

                if (WebServiceMotaheda_N_Code.ErrorID == 0) {
                    if (supername.length() > 1) {
                        webc.Save_cover_page(Motaheda_codeS, empsercode, nc_ph_doc_name.getText().toString(), emp_name, supername, nc_con_value.getText().toString(), contractTypeCode, nc_pay_up.getText().toString(), nc_rest_value.getText().toString(), nc_running_dateAF, real_Date, phar_typeCode, nc_join_value.getText().toString(), con_note.getText().toString(), nc_ph_ph1.getText().toString(), nc_ph_ph2.getText().toString(), nc_ph_name.getText().toString(), nc_ph_add.getText().toString(), BranchCodeS, nc_des.getText().toString());

                    } else {
                        webc.Save_cover_page(Motaheda_codeS, empsercode, nc_ph_doc_name.getText().toString(), emp_name, emp_name, nc_con_value.getText().toString(), contractTypeCode, nc_pay_up.getText().toString(), nc_rest_value.getText().toString(), nc_running_dateAF, real_Date, phar_typeCode, nc_join_value.getText().toString(), con_note.getText().toString(), nc_ph_ph1.getText().toString(), nc_ph_ph2.getText().toString(), nc_ph_name.getText().toString(), nc_ph_add.getText().toString(), BranchCodeS, nc_des.getText().toString());

                    }

                    if (Motaheda_codeS.length() > 1) {
                        WebServiceClientData.code = "";
                        webc.GetClientData(Motaheda_codeS);
                        P_Code = WebServiceClientData.code.toString();
                        Log.e(TAG, "P_Code P_Code: " + P_Code);

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

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }


            try {

                if (Motaheda_codeS.length() > 1 && WebServiceCoverData.ErrorID == 0 && WebServiceClientData.ErrorID == 0) {
                    new NewConActivity.Ending_New_Con().execute();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "خطأ راجع البيانات الاساسية قد يكون العميل مسجل من قبل",
                            Toast.LENGTH_LONG)
                            .show();
                }

                Log.e(TAG, "WebServiceCoverData.ErrorID: " + String.valueOf(WebServiceCoverData.ErrorID));
                Log.e(TAG, "WebServiceCoverData.ErrorMessage: " + WebServiceCoverData.ErrorMessage + " & m_code:" + Motaheda_codeS);
                Log.e(TAG, "WebServiceClientData.ErrorID: " + String.valueOf(WebServiceClientData.ErrorID));
                Log.e(TAG, "WebServiceClientData.ErrorMessage: " + WebServiceClientData.ErrorMessage);

            } catch (Exception ex) {


                Log.e(TAG, "New_Client onPostExecute Error: " + ex.getMessage());

            }


        }

    }

    private class Ending_New_Con extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog


        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                WebServiceCall webc = new WebServiceCall(NewConActivity.this);
                WebServiceSuperName webServiceSuperName = new WebServiceSuperName();
                webServiceSuperName.ErrorMessage = "";
                WebServiceCoverData.ErrorID = 1;
                WebServiceResult.ErrorID = 007;

                if (Motaheda_codeS.length() > 1) {
                    WebServiceClientData.code = "";
                    webc.GetClientData(Motaheda_codeS);
                    P_Code = WebServiceClientData.code.toString();
                    Log.e(TAG, "P_Code P_Code: " + P_Code);

                }

                if (WebServiceClientData.ErrorID == 0) {
                    webc.Insert_Activate_Data(P_Code);
                    Log.e(TAG, "WebServiceResult.ErrorID1: " + String.valueOf(WebServiceResult.ErrorID));


                    if (phar_typePosition == 1) {
                        webc.Insert_Master_Phar_Mapping(P_Code, nc_ph_name.getText().toString(), Mster_name.getText().toString());
                        Log.e(TAG, "WebServiceResult.ErrorID2: " + String.valueOf(WebServiceResult.ErrorID));

                    }

                    if (paytypeSp_position == 0 || paytypeSp_position == 2) {
                        webc.Insert_Contract_Fess(P_Code, empsercode, nc_rep_code.getText().toString(), nc_pay_up.getText().toString(), paytypeCode, con_note.getText().toString(), chenam.getText().toString(), cheDate_dateAF, "", "downpay");
                        Log.e(TAG, "WebServiceResult.ErrorID3: " + String.valueOf(WebServiceResult.ErrorID));

                        //  webc.Insert_another_fees(P_Code,empsercode,nc_con_value.getText().toString(),total_value.getText().toString(),rec_Date_dateAF,nc_rep_code.getText().toString(),paytypeCode,chenam.getText().toString(),cheDate_dateAF);

                    }
                    if (paytypeSp_position == 1) {
                        webc.Insert_Contract_Fess(P_Code, empsercode, nc_rep_code.getText().toString(), nc_pay_up.getText().toString(), paytypeCode, con_note.getText().toString(), " ", " ", "", "downpay");
                        // webc.Insert_another_fees(P_Code,empsercode,nc_con_value.getText().toString(),total_value.getText().toString(),rec_Date_dateAF,nc_rep_code.getText().toString(),paytypeCode," "," ");
                        Log.e(TAG, "WebServiceResult.ErrorID4: " + String.valueOf(WebServiceResult.ErrorID));
                        Log.e(TAG, "WebServiceResult.ErrorMessage4: " + String.valueOf(WebServiceResult.ErrorMessage));

                    }
                    if (nc_ph_ph1.getText().length() < 1) {

                        PhoneS = nc_ph_ph2.getText().toString();
                    }
                    if (nc_ph_ph2.getText().length() < 1) {
                        PhoneS = nc_ph_ph1.getText().toString();
                    }
                    Log.e(TAG, "WebServiceResult.ErrorID5: " + String.valueOf(WebServiceResult.ErrorID));

                    if (WebServiceResult.ErrorID == 0) {
                        webc.Insert_Emp_Daily_Report(nc_rep_code.getText().toString(), P_Code, nc_ph_doc_name.getText().toString(), con_note.getText().toString(), nc_ph_name.getText().toString(), PhoneS, BranchCodeS);
                        webc.Insert_Reciepts_Data(nc_rep_code.getText().toString(), rec_Date.getText().toString());
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
                Log.e(TAG, "WebServiceResult.ErrorID.Final: " + String.valueOf(WebServiceResult.ErrorID));

                if (WebServiceResult.ErrorID == 0) {


                    AlertDialog.Builder builder1 = new AlertDialog.Builder(NewConActivity.this);
                    builder1.setMessage("تمت العملية بنجاح");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton(
                            "انشاء عقد جديد بنفس الفرع",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    finish();
                                    Intent i = new Intent(NewConActivity.this, NewConActivity.class);
                                    i.putExtra("BranchCodeToActivity", BranchCodeS);
                                    i.putExtra("ToActivity", "2");
                                    startActivity(i);

                                }
                            });

                    builder1.setNegativeButton(
                            "اختيار فرع اخر",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                    Intent i = new Intent(NewConActivity.this, BranchsActivity.class);
                                    i.putExtra("ToActivity", "2");
                                    i.putExtra("activation_flag", "a");
                                    startActivity(i);
                                }
                            });
                    builder1.setNeutralButton(
                            "خروج",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                    Intent i = new Intent(NewConActivity.this, MainActivity.class);

                                    startActivity(i);
                                }
                            });


                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "خطأ راحع البيانات",
                            Toast.LENGTH_LONG)
                            .show();
                }


                Log.e(TAG, "supername supername: " + supername);

            } catch (Exception ex) {


                Log.e(TAG, "New_Client onPostExecute Error: " + ex.getMessage());

            }


        }

    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}
