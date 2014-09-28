package com.example.coups;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TabFivActivity extends Activity {

	Button alarm;
	Button couponpresent;
	Button leave;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.tabfiv);

	    alarm = (Button)findViewById(R.id.alarm);
	    alarm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TabFivActivity.this, AlarmActivity.class);
				startActivity(intent);
			}
		});
		
	    couponpresent = (Button)findViewById(R.id.couponpresent);
	    couponpresent.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TabFivActivity.this, PresentActivity.class);
				startActivity(intent);
			}
		});
		
	    leave = (Button)findViewById(R.id.leave);
	    leave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TabFivActivity.this, LeaveActivity.class);
				startActivity(intent);
			}
		});
	
	    // TODO Auto-generated method stub
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
       switch(keyCode) {
         case KeyEvent.KEYCODE_BACK:
        	 Toast.makeText(this, "뒤로가기버튼 눌림", Toast.LENGTH_SHORT).show();
           new AlertDialog.Builder(this)
           .setTitle("프로그램 종료")
           .setMessage("프로그램을 종료 하시겠습니까?")
           .setPositiveButton("예", new DialogInterface.OnClickListener() {
        	   @Override
        	   public void onClick(DialogInterface dialog, int whichButton) {
                             finish();
                           }
                         })
                         .setNegativeButton("아니오", null)
                         .show();
                         break;
         default:
           break;
      }
       return super.onKeyDown(keyCode, event);
	}

}
