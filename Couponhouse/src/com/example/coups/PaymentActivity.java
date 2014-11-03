package com.example.coups;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class PaymentActivity extends ListActivity {
    LayoutInflater inflater;
    Global global;
    BuyActivity buy;
    ArrayList<String> Disc_Numbers;
    ArrayList<HashMap<String, Object>> store;
    String total;
    TextView Total;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.payment);

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        Total = (TextView)findViewById(R.id.total);

        buy = new BuyActivity();

        Disc_Numbers = new ArrayList<String>();

        for(int i = 0 ; i < buy.checked_coupon.size() ; i++){
            if(buy.checked_coupon.get(i) != null){
                Disc_Numbers.add(buy.checked_coupon.get(i).get("Disc_Number").toString());
            }
        }
        inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        HttpConnect httpConnect = new HttpConnect();
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


        CustomAdapter adapter = new CustomAdapter(this, R.layout.buylist,store);
        //finally,set the adapter to the default ListView
        setListAdapter(adapter);
	}

    private class HttpConnect extends AsyncTask<Void, Void, Void> {
        String url = "http://112.172.217.79:8080/JSP_Server/disc_perchase_test.jsp";
        ArrayList<HashMap<String, Object>> store;
        String tagName = null;
        int eventType;
        boolean flag = false;
        boolean inS_Name = false, inDisc_Name = false, inPrice = false, intotal = false;

        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, Object> temp = new HashMap<String, Object>();

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                List nameValuePairs = new ArrayList();
                nameValuePairs.add(new BasicNameValuePair("c_number", global.c_Number));
                for(int i = 0; i < Disc_Numbers.size(); i++){
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
                            } else if (tagName.equals("total_price")) {
                                intotal = true;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            if (tagName.equals("s_name") && inS_Name) {
                                temp.put("S_Name", parser.getText());
                            } else if (tagName.equals("disc_name") && inDisc_Name) {
                                temp.put("Disc_Name", parser.getText());
                            } else if (tagName.equals("disc_price") && inPrice) {
                                temp.put("Price", parser.getText());
                            } else if (tagName.equals("total_price") && intotal) {
                                total = parser.getText();
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            tagName = parser.getName();
                            if (tagName.equals("data")) {
                                store.add(temp);
                            } else if (tagName.equals("s_name")) {
                                inS_Name = false;
                            } else if (tagName.equals("disc_name")) {
                                inDisc_Name = false;
                            } else if (tagName.equals("disc_price")) {
                                inPrice = false;
                            } else if (tagName.equals("total_price")) {
                                intotal = false;
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
            Total.setText("합계 : " + total + " 원");
        }
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
            viewHolder.BuyList_Name.setText(store.get(position).get("S_Name").toString() + " " + store.get(position).get("Disc_Name").toString());
            viewHolder.BuyList_Price.setText(store.get(position).get("Price").toString() + " 원");
            //return the view to be displayed
            return convertView;
        }
    }// TODO Auto-generated method stub

}
