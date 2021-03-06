package com.example.coups;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.google.android.gcm.GCMRegistrar;

public class SignUpActivity extends Activity {

    Button sign;
    Button sign_before;
    EditText sign_name, sign_phoneNum, sign_birth;
    RadioGroup sign_rGroup;
    RadioButton sign_rb_male, sign_rb_female;

    public static String PROJECT_ID = "840576167931";

    static Context mContext;
    String name, gender = "남", phoneNum, birth;

    Global global;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        sign_name = (EditText) findViewById(R.id.sign_et_name);
        sign_birth = (EditText) findViewById(R.id.sign_et_birth);
        sign_phoneNum = (EditText) findViewById(R.id.sign_et_phoneNum);

        sign_rGroup = (RadioGroup) findViewById(R.id.sign_radioGroup);
        sign_rb_female = (RadioButton) findViewById(R.id.sign_rb_female);
        sign_rb_male = (RadioButton) findViewById(R.id.sign_rb_male);

        sign_rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.sign_rb_male:
                        gender = "남";
                    case R.id.sign_rb_female:
                        gender = "여";
                }
            }
        });

        mContext = this;
        global = new Global();
        global.actList.add(this);
        global.change = "1";

        sign = (Button) findViewById(R.id.sign);
        sign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                GCMRegistrar.checkDevice(mContext);
                GCMRegistrar.checkManifest(mContext);
                if (GCMRegistrar.getRegistrationId(mContext).equals("")) {
                    GCMRegistrar.register(mContext, PROJECT_ID);

                } else {
                    // 이미 GCM 을 상요하기위해 등록ID를 구해왔음
                    GCMRegistrar.unregister(mContext);
                    GCMRegistrar.register(mContext, PROJECT_ID);
                }

                name = sign_name.getText().toString();
                birth = sign_birth.getText().toString();
                phoneNum = sign_phoneNum.getText().toString();
                global.name = name;
                global.birth = birth;
                global.phoneNum = phoneNum;
                global.gender = gender;

                //savePreferences("1");

//                Intent intent = new Intent(SignUpActivity.this, Tabview.class);
//                startActivity(intent);
            }
        });

        sign_before = (Button) findViewById(R.id.sign_before);
        sign_before.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
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
