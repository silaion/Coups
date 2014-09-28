package com.example.coups;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class TabOneActivity extends Activity {
	
	Thread vib;
	static Intent i;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.tabone);
	    // TODO Auto-generated method stub
//	    Intent i = new Intent(getApplicationContext(), VibrateService.class);
//		i.putExtra("phone", "1");
//		startService(i);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}
}
