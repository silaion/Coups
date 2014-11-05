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

public class MainActivity extends Activity {
    Button user;
    Button signin;
    Global global;
    static String name, phoneNum, birth, gender = "1", c_Number;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // startActivity(new Intent(this, SplashActivity.class));

        user = (Button)findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, ExistMemberActivity.class);
                startActivity(intent);
            }
        });

        signin = (Button)findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        global = new Global();
        global.actList.add(this);

        getPreferences();
    }

    private void getPreferences(){

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        c_Number = pref.getString("c_Number", "");
        name = pref.getString("Name", "");
        gender = pref.getString("Gender", "");
        phoneNum = pref.getString("PhoneNumber", "");
        birth = pref.getString("Birthday", "");
        Log.d("getPref", c_Number + " " + name + " " + gender + " " + phoneNum + " " + birth);
        global.c_Number = c_Number;
        global.name = name;
        global.gender = gender;
        global.phoneNum = phoneNum;
        global.birth = birth;

        if(!c_Number.equals("")){
            Intent intent = new Intent(MainActivity.this, Tabview.class);
            intent.putExtra("tab", "else");
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(this)
                        .setTitle("프로그램 종료")
                        .setMessage("프로그램을 종료 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                for(int i = 0; i < global.actList.size() ; i++){
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
