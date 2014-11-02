package com.example.coups;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
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

public class BuyActivity extends ListActivity {
    ArrayList<HashMap<String, Object>> searchResults;
    ArrayList<HashMap<String, Object>> Dis_Coupon;
    static ArrayList<HashMap<String, Object>> checked_coupon;
    LayoutInflater inflater;
    Button buy;
    Global global = new Global();
    HttpConnect httpConnect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy);

        global = new Global();
        httpConnect = new HttpConnect();

        final EditText searchBox = (EditText) findViewById(R.id.searchBox);

        ListView Dis_CouponListView = (ListView) findViewById(android.R.id.list);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        buy = (Button) findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(BuyActivity.this, BuytwoActivity.class);
                startActivity(intent);
            }
        });
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Dis_Coupon = new ArrayList<HashMap<String, Object>>();

        httpConnect.execute(null, null, null);

        while (true) {
            try {
                Thread.sleep(1000);
                if (httpConnect.flag) {
                    Dis_Coupon = httpConnect.store;
                    break;
                }
            } catch (Exception e) {
            }
        }//while

        searchResults = new ArrayList<HashMap<String, Object>>(Dis_Coupon);
        checked_coupon = new ArrayList<HashMap<String, Object>>();
        final CustomAdapter adapter = new CustomAdapter(this, R.layout.discountsell, searchResults);
        Dis_CouponListView.setAdapter(adapter);
        searchBox.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //get the text in the EditText
                String searchString = searchBox.getText().toString();
                int textLength = searchString.length();

                //clear the initial data set
                searchResults.clear();

                for (int i = 0; i < Dis_Coupon.size(); i++) {
                    String storeName = Dis_Coupon.get(i).get("S_Name").toString();
                    if (textLength <= storeName.length()) {
                        //compare the String in EditText with Names in the ArrayList
                        if (searchString.equalsIgnoreCase(storeName.substring(0, textLength)))
                            searchResults.add(Dis_Coupon.get(i));
                    }
                }

                adapter.notifyDataSetChanged();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    private class HttpConnect extends AsyncTask<Void, Void, Void> {
        String url = "http://112.172.217.79:8080/JSP_Server/c_AllDiscoup.jsp";
        ArrayList<HashMap<String, Object>> store;
        String tagName = null;
        int eventType;
        boolean flag = false;
        boolean inS_Name = false, inC_Name = false, inPrice = false, inState = false, inDisc = false;
        boolean inS_Number = false, inC_Number = false;

        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, Object> temp = new HashMap<String, Object>();

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                List nameValuePairs = new ArrayList(1);
                nameValuePairs.add(new BasicNameValuePair("c_number", global.c_Number));
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
                            } else if (tagName.equals("C_Name")) {
                                inC_Name = true;
                            } else if (tagName.equals("Price")) {
                                inPrice = true;
                            } else if (tagName.equals("State")) {
                                inState = true;
                            } else if (tagName.equals("Disc_Number")) {
                                inDisc = true;
                            } else if(tagName.equals("S_Number")){
                                inS_Number = true;
                            } else if(tagName.equals("C_Number")){
                                inC_Number = true;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            if (tagName.equals("S_Name") && inS_Name) {
                                temp.put("S_Name", parser.getText());
                            } else if (tagName.equals("C_Name") && inC_Name) {
                                temp.put("C_Name", parser.getText());
                            } else if (tagName.equals("Price") && inPrice) {
                                temp.put("Price", parser.getText());
                            } else if (tagName.equals("State") && inState) {
                                temp.put("State", parser.getText());
                            } else if (tagName.equals("Disc_Number") && inDisc) {
                                temp.put("Disc_Number", parser.getText());
                            } else if(tagName.equals("S_Number") && inS_Number){
                                temp.put("S_Number", parser.getText());
                            } else if(tagName.equals("C_Number") && inC_Number){
                                temp.put("C_Number", parser.getText());
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            tagName = parser.getName();
                            if (tagName.equals("data")) {
                                store.add(temp);
                            } else if (tagName.equals("S_Name")) {
                                inS_Name = false;
                            } else if (tagName.equals("C_Name")) {
                                inC_Name = false;
                            } else if (tagName.equals("Price")) {
                                inPrice = false;
                            } else if (tagName.equals("State")) {
                                inState = false;
                            } else if (tagName.equals("Disc_Number")) {
                                inDisc = false;
                            } else if(tagName.equals("S_Number")){
                                inS_Number = false;
                            } else if(tagName.equals("C_Number")){
                                inC_Number = false;
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
    }


    private class CustomAdapter extends ArrayAdapter<HashMap<String, Object>> {

        HashMap<String, Object> checked;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<HashMap<String, Object>> Strings) {
            //let android do the initializing :)
            super(context, textViewResourceId, Strings);

        }

        //class for caching the views in a row
        private class ViewHolder {
            TextView Disc_Name, Disc_Price, Disc_had;
            CheckBox Disc_Check;
        }

        ViewHolder viewHolder;

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                //inflate the custom layout
                convertView = inflater.inflate(R.layout.discountsell, null);
                viewHolder = new ViewHolder();

                //cache the views
                viewHolder.Disc_Name = (TextView) convertView.findViewById(R.id.Disc_Name);
                viewHolder.Disc_Price = (TextView) convertView.findViewById(R.id.Disc_Price);
                viewHolder.Disc_had = (TextView) convertView.findViewById(R.id.Disc_have);
                viewHolder.Disc_Check = (CheckBox) convertView.findViewById(R.id.check);

                //link the cached views to the convertview
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            //set the data to be displayed
            viewHolder.Disc_Name.setText(Dis_Coupon.get(position).get("S_Name").toString() + " " + Dis_Coupon.get(position).get("C_Name").toString());
            viewHolder.Disc_Price.setText(Dis_Coupon.get(position).get("Price").toString() + " 원");
            viewHolder.Disc_had.setText(Dis_Coupon.get(position).get("State").toString());
            if(viewHolder.Disc_Check != null) {
                viewHolder.Disc_Check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            checked = new HashMap<String, Object>();
                            checked.put("Counter", position);
                            checked.put("S_Number", Dis_Coupon.get(position).get("S_Number"));
                            checked.put("S_Name", Dis_Coupon.get(position).get("S_Name"));
                            checked.put("C_Name", Dis_Coupon.get(position).get("C_Name"));
                            checked.put("Price", Dis_Coupon.get(position).get("Price"));
                            checked.put("Disc_Number", Dis_Coupon.get(position).get("Disc_Number"));
                            checked.put("State", Dis_Coupon.get(position).get("State"));
                            checked_coupon.add(checked);
                        } else {
                            for(int i = 0 ; i < checked_coupon.size() ; i++){
                                int count = Integer.parseInt(checked_coupon.get(i).get("Counter").toString());
                                if(count == position) {
                                    checked_coupon.remove(i);
                                    break;
                                }
                            }
                        }
                    }
                });

                for(int i = 0; i < getCount() ; i++){
                    if(Dis_Coupon.get(i) != null){
                        viewHolder.Disc_Check.setChecked(false);
                        break;
                    } else{
                        viewHolder.Disc_Check.setChecked(true);
                    }
                }
            }
            //return the view to be displayed
            return convertView;
        }
    }
}