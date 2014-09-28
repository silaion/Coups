package com.example.coups;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

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

    public IBinder onBind(Intent intent){
        return null;
    }

    public void onStart(Intent intent, String id){
        vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        try{
            s = new Socket("10.102.11.122", 9830);
            i = new DataInputStream(new BufferedInputStream(s.getInputStream()));
            o = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));;
            Log.d("c", "Connection Start");
            o.writeUTF(id);
            o.flush();

            while(true){
            	l = i.readUTF();
            	if(l != null){
            		vib.vibrate(pattern, 5);
            		l = null;
            	}
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
