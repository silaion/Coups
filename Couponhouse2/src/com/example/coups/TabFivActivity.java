package com.example.coups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

}
