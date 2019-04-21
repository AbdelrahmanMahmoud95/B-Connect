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
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class InstallActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private String TAG = "SR: "+InstallActivity.class.getSimpleName()+": ";
    private int sentActive,is_down_ok,is_reused ;

    SharedPreferences pref ;
    SharedPreferences.Editor editor;
    String  empsercode,visit_noS,yearS,monthS;

    private ProgressDialog pDialog;


    TextView banknameTit,FMTit,yearFMTit,textView9,paytypeTit;
    TextInputLayout cheDateT,install_first_month_valueT,rec_DateT,install_down_payT;

    String  emp_name,pharm_nameS,BranchCodeS;
    String   banknameCode,paytypeCode,P_Code ,doc_nameS;
    String   rec_Date_dateAF,instal_visit_dateAF,cheDate_dateAF,real_Date,real_Date2,month_valueS ;

    EditText nc_rep_code,rec_Date,cheDate,chenam,con_note,install_down_pay,install_first_month_value,instal_visit_date;

    Intent intent;

    int  paytypeSp_position,IS_instal_down_pay,IS_instal_first_m_sw;

    SimpleDateFormat rec_Date_SDF,cheDate_SDF,instal_visit_date_SDF;

    Button nc_sendbut,nc_rep_code_vil_but,install_down_con_send,install_monthly_send;

    Switch instal_down_pay_sw,instal_first_m_sw;

    private static String paytypeUrl,banknameUrl;

    Spinner paytypeSp,banknameSp,FMSp,yearFMSp;


    List<HashMap<String, String>> paytypeList,banknameList;
    List<String> FMSpList,yearFMSpList;
    ArrayAdapter<String> FMSpadapter,yearFMSpadapter;

    private SimpleAdapter  paytypeSAdapter,banknameSAdapter ;

    Calendar rec_Date_myCalendar,cheDate_myCalendar,real_Date_myCalendar,instal_visit_date_myCalendar;



    NestedScrollView scrollView;

    SharedPreferencesSR SPsr=new SharedPreferencesSR();

    Switch sup_sw_rec_code_reuse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install);

        basic();
        control();

        new InstallActivity.GetClient_by_DCode().execute();
        new InstallActivity.GetClientData_Master_Code().execute();


    }




    public  void basic(){

        scrollView= (NestedScrollView) findViewById(R.id.Install_NS);
        sup_sw_rec_code_reuse=(Switch) findViewById(R.id.sup_sw_rec_code_reuse);



        IS_instal_down_pay=0;
        IS_instal_first_m_sw=0;
        is_down_ok=0;
        instal_down_pay_sw=(Switch) findViewById(R.id.instal_down_pay_sw);
        instal_first_m_sw=(Switch) findViewById(R.id.instal_first_m_sw);


        banknameTit=(TextView)findViewById(R.id.banknameTit);
        FMTit=(TextView)findViewById(R.id.FMTit);
        yearFMTit=(TextView)findViewById(R.id.yearFMTit);
        textView9=(TextView)findViewById(R.id.textView9);
        paytypeTit=(TextView)findViewById(R.id.paytypeTit);



        cheDateT=(TextInputLayout) findViewById(R.id.cheDateT);
        install_first_month_valueT=(TextInputLayout) findViewById(R.id.install_first_month_valueT);
        rec_DateT=(TextInputLayout) findViewById(R.id.rec_DateT);
        install_down_payT=(TextInputLayout) findViewById(R.id.install_down_payT);

        nc_sendbut=(Button) findViewById(R.id.nc_sendbut);
        nc_rep_code_vil_but=(Button) findViewById(R.id.nc_rep_code_vil_but);
        install_down_con_send=(Button) findViewById(R.id.install_down_con_send);
        install_monthly_send=(Button) findViewById(R.id.install_monthly_send);

        nc_rep_code=(EditText) findViewById(R.id.nc_rep_code);
        rec_Date=(EditText) findViewById(R.id.rec_Date);
        cheDate=(EditText) findViewById(R.id.cheDate);
        chenam=(EditText) findViewById(R.id.chenam);
        con_note=(EditText) findViewById(R.id.con_note);
        instal_visit_date=(EditText) findViewById(R.id.instal_visit_date);
        install_first_month_value=(EditText) findViewById(R.id.install_first_month_value);
        install_down_pay=(EditText) findViewById(R.id.install_down_pay);





        paytypeUrl = "http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/GetPayWay";
        banknameUrl = "http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/GetBank";


        paytypeSp = (Spinner) findViewById(R.id.paytypeSp);
        banknameSp = (Spinner) findViewById(R.id.banknameSp);
        FMSp  = (Spinner) findViewById(R.id.FMSp);
        yearFMSp  = (Spinner) findViewById(R.id.yearFMSp);


        FMSpList = new ArrayList<String> (Arrays.asList(getResources().getStringArray(R.array.MonthArray)));
        FMSpadapter = new ArrayAdapter<String>(this,R.layout.sr_spinner_item,R.id.tvName, FMSpList);
        FMSp.setAdapter(FMSpadapter);
        int thisMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
        final int pos1=FMSpList.indexOf(String.valueOf(thisMonth));
        FMSp.post(new Runnable() {
            @Override
            public void run() {
                FMSp.setSelection(pos1);
            }
        });


        yearFMSpList = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear-10; i <= thisYear+1; i++) {
            yearFMSpList.add(Integer.toString(i));
        }
        yearFMSpadapter = new ArrayAdapter<String>(this,R.layout.sr_spinner_item,R.id.tvName, yearFMSpList);

        yearFMSp.setAdapter(yearFMSpadapter);

        final int pos2=yearFMSpList.indexOf(String.valueOf(thisYear));
        yearFMSp.post(new Runnable() {
            @Override
            public void run() {
                yearFMSp.setSelection(pos2);
            }
        });



        paytypeList = new ArrayList<HashMap<String, String>>();
        banknameList = new ArrayList<HashMap<String, String>>();

        paytypeSAdapter = new SimpleAdapter(this, paytypeList, R.layout.pay_way_spinner_item,
                new String[] { "name" }, new int[] { R.id.tvName });
        banknameSAdapter = new SimpleAdapter(this, banknameList, R.layout.bank_name_spinner_item,
                new String[] { "name" }, new int[] { R.id.tvName });


        real_Date_myCalendar = Calendar.getInstance();
        rec_Date_myCalendar = Calendar.getInstance();
        cheDate_myCalendar = Calendar.getInstance();
        instal_visit_date_myCalendar = Calendar.getInstance();

        SimpleDateFormat real_DateSDF2 = new SimpleDateFormat("yyyy/dd/MM");
        SimpleDateFormat real_DateSDF = new SimpleDateFormat("yyyy/MM/dd");
        real_Date = real_DateSDF2.format( real_Date_myCalendar.getTime()).toString();
        real_Date2= real_DateSDF.format( real_Date_myCalendar.getTime()).toString();
        rec_Date_updateData();
        cheDate_updateData();
        instal_visit_date_updateData();
        sentActive=0;

        pref= getApplicationContext().getSharedPreferences("MySr", MODE_PRIVATE);
        editor = pref.edit();

        try{
            intent = getIntent();
            empsercode="";
            empsercode=SPsr.getSP(getApplicationContext(),"empsercode");
            emp_name="";
            emp_name=SPsr.getSP(getApplicationContext(),"empsername");
            P_Code="";
            P_Code = intent.getExtras().getString("P_Code");

        }catch (Exception ex){
         super.onBackPressed();
        }


        doc_nameS="";


        is_reused=0;


    }

    public  void control(){


        sup_sw_rec_code_reuse.setChecked(false);


        sup_sw_rec_code_reuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sup_sw_rec_code_reuse.isChecked()){
                    is_reused=1;
                    sup_sw_rec_code_reuse.setTextColor(Color.BLUE);
                }else {
                    is_reused=0;
                    sup_sw_rec_code_reuse.setTextColor(Color.RED);


                }
            }
        });


        instal_first_m_sw.setChecked(false);
        instal_first_m_sw.setChecked(false);

        if(instal_down_pay_sw.isChecked()&&!instal_first_m_sw.isChecked()){
            IS_instal_down_pay=1;
            sentActive=0;
            IS_instal_first_m_sw=0;

            sup_sw_rec_code_reuse.setVisibility(View.VISIBLE);
            install_down_payT.setVisibility(View.VISIBLE);
            install_down_con_send.setVisibility(View.VISIBLE);
            install_down_pay.setVisibility(View.VISIBLE);
            install_first_month_value.setVisibility(View.GONE);
            install_first_month_valueT.setVisibility(View.GONE);
            install_monthly_send.setVisibility(View.GONE);

            FMTit.setVisibility(View.GONE);
            FMSp.setVisibility(View.GONE);
            yearFMTit.setVisibility(View.GONE);
            yearFMSp.setVisibility(View.GONE);

            textView9.setVisibility(View.VISIBLE);
            nc_rep_code.setVisibility(View.VISIBLE);
            nc_rep_code_vil_but.setVisibility(View.VISIBLE);
            rec_Date.setVisibility(View.VISIBLE);
            paytypeTit.setVisibility(View.VISIBLE);
            paytypeSp.setVisibility(View.VISIBLE);

            if(paytypeSp_position==0||paytypeSp_position==2){
                banknameTit.setVisibility(View.VISIBLE);
                banknameSp.setVisibility(View.VISIBLE);
                chenam.setVisibility(View.VISIBLE);
                cheDate.setVisibility(View.VISIBLE);
                cheDateT.setVisibility(View.VISIBLE);
            }




        }
        else  if(!instal_down_pay_sw.isChecked()&&instal_first_m_sw.isChecked()){
            IS_instal_down_pay=0;
            sentActive=0;
            IS_instal_first_m_sw=1;
            sup_sw_rec_code_reuse.setVisibility(View.VISIBLE);

            install_down_payT.setVisibility(View.GONE);
            install_down_con_send.setVisibility(View.GONE);
            install_down_pay.setVisibility(View.GONE);
            install_monthly_send.setVisibility(View.VISIBLE);
            install_first_month_value.setVisibility(View.VISIBLE);
            install_first_month_valueT.setVisibility(View.VISIBLE);

            FMTit.setVisibility(View.VISIBLE);
            FMSp.setVisibility(View.VISIBLE);
            yearFMTit.setVisibility(View.VISIBLE);
            yearFMSp.setVisibility(View.VISIBLE);

            textView9.setVisibility(View.VISIBLE);
            nc_rep_code.setVisibility(View.VISIBLE);
            nc_rep_code_vil_but.setVisibility(View.VISIBLE);
            rec_Date.setVisibility(View.VISIBLE);
            paytypeTit.setVisibility(View.VISIBLE);
            paytypeSp.setVisibility(View.VISIBLE);

            if(paytypeSp_position==0||paytypeSp_position==2){
                banknameTit.setVisibility(View.VISIBLE);
                banknameSp.setVisibility(View.VISIBLE);
                chenam.setVisibility(View.VISIBLE);
                cheDate.setVisibility(View.VISIBLE);
                cheDateT.setVisibility(View.VISIBLE);
            }



        }
        else  if(instal_down_pay_sw.isChecked()&&instal_first_m_sw.isChecked()){
            IS_instal_down_pay=1;
            sentActive=0;
            IS_instal_first_m_sw=1;
            sup_sw_rec_code_reuse.setVisibility(View.VISIBLE);

           nc_sendbut.setText("تحصيل و إنهاء التدريب");
            install_down_con_send.setVisibility(View.GONE);
            install_monthly_send.setVisibility(View.GONE);
            install_down_payT.setVisibility(View.VISIBLE);
            install_down_pay.setVisibility(View.VISIBLE);
            install_first_month_value.setVisibility(View.VISIBLE);
            install_first_month_valueT.setVisibility(View.VISIBLE);

            FMTit.setVisibility(View.VISIBLE);
            FMSp.setVisibility(View.VISIBLE);
            yearFMTit.setVisibility(View.VISIBLE);
            yearFMSp.setVisibility(View.VISIBLE);

            textView9.setVisibility(View.VISIBLE);
            nc_rep_code.setVisibility(View.VISIBLE);
            nc_rep_code_vil_but.setVisibility(View.VISIBLE);
            rec_Date.setVisibility(View.VISIBLE);
            paytypeTit.setVisibility(View.VISIBLE);
            paytypeSp.setVisibility(View.VISIBLE);

            if(paytypeSp_position==0||paytypeSp_position==2){
                banknameTit.setVisibility(View.VISIBLE);
                banknameSp.setVisibility(View.VISIBLE);
                chenam.setVisibility(View.VISIBLE);
                cheDate.setVisibility(View.VISIBLE);
                cheDateT.setVisibility(View.VISIBLE);
            }



        }
        else  if(!instal_down_pay_sw.isChecked()&&!instal_first_m_sw.isChecked()){
            IS_instal_down_pay=0;
            IS_instal_first_m_sw=0;
            sentActive=1;
            sup_sw_rec_code_reuse.setVisibility(View.GONE);

            nc_sendbut.setText("إنهاء التدريب فقط");
            install_down_payT.setVisibility(View.GONE);
            install_down_con_send.setVisibility(View.GONE);
            install_down_pay.setVisibility(View.GONE);
            install_first_month_value.setVisibility(View.GONE);
            install_first_month_valueT.setVisibility(View.GONE);
            install_monthly_send.setVisibility(View.GONE);
            FMTit.setVisibility(View.GONE);
            FMSp.setVisibility(View.GONE);
            yearFMTit.setVisibility(View.GONE);
            yearFMSp.setVisibility(View.GONE);
            textView9.setVisibility(View.GONE);
            nc_rep_code.setVisibility(View.GONE);
            nc_rep_code_vil_but.setVisibility(View.GONE);
            rec_Date.setVisibility(View.GONE);
            rec_DateT.setVisibility(View.GONE);
            paytypeTit.setVisibility(View.GONE);
            paytypeSp.setVisibility(View.GONE);

            banknameTit.setVisibility(View.GONE);
            banknameSp.setVisibility(View.GONE);
            chenam.setVisibility(View.GONE);
            cheDate.setVisibility(View.GONE);
            cheDateT.setVisibility(View.GONE);



        }





        instal_down_pay_sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(instal_down_pay_sw.isChecked()&&!instal_first_m_sw.isChecked()){
                    IS_instal_down_pay=1;
                    sentActive=0;
                    IS_instal_first_m_sw=0;
                    sup_sw_rec_code_reuse.setVisibility(View.VISIBLE);

                    install_down_payT.setVisibility(View.VISIBLE);
                    install_down_con_send.setVisibility(View.VISIBLE);
                    install_down_pay.setVisibility(View.VISIBLE);
                    install_first_month_value.setVisibility(View.GONE);
                    install_first_month_valueT.setVisibility(View.GONE);
                    install_monthly_send.setVisibility(View.GONE);

                    FMTit.setVisibility(View.GONE);
                    FMSp.setVisibility(View.GONE);
                    yearFMTit.setVisibility(View.GONE);
                    yearFMSp.setVisibility(View.GONE);

                    textView9.setVisibility(View.VISIBLE);
                    nc_rep_code.setVisibility(View.VISIBLE);
                    nc_rep_code_vil_but.setVisibility(View.VISIBLE);
                    rec_Date.setVisibility(View.VISIBLE);
                    paytypeTit.setVisibility(View.VISIBLE);
                    paytypeSp.setVisibility(View.VISIBLE);

                    if(paytypeSp_position==0||paytypeSp_position==2){
                        banknameTit.setVisibility(View.VISIBLE);
                        banknameSp.setVisibility(View.VISIBLE);
                        chenam.setVisibility(View.VISIBLE);
                        cheDate.setVisibility(View.VISIBLE);
                        cheDateT.setVisibility(View.VISIBLE);
                    }




                }
                else  if(!instal_down_pay_sw.isChecked()&&instal_first_m_sw.isChecked()){
                    IS_instal_down_pay=0;
                    sentActive=0;
                    IS_instal_first_m_sw=1;
                    sup_sw_rec_code_reuse.setVisibility(View.VISIBLE);

                    install_down_payT.setVisibility(View.GONE);
                    install_down_con_send.setVisibility(View.GONE);
                    install_down_pay.setVisibility(View.GONE);
                    install_monthly_send.setVisibility(View.VISIBLE);
                    install_first_month_value.setVisibility(View.VISIBLE);
                    install_first_month_valueT.setVisibility(View.VISIBLE);

                    FMTit.setVisibility(View.VISIBLE);
                    FMSp.setVisibility(View.VISIBLE);
                    yearFMTit.setVisibility(View.VISIBLE);
                    yearFMSp.setVisibility(View.VISIBLE);

                    textView9.setVisibility(View.VISIBLE);
                    nc_rep_code.setVisibility(View.VISIBLE);
                    nc_rep_code_vil_but.setVisibility(View.VISIBLE);
                    rec_Date.setVisibility(View.VISIBLE);
                    paytypeTit.setVisibility(View.VISIBLE);
                    paytypeSp.setVisibility(View.VISIBLE);

                    if(paytypeSp_position==0||paytypeSp_position==2){
                        banknameTit.setVisibility(View.VISIBLE);
                        banknameSp.setVisibility(View.VISIBLE);
                        chenam.setVisibility(View.VISIBLE);
                        cheDate.setVisibility(View.VISIBLE);
                        cheDateT.setVisibility(View.VISIBLE);
                    }



                }
                else  if(instal_down_pay_sw.isChecked()&&instal_first_m_sw.isChecked()){
                    IS_instal_down_pay=1;
                    sentActive=0;
                    IS_instal_first_m_sw=1;
                    sup_sw_rec_code_reuse.setVisibility(View.VISIBLE);

                   nc_sendbut.setText("تحصيل و إنهاء التدريب");
                    install_down_con_send.setVisibility(View.GONE);
                    install_monthly_send.setVisibility(View.GONE);
                    install_down_payT.setVisibility(View.VISIBLE);
                    install_down_pay.setVisibility(View.VISIBLE);
                    install_first_month_value.setVisibility(View.VISIBLE);
                    install_first_month_valueT.setVisibility(View.VISIBLE);

                    FMTit.setVisibility(View.VISIBLE);
                    FMSp.setVisibility(View.VISIBLE);
                    yearFMTit.setVisibility(View.VISIBLE);
                    yearFMSp.setVisibility(View.VISIBLE);

                    textView9.setVisibility(View.VISIBLE);
                    nc_rep_code.setVisibility(View.VISIBLE);
                    nc_rep_code_vil_but.setVisibility(View.VISIBLE);
                    rec_Date.setVisibility(View.VISIBLE);
                    paytypeTit.setVisibility(View.VISIBLE);
                    paytypeSp.setVisibility(View.VISIBLE);

                    if(paytypeSp_position==0||paytypeSp_position==2){
                        banknameTit.setVisibility(View.VISIBLE);
                        banknameSp.setVisibility(View.VISIBLE);
                        chenam.setVisibility(View.VISIBLE);
                        cheDate.setVisibility(View.VISIBLE);
                        cheDateT.setVisibility(View.VISIBLE);
                    }



                }
                else  if(!instal_down_pay_sw.isChecked()&&!instal_first_m_sw.isChecked()){
                    IS_instal_down_pay=0;
                    IS_instal_first_m_sw=0;
                    sentActive=1;
                    sup_sw_rec_code_reuse.setVisibility(View.GONE);

                    nc_sendbut.setText("إنهاء التدريب فقط");
                    install_down_payT.setVisibility(View.GONE);
                    install_down_con_send.setVisibility(View.GONE);
                    install_down_pay.setVisibility(View.GONE);
                    install_first_month_value.setVisibility(View.GONE);
                    install_first_month_valueT.setVisibility(View.GONE);
                    install_monthly_send.setVisibility(View.GONE);
                    FMTit.setVisibility(View.GONE);
                    FMSp.setVisibility(View.GONE);
                    yearFMTit.setVisibility(View.GONE);
                    yearFMSp.setVisibility(View.GONE);
                    textView9.setVisibility(View.GONE);
                    nc_rep_code.setVisibility(View.GONE);
                    nc_rep_code_vil_but.setVisibility(View.GONE);
                    rec_Date.setVisibility(View.GONE);
                    rec_DateT.setVisibility(View.GONE);
                    paytypeTit.setVisibility(View.GONE);
                    paytypeSp.setVisibility(View.GONE);

                    banknameTit.setVisibility(View.GONE);
                    banknameSp.setVisibility(View.GONE);
                    chenam.setVisibility(View.GONE);
                    cheDate.setVisibility(View.GONE);
                    cheDateT.setVisibility(View.GONE);



                }



            }
        });
        instal_first_m_sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(instal_down_pay_sw.isChecked()&&!instal_first_m_sw.isChecked()){
                    IS_instal_down_pay=1;
                    sentActive=0;
                    IS_instal_first_m_sw=0;
                    sup_sw_rec_code_reuse.setVisibility(View.VISIBLE);

                    install_down_payT.setVisibility(View.VISIBLE);
                    install_down_con_send.setVisibility(View.VISIBLE);
                    install_down_pay.setVisibility(View.VISIBLE);
                    install_first_month_value.setVisibility(View.GONE);
                    install_first_month_valueT.setVisibility(View.GONE);
                    install_monthly_send.setVisibility(View.GONE);

                    FMTit.setVisibility(View.GONE);
                    FMSp.setVisibility(View.GONE);
                    yearFMTit.setVisibility(View.GONE);
                    yearFMSp.setVisibility(View.GONE);

                    textView9.setVisibility(View.VISIBLE);
                    nc_rep_code.setVisibility(View.VISIBLE);
                    nc_rep_code_vil_but.setVisibility(View.VISIBLE);
                    rec_Date.setVisibility(View.VISIBLE);
                    paytypeTit.setVisibility(View.VISIBLE);
                    paytypeSp.setVisibility(View.VISIBLE);

                    if(paytypeSp_position==0||paytypeSp_position==2){
                        banknameTit.setVisibility(View.VISIBLE);
                        banknameSp.setVisibility(View.VISIBLE);
                        chenam.setVisibility(View.VISIBLE);
                        cheDate.setVisibility(View.VISIBLE);
                        cheDateT.setVisibility(View.VISIBLE);
                    }




                }
                else  if(!instal_down_pay_sw.isChecked()&&instal_first_m_sw.isChecked()){
                    IS_instal_down_pay=0;
                    sentActive=0;
                    IS_instal_first_m_sw=1;
                    sup_sw_rec_code_reuse.setVisibility(View.VISIBLE);

                    install_down_payT.setVisibility(View.GONE);
                    install_down_con_send.setVisibility(View.GONE);
                    install_down_pay.setVisibility(View.GONE);
                    install_monthly_send.setVisibility(View.VISIBLE);
                    install_first_month_value.setVisibility(View.VISIBLE);
                    install_first_month_valueT.setVisibility(View.VISIBLE);

                    FMTit.setVisibility(View.VISIBLE);
                    FMSp.setVisibility(View.VISIBLE);
                    yearFMTit.setVisibility(View.VISIBLE);
                    yearFMSp.setVisibility(View.VISIBLE);

                    textView9.setVisibility(View.VISIBLE);
                    nc_rep_code.setVisibility(View.VISIBLE);
                    nc_rep_code_vil_but.setVisibility(View.VISIBLE);
                    rec_Date.setVisibility(View.VISIBLE);
                    paytypeTit.setVisibility(View.VISIBLE);
                    paytypeSp.setVisibility(View.VISIBLE);

                    if(paytypeSp_position==0||paytypeSp_position==2){
                        banknameTit.setVisibility(View.VISIBLE);
                        banknameSp.setVisibility(View.VISIBLE);
                        chenam.setVisibility(View.VISIBLE);
                        cheDate.setVisibility(View.VISIBLE);
                        cheDateT.setVisibility(View.VISIBLE);
                    }



                }
                else  if(instal_down_pay_sw.isChecked()&&instal_first_m_sw.isChecked()){
                    IS_instal_down_pay=1;
                    sentActive=0;
                    IS_instal_first_m_sw=1;
                    sup_sw_rec_code_reuse.setVisibility(View.VISIBLE);

                   nc_sendbut.setText("تحصيل و إنهاء التدريب");
                    install_down_con_send.setVisibility(View.GONE);
                    install_monthly_send.setVisibility(View.GONE);
                    install_down_payT.setVisibility(View.VISIBLE);
                    install_down_pay.setVisibility(View.VISIBLE);
                    install_first_month_value.setVisibility(View.VISIBLE);
                    install_first_month_valueT.setVisibility(View.VISIBLE);

                    FMTit.setVisibility(View.VISIBLE);
                    FMSp.setVisibility(View.VISIBLE);
                    yearFMTit.setVisibility(View.VISIBLE);
                    yearFMSp.setVisibility(View.VISIBLE);

                    textView9.setVisibility(View.VISIBLE);
                    nc_rep_code.setVisibility(View.VISIBLE);
                    nc_rep_code_vil_but.setVisibility(View.VISIBLE);
                    rec_Date.setVisibility(View.VISIBLE);
                    paytypeTit.setVisibility(View.VISIBLE);
                    paytypeSp.setVisibility(View.VISIBLE);

                    if(paytypeSp_position==0||paytypeSp_position==2){
                        banknameTit.setVisibility(View.VISIBLE);
                        banknameSp.setVisibility(View.VISIBLE);
                        chenam.setVisibility(View.VISIBLE);
                        cheDate.setVisibility(View.VISIBLE);
                        cheDateT.setVisibility(View.VISIBLE);
                    }



                }
                else  if(!instal_down_pay_sw.isChecked()&&!instal_first_m_sw.isChecked()){
                    IS_instal_down_pay=0;
                    IS_instal_first_m_sw=0;
                    sentActive=1;
                    sup_sw_rec_code_reuse.setVisibility(View.GONE);

                    nc_sendbut.setText("إنهاء التدريب فقط");
                    install_down_payT.setVisibility(View.GONE);
                    install_down_con_send.setVisibility(View.GONE);
                    install_down_pay.setVisibility(View.GONE);
                    install_first_month_value.setVisibility(View.GONE);
                    install_first_month_valueT.setVisibility(View.GONE);
                    install_monthly_send.setVisibility(View.GONE);
                    FMTit.setVisibility(View.GONE);
                    FMSp.setVisibility(View.GONE);
                    yearFMTit.setVisibility(View.GONE);
                    yearFMSp.setVisibility(View.GONE);
                    textView9.setVisibility(View.GONE);
                    nc_rep_code.setVisibility(View.GONE);
                    nc_rep_code_vil_but.setVisibility(View.GONE);
                    rec_Date.setVisibility(View.GONE);
                    rec_DateT.setVisibility(View.GONE);
                    paytypeTit.setVisibility(View.GONE);
                    paytypeSp.setVisibility(View.GONE);

                    banknameTit.setVisibility(View.GONE);
                    banknameSp.setVisibility(View.GONE);
                    chenam.setVisibility(View.GONE);
                    cheDate.setVisibility(View.GONE);
                    cheDateT.setVisibility(View.GONE);



                }




            }
        });




        install_first_month_value.setEnabled(false);

        final DatePickerDialog.OnDateSetListener instal_visit_date_datepicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                instal_visit_date_myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                instal_visit_date_myCalendar.set(Calendar.MONTH, monthOfYear);
                instal_visit_date_myCalendar.set(Calendar.YEAR, year);

                instal_visit_date_updateData();

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

        instal_visit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(InstallActivity.this, instal_visit_date_datepicker,
                        rec_Date_myCalendar.get(Calendar.YEAR),
                        rec_Date_myCalendar.get(Calendar.MONTH),
                        rec_Date_myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
        rec_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(InstallActivity.this, rec_Date_datepicker,
                        rec_Date_myCalendar.get(Calendar.YEAR),
                        rec_Date_myCalendar.get(Calendar.MONTH),
                        rec_Date_myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
        cheDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(InstallActivity.this, cheDate_datepicker,
                        cheDate_myCalendar.get(Calendar.YEAR),
                        cheDate_myCalendar.get(Calendar.MONTH),
                        cheDate_myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });


        yearFMSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {

                 yearS = adapter.getAdapter().getItem(position).toString();
                Log.e(TAG, "yearS yearS: " + yearS);

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(InstallActivity.this, "تذكر ان تختار السنة " , Toast.LENGTH_SHORT).show();

            }
        });

        FMSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {

                monthS = adapter.getAdapter().getItem(position).toString();
                Log.e(TAG, "monthS monthS: " + monthS);

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(InstallActivity.this, "تذكر ان تختار نوع التحصيل " , Toast.LENGTH_SHORT).show();

            }
        });


        paytypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {

                HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getAdapter().getItem(position);
                paytypeCode = (String) obj.get("code");
                paytypeSp_position=position;
                if(position==0){
                    Toast.makeText(InstallActivity.this, "تحصيل بشيك " , Toast.LENGTH_SHORT).show();

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



                    Toast.makeText(InstallActivity.this, "تحصيل نقدي" , Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(InstallActivity.this, "تحصيل بحوالة بنكية" , Toast.LENGTH_SHORT).show();

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
                Toast.makeText(InstallActivity.this, "تذكر ان تختار نوع التحصيل " , Toast.LENGTH_SHORT).show();

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
                Toast.makeText(InstallActivity.this, "تذكر ان تختار نوع التحصيل " , Toast.LENGTH_SHORT).show();

            }
        });

        nc_rep_code_vil_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new InstallActivity.GetReciept_ver().execute();
            }
        });



        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(IS_instal_down_pay==1){
                    if(install_down_pay.getText().toString().length()<=2){

                        install_down_pay.setTextColor(Color.RED);
                        is_down_ok=0;
                    }
                    else {
                        install_down_pay.setTextColor(Color.BLACK);
                        is_down_ok=1;
                    }
                }
                else {
                    is_down_ok=1;

                }

                closeKeyboard();
                return false;

            }
        });



        install_down_pay.setText("0");
        install_down_pay.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus){

                    if(install_down_pay.getText().length()==0){

                        install_down_pay.setText("0");

                    }

                }
                else {
                    if(install_down_pay.getText().toString().length()==1){
                        install_down_pay.setText("");
                    }
                }
                // TODO: the editText has just been left
            }
        });

        install_down_pay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(IS_instal_down_pay==1){
                    if(install_down_pay.getText().toString().length()<=2){

                        install_down_pay.setTextColor(Color.RED);
                        is_down_ok=0;
                    }
                    else {
                        install_down_pay.setTextColor(Color.BLACK);
                        is_down_ok=1;
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });


        //SR: Start send data


        install_down_con_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(IS_instal_down_pay==1){
                   if(is_down_ok==0) {

                       Toast.makeText(getApplicationContext(),
                               "قم بالتحقق من باقي التعاقد",
                               Toast.LENGTH_LONG)
                               .show();
                       scrollView.getParent().requestChildFocus(scrollView, scrollView);
                       scrollView.fullScroll(View.FOCUS_UP);
                       install_down_pay.requestFocus();

                   }
               }
                if(IS_instal_down_pay==1){
                    if(is_down_ok==1) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(InstallActivity.this);
                        if(sentActive==0) {

                            builder1.setMessage("سيتم الان التحقق من رقم الإيصال: "
                                    +nc_rep_code.getText().toString()+"\n"
                                    +"\n"
                                    +"\nتأكيد تحصيل باقي التعاقد فقط؟");

                        }
                        if(sentActive==1) {

                            builder1.setMessage(
                                    "\nتأكيد تحصيل باقي التعاقد فقط؟");

                        }
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "نعم",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {


                                        if(sentActive==0) {

                                            new InstallActivity.GetReciept_AND_Save().execute();
                                        }
                                        if(sentActive==1) {
                                            new InstallActivity.Save_down_only().execute();

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



            }
        });
        install_monthly_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                AlertDialog.Builder builder1 = new AlertDialog.Builder(InstallActivity.this);
                if(sentActive==0) {

                    builder1.setMessage("سيتم الان التحقق من رقم الإيصال: "
                            +nc_rep_code.getText().toString()+"\n"
                            +"\n"
                            +"\nتأكيد تحصيل الإشتراك فقط؟");

                }
                if(sentActive==1) {

                    builder1.setMessage(
                            "\nتأكيد تحصيل الإشتراك فقط؟");

                }
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "نعم",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                if(sentActive==0) {

                                    new InstallActivity.GetReciept_AND_Save().execute();
                                }
                                if(sentActive==1) {
                                    new InstallActivity.Save_monthly_only().execute();

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
        nc_sendbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(IS_instal_down_pay==1){
                    if(is_down_ok==0) {

                        Toast.makeText(getApplicationContext(),
                                "قم بالتحقق من باقي التعاقد",
                                Toast.LENGTH_LONG)
                                .show();
                        scrollView.getParent().requestChildFocus(scrollView, scrollView);
                        scrollView.fullScroll(View.FOCUS_UP);
                        install_down_pay.requestFocus();

                    }
                }
                if(IS_instal_down_pay==1){
                    if(is_down_ok==1) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(InstallActivity.this);
                        if(sentActive==0) {

                            builder1.setMessage("سيتم الان التحقق من رقم الإيصال: "
                                    +nc_rep_code.getText().toString()+"\n"
                                    +"\n"
                                    +"\nتأكيد التحصيل و التدريب؟");

                        }
                        if(sentActive==1) {

                            builder1.setMessage(
                                    "\nتأكيد التدريب؟");

                        }
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "تأكيد",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {


                                        if(sentActive==0) {

                                            new InstallActivity.GetReciept_AND_Save().execute();
                                        }
                                        if(sentActive==1) {
                                            new InstallActivity.Save_Trane_Client().execute();

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
                else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(InstallActivity.this);
                    if(sentActive==0) {

                        builder1.setMessage("سيتم الان التحقق من رقم الإيصال: "
                                +nc_rep_code.getText().toString()+"\n"
                                +"\n"
                                +"\nتأكيد التحصيل و التدريب؟");

                    }
                    if(sentActive==1) {

                        builder1.setMessage(
                                "\nتأكيد التدريب؟");

                    }
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "تأكيد",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    if(sentActive==0) {

                                        new InstallActivity.GetReciept_AND_Save().execute();
                                    }
                                    if(sentActive==1) {
                                        new InstallActivity.Save_Trane_Client().execute();

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

    private void  instal_visit_date_updateData() {

        try {

            instal_visit_date_SDF = new SimpleDateFormat("yyyy/MM/dd");
            instal_visit_dateAF = instal_visit_date_SDF.format( instal_visit_date_myCalendar.getTime()).toString();
            SimpleDateFormat instal_visit_date_SDF2 = new SimpleDateFormat("yyyy/MM/dd");
            String instal_visit_dateAF2 = instal_visit_date_SDF2.format( instal_visit_date_myCalendar.getTime()).toString();
            instal_visit_date.setText(instal_visit_dateAF2);

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
            pDialog = new ProgressDialog(InstallActivity.this);
            pDialog.setMessage("يتم الان التحقق من رقم الإيصال");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {
                WebServiceCall webc =new WebServiceCall(InstallActivity.this);
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
                    sentActive=0;

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
    private class GetReciept_AND_Save extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(InstallActivity.this);
            pDialog.setMessage("يتم الان التحقق من رقم الإيصال");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {
                WebServiceCall webc =new WebServiceCall(InstallActivity.this);
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

                    if(IS_instal_down_pay==1&&IS_instal_first_m_sw==0){
                        new InstallActivity.Save_down_only().execute();
                    }
                    else if(IS_instal_first_m_sw==1&&IS_instal_down_pay==0){
                        new InstallActivity.Save_monthly_only().execute();

                    }
                    else if(IS_instal_first_m_sw==1&&IS_instal_down_pay==1){
                        new InstallActivity.Save_Trane_Client().execute();

                    }


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

    private class GetClient_by_DCode extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(InstallActivity.this);
            pDialog.setMessage("انتظر قليلا...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {

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
                    SPsr.removeAllSP(InstallActivity.this);
                    finish();
                    Intent intent = new Intent(InstallActivity.this, LoginActivity.class); intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    private class GetClientData_Master_Code extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog


        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {
                WebServiceCall webc =new WebServiceCall(InstallActivity.this);
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


                // Dismiss the progress dialog
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }


                BranchCodeS="";
                BranchCodeS=WebServiceClientData.motaheda_branch.toString();

                Log.e(TAG, "P_Code P_Code P_Code: " + P_Code.toString());

                doc_nameS=WebServiceClientData.manager_name.toString();
                pharm_nameS="";
                pharm_nameS=WebServiceClientData.phar_name.toString();

                install_first_month_value.setText(WebServiceClientDataMore.monthly_fee.toString());
                month_valueS="";
                month_valueS=WebServiceClientDataMore.monthly_fee.toString();

            }
            catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }





        }

    }

    private class Save_monthly_only extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(InstallActivity.this);
            pDialog.setMessage("يتم الان تحصيل باقي التعاقد");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {

                WebServiceVisitNumberData.visit_no="";
                WebServiceCall webc =new WebServiceCall(InstallActivity.this);
                WebServiceResult.ErrorID=777;
                WebServiceResult.ErrorMessage="";

                Log.e(TAG, "IS_instal_down_pay :" + IS_instal_down_pay);
                Log.e(TAG, "IS_instal_first_m_sw :" + IS_instal_first_m_sw);

                if(P_Code.length()>0)
                {

                    if(IS_instal_first_m_sw==1){
                        if(paytypeSp_position==0||paytypeSp_position==2) {

                            if(install_first_month_value.getText().toString().length()>1) {

                                webc.save_monthly_fees(P_Code, empsercode, nc_rep_code.getText().toString(), install_first_month_value.getText().toString(), paytypeCode,yearS,monthS,banknameCode,"",con_note.getText().toString(),chenam.getText().toString(),cheDate_dateAF,"");

                            }
                        }
                        if(paytypeSp_position==1) {

                            if(install_first_month_value.getText().toString().length()>1) {

                                webc.save_monthly_fees(P_Code, empsercode, nc_rep_code.getText().toString(), install_first_month_value.getText().toString(), paytypeCode,yearS,monthS,banknameCode,"",con_note.getText().toString(),"","","");

                            }
                        }

                    }

                    if(WebServiceResult.ErrorID==0){
                        if(IS_instal_down_pay==1||IS_instal_first_m_sw==1){
                            if(is_reused==0){
                                webc.Insert_Reciepts_Data(nc_rep_code.getText().toString(),rec_Date.getText().toString());

                            }
                        }

                        webc.Insert_Emp_Daily_Report(nc_rep_code.getText().toString(),P_Code,doc_nameS.toString(),con_note.getText().toString(),pharm_nameS,pharm_nameS,BranchCodeS);


                    }



                }
            }
            catch(Exception ex)
            {
                Toast.makeText(getApplicationContext(),
                        "تعذر الإتصال بالإنترنت, راجع مزود الإنترنت",
                        Toast.LENGTH_LONG)
                        .show();
                Log.e(TAG, "Save_Trane_Client Error: " + ex.getMessage());


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

                if(WebServiceResult.ErrorID==0){

                    Toast.makeText(getApplicationContext(),
                            "تمت العملية بنجاح",
                            Toast.LENGTH_LONG)
                            .show();
                    finish();
                }
                else if(WebServiceResult.ErrorID==1){
                    Toast.makeText(getApplicationContext(),
                            "خطأ",
                            Toast.LENGTH_LONG)
                            .show();
                    Log.e(TAG, "Save_Trane_Client Error: " + WebServiceResult.ErrorMessage.toString());
                    finish();
                }



            }
            catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }





        }

    }
    private class Save_down_only extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(InstallActivity.this);
            pDialog.setMessage("يتم الان تحصيل باقي التعاقد");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {

                WebServiceVisitNumberData.visit_no="";
                WebServiceCall webc =new WebServiceCall(InstallActivity.this);
                WebServiceResult.ErrorID=777;
                WebServiceResult.ErrorMessage="";

                Log.e(TAG, "IS_instal_down_pay :" + IS_instal_down_pay);
                Log.e(TAG, "IS_instal_first_m_sw :" + IS_instal_first_m_sw);




                if(P_Code.length()>0)
                {


                    if(IS_instal_down_pay==1){

                        if(paytypeSp_position==0||paytypeSp_position==2){

                            if(install_down_pay.getText().toString().length()>1){
                                webc.Insert_Contract_Fess(P_Code,empsercode,nc_rep_code.getText().toString(),install_down_pay.getText().toString(),paytypeCode,con_note.getText().toString(),chenam.getText().toString(),cheDate_dateAF,"","remain");

                            }


                        }
                        if(paytypeSp_position==1){

                            if(install_down_pay.getText().toString().length()>1) {

                                webc.Insert_Contract_Fess(P_Code, empsercode, nc_rep_code.getText().toString(), install_down_pay.getText().toString(), paytypeCode, con_note.getText().toString(),"","","","remain");

                            }
                        }


                    }


                    if(WebServiceResult.ErrorID==0){
                        if(IS_instal_down_pay==1||IS_instal_first_m_sw==1){
                            if(is_reused==0){
                                webc.Insert_Reciepts_Data(nc_rep_code.getText().toString(),rec_Date.getText().toString());

                            }
                        }

                        webc.Insert_Emp_Daily_Report(nc_rep_code.getText().toString(),P_Code,doc_nameS.toString(),con_note.getText().toString(),pharm_nameS,pharm_nameS,BranchCodeS);


                    }



                }
            }
            catch(Exception ex)
            {
                Toast.makeText(getApplicationContext(),
                        "تعذر الإتصال بالإنترنت, راجع مزود الإنترنت",
                        Toast.LENGTH_LONG)
                        .show();
                Log.e(TAG, "Save_Trane_Client Error: " + ex.getMessage());


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

                if(WebServiceResult.ErrorID==0){

                    Toast.makeText(getApplicationContext(),
                            "تمت العملية بنجاح",
                            Toast.LENGTH_LONG)
                            .show();
                    finish();
                }
                else if(WebServiceResult.ErrorID==1){
                    Toast.makeText(getApplicationContext(),
                            "خطأ",
                            Toast.LENGTH_LONG)
                            .show();
                    Log.e(TAG, "Save_Trane_Client Error: " + WebServiceResult.ErrorMessage.toString());
                    finish();
                }



            }
            catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }





        }

    }

    private class Save_Trane_Client extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(InstallActivity.this);
            pDialog.setMessage("يتم الان اتمام التدريب");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {

                WebServiceVisitNumberData.visit_no="";
                WebServiceCall webc =new WebServiceCall(InstallActivity.this);
                WebServiceResult.ErrorID=777;
                WebServiceResult.ErrorMessage="";

                Log.e(TAG, "IS_instal_down_pay :" + IS_instal_down_pay);
                Log.e(TAG, "IS_instal_first_m_sw :" + IS_instal_first_m_sw);



                if(P_Code.length()>0&& empsercode.length()>0)
                {

                    webc.Get_visit_number(P_Code,empsercode);
                    visit_noS="";
                    visit_noS=WebServiceVisitNumberData.visit_no.toString();
                    Log.e(TAG, "visit_no :" + visit_noS);


                }


                if(P_Code.length()>0&&visit_noS.length()>0)
                {

                    webc.Save_Visit_Client(P_Code,empsercode,visit_noS,instal_visit_dateAF,con_note.getText().toString());

                    if(IS_instal_down_pay==1){

                        if(paytypeSp_position==0||paytypeSp_position==2){

                            if(install_down_pay.getText().toString().length()>1){
                                webc.Insert_Contract_Fess(P_Code,empsercode,nc_rep_code.getText().toString(),install_down_pay.getText().toString(),paytypeCode,con_note.getText().toString(),chenam.getText().toString(),cheDate_dateAF,"","remain");

                            }


                        }
                        if(paytypeSp_position==1){

                            if(install_down_pay.getText().toString().length()>1) {

                                webc.Insert_Contract_Fess(P_Code, empsercode, nc_rep_code.getText().toString(), install_down_pay.getText().toString(), paytypeCode, con_note.getText().toString(),"","","","remain");

                            }
                        }
                        if(WebServiceResult.ErrorID==0) {
                            webc.Update_client_stage(P_Code, "l");
                        }

                    }
                    if(IS_instal_first_m_sw==1){
                        if(paytypeSp_position==0||paytypeSp_position==2) {

                            if(install_first_month_value.getText().toString().length()>1) {

                                webc.save_monthly_fees(P_Code, empsercode, nc_rep_code.getText().toString(), install_first_month_value.getText().toString(), paytypeCode,yearS,monthS,banknameCode,"",con_note.getText().toString(),chenam.getText().toString(),cheDate_dateAF,"");

                            }
                        }
                        if(paytypeSp_position==1) {

                            if(install_first_month_value.getText().toString().length()>1) {

                                webc.save_monthly_fees(P_Code, empsercode, nc_rep_code.getText().toString(), install_first_month_value.getText().toString(), paytypeCode,yearS,monthS,banknameCode,"",con_note.getText().toString(),"","","");

                            }
                        }
                        if(WebServiceResult.ErrorID==0) {
                            webc.Update_client_stage(P_Code, "c");
                        }
                    }

                    if(WebServiceResult.ErrorID==0){
                        if(IS_instal_down_pay==1||IS_instal_first_m_sw==1){
                            if(is_reused==0){
                                webc.Insert_Reciepts_Data(nc_rep_code.getText().toString(),rec_Date.getText().toString());

                            }
                        }

                        webc.Insert_Emp_Daily_Report(nc_rep_code.getText().toString(),P_Code,doc_nameS.toString(),con_note.getText().toString(),pharm_nameS,pharm_nameS,BranchCodeS);

                        if(IS_instal_down_pay==0&&IS_instal_first_m_sw==0) {

                            webc.Update_client_stage(P_Code, "c");
                        }
                    }



                }
            }
            catch(Exception ex)
            {
                Toast.makeText(getApplicationContext(),
                        "تعذر الإتصال بالإنترنت, راجع مزود الإنترنت",
                        Toast.LENGTH_LONG)
                        .show();
                Log.e(TAG, "Save_Trane_Client Error: " + ex.getMessage());


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

                if(WebServiceResult.ErrorID==0){

                    Toast.makeText(getApplicationContext(),
                            "تمت العملية بنجاح",
                            Toast.LENGTH_LONG)
                            .show();
                    finish();
                }
                else if(WebServiceResult.ErrorID==1){
                    Toast.makeText(getApplicationContext(),
                            "خطأ",
                            Toast.LENGTH_LONG)
                            .show();
                    Log.e(TAG, "Save_Trane_Client Error: " + WebServiceResult.ErrorMessage.toString());
                    finish();
                }



            }
            catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }





        }

    }


    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nc_rep_code.getWindowToken(), 0);
    }















    @Override
    public void onBackPressed() {
         super.onBackPressed();
//         finish();
//        Intent intent = new Intent(InstallActivity.this, UnderInstalListActivity.class);
//        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_main) {
//            Intent intent = new Intent(InstallActivity.this, MainActivity.class);
//            startActivity(intent);
//        } else if (id == R.id.nav_new_con) {
//            Intent intent = new Intent(InstallActivity.this, NewConActivity.class);
//            startActivity(intent);
//        }else if (id == R.id.nav_sup) {
//            Intent intent = new Intent(InstallActivity.this, SupActivity.class);
//            startActivity(intent);
//        } else if (id == R.id.nav_inst) {
//            Intent intent = new Intent(InstallActivity.this, InstallActivity.class);
//            startActivity(intent);
//        } else if (id == R.id.nav_order) {
//            Intent intent = new Intent(InstallActivity.this, OrderActivity.class);
//            startActivity(intent);
//        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
