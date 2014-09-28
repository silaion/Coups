package com.example.coups;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	Button user;
	Button signin;
	
	Sign_up_in sign;
	
	static String name, phoneNum, birth, gender = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		user = (Button)findViewById(R.id.user);
		user.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, FirstActivity.class);
				startActivity(intent);
			}
		});
		
		signin = (Button)findViewById(R.id.signin);
		signin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
				startActivity(intent);
			}
		});
		
		getPreferences();
		sign = new Sign_up_in(this, name, gender, phoneNum, birth);
		sign.loginProcess();
		
	}
	
	private void getPreferences(){
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		name = pref.getString("Name", "");
		//gender = pref.getString("Gender", "");
		phoneNum = pref.getString("PhoneNumber", "");
		birth = pref.getString("Birthday", "");
		
	}
}
