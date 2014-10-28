package com.example.coups;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private class HttpConnect extends AsyncTask<Void, Void, Void> {

        XmlPullParserFactory factory;
        XmlPullParser parser;

        String url = "http://112.172.217.79:8080/JSP_Server/user_login.jsp";
        String tagName;
        HashMap<String, Object> temp;
        int eventType;
        boolean inResult = false;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                List nameValuePairs = new ArrayList(3);
                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("contact", phoneNum));
                nameValuePairs.add(new BasicNameValuePair("birthday", birth));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                // HTTP Post 요청 실행
                HttpResponse response = httpClient.execute(httpPost);
                InputStream is = response.getEntity().getContent();

                factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                parser = factory.newPullParser();

                parser.setInput(is, null);

                eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) { //최초 title테그안에 쓸데없는 내용이 있어서 추가해줬음.
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            tagName = parser.getName();
                            Log.d("tagName", tagName);
                            if (tagName.equals("data")) {
                                temp = new HashMap<String, Object>();
                            } else if (tagName.equals("Result")) {
                                inResult = true;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            if (tagName.equals("Result") && inResult) {
                                temp.put("Result", parser.getText());
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            tagName = parser.getName();
                            if (tagName.equals("data")) {
                                //store.add(temp);
                            } else if (tagName.equals("Result")) {
                                inResult = false;
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param){
            try {
                if (temp.get("Result").equals("success")) {
                    Intent intent = new Intent(MainActivity.this, Tabview.class);
                    startActivity(intent);
                } else Log.d("Login Fail", global.c_Number + " " + name + " " + gender + " " + birth + " " + phoneNum);
            }catch(Exception e){}
        }
    }
}
