package com.bconnect.b_connect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import java.util.List;

public class AmountPaybackListActivity extends AppCompatActivity {

    private String TAG = AmountPaybackListActivity.class.getSimpleName();
    private ProgressDialog pDialog,pDialog_unpay;
    private ListView lv;
    private ListAdapter AmountPaybackAdapter;
    private static String url;

    ArrayList<HashMap<String, String>> AmountPaybackList;
    ArrayList<HashMap<String, String>> AmountListToActivity;
    String  serurl,serurlAF ,activation_flag,phar_nameToActivity;
    int intToActivity;
    private EditText editText;

    public AsyncTask<Void, Void, Void> myTask;


    int time = 0;
    SwipeRefreshLayout pullToRefresh;
    SharedPreferencesSR SPsr=new SharedPreferencesSR();

    TextView TV_count;
    
    String emp_code;

    CardView amount_cv,LI_cv;

    Button pay_bt,unpay_bt,clear_bt;
    Integer ITEM;

    View v;

    List<Integer> num = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount_payback_list);

        basic();
        control();

    }

    public  void basic(){

        v = new View(AmountPaybackListActivity.this);


        amount_cv=(CardView) findViewById(R.id.amount_cv);

        pay_bt=(Button) findViewById(R.id.pay_bt);
        unpay_bt=(Button) findViewById(R.id.unpay_bt);
        clear_bt=(Button) findViewById(R.id.clear_bt);
        emp_code=  SPsr.getSP(getApplicationContext(),"empsercode");
    
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(AmountPaybackListActivity.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setTitle("جري جلب البيانات...");
        pDialog.setMessage("إنتظر قليلا");
        pDialog.setCancelable(true);

        pDialog_unpay = new ProgressDialog(AmountPaybackListActivity.this);
        pDialog_unpay.setMessage("جاري الان إلغاء التحويل");
        pDialog_unpay.setCancelable(false);

        pullToRefresh = findViewById(R.id.pullToRefresh);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        url = "http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/Get_Amounts_to_Payback_EMP?search=&emp_code="+emp_code+"&is_first=1";

        TV_count = (TextView) findViewById(R.id.TV_count);


        editText = (EditText) findViewById(R.id.etSearch);
        editText.clearFocus();

        lv = (ListView) findViewById(R.id.AmountPaybackList);


        AmountPaybackList = new ArrayList<>();
        AmountListToActivity = new ArrayList<>();
//        new AmountPaybackListActivity.Get_Amounts_to_Payback_EMP().execute();
        myTask = new AmountPaybackListActivity.Get_Amounts_to_Payback_EMP().execute();

         ITEM=0;

    }
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public  void control(){

//        pDialog.setCanceledOnTouchOutside(false);

        CheckList();


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                editText.setText("");
                url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/Get_Amounts_to_Payback_EMP?search=&emp_code="+emp_code+"&is_first=1";
                closeKeyboard();
                new AmountPaybackListActivity.Get_Amounts_to_Payback_EMP().execute();
                pullToRefresh.setRefreshing(false);
            }
        });


        pay_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("AmountListToActivity",AmountListToActivity);

                Intent intent=new Intent(AmountPaybackListActivity.this,AmountPAActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        clear_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 if(AmountListToActivity.size()==0){
                     for (int i = 0; i < lv.getCount(); i++) {
                         lv.setItemChecked(i, true);
                         HashMap<String, String> obj = (HashMap<String, String>) lv.getAdapter().getItem(i);
                         Integer yes = 1;

                         if(AmountListToActivity.size()==0) {
                             AmountListToActivity.add(obj);

                             // LI_cv.setBackgroundColor(Color.GREEN);
                             ITEM=AmountListToActivity.size();
                             pay_bt.setText( "تسديد ( "+String.valueOf(ITEM)+" )");
                             CheckList();

                         }else {
                             for(int j=0;i<AmountListToActivity.size();j++) {

                                 if(AmountListToActivity.get(j).get("id").contains((String) obj.get("id"))) {
                                     Log.e(TAG, "AmountListToActivity equals: " + "YES");
                                     yes=0;


                                 }


                             }

                             if(yes==1){
                                 AmountListToActivity.add(obj);
                                 //Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "تم اختيار  "+obj.get("phar_name")+" - "+obj.get("desc_ar")+" - "+obj.get("amt"), Snackbar.LENGTH_SHORT);
                                 //snackbar.show();
                                 ITEM=AmountListToActivity.size();
                                 pay_bt.setText( "تسديد ( "+String.valueOf(ITEM)+" )");
                                 CheckList();


                             }



                         }


                     }
                     Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "تم اختيار  "+AmountListToActivity.size()+" مبلغ", Snackbar.LENGTH_SHORT);
                     snackbar.show();

                 }
                 else {

                     for (int h = 0; h < lv.getCount(); h++) {


                             AmountListToActivity.clear();

                             lv.setItemChecked(h, false);
                             ITEM=AmountListToActivity.size();


                     }
                     if(AmountListToActivity.size()==0){
                         pay_bt.setText( "تسديد ( "+String.valueOf(ITEM)+" )");
                         CheckList();

                         Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "  تم الغاء اختيار الكل", Snackbar.LENGTH_SHORT);
                         snackbar.show();
                     }

                 }

            }
        });


         lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL); // Important


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                HashMap<String, String> obj = (HashMap<String, String>) arg0.getAdapter().getItem(position);
                phar_nameToActivity = (String) obj.get("phar_name");

                Integer yes = 1;

                if(AmountListToActivity.size()==0) {
                    AmountListToActivity.add(obj);
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "تم اختيار  "+obj.get("phar_name")+" - "+obj.get("desc_ar")+" - "+obj.get("amt"), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                   // LI_cv.setBackgroundColor(Color.GREEN);
                    ITEM=AmountListToActivity.size();
                    pay_bt.setText( "تسديد ( "+String.valueOf(ITEM)+" )");
                    CheckList();

                }else {
                    for(int i=0;i<AmountListToActivity.size();i++) {

                        if(AmountListToActivity.get(i).get("id").contains((String) obj.get("id"))) {
                            Log.e(TAG, "AmountListToActivity equals: " + "YES");
                            yes=0;

                            AmountListToActivity.remove(i);
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "تم الغاء اختيار  "+obj.get("phar_name"), Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            ITEM=AmountListToActivity.size();
                            pay_bt.setText( "تسديد ( "+String.valueOf(ITEM)+" )");
                            CheckList();
                        }


                    }

                    if(yes==1){
                        AmountListToActivity.add(obj);
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "تم اختيار  "+obj.get("phar_name")+" - "+obj.get("desc_ar")+" - "+obj.get("amt"), Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        ITEM=AmountListToActivity.size();
                        pay_bt.setText( "تسديد ( "+String.valueOf(ITEM)+" )");
                        CheckList();


                    }



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
                        url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/Get_Amounts_to_Payback_EMP?search="+serurlAF+"&emp_code="+emp_code+"&is_first=0";
                        new AmountPaybackListActivity.Get_Amounts_to_Payback_EMP().execute();

                        return true;

                    }
                    if(editText.getText()==null ){
                        url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/Get_Amounts_to_Payback_EMP?search=&emp_code="+emp_code+"&is_first=0";
                        closeKeyboard();
                        myTask = new AmountPaybackListActivity.Get_Amounts_to_Payback_EMP().execute();

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
                            url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/Get_Amounts_to_Payback_EMP?search="+serurlAF+"&emp_code="+emp_code+"&is_first=0";
                            new AmountPaybackListActivity.Get_Amounts_to_Payback_EMP().execute();


                        }
                        if(editText.getText()==null ){
                            url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/Get_Amounts_to_Payback_EMP?search=&emp_code="+emp_code+"&is_first=0";
                            myTask = new AmountPaybackListActivity.Get_Amounts_to_Payback_EMP().execute();
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






        //////////////////////////////////////////// send data ////////////////////////////////////////////



        unpay_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (AmountListToActivity.size()<1) {


                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "يجب ان تختار المبالغ اولا" , Snackbar.LENGTH_LONG);
                    snackbar.show();
                }



                if(AmountListToActivity.size()>0) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AmountPaybackListActivity.this);


                    builder1.setMessage(
                            "هل انت متأكد انك تريد إلغاء التحويل؟"
                                    + "\n"
                    );


                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "تأكيد",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    new AmountPaybackListActivity.unpay_Fees().execute();


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

    private void CheckList(){
        if (AmountListToActivity.size() == 0) {
            pay_bt.setEnabled(false);
            unpay_bt.setEnabled(false);
            clear_bt.setText("تحديد الكل");
            clear_bt.setBackgroundColor(Color.GREEN);
        } else {
            pay_bt.setEnabled(true);
            unpay_bt.setEnabled(true);
            clear_bt.setText("الغاء تحديد الكل");
            clear_bt.setBackgroundColor(Color.RED);
        }
    }

    private class Get_Amounts_to_Payback_EMP extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.show();

            AmountListToActivity.clear();
            CheckList();

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

                    JSONArray contacts = jsonObj.getJSONArray("Table");
                    AmountPaybackList.clear();

                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("id");
                        String serial = c.getString("serial");
                        String cl_code = c.getString("cl_code");
                        String phar_name = c.getString("phar_name");
                        String mot_code= c.getString("mot_code");
                        String amt= c.getString("amt1");
                        String desc_ar = c.getString("desc_ar");
                        String table_name = c.getString("table_name");
                        String monthlyDate= c.getString("the_paied_year")+"/"+c.getString("the_paied_month");
                        String monthlyM= c.getString("the_paied_month");
                        String monthlyY= c.getString("the_paied_year");
                        String reciept_serial= c.getString("reciept_serial");
                        String hewala_ser= c.getString("hewala_ser");
                        String hewala_date= c.getString("hewala_date");
                        String pay_way1_disc= c.getString("pay_way1_disc")+" - "+c.getString("the_bank_name");


                        HashMap<String, String> contact = new HashMap<>();
                        if(monthlyM=="null"){
                            monthlyDate="";
                            monthlyM="";
                            monthlyY="";
                        }
                        if(hewala_ser=="null"){
                            hewala_ser="";
                            hewala_date="";
                            pay_way1_disc="";
//                            the_bank_name="";

                        }
                        contact.clear();
                        contact.put("id", id);
                        contact.put("serial", serial);
                        contact.put("cl_code", cl_code);
                        contact.put("phar_name", phar_name);
                        contact.put("mot_code", mot_code.replace(".0",""));
                        contact.put("amt", amt.replace(".0",""));
                        contact.put("desc_ar", desc_ar);
                        contact.put("table_name", table_name);
                        contact.put("monthlyDate", monthlyDate);
                        contact.put("monthlyM", monthlyM);
                        contact.put("monthlyY", monthlyY);
                        contact.put("reciept_serial", reciept_serial.replace(".0",""));
                        contact.put("hewala_ser", hewala_ser);
                        contact.put("hewala_date", hewala_date);
                        contact.put("pay_way1_disc", pay_way1_disc);
//                        contact.put("the_bank_name", the_bank_name);



                        AmountPaybackList.add(contact);





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
                        SPsr.removeAllSP(AmountPaybackListActivity.this);
                        finish();
                        Intent intent = new Intent(AmountPaybackListActivity.this, LoginActivity.class); intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

                AmountPaybackAdapter = new SimpleAdapter(
                        AmountPaybackListActivity.this, AmountPaybackList,
                        R.layout.amount_payback_list_item, new String[]{"phar_name","mot_code","amt","desc_ar","monthlyDate",
                         "reciept_serial","hewala_ser","hewala_date","pay_way1_disc"},
                        new int[]{R.id.client_name ,R.id.client_code,R.id.client_value, R.id.client_service
                        , R.id.client_monthly_date,R.id.client_rec_no,R.id.client_hewala_no,R.id.client_hewala_date,R.id.client_pay_type});



                lv.setAdapter(null);

                lv.setAdapter(AmountPaybackAdapter);
                lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
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

    private class unpay_Fees extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

            closeKeyboard();

            pDialog_unpay.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {

                WebServiceCall webc =new WebServiceCall(AmountPaybackListActivity.this);



                if(AmountListToActivity.size()>0){




                        for(int i=0;i<AmountListToActivity.size();i++) {
                            Log.e(TAG, "table_name: " + AmountListToActivity.get(i).get("table_name").toString());


                            webc.update_fees_for_AP("0",AmountListToActivity.get(i).get("table_name").toString(),"","","","",AmountListToActivity.get(i).get("serial").toString(),AmountListToActivity.get(i).get("reciept_serial").toString());



                        }


                }
            }
            catch(Exception ex)
            {
                Toast.makeText(getApplicationContext(),
                        "تعذر الإتصال بالإنترنت, راجع مزود الإنترنت",
                        Toast.LENGTH_LONG)
                        .show();
                Log.e(TAG, "Clear_Fees: " + ex.getMessage());


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            try{

                // Dismiss the progress dialog
                if (pDialog_unpay.isShowing()) {
                    pDialog_unpay.dismiss();
                }

                if(WebServiceResult.ErrorID==0){





                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "تمت العملية بنجاح", Snackbar.LENGTH_LONG);
                    snackbar.show();

                    if(editText.getText().toString().length()>0){
                        url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/Get_Amounts_to_Payback_EMP?search="+editText.getText().toString()+"&emp_code="+emp_code+"&is_first=1";
                    }
                    else if(editText.getText().toString().length()<1){
                        url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/Get_Amounts_to_Payback_EMP?search=&emp_code="+emp_code+"&is_first=1";
                    }

                    for (int h = 0; h < lv.getCount(); h++) {


                        AmountListToActivity.clear();

                        lv.setItemChecked(h, false);
                        ITEM=AmountListToActivity.size();


                    }
                    if(AmountListToActivity.size()==0){
                        pay_bt.setText( "تسديد ( "+String.valueOf(ITEM)+" )");
                        CheckList();

                    }

                    new AmountPaybackListActivity.Get_Amounts_to_Payback_EMP().execute();



                }
                if(WebServiceResult.ErrorID==1){

                    Toast.makeText(getApplicationContext(),
                            "حدث خطأ",
                            Toast.LENGTH_LONG)
                            .show();
                    Log.e(TAG, "Clear_Fees: " + WebServiceResult.ErrorMessage.toString());

                    AmountPaybackListActivity.super.onBackPressed();


                }

            }
            catch (Exception ex) {


                Log.e(TAG, "Clear_Fees: " + ex.getMessage());

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
        url="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/android.asmx/Get_Amounts_to_Payback_EMP?search=&emp_code="+emp_code+"&is_first=1";
        closeKeyboard();
        //amount_cv.setVisibility(View.VISIBLE);
        new AmountPaybackListActivity.Get_Amounts_to_Payback_EMP().execute();
        for (int h = 0; h < lv.getCount(); h++) {


            AmountListToActivity.clear();

            lv.setItemChecked(h, false);
            ITEM=AmountListToActivity.size();


        }
        if(AmountListToActivity.size()==0){
            pay_bt.setText( "تسديد ( "+String.valueOf(ITEM)+" )");
            CheckList();

        }
        super.onResume();


    }
}
