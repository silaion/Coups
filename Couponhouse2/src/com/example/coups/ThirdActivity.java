package com.example.coups;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gcm.GCMRegistrar;

public class ThirdActivity extends Activity {

	Button sign;
	Button before1;
	static Context mContext;
	public static String PROJECT_ID = "840576167931";
	
	Sign_up_in sign_ui;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.third);
	    
	    mContext = this;
	
	    sign = (Button)findViewById(R.id.sign);
	    sign.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				GCMRegistrar.checkDevice(mContext);
				GCMRegistrar.checkManifest(mContext);
				if (GCMRegistrar.getRegistrationId(mContext).equals("")) {
					GCMRegistrar.register(mContext, PROJECT_ID);

				} else {
					// 이미 GCM 을 상요하기위해 등록ID를 구해왔음
					GCMRegistrar.unregister(mContext);
					GCMRegistrar.register(mContext, PROJECT_ID);
				}
				
				Intent intent = new Intent(ThirdActivity.this, Tabview.class);
				startActivity(intent);
			}
		});
		
	    before1 = (Button)findViewById(R.id.before1);
	    before1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ThirdActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
	}

}
