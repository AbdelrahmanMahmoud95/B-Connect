package com.bconnect.b_connect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bconnect.b_connect.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OrderActivity extends AppCompatActivity {


    private String TAG = "SR: "+OrderActivity.class.getSimpleName()+": ";
    private int sentActive,is_new_mon_OK ;

    SharedPreferences pref ;
    SharedPreferences.Editor editor;
    String asm,ser,empsercode,supername;


    //Definitions
    private ProgressDialog pDialog;
    TextView pharm_name,pharm_code,pharm_add,pharm_phone_line,pharm_phone_mobile, pharm_doc_name;
    TextView con_month_val,con_val,con_remaning_val,con_down_val,con_type,banknameTit,cheDateTit;

    EditText uni_num,uni_val,uni_num_sall,uni_new_mon_val,uni_note;


    String  MotahedaCodeToActivity,nc_running_dateAF,rec_Date_dateAF,cheDate_dateAF,PharCode,real_Date,real_Date2,pharm_phone_lineS,pharm_phone_mobileS,PhoneS;
    String  contractTypeCode,phar_typeCode,banknameCode,paytypeCode,P_Code ,doc_nameS,nc_con_value_S,nc_join_value_S;
    String  emp_name,remaining,pharm_nameS,BranchCodeS,Total_valS,activate_pahr_codeS,uni_numS;


    int uni_num_sall_INT,uni_val_INT,Total_val,uni_num_INT,uni_NEW_mon_v_INT,uni_OLD_mon_v_INT;



    Button nc_sendbut,nc_rep_code_vil_but;



    Intent intent;

    Calendar  real_Date_myCalendar;

    NestedScrollView scrollView;

    SharedPreferencesSR SPsr=new SharedPreferencesSR();


    private static String ContractTypeUrl,PharTypeUrl,paytypeUrl,banknameUrl,emp_by_superUrl;



            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_order);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                basic();
                control();

                 new OrderActivity.GetClient_by_DCode().execute();




            }


    public  void basic(){

        scrollView = (NestedScrollView) findViewById(R.id.order_ns);


        pharm_name=(TextView)findViewById(R.id.pharm_name);
        pharm_code=(TextView)findViewById(R.id.pharm_code);
        pharm_add=(TextView)findViewById(R.id.pharm_add);
        pharm_phone_line=(TextView)findViewById(R.id.pharm_phone_line);
        pharm_phone_mobile=(TextView)findViewById(R.id.pharm_phone_mobile);
        pharm_doc_name=(TextView)findViewById(R.id.pharm_doc_name);
        con_month_val=(TextView)findViewById(R.id.con_month_val);
        con_val=(TextView)findViewById(R.id.con_val);
        //con_remaning_val=(TextView)findViewById(R.id.con_remaning_val);
        con_down_val=(TextView)findViewById(R.id.con_down_val);
        con_type=(TextView)findViewById(R.id.con_type);
        banknameTit=(TextView)findViewById(R.id.banknameTit);


        uni_num=(EditText)findViewById(R.id.uni_num);
        uni_val=(EditText)findViewById(R.id.uni_val);
        uni_num_sall=(EditText)findViewById(R.id.uni_num_sall);
        uni_new_mon_val=(EditText)findViewById(R.id.uni_new_mon_val);
        uni_note=(EditText)findViewById(R.id.uni_note);

        nc_sendbut=(Button) findViewById(R.id.nc_sendbut);
        nc_rep_code_vil_but=(Button) findViewById(R.id.nc_rep_code_vil_but);

        try{
            ContractTypeUrl = "http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/GetContractType";
            PharTypeUrl = "http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/GetPharType";
            emp_by_superUrl="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/Get_Emp_by_Super?emp_code=";
            intent = getIntent();
            MotahedaCodeToActivity="";
            MotahedaCodeToActivity = intent.getExtras().getString("MotahedaCodeToActivity");
            PharCode="";
            PharCode = intent.getExtras().getString("PharCode");

            empsercode="";
            empsercode=SPsr.getSP(getApplicationContext(),"empsercode");
            emp_name="";
            emp_name=SPsr.getSP(getApplicationContext(),"empsername");
        }catch (Exception ex){
            Log.e(TAG, "basic Error: " + ex.getMessage());

            SPsr.removeAllSP(OrderActivity.this);
            finish();
            Intent intent = new Intent(OrderActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }






        real_Date_myCalendar = Calendar.getInstance();

        SimpleDateFormat real_DateSDF = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat real_DateSDF2 = new SimpleDateFormat("yyyy/dd/MM");
        real_Date = real_DateSDF2.format( real_Date_myCalendar.getTime()).toString();
        real_Date2= real_DateSDF.format( real_Date_myCalendar.getTime()).toString();

        sentActive=0;



        P_Code="";
        doc_nameS="";

        is_new_mon_OK=0;
    }

    public  void control(){





                try{
                    if(empsercode!=null||empsercode!=""||empsercode.length()>0)
                    {
                        emp_by_superUrl="http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/Get_Emp_by_Super?emp_code="+empsercode.toString();

                    }


                }catch (Exception ex){
                    SPsr.removeAllSP(OrderActivity.this);
                    finish();
                    Intent intent = new Intent(OrderActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }



        uni_val.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(uni_val.getText().toString()=="0"||uni_val.getText().toString().length()<1){

                    Toast.makeText(OrderActivity.this, "قيمة الوحدة الإضافية فارغة او القيمه 0" , Toast.LENGTH_SHORT).show();
                    sentActive=0;
                }
                else
                {
                    sentActive=1;

                    if(s.toString().length()<1){
                        uni_val_INT=0;
                    }
                    else {
                        uni_val_INT=Integer.parseInt(s.toString());

                    }
                    if(uni_num_sall.getText().toString().length()<1){
                        uni_num_sall_INT=0;
                    }
                    else {
                        uni_num_sall_INT=Integer.parseInt(uni_num_sall.getText().toString());

                    }

                    Total_val=(uni_val_INT*uni_num_sall_INT);
                    Total_valS=String.valueOf(Total_val);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        uni_num_sall.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(uni_val.getText().toString()=="0"||uni_val.getText().toString().length()<1){

                    Toast.makeText(OrderActivity.this, "قيمة الوحدة الإضافية فارغة او القيمه 0" , Toast.LENGTH_SHORT).show();
                    sentActive=0;

                }
                else
                {
                    sentActive=1;

                    if(s.toString().length()<1){
                        uni_num_sall_INT=0;
                    }
                    else {
                        uni_num_sall_INT=Integer.parseInt(s.toString());

                    }
                    if(uni_val.getText().toString().length()<1){
                        uni_val_INT=0;
                    }
                    else {
                        uni_val_INT=Integer.parseInt(uni_val.getText().toString());

                    }

                    Total_val=(uni_val_INT*uni_num_sall_INT);
                    Total_valS=String.valueOf(Total_val);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        uni_val.setText("1000");
        uni_num_sall.setText("1");



        uni_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString()=="0"||s.toString().length()<1){

                    Toast.makeText(OrderActivity.this, "رقم الوحدة فارغ او القيمه 0" , Toast.LENGTH_SHORT).show();
                    uni_num_INT=0;
                    sentActive=0;

                }
                else
                {

                    if(s.toString().length()<1){
                        uni_num_INT=0;
                    }
                    else {
                        uni_num_INT=Integer.parseInt(s.toString());
                        if(uni_num_INT<1)
                        {
                            uni_num.setTextColor(Color.RED);
                            sentActive=0;

                        }
                        else {
                            uni_num.setTextColor(Color.BLACK);
                            sentActive=1;
                        }

                    }


                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        uni_new_mon_val.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                is_new_mon_OK=0;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString()=="0"||s.toString().length()<1){

                    Toast.makeText(OrderActivity.this, "الاشتراك الجديد فارغ او القيمه 0" , Toast.LENGTH_SHORT).show();
                    uni_NEW_mon_v_INT=0;
                    is_new_mon_OK=0;

                }
                else
                {

//                    if(s.toString().length()>0){
//                        uni_NEW_mon_v_INT=Integer.parseInt(s.toString());
//                        if(uni_NEW_mon_v_INT<=uni_OLD_mon_v_INT)
//                        {
//                            uni_new_mon_val.setTextColor(Color.RED);
//                            is_new_mon_OK=0;
//
//                        }
//                        else {
//                            uni_new_mon_val.setTextColor(Color.BLACK);
//                            is_new_mon_OK=1;
//                        }
//                    }

                    is_new_mon_OK=1;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {



                if(uni_num.getText().toString().length()<1||uni_val.getText().toString().length()<1
                        ||uni_new_mon_val.getText().toString().length()<1||uni_num_sall.getText().toString().length()<1){


                    sentActive=0;



                }
                else
                {

                    if (uni_num.getText().toString().length()>1) {
                        uni_num_INT=Integer.parseInt(uni_num.getText().toString());
                        if(uni_num_INT<1)
                        {
                            uni_num.setTextColor(Color.RED);
                            sentActive=0;

                        }
                        else {
                            uni_num.setTextColor(Color.BLACK);
                            sentActive=1;
                        }
                    }
                    if(uni_new_mon_val.getText().length()>0){
                        uni_NEW_mon_v_INT=Integer.parseInt(uni_new_mon_val.getText().toString());
//                        if(uni_NEW_mon_v_INT<=uni_OLD_mon_v_INT)
//                        {
//                            uni_new_mon_val.setTextColor(Color.RED);
//                            is_new_mon_OK=0;
//
//                        }
//                        else {
//                            uni_new_mon_val.setTextColor(Color.BLACK);
//                            is_new_mon_OK=1;
//                        }
                        is_new_mon_OK=1;
                    }





                }

                closeKeyboard();
                return false;

            }
        });





        //SR: Start send data methods



        nc_sendbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                StringBuilder t = new StringBuilder();
//                for(int l=(uni_num_INT); l<(uni_num_sall_INT+uni_num_INT); l++){
//                     t.append(l);
//                    uni_note.setText(t);
//                }


                if(sentActive==0) {
                    Toast.makeText(getApplicationContext(),
                            "قم بالتحقق من البيانات",
                            Toast.LENGTH_LONG)
                            .show();

                    uni_num.requestFocus();
                    scrollView.scrollTo(0, scrollView.getBottom()/2);

                }
                if(is_new_mon_OK==0) {
                    Toast.makeText(getApplicationContext(),
                            "يجب ان تعدل الاشتراك الجديد",
                            Toast.LENGTH_LONG)
                            .show();

                    uni_new_mon_val.requestFocus();
                    scrollView.scrollTo(0, scrollView.getBottom()/2);

                }


                if(sentActive==1&&is_new_mon_OK==1) {




                    AlertDialog.Builder builder1 = new AlertDialog.Builder(OrderActivity.this);
                    builder1.setMessage("اجمالي عدد الوحدات المباعة= "+uni_num_sall.getText().toString()+"\n اجمالي قيمة الوحدات: "+Total_valS+"\n\nتأكيد ارسال الطلب؟");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "تأكيد",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    new OrderActivity.save_phar_uni().execute();



                                }
                            });

                    builder1.setNegativeButton(
                            "لا",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }


            }
        });



    }





    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(uni_new_mon_val.getWindowToken(), 0);
    }





    private class GetClient_by_DCode extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(OrderActivity.this);
            pDialog.setMessage("إنتظر قليلا...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {
                WebServiceClientData.code="";
                WebServiceClientDataMore.cont_kind="";
                WebServiceCall webc =new WebServiceCall(OrderActivity.this);
                webc.GetClientData(MotahedaCodeToActivity);
                webc.GetClientDataMore(WebServiceClientData.code.toString());
                webc.GetContractTypeDataByCode(WebServiceClientDataMore.cont_kind.toString());


            }
            catch(Exception ex)
            {
                Toast.makeText(getApplicationContext(),
                        "تعذر الإتصال بالإنترنت, راجع مزود الإنترنت",
                        Toast.LENGTH_LONG)
                        .show();
                Log.e(TAG, "getAllClient Error: " + ex.getMessage());


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            try{

                // Dismiss the progress dialog
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                BranchCodeS="";
                BranchCodeS=WebServiceClientData.motaheda_branch.toString();
                P_Code="";
                P_Code=WebServiceClientData.code.toString();
                Log.e(TAG, "P_Code P_Code P_Code: " + P_Code.toString());

                doc_nameS=WebServiceClientData.manager_name.toString();
                pharm_nameS="";
                pharm_nameS=WebServiceClientData.phar_name.toString();
                pharm_name.setText("اسم الصيدلية:"+" "+WebServiceClientData.phar_name.toString());
                pharm_code.setText("كود الصيدلية:"+" "+WebServiceClientData.motaheda_code.toString());
                pharm_add.setText("عنوان الصيدلية:"+" "+WebServiceClientData.address.toString());
                pharm_phone_lineS="";
                pharm_phone_lineS=  WebServiceClientData.tel1.toString();
                if(pharm_phone_lineS.length()>1){
                    pharm_phone_line.setText("رقم الهاتف الارضي:"+" "+pharm_phone_lineS);
                }
                if(pharm_phone_lineS.length()<1){
                    pharm_phone_line.setText("رقم الهاتف الارضي:"+" ");
                }
                pharm_phone_mobileS="";
                pharm_phone_mobileS=  WebServiceClientData.tel1.toString();
                if(pharm_phone_mobileS.length()>1){

                    pharm_phone_mobile.setText("رقم الهاتف الارضي:"+" "+pharm_phone_mobileS);
                }
                if(pharm_phone_lineS.length()<1){
                    pharm_phone_mobile.setText("رقم الهاتف الارضي:"+" ");
                }
                pharm_phone_mobile.setText("رقم الهاتف المحمول:"+" "+WebServiceClientData.mob1.toString());
                pharm_doc_name.setText("اسم الصيدلي:"+" "+WebServiceClientData.manager_name.toString());
                con_month_val.setText("الاشتراك الشهري:"+" "+WebServiceClientDataMore.monthly_fee.toString());
                uni_new_mon_val.setText(WebServiceClientDataMore.monthly_fee.toString());
//                if(WebServiceClientDataMore.monthly_fee.toString().length()>0){
//                    uni_OLD_mon_v_INT=Integer.parseInt(WebServiceClientDataMore.monthly_fee.toString());
//                }
//                else {
//                    uni_OLD_mon_v_INT=0;
//                }


                con_down_val.setText("مقدم العقد:"+" "+WebServiceClientDataMore.down_pay.toString());

                //con_remaning_val.setText("الدفعة المقدمة:"+" "+WebServiceClientDataMore.remaining.toString());
                remaining="";
                remaining=WebServiceClientDataMore.remaining.toString();
                con_val.setText("الدفعة السابقة:"+" "+WebServiceClientDataMore.prog_price.toString());
                con_type.setText("نوع العقد:"+" "+WebServiceContTypeData.name.toString());




                // updatePharmCreatData(WebServiceClientData.date_created.toString());
                //nc_running_date.setText(WebServiceClientData.date_created.toString());


            }
            catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }

        }

    }




    private class save_phar_uni extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(OrderActivity.this);
            pDialog.setMessage("إنتظر قليلا...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {
                WebServiceInvoiceNumberData.invoice_number="";

                WebServiceCall webc =new WebServiceCall(OrderActivity.this);

                webc.Get_invoice_number();



                activate_pahr_codeS=WebServiceInvoiceNumberData.invoice_number.toString();
                Log.e(TAG, "activate_pahr_codeS : " + activate_pahr_codeS.toString());


                if(WebServiceResult.ErrorID==0){

                    for(int l=(uni_num_INT); l<(uni_num_sall_INT+uni_num_INT); l++){
                        uni_numS=String.valueOf(l);

                        webc.save_phar_add_items("1",uni_val.getText().toString(),uni_numS,Total_valS,P_Code,empsercode,uni_note.getText().toString(),uni_new_mon_val.getText().toString(),activate_pahr_codeS);

                    }
                }
                if(WebServiceResult.ErrorID==0){

                    webc.Insert_Emp_Daily_Report(empsercode,P_Code,doc_nameS,uni_note.getText().toString(),pharm_nameS,pharm_phone_lineS,BranchCodeS);

                }
                if(WebServiceResult.ErrorID==1){
                    Toast.makeText(getApplicationContext(),
                            "حدث خطأ",
                            Toast.LENGTH_LONG)
                            .show();
                    Log.e(TAG, "save_phar_uni onPostExecute Error: " + WebServiceResult.ErrorMessage.toString());

                }




            }
            catch(Exception ex)
            {
//                Toast.makeText(getApplicationContext(),
//                        "تعذر الإتصال بالإنترنت, راجع مزود الإنترنت",
//                        Toast.LENGTH_LONG)
//                        .show();
//                Log.e(TAG, "getAllClient Error: " + ex.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            try{

                // Dismiss the progress dialog
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                if(WebServiceResult.ErrorID==0){
                    Toast.makeText(getApplicationContext(),
                            "تم ارسال طلبك بنجاح",
                            Toast.LENGTH_LONG)
                            .show();

                    finish();

                }
                if(WebServiceResult.ErrorID==1){
                    Toast.makeText(getApplicationContext(),
                            "حدث خطأ",
                            Toast.LENGTH_LONG)
                            .show();
                    Log.e(TAG, "save_phar_uni onPostExecute Error: " + WebServiceResult.ErrorMessage.toString());

                }


            }
            catch (Exception ex) {


                Log.e(TAG, "getAllClient onPostExecute Error: " + ex.getMessage());

            }

        }

    }













    @Override
          public boolean onOptionsItemSelected(MenuItem item) {
              switch (item.getItemId()) {
                  case android.R.id.home:
                      // API 5+ solution
                      onBackPressed();
                      return true;

                  default:
                      return super.onOptionsItemSelected(item);
              }
          }
          @Override
          public void onBackPressed() {
              DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
              if (drawer.isDrawerOpen(GravityCompat.START)) {
                  drawer.closeDrawer(GravityCompat.START);
              } else {
                  super.onBackPressed();
              }
          }



    }
