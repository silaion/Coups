package com.example.coups;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class TabOneActivity extends Activity {
    Global global;
    TextView c_Number;
    AdapterThread adapterThread;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tabone);
	    // TODO Auto-generated method stub
        global = new Global();

        c_Number = (TextView) findViewById(R.id.tab_c_Number);
        adapterThread = new AdapterThread();
        adapterThread.execute(null, null, null);
	}

    private class AdapterThread extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            c_Number.setText("회원번호 : " + global.c_Number);
            return null;
        }
    }
}
