package com.bconnect.b_connect.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;


import com.bconnect.b_connect.R;
import com.bconnect.b_connect.controllers.AppKeys;

import maes.tech.intentanim.CustomIntent;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class VisitByDateActivity extends AppCompatActivity implements View.OnClickListener {
    CalendarView calendarView;
    Button allVisits, visitByBranch;
    SharedPreferences dataSaver;
    TextView header;
    String emp_name, visit_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_by_date);
        calendarView = findViewById(R.id.calender);
        header = findViewById(R.id.header);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        emp_name = dataSaver.getString(AppKeys.EMP_NAME, "");
        header.setText(" Visits " + emp_name);
        allVisits = findViewById(R.id.all_visits);
        visitByBranch = findViewById(R.id.visit_by_branch);
        visitByBranch.setOnClickListener(this);
        allVisits.setOnClickListener(this);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                month = month + 1;
                visit_date = (dayOfMonth + " / " + month + " / " + year);
                Log.e("visit_date", "visit_date " + visit_date);

                dataSaver.edit()
                        .putString(AppKeys.VISIT_DATE, visit_date)
                        .apply();
                dataSaver.edit()
                        .putString(AppKeys.VISIT_TYPE, "visitByDate")
                        .apply();

                Intent intent = new Intent(VisitByDateActivity.this, HistoryActivity.class);
                startActivity(intent);
                CustomIntent.customType(VisitByDateActivity.this, "left-to-right");

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == allVisits) {
            dataSaver.edit()
                    .putString(AppKeys.VISIT_TYPE, "allVisits")
                    .apply();
            Intent intent = new Intent(VisitByDateActivity.this, HistoryActivity.class);
            startActivity(intent);
            CustomIntent.customType(this, "left-to-right");

        }
        if (view == visitByBranch) {
            dataSaver.edit()
                    .putString(AppKeys.CHOOSE_REPORT, "VISITS")
                    .apply();
            Intent intent = new Intent(VisitByDateActivity.this, BranchActivity.class);
            startActivity(intent);
            CustomIntent.customType(this, "left-to-right");

        }
    }
}
