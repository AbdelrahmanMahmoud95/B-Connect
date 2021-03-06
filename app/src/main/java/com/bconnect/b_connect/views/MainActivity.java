package com.bconnect.b_connect.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bconnect.b_connect.R;
import com.bconnect.b_connect.controllers.AppKeys;

import maes.tech.intentanim.CustomIntent;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button notVisit, history, allPramacies;
    String emp_name;
    TextView userName;
    SharedPreferences dataSaver;
    ImageView logOut;
    Animation upToDown, downToUp, leftToRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        notVisit = findViewById(R.id.not_visit);
        history = findViewById(R.id.history);
        allPramacies = findViewById(R.id.all_pharmacies);
        logOut = findViewById(R.id.log_out);
        history.setOnClickListener(this);
        notVisit.setOnClickListener(this);
        allPramacies.setOnClickListener(this);
        logOut.setOnClickListener(this);
        userName = findViewById(R.id.username);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        emp_name = dataSaver.getString(AppKeys.EMP_NAME, "");
        userName.setText("Welcome: " + emp_name);

        upToDown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        downToUp = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        leftToRight = AnimationUtils.loadAnimation(this, R.anim.lefttoright);

        leftToRight.setDuration(1200);
        downToUp.setDuration(1200);
        upToDown.setDuration(1200);

        notVisit.setAnimation(downToUp);
        history.setAnimation(upToDown);
        allPramacies.setAnimation(leftToRight);
    }

    public void logOut() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dataSaver.edit()
                        .putString(AppKeys.EMP_CODE, "")
                        .apply();
                dataSaver.edit()
                        .putString(AppKeys.EMP_NAME, "")
                        .apply();
                dataSaver.edit()
                        .putString(AppKeys.IS_ADMIN, "")
                        .apply();
                dataSaver.edit()
                        .putString(AppKeys.IS_LOGIN, "")
                        .apply();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();

        dialog.setTitle("تسجيل الخروج");
        dialog.setMessage("هل انت متأكد انك تريد تسجيل الخروج؟");

        dialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view == notVisit) {
            Intent intent = new Intent(this, BranchActivity.class);
            dataSaver.edit()
                    .putString(AppKeys.CHOOSE_REPORT, "HOLDS")
                    .apply();
            startActivity(intent);
            CustomIntent.customType(this, "left-to-right");

        }
        if (view == history) {
            Intent intent = new Intent(this, ChooseVisitTypActivity.class);
            startActivity(intent);
            CustomIntent.customType(this, "left-to-right");

        }
        if (view == allPramacies) {
            Intent intent = new Intent(this, BranchActivity.class);
            dataSaver.edit()
                    .putString(AppKeys.CHOOSE_REPORT, "ALL_PH")
                    .apply();
            startActivity(intent);
            CustomIntent.customType(this, "left-to-right");

        }
        if (view == logOut) {
            logOut();
        }
    }
}