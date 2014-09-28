package com.example.coups;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class usediscouponlistActivity extends ListActivity {
	
	ArrayList<HashMap<String, Object>> players;
	LayoutInflater inflater;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.usecouponlist);
	    ListView lv = getListView();  
    	lv.setTextFilterEnabled(true);  

	  //not necessary as ListActivity has an 
        //implicitly defined Layout(with a ListView of course)
        //setContentView(R.layout.players);   
         
        //get the LayoutInflater for inflating the customomView
        inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         
        //these arrays are just the data that 
        //I'll be using to populate the ArrayList
        //You can use our own methods to get the data
        String names[]={"�Ѱ�ī�� ������� 1000�� ���α�","�Ѱ�ī�� ġ������ 10% ���α�","�ȼ�ī�� ������� 1000�� ���α�",};
        
        String teams[]={"��ȿ�Ⱓ (2014.4.1~2014.6.31)","��ȿ�Ⱓ (2014.9.1~2014.12.31)","��ȿ�Ⱓ (2014.9.1~2014.12.31)"};

       players=new ArrayList<HashMap<String,Object>>();
        
       //HashMap for storing a single row
       HashMap<String , Object> temp;
        
       //total number of rows in the ListView
       int noOfPlayers=names.length;
        
       //now populate the ArrayList players
       for(int i=0;i<noOfPlayers;i++)
       {
	     temp=new HashMap<String, Object>();
	      
	     temp.put("name", names[i]);
	     temp.put("team", teams[i]);    
	      
	     //add the row to the ArrayList
	     players.add(temp);        
       }
        
       
       CustomAdapter adapter=new CustomAdapter(this, R.layout.usecoupon,players); 
    
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
	   TextView name,team;
	    
	  }
	   
	  ViewHolder viewHolder;
	 
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	   
	   if(convertView==null)
	   {        
	                                //inflate the custom layout                        
	    convertView=inflater.inflate(R.layout.usecoupon, null);
	    viewHolder=new ViewHolder();
	     
	    //cache the views
	    viewHolder.name=(TextView) convertView.findViewById(R.id.name);
	    viewHolder.team=(TextView) convertView.findViewById(R.id.team);
	     
	    //link the cached views to the convertview
	    convertView.setTag(viewHolder);
	     
	   }
	   else
	    viewHolder=(ViewHolder) convertView.getTag();
	    
	   
	    
	      //set the data to be displayed
	   viewHolder.name.setText(players.get(position).get("name").toString());
	   viewHolder.team.setText(players.get(position).get("team").toString());
	   
	   //return the view to be displayed
	  return convertView;
	  }
	 }
	
	    // TODO Auto-generated method stub
}
