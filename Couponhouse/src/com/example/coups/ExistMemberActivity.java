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

public class ExistMemberActivity extends Activity {

    Button register;
    Button before;
    EditText et_name, et_phoneNum, et_birth, et_change;
    RadioGroup rGroup;
    RadioButton rb_male, rb_female;

    static Context mContext;

    Global global;

    String name, gender = "남", phoneNum, birth, change;

    public static String PROJECT_ID = "840576167931";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existmember);

        global = new Global();
        global.actList.add(this);
        global.change = "1";


        et_name = (EditText)findViewById(R.id.editText4);
        et_birth = (EditText)findViewById(R.id.editText1);
        et_phoneNum = (EditText)findViewById(R.id.editText2);
        et_change = (EditText)findViewById(R.id.editText3);

        rGroup = (RadioGroup)findViewById(R.id.radioGroup);
        rb_male = (RadioButton)findViewById(R.id.rd1);
        rb_female= (RadioButton)findViewById(R.id.rd2);

        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch(checkedId){
                    case R.id.rd1:
                        gender = "남";
                    case R.id.rd2:
                        gender = "여";
                }
            }
        });

        mContext = this;
        register = (Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString();
                birth = et_birth.getText().toString();
                phoneNum = et_phoneNum.getText().toString();
//                change = et_change.getText().toString();
                Log.d("SharedPreference", name + " " + gender + " " + birth + " " + phoneNum);

                global.name = name;
                global.gender = gender;
                global.birth = birth;
                global.phoneNum = phoneNum;
                global.change = change;

//                GCMRegistrar.checkDevice(mContext);
//                GCMRegistrar.checkManifest(mContext);
//                if (GCMRegistrar.getRegistrationId(mContext).equals("")) {
//                    GCMRegistrar.register(mContext, PROJECT_ID);
//                } else {
//                    // 이미 GCM 을 사용하기위해 등록ID를 구해왔음
//                    GCMRegistrar.unregister(mContext);
//                    GCMRegistrar.register(mContext, PROJECT_ID);
//                }
                savePreferences(global.c_Number);
                global.c_Number = "1";

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

    private void savePreferences(String c_Number){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Name", global.name);
        editor.putString("Gender", global.gender);
        editor.putString("PhoneNumber", global.phoneNum);
        editor.putString("Birthday", global.birth);
        editor.putString("c_Number", c_Number);
        editor.commit();
    }
}
