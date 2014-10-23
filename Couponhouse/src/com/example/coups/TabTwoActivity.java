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
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class TabTwoActivity extends ListActivity {

    Button shoplist;
    Button maps;
    static ArrayList<HashMap<String, Object>> store;
    LayoutInflater inflater;
    AdapterThread adapterThread;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tabtwo);

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, show a toast with the TextView text
                Intent intent = new Intent(TabTwoActivity.this, InfoclickActivity.class);
                startActivity(intent);
            }
        });


        shoplist = (Button) findViewById(R.id.shoplist);
        shoplist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(TabTwoActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        maps = (Button) findViewById(R.id.maps);
        maps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(TabTwoActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //these arrays are just the data that 
        //I'll be using to populate the ArrayList
        //You can use our own methods to get the data
        store = new ArrayList<HashMap<String, Object>>();
        adapterThread = new AdapterThread();
        adapterThread.execute(null, null, null);

        while(true){
            try{
                Thread.sleep(1000);
                if(adapterThread.flag){
                    store = adapterThread.store;
                    break;
                }
            }catch(Exception e){}
        }//while

        CustomAdatper adapter = new CustomAdatper(this, R.layout.favorite, store);
        lv.setAdapter(adapter);

        // TODO Auto-generated method stub
    }

    private class CustomAdatper extends ArrayAdapter<HashMap<String, Object>> {
        public CustomAdatper(Context context, int resId, ArrayList<HashMap<String, Object>> store) {
            super(context, resId, store);
        }

        private class ViewHolder {
            TextView name, addr;
        }

        ViewHolder viewHolder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.favorite, null);
                viewHolder = new ViewHolder();

                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.addr = (TextView) convertView.findViewById(R.id.addr);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.name.setText(store.get(position).get("Name").toString());
            viewHolder.addr.setText(store.get(position).get("Addr").toString());

            return convertView;
        }
    }

    class AdapterThread extends AsyncTask<Void, Void, Void> {
        ArrayList<HashMap<String, Object>> store;
        HashMap<String, Object> temp = new HashMap<String, Object>();

        String mAddr = "http://112.172.217.79:8080/JSP_Server/xmlout.jsp";
        //String mAddr = "http://172.30.76.31:8081/gcm_jsp/xmlout.jsp";
        String tagName;
        int eventType;
        boolean flag = false;
        boolean inName = false, inAddr = false;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL targetURL = new URL(mAddr);
                InputStream is = targetURL.openStream();

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
                            } else if (tagName.equals("Name")) {
                                inName = true;
                            } else if (tagName.equals("Addr")) {
                                inAddr = true;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            if (tagName.equals("Name") && inName) {
                                temp.put("Name", parser.getText());
                            } else if (tagName.equals("Addr") && inAddr) {
                                temp.put("Addr", parser.getText());
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            tagName = parser.getName();
                            if (tagName.equals("data")) {
                                store.add(temp);
                            } else if (tagName.equals("Name")) {
                                inName = false;
                            } else if (tagName.equals("Addr")) {
                                inAddr = false;
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
