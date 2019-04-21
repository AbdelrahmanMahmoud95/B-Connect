package com.bconnect.b_connect;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SupListActivity extends AppCompatActivity {
    SharedPreferencesSR SPsr = new SharedPreferencesSR();

    private String TAG = SupListActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private ListAdapter branchadapter;
    private static String url, url1, url2;
    ArrayList<HashMap<String, String>> branchlist;
    String phar_code, mot_code, month_target, month_done, empnameS, empcodeS, SearchToS;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Dialog dialog_sup;
    int SearchTo;

    Intent intent;

    Button butsupm, butsupo;

    public AsyncTask<Void, Void, Void> myTask;

    TextView TV_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_list);

        basic();
        control();

        myTask = new SupListActivity.Get_Emp_Goals().execute();

    }

    public void basic() {

        SearchTo = 0;
        SearchToS = "";
        pref = getApplicationContext().getSharedPreferences("MySr", MODE_PRIVATE);
        editor = pref.edit();

        empnameS = SPsr.getSP(getApplicationContext(), "empsername");
        empcodeS = SPsr.getSP(getApplicationContext(), "empsercode");

        intent = getIntent();
        SearchTo = intent.getExtras().getInt("SearchTo");
        SearchToS = intent.getExtras().getString("SearchToS");

        Log.e(TAG, "SearchToS: " + SearchToS);

        url1 = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/Android.asmx/Get_Emp_Goals_by_m_code?emp_code=" + empcodeS + "&motaheda_code=";
        url2 = "http://" + SPsr.getSP(getApplicationContext(), "IP_iswork") + "/webser/Android.asmx/Get_Emp_Goals_by_phr_name?emp_code=" + empcodeS + "&phar_name=";

        if (SearchTo == 0) {
            url = url2;
        }
        if (SearchTo == 1) {
            url = url1 + SearchToS;
            Log.e(TAG, "url1: " + url);

        }
        if (SearchTo == 2) {
            url = url2 + SearchToS.replace(" ", "+");
            Log.e(TAG, "url2: " + url);

        }

        lv = (ListView) findViewById(R.id.branch_list);

        branchlist = new ArrayList<>();

        TV_count = (TextView) findViewById(R.id.TV_count);

        dialog_sup = new Dialog(SupListActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        dialog_sup.setContentView(R.layout.dialog_sup);
        dialog_sup.setCancelable(true);


        butsupm = (Button) dialog_sup.findViewById(R.id.butsupm);
        butsupo = (Button) dialog_sup.findViewById(R.id.butsupo);

        pDialog = new ProgressDialog(SupListActivity.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setTitle("جري جلب البيانات...");
        pDialog.setMessage("إنتظر قليلا");
        pDialog.setCancelable(true);

    }

    public void control() {

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                HashMap<String, Object> obj = (HashMap<String, Object>) arg0.getAdapter().getItem(position);

                phar_code = (String) obj.get("code");
                mot_code = (String) obj.get("motcode");
                month_done = (String) obj.get("target_month1");
                month_target = (String) obj.get("target_month");


                Log.e(TAG, "phar_code :" + phar_code);
                Log.e(TAG, "mot_code :" + mot_code);
                Log.e(TAG, "month_done :" + month_done);
                Log.e(TAG, "month_target :" + month_target);

                dialog_sup.show();


            }
        });

        butsupm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(SupListActivity.this, SupMActivity.class);
                i.putExtra("phar_code", phar_code);
                i.putExtra("mot_code", mot_code);
                i.putExtra("month_done", month_done);
                i.putExtra("month_target", month_target);
                startActivity(i);

                dialog_sup.dismiss();

            }
        });
        butsupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(SupListActivity.this, SupOActivity.class);
                i.putExtra("phar_code", phar_code);
                i.putExtra("mot_code", mot_code);
                i.putExtra("month_done", month_done);
                i.putExtra("month_target", month_target);
                startActivity(i);
                dialog_sup.dismiss();


            }
        });

    }

    private class Get_Emp_Goals extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    myTask.cancel(true);
                }
            }, 120000);

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

                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String code = c.getString("code");
                        String bname = c.getString("bname");
                        String motcode = c.getString("motcode");
                        String pname = c.getString("pname");
                        String monthly_fee = c.getString("monthly_fee");
                        String target_month1 = c.getString("target_month1");
                        String target_month = c.getString("target_month");

                        HashMap<String, String> contact = new HashMap<>();

                        contact.clear();
                        contact.put("code", code);
                        contact.put("bname", bname);
                        contact.put("motcode", motcode);
                        contact.put("pname", pname);
                        contact.put("monthly_fee", monthly_fee);
                        contact.put("target_month1", target_month1);
                        contact.put("target_month", target_month);

                        branchlist.add(contact);

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e(TAG, "Json parsing error: " + e.getMessage());

                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SPsr.removeAllSP(SupListActivity.this);
                        finish();
                        Intent intent = new Intent(SupListActivity.this, LoginActivity.class);
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

            try {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }

                branchadapter = new SimpleAdapter(
                        SupListActivity.this, branchlist,
                        R.layout.sup_list_item, new String[]{"pname", "bname", "motcode", "monthly_fee", "target_month1", "target_month"}, new int[]{R.id.client_name, R.id.client_branch, R.id.client_motaheda_code, R.id.client_month_val, R.id.client_month_done, R.id.client_month_need});
                lv.setAdapter(null);
                lv.setAdapter(branchadapter);
                try {
                    TV_count.setText("عدد النتائج: " + String.valueOf(lv.getAdapter().getCount()));
                } catch (NullPointerException e) {
                    TV_count.setText("");
                }
            } catch (Exception ex) {

                Log.e(TAG, "LIST Error: " + ex.getMessage());

            }
        }

        @Override
        protected void onCancelled(Void result) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            this.cancel(true);
            SupListActivity.super.onBackPressed();
        }

    }
}
