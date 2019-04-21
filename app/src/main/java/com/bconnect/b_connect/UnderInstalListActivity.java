package com.bconnect.b_connect;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UnderInstalListActivity extends AppCompatActivity {
    SharedPreferencesSR SPsr=new SharedPreferencesSR();

    private String TAG = UnderInstalListActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    SwipeRefreshLayout pullToRefresh;
    private ListView lv;
    private ListAdapter branchadapter;
    private static String url;
    ArrayList<HashMap<String, String>> branchlist;

    String phar_code,stage_flag,empnameS,empcodeS,stage_flagAF;
    int ToActivityINT ,stage_flagINT;

    SharedPreferences pref ;
    SharedPreferences.Editor editor;

     Dialog u_install_dialog,u_install_note_dialog;

     Button instal_setup_bt,instal_tran_bt,instal_get_dow_pay_bt,instal_note_bt,instal_note_send_bt;

     EditText instal_D_note;

    public AsyncTask<Void, Void, Void> myTask;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_under_instal_list);
        basic();
        control();

        myTask=new UnderInstalListActivity.GetUnderInstallClient().execute();


    }

    public  void basic(){




        pDialog = new ProgressDialog(UnderInstalListActivity.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setTitle("جري جلب البيانات...");
        pDialog.setMessage("إنتظر قليلا");
        pDialog.setCancelable(true);

         pref= getApplicationContext().getSharedPreferences("MySr", MODE_PRIVATE);
         editor = pref.edit();

        empnameS=  SPsr.getSP(getApplicationContext(),"empsername");
        empcodeS=  SPsr.getSP(getApplicationContext(),"empsercode");


        url = "http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/Get_client_under_install?emp_code="+empcodeS;



        lv = (ListView) findViewById(R.id.install_list);

        branchlist = new ArrayList<>();


       // Intent intent = getIntent();

        u_install_dialog = new Dialog(UnderInstalListActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        u_install_dialog.setContentView(R.layout.dialog_install_process);
        u_install_dialog.setCancelable(true);



        u_install_note_dialog = new Dialog(UnderInstalListActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        u_install_note_dialog.setContentView(R.layout.dialog_install_note);
        u_install_note_dialog.setCancelable(true);


        instal_setup_bt= (Button) u_install_dialog.findViewById(R.id.instal_setup_bt);
        instal_tran_bt= (Button) u_install_dialog.findViewById(R.id.instal_tran_bt);
        instal_get_dow_pay_bt= (Button) u_install_dialog.findViewById(R.id.instal_get_dow_pay_bt);
        instal_note_bt= (Button) u_install_dialog.findViewById(R.id.instal_note_bt);

        instal_D_note= (EditText) u_install_note_dialog.findViewById(R.id.instal_D_note);
        instal_note_send_bt= (Button) u_install_note_dialog.findViewById(R.id.instal_note_send_bt);

          pullToRefresh = findViewById(R.id.pullToRefresh);


    }
    public  void control(){


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myTask=new UnderInstalListActivity.GetUnderInstallClient().execute();

                pullToRefresh.setRefreshing(false);
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                HashMap<String, Object> obj = (HashMap<String, Object>) arg0.getAdapter().getItem(position);

                phar_code = (String) obj.get("code");


                Log.e(TAG, "code: " + phar_code);

                new UnderInstalListActivity.GetClientStage().execute();





                             //      Object object
                //   Toast.makeText(UnderInstalListActivity.this, "" + BranchCodeAF, Toast.LENGTH_SHORT).show();

//                if(ToActivityINT==1){
//                    Intent i = new Intent(BranchsActivity.this, ClientByBranchActivity.class);
//                    i.putExtra("BranchCodeToActivity", BranchCodeToActivity);
//                    i.putExtra("ToActivity", ToActivity);
//                    i.putExtra("activation_flag", activation_flag);
//                    startActivity(i);
//                }







            }
        });
        instal_setup_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stage_flag="k";
                u_install_dialog.dismiss();
                new UnderInstalListActivity.UpdateClientStage().execute();

            }
        });
        instal_get_dow_pay_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stage_flag="f";
                u_install_dialog.dismiss();
                new UnderInstalListActivity.UpdateClientStage().execute();

            }
        });

        instal_note_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                u_install_dialog.dismiss();
                u_install_note_dialog.show();


            }
        });

        instal_note_send_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    new UnderInstalListActivity.UpdateSetupNote().execute();


            }
        });

        instal_tran_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                u_install_dialog.dismiss();
                finish();
                Intent i = new Intent(UnderInstalListActivity.this, InstallActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("P_Code", phar_code);
                startActivity(i);

            }
        });




    }


    private class GetUnderInstallClient extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

            pDialog.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    myTask.cancel(true);
                }
            }, 30000);

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
                         String motaheda_code = c.getString("motaheda_code");
                         String phar_name = c.getString("phar_name");
                          String stage_flag = c.getString("stage_flag");
                         String  rep_setup_notes = c.getString("rep_setup_notes");
                         String client_ck = c.getString("c_k");

                         String client_phon ;
                         String tel1="";
                         String mob1="";
                         if(c.getString("tel1")!="null"||c.getString("tel1")!=null){
                             tel1=c.getString("tel1").toString();
                         }
                         if(c.getString("mob1")!="null"||c.getString("mob1")!=null){
                             mob1=c.getString("mob1").toString();
                         }
                         client_phon=tel1+" - "+mob1;

                         String client_branch = c.getString("Column1");
                         String client_con_type = c.getString("Column3")+" / " + c.getString("Column2") ;
                         String client_down_pay = c.getString("remaining");
                         String client_monthly_val = c.getString("monthly_fee");
                         String client_add = c.getString("address");
                         String client_setupdate = c.getString("will_setup_date");
                          String client_noteM = c.getString("super_notes");
                         String client_noteS = c.getString("snotes");

                         if(client_noteM=="null"||client_noteM==null){
                             client_noteM="";
                         }
                         if(client_noteS=="null"||client_noteS==null){
                             client_noteS="";
                         }
                         if(rep_setup_notes=="null"||rep_setup_notes==null){
                             rep_setup_notes="";
                         }


                         HashMap<String, String> contact = new HashMap<>();

                         contact.clear();
                         contact.put("code", code);
                         contact.put("motaheda_code", motaheda_code);
                         contact.put("phar_name", phar_name);
                         contact.put("rep_setup_notes", rep_setup_notes);
                         contact.put("client_ck", client_ck);
                         contact.put("stage_flag", stage_flag);
                         contact.put("client_phon", client_phon);
                         contact.put("client_branch", client_branch);
                         contact.put("client_con_type", client_con_type);
                         contact.put("client_down_pay", client_down_pay);
                         contact.put("client_monthly_val", client_monthly_val);
                         contact.put("client_add", client_add);
                         contact.put("client_setupdate", client_setupdate.substring(0, 10).replace("-","/"));
                         contact.put("client_noteM", client_noteM);
                         contact.put("client_noteS", client_noteS);




                        branchlist.add(contact);





                        // adding contact to contact list

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
                        SPsr.removeAllSP(UnderInstalListActivity.this);
                        finish();
                        Intent intent = new Intent(UnderInstalListActivity.this, LoginActivity.class);
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



            try{
                if (pDialog.isShowing())
                {pDialog.dismiss();}


                branchadapter = new SimpleAdapter(
                        UnderInstalListActivity.this, branchlist,
                        R.layout.under_install_list_item, new String[]{"phar_name","motaheda_code","client_ck","client_phon","client_branch"
                                                                        ,"client_con_type","client_down_pay","client_monthly_val","client_add","client_setupdate"
                                                                        ,"client_noteM","client_noteS","rep_setup_notes"},
                        new int[]{R.id.client_name ,R.id.client_code ,R.id.client_ck ,R.id.client_phon ,R.id.client_branch ,R.id.client_con_type ,R.id.client_down_pay ,R.id.client_monthly_val
                                ,R.id.client_add ,R.id.client_setupdate ,R.id.client_noteM ,R.id.client_noteS , R.id.client_noteI});
                lv.setAdapter(null);

                lv.setAdapter(branchadapter);


            }
            catch (Exception ex) {
                Toast.makeText(getApplicationContext(),
                        "خطأ" ,
                        Toast.LENGTH_LONG)
                        .show();

                Log.e(TAG, "LIST Error: " + ex.getMessage());

            }


        }

        @Override
        protected void onCancelled(Void result) {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }




    }


    private class GetClientStage extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

            pDialog.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    myTask.cancel(true);
                }
            }, 10000);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            WebServiceCall wsc=new WebServiceCall(UnderInstalListActivity.this);
            WebServiceResult.ErrorID=0;
            WebServiceResult.ErrorMessage="";

            wsc.Get_client_stage(phar_code);


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            try{   // Dismiss the progress dialog
                if (pDialog.isShowing())
                {pDialog.dismiss();}


                if(WebServiceResult.ErrorID>0){

                    //under setup
                    if(WebServiceResult.ErrorID==1){

                        instal_setup_bt.setVisibility(View.VISIBLE);
                        instal_tran_bt.setVisibility(View.GONE);
                        instal_get_dow_pay_bt.setVisibility(View.GONE);
                        instal_note_bt.setVisibility(View.VISIBLE);
                        u_install_dialog.show();

                    }
                    //under tran
                    else if(WebServiceResult.ErrorID==2){

                        instal_setup_bt.setVisibility(View.GONE);
                        instal_tran_bt.setVisibility(View.VISIBLE);
                        instal_get_dow_pay_bt.setVisibility(View.GONE);
                        instal_note_bt.setVisibility(View.GONE);
                        Log.e(TAG, "stage_flag: " + "under tran");

                        u_install_dialog.show();

                    }
                    //under down_pay
                    else if(WebServiceResult.ErrorID==3){

                        instal_setup_bt.setVisibility(View.GONE);
                        instal_tran_bt.setVisibility(View.GONE);
                        instal_get_dow_pay_bt.setVisibility(View.VISIBLE);
                        instal_note_bt.setVisibility(View.GONE);
                        u_install_dialog.show();

                    }
                    //else
                    else {

                        instal_setup_bt.setVisibility(View.GONE);
                        instal_tran_bt.setVisibility(View.GONE);
                        instal_get_dow_pay_bt.setVisibility(View.GONE);
                        instal_note_bt.setVisibility(View.GONE);

                    }

                    Log.e(TAG, "code: " + phar_code);


                }

            }
            catch (Exception ex) {
                Toast.makeText(getApplicationContext(),
                        "خطأ" ,
                        Toast.LENGTH_LONG)
                        .show();

                Log.e(TAG, "LIST Error: " + ex.getMessage());

            }


        }

        @Override
        protected void onCancelled(Void result) {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }


    }

    private class UpdateClientStage extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(UnderInstalListActivity.this);
            pDialog.setMessage("إنتظر قليلا...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            WebServiceCall wsc=new WebServiceCall(UnderInstalListActivity.this);
            WebServiceResult.ErrorID=0;
            WebServiceResult.ErrorMessage="";
            Log.e(TAG, "code: " + phar_code);

            if(phar_code.length()>0){
                wsc.Update_client_stage(phar_code,stage_flag);

            }else{
                Log.e(TAG, "phar_code: IS EMPETY");

            }



            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            try{   // Dismiss the progress dialog
                if (pDialog.isShowing())
                {pDialog.dismiss();}


                if(WebServiceResult.ErrorID==0){

                    //under setup
                    Toast.makeText(getApplicationContext(),
                            "تمت العملية بنجاح" ,
                            Toast.LENGTH_LONG)
                            .show();

                    myTask=new UnderInstalListActivity.GetUnderInstallClient().execute();


                }else {
                    Toast.makeText(getApplicationContext(),
                            "خطأ" ,
                            Toast.LENGTH_LONG)
                            .show();
                }

            }
            catch (Exception ex) {
                Toast.makeText(getApplicationContext(),
                        "خطأ" ,
                        Toast.LENGTH_LONG)
                        .show();

                Log.e(TAG, "LIST Error: " + ex.getMessage());

            }


        }




    }

    private class UpdateSetupNote extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(UnderInstalListActivity.this);
            pDialog.setMessage("إنتظر قليلا...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            WebServiceCall wsc=new WebServiceCall(UnderInstalListActivity.this);
            WebServiceResult.ErrorID=0;
            WebServiceResult.ErrorMessage="";
            Log.e(TAG, "code: " + phar_code);

            if(phar_code.length()>0){
                wsc.Update_client_setup_note(phar_code,instal_D_note.getText().toString());

            }else{
                Log.e(TAG, "phar_code: IS EMPETY");

            }



            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            try{   // Dismiss the progress dialog
                if (pDialog.isShowing())
                    pDialog.dismiss();


                if(WebServiceResult.ErrorID==0){

                    //under setup
                    Toast.makeText(getApplicationContext(),
                            "تمت العملية بنجاح" ,
                            Toast.LENGTH_LONG)
                            .show();
                    u_install_note_dialog.cancel();

                    myTask=new UnderInstalListActivity.GetUnderInstallClient().execute();


                }else {
                    Toast.makeText(getApplicationContext(),
                            "خطأ" ,
                            Toast.LENGTH_LONG)
                            .show();
                }

            }
            catch (Exception ex) {
                Toast.makeText(getApplicationContext(),
                        "خطأ" ,
                        Toast.LENGTH_LONG)
                        .show();

                Log.e(TAG, "LIST Error: " + ex.getMessage());

            }


        }




    }

    @Override
    protected void onResume() {
        url = "http://"+SPsr.getSP(getApplicationContext(),"IP_iswork")+"/webser/Android.asmx/Get_client_under_install?emp_code="+empcodeS;
        myTask=new UnderInstalListActivity.GetUnderInstallClient().execute();


        super.onResume();
    }

    //    public Bitmap getWholeListViewItemsToBitmap() {
//
//        ListView listview    = lv;
//        ListAdapter adapter  = listview.getAdapter();
//        int itemscount       = adapter.getCount();
//        int allitemsheight   = 0;
//        List<Bitmap> bmps    = new ArrayList<Bitmap>();
//
//        for (int i = 0; i < itemscount; i++) {
//
//            View childView      = adapter.getView(i, null, listview);
//            childView.measure(View.MeasureSpec.makeMeasureSpec(listview.getWidth(), View.MeasureSpec.EXACTLY),
//                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//
//            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
//            childView.setDrawingCacheEnabled(true);
//            childView.buildDrawingCache();
//            bmps.add(childView.getDrawingCache());
//            allitemsheight+=childView.getMeasuredHeight();
//        }
//        Bitmap bigbitmap    = Bitmap.createBitmap(listview.getMeasuredWidth(), allitemsheight, Bitmap.Config.ARGB_8888);
//        Canvas bigcanvas    = new Canvas(bigbitmap);
//        Paint paint = new Paint();
//        int iHeight = 0;
//        for (int i = 0; i < bmps.size(); i++) {
//            Bitmap bmp = bmps.get(i);
//            bigcanvas.drawBitmap(bmp, 0, iHeight, paint);
//            iHeight+=bmp.getHeight();
//            bmp.recycle();
//            bmp=null;
//        }
//        return bigbitmap;
//    }
//
//
//
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void createPdf(Bitmap bs){
//        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        float hight = displaymetrics.heightPixels ;
//        float width = displaymetrics.widthPixels ;
//
//        int convertHighet = (int) hight, convertWidth = (int) width;
//
////        Resources mResources = getResources();
////        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);
//
//        PdfDocument document = new PdfDocument();
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
//        PdfDocument.Page page = document.startPage(pageInfo);
//
//        Canvas canvas = page.getCanvas();
//
//
//        Paint paint = new Paint();
//        paint.setColor(Color.parseColor("#ffffff"));
//        canvas.drawPaint(paint);
//
//
//
//        bitmap = Bitmap.createScaledBitmap(bs, bs.getWidth(), bs.getHeight(), true);
//
//        paint.setColor(Color.BLUE);
//        canvas.drawBitmap(bitmap, 0, 0 , null);
//        document.finishPage(page);
//
//
//        // write the document content
//        String targetPdf = "/sdcard/test.pdf";
//        File filePath = new File(targetPdf);
//        try {
//            document.writeTo(new FileOutputStream(filePath));
//                boolean_save=true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
//        }
//
//        // close the document
//        document.close();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK) {
//
//            if (resultCode == RESULT_OK) {
//                Uri selectedImage = data.getData();
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                Cursor cursor = getContentResolver().query(
//                        selectedImage, filePathColumn, null, null, null);
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                String filePath = cursor.getString(columnIndex);
//                cursor.close();
//
//
//                bitmap = BitmapFactory.decodeFile(filePath);
//
//            }
//        }
//    }

//    private void fn_permission() {
//        if ((ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
//                (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
//
//            if ((ActivityCompat.shouldShowRequestPermissionRationale(UnderInstalListActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
//            } else {
//                ActivityCompat.requestPermissions(UnderInstalListActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
//                        REQUEST_PERMISSIONS);
//
//            }
//
//            if ((ActivityCompat.shouldShowRequestPermissionRationale(UnderInstalListActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
//            } else {
//                ActivityCompat.requestPermissions(UnderInstalListActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        REQUEST_PERMISSIONS);
//
//            }
//        } else {
//            boolean_permission = true;
//
//
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_PERMISSIONS) {
//
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                boolean_permission = true;
//
//
//            } else {
//                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();
//
//            }
//        }
//    }
//
//    private void requestStoragePermission() {
//
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
//            return;
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
//            return;
//
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            //If the user has denied the permission previously your code will come to this block
//            //Here you can explain why you need this permission
//            //Explain here why you need this permission
//        }
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//        }
//
//        //And finally ask for the permission
//        ActivityCompat.requestPermissions(this, new String[]
//                {
//                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//
//                }, REQUEST_PERMISSIONS);
//
//
//    }



}
