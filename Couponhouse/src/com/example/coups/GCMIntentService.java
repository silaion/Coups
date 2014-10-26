package com.example.coups;

/**
 * Created by DaeSeung on 2014-09-21.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gcm.GCMBaseIntentService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;

public class GCMIntentService extends GCMBaseIntentService {
    //    //레지스트레이션 id를 확인하는 코드였음
//    private static void generateNotification(Context context, String message) {
//
//        int icon = R.drawable.ic_launcher;
//        long when = System.currentTimeMillis();
//
//
//        NotificationManager notificationManager = (NotificationManager) context
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Notification notification = new Notification(icon, message, when);
//
//        String title = context.getString(R.string.app_name);
//
//        Intent notificationIntent = new Intent(context, FirstActivity.class);
//
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent intent = PendingIntent.getActivity(context, 0,
//                notificationIntent, 0);
//
//
//
//        notification.setLatestEventInfo(context, title, message, intent);
//
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        notificationManager.notify(0, notification);
//
//    }
//
//    @Override
//    protected void onError(Context arg0, String arg1) {
//
//    }
//
//    @Override
//    protected void onMessage(Context context, Intent intent) {
//
//        String msg = intent.getStringExtra("msg");
//        Log.e("getmessage", "getmessage:" + msg);
//        generateNotification(context,msg);
//
//    }
//
//
//
//    @Override
//
//    protected void onRegistered(Context context, String reg_id) {
//        Log.e("키를 등록합니다.(GCM INTENTSERVICE)", reg_id);
//    }
//
//
//
//    @Override
//
//    protected void onUnregistered(Context arg0, String arg1) {
//        Log.e("키를 제거합니다.(GCM INTENTSERVICE)","제거되었습니다.");
//    }
//여기까지
    static String gcm_msg = null;
    public ServerRequest serverRequest_insert = null;
    NotificationManager nm;
    Notification mNoti;
    static PendingIntent pIntent;

    Global global;

    Context mContext = null;

    String name, gender, phoneNum, birth;

    // GCM에 정상적으로 등록되었을경우 발생하는 메소드
    @Override
    protected void onRegistered(Context arg0, String arg1) {
        // TODO Auto-generated method stub
        Log.d("test", "등록ID:" + arg1);
        mContext = arg0;
        global = new Global();

        HashMap<Object, Object> param = new HashMap<Object, Object>();
        param.put("regid", arg1);
        param.put("name", global.name);
        param.put("gender", global.gender);
        param.put("birth", global.birth);
        param.put("phoneNum", global.phoneNum);
        //serverRequest_insert = new ServerRequest("http://112.172.217.79:8080/JSP_Server/add_AppKey.jsp", param, mResHandler, mHandler, arg0);
        serverRequest_insert = new ServerRequest("http://112.172.217.79:8080/JSP_Server/insert.jsp", param, mResHandler, mHandler, arg0);
        //serverRequest_insert = new ServerRequest("http://192.168.0.21:8081/gcm_jsp/insert.jsp", param, mResHandler, mHandler, arg0);
        serverRequest_insert.start();
    }

    /**
     * 요청후 핸들러에의해 리스트뷰 구성
     */
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            serverRequest_insert.interrupt();
            String result = msg.getData().getString("result");
            global = new Global();

            if (result.equals("success")) {
                Toast.makeText(mContext,"Coups에 가입 되신것을 환영합니다.", Toast.LENGTH_LONG).show();
                Log.d("regid", "데이터베이스에 regid가 등록되었습니다.");
                global.start = true;
            } else {
                Toast.makeText(mContext, "중복되는 사용자가 있습니다.\n 이미 가입하셨다면 기존 사용자등록을 이용해주세요.", Toast.LENGTH_LONG).show();
                Log.d("regid", "데이터베이스에 regid가 등록되지 않았습니다.");
            }
        }
    };


    /**
     * 요청후 response에 대한 파싱처리
     */
    private ResponseHandler<String> mResHandler = new ResponseHandler<String>() {

        public String handleResponse(HttpResponse response) throws java.io.IOException {

            HttpEntity entity = response.getEntity();
            Message message = mHandler.obtainMessage();
            Bundle bundle = new Bundle();
            String result = EntityUtils.toString(entity).trim();
            if (result.equals("success")) {
                bundle.putString("result", "success");
            } else {
                bundle.putString("result", "fail");
            }

            String msg = message.getData().getString("result");
            message.setData(bundle);
            mHandler.sendMessage(message);
            return null;
        }

    };

    // GCM에 해지하였을경우 발생하는 메소드
    @Override
    protected void onUnregistered(Context arg0, String arg1) {
        Log.d("test", "해지ID:" + arg1);
    }

    // GCM이 메시지를 보내왔을때 발생하는 메소드
    @Override
    protected void onMessage(Context arg0, Intent arg1) {
        // TODO Auto-generated method stub
        gcm_msg = arg1.getExtras().getString("test");
        if(gcm_msg.startsWith("N_")){
            global.c_Number = gcm_msg.substring(2);
            Log.d("Customer Number", String.valueOf(global.c_Number));
        }else {
            long[] pattern = {3000, 500, 0, 2000, 400};

            try {
                Vibrator vibrator =
                        (Vibrator) arg0.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(pattern, 1);
                //vibrator.vibrate(3000);
                setNotification(arg0, gcm_msg);
            } catch (Exception e) {
                Log.e("GCM_onMessage", "failed");
            }
            Log.d("test", "메시지가 왔습니다 : " + gcm_msg);
            showMessage();
        }
    }

    private void setNotification(Context context, String message){
        NotificationManager nm;
        Notification mNoti;
        try{
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, new Intent(context, TabOneActivity.class), 0);
            mNoti = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(message)
                    .setAutoCancel(true)
                    .setContentIntent(pIntent)
                    .build();
            nm.notify(0, mNoti);
        }catch(Exception e){
            e.printStackTrace();
            Log.d("GCM_setNotification", "failed");
        }
    }


    // 오류를 핸들링하는 메소드
    @Override
    protected void onError(Context arg0, String arg1) {
        Log.d("test", arg1);
    }


    // 서비스 생성자
    public GCMIntentService() {
        //super(MainActivity.PROJECT_ID);
        Log.d("test", "GCM서비스 생성자 실행");
    }

    public void showMessage() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                handler.sendEmptyMessage(0);
            }
        });
        thread.start();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
//            Toast.makeText(FirstActivity.mContext, "수신 메시지 : " + gcm_msg, Toast.LENGTH_LONG).show();
//            Toast.makeText(ThirdActivity.mContext, "수신 메시지 : " + gcm_msg, Toast.LENGTH_LONG).show();
            Log.d("GCM_handler", "수신 메시지 : " + gcm_msg);
        }
    };
}