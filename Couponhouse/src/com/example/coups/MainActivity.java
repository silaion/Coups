package com.example.coups;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
		Log.d("pref", name + " " + gender + " " + birth + " " + phoneNum);
//		sign = new Sign_up_in(this, name, gender, phoneNum, birth);
//		sign.loginProcess();
		
	}
	
	private void getPreferences(){
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		name = pref.getString("Name", "");
		gender = pref.getString("Gender", "");
		phoneNum = pref.getString("PhoneNumber", "");
		birth = pref.getString("Birthday", "");
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
       switch(keyCode) {
         case KeyEvent.KEYCODE_BACK:
        	 Toast.makeText(this, "�ڷΰ����ư ����", Toast.LENGTH_SHORT).show();
           new AlertDialog.Builder(this)
           .setTitle("���α׷� ����")
           .setMessage("���α׷��� ���� �Ͻðڽ��ϱ�?")
           .setPositiveButton("��", new DialogInterface.OnClickListener() {
        	   @Override
        	   public void onClick(DialogInterface dialog, int whichButton) {
                             finish();
                           }
                         })
                         .setNegativeButton("�ƴϿ�", null)
                         .show();
                         break;
         default:
           break;
      }
       return super.onKeyDown(keyCode, event);
	}
}
