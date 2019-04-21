package com.bconnect.b_connect;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class SupSearchActivity extends AppCompatActivity {

    Switch nc_sw_new_code;
    EditText sup_search_name,sup_search_code;
    TextInputLayout sup_search_nameT,sup_search_codeT;
    Button sup_search_bt,sup_search_bt2;

    String SearchToS;
    int SearchTo;
    int ISok;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_search);
        ISok=0;

        nc_sw_new_code=(Switch)findViewById(R.id.nc_sw_new_code);
        sup_search_name=(EditText)findViewById(R.id.sup_search_name);
        sup_search_code=(EditText)findViewById(R.id.sup_search_code);
        sup_search_nameT=(TextInputLayout)findViewById(R.id.sup_search_nameT);
        sup_search_codeT=(TextInputLayout)findViewById(R.id.sup_search_codeT);
        sup_search_bt=(Button)findViewById(R.id.sup_search_bt);
        sup_search_bt2=(Button)findViewById(R.id.sup_search_bt2);

        nc_sw_new_code.setChecked(false);


        if(nc_sw_new_code.isChecked()){
            SearchTo=1;
            SearchToS=sup_search_code.getText().toString();

            sup_search_nameT.setVisibility(View.GONE);
            sup_search_codeT.setVisibility(View.VISIBLE);
            sup_search_name.setVisibility(View.GONE);
            sup_search_name.setEnabled(false);
            sup_search_code.setVisibility(View.VISIBLE);
            sup_search_code.setEnabled(true);

        }
        if(!nc_sw_new_code.isChecked()){
            SearchTo=2;

            sup_search_nameT.setVisibility(View.VISIBLE);
            sup_search_codeT.setVisibility(View.GONE);
            sup_search_name.setVisibility(View.VISIBLE);
            sup_search_name.setEnabled(true);
            sup_search_code.setVisibility(View.GONE);
            sup_search_code.setEnabled(false);

        }


        nc_sw_new_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nc_sw_new_code.isChecked()){
                    SearchTo=1;
                    SearchToS=sup_search_code.getText().toString();

                    sup_search_nameT.setVisibility(View.GONE);
                    sup_search_codeT.setVisibility(View.VISIBLE);
                    sup_search_name.setVisibility(View.GONE);
                    sup_search_name.setEnabled(false);
                    sup_search_code.setVisibility(View.VISIBLE);
                    sup_search_code.setEnabled(true);

                }
                if(!nc_sw_new_code.isChecked()){
                    SearchTo=2;

                    sup_search_nameT.setVisibility(View.VISIBLE);
                    sup_search_codeT.setVisibility(View.GONE);
                    sup_search_name.setVisibility(View.VISIBLE);
                    sup_search_name.setEnabled(true);
                    sup_search_code.setVisibility(View.GONE);
                    sup_search_code.setEnabled(false);

                }


            }
        });

        sup_search_code.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if(SearchTo==1&&sup_search_code.getText().toString().length()>=3){

                        SearchToS=sup_search_code.getText().toString();


                        Intent i = new Intent(SupSearchActivity.this, SupListActivity.class);
                        i.putExtra("SearchTo", SearchTo);
                        i.putExtra("SearchToS", SearchToS);
                        startActivity(i);

                        return true;

                    }
                    if(SearchTo==1&&sup_search_code.getText().toString().length()<3){

                        Toast.makeText(SupSearchActivity.this, "الكود فارغ او اقل من المطلوب" , Toast.LENGTH_SHORT).show();
                        return true;

                    }
                    if(SearchTo==2&&sup_search_name.getText().toString().length()>1){
                        SearchToS=sup_search_name.getText().toString();

                        Intent i = new Intent(SupSearchActivity.this, SupListActivity.class);
                        i.putExtra("SearchTo", SearchTo);
                        i.putExtra("SearchToS", SearchToS);
                        startActivity(i);
                        return true;

                    }
                    if(SearchTo==2&&sup_search_name.getText().toString().length()<=1){
                        Toast.makeText(SupSearchActivity.this, "الاسم فارغ او اقل من المطلوب" , Toast.LENGTH_SHORT).show();
                        return true;

                    }
                   // Toast.makeText(SupSearchActivity.this, "الاسم فارغ او اقل من المطلوب" , Toast.LENGTH_SHORT).show();

                    return true;

                }



                return false;
            }
        });

        sup_search_name.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if(SearchTo==1&&sup_search_code.getText().toString().length()>=3){

                        SearchToS=sup_search_code.getText().toString();


                        Intent i = new Intent(SupSearchActivity.this, SupListActivity.class);
                        i.putExtra("SearchTo", SearchTo);
                        i.putExtra("SearchToS", SearchToS);
                        startActivity(i);

                        return true;

                    }
                    if(SearchTo==1&&sup_search_code.getText().toString().length()<3){

                        Toast.makeText(SupSearchActivity.this, "الكود فارغ او اقل من المطلوب" , Toast.LENGTH_SHORT).show();
                        return true;

                    }
                    if(SearchTo==2&&sup_search_name.getText().toString().length()>1){
                        SearchToS=sup_search_name.getText().toString();

                        Intent i = new Intent(SupSearchActivity.this, SupListActivity.class);
                        i.putExtra("SearchTo", SearchTo);
                        i.putExtra("SearchToS", SearchToS);
                        startActivity(i);
                        return true;

                    }
                    if(SearchTo==2&&sup_search_name.getText().toString().length()<=1){
                        Toast.makeText(SupSearchActivity.this, "الاسم فارغ او اقل من المطلوب" , Toast.LENGTH_SHORT).show();
                        return true;

                    }

                    return true;

                }



                return false;
            }
        });



        sup_search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SearchTo==1&&sup_search_code.getText().toString().length()>=3){

                    SearchToS=sup_search_code.getText().toString();


                    Intent i = new Intent(SupSearchActivity.this, SupListActivity.class);
                    i.putExtra("SearchTo", SearchTo);
                    i.putExtra("SearchToS", SearchToS);
                    startActivity(i);


                }
                if(SearchTo==1&&sup_search_code.getText().toString().length()<3){

                    Toast.makeText(SupSearchActivity.this, "الكود فارغ او اقل من المطلوب" , Toast.LENGTH_SHORT).show();

                }
                if(SearchTo==2&&sup_search_name.getText().toString().length()>1){
                    SearchToS=sup_search_name.getText().toString();

                    Intent i = new Intent(SupSearchActivity.this, SupListActivity.class);
                    i.putExtra("SearchTo", SearchTo);
                    i.putExtra("SearchToS", SearchToS);
                    startActivity(i);
                }
                if(SearchTo==2&&sup_search_name.getText().toString().length()<=1){
                    Toast.makeText(SupSearchActivity.this, "الاسم فارغ او اقل من المطلوب" , Toast.LENGTH_SHORT).show();

                }
            }
        });
        sup_search_bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SupSearchActivity.this, SupListActivity.class);
                i.putExtra("SearchTo", 0);
                i.putExtra("SearchToS", "");
                startActivity(i);

            }
        });





    }
}
