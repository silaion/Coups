package com.example.coups;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
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

public class InfoclickActivity extends FragmentActivity {
    private UiLifecycleHelper uiHelper;
    private Button facebook, twit, kakao;
    HashMap<String, Object> temp;

    Global global;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HttpConnect httpConnect = new HttpConnect();
        httpConnect.execute(null, null, null);

        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);
        setContentView(R.layout.infoclick);

        facebook = (Button)findViewById(R.id.facebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(InfoclickActivity.this)
                        .setLink("https://developers.facebook.com/android")
                        .build();
                uiHelper.trackPendingDialogCall(shareDialog.present());
            }
        });

        twit = (Button)findViewById(R.id.twitter);
        twit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent it = new Intent(getBaseContext(), TwitterLoad.class);
                it.putExtra("url", "https://play.google.com/store/apps/details?id=Coups");
                it.putExtra("msg", "Coupon House");
                startActivity(it);
            }
        });

        kakao = (Button)findViewById(R.id.kakao);
        kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = getBaseContext();
                Kakaolink kakaolink = new Kakaolink(getBaseContext());
                kakaolink.sendKakaoTalkLink();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    private class HttpConnect extends AsyncTask<Void, Void, Void> {
        HashMap<String, Object> temp;
        String url = "http://112.172.217.79:8080/JSP_Server/s_info.jsp";
        ArrayList<HashMap<String, Object>> store;
        String tagName = null;
        int eventType;
        boolean flag = false;
        boolean inS_Name = false, inEmail = false, inAddr = false, inContact = false, inWebAddr = false, inStamp = false;

        @Override
        protected Void doInBackground(Void... params) {
            temp = new HashMap<String, Object>();
            try {
                HttpClient httpClient = new DefaultHttpClient();
                httpClient.getParams().setParameter("http.protocol.expect-continue", false);
                httpClient.getParams().setParameter("http.connection.timeout", 5000);
                HttpPost httpPost = new HttpPost(url);
                List nameValuePairs = new ArrayList(1);
                nameValuePairs.add(new BasicNameValuePair("s_number", global.s_Number));
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
                            } else if (tagName.equals("S_Name")) {
                                inS_Name = true;
                            } else if (tagName.equals("S_Email")) {
                                inEmail = true;
                            } else if (tagName.equals("S_Addr")) {
                                inAddr = true;
                            } else if(tagName.equals("S_Contact")){
                                inContact = true;
                            } else if(tagName.equals("S_Webaddr")){
                                inWebAddr = true;
                            } else if(tagName.equals("S_Stamp")){
                                inStamp = true;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            if (tagName.equals("S_Name") && inS_Name) {
                                temp.put("S_Name", parser.getText());
                            } else if (tagName.equals("S_Email") && inEmail) {
                                temp.put("S_Email", parser.getText());
                            } else if (tagName.equals("S_Addr") && inAddr) {
                                temp.put("S_Addr", parser.getText());
                            } else if(tagName.equals("S_Contact") && inContact){
                                temp.put("S_Contact", parser.getText());
                            } else if(tagName.equals("S_Webaddr") && inWebAddr){
                                temp.put("S_Webaddr", parser.getText());
                            } else if(tagName.equals("S_Stamp") && inStamp){
                                temp.put("S_Stamp", parser.getText());
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            tagName = parser.getName();
                            if (tagName.equals("data")) {
                                store.add(temp);
                            }  else if (tagName.equals("S_Name")) {
                                inS_Name = false;
                            } else if (tagName.equals("S_Email")) {
                                inEmail = false;
                            } else if (tagName.equals("S_Addr")) {
                                inAddr = false;
                            } else if(tagName.equals("S_Contact")){
                                inContact = false;
                            } else if(tagName.equals("S_Webaddr")){
                                inWebAddr = false;
                            } else if(tagName.equals("S_Stamp")){
                                inStamp = false;
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
        protected void onPostExecute(Void params){
            TextView S_Name = (TextView)findViewById(R.id.textView2);
            S_Name.setText(temp.get("S_Name").toString());
            TextView S_Addr = (TextView)findViewById(R.id.textView3);
            S_Addr.setText(temp.get("S_Addr").toString());
            TextView S_Text = (TextView)findViewById(R.id.textView4);
            S_Text.setText("전화번호 : " + temp.get("S_Contact").toString() +
                    "\n홈페이지 : " + temp.get("S_Webaddr").toString());
        }

    }
}