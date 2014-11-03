package com.example.coups;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TabOneActivity extends Activity {
    Global global;
    TextView c_Number;
    AdapterThread adapterThread;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabone);
        c_Number = (TextView) findViewById(R.id.tab_c_Number);

        // TODO Auto-generated method stub
        global = new Global();
        global.actList.add(this);


        adapterThread = new AdapterThread();
        adapterThread.execute(null, null, null);
    }

    private class AdapterThread extends AsyncTask<Void, Void, Void> {
        ProgressBar progressBar;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(TabOneActivity.this);
            progressDialog.setMessage("잠시만 기다려주세요");
            progressDialog.show();
            progressBar = new ProgressBar(TabOneActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            while(true) {
                progressBar.setVisibility(View.VISIBLE);
                if (!global.c_Number.equals("")) {
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(Void params){
            progressBar.setVisibility(View.INVISIBLE);
            progressDialog.cancel();
            c_Number.setText("회원번호 : " + global.c_Number);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(this)
                        .setTitle("프로그램 종료")
                        .setMessage("프로그램을 종료 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                for (int i = 0; i < global.actList.size(); i++) {
                                    global.actList.get(i).finish();
                                }
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
