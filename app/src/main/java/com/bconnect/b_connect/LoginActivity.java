package com.bconnect.b_connect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bconnect.b_connect.controllers.AppKeys;
import com.bconnect.b_connect.views.ChooseApplicationActivity;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class LoginActivity extends AppCompatActivity {

    private String TAG = LoginActivity.class.getSimpleName();
    EditText Login_username, Login_password;
    Button Login_button;
    private ProgressDialog pDialog;
    private ProgressDialog pDialogIP;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int time = 0;
    int is_test;
    int snakINT = 0;
    ImageView login_img;
    SharedPreferences dataSaver;
    SharedPreferencesSR SPsr = new SharedPreferencesSR();
    int is_server_ok;
    private String IPFromDomain = "";
    private String IPMasterLocal = "";
    private String IPMasterLive = "";
    private String IPMasterWork = "";
    String urlS = "";
    private WebView mWebview;
    ArrayList<String> list = new ArrayList<>();
    Snackbar snackbar;
    boolean isNetworkConnectedBL;
    int is_start_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().hide();
        stopService(new Intent(LoginActivity.this, NotiService.class));
        findById();
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        control();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        LinearLayout contentPain = findViewById(R.id.contentpain);
        contentPain.setAlpha(0);
        contentPain.animate().alpha(1.0f).setDuration(3000).start();
        isNetworkConnectedBL = true;
        is_start_login = 0;

        if (isNetworkConnected()) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isNetworkConnectedBL = true;
                }
            }, 500);

        } else if (!isNetworkConnected()) {

            isNetworkConnectedBL = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("لا يوجد خدمة انترنت , راجع خدمة الإنترنت ثم اضغط إعادة التحقق")
                    .setCancelable(true)
                    .setPositiveButton("إعادة التحقق", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void findById() {

        pref = getApplicationContext().getSharedPreferences("MySr", MODE_PRIVATE);
        editor = pref.edit();

        try {
            IPMasterWork = SPsr.getSP(getApplicationContext(), "IP_iswork");
            Log.e(TAG, "IP_iswork: " + SPsr.getSP(getApplicationContext(), "IP_iswork"));

        } catch (Exception ex) {
            SPsr.removeAllSP(LoginActivity.this);
            finish();
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        Login_username = findViewById(R.id.username);
        Login_password = findViewById(R.id.password);
        //  login_img = findViewById(R.id.login_img);
        Login_button = findViewById(R.id.login);
        //mWebview = findViewById(R.id.Logain_webView);
        pDialogIP = new ProgressDialog(LoginActivity.this);
        pDialogIP.setMessage("جاري تحضير التطبيق , إنتظر قليلا...");
        pDialogIP.setCancelable(false);
        is_server_ok = 3;
    }

    private void control() {

        Log.e("SR", "getSP.is_test :" + String.valueOf(is_test));

        Intent intent = getIntent();
        try {
            if (intent.getExtras().getString("snack").toString().length() > 0) {
                snakINT = Integer.parseInt(intent.getExtras().getString("snack").toString());
            }

        } catch (Exception e) {

            snakINT = 0;
        }

        if (snakINT == 1) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "تم تسجيل الخروج", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "عفوا, يرجا اعادة تسحيل الدخول", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        Login_password.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    if (Login_password.getText().length() >= 1) {
                        new LoginActivity.sendLoginData().execute();
                        return true;

                    }
                }

                return false;
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void StartGetIP() {
        IPMasterLocal = "";
        IPMasterLive = "";
        if (is_test == 1) {

            IPMasterLocal = "172.16.0.2";
            list.clear();
            list.add(IPMasterLocal);

        } else if (is_test == 0) {

            IPMasterLocal = "172.16.0.2";
            list.clear();
            list.add(IPMasterLocal);

        }
    }

    public void login(View view) {
        if (isNetworkConnectedBL == true) {
            new LoginActivity.sendLoginData().execute();
            closeKeyboard();
        } else {
            if (isNetworkConnected()) {

                is_start_login = 1;
                closeKeyboard();
                new LoginActivity.sendLoginData().execute();
                isNetworkConnectedBL = true;
            } else if (!isNetworkConnected()) {

                isNetworkConnectedBL = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("لا يوجد خدمة انترنت , راجع خدمة الإنترنت ثم اضغط إعادة التحقق")
                        .setCancelable(true)
                        .setPositiveButton("إعادة التحقق", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //  onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    class sendLoginData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("إنتظر قليلا...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {

                if (IPMasterWork.length() > 1) {

                    WebServiceCall cweb = new WebServiceCall(LoginActivity.this);
                    cweb.Login(Login_username.getText().toString(), Login_password.getText().toString());
                    cweb.getUserData(WebServiceResult.eCode.toString());

                } else {

                    WebServiceResult.ErrorID = 6;
                    finish();
                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            } catch (Exception ex) {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "تعذر الإتصال بالإنترنت, راجع مزود الإنترنت", Snackbar.LENGTH_LONG);
                snackbar.show();
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                if ((pDialog != null) && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                if (WebServiceResult.ErrorID == 6) {
                    is_start_login = 1;
                    StartGetIP();
                }
                if (WebServiceResult.ErrorID == 1) {

                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "خطأ: راجع اسم المستخدم او كلمة المرور", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                if (WebServiceResult.ErrorID == 0) {
                    SPsr.setSP(LoginActivity.this, "asm", String.valueOf(Login_username.getText().toString()));
                    SPsr.setSP(LoginActivity.this, "ser", String.valueOf(Login_password.getText().toString()));
                    SPsr.setSP(LoginActivity.this, "empsercode", String.valueOf(WebServiceUserData.code.toString()));
                    SPsr.setSP(LoginActivity.this, "empsername", String.valueOf(WebServiceUserData.name.toString()));
                    SPsr.setSP(LoginActivity.this, "is_test", String.valueOf(is_test));
                    Log.e("username", "username " + Login_username.getText().toString());
                    dataSaver.edit()
                            .putString(AppKeys.ASM, Login_username.getText().toString())
                            .apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("snack", "1");
                    startActivity(intent);
                }
            } catch (Exception ex) {

                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "تعذر ارسال البيانات, راجع مزود الإنترنت", Snackbar.LENGTH_LONG);
                snackbar.show();

                StartGetIP();

                ex.printStackTrace();
            }
        }
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(Login_username.getWindowToken(), 0);
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);
        Log.e("SR", "SRSRSR ok:");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if ((pDialogIP != null) && pDialogIP.isShowing()) {
            pDialogIP.dismiss();
        }
    }
}