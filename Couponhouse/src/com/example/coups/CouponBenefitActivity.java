package com.example.coups;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class CouponBenefitActivity extends Activity {

    TextView benefit_name_1, benefit_name_2, benefit_many_1, benefit_many_2;
    Global global;

    AdapterThread adapterThread;
    CouponclickActivity couponclickActivity;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.couponbenefit);
	    // TODO Auto-generated method stub

        global = new Global();

        benefit_many_1 = (TextView) findViewById(R.id.benefit_many_1);
        benefit_many_2 = (TextView) findViewById(R.id.benefit_many_2);
        benefit_name_1 = (TextView) findViewById(R.id.benefit_name_1);
        benefit_name_2 = (TextView) findViewById(R.id.benefit_name_2);

        adapterThread = new AdapterThread();
        adapterThread.execute(null, null, null);


	}

    class AdapterThread extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            benefit_name_1.setText(global.imsi_string1);
            benefit_many_1.setText(global.imsi_string2);
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }
}
