package com.example.coups;

/**
 * Created by DaeSeung on 2014-09-21.
 */

import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
//    //������Ʈ���̼� id�� Ȯ���ϴ� �ڵ忴��
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
//        Log.e("Ű�� ����մϴ�.(GCM INTENTSERVICE)", reg_id);
//    }
//
//
//
//    @Override
//
//    protected void onUnregistered(Context arg0, String arg1) {
//        Log.e("Ű�� �����մϴ�.(GCM INTENTSERVICE)","���ŵǾ����ϴ�.");
//    }
//�������
    String gcm_msg = null;
    public ServerRequest serverRequest_insert = null;

    // GCM�� ���������� ��ϵǾ������ �߻��ϴ� �޼ҵ�
    @Override
    protected void onRegistered(Context arg0, String arg1) {
        // TODO Auto-generated method stub
        Log.d("test", "���ID:" + arg1);

        HashMap<Object, Object> param = new HashMap<Object, Object>();
        param.put("regid", arg1);
        serverRequest_insert = new ServerRequest("http://112.172.217.74:8080/JSP_Server/add_AppKey.jsp", param, mResHandler, mHandler);
        serverRequest_insert.start();
    }


    /**
     * ��û�� �ڵ鷯������ ����Ʈ�� ����
     */
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            serverRequest_insert.interrupt();
            String result = msg.getData().getString("result");

            if (result.equals("success")) {
//                Toast.makeText(ThirdActivity.mContext,"�����ͺ��̽��� regid�� ��ϵǾ����ϴ�.", Toast.LENGTH_LONG).show();
//                Toast.makeText(FirstActivity.mContext,"�����ͺ��̽��� regid�� ��ϵǾ����ϴ�.", Toast.LENGTH_LONG).show();
            } else {
//            	Toast.makeText(ThirdActivity.mContext,"�����ͺ��̽��� regid�� ��ϵ��� �ʾҽ��ϴ�.", Toast.LENGTH_LONG).show();
//                Toast.makeText(FirstActivity.mContext,"�����ͺ��̽��� regid�� ��ϵ��� �ʾҽ��ϴ�.", Toast.LENGTH_LONG).show();
            }
        }
    };


    /**
     * ��û�� response�� ���� �Ľ�ó��
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

            message.setData(bundle);
            mHandler.sendMessage(message);
            return null;
        }

    };

    // GCM�� �����Ͽ������ �߻��ϴ� �޼ҵ�
    @Override
    protected void onUnregistered(Context arg0, String arg1) {
        Log.d("test", "����ID:" + arg1);
    }

    // GCM�� �޽����� ���������� �߻��ϴ� �޼ҵ�
    @Override
    protected void onMessage(Context arg0, Intent arg1) {
        // TODO Auto-generated method stub
        Log.d("test", "�޽����� �Խ��ϴ� : " + arg1.getExtras().getString("test"));
        gcm_msg = arg1.getExtras().getString("test");
        showMessage();

    }


    // ������ �ڵ鸵�ϴ� �޼ҵ�
    @Override
    protected void onError(Context arg0, String arg1) {
        Log.d("test", arg1);
    }


    // ���� ������
    public GCMIntentService() {
        //super(MainActivity.PROJECT_ID);
        Log.d("test", "GCM���� ������ ����");
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
            Toast.makeText(FirstActivity.mContext, "���� �޽��� : " + gcm_msg, Toast.LENGTH_LONG).show();
            Toast.makeText(ThirdActivity.mContext, "���� �޽��� : " + gcm_msg, Toast.LENGTH_LONG).show();
        }
    };
}