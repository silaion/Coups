package com.example.coups;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gcm.GCMRegistrar;

public class ExistMemberActivity extends Activity {

    Button register;
    Button before;
    EditText et_name, et_phoneNum, et_birth;
    RadioGroup rGroup;
    RadioButton rb_male, rb_female;

    static Context mContext;

    Global global;

    Sign_up_in signui;

    String name, gender = "1", phoneNum, birth;

    public static String PROJECT_ID = "840576167931";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existmember);

        global = new Global();

        et_name = (EditText)findViewById(R.id.editText4);
        et_birth = (EditText)findViewById(R.id.editText1);
        et_phoneNum = (EditText)findViewById(R.id.editText2);

        rGroup = (RadioGroup)findViewById(R.id.radioGroup);
        rb_male = (RadioButton)findViewById(R.id.rd1);
        rb_female= (RadioButton)findViewById(R.id.rd2);

        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch(checkedId){
                    case R.id.rd1:
                        gender = "1";
                    case R.id.rd2:
                        gender = "2";
                }
            }
        });

        mContext = this;
        register = (Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                GCMRegistrar.checkDevice(mContext);
                GCMRegistrar.checkManifest(mContext);
                if (GCMRegistrar.getRegistrationId(mContext).equals("")) {
                    GCMRegistrar.register(mContext, PROJECT_ID);
                } else {
                    // 이미 GCM 을 사용하기위해 등록ID를 구해왔음
                    GCMRegistrar.unregister(mContext);
                    GCMRegistrar.register(mContext, PROJECT_ID);
                }

                name = et_name.getText().toString();
                birth = et_birth.getText().toString();
                phoneNum = et_phoneNum.getText().toString();
                savePreferences();
                Log.d("SharedPreference", name + " " + gender + " " + birth + " " + phoneNum);

//                while(true){
//                    if(global.start) {
//                        Intent intent = new Intent(ExistMemberActivity.this, Tabview.class);
//                        startActivity(intent);
//                    }
//                }//while
                Intent intent = new Intent(ExistMemberActivity.this, Tabview.class);
                startActivity(intent);
            }
        });

        before = (Button)findViewById(R.id.before);
        before.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ExistMemberActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void savePreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Name", name);
        editor.putString("Gender", gender);
        editor.putString("PhoneNumber", phoneNum);
        editor.putString("Birthday", birth);
        global.name = name;
        global.gender = gender;
        global.phoneNum = phoneNum;
        global.birth = birth;
        editor.commit();
    }
}
