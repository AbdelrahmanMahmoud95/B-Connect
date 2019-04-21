package com.bconnect.b_connect;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MySaleListActivity extends AppCompatActivity {

    private String TAG = MySaleListActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private ListAdapter msListAdapter;
    private static String url;

    ArrayList<HashMap<String, String>> msList;
    String  serurl,serurlAF ;

    private EditText editText,ms_date_from,ms_date_to;

    public MySaleListActivity.get_my_sall_for_EMP myTask;

    int time = 0;
    SwipeRefreshLayout pullToRefresh;

    SharedPreferencesSR SPsr=new SharedPreferencesSR();

    TextView TV_count;

    String emp_code,ms_date_fromAF,ms_date_toAF;

    Integer ITEM;

    Calendar from_Date_myCalendar,to_Date_myCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sale_list);

        basic();
        control();
    }

    public  void basic(){
        TV_count = (TextView) findViewById(R.id.TV_count);


        editText = (EditText) findViewById(R.id.etSearch);
        editText.clearFocus();

        ms_date_from= (EditText) findViewById(R.id.ms_date_from);
        ms_date_to= (EditText) findViewById(R.id.ms_date_to);

        lv = (ListView) findViewById(R.id.msList);

        from_Date_myCalendar = Calendar.getInstance();
        to_Date_myCalendar = Calendar.getInstance();


        emp_code=  SPsr.getSP(getApplicationContext(),"empsercode");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(MySaleListActivity.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setTitle("جري جلب البيانات...");
        pDialog.setMessage("إنتظر قليلا");
        pDialog.setCancelable(true);

        pullToRefresh = findViewById(R.id.pullToRefresh);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);






        msList = new ArrayList<>();



        ITEM=0;

    }

    public  void control(){


        ms_date_from_updateData();
        ms_date_to_updateData();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                editText.setText("");
                ms_date_from_updateData();
                ms_date_to_updateData();
                url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/get_my_sall_for_EMP?search=&rep_code="+emp_code+"&StartDate="+ms_date_fromAF+"&EndDate="+ms_date_toAF;
                closeKeyboard();
                //amount_cv.setVisibility(View.VISIBLE);
                new MySaleListActivity.get_my_sall_for_EMP().execute();
                pullToRefresh.setRefreshing(false);
            }
        });


      

        lv.setOnTouchListener(new View.OnTouchListener() {
            float height;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editText.clearFocus();

                closeKeyboard();

 

                return false;

            }
        });

 

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openKeyboard();
            }
        });
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    if(editText.getText()!=null){
                        ms_date_from_updateData();
                        ms_date_to_updateData();

                        serurl=editText.getText().toString();
                        serurlAF=serurl.replace(" ","+");
                        url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/get_my_sall_for_EMP?search="+serurlAF+"&rep_code="+emp_code+"&StartDate="+ms_date_fromAF+"&EndDate="+ms_date_toAF;
                        myTask = new MySaleListActivity.get_my_sall_for_EMP();
                        myTask.execute();

                        return true;

                    }
                    if(editText.getText()==null ){
                        ms_date_from_updateData();
                        ms_date_to_updateData();

                        url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/get_my_sall_for_EMP?search=&rep_code="+emp_code+"&StartDate="+ms_date_fromAF+"&EndDate="+ms_date_toAF;
                        closeKeyboard();
                        myTask = new MySaleListActivity.get_my_sall_for_EMP();
                        myTask.execute();

                        return true;
                    }
                }



                return false;

            }

        });

        final long delay = 1000; // 1 seconds after user stops typing
        final long[] last_text_edit = {0};
        final Handler handler = new Handler();


        final Runnable input_finish_checker = new Runnable() {
            public void run() {


                if (System.currentTimeMillis() > (last_text_edit[0] + delay - 500)) {
                    time  +=1;
                    Log.e(TAG, "time" + String.valueOf(time));
                    if(time==1){
                        if(editText.getText()!=null){
                            ms_date_from_updateData();
                            ms_date_to_updateData();

                            serurl=editText.getText().toString();
                            serurlAF=serurl.replace(" ","+");
                            url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/get_my_sall_for_EMP?search="+serurlAF+"&rep_code="+emp_code+"&StartDate="+ms_date_fromAF+"&EndDate="+ms_date_toAF;
                            myTask = new MySaleListActivity.get_my_sall_for_EMP();
                            myTask.execute();

                        }
                        if(editText.getText()==null ){

                            ms_date_from_updateData();
                            ms_date_to_updateData();


                            url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/get_my_sall_for_EMP?search=&rep_code="+emp_code+"&StartDate="+ms_date_fromAF+"&EndDate="+ms_date_toAF;
                            myTask = new MySaleListActivity.get_my_sall_for_EMP();
                            myTask.execute();
                        }
                    }


                }
            }
        };

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() > 0) {
                    last_text_edit[0] = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
                    time  =0;

                } else {

                }
            }
        });

        final DatePickerDialog.OnDateSetListener  ms_date_from_datepicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                from_Date_myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                from_Date_myCalendar.set(Calendar.MONTH, monthOfYear);
                from_Date_myCalendar.set(Calendar.YEAR, year);

                ms_date_from_updateData();

                url = "http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/get_my_sall_for_EMP?search=&rep_code="+emp_code+"&StartDate="+ms_date_fromAF+"&EndDate="+ms_date_toAF;

                myTask = new MySaleListActivity.get_my_sall_for_EMP();
                myTask.execute();

            }

        };



        ms_date_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MySaleListActivity.this, ms_date_from_datepicker,
                        from_Date_myCalendar.get(Calendar.YEAR),
                        from_Date_myCalendar.get(Calendar.MONTH),
                        from_Date_myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        final DatePickerDialog.OnDateSetListener  ms_date_to_datepicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                to_Date_myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                to_Date_myCalendar.set(Calendar.MONTH, monthOfYear);
                to_Date_myCalendar.set(Calendar.YEAR, year);

                ms_date_to_updateData();

                url = "http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/get_my_sall_for_EMP?search=&rep_code="+emp_code+"&StartDate="+ms_date_fromAF+"&EndDate="+ms_date_toAF;

                myTask = new MySaleListActivity.get_my_sall_for_EMP();
                myTask.execute();

            }

        };



        ms_date_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(MySaleListActivity.this, ms_date_to_datepicker,
                        to_Date_myCalendar.get(Calendar.YEAR),
                        to_Date_myCalendar.get(Calendar.MONTH),
                        to_Date_myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();




            }
        });


        url = "http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/get_my_sall_for_EMP?search=&rep_code="+emp_code+"&StartDate="+ms_date_fromAF+"&EndDate="+ms_date_toAF;

        myTask = new MySaleListActivity.get_my_sall_for_EMP();
        myTask.execute();
 

    }

    private void ms_date_from_updateData() {

        try {
            SimpleDateFormat ms_date_fromSDF2 = new SimpleDateFormat("dd/MM/yyyy");
            ms_date_fromAF = ms_date_fromSDF2.format( from_Date_myCalendar.getTime()).toString();
            SimpleDateFormat ms_date_fromSDF = new SimpleDateFormat("yyyy/MM/dd");
            String  ms_date_fromS = ms_date_fromSDF.format( from_Date_myCalendar.getTime()).toString();

            ms_date_from.setText(ms_date_fromS);

            Log.e(TAG, "ms_date_fromAF:" +ms_date_fromAF);



        }
        catch (Exception e) {
            //The handling for the code
        }

    }
    private void ms_date_to_updateData() {

        try {
            SimpleDateFormat ms_date_toSDF2 = new SimpleDateFormat("dd/MM/yyyy");
            ms_date_toAF = ms_date_toSDF2.format( to_Date_myCalendar.getTime()).toString();
            SimpleDateFormat ms_date_toSDF = new SimpleDateFormat("yyyy/MM/dd");
            String  ms_date_toS = ms_date_toSDF.format( to_Date_myCalendar.getTime()).toString();

            ms_date_to.setText(ms_date_toS);

            Log.e(TAG, "ms_date_toAF:" +ms_date_toAF);

        }
        catch (Exception e) {
            //The handling for the code
        }

    }


    private class get_my_sall_for_EMP extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog



            pDialog.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    myTask.cancel(true);
                }
            }, 30000);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            WebServiceListHttpHolder sh = new WebServiceListHttpHolder();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("Table");
                    msList.clear();

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String code = c.getString("code");
                        String branch = c.getString("Column1");
                        String phar_name = c.getString("phar_name");
                        String motaheda_code = c.getString("motaheda_code");
                        String address= c.getString("address");
                        String cont_type= c.getString("Column3");
                        String prog_name = c.getString("Column4");
                        String down_pay = c.getString("down_pay");
                        String remaining= c.getString("remaining") ;
                        String date_created= c.getString("date_created");
                        String phar_type= c.getString("Column7");
                        String snotes= c.getString("snotes");
                        String super_notes= c.getString("super_notes");
                        String cl_stage= c.getString("Column8");
                        String app_ar= c.getString("app_ar");
                        String monthly_fee= c.getString("monthly_fee");

                        HashMap<String, String> contact = new HashMap<>();


                        if(snotes=="null"){
                            snotes="";

                        }
                        if(super_notes=="null"){
                            super_notes="";

                        }


                        contact.clear();
                        contact.put("code", code);
                        contact.put("branch", branch);
                        contact.put("phar_name", phar_name);
                        contact.put("motaheda_code", motaheda_code.replace(".0",""));
                        contact.put("address", address);
                        contact.put("cont_type", cont_type);
                        contact.put("prog_name", prog_name);
                        contact.put("down_pay", down_pay.replace(".0",""));
                        contact.put("remaining", remaining.replace(".0",""));
                        contact.put("monthly_fee", monthly_fee);
                        contact.put("date_created", date_created.substring(0, 10).replace("-","/"));
                        contact.put("phar_type", phar_type);
                        contact.put("snotes", snotes);
                        contact.put("super_notes", super_notes);
                        contact.put("cl_stage", cl_stage);
                        contact.put("app_ar", app_ar);



                        msList.add(contact);





                        // adding contact to contact list

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "تعذر جلب البيانات, راجع مزود الإنترنت",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SPsr.removeAllSP(MySaleListActivity.this);
                        finish();
                        Intent intent = new Intent(MySaleListActivity.this, LoginActivity.class); intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            editText = (EditText) findViewById(R.id.etSearch);

            try{   // Dismiss the progress dialog
                if (pDialog.isShowing()){pDialog.dismiss();}



                msListAdapter = new SimpleAdapter(
                        MySaleListActivity.this, msList,
                        R.layout.my_sall_list_item, new String[]{"phar_name","motaheda_code","cl_stage","app_ar","branch","cont_type","remaining","monthly_fee","date_created",
                        "snotes","super_notes"},
                        new int[]{R.id.client_name ,R.id.client_code,R.id.client_app, R.id.client_stage
                                , R.id.client_branch,R.id.client_con_type,R.id.client_down_pay,R.id.client_monthly_val,R.id.client_send_date,R.id.client_note,R.id.client_snote });



                lv.setAdapter(null);

                lv.setAdapter(msListAdapter);
                try{
                    TV_count.setText("عدد النتائج: "+String.valueOf(lv.getAdapter().getCount()));
                }catch (NullPointerException e){
                    TV_count.setText("");
                }
            }
            catch (Exception ex) {
                Toast.makeText(getApplicationContext(),
                        "خطأ" ,
                        Toast.LENGTH_LONG)
                        .show();

                Log.e(TAG, "LIST Error: " + ex.getMessage());

            }


        }

        @Override
        protected void onCancelled(Void result) {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }


    }



    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void openKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY); // show
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        myTask.cancel(true);
        if (pDialog.isShowing()){pDialog.dismiss();}

        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        ms_date_from_updateData();
        ms_date_to_updateData();
        editText.setText("");
        url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx//get_my_sall_for_EMP?search=&rep_code="+emp_code+"&StartDate="+ms_date_fromAF+"&EndDate="+ms_date_toAF;
        closeKeyboard();
        myTask = new MySaleListActivity.get_my_sall_for_EMP();
        myTask.execute();
        super.onResume();


    }

}
