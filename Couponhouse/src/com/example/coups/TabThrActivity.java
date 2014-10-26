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
import android.widget.AdapterView.OnItemClickListener;
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

public class TabThrActivity extends ListActivity {

    ArrayList<HashMap<String, Object>> searchResults;
    ArrayList<HashMap<String, Object>> store;
    LayoutInflater inflater;
    Button adjust;
    Global global;
    HttpConnect httpConnect;
    /** Called when the activity is first created. */
    @Override


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adjust);

        final EditText searchBox=(EditText) findViewById(R.id.searchBox);
        ListView stampListView=(ListView) findViewById(android.R.id.list);

        //global = (Global)getApplicationContext();
        global = new Global();

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, show a toast with the TextView text

                global.s_Number = store.get(position).get("Number").toString();
                Intent intent = new Intent(TabThrActivity.this, CouponclickActivity.class);
                startActivity(intent);
            }});

        adjust = (Button)findViewById(R.id.adjust);
        adjust.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(TabThrActivity.this, CoDelActivity.class);
                startActivity(intent);
            }
        });

        inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        store=new ArrayList<HashMap<String,Object>>();
        httpConnect = new HttpConnect();
        httpConnect.execute(null, null, null);

        while(true){
            try{
                Thread.sleep(1000);
                if(httpConnect.flag){
                    store = httpConnect.store;
                    break;
                }
            }catch(Exception e){}
        }//while

        searchResults=new ArrayList<HashMap<String,Object>>(store);
        final CustomAdapter adapter = new CustomAdapter(this, R.layout.favorite, searchResults);
        stampListView.setAdapter(adapter);
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

            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }


            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    class HttpConnect extends AsyncTask<Void, Void, Void>{
        String url = "http://112.172.217.79:8080/JSP_Server/c_Allstamps.jsp";
        ArrayList<HashMap<String, Object>> store;
        String tagName = null;
        int eventType;
        boolean flag = false;
        boolean inName = false, inTotal = false, inCurrent = false, inDue_date = false, inNumber = false;
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
//
//                URL targetURL = new URL(url);
//                InputStream is = targetURL.openStream();

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
                            } else if (tagName.equals("Total")) {
                                inTotal = true;
                            } else if(tagName.equals("Current")){
                                inCurrent = true;
                            } else if(tagName.equals("Due_date")){
                                inDue_date = true;
                            } else if(tagName.equals("Number")){
                                inNumber = true;
                            }
                            break;
                        case XmlPullParser.TEXT :
                            if(tagName.equals("Name") && inName) {
                                temp.put("Name", parser.getText());
                            } else if (tagName.equals("Total") && inTotal) {
                                temp.put("Total", parser.getText());
                            } else if (tagName.equals("Current") && inCurrent){
                                temp.put("Current", parser.getText());
                            } else if(tagName.equals("Due_date") && inDue_date){
                                temp.put("Due_date", parser.getText());
                            } else if(tagName.equals("Number") && inNumber){
                                temp.put("Number", parser.getText());
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            tagName = parser.getName();
                            if (tagName.equals("data")) {
                                store.add(temp);
                            } else if (tagName.equals("Name")) {
                                inName = false;
                            } else if (tagName.equals("Total")) {
                                inTotal = false;
                            } else if(tagName.equals("Current")){
                                inCurrent = false;
                            } else if(tagName.equals("Due_date")){
                                inDue_date = false;
                            } else if(tagName.equals("Number")){
                                inNumber = false;
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

    private class CustomAdapter extends ArrayAdapter<HashMap<String, Object>>
    {

        public CustomAdapter(Context context, int textViewResourceId,
                             ArrayList<HashMap<String, Object>> Strings) {

            //let android do the initializing :)
            super(context, textViewResourceId, Strings);
        }

        //class for caching the views in a row
        private class ViewHolder
        {
            TextView name,date,stamp;

        }

        ViewHolder viewHolder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView==null)
            {
                convertView=inflater.inflate(R.layout.tabthr, null);
                viewHolder=new ViewHolder();

                //cache the views
                viewHolder.name=(TextView) convertView.findViewById(R.id.name);
                viewHolder.date=(TextView) convertView.findViewById(R.id.date);
                viewHolder.stamp=(TextView) convertView.findViewById(R.id.stamp);

                //link the cached views to the convertview
                convertView.setTag(viewHolder);

            }
            else
                viewHolder=(ViewHolder) convertView.getTag();

            //set the data to be displayed
            viewHolder.name.setText(searchResults.get(position).get("Name").toString());
            viewHolder.date.setText(searchResults.get(position).get("Due_date").toString());
            viewHolder.stamp.setText(searchResults.get(position).get("Current").toString() + "/ " + searchResults.get(position).get("Total").toString());

            //return the view to be displayed
            return convertView;
        }
    }
}
