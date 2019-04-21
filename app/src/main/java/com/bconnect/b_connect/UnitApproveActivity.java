package com.bconnect.b_connect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UnitApproveActivity extends AppCompatActivity {

    SharedPreferencesSR SPsr=new SharedPreferencesSR();


    private String TAG = ClientByBranchActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private ListAdapter branchadapter;
    private static String url;

    TextView pharm_name,pharm_code,pharm_add,pharm_phone_line,pharm_phone_mobile, pharm_doc_name;

    Button bt_re;
    ArrayList<HashMap<String, String>> branchlist;
    String  PharCode,PharCodeAF,serurl,serurlAF,BranchCodeToActivity,activation_flag;

    private EditText editText;

    SharedPreferences pref ;
    SharedPreferences.Editor editor;
    String asm,ser,empsercode,emp_name;

    public AsyncTask<Void, Void, Void> myTask;

    SwipeRefreshLayout pullToRefresh;

    int time = 0;
      long delay = 1000; // 1 seconds after user stops typing
      long[] last_text_edit = {0};
      Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_approve);

        basic();

        control();



    }

    public  void basic(){
        pref= getApplicationContext().getSharedPreferences("MySr", MODE_PRIVATE);
        editor = pref.edit();

        empsercode= SPsr.getSP(getApplicationContext(),"empsercode");
        emp_name="";
        emp_name= SPsr.getSP(getApplicationContext(),"empsername");



        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Intent intent = getIntent();

        Log.e(TAG, "empsercode: " + empsercode);

        url = "http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/Get_uni_client_for_emp?emp_code="+empsercode+"&phar_name=";

        editText = (EditText) findViewById(R.id.etSearch);
        editText.clearFocus();

        lv = (ListView) findViewById(R.id.branch_list);
        bt_re=findViewById(R.id.bt_re);

        branchlist = new ArrayList<>();

        pDialog = new ProgressDialog(UnitApproveActivity.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setTitle("إنتظر قليلا");
        pDialog.setMessage("جري جلب البيانات...");
        pDialog.setCancelable(true);

        pullToRefresh = findViewById(R.id.pullToRefresh);

        myTask= new UnitApproveActivity.GetClient_by_DCode().execute();

    }
    public  void control(){



        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myTask= new UnitApproveActivity.GetClient_by_DCode().execute();

                pullToRefresh.setRefreshing(false);
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                HashMap<String, Object> obj = (HashMap<String, Object>) arg0.getAdapter().getItem(position);
                PharCode= (String) obj.get("code");

                Log.e(TAG, "PharCode PharCode: " + PharCode);

                myTask= new UnitApproveActivity.Get_invoice_data().execute();



            }
        });
        lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editText.clearFocus();

                closeKeyboard();
                return false;

            }
        });

        bt_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText()!=null){
                    serurl=editText.getText().toString();
                    serurlAF=serurl.replace(" ","+");
                    url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/Get_uni_client_for_emp?emp_code="+empsercode+"&phar_name="+serurlAF;
                    myTask= new UnitApproveActivity.GetClient_by_DCode().execute();

                }
                if(editText.getText()==null ){
                    url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/Get_uni_client_for_emp?emp_code="+empsercode+"&phar_name=";
                    closeKeyboard();
                    myTask= new UnitApproveActivity.GetClient_by_DCode().execute();
                }
            }
        });


        final Runnable input_finish_checker = new Runnable() {
            public void run() {


                if (System.currentTimeMillis() > (last_text_edit[0] + delay - 500)) {
                    time  +=1;
                    Log.e(TAG, "time" + String.valueOf(time));
                    if(time==1){
                        if(editText.getText().length()>=1){
                            serurl=editText.getText().toString();
                            serurlAF=serurl.replace(" ","+");
                            url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/Get_uni_client_for_emp?emp_code="+empsercode+"&phar_name="+serurlAF;
                            myTask= new UnitApproveActivity.GetClient_by_DCode().execute();

                        }
                        if(editText.getText().length()<1 ){
                            url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/Get_uni_client_for_emp?emp_code="+empsercode+"&phar_name=";
                            closeKeyboard();
                            myTask= new UnitApproveActivity.GetClient_by_DCode().execute();

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
                        serurl=editText.getText().toString();
                        serurlAF=serurl.replace(" ","+");
                        url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/Get_uni_client_for_emp?emp_code="+empsercode+"&phar_name="+editText.getText().toString()+"";
                        myTask= new UnitApproveActivity.GetClient_by_DCode().execute();

                    }
                    if(editText.getText()==null ){
                        url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/Get_uni_client_for_emp?emp_code="+empsercode+"&phar_name=";
                        closeKeyboard();
                        myTask= new UnitApproveActivity.GetClient_by_DCode().execute();
                    }
                }

//                editText.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                                    if(charSequence.length()<=0){
//                                        url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/getclient_by_DCode?search="+BranchCodeToActivity+"&activation_flag="+activation_flag+"&name=";
//                                         new ClientByBranchActivity.GetClient_by_DCode().execute();
//                                    }
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable editable) {
//
//
//                    }
//                });

                return false;

            }

        });

    }


    private class GetClient_by_DCode extends AsyncTask<Void, Void, Void> {

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
                    branchlist.clear();

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        float add_no_F=0;
                        int add_no_INT=0;
                        String code = c.getString("code");
                        String phar_name = c.getString("phar_name");
                       // String add_no = c.getString("add_no");
                        add_no_F=Float.parseFloat(c.getString("add_no"));
                        add_no_INT=Math.round(add_no_F);
                        String add_no =String.valueOf(add_no_INT);
                        HashMap<String, String> contact = new HashMap<>();

                        contact.clear();
                        contact.put("code", code);
                        contact.put("phar_name", phar_name);
                        contact.put("add_no", add_no);
                        //                            contact.put("mobile", mobile);

                        branchlist.add(contact);





                        // adding contact to contact list

                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "تعذر الإتصال بالإنترنت, راجع مزود الإنترنت",
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
                        SPsr.removeAllSP(UnitApproveActivity.this);
                        finish();
                        Intent intent = new Intent(UnitApproveActivity.this, LoginActivity.class); intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                if (pDialog.isShowing())
                {pDialog.dismiss();}
                /**
                 * Updating parsed JSON data into ListView
                 */

                branchadapter = new SimpleAdapter(
                        UnitApproveActivity.this, branchlist,
                        R.layout.unit_approve_list_item, new String[]{"phar_name","add_no"}, new int[]{R.id.client_name ,R.id.uni_no });
                lv.setAdapter(null);

                lv.setAdapter(branchadapter);
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
            try{
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }catch (Exception ex){


            }

        }



    }


    private class Get_invoice_data extends AsyncTask<Void, Void, Void> {

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

            try
            {
                WebServiceUniData.ErrorID=5;

                WebServiceCall webc =new WebServiceCall(UnitApproveActivity.this);
                 webc.Get_invoice_data(PharCode,empsercode);


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

                if(WebServiceUniData.ErrorID==0){



                        Intent i = new Intent(UnitApproveActivity.this, SupUnitActivity.class);
                        i.putExtra("PharCode", PharCode);
                        startActivity(i);



                }
                if( WebServiceUniData.ErrorID==1){

                    Toast.makeText(getApplicationContext(),
                            "هذه الوحدة  في انتظار الموافقة",
                            Toast.LENGTH_LONG)
                            .show();
                }



            }
            catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }





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
    protected void onResume() {

        url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/Get_uni_client_for_emp?emp_code="+empsercode+"&phar_name=";
        myTask= new UnitApproveActivity.GetClient_by_DCode().execute();

        super.onResume();
    }
}
