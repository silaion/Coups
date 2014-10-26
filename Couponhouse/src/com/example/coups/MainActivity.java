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
    Sign_up_in sign;
    static String name, phoneNum, birth, gender = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        getPreferences();
        Log.d("pref", name + " " + gender + " " + birth + " " + phoneNum);

        global = new Global();

        if(!name.equals("") && !gender.equals("") && !birth.equals("") && !phoneNum.equals("")){
            global.name = name;
            global.gender = gender;
            global.birth = birth;
            global.phoneNum = phoneNum;

//            sign = new Sign_up_in(this, name, gender, phoneNum, birth);
//            sign.loginProcess();
//            while(true){
//                if(global.start){
//                    Intent intent = new Intent(MainActivity.this, Tabview.class);
//                    startActivity(intent);
//                }
//            }//while

            Intent intent = new Intent(MainActivity.this, Tabview.class);
            startActivity(intent);
        }
    }

    private void getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        global.c_Number = pref.getString("c_Number", "");
        name = pref.getString("Name", "");
        gender = pref.getString("Gender", "");
        phoneNum = pref.getString("PhoneNumber", "");
        birth = pref.getString("Birthday", "");
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
