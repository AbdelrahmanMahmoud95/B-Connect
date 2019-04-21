package com.bconnect.b_connect;

import android.app.Activity;
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

public class ClientByBranchActivity extends AppCompatActivity {

    private String TAG = ClientByBranchActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private ListAdapter branchadapter;
    private static String url;

    Button bt_re;
    ArrayList<HashMap<String, String>> branchlist;
    String MotahedaCode, PharCode, PharCodeAF, MotahedaCodeAF, serurl, serurlAF, BranchCodeToActivity, ToActivity, MotahedaCodeToActivity, activation_flag, phar_nameToActivity;
    int intToActivity;
    private EditText editText;

    public AsyncTask<Void, Void, Void> myTask;


    int time = 0;
    SwipeRefreshLayout pullToRefresh;

    SharedPreferencesSR SPsr = new SharedPreferencesSR();

    TextView TV_count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_by_branch);

        basic();
        control();


    }

    public void basic() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(ClientByBranchActivity.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setTitle("جري جلب البيانات...");
        pDialog.setMessage("إنتظر قليلا");
        pDialog.setCancelable(true);

        pullToRefresh = findViewById(R.id.pullToRefresh);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Intent intent = getIntent();
        BranchCodeToActivity = intent.getExtras().getString("BranchCodeToActivity");
        activation_flag = intent.getExtras().getString("activation_flag");
        ToActivity = intent.getExtras().getString("ToActivity");
        intToActivity = Integer.parseInt(ToActivity);

        url = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/android.asmx/getclient_by_DCode?search=" + BranchCodeToActivity + "&activation_flag=" + activation_flag + "&name=";

        TV_count = (TextView) findViewById(R.id.TV_count);


        editText = (EditText) findViewById(R.id.etSearch);
        editText.clearFocus();

        lv = (ListView) findViewById(R.id.branch_list);
        bt_re = findViewById(R.id.bt_re);

        branchlist = new ArrayList<>();
//        new ClientByBranchActivity.GetClient_by_DCode().execute();
        myTask = new ClientByBranchActivity.GetClient_by_DCode().execute();


    }

    public void control() {

//        pDialog.setCanceledOnTouchOutside(false);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                url = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/android.asmx/getclient_by_DCode?search=" + BranchCodeToActivity + "&activation_flag=" + activation_flag + "&name=";
                closeKeyboard();
                new ClientByBranchActivity.GetClient_by_DCode().execute();
                pullToRefresh.setRefreshing(false);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                HashMap<String, Object> obj = (HashMap<String, Object>) arg0.getAdapter().getItem(position);
                phar_nameToActivity = (String) obj.get("phar_name");
                PharCode = (String) obj.get("cl_code");
                MotahedaCode = (String) obj.get("motaheda_code");
                MotahedaCodeAF = MotahedaCode.replace(".0", "");
                //PharCodeAF=PharCode.replace(".0","");
                //      Object object
                Toast.makeText(ClientByBranchActivity.this, "" + MotahedaCodeAF, Toast.LENGTH_SHORT).show();
                MotahedaCodeToActivity = MotahedaCodeAF;
                if (intToActivity == 1) {
                    finish();

                    Intent i = new Intent(ClientByBranchActivity.this, EditContActivity.class);
                    i.putExtra("MotahedaCodeToActivity", MotahedaCodeToActivity);
                    i.putExtra("BranchCodeToActivity", BranchCodeToActivity);
                    i.putExtra("PharCode", PharCode);
                    startActivity(i);

                }
                if (intToActivity == 22) {
                    Intent i = new Intent(ClientByBranchActivity.this, BranchsActivity.class);
                    i.putExtra("phar_nameToActivity", phar_nameToActivity);
                    setResult(RESULT_OK, i);
                    finish();
                }

                if (intToActivity == 3) {
                    finish();

                    Intent i = new Intent(ClientByBranchActivity.this, OrderActivity.class);
                    i.putExtra("MotahedaCodeToActivity", MotahedaCodeToActivity);
                    i.putExtra("BranchCodeToActivity", BranchCodeToActivity);
                    i.putExtra("PharCode", PharCode);
                    startActivity(i);

                }

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
                if (editText.getText().length() >= 1) {
                    serurl = editText.getText().toString();
                    serurlAF = serurl.replace(" ", "+");
                    url = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/android.asmx/getclient_by_DCode?search=" + BranchCodeToActivity + "&activation_flag=" + activation_flag + "&name=" + serurlAF;
                    new ClientByBranchActivity.GetClient_by_DCode().execute();
                }
                if (editText.getText().length() < 1) {
                    url = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/android.asmx/getclient_by_DCode?search=" + BranchCodeToActivity + "&activation_flag=" + activation_flag + "&name=";
                    closeKeyboard();
                    new ClientByBranchActivity.GetClient_by_DCode().execute();
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
                    if (editText.getText() != null) {
                        serurl = editText.getText().toString();
                        serurlAF = serurl.replace(" ", "+");
                        url = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/android.asmx/getclient_by_DCode?search=" + BranchCodeToActivity + "&activation_flag=" + activation_flag + "&name=" + serurlAF;
                        new ClientByBranchActivity.GetClient_by_DCode().execute();

                        return true;

                    }
                    if (editText.getText() == null) {
                        url = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/android.asmx/getclient_by_DCode?search=" + BranchCodeToActivity + "&activation_flag=" + activation_flag + "&name=";
                        closeKeyboard();
                        myTask = new ClientByBranchActivity.GetClient_by_DCode().execute();

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
                    time += 1;
                    Log.e(TAG, "time" + String.valueOf(time));
                    if (time == 1) {
                        if (editText.getText() != null) {
                            serurl = editText.getText().toString();
                            serurlAF = serurl.replace(" ", "+");
                            url = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/android.asmx/getclient_by_DCode?search=" + BranchCodeToActivity + "&activation_flag=" + activation_flag + "&name=" + serurlAF;
                            new ClientByBranchActivity.GetClient_by_DCode().execute();


                        }
                        if (editText.getText() == null) {
                            url = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/android.asmx/getclient_by_DCode?search=" + BranchCodeToActivity + "&activation_flag=" + activation_flag + "&name=";
                            myTask = new ClientByBranchActivity.GetClient_by_DCode().execute();
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
                    time = 0;

                } else {

                }
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

                        String motaheda_code = c.getString("motaheda_code");
                        String phar_name = c.getString("phar_name");
                        String address = c.getString("address");
                        String cl_code = c.getString("cl_code");

                        HashMap<String, String> contact = new HashMap<>();

                        contact.clear();
                        contact.put("motaheda_code", motaheda_code);
                        contact.put("phar_name", phar_name);
                        contact.put("address", address);
                        contact.put("cl_code", cl_code);
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
                        SPsr.removeAllSP(ClientByBranchActivity.this);
                        finish();
                        Intent intent = new Intent(ClientByBranchActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

            try {   // Dismiss the progress dialog
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                /**
                 * Updating parsed JSON data into ListView
                 */

                branchadapter = new SimpleAdapter(
                        ClientByBranchActivity.this, branchlist,
                        R.layout.client_by_branch_list_item, new String[]{"phar_name", "address"}, new int[]{R.id.client_name, R.id.client_add});
                lv.setAdapter(null);

                lv.setAdapter(branchadapter);
                try {
                    TV_count.setText("عدد النتائج: " + String.valueOf(lv.getAdapter().getCount()));
                } catch (NullPointerException e) {
                    TV_count.setText("");
                }
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(),
                        "خطأ",
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
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
        super.onBackPressed();
    }
}
