package com.bconnect.b_connect;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MessListActivity extends AppCompatActivity {
    private String TAG = MessListActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private ListAdapter msListAdapter;
    private static String url;

    ArrayList<HashMap<String, String>> msList;
    String  serurl,serurlAF ;

    private EditText editText ;

    public MessListActivity.get_my_sall_for_EMP myTask;

    int time = 0;
    SwipeRefreshLayout pullToRefresh;

    SharedPreferencesSR SPsr=new SharedPreferencesSR();

    TextView TV_count;

    String emp_code ;

    Integer ITEM;

    Dialog dialog_mess;

    TextView d_mess_title,d_mess_body;

     Button d_button;
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_list);

        basic();
        control();
    }

    public  void basic(){

        dialog_mess = new Dialog(MessListActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        dialog_mess.setContentView(R.layout.dialog_mess_more);
        dialog_mess.setCancelable(true);


        d_mess_title= (TextView) dialog_mess.findViewById(R.id.d_mess_title);
        d_mess_body= (TextView) dialog_mess.findViewById(R.id.d_mess_body);
        d_button= (Button) dialog_mess.findViewById(R.id.d_button);
        TV_count = (TextView) findViewById(R.id.TV_count);


        editText = (EditText) findViewById(R.id.etSearch);
        editText.clearFocus();

        

        lv = (ListView) findViewById(R.id.msList);
 

        emp_code=  SPsr.getSP(getApplicationContext(),"empsercode");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(MessListActivity.this);
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

        d_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialog_mess.cancel();
            }
        });

       
        

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                editText.setText("");
               
                
                url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/get_Message?search=";
                closeKeyboard();
                //amount_cv.setVisibility(View.VISIBLE);
                new MessListActivity.get_my_sall_for_EMP().execute();
                pullToRefresh.setRefreshing(false);
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                HashMap<String, Object> obj = (HashMap<String, Object>) arg0.getAdapter().getItem(position);
              //  BranchCode = (String) obj.get("code");
                    if(((String) obj.get("the_title")).length()>0&&((String) obj.get("the_body")).length()>0){
                        d_mess_title.setText((String) obj.get("the_title"));
                        d_mess_body.setText((String) obj.get("the_body"));
                         dialog_mess.show();
                    }




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
                       
                        

                        serurl=editText.getText().toString();
                        serurlAF=serurl.replace(" ","+");
                        url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/get_Message?search="+serurlAF;
                        myTask = new MessListActivity.get_my_sall_for_EMP();
                        myTask.execute();

                        return true;

                    }
                    if(editText.getText()==null ){
                       
                        

                        url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/get_Message?search=";
                        closeKeyboard();
                        myTask = new MessListActivity.get_my_sall_for_EMP();
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

                           
                            

                            serurl=editText.getText().toString();
                            serurlAF=serurl.replace(" ","+");
                            url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/get_Message?search="+serurlAF;
                            myTask = new MessListActivity.get_my_sall_for_EMP();
                            myTask.execute();


                        }
                        if(editText.getText()==null ){

                           
                            


                            url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/get_Message?search=";
                            myTask = new MessListActivity.get_my_sall_for_EMP();
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


 


        url = "http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/get_Message?search=";

        myTask = new MessListActivity.get_my_sall_for_EMP();
        myTask.execute();


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

                        String ser = c.getString("ser");
                        String the_title = c.getString("the_title");
                        String the_body = c.getString("the_body");



                        HashMap<String, String> contact = new HashMap<>();
                        if(the_title=="null"){
                            the_title="";

                        }

                        if(the_body=="null"){
                            the_body="";

                        }



                        contact.clear();
                        contact.put("ser", ser);
                        contact.put("the_title", the_title);
                        contact.put("the_body", the_body);





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
                        SPsr.removeAllSP(MessListActivity.this);
                        finish();
                        Intent intent = new Intent(MessListActivity.this, LoginActivity.class); intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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


                msListAdapter = new SimpleAdapter(
                        MessListActivity.this, msList,
                        R.layout.mess_list_item, new String[]{"the_title","the_body"
                      },
                        new int[]{R.id.mess_title ,R.id.mess_body });



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
        if (pDialog.isShowing())
        {pDialog.dismiss();}
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
       
        
        editText.setText("");
        url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx//get_Message?search=";
        closeKeyboard();
        myTask = new MessListActivity.get_my_sall_for_EMP();
        myTask.execute();
        super.onResume();


    }
}
