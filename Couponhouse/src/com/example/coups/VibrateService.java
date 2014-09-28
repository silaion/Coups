package com.example.coups;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by DaeSeung on 2014-08-07.
 */
public class VibrateService extends Service {

    Socket s;
    private DataInputStream i;
    private DataOutputStream o;
    String l;
    Vibrator vib;
    private long[] pattern = {1000, 300, 1000, 500, 1000};
    
    NotificationManager nm;
    Notification mNoti;
    
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
    	String id = intent.getExtras().getString("phone");
    	new VibService().execute(id, null, null);
        return START_STICKY;
    }
    
    private class VibService extends AsyncTask<String, Void, Void>{
		@Override
		protected Void doInBackground(String... params){
			vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
			nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			mNoti = new NotificationCompat.Builder(getApplicationContext())
				.setContentTitle("주문하신 읍식이 나왔습니다.")
				.setAutoCancel(true)
				.build();
			
	        String id = params[0];
	        try{
	            s = new Socket("112.172.217.74", 8080);
	            i = new DataInputStream(new BufferedInputStream(s.getInputStream()));
	            o = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));;
	            Log.d("VibrateService", "Connection Start");
	            System.out.println("Connection Start");
	            o.writeUTF(id);
	            o.flush();
	            
	            while(true){
	            	l = i.readUTF();
	            	if(l != null){
	            		vib.vibrate(pattern, 5);
	            		nm.notify(5, mNoti);
	            		l = null;
	            	}
	            }
	        }catch(Exception e){
	            e.printStackTrace();
	        }
			return null;
		}
	}
}
