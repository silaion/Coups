package com.example.coups;


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

public class BuytwoActivity extends ListActivity {

    Button payment;
    ArrayList<HashMap<String, Object>> buy_coupon;
    LayoutInflater inflater;
    Global global;
    BuyActivity buy;
    ArrayList<String> S_Numbers;
    ArrayList<String> Disc_Numbers;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.discount);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        global = new Global();
        buy = new BuyActivity();

        payment = (Button)findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(BuytwoActivity.this, PaymentActivity.class);
                startActivity(intent);
                HttpConnect httpConnect = new HttpConnect();
                httpConnect.execute(null, null, null);
            }
        });

        inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        buy_coupon = new ArrayList<HashMap<String, Object>>();
        buy_coupon = buy.checked_coupon;

        Disc_Numbers = new ArrayList<String>();

        for(int i = 0 ; i < buy_coupon.size() ; i++){
            if(buy_coupon.get(i) != null){
                Disc_Numbers.add(buy_coupon.get(i).get("Disc_Number").toString());
            }
        }


        CustomAdapter adapter = new CustomAdapter(this, R.layout.buylist,buy_coupon);

        //finally,set the adapter to the default ListView
        setListAdapter(adapter);
        // TODO Auto-generated method stub
    }

    class CustomAdapter extends ArrayAdapter<HashMap<String, Object>>
    {

        public CustomAdapter(Context context, int textViewResourceId,
                             ArrayList<HashMap<String, Object>> Strings) {

            //let android do the initializing :)
            super(context, textViewResourceId, Strings);
        }


        //class for caching the views in a row
        private class ViewHolder
        {
            TextView BuyList_Name,BuyList_Price, BuyList_have;

        }

        ViewHolder viewHolder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView==null)
            {
                //inflate the custom layout
                convertView=inflater.inflate(R.layout.buylist, null);
                viewHolder=new ViewHolder();

                //cache the views
                viewHolder.BuyList_Name = (TextView) convertView.findViewById(R.id.BuyList_Name);
                viewHolder.BuyList_Price = (TextView) convertView.findViewById(R.id.BuyList_Price);
                viewHolder.BuyList_have = (TextView) convertView.findViewById(R.id.BuyList_have);

                //link the cached views to the convertview
                convertView.setTag(viewHolder);

            }
            else
                viewHolder=(ViewHolder) convertView.getTag();

            //set the data to be displayed
            viewHolder.BuyList_Name.setText(buy_coupon.get(position).get("S_Name").toString() + " " + buy_coupon.get(position).get("C_Name").toString());
            viewHolder.BuyList_Price.setText(buy_coupon.get(position).get("Price").toString() + " 원");
            viewHolder.BuyList_have.setText(buy_coupon.get(position).get("State").toString());
            //return the view to be displayed
            return convertView;
        }
    }// TODO Auto-generated method stub

    private class HttpConnect extends AsyncTask<Void, Void, Void> {
        String url = "http://112.172.217.79:8080/JSP_Server/disc_perchase.jsp";
        ArrayList<HashMap<String, Object>> store;
        String tagName = null;
        int eventType;
        boolean flag = false;
        boolean inS_Name = false, inDisc_Name = false, inPrice = false, inState = false, inDisc = false;
        boolean inS_Number = false, inC_Number = false;

        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, Object> temp = new HashMap<String, Object>();

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                List nameValuePairs = new ArrayList();
                nameValuePairs.add(new BasicNameValuePair("c_number", global.c_Number));
                for(int i = 0; i < S_Numbers.size(); i++){
                    nameValuePairs.add(new BasicNameValuePair("Disc_Number", Disc_Numbers.get(i)));
                }
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
                            } else if (tagName.equals("s_name")) {
                                inS_Name = true;
                            } else if (tagName.equals("disc_name")) {
                                inDisc_Name = true;
                            } else if (tagName.equals("disc_price")) {
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
                            if (tagName.equals("s_name") && inS_Name) {
                                temp.put("S_Name", parser.getText());
                            } else if (tagName.equals("disc_name") && inDisc_Name) {
                                temp.put("C_Name", parser.getText());
                            } else if (tagName.equals("disc_price") && inPrice) {
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
                            } else if (tagName.equals("disc_name")) {
                                inDisc_Name = false;
                            } else if (tagName.equals("disc_price")) {
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

}
