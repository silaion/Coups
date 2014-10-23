package com.example.coups;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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

public class InfoActivity extends ListActivity {

    //the ArrayList that will hold the data to be displayed in the ListView
    ArrayList<HashMap<String, Object>> searchResults;
    ArrayList<HashMap<String, Object>> store;
    LayoutInflater inflater;
    AdapterThread adapterThread;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infos);
        final EditText searchBox=(EditText) findViewById(R.id.searchBox);

        ListView storeListView=(ListView) findViewById(android.R.id.list);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, show a toast with the TextView text
                Intent intent = new Intent(InfoActivity.this, InfoclickActivity.class);
                startActivity(intent);
            }});

        //not necessary as ListActivity has an
        //implicitly defined Layout(with a ListView of course)
        //setContentView(R.layout.store);

        //get the LayoutInflater for inflating the customomView
        inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        store=new ArrayList<HashMap<String,Object>>();
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

        searchResults=new ArrayList<HashMap<String,Object>>(store);
        final CustomAdatper adapter = new CustomAdatper(this, R.layout.favorite, searchResults);
        storeListView.setAdapter(adapter);
        searchBox.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //get the text in the EditText
                String searchString=searchBox.getText().toString();
                int textLength=searchString.length();

                //clear the initial data set
                searchResults.clear();

                for(int i = 0 ; i < store.size();i++)
                {
                    String storeName = store.get(i).get("Name").toString();
                    if(textLength <= storeName.length()){
                        //compare the String in EditText with Names in the ArrayList
                        if(searchString.equalsIgnoreCase(storeName.substring(0,textLength)))
                            searchResults.add(store.get(i));
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
        private Handler handler;

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
