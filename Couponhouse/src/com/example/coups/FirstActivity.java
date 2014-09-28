package com.example.coups;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gcm.GCMRegistrar;

public class FirstActivity extends Activity {

	Button register;
	Button before;
	EditText et_name, et_phoneNum, et_birth;
	static Context mContext;
	
	Sign_up_in signui;
	
	String name, gender, phoneNum, birth;
	
	public static String PROJECT_ID = "840576167931";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.firstview);
	    
	    EditText et_name = (EditText)findViewById(R.id.editText4);
	    EditText et_birth = (EditText)findViewById(R.id.editText1);
	    EditText et_phoneNum = (EditText)findViewById(R.id.editText2);
	    
	
	    mContext = this;
	    register = (Button)findViewById(R.id.register);
	    register.setOnClickListener(new View.OnClickListener() {			
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
				
				Intent intent = new Intent(FirstActivity.this, Tabview.class);
				startActivity(intent);
				
				//savePreferences();
//				signui = new Sign_up_in(mContext, name, gender, phoneNum, birth);
//				signui.insertProcess();
//				signui.loginProcess();
				
			}
		});
		
		before = (Button)findViewById(R.id.before);
		before.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FirstActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private void savePreferences(){
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("Name", et_name.getText().toString());
		editor.putString("Gender", gender);
		editor.putString("PhoneNumber", et_phoneNum.getText().toString());
		editor.putString("Birthday", et_birth.getText().toString());
	}
	
	private void getPreferences(){
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		name = pref.getString("Name", "");
		//gender = pref.getString("Gender", "");
		phoneNum = pref.getString("PhoneNumber", "");
		birth = pref.getString("Birthday", "");
		
	}
	
	

}
