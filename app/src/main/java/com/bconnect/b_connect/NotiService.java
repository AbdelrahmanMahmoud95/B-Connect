package com.bconnect.b_connect;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public  class NotiService extends Service {

    public static Timer timer ;


    private Context ctx;

    SharedPreferencesSR SPsr=new SharedPreferencesSR();

    Integer Localunit,Liveunit,LocalInstall,LiveInstall,LocalNMS,LiveNMS;
    public CheckNotiUni myTaskUni;
    public CheckNotiInstall myTaskInstall;
    public CheckNoti_NMS myTask_NMS;
    public sendLoginData myTask_Login;

    TimerTask myTimerTask;

    String  empsercode;

    int error_time=0;

    int notofiTime=0;
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    private String IPWork,asm,ser=null;


    public void onCreate()
    {
        super.onCreate();
        ctx = this;
        startService();

        try{

            empsercode="";
            empsercode=SPsr.getSP(getApplicationContext(),"empsercode").toString();
            IPWork=SPsr.getSP(getApplicationContext(),"IP_iswork").toString();

            asm=SPsr.getSP(getApplicationContext(),"asm").toString();
            ser=SPsr.getSP(getApplicationContext(),"ser").toString();


        }catch (Exception ex){
            Log.e("SR", "basic Error: " + ex.getMessage());


        }


    }

    private void startService()
    {

        StartTimerTask();

    }

    public void StartTimerTask(){

        myTimerTask = new TimerTask() {
            public void run() {
                toastHandler.sendEmptyMessage(0);

                Log.e("SR", "mainTask :myTask is Run " );


                myTaskUni= new NotiService.CheckNotiUni();
                myTaskUni.execute();

                myTaskInstall= new NotiService.CheckNotiInstall();
                myTaskInstall.execute();

                myTask_NMS= new NotiService.CheckNoti_NMS();
                myTask_NMS.execute();
                myTask_Login= new NotiService.sendLoginData();
                myTask_Login.execute();



            }};

        // public void schedule (TimerTask task, long delay, long period)
        if(timer==null){
            timer = new Timer();

            timer.scheduleAtFixedRate(myTimerTask, 0, 2000);



//            Toast.makeText(this, "Service Start ...", Toast.LENGTH_SHORT).show();
            Log.e("SR TIMER", "Service Start ...");


        }

    }

    public void stopTimerTask() {
        stopService(new Intent(NotiService.this, NotiService.class));


        if (myTaskUni != null || myTaskInstall != null) {


            myTaskUni.cancel(true);
            myTaskInstall.cancel(true);
            myTask_NMS.cancel(true);
            myTask_Login.cancel(true);

            if(timer != null) {
                timer.cancel();
                timer.purge();

                timer = null;
                

//                Toast.makeText(this, "Service Stopped ..." , Toast.LENGTH_SHORT).show();
                Log.e("SR TIMER", "Service Stopped ...");

            }
        }
    }



    public void onDestroy()
    {
        super.onDestroy();

        stopTimerTask();

    }



    private final Handler toastHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
           // Toast.makeText(getApplicationContext(), "Scan", Toast.LENGTH_SHORT).show();
            Log.e("SR TIMER", "Scan");

        }
    };
    private void generateNotificationHolde(Context context, String message) {
        cancelNotification(ctx,235);
        cancelNotification(ctx,236);
        cancelNotification(ctx,237);


        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra("is_noti_mess","1");
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(LoginActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        int NOTIFICATION_ID = 234;
        String CHANNEL_ID = "B-Connect_01";
        CharSequence name = "B-Connect_channel";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            String Description = "B-Connect";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.setShowBadge(true);
            mChannel.setSound(null, null);

            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.bclogowt)
                .setContentTitle("B-Connect")
                .setContentText(message);
        builder.setSound(null);
         builder.setOngoing(false);



        stackBuilder.addParentStack(LoginActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }
    private void generateNoti(Context context, String message,String TitleS,String Footer,int NotiID ,Class ActivityClass) {

        Intent notificationIntent = new Intent(context, ActivityClass);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ActivityClass);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        int NOTIFICATION_ID = NotiID;
        String CHANNEL_ID = "B-Connect_01";
        CharSequence name = "B-Connect_channel";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            String Description = "B-Connect";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(message);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300});
            mChannel.setShowBadge(false);
            mChannel.setName(Description);


            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mChannel.setSound(alarmSound,att);
            notificationManager.createNotificationChannel(mChannel);
         }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.bclogowt)
                .setContentTitle(TitleS)
                 .setContentText(message);

        builder.setVibrate(new long[] { 100, 200, 300});

            //LED
            builder.setLights(Color.RED, 3000, 3000);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);
            builder.setAutoCancel(true);


            Intent resultIntent = new Intent(context, ActivityClass);
         stackBuilder.addParentStack(ActivityClass);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }
     private void generateNotiBig(Context context, String message,String TitleS,String Footer,int NotiID ,Class ActivityClass) {

        Intent notificationIntent = new Intent(context, ActivityClass);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ActivityClass);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        int NOTIFICATION_ID = NotiID;
        String CHANNEL_ID = "B-Connect_01";
        CharSequence name = "B-Connect_channel";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            String Description = "B-Connect";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300});
            mChannel.setShowBadge(false);


            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mChannel.setSound(alarmSound,att);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(message);
        bigText.setBigContentTitle(TitleS);
        bigText.setSummaryText(Footer);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.bclogowt)
                .setContentTitle("B-Connect - "+TitleS)
                .setStyle(bigText);

        builder.setVibrate(new long[] { 100, 200, 300});

        //LED
        builder.setLights(Color.RED, 3000, 3000);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setAutoCancel(true);


        Intent resultIntent = new Intent(context, ActivityClass);
        stackBuilder.addParentStack(ActivityClass);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }


    private class CheckNotiUni extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog



        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {
                if(SPsr.getSP(getApplicationContext(),"empsercode").toString().length()>0){
                    WSNotificationResult.ErrorID=235;
                    WebServiceCall webc =new WebServiceCall(getApplicationContext());
                    webc.Noti_unit_app(SPsr.getSP(getApplicationContext(),"empsercode").toString());

                }

            }
            catch(Exception ex)
            {

                Log.e("SR", "CheckNoti : تعذر الإتصال, راجع مزود الانترنت");
                Log.e("SR", "CheckNoti Error: " + ex.getMessage());

             }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            try{

                if(WSNotificationResult.ErrorID==0){


                    if(SPsr.getSP(getApplicationContext(),"Noti_unit_app").length()==0){
                        SPsr.setSP(getApplicationContext(),"Noti_unit_app",WSNotificationResult.unit_app_count.toString());
                        Localunit=Integer.parseInt(SPsr.getSP(getApplicationContext(),"Noti_unit_app"));

                        generateNoti(getApplicationContext(), "لديك وحدات موافق عليها عددها: "+String.valueOf(Localunit),"تحصيل الوحدات الإضافية","",235,UnitApproveActivity.class);
                    }
                    else   if(SPsr.getSP(getApplicationContext(),"Noti_unit_app").length()>0){
                        if(WSNotificationResult.unit_app_count.toString().length()>0){
                            Liveunit=Integer.parseInt(WSNotificationResult.unit_app_count.toString());

                        }

                        if(SPsr.getSP(getApplicationContext(),"Noti_unit_app").length()>0){
                            Localunit=Integer.parseInt(SPsr.getSP(getApplicationContext(),"Noti_unit_app"));

                        }

                        if(Liveunit>Localunit){
                            SPsr.setSP(getApplicationContext(),"Noti_unit_app",WSNotificationResult.unit_app_count.toString());
                            generateNoti(getApplicationContext(), "لديك وحدات موافق عليها عددها: "+String.valueOf(Localunit),"تحصيل الوحدات الإضافية","",235,UnitApproveActivity.class);

                        }

                    }


                    cancelNotification(ctx,234);

                }
                else if(WSNotificationResult.ErrorID==1||WSNotificationResult.ErrorID==235){

                    myTaskUni.cancel(true);

                    error_time+=1;
                    if(error_time==1){
                        

                    }
                }

            }
            catch (Exception ex) {


                Log.e("SR", "getAllClient onPostExecute Error: " + ex.getMessage());

            }

        }
        @Override
        protected void onCancelled(Void result) {
            
            Log.e("SR", "myTask is onCancelled " );

        }

    }
    private class CheckNotiInstall extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {
                if(SPsr.getSP(getApplicationContext(),"empsercode").toString().length()>0){
                    WSNotificationResult.ErrorID=236;
                    WebServiceCall webc =new WebServiceCall(getApplicationContext());
                    webc.Noti_install(SPsr.getSP(getApplicationContext(),"empsercode").toString());
                 }

            }
            catch(Exception ex)
            {

                Log.e("SR", "CheckNotiInstall : تعذر الإتصال, راجع مزود الانترنت");
                Log.e("SR", "CheckNotiInstall Error: " + ex.getMessage());

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            try{

                if(WSNotificationResult.ErrorID==0){


                    ////////////////////////////////// INSTALL  //////////////////////////////////



                    if(SPsr.getSP(getApplicationContext(),"Noti_install").length()==0){
                        SPsr.setSP(getApplicationContext(),"Noti_install",WSNotificationResult.install_count.toString());
                        LocalInstall=Integer.parseInt(SPsr.getSP(getApplicationContext(),"Noti_install"));
                        if(LocalInstall>0) {
                            generateNoti(getApplicationContext(), "لديك عملاء في التركيبات الجديدة عددهم: " + String.valueOf(LocalInstall),"التركيبات الجديدة","",236,UnderInstalListActivity.class);
                        }
                    }
                    else   if(SPsr.getSP(getApplicationContext(),"Noti_install").length()>0){
                        if(WSNotificationResult.install_count.toString().length()>0){
                            LiveInstall=Integer.parseInt(WSNotificationResult.install_count.toString());

                        }

                        if(SPsr.getSP(getApplicationContext(),"Noti_install").length()>0){
                            LocalInstall=Integer.parseInt(SPsr.getSP(getApplicationContext(),"Noti_install"));

                        }

                        if(LiveInstall>LocalInstall){
                            SPsr.setSP(getApplicationContext(),"Noti_install",WSNotificationResult.install_count.toString());
                            generateNoti(getApplicationContext(), "لديك عملاء في التركيبات الجديدة عددهم: "+String.valueOf(LiveInstall),"التركيبات الجديدة","",236,UnderInstalListActivity.class);

                        }

                    }

                    cancelNotification(ctx,234);




                }
                else if(WSNotificationResult.ErrorID==1||WSNotificationResult.ErrorID==236){

                    myTaskInstall.cancel(true);

                    error_time+=1;
                    if(error_time==1){
                        

                    }
                }

            }
            catch (Exception ex) {

                Log.e("SR", "getAllClient onPostExecute Error: " + ex.getMessage());
                error_time+=1;
                if(error_time==1){
                    

                }
            }

        }
        @Override
        protected void onCancelled(Void result) {
             
            Log.e("SR", "myTask is onCancelled " );

        }

    }


    private class CheckNoti_NMS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog


        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {
                if(SPsr.getSP(getApplicationContext(),"empsercode").toString().length()>0){
                    WSNotificationResult.ErrorID=237;
                    WebServiceCall webc =new WebServiceCall(getApplicationContext());
                    webc.Noti_nms(SPsr.getSP(getApplicationContext(),"empsercode").toString(),"3");
                }

            }
            catch(Exception ex)
            {
                Log.e("SR", "CheckNoti_NMS : تعذر الإتصال, راجع مزود الانترنت");
                Log.e("SR", "CheckNoti_NMS Error: " + ex.getMessage());

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            notofiTime+=1;
            try{

                if(WSNotificationResult.ErrorID==0){


                    ////////////////////////////////// CheckNoti_NMS  //////////////////////////////////



                    if(SPsr.getSP(getApplicationContext(),"CheckNoti_NMS").length()==0){
                        SPsr.setSP(getApplicationContext(),"CheckNoti_NMS",WSNotificationResult.ms_count.toString());
                        LocalNMS=Integer.parseInt(SPsr.getSP(getApplicationContext(),"CheckNoti_NMS"));
                        if(LocalNMS>0) {
                            generateNoti(getApplicationContext(), "لديك عملاء موافق عليهم من قبل الإدارة عددهم: " + String.valueOf(LocalNMS) ,"مبيعاتي","" ,237,MySaleListActivity.class);
                        }
                    }
                    else   if(SPsr.getSP(getApplicationContext(),"CheckNoti_NMS").length()>0){
                        if(WSNotificationResult.ms_count.toString().length()>0){
                            LiveNMS=Integer.parseInt(WSNotificationResult.ms_count.toString());

                        }

                        if(SPsr.getSP(getApplicationContext(),"CheckNoti_NMS").length()>0){
                            LocalNMS=Integer.parseInt(SPsr.getSP(getApplicationContext(),"CheckNoti_NMS"));

                        }

                        if(LiveNMS>LocalNMS){
                            SPsr.setSP(getApplicationContext(),"CheckNoti_NMS",WSNotificationResult.ms_count.toString());
                            generateNotiBig(getApplicationContext(), "\n تم الموافقة على العميل : "+
                                            WSNotificationResult.ms_phar_name.toString()+"\n موقف العميل الان : "+
                                            WSNotificationResult.ms_phar_appr.toString(),"مبيعاتي","لديك عملاء موافق عليهم من قبل الإدارة عددهم: " + String.valueOf(LocalNMS)
                                    ,(237+notofiTime),MySaleListActivity.class);

                        }

                    }


                    cancelNotification(ctx,234);



                }
                else if(WSNotificationResult.ErrorID==1||WSNotificationResult.ErrorID==237){

                    myTask_NMS.cancel(true);
                    error_time+=1;
                    if(error_time==1){
                        

                    }

                }

            }
            catch (Exception ex) {


                Log.e("SR", "getAllClient onPostExecute Error: " + ex.getMessage());

            }

        }
        @Override
        protected void onCancelled(Void result) {

            
            Log.e("SR", "myTask is onCancelled " );

        }

    }


    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }



    class sendLoginData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            Log.e("SR", "sendLoginData.service.RUN");
            WebServiceResult.ErrorID=777;
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Void... arg0) {
            try{

                if(IPWork.length()>1&&asm.length()>1){
                    WebServiceCall cweb =new WebServiceCall(ctx);
                    cweb.Login(asm.toString(),ser.toString());

                }
                else {

                    WebServiceResult.ErrorID=6;

                }


            }catch (Exception ex){


                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try{

                if(WebServiceResult.ErrorID==6){
                    Log.e("SR", "sendLoginData.service.CATCH");

                    SPsr.removeAllSP(ctx);


                    Intent intent = new Intent(ctx, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                     startActivity(intent);

                    stopService(new Intent(ctx, NotiService.class));



                }
                if(WebServiceResult.ErrorID==1){
                    Log.e("SR", "sendLoginData.service.CATCH");


                    SPsr.removeAllSP(ctx);


                    Intent intent = new Intent(ctx, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);

                    stopService(new Intent(ctx, NotiService.class));

                    //  StartGetIP();

                }
                if(WebServiceResult.ErrorID==0){
                    Log.e("SR", "sendLoginData.service.OK");

                }
            }catch (Exception ex){



                ex.printStackTrace();
            }

        }

        @Override
        protected void onCancelled() {

            myTask_Login=null;

            super.onCancelled();
        }
    }
}

