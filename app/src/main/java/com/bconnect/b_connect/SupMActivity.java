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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
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


public class SupMActivity extends AppCompatActivity {

    SharedPreferencesSR SPsr=new SharedPreferencesSR();


    private String TAG = "SR: "+SupMActivity.class.getSimpleName()+": ";
    private int sentActive,is_monthly_payed1,is_monthly_payed2,is_year_ok ,is_rec_code;

    SharedPreferences pref ;
    SharedPreferences.Editor editor;
    String  empsercode,payedMonths,notpayedMonths;
    String  payedMonths2,notpayedMonths2;

    private ProgressDialog pDialog;


    TextView pharm_name,pharm_code,pharm_add,pharm_phone_line,pharm_phone_mobile, pharm_doc_name;
    TextView  banknameTit,pharm_month_val,servtypeTit,pharm_month_target;
    TextInputLayout cheDateT;

    String  emp_name,pharm_nameS,BranchCodeS;
    String   banknameCode,servtypeCode,paytypeCode,P_Code ,doc_nameS;
    String   rec_Date_dateAF,cheDate_dateAF,real_Date,real_Date2,pharm_phone_lineS,pharm_phone_mobileS,PhoneS,yearS,yearS2,MonthS,MonthS2;

    int total_valueINT,monthly_notpayedINT,monthly_notpayedINT2;

    EditText nc_rep_code,rec_Date,cheDate,chenam,con_note,sup_month_cho,sup_month_cho2;

    EditText total_value,total_value2;
 
    Intent intent;
 

    SimpleDateFormat rec_Date_SDF,cheDate_SDF;

    Button nc_sendbut,nc_rep_code_vil_but;

    private static String paytypeUrl,banknameUrl;

    Spinner paytypeSp,banknameSp,yearFMSp;
 
    List<HashMap<String, String>> paytypeList,banknameList;
    private SimpleAdapter  paytypeSAdapter,banknameSAdapter ;

    Calendar rec_Date_myCalendar,cheDate_myCalendar,real_Date_myCalendar;
 
    String mot_code,month_target,month_done,pharm_month_valS,empnameS,empcodeS,SearchToS,recS;

    int  paytypeSp_position,is_Total_V,is_two_year,month_target_INT,month_done_INT,year_INT;
    int month_done_INT2,year_INT2;

    String[] listItems;
    ArrayList<Integer> MonthItems = new ArrayList<>();
    ArrayList<Integer> TranzItems = new ArrayList<>();
    ArrayList<String> stringlist = new ArrayList<>();

    String[] listItems2;
     ArrayList<Integer> MonthItems2 = new ArrayList<>();
    ArrayList<Integer> TranzItems2 = new ArrayList<>();
    ArrayList<String> stringlist2 = new ArrayList<>();


    NestedScrollView scrollView;

    List<String>   yearFMSpList;
    ArrayAdapter<String>  yearFMSpadapter;


    Switch sup_sw_rec_code_reuse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_m);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        basic();
        control();


        new SupMActivity.GetClientData_Master_Code().execute();
        new SupMActivity.GetClient_by_DCode().execute();


    }


    public  void basic(){

        scrollView = (NestedScrollView) findViewById(R.id.sup_NS);


        pharm_name=(TextView)findViewById(R.id.pharm_name);
        pharm_code=(TextView)findViewById(R.id.pharm_code);
        pharm_add=(TextView)findViewById(R.id.pharm_add);
        pharm_phone_line=(TextView)findViewById(R.id.pharm_phone_line);
        pharm_phone_mobile=(TextView)findViewById(R.id.pharm_phone_mobile);
        pharm_doc_name=(TextView)findViewById(R.id.pharm_doc_name);
        banknameTit=(TextView)findViewById(R.id.banknameTit);
        pharm_month_val=(TextView)findViewById(R.id.pharm_month_val);
        servtypeTit=(TextView)findViewById(R.id.servtypeTit);
        pharm_month_target=(TextView)findViewById(R.id.pharm_month_target);

        cheDateT=(TextInputLayout) findViewById(R.id.cheDateT);


        nc_sendbut=(Button) findViewById(R.id.nc_sendbut);
        nc_rep_code_vil_but=(Button) findViewById(R.id.nc_rep_code_vil_but);

        sup_sw_rec_code_reuse=(Switch) findViewById(R.id.sup_sw_rec_code_reuse);


        total_value=(EditText) findViewById(R.id.total_value);
        total_value2=(EditText) findViewById(R.id.total_value2);
        nc_rep_code=(EditText) findViewById(R.id.nc_rep_code);
        rec_Date=(EditText) findViewById(R.id.rec_Date);
        cheDate=(EditText) findViewById(R.id.cheDate);
        chenam=(EditText) findViewById(R.id.chenam);
        con_note=(EditText) findViewById(R.id.con_note);
        sup_month_cho=(EditText) findViewById(R.id.sup_month_cho);
        sup_month_cho2=(EditText) findViewById(R.id.sup_month_cho2);



        paytypeSp = (Spinner) findViewById(R.id.paytypeSp);
        banknameSp = (Spinner) findViewById(R.id.banknameSp);
        yearFMSp= (Spinner) findViewById(R.id.yearFMSp);



        nc_rep_code_vil_but.setBackgroundColor(Color.RED);
        nc_rep_code_vil_but.setText("التحقق من صلاحية رقم الإيصال");
        sentActive=0;


        sentActive=0;
        is_Total_V=0;




        pref= getApplicationContext().getSharedPreferences("MySr", MODE_PRIVATE);
        editor = pref.edit();

        empsercode="";
        empsercode= SPsr.getSP(getApplicationContext(),"empsercode");
        emp_name="";
        emp_name=SPsr.getSP(getApplicationContext(),"empsername");
        P_Code="";
        doc_nameS="";

        try{
            intent = getIntent();
            P_Code  = intent.getExtras().getString("phar_code");
            mot_code = intent.getExtras().getString("mot_code");
            month_done  = intent.getExtras().getString("month_done");
            month_target = intent.getExtras().getString("month_target");
        }catch (Exception ex){
            super.onBackPressed();
        }



        Log.e("SR", "phar_code :" + P_Code);
        Log.e("SR", "mot_code :" + mot_code);
        Log.e("SR", "month_done :" + month_done);
        Log.e("SR", "month_target :" + month_target);

        yearFMSpList = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear-10; i <= thisYear+1; i++) {
            yearFMSpList.add(Integer.toString(i));
        }
        yearFMSpadapter = new ArrayAdapter<String>(this,R.layout.sr_spinner_item,R.id.tvName, yearFMSpList);

        yearFMSp.setAdapter(yearFMSpadapter);

            final int pos=yearFMSpList.indexOf(String.valueOf(thisYear));
            yearFMSp.post(new Runnable() {
                @Override
                public void run() {
                    yearFMSp.setSelection(pos);
                }
            });





//        SimpleDateFormat spf=new SimpleDateFormat("MM-yyyy");
//        Date newDate= null;
//        try {
//            newDate = spf.parse(month_target);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        spf= new SimpleDateFormat("yyyy");
//        yearS = spf.format(newDate);

//        year_INT=Integer.parseInt(yearS);
//        year_INT2=year_INT+1;
//        yearS2=String.valueOf(year_INT2);
//        Log.e("SR", "yearS :" + yearS);
//
//        SimpleDateFormat spf2=new SimpleDateFormat("MM-yyyy");
//        Date newDate2= null;
//        try {
//            newDate2 = spf2.parse(month_target);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        spf2= new SimpleDateFormat("MM");
//        MonthS = spf2.format(newDate2);
//
//        Log.e("SR", "MonthS :" + MonthS);
//
//        month_done_INT=Integer.parseInt(MonthS);
//        month_done_INT2=1;
//        MonthS2 =String.valueOf(month_done_INT2); ;
//
//
//
//        for(int i=month_done_INT;i<=12;i++){
//            stringlist.add(yearS+   " - "+i);
//
//        }
//        for(int i=month_done_INT2;i<=12;i++){
//            stringlist2.add(yearS2+   " - "+i);
//
//        }
//
//        listItems = new String[stringlist.size()];
//        listItems = stringlist.toArray(listItems);
//        listItems2 = new String[stringlist2.size()];
//        listItems2 = stringlist2.toArray(listItems2);


        paytypeUrl = "http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/GetPayWay";
        banknameUrl = "http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/GetBank";


        paytypeList = new ArrayList<HashMap<String, String>>();
        banknameList = new ArrayList<HashMap<String, String>>();

        paytypeSAdapter = new SimpleAdapter(this, paytypeList, R.layout.pay_way_spinner_item,
                new String[] { "name" }, new int[] { R.id.tvName });
        banknameSAdapter = new SimpleAdapter(this, banknameList, R.layout.bank_name_spinner_item,
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

        is_two_year=0;

        is_monthly_payed1=0;
        is_monthly_payed2=0;

        is_year_ok=0;
        is_rec_code=0;
    }

    public  void control(){


        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


            if(TranzItems.size()>0||TranzItems2.size()>0){
                is_year_ok=1;
            }
            else {
                is_year_ok=0;

            }

                closeKeyboard();
                return false;
            }
        });

        sup_sw_rec_code_reuse.setChecked(false);

        sup_sw_rec_code_reuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sup_sw_rec_code_reuse.isChecked()){
                    is_rec_code=0;
                    sup_sw_rec_code_reuse.setTextColor(Color.BLUE);
                }
                else{
                    is_rec_code=1;
                    sup_sw_rec_code_reuse.setTextColor(Color.RED);

                }
            }
        });

        yearFMSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {

                yearS = adapter.getAdapter().getItem(position).toString();
                Log.e(TAG, "yearS yearS: " + yearS);
                year_INT=Integer.parseInt(yearS);
                year_INT2=year_INT+1;
                yearS2=String.valueOf(year_INT2);
                Log.e("SR", "yearS :" + yearS);

                stringlist.clear();
                stringlist2.clear();
                for(int i=1;i<=12;i++){
                    stringlist.add(yearS+   " - "+i);

                }
                for(int i=1;i<=12;i++){
                    stringlist2.add(yearS2+   " - "+i);

                }

                listItems = new String[stringlist.size()];
                listItems = stringlist.toArray(listItems);
                listItems2 = new String[stringlist2.size()];
                listItems2 = stringlist2.toArray(listItems2);



            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(SupMActivity.this, "تذكر ان تختار السنة" , Toast.LENGTH_SHORT).show();

            }
        });



        //////////////////////////////////  sup_month_cho1 //////////////////////////////////


        sup_month_cho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int s=0;
                while(s < listItems.length) {
                    MonthItems.remove((Integer.valueOf(s)));
                    TranzItems.remove((Integer.valueOf(s)));


                    s++;

                }
                android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(SupMActivity.this);
                mBuilder.setTitle("الاشهر التي لم تحصل");
                mBuilder.setMultiChoiceItems(listItems, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
//
                        if(isChecked){
                            MonthItems.add(position);
                        }else{
                            MonthItems.remove((Integer.valueOf(position)));
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("تاكيد", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        sup_month_cho.setText("");


                         new SupMActivity.Get_Monthly_Val().execute();



                    }
                });

                mBuilder.setNegativeButton("مسح الكل", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });


                mBuilder.setNeutralButton("اختيار الكل",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                            }
                        });

                final android.support.v7.app.AlertDialog dialog = mBuilder.create();
                dialog.show();
                dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Boolean wantToCloseDialog = false;


                        ListView vv = dialog.getListView();
                        int i = 0;
                        while(i < listItems.length) {
                            MonthItems.remove((Integer.valueOf(i)));

                            vv.setItemChecked(i, true);

                            if(vv.isClickable()){
                                MonthItems.add(i);
                            }else{
                                MonthItems.remove((Integer.valueOf(i)));
                            }
                            i++;

                        }

                         if(wantToCloseDialog)
                            dialog.dismiss();
                     }
                });
                dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Boolean wantToCloseDialog = false;

                        ListView vv = dialog.getListView();
                        int i = 0;
                        while(i < listItems.length) {
                            vv.setItemChecked(i, false);

                            MonthItems.remove((Integer.valueOf(i)));

                            i++;

                        }

                        //Do stuff, possibly set wantToCloseDialog to true then...
                        if(wantToCloseDialog)
                            dialog.dismiss();
                        //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                    }
                });
            }
        });



        //////////////////////////////////  sup_month_cho2 //////////////////////////////////





        sup_month_cho2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int s=0;
                while(s < listItems2.length) {
                    MonthItems2.remove((Integer.valueOf(s)));
                    TranzItems2.remove((Integer.valueOf(s)));


                    s++;

                }
                android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(SupMActivity.this);
                mBuilder.setTitle("الاشهر التي لم تحصل");
                mBuilder.setMultiChoiceItems(listItems2, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
//
                        if(isChecked){
                            MonthItems2.add(position);
                        }else{
                            MonthItems2.remove((Integer.valueOf(position)));
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("تاكيد", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {



                        sup_month_cho2.setText("");


                        new SupMActivity.Get_Monthly_Val2().execute();





//                        String item = "";
//                        int itemINT;
//                        sup_month_cho2.setText("");
//
//                        for (int i = 0; i < MonthItems2.size(); i++) {
//                            item =  MonthItems2.get(i).toString() ;
//                            //  itemINT=Integer.parseInt(item)+1;
//
//                            if (i != MonthItems2.size() - 1) {
//
//                            }
//                            itemINT=Integer.parseInt(MonthItems2.get(i).toString())+1;
//                            String S=String.valueOf(itemINT);
//                            sup_month_cho2.append(S+",");
//
//                        }
//                        if(sup_month_cho2.getText().toString().length()>=1){
//                            is_two_year=1;
//                            Toast.makeText(SupMActivity.this,"تم اختيار الاشهر: " +sup_month_cho2.getText().toString(),Toast.LENGTH_LONG).show();
//
//                        }
//                        if(sup_month_cho2.getText().toString().length()<1){
//                            is_two_year=0;
//
//                        }

                    }
                });

                mBuilder.setNegativeButton("مسح الكل", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });


                mBuilder.setNeutralButton("اختيار الكل",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                            }
                        });

                final android.support.v7.app.AlertDialog dialog = mBuilder.create();
                dialog.show();
                dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Boolean wantToCloseDialog = false;


                        ListView vv = dialog.getListView();
                        int i = 0;
                        while(i < listItems2.length) {
                            MonthItems2.remove((Integer.valueOf(i)));

                            vv.setItemChecked(i, true);

                            if(vv.isClickable()){
                                MonthItems2.add(i);
                            }else{
                                MonthItems2.remove((Integer.valueOf(i)));
                            }
                            i++;

                        }

                        if(wantToCloseDialog)
                            dialog.dismiss();
                    }
                });
                dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Boolean wantToCloseDialog = false;

                        ListView vv = dialog.getListView();
                        int i = 0;
                        while(i < listItems2.length) {
                            vv.setItemChecked(i, false);

                            MonthItems2.remove((Integer.valueOf(i)));

                            i++;

                        }

                        //Do stuff, possibly set wantToCloseDialog to true then...
                        if(wantToCloseDialog)
                            dialog.dismiss();
                        //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                    }
                });
            }
        });

        total_value2.setEnabled(false);


        if(total_value.getText().toString().length()<1){
            is_Total_V=0;
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
                if(s.toString().length()>0){
                    total_value2.setText(String.valueOf((monthly_notpayedINT+monthly_notpayedINT2)*Integer.parseInt(s.toString())));
                }else {
                    total_value2.setText("0");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
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
                new DatePickerDialog(SupMActivity.this, rec_Date_datepicker,
                        rec_Date_myCalendar.get(Calendar.YEAR),
                        rec_Date_myCalendar.get(Calendar.MONTH),
                        rec_Date_myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
        cheDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SupMActivity.this, cheDate_datepicker,
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
                paytypeSp_position=position;
                if(position==0){
                    Toast.makeText(SupMActivity.this, "تحصيل بشيك " , Toast.LENGTH_SHORT).show();

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



                    Toast.makeText(SupMActivity.this, "تحصيل نقدي" , Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(SupMActivity.this, "تحصيل بحوالة بنكية" , Toast.LENGTH_SHORT).show();

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
                Toast.makeText(SupMActivity.this, "تذكر ان تختار نوع التحصيل " , Toast.LENGTH_SHORT).show();

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
                Toast.makeText(SupMActivity.this, "تذكر ان تختار نوع التحصيل " , Toast.LENGTH_SHORT).show();

            }
        });


        nc_rep_code_vil_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SupMActivity.GetReciept_ver().execute();
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


        //SR: Start send data





        nc_sendbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.e(TAG, "MonthItems1.size(): " +  MonthItems.size());
                Log.e(TAG, "MonthItems2.size(): " +  MonthItems2.size());

//                if(is_year_ok==0){
//                    Toast.makeText(SupMActivity.this,"قم بختيار الاشهر المراد تحصيلها" ,Toast.LENGTH_LONG).show();
//
//                }


//                StringBuilder t = new StringBuilder();
//                for(int l=(uni_num_INT); l<(uni_num_sall_INT+uni_num_INT); l++){
//                     t.append(l);
//                    uni_note.setText(t);
//                }

//                if(MonthItems2.size()>0&&MonthItems.size()!=(13-month_done_INT)){
//                    Toast.makeText(SupMActivity.this,"يجب ان تحصل السنة الاولى كاملة لكي تتمكن من تحصيل السنة الثانية" ,Toast.LENGTH_LONG).show();
//                    sentActive=0;
//
//                }
//                if(MonthItems2.size()>0&&MonthItems.size()==(13-month_done_INT)){
//                     sentActive=1;
//
//                }

//                if(sentActive==0) {
//                    Toast.makeText(getApplicationContext(),
//                            "قم بالتحقق من البيانات",
//                            Toast.LENGTH_LONG)
//                            .show();
//                }
                if(is_Total_V==0) {
                    Toast.makeText(getApplicationContext(),
                            "اجمالي المبلغ فارغ او اقل من المطلوب",
                            Toast.LENGTH_LONG)
                            .show();
                }


                if(is_Total_V==1) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SupMActivity.this);
                    if(sentActive==0) {

                        builder1.setMessage("سيتم الان التحقق من رقم الإيصال: "
                            +nc_rep_code.getText().toString()+"\n"+
                                "اجمالي المبلغ: "
                                +total_value2.getText().toString()+"\n" +
                                "مبلغ الاشتراك الشهري: "
                                +total_value.getText().toString()+"\n"
                                    +
                                    "الاشهر التي تم اختيارها في سنة "+yearS+": "
                                    +sup_month_cho.getText().toString()+"\n"
                                    +
                                    "الاشهر التي تم اختيارها في سنة "+yearS2+": "
                                    +sup_month_cho2.getText().toString()+"\n"
                            +"\nتأكيد التحصيل؟");

                    }
                    if(sentActive==1) {

                        builder1.setMessage(
                                "اجمالي المبلغ: "
                                        +total_value2.getText().toString()+"\n" +
                                        "مبلغ الاشتراك الشهري: "
                                        +total_value.getText().toString()+"\n"
                                        +
                                        "الاشهر التي تم اختيارها في سنة "+yearS+": "
                                        +sup_month_cho.getText().toString()+"\n"
                                        +
                                        "الاشهر التي تم اختيارها في سنة "+yearS2+": "
                                        +sup_month_cho2.getText().toString()+"\n"
                                +"\nتأكيد التحصيل؟");

                    }
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "تأكيد",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    if(sentActive==0) {

                                        new SupMActivity.GetReciept_AND_Save_Sup_Fees().execute();
                                    }
                                    if(sentActive==1) {
                                        new SupMActivity.Save_Sup_Fees().execute();

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
            pDialog = new ProgressDialog(SupMActivity.this);
            pDialog.setMessage("يتم الان التحقق من رقم الإيصال");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {
                WebServiceCall webc =new WebServiceCall(SupMActivity.this);
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
    private class GetClientData_Master_Code extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SupMActivity.this);
            pDialog.setMessage("إنتظر قليلا...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {
                Log.e(TAG, "P_Code P_Code: " + P_Code);

                WebServiceCall webc =new WebServiceCall(SupMActivity.this);
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
                pharm_phone_lineS=WebServiceClientData.tel1;
                if(pharm_phone_lineS.length()>1){
                    pharm_phone_line.setText("رقم الهاتف الارضي:"+" "+WebServiceClientData.tel2.toString());
                }
                if(pharm_phone_lineS.length()<1){
                    pharm_phone_line.setText("رقم الهاتف الارضي:"+" ");
                }
                pharm_phone_mobileS="";
                pharm_phone_mobileS=WebServiceClientData.mob1;
                if(pharm_phone_mobileS.length()>1){

                    pharm_phone_mobile.setText("رقم الهاتف المحمول:"+" "+WebServiceClientData.mob1.toString());
                }
                if(pharm_phone_lineS.length()<1){
                    pharm_phone_mobile.setText("رقم الهاتف المحمول:"+" ");
                }
                 pharm_doc_name.setText("اسم الصيدلي:"+" "+WebServiceClientData.manager_name.toString());
                pharm_month_valS="";
                pharm_month_valS=WebServiceClientDataMore.monthly_fee.toString();
                pharm_month_val.setText("قيمة الإشتراك:"+" "+WebServiceClientDataMore.monthly_fee.toString());
                total_value.setText(WebServiceClientDataMore.monthly_fee.toString());
                if(total_value.getText().toString().length()>0){
                    total_valueINT=Integer.parseInt(total_value.getText().toString());

                }
                else {
                    total_valueINT=0;

                }
                pharm_month_target.setText("الشهر المطلوب تحصيله:"+" "+month_target);


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
                    SPsr.removeAllSP(SupMActivity.this);
                    finish();
                    Intent intent = new Intent(SupMActivity.this, LoginActivity.class); intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
            pDialog = new ProgressDialog(SupMActivity.this);
            pDialog.setMessage("جاري الان تحصيل السنة الاولى, انتظر قليلا...");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {

                WebServiceCall webc =new WebServiceCall(SupMActivity.this);

                Log.e(TAG, "P_Code:" + P_Code);


                if(P_Code.length()>1){

                    Log.e(TAG, "P_Code P_Code: " + P_Code);

                    if(TranzItems.size()>0){


                        int itemINT;
                        for (int i = 0; i < TranzItems.size(); i++) {


                            itemINT=Integer.parseInt(TranzItems.get(i).toString())+1;
                            String S=String.valueOf(itemINT);
                            Log.e(TAG, "S: " + S);
                            Log.e(TAG, "yearS: " + yearS);



                                if(paytypeSp_position==0||paytypeSp_position==2) {

                                    if(total_value.getText().toString().length()>1) {

                                        webc.save_monthly_fees(P_Code, empsercode, nc_rep_code.getText().toString(), total_value.getText().toString(), paytypeCode,yearS,S,banknameCode,"",con_note.getText().toString(),chenam.getText().toString(),cheDate_dateAF,"");
                                        is_monthly_payed1=1;

                                    }
                                }
                                if(paytypeSp_position==1) {

                                    if(total_value.getText().toString().length()>1) {

                                        webc.save_monthly_fees(P_Code, empsercode, nc_rep_code.getText().toString(), total_value.getText().toString(), paytypeCode,yearS,S,banknameCode,"",con_note.getText().toString(),"","","");
                                        is_monthly_payed1=1;

                                    }
                                }






                        }

                        if(WebServiceResult.ErrorID==0){
                            PhoneS=pharm_phone_mobileS.toString();
                            if(is_rec_code==1){
                                webc.Insert_Reciepts_Data(nc_rep_code.getText().toString(),rec_Date.getText().toString());
                            }

                            webc.Insert_Emp_Daily_Report(nc_rep_code.getText().toString(),P_Code,doc_nameS.toString(),con_note.getText().toString(),pharm_nameS,PhoneS,BranchCodeS);
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
                Log.e(TAG, "Save_Sup_Fees Error: " + ex.getMessage());


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
                if(TranzItems.size()>0){

                    if(WebServiceResult.ErrorID==0){

                        if(TranzItems2.size()>0){
                            new SupMActivity.Save_Sup_Fees2().execute();
                        }
                        else {
                            if(is_monthly_payed1==0){

                                finishDialog("جميع الاشهر التي تم اختيارها في السنة الاولى محصلة مسبقا");
                            }
                            else {


                                finishDialog("تم تحصيل السنة الاولى بنجاح");
                            }
                        }

                    }
                    else {

                        Toast.makeText(getApplicationContext(),
                                "حدث خطأ",
                                Toast.LENGTH_LONG)
                                .show();
                        Log.e(TAG, "Save_Sup_Fees Error: " + WebServiceResult.ErrorMessage.toString());

                        finish();

                    }
                }
                else{
                    new SupMActivity.Save_Sup_Fees2().execute();
                }

            }
            catch (Exception ex) {


                Log.e(TAG, "New_Client onPostExecute Error: " + ex.getMessage());

            }





        }

    }

    private class Save_Sup_Fees2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SupMActivity.this);
            pDialog.setMessage("جاري الان تحصيل السنة الثانية, انتظر قليلا...");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {

                WebServiceCall webc =new WebServiceCall(SupMActivity.this);

                Log.e(TAG, "P_Code:" + P_Code);


                if(P_Code.length()>1){

                    Log.e(TAG, "P_Code P_Code: " + P_Code);


                        int itemINT2;
                        if(TranzItems2.size()>0){

                            for (int ii = 0; ii < TranzItems2.size(); ii++) {

//                                month_done_INT2
                                itemINT2=Integer.parseInt(TranzItems2.get(ii).toString())+1;
                                String S2=String.valueOf(itemINT2);
                                Log.e(TAG, "S2: " + S2);

                                    if(paytypeSp_position==0||paytypeSp_position==2) {

                                        if(total_value.getText().toString().length()>1) {

                                            webc.save_monthly_fees(P_Code, empsercode, nc_rep_code.getText().toString(), total_value.getText().toString(), paytypeCode,yearS2,S2,banknameCode,"",con_note.getText().toString(),chenam.getText().toString(),cheDate_dateAF,"");
                                            is_monthly_payed2=1;

                                        }
                                    }
                                    if(paytypeSp_position==1) {

                                        if(total_value.getText().toString().length()>1) {

                                            webc.save_monthly_fees(P_Code, empsercode, nc_rep_code.getText().toString(), total_value.getText().toString(), paytypeCode,yearS2,S2,banknameCode,"",con_note.getText().toString(),"","","");
                                            is_monthly_payed2=1;

                                        }
                                    }

                            }

                        }
                        if(WebServiceResult.ErrorID==0){
                            PhoneS=pharm_phone_mobileS.toString();
                            if(is_rec_code==1){
                                webc.Insert_Reciepts_Data(nc_rep_code.getText().toString(),rec_Date.getText().toString());
                            }

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
                Log.e(TAG, "Save_Sup_Fees2 Error: " + ex.getMessage());

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

                    if(is_monthly_payed1==0&&is_monthly_payed2==0){

                        finishDialog("جميع الاشهر التي تم اختيارها في السنة الاولى و الثانية محصلة مسبقا");
                    }
                    else if(is_monthly_payed1==1&&is_monthly_payed2==0){

                        finishDialog("تم تحصيل السنة الاولى فقط");
                    }
                    else if(is_monthly_payed1==0&&is_monthly_payed2==1){

                        finishDialog("تم تحصيل السنة الثانية فقط");
                    }
                    else if(is_monthly_payed1==1&&is_monthly_payed2==1){

                        finishDialog("تم تحصيل السنة الاولى و السنة الثانية بنجاح");
                    }


                }
                else {

                    Toast.makeText(getApplicationContext(),
                            "حدث خطأ",
                            Toast.LENGTH_LONG)
                            .show();
                    Log.e(TAG, "Save_Sup_Fees2 Error: " + WebServiceResult.ErrorMessage.toString());

                    finish();

                }




            }
            catch (Exception ex) {


                Log.e(TAG, "New_Client onPostExecute Error: " + ex.getMessage());

            }





        }

    }
    private class GetReciept_AND_Save_Sup_Fees extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SupMActivity.this);
            pDialog.setMessage("يتم الان التحقق من رقم الإيصال");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {
                WebServiceCall webc =new WebServiceCall(SupMActivity.this);
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
                    new SupMActivity.Save_Sup_Fees().execute();

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

    private class Get_Monthly_Val extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SupMActivity.this);
            pDialog.setMessage("يتم الان التحقق من الاشهر المختارة");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {
                WebServiceCall webc =new WebServiceCall(SupMActivity.this);
                payedMonths="";
                notpayedMonths="";
                int TrasitemINT;
                int s=0;
                while(s < listItems.length) {
                     TranzItems.remove((Integer.valueOf(s)));

                    s++;

                }

                TranzItems  = new ArrayList<Integer>(MonthItems);
                int itemINT;
                int itemINT2;
                for (int i = 0; i < MonthItems.size(); i++) {


                    itemINT=Integer.parseInt(MonthItems.get(i).toString())+1;
                    itemINT2=Integer.parseInt(MonthItems.get(i).toString());
                    String S=String.valueOf(itemINT);
                    Log.e(TAG, "S: " + S);
                    Log.e(TAG, "yearS: " + yearS);

                    WebServiceMonthlyFeesResult.EX=3;
                    webc.Monthly_Fee_vel(S,yearS,P_Code);

                    Log.e(TAG, "Monthly_Fee_vel: " +String.valueOf(WebServiceMonthlyFeesResult.EX) );
                    if(WebServiceMonthlyFeesResult.EX==1){
                        TranzItems.remove((Integer.valueOf(itemINT2)));
                        payedMonths +=S+",";

                    }

                }
                for (int i = 0; i < TranzItems.size(); i++) {

                    MonthItems.remove((Integer.valueOf(i)));

                }


                for (int i = 0; i < TranzItems.size(); i++) {

                    //  itemINT=Integer.parseInt(item)+1;

                    itemINT=Integer.parseInt(TranzItems.get(i).toString())+1;
                    String S=String.valueOf(itemINT);

                    notpayedMonths+=S.replace("null","")+",";



                }
                Log.e(TAG, "payedMonths: " +payedMonths );
                Log.e(TAG, "notpayedMonths: " +notpayedMonths );



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




                    if(WebServiceMonthlyFeesResult.ErrorID==0){
                        if(TranzItems.size()>0){
                           // Toast.makeText(SupMActivity.this,"الاشهر المحصلة مسبقا\n"+payedMonths+"\n غير ممكن تحصيل هذه الاشهر" ,Toast.LENGTH_LONG).show();
                            if(payedMonths==null){
                                payedMonths="0";
                            }
                            MonthlyDialog("الاشهر المحصلة مسبقا:",payedMonths,notpayedMonths);
                        }else{
                           // Toast.makeText(SupMActivity.this,"الاشهر المحصلة مسبقا\n"+payedMonths+"\n لا يمكن تحصيل هذه الاشهر" ,Toast.LENGTH_LONG).show();
                            monthly_notpayedINT=0;
                            total_value2.setText("");
                            if(total_value.getText().toString().length()>0){
                                total_valueINT=Integer.parseInt(total_value.getText().toString());
                            }
                            else {
                                total_valueINT=0;

                            }
                            total_value2.setText("");
                            total_value2.setText(String.valueOf(((monthly_notpayedINT2+monthly_notpayedINT)*total_valueINT)));
                        }

                    }
                    else {

                        Toast.makeText(getApplicationContext(),
                                "حدث خطأ",
                                Toast.LENGTH_LONG)
                                .show();
                        Log.e(TAG, "Save_Sup_Fees Error: " + WebServiceMonthlyFeesResult.ErrorMessage.toString());

                        finish();

                    }


            }
            catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }





        }

    }
    private class Get_Monthly_Val2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SupMActivity.this);
            pDialog.setMessage("يتم الان التحقق من الاشهر المختارة");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {
                WebServiceCall webc =new WebServiceCall(SupMActivity.this);
                payedMonths2="";
                notpayedMonths2="";
                int TrasitemINT;
                int s=0;
                while(s < listItems2.length) {
                    TranzItems2.remove((Integer.valueOf(s)));

                    s++;

                }

                TranzItems2  = new ArrayList<Integer>(MonthItems2);
                int itemINT;
                int itemINT2;
                for (int i = 0; i < MonthItems2.size(); i++) {


                    itemINT=Integer.parseInt(MonthItems2.get(i).toString())+1;
                    itemINT2=Integer.parseInt(MonthItems2.get(i).toString());
                    String S=String.valueOf(itemINT);
                    Log.e(TAG, "S: " + S);
                    Log.e(TAG, "yearS2: " + yearS2);

                    WebServiceMonthlyFeesResult.EX=3;
                    webc.Monthly_Fee_vel(S,yearS2,P_Code);

                    Log.e(TAG, "Monthly_Fee_vel: " +String.valueOf(WebServiceMonthlyFeesResult.EX) );
                    if(WebServiceMonthlyFeesResult.EX==1){
                        TranzItems2.remove((Integer.valueOf(itemINT2)));
                        payedMonths2 +=S.replace("null","")+",";

                    }

                }
                for (int i = 0; i < TranzItems2.size(); i++) {

                    MonthItems2.remove((Integer.valueOf(i)));

                }


                for (int i = 0; i < TranzItems2.size(); i++) {

                    //  itemINT=Integer.parseInt(item)+1;

                    itemINT=Integer.parseInt(TranzItems2.get(i).toString())+1;
                    String S=String.valueOf(itemINT);

                    notpayedMonths2+=S.replace("null","")+",";



                }
                Log.e(TAG, "payedMonths2: " +payedMonths2 );
                Log.e(TAG, "notpayedMonths2: " +notpayedMonths2 );



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




                if(WebServiceMonthlyFeesResult.ErrorID==0){
                    if(TranzItems2.size()>0){
                        // Toast.makeText(SupMActivity.this,"الاشهر المحصلة مسبقا\n"+payedMonths+"\n غير ممكن تحصيل هذه الاشهر" ,Toast.LENGTH_LONG).show();
                        if(payedMonths2==null){
                            payedMonths2="0";
                        }
                        MonthlyDialog2("الاشهر المحصلة مسبقا:",payedMonths2,notpayedMonths2);
                    }else{
                      //  Toast.makeText(SupMActivity.this,"الاشهر المحصلة مسبقا\n"+payedMonths2+"\n لا يمكن تحصيل هذه الاشهر" ,Toast.LENGTH_LONG).show();
                        monthly_notpayedINT2=0;
                        if(total_value.getText().toString().length()>0){
                            total_valueINT=Integer.parseInt(total_value.getText().toString());
                        }
                        else {
                            total_valueINT=0;

                        }
                        total_value2.setText("");
                        total_value2.setText(String.valueOf(((monthly_notpayedINT2+monthly_notpayedINT)*total_valueINT)));
                    }

                }
                else {

                    Toast.makeText(getApplicationContext(),
                            "حدث خطأ",
                            Toast.LENGTH_LONG)
                            .show();
                    Log.e(TAG, "Get_Monthly_Val2 Error: " + WebServiceMonthlyFeesResult.ErrorMessage.toString());

                    finish();

                }


            }
            catch (Exception ex) {


                Log.e(TAG, "Get_Monthly_Val2 Error: " + ex.getMessage());

            }





        }

    }


    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(sup_month_cho.getWindowToken(), 0);
    }
    private void MonthlyDialog(String mess,String mess2,String mess3){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SupMActivity.this);

        builder1.setTitle("الاشهر المختارة:-");
        builder1.setMessage(
                mess.toString()+"\n"+mess2+"\nهل تود تحصيل الاشهر:"+"\n"+mess3
        );


        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "نعم",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        int itemINT;
                        monthly_notpayedINT=0;
                        if(total_value.getText().toString().length()>0){
                            total_valueINT=Integer.parseInt(total_value.getText().toString());
                        }
                        else {
                            total_valueINT=0;

                        }
                        for (int i = 0; i < TranzItems.size(); i++) {

                            //  itemINT=Integer.parseInt(item)+1;

                            itemINT=Integer.parseInt(TranzItems.get(i).toString())+1;
                            String S=String.valueOf(itemINT);

                            sup_month_cho.append(S+",");

                            monthly_notpayedINT+=1;


                        }
                        if(sup_month_cho.getText().toString().length()>=1){
                            Toast.makeText(SupMActivity.this,"تم اختيار الاشهر: " +sup_month_cho.getText().toString(),Toast.LENGTH_LONG).show();
                            total_value2.setText("");
                            total_value2.setText(String.valueOf(((monthly_notpayedINT2+monthly_notpayedINT)*total_valueINT)));
                        }
                        else {
                            total_value2.setText("");
                            total_value2.setText(String.valueOf(((monthly_notpayedINT2+monthly_notpayedINT)*total_valueINT)));
                        }

                    }
                });

        builder1.setNegativeButton(
                "لا",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int s=0;
                        while(s < listItems.length) {
                            MonthItems.remove((Integer.valueOf(s)));
                            TranzItems.remove((Integer.valueOf(s)));


                            s++;

                        }
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    private void MonthlyDialog2(String mess,String mess2,String mess3){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SupMActivity.this);

        builder1.setTitle("الاشهر المختارة:-");
        builder1.setMessage(
                mess.toString()+"\n"+mess2+"\nهل تود تحصيل الاشهر:"+"\n"+mess3
        );


        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "نعم",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        int itemINT;
                        if(total_value.getText().toString().length()>0){
                            total_valueINT=Integer.parseInt(total_value.getText().toString());
                        }
                        else {
                            total_valueINT=0;

                        }

                        monthly_notpayedINT2=0;
                        for (int i = 0; i < TranzItems2.size(); i++) {

                            //  itemINT=Integer.parseInt(item)+1;

                            itemINT=Integer.parseInt(TranzItems2.get(i).toString())+1;
                            String S=String.valueOf(itemINT);

                            sup_month_cho2.append(S+",");

                            monthly_notpayedINT2+=1;


                        }
                        if(sup_month_cho2.getText().toString().length()>=1){
                            Toast.makeText(SupMActivity.this,"تم اختيار الاشهر: " +sup_month_cho2.getText().toString(),Toast.LENGTH_LONG).show();
                            total_value2.setText("");
                            total_value2.setText(String.valueOf(((monthly_notpayedINT2+monthly_notpayedINT)*total_valueINT)));
                        }
                        else {
                            total_value2.setText("");
                            total_value2.setText(String.valueOf(((monthly_notpayedINT2+monthly_notpayedINT)*total_valueINT)));
                        }

                    }
                });

        builder1.setNegativeButton(
                "لا",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int s=0;
                        while(s < listItems.length) {
                            MonthItems.remove((Integer.valueOf(s)));
                            TranzItems.remove((Integer.valueOf(s)));


                            s++;

                        }
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void finishDialog(String mess){
    AlertDialog.Builder builder1 = new AlertDialog.Builder(SupMActivity.this);


            builder1.setMessage(
                    mess.toString()+"\n" +"\nهل تود الاستمرار في تحصيل نفس العميل؟؟"
            );


            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "نعم",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                            finish();
                            startActivity(getIntent());

                        }
                    });

            builder1.setNegativeButton(
                    "لا",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
}





/// /// /// /// /// /// Methods Override for activity //// //// //// //// //// ////





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


            super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }













/// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// END OF CODE //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ////

}
