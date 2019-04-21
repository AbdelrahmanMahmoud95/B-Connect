package com.bconnect.b_connect.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.bconnect.b_connect.R;
import com.bconnect.b_connect.controllers.AppKeys;

import maes.tech.intentanim.CustomIntent;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class ChooseVisitTypActivity extends AppCompatActivity implements View.OnClickListener {
    TextView header;
    Button allVisits, visitByDate, visitByBranch;
    SharedPreferences dataSaver;
    String emp_name;
    Animation upToDown, downToUp, leftToRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_visit_typ);
        header = findViewById(R.id.header);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        emp_name = dataSaver.getString(AppKeys.EMP_NAME, "");
        header.setText(emp_name + " Visits");
        allVisits = findViewById(R.id.all_visits);
        visitByDate = findViewById(R.id.visit_by_date);
        visitByBranch = findViewById(R.id.visit_by_branch);
        visitByBranch.setOnClickListener(this);
        visitByDate.setOnClickListener(this);
        allVisits.setOnClickListener(this);

        upToDown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        downToUp = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        leftToRight = AnimationUtils.loadAnimation(this, R.anim.lefttoright);

        leftToRight.setDuration(1000);
        downToUp.setDuration(1000);
        upToDown.setDuration(1000);

        visitByDate.setAnimation(upToDown);
        visitByBranch.setAnimation(leftToRight);
        allVisits.setAnimation(downToUp);
    }

    @Override
    public void onClick(View view) {
        if (view == allVisits) {

            dataSaver.edit()
                    .putString(AppKeys.VISIT_TYPE, "allVisits")
                    .apply();
            Intent intent = new Intent(ChooseVisitTypActivity.this, HistoryActivity.class);
            startActivity(intent);
            CustomIntent.customType(this, "left-to-right");

        }

        if (view == visitByBranch) {

            dataSaver.edit()
                    .putString(AppKeys.CHOOSE_REPORT, "VISITS")
                    .apply();
            Intent intent = new Intent(ChooseVisitTypActivity.this, BranchActivity.class);
            startActivity(intent);
            CustomIntent.customType(this, "left-to-right");

        }

        if (view == visitByDate) {
            dataSaver.edit()
                    .putString(AppKeys.VISIT_TYPE, "visitByDate")
                    .apply();
            Intent intent = new Intent(ChooseVisitTypActivity.this, VisitByDateActivity.class);
            startActivity(intent);
            CustomIntent.customType(this, "left-to-right");

        }
    }
}