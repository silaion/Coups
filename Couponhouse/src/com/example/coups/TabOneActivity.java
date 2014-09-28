package com.example.coups;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

public class TabOneActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.tabone);
	
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
