package com.bconnect.b_connect.views;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bconnect.b_connect.R;
import com.bconnect.b_connect.controllers.AppKeys;
import com.bconnect.b_connect.controllers.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText username, password;
    Button login;
    SharedPreferences dataSaver;
    String emp_code;
    String is_login;
    String is_admin;
    String welcome_back, pass, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        is_login = dataSaver.getString(AppKeys.IS_LOGIN, "");
        is_admin = dataSaver.getString(AppKeys.IS_ADMIN, "");
        login = findViewById(R.id.login);
        login.setOnClickListener(this);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
            username.setText(name);
            password.setText(pass);
        } else {
            Log.e("TAG", "welcome");
        }

        Log.e("is_login", "is_login " + is_login);
        Log.e("is_admin", "is_admin " + is_login);

        if (is_login.equals("Y") && is_admin.equals("Y")) {
            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
            startActivity(intent);
            finish();

        } else if (is_login.equals("Y") && is_admin.equals("N")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        LinearLayout contentPain = findViewById(R.id.contentpain);
        contentPain.setAlpha(0);
        contentPain.animate().alpha(1.0f).setDuration(3000).start();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("50", "welcome back");
        outState.putString("user_name", username.getText().toString());
        outState.putString("password", password.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        welcome_back = savedInstanceState.getString("50");
        name = savedInstanceState.getString("user_name");
        pass = savedInstanceState.getString("password");
    }

    @Override
    public void onClick(View view) {
        if (view == login) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("جاري تسجيل الدخول ...");
            progressDialog.show();

            final String name = username.getText().toString();
            final String pass = password.getText().toString();

            if (name.equals("") || pass.equals("")) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "ادخل الاسم وكلمة السر",
                        Toast.LENGTH_SHORT).show();
            }

            Service.FetcherXml.getInstance().userLogin(name, pass).enqueue(new Callback<String>() {

                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (response.isSuccessful()) {

                        String res = response.body();
                        Log.e("isSuccessful", "isSuccessful");

                        try {
                            JSONObject places = new JSONObject(res);
                            JSONArray results = places.getJSONArray("LOGIN_DATA");
                            for (int i = 0; i < results.length(); i++) {

                                JSONObject temp = results.getJSONObject(i);
                                emp_code = temp.getString("emp_code");
                                is_login = temp.getString("login");
                                is_admin = temp.getString("admin");
                                Log.e("is_login", "is_login1 " + temp.getString("login"));
                                Log.e("is_admin", "is_admin1 " + temp.getString("admin"));
                                Log.e("emp_code", "emp_code " + temp.getString("emp_code"));
                                dataSaver.edit()
                                        .putString(AppKeys.IS_LOGIN, is_login)
                                        .apply();
                                dataSaver.edit()
                                        .putString(AppKeys.IS_ADMIN, is_admin)
                                        .apply();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                        if (is_login.equals("Y") && is_admin.equals("Y")) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "تم تسجيل الدخول",
                                    Toast.LENGTH_SHORT).show();
                            dataSaver.edit()
                                    .putString(AppKeys.EMP_CODE, emp_code)
                                    .apply();

                            dataSaver.edit()
                                    .putString(AppKeys.ADMIN_NAME, name)
                                    .apply();

                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(intent);
                            CustomIntent.customType(LoginActivity.this, "left-to-right");
                            finish();
                        } else if (is_login.equals("Y") && is_admin.equals("N")) {
                            progressDialog.dismiss();
                            Log.e("is-login", "is-login " + is_login);
                            Toast.makeText(LoginActivity.this, "تم تسجيل الدخول",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            CustomIntent.customType(LoginActivity.this, "left-to-right");
                            finish();
                            dataSaver.edit()
                                    .putString(AppKeys.EMP_CODE, emp_code)
                                    .apply();

                            dataSaver.edit()
                                    .putString(AppKeys.EMP_NAME, name)
                                    .apply();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "مستخدم غير موجود",
                                    Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "خطأ ",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("TAG", "onFailure " + t.getMessage());
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}