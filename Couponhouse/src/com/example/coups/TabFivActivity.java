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

public class TabFivActivity extends Activity implements View.OnClickListener {
    Button leave;
    Global global;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tabfiv);
        global = new Global();

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
                            HttpConnect httpConnect = new HttpConnect();
                            httpConnect.execute(null, null, null);
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
        Log.d("removePref", "성공적인 삭제");
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

    class HttpConnect extends AsyncTask<Void, Void, Void> {
        String url = "http://112.172.217.79:8080/JSP_Server/user_delete.jsp";
        ArrayList<HashMap<String, Object>> store;
        HashMap<String, Object> temp;
        String tagName = null;
        int eventType;
        boolean flag = false;
        boolean inResult = false;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                List nameValuePairs = new ArrayList(1);
                nameValuePairs.add(new BasicNameValuePair("c_number", global.c_Number));
                Log.d("global.c_Number", global.c_Number);
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                // HTTP Post 요청 실행
                HttpResponse response = httpClient.execute(httpPost);
                InputStream is = response.getEntity().getContent();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser parser = factory.newPullParser();

                parser.setInput(is, null);

                store = new ArrayList<HashMap<String, Object>>();

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
                                store.add(temp);
                            } else if (tagName.equals("Result")) {
                                inResult = false;
                            }
                            break;
                    }
                    eventType = parser.next();
                }
                flag = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param){
            try {
                if (temp.get("Result").equals("success")) {
                    removePreferences();
                    Intent intent = new Intent(TabFivActivity.this, MainActivity.class);
                    startActivity(intent);
                } else return;
            }catch(Exception e){}

        }
    }


}
