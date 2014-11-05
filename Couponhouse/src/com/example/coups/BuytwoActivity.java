package com.example.coups;


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;

public class BuytwoActivity extends ListActivity {

    Button payment;
    ArrayList<HashMap<String, Object>> buy_coupon;
    LayoutInflater inflater;
    Global global;
    BuyActivity buy;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.discount);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        global = new Global();
        buy = new BuyActivity();
        buy_coupon = new ArrayList<HashMap<String, Object>>(buy.checked_coupon);
        payment = (Button)findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(BuytwoActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });



        inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CustomAdapter adapter = new CustomAdapter(this, R.layout.buylist,buy_coupon);
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

}
