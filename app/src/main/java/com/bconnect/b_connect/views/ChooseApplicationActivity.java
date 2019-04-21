package com.bconnect.b_connect.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bconnect.b_connect.MainActivity;
import com.bconnect.b_connect.R;
import com.bconnect.b_connect.controllers.AppKeys;

import maes.tech.intentanim.CustomIntent;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class ChooseApplicationActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout quality, support, customer;
    String asm;
    SharedPreferences dataSaver;
    Animation downToUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_application);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        asm = dataSaver.getString(AppKeys.ASM, "");
        quality = findViewById(R.id.linear_quality);
        support = findViewById(R.id.linear_support);
        customer = findViewById(R.id.linear_customer);

        downToUp = AnimationUtils.loadAnimation(this, R.anim.downtoup);

        customer.setAnimation(downToUp);
        quality.setAnimation(downToUp);
        support.setAnimation(downToUp);

        quality.setOnClickListener(this);
        support.setOnClickListener(this);
        customer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == customer) {

            Toast.makeText(ChooseApplicationActivity.this, " Coming Soon.. ", Toast.LENGTH_SHORT).show();
        }
        if (view == quality) {

            Intent intent = new Intent(ChooseApplicationActivity.this, LoginActivity.class);
            startActivity(intent);
            CustomIntent.customType(this, "left-to-right");

        }
        if (view == support) {

            Intent intent = new Intent(ChooseApplicationActivity.this, com.bconnect.b_connect.LoginActivity.class);
            startActivity(intent);
            CustomIntent.customType(this, "left-to-right");

        }
    }
}
