package com.example.coups;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class CouponclickActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    TextView s_Name, s_Addr, s_Due_date, c_Number;
    LayoutInflater inflater;
    ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9, iv10;
    Button discouponbuy;
    Button benefit;
    HashMap<String, String> temp;

    Global global;
    String s_Stamp, total, c_number;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.couponclick);
        global = new Global();

        s_Name = (TextView) findViewById(R.id.s_Name);
        s_Addr = (TextView) findViewById(R.id.s_Addr);
        s_Due_date = (TextView) findViewById(R.id.s_Due_date);
        c_Number = (TextView) findViewById(R.id.c_Number);


        AdapterThread adapterThread = new AdapterThread();
        adapterThread.execute(null, null, null);


        discouponbuy = (Button) findViewById(R.id.discouponbuy);
        discouponbuy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CouponclickActivity.this, BuyActivity.class);
                startActivity(intent);
            }
        });

        benefit = (Button) findViewById(R.id.benefit);
        benefit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                global.imsi_string1 = s_Stamp;
                global.imsi_string2 = total;
                Intent intent = new Intent(CouponclickActivity.this, CouponBenefitActivity.class);
                startActivity(intent);
            }
        });

        iv1 = (ImageView) findViewById(R.id.ImageView01);
        iv2 = (ImageView) findViewById(R.id.ImageView02);
        iv3 = (ImageView) findViewById(R.id.ImageView03);
        iv4 = (ImageView) findViewById(R.id.ImageView04);
        iv5 = (ImageView) findViewById(R.id.ImageView05);
        iv6 = (ImageView) findViewById(R.id.ImageView06);
        iv7 = (ImageView) findViewById(R.id.ImageView07);
        iv8 = (ImageView) findViewById(R.id.ImageView08);
        iv9 = (ImageView) findViewById(R.id.ImageView09);
        iv10 = (ImageView) findViewById(R.id.ImageView10);
    }

    class AdapterThread extends AsyncTask<Void, Void, Void> {

        XmlPullParserFactory factory;
        XmlPullParser parser;

        String url = "http://112.172.217.79:8080/JSP_Server/c_store.jsp";
        String tagName;
        int eventType;
        boolean inName = false, inAddr = false, inDue_date = false, inCurrent = false, ins_Stamp = false, inTotal = false;

        ProgressBar progressBar;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(CouponclickActivity.this);
            progressDialog.setMessage("잠시만 기다려주세요");
            progressDialog.show();
            progressBar = new ProgressBar(CouponclickActivity.this);
            if(global.c_Number == null){
                getPreferences();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            progressBar.setVisibility(View.VISIBLE);
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                List nameValuePairs = new ArrayList();
                nameValuePairs.add(new BasicNameValuePair("c_number", global.c_Number));
                nameValuePairs.add(new BasicNameValuePair("s_number", global.s_Number));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                // HTTP Post 요청 실행
                HttpResponse response = httpClient.execute(httpPost);
                InputStream is = response.getEntity().getContent();

                factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                parser = factory.newPullParser();

                parser.setInput(is, null);

                //store = new ArrayList<HashMap<String, Object>>();

                eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) { //최초 title테그안에 쓸데없는 내용이 있어서 추가해줬음.
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            tagName = parser.getName();
                            Log.d("tagName", tagName);
                            if (tagName.equals("data")) {
                                temp = new HashMap<String, String>();
                            } else if (tagName.equals("Name")) {
                                inName = true;
                            } else if (tagName.equals("Addr")) {
                                inAddr = true;
                            } else if (tagName.equals("Due_Date")) {
                                inDue_date = true;
                            } else if (tagName.equals("Current")) {
                                inCurrent = true;
                            } else if (tagName.equals("S_Stamp")){
                                ins_Stamp = true;
                            } else if (tagName.equals("Total")){
                                inTotal = true;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            if (tagName.equals("Name") && inName) {
                                temp.put("Name", parser.getText());
                            } else if (tagName.equals("Addr") && inAddr) {
                                temp.put("Addr", parser.getText());
                            } else if (tagName.equals("Due_Date") && inDue_date) {
                                temp.put("Due_Date", parser.getText());
                            } else if (tagName.equals("Current") && inCurrent) {
                                temp.put("Current", parser.getText());
                            } else if (tagName.equals("S_Stamp") && ins_Stamp){
                                temp.put("S_Stamp", parser.getText());
                            } else if (tagName.equals("Total") && inTotal){
                                temp.put("Total", parser.getText());
                                Log.d("Total", parser.getText());
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            tagName = parser.getName();
                            if (tagName.equals("data")) {
                                //store.add(temp);
                            } else if (tagName.equals("Name")) {
                                inName = false;
                            } else if (tagName.equals("Addr")) {
                                inAddr = false;
                            } else if (tagName.equals("Due_Date")) {
                                inDue_date = false;
                            } else if (tagName.equals("Current")) {
                                inCurrent = false;
                            } else if (tagName.equals("S_Stamp")){
                                ins_Stamp = false;
                            } else if (tagName.equals("Total")){
                                inTotal = false;
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
        protected void onPostExecute(Void params) {
            progressBar.setVisibility(View.INVISIBLE);
            progressDialog.cancel();
            try {
                c_Number.setText("회원번호 : " + String.valueOf(global.c_Number));
                s_Name.setText(temp.get("Name").toString());
                s_Addr.setText(temp.get("Addr").toString());
                s_Due_date.setText(temp.get("Due_Date").toString());
                s_Stamp = temp.get("S_Stamp").toString();
                total = temp.get("Total").toString();

                switch (Integer.parseInt(temp.get("Current").toString())) {
                    case 10:
                        iv10.setVisibility(View.VISIBLE);
                    case 9:
                        iv9.setVisibility(View.VISIBLE);
                    case 8:
                        iv8.setVisibility(View.VISIBLE);
                    case 7:
                        iv7.setVisibility(View.VISIBLE);
                    case 6:
                        iv6.setVisibility(View.VISIBLE);
                    case 5:
                        iv5.setVisibility(View.VISIBLE);
                    case 4:
                        iv4.setVisibility(View.VISIBLE);
                    case 3:
                        iv3.setVisibility(View.VISIBLE);
                    case 2:
                        iv2.setVisibility(View.VISIBLE);
                    case 1:
                        iv1.setVisibility(View.VISIBLE);
                        break;
                }
            }catch(Exception e){}
        }
    }

    private void getPreferences() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        c_number = pref.getString("c_Number", "");
        global.c_Number = c_number;
    }
}
