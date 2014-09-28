package com.example.coups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CouponclickActivity extends Activity {

	/** Called when the activity is first created. */
	Button discouponbuy;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.couponclick);
	    // TODO Auto-generated method stub
	    discouponbuy = (Button)findViewById(R.id.discouponbuy);
	    discouponbuy.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CouponclickActivity.this, BuyActivity.class);
				startActivity(intent);
			}
		});
	}

}
