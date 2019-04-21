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

public class BranchsActivity extends AppCompatActivity {


    private String TAG = BranchsActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    Button bt_re;
    private ListAdapter branchadapter;
    private static String url;
    ArrayList<HashMap<String, String>> branchlist;

    String BranchCode, BranchCodeAF, serurl, serurlAF, BranchCodeToActivity, ToActivity, activation_flag, phar_nameToActivity;
    int ToActivityINT;
    private EditText editText;


    int time = 0;

    public AsyncTask<Void, Void, Void> myTask;
    SwipeRefreshLayout pullToRefresh;

    SharedPreferencesSR SPsr = new SharedPreferencesSR();

    TextView TV_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branchs);

        basic();
        control();

    }

    public void basic() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        pDialog = new ProgressDialog(BranchsActivity.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setTitle("إنتظر قليلا");
        pDialog.setMessage("جري جلب البيانات...");
        pDialog.setCancelable(true);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        url = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/android.asmx/getBranch?search=";

        TV_count = (TextView) findViewById(R.id.TV_count);

        editText = (EditText) findViewById(R.id.etSearch);
        editText.clearFocus();


        lv = (ListView) findViewById(R.id.branch_list);

        branchlist = new ArrayList<>();


        bt_re = findViewById(R.id.bt_re);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        ToActivity = extras.getString("ToActivity");
        activation_flag = extras.getString("activation_flag");
        ToActivityINT = Integer.parseInt(ToActivity.toString());
        pullToRefresh = findViewById(R.id.pullToRefresh);
        myTask = new BranchsActivity.GetBranch().execute();

    }

    public void control() {


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                url = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/android.asmx/getBranch?search=";
                myTask = new BranchsActivity.GetBranch().execute();
                pullToRefresh.setRefreshing(false);
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                HashMap<String, Object> obj = (HashMap<String, Object>) arg0.getAdapter().getItem(position);
                BranchCode = (String) obj.get("code");
                BranchCodeAF = BranchCode.replace(".0", "");

                BranchCodeToActivity = BranchCodeAF;
                if (ToActivityINT == 1) {

                    Intent i = new Intent(BranchsActivity.this, ClientByBranchActivity.class);
                    i.putExtra("BranchCodeToActivity", BranchCodeToActivity);
                    i.putExtra("ToActivity", ToActivity);
                    i.putExtra("activation_flag", activation_flag);
                    startActivity(i);
                }
                if (ToActivityINT == 2) {
                    //  finish();

                    Intent i = new Intent(BranchsActivity.this, NewConActivity.class);
                    i.putExtra("BranchCodeToActivity", BranchCodeToActivity);
                    i.putExtra("ToActivity", ToActivity);
                    i.putExtra("activation_flag", activation_flag);
                    startActivity(i);
                }
                if (ToActivityINT == 22) {

                    Intent i = new Intent(BranchsActivity.this, ClientByBranchActivity.class);
                    i.putExtra("BranchCodeToActivity", BranchCodeToActivity);
                    i.putExtra("ToActivity", ToActivity);
                    i.putExtra("activation_flag", activation_flag);
                    startActivityForResult(i, 22);
                }

                if (ToActivityINT == 3) {

                    finish();

                    Intent i = new Intent(BranchsActivity.this, ClientByBranchActivity.class);
                    i.putExtra("ToActivity", ToActivity);
                    i.putExtra("BranchCodeToActivity", BranchCodeToActivity);
                    i.putExtra("activation_flag", activation_flag);
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
                if (editText.getText() != null) {
                    serurl = editText.getText().toString();
                    serurlAF = serurl.replace(" ", "+");
                    url = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/android.asmx/getBranch?search=" + serurlAF;
                    myTask = new GetBranch().execute();
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
                    if (editText.getText().length() >= 1) {
                        serurl = editText.getText().toString();
                        serurlAF = serurl.replace(" ", "+");
                        url = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/android.asmx/getBranch?search=" + serurlAF;
                        myTask = new BranchsActivity.GetBranch().execute();
                        return true;

                    }
                    if (editText.getText().length() < 1) {
                        url = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/android.asmx/getBranch?search=";
                        myTask = new BranchsActivity.GetBranch().execute();
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
                        if (editText.getText().length() >= 1) {
                            serurl = editText.getText().toString();
                            serurlAF = serurl.replace(" ", "+");
                            url = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/android.asmx/getBranch?search=" + serurlAF;
                            myTask = new BranchsActivity.GetBranch().execute();

                        }
                        if (editText.getText().length() < 1) {
                            url = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/android.asmx/getBranch?search=";
                            myTask = new BranchsActivity.GetBranch().execute();

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


    private class GetBranch extends AsyncTask<Void, Void, Void> {

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

                        String code = c.getString("code");
                        String name = c.getString("name");


                        HashMap<String, String> contact = new HashMap<>();

                        contact.clear();
                        contact.put("code", code);
                        contact.put("name", name);

                        branchlist.add(contact);


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
                        Toast.makeText(getApplicationContext(),
                                "تعذر جلب البيانات, راجع مزود الإنترنت",
                                Toast.LENGTH_LONG)
                                .show();

                        SPsr.removeAllSP(BranchsActivity.this);
                        finish();
                        Intent intent = new Intent(BranchsActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                        BranchsActivity.this, branchlist,
                        R.layout.branch_list_item, new String[]{"name"}, new int[]{R.id.branch_name});
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22) {
            if (resultCode == RESULT_OK) {
                phar_nameToActivity = data.getStringExtra("phar_nameToActivity");
                Intent i = new Intent(BranchsActivity.this, NewConActivity.class);
                i.putExtra("phar_nameToActivity", phar_nameToActivity);
                setResult(RESULT_OK, i);
                finish();
            }
        }
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


        finish();

        super.onBackPressed();
    }


}
