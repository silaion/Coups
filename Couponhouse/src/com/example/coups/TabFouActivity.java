package com.example.coups;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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


public class TabFouActivity extends ListActivity {

    ArrayList<HashMap<String, Object>> discoupon;
    LayoutInflater inflater;
    Button uselist;
    Button buy;
    Global global;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discountcoupontab);

        global = new Global();

        uselist = (Button)findViewById(R.id.uselist);
        uselist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(TabFouActivity.this, usediscouponlistActivity.class);
                startActivity(intent);
            }
        });

        buy = (Button)findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(TabFouActivity.this, BuyActivity.class);
                startActivity(intent);
            }
        });

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);


        inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        discoupon=new ArrayList<HashMap<String,Object>>();


        HttpConnect httpConnect = new HttpConnect();
        httpConnect.execute(null, null, null);

        while(true){
            try{
                Thread.sleep(1000);
                if(httpConnect.flag){
                    discoupon = httpConnect.store;
                    break;
                }
            }catch(Exception e){}
        }//while


        CustomAdapter adapter=new CustomAdapter(this, R.layout.tabfou,discoupon);

        //finally,set the adapter to the default ListView
        setListAdapter(adapter);
        // TODO Auto-generated method stub

    }
    private class HttpConnect extends AsyncTask<Void, Void, Void> {
        String url = "http://112.172.217.79:8080/JSP_Server/c_getDiscoup.jsp";
        ArrayList<HashMap<String, Object>> store;
        String tagName = null;
        int eventType;
        boolean flag = false;
        boolean inS_Name = false, inC_Name = false, inPrice = false;

        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, Object> temp = new HashMap<String, Object>();

            try {
                HttpClient httpClient = new DefaultHttpClient();
                httpClient.getParams().setParameter("http.protocol.expect-continue", false);
                httpClient.getParams().setParameter("http.connection.timeout", 5000);
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
                            } else if (tagName.equals("S_name")) {
                                inS_Name = true;
                            } else if (tagName.equals("Coupon_name")) {
                                inC_Name = true;
                            } else if (tagName.equals("Price")) {
                                inPrice = true;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            if (tagName.equals("S_name") && inS_Name) {
                                temp.put("S_name", parser.getText());
                            } else if (tagName.equals("Coupon_name") && inC_Name) {
                                temp.put("Coupon_name", parser.getText());
                            } else if (tagName.equals("Price") && inPrice) {
                                temp.put("Price", parser.getText());
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            tagName = parser.getName();
                            if (tagName.equals("data")) {
                                store.add(temp);
                            } else if (tagName.equals("S_name")) {
                                inS_Name = false;
                            } else if (tagName.equals("Coupon_name")) {
                                inC_Name = false;
                            } else if (tagName.equals("Price")) {
                                inPrice = false;
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

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<HashMap<String, Object>> Strings) {
            //let android do the initializing :)
            super(context, textViewResourceId, Strings);
        }

        //class for caching the views in a row
        private class ViewHolder {
            TextView disc_Name, disc_Price;
        }

        ViewHolder viewHolder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                //inflate the custom layout
                convertView = inflater.inflate(R.layout.tabfou, null);
                viewHolder = new ViewHolder();

                //cache the views
                viewHolder.disc_Name = (TextView) convertView.findViewById(R.id.disc_Name);
                viewHolder.disc_Price = (TextView) convertView.findViewById(R.id.disc_Price);

                //link the cached views to the convertview
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            //set the data to be displayed
            viewHolder.disc_Name.setText(discoupon.get(position).get("S_name").toString() + " " + discoupon.get(position).get("Coupon_name").toString());
            viewHolder.disc_Price.setText(discoupon.get(position).get("Price").toString() + " 원");
            //return the view to be displayed
            return convertView;
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
}



