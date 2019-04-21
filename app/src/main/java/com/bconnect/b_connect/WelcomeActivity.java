package com.bconnect.b_connect;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.bconnect.b_connect.controllers.AppKeys;
import com.bconnect.b_connect.views.ChooseApplicationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import maes.tech.intentanim.CustomIntent;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class WelcomeActivity extends AppCompatActivity {

    private String TAG = WelcomeActivity.class.getSimpleName();
    static int SPLACH_TIME_OUT;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String asm, ser, name;
    SharedPreferencesSR SPsr = new SharedPreferencesSR();
    SharedPreferencesSR sharedPreferencesAdapter = new SharedPreferencesSR();
    int is_server_ok;
    Boolean is_IP_OK = false;
    private WebView mWebview;
    ArrayList<String> list = new ArrayList<>();
    boolean isNetworkConnectedBL;
    int is_start_login;
    ProgressDialog pDialogIP;
    ProgressBar welcome_progress;
    SharedPreferences dataSaver;
    String URL_IP = "";
    String json_url = "";
    Integer versionINT = 0;
    int Time = 0;
    int TimeOUT = 20;
    String IP_PORT = "8100";
    boolean timeout = true;
    String LocalIP = "41.187.17.242";
    String LocalIP2 = "172.16.0.2";
    String Domain = "http://bconnect-egypt.info/";
    String currentVersion, latestVersion;
    Dialog dialog;

    private Get_ConnectionData_JSON_Data get_connectionData_json_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        getSupportActionBar().hide();
        getCurrentVersion();
        findbyid();

    }

    private void getCurrentVersion() {
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;
        Log.e("currentVersion ", "currentVersion " + currentVersion);
        new GetLatestVersion().execute();
    }

    private class GetLatestVersion extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=com.bconnect.b_connect&hl=en_US").get();
                latestVersion = doc.getElementsByClass("htlgb").get(6).text();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Log.e("latestVersion", "latestVersion " + latestVersion);
            if (latestVersion != null) {
                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    if (!isFinishing()) {
                        showUpdateDialog();
                    }
                }
            } else
                super.onPostExecute(jsonObject);
        }
    }

    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("A New Update is Available");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=com.bconnect.b_connect")));
                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setCancelable(false);
        dialog = builder.show();
    }

    private void findbyid() {

        welcome_progress = findViewById(R.id.welcome_progress);
        mWebview = findViewById(R.id.mWebView);

        SPLACH_TIME_OUT = 1200;

        asm = SPsr.getSP(WelcomeActivity.this, "asm");
        ser = SPsr.getSP(WelcomeActivity.this, "ser");
        name = SPsr.getSP(WelcomeActivity.this, "name");
        Log.e(TAG, "asm: " + asm);

        try {
            Log.e(TAG, "WelcomeActivity CLC: " + sharedPreferencesAdapter.getSP(getApplicationContext(), "CLC"));
            Log.e(TAG, "WelcomeActivity IP: " + sharedPreferencesAdapter.getSP(getApplicationContext(), "IP_iswork"));

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Map<String, ?> allEntries = sharedPreferences.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                Log.d("SR values", entry.getKey() + ": " + entry.getValue().toString());
            }

            asm = sharedPreferencesAdapter.getSP(WelcomeActivity.this, "asm");
            ser = sharedPreferencesAdapter.getSP(getApplicationContext(), "ser");
            name = sharedPreferencesAdapter.getSP(getApplicationContext(), "name");

        } catch (Exception ex) {

        }

        isNetworkConnectedBL = false;
        is_start_login = 0;
        is_server_ok = 3;


        if (isNetworkConnected()) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        loadArray(getApplicationContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isNetworkConnectedBL = true;
                }
            }, 500);

        } else if (!isNetworkConnected()) {

            isNetworkConnectedBL = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.con_message_body))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.con_message_botton_check), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            Intent intent = new Intent(WelcomeActivity.this, WelcomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }

    }

    public static String executeCmd(String cmd, boolean sudo) {
        try {
            Process p;
            String lost = "";
            if (!sudo)
                p = Runtime.getRuntime().exec(cmd);
            else {
                p = Runtime.getRuntime().exec(new String[]{"su", "-c", cmd});
            }
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String s;
            String res = "";
            while ((s = stdInput.readLine()) != null) {
                res += s;
            }
            p.destroy();
            Log.e("SR", "ping: " + res);
            if (res.contains("received")) {
                int i = res.indexOf("transmitted,");
                int j = res.indexOf("received,");

                lost = res.substring(i + 12, j);

            }
            return lost.replace(" ", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }


    public void load_IP_Domain(String LocalDomain) throws IOException {


        mWebview = findViewById(R.id.mWebView);


        Log.e(TAG, "load_IP_Domain: START");

        mWebview.setInitialScale(1);

        mWebview.getSettings().setCacheMode(mWebview.getSettings().LOAD_NO_CACHE);
        mWebview.getSettings().setAppCacheEnabled(false);
        mWebview.clearHistory();
        mWebview.clearCache(true);

        mWebview.getSettings().setLoadWithOverviewMode(false);
        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.getSettings().setJavaScriptEnabled(false);
        mWebview.getSettings().setAllowFileAccess(false);
        mWebview.getSettings().setAllowContentAccess(false);
        mWebview.getSettings().setLoadsImagesAutomatically(false);
        mWebview.setScrollbarFadingEnabled(false);
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        mWebview.loadUrl(LocalDomain);

        mWebview.setWebViewClient(new WebViewClient() {
            private int running = 0; // Could be public if you want a timer to check.

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String urlNewString) {

                running++;
                webView.loadUrl(urlNewString);
                return true;
            }
//E/WelcomeActivity: received: 0 ,Connection is bad
//E/WelcomeActivity: load_IP_Domain: START
//Guide.is_IP_OK: false
//E/latestVersion: latestVersion 1.5
//E/WelcomeActivity: load_IP_Domain: RUN
//E/WelcomeActivity: load_IP_Domain: RUN
//E/WelcomeActivity: load_IP_Domain: RUN
//E/WelcomeActivity: URL_IP: 41.187.17.242
//E/WebServiceListHttpHolder: IOException:
//http://41.187.17.242:8090/bccws/bcc-ws.asmx/Get_ConnectionData_JSON_Data
//E/WelcomeActivity: Response from url: null
//E/WelcomeActivity: Couldn't get json from server.
//E/WelcomeActivity: get_connectionData_json_data: onCancelled

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                running = Math.max(running, 1);
                timeout = true;
                Log.e(TAG, "load_IP_Domain: RUN");

            }

            @Override
            public void onPageFinished(WebView view, String url) {

                if (--running == 0) {

                    timeout = false;
                    URL urlfinish = null;

                    if (!timeout) {

                        try {
                            urlfinish = new URL(url);
                            URL_IP = urlfinish.getHost();
                            Log.e(TAG, "URL_IP: " + URL_IP);

                            json_url = "http://" + URL_IP + ":" + "8090" + "/bccws/bcc-ws.asmx/Get_ConnectionData_JSON_Data";

                            get_connectionData_json_data = new Get_ConnectionData_JSON_Data();
                            get_connectionData_json_data.execute();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (get_connectionData_json_data != null) {
                                        get_connectionData_json_data.cancel(true);
                                    }

                                }
                            }, 10000);
                            //     http://41.187.17.242:8090/bccws/bcc-ws.asmx/Get_ConnectionData_JSON_Data
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private class Get_ConnectionData_JSON_Data extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            WebServiceListHttpHolder sh = new WebServiceListHttpHolder();
            String jsonStr = sh.makeServiceCall(json_url);

            Log.e(TAG, "Response from url: " + jsonStr);


            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray contacts = jsonObj.getJSONArray("Table");

                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String CONID = c.getString("CONID");
                        String CONip_local = c.getString("CONip_local");
                        String CONip1 = c.getString("CONip1");
                        String CONip2 = c.getString("CONip2");
                        String CONip3 = c.getString("CONip3");
                        String CONip4 = c.getString("CONip4");
                        String CONversion = c.getString("CONversion");

                        Integer CONversionINT = 0;
                        if (CONversion.length() > 0) {
                            CONversionINT = Integer.parseInt(CONversion);
                        }
                        Log.e(TAG, String.valueOf(versionINT) + " <-CONversion-> " + String.valueOf(CONversionINT));

                        if (versionINT != CONversionINT) {

                            sharedPreferencesAdapter.setSP(getApplicationContext(), "IP_Array_ver", CONversion);

                            list.clear();

                            if (CONip_local.length() > 0) {
                                list.add(CONip_local);
                            }
                            if (CONip1.length() > 0) {
                                list.add(CONip1);
                            }
                            if (CONip2.length() > 0) {
                                list.add(CONip2);
                            }
                            if (CONip3.length() > 0) {
                                list.add(CONip3);
                            }
                            if (CONip4.length() > 0) {
                                list.add(CONip4);
                            }
                            for (int ii = 0; ii < list.size(); ii++) {
                                Log.e(TAG, "list: " + list.get(ii));
                            }
                        }
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());

                    showsnak();
                    get_connectionData_json_data.cancel(true);
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                get_connectionData_json_data.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                ChackIPLIST(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled(Void result) {

            if (get_connectionData_json_data != null) {
                get_connectionData_json_data = null;
            }
            Log.e(TAG, "get_connectionData_json_data: onCancelled");
            showProgress(false);
        }

    }

    public void loadArray(Context mContext) throws IOException {

        int size = 0;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("SP values", entry.getKey() + ": " + entry.getValue().toString());
        }


        size = sharedPreferencesAdapter.getSPINT(mContext, "IP_Array_size");

        if (size > 0) {
            list.clear();
            try {
                versionINT = Integer.parseInt(sharedPreferencesAdapter.getSP(mContext, "IP_Array_ver"));

            } catch (Exception e) {
                Log.e(TAG, "loadArray ERROR: " + e.getClass().getSimpleName());
                showsnak();
            }
            for (int i = 0; i < size; i++) {

                list.add(sharedPreferencesAdapter.getSP(mContext, "IP_Array" + i));

                if ((i + 1) == size) {

                    ChackIPLIST(false);
                }
            }
        } else {

            try {
                list.add(LocalIP2);
                list.add(LocalIP);
                ChackIPLIST(false);
            } catch (Exception e1) {
                e1.printStackTrace();
                Log.e(TAG, "Error in: " + e1.getClass().getSimpleName().toString());
                showsnak();
            }
        }
    }

    public void ChackIPLIST(Boolean NEW) throws IOException {

        if (NEW) {
            try {
                for (int i = 0; i < list.size(); i++) {

                    if (i == TimeOUT) {
                        Log.e(TAG, "TIME IS OUT");
                        break;

                    }

                    Log.e(TAG, list.get(i));

                    String url = list.get(i);
                    String cmd = executeCmd("ping -c 1 -w 1 " + url, false);

                    Log.e(TAG, "PING OF:" + url + "-" + cmd);
                    Integer lostINT = Integer.parseInt(cmd.replace(" ", ""));
                    if (lostINT > 0) {
                        Log.e(TAG, "received: " + lostINT + " ,Connection is GOOD");
                        Log.e(TAG, "IP: " + url);

                        is_IP_OK = true;

                        Log.e(TAG, "ChackIPLIST.is_IP_OK: " + String.valueOf(is_IP_OK));

                        if (url.startsWith(LocalIP2) || url.startsWith(LocalIP)) {
                            sharedPreferencesAdapter.setSP(getApplicationContext(), "IP_iswork", url);

                        } else {
                            sharedPreferencesAdapter.setSP(getApplicationContext(), "IP_iswork", LocalIP);

                        }

                        Log.e(TAG, "sharedPreferencesAdapter.IP_iswork: " + String.valueOf(url));
                        showProgress(false);
                        break;

                    } else {
                        Log.e(TAG, "received: " + lostINT + ",Connection is bad");

                        if ((i + 1) == list.size()) {

                            load_IP_Domain(Domain);
                        }
                    }
                }

                sharedPreferencesAdapter.setArraySP(getApplicationContext(), "IP_Array", list);
                Guide();

            } catch (Exception e) {
                Log.e(TAG, "GET IP ERROR: " + e.getClass().getSimpleName());

                showsnak();
            }
        } else {

            try {
                for (int i = 0; i < list.size(); i++) {

                    Time += 5;

                    if (Time == TimeOUT) {
                        Log.e(TAG, "TIME IS OUT");
                        break;

                    }
                    Log.e(TAG, list.get(i));
                    String url = list.get(i).toString();
                    String cmd = executeCmd("ping -c 1 -w 1 " + url, false).toString();
                    Log.e(TAG, "PING OF:" + url + "-" + cmd);
                    Integer lostINT = Integer.parseInt(cmd.replace(" ", ""));
                    if (lostINT > 0) {
                        Log.e(TAG, "received: " + lostINT + " ,Connection is GOOD");
                        Log.e(TAG, "IP: " + url);

                        is_IP_OK = true;
                        Log.e(TAG, "ChackIPLIST.is_IP_OK: " + String.valueOf(is_IP_OK));

                        if (url.startsWith(LocalIP2) || url.startsWith(LocalIP)) {
                            sharedPreferencesAdapter.setSP(getApplicationContext(), "IP_iswork", url);

                            dataSaver.edit()
                                    .putString(AppKeys.IP_IS_WORK, url)
                                    .apply();
                        } else {
                            sharedPreferencesAdapter.setSP(getApplicationContext(), "IP_iswork", url + ":" + LocalIP);
                            dataSaver.edit()
                                    .putString(AppKeys.IP_IS_WORK, url)
                                    .apply();
                        }
                        showProgress(false);
                        break;
                    } else {
                        Log.e(TAG, "received: " + lostINT + " ,Connection is bad");

                        if ((i + 1) == list.size()) {
                            load_IP_Domain(Domain);
                        }

                    }
                }

                Guide();

            } catch (Exception e) {
                Log.e(TAG, "GET IP ERROR: " + e.getClass().getSimpleName());
                showsnak();
                load_IP_Domain(Domain);
            }
        }
    }

    public void showsnak() {
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.data_lost_message), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("DISMISS", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (snackbar != null)
                    snackbar.dismiss();
            }
        });
        View snackbarView = snackbar.getView();
        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = snackbarView.findViewById(snackbarTextId);
        textView.setTextColor(Color.WHITE);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (snackbar != null)
                    snackbar.dismiss();
                if (get_connectionData_json_data != null) {
                    get_connectionData_json_data.cancel(true);
                }
            }
        });
        if (!snackbar.isShown()) {
            snackbar.show();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    void showProgress(boolean show) {
        if (show) {
            if (!welcome_progress.isShown()) {
                welcome_progress.setVisibility(View.VISIBLE);
            }
        } else {
            if (welcome_progress.isShown()) {
                welcome_progress.setVisibility(View.INVISIBLE);
            }
        }
    }

    void Guide() {
        if (currentVersion.equals(latestVersion) || latestVersion == null) {
            Log.e(TAG, "Guide.is_IP_OK: " + String.valueOf(is_IP_OK));
            if (is_IP_OK) {
                try {
                    Log.e(TAG, "WelcomeActivity asm: " + sharedPreferencesAdapter.getSP(WelcomeActivity.this, "asm"));
                    asm = sharedPreferencesAdapter.getSP(WelcomeActivity.this, "asm");

                } catch (Exception ex) {
                }
                if (asm.length() > 0) {
                    finish();
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    intent.putExtra("snack", "1");
                    startActivity(intent);
                    CustomIntent.customType(WelcomeActivity.this, "left-to-right");
                } else {
                    finish();
                    Intent intent = new Intent(WelcomeActivity.this, ChooseApplicationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if ((pDialogIP != null) && pDialogIP.isShowing()) {
            pDialogIP.dismiss();
        }
        if (get_connectionData_json_data != null) {
            get_connectionData_json_data.cancel(true);

        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(WelcomeActivity.this)
                .setIcon(R.drawable.ic_warning_24dp)
                .setTitle(getString(R.string.exit_message_header))
                .setMessage(getString(R.string.exit_message_body))
                .setPositiveButton(getString(R.string.exit_message_botton_true), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        if (get_connectionData_json_data != null) {
                            get_connectionData_json_data.cancel(true);
                        }
                    }

                })
                .setNegativeButton(getString(R.string.exit_message_botton_false), null)
                .show();
    }
}
