package com.example.coups;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TabFivActivity extends Activity implements View.OnClickListener {

    Button alarm;
    Button couponpresent;
    Button leave;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tabfiv);

        leave = (Button) findViewById(R.id.leave);
        leave.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == leave) {
            new AlertDialog.Builder(this)
                    .setTitle("회원탈퇴")
                    .setMessage("정말로 탈퇴하시겠습니까?")
                    .setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            removePreferences();
                            Intent intent = new Intent(TabFivActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("취소", null).show();
        }
    }

    private void removePreferences() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
