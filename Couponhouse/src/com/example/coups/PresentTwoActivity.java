package com.example.coups;


import android.widget.Button;

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
import android.widget.ListView;
import android.widget.TextView;

public class PresentTwoActivity extends ListActivity {

	Button couponlist;
	ArrayList<HashMap<String, Object>> players;
	LayoutInflater inflater;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.presenttwo);
	    ListView lv = getListView();  
    	lv.setTextFilterEnabled(true);  
    	
    	
    	
	    
    	couponlist = (Button)findViewById(R.id.couponlist);
    	couponlist.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PresentTwoActivity.this, PresentActivity.class);
				startActivity(intent);
			}
		});
		
		
		inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        //these arrays are just the data that 
        //I'll be using to populate the ArrayList
        //You can use our own methods to get the data
                
        String names[]={"�Ѱ�ī�� ���̱� 10% ���α�","�ȼ�ī�� �Ƹ޸�ī�� 1000�� ���α�","�ȼ�ī�� ��� ���� 10% ���α�",};
        
        String teams[]={"��ȿ�Ⱓ (2014.9.1~2014.12.31)","��ȿ�Ⱓ (2014.9.1~2014.12.31)","��ȿ�Ⱓ (2014.9.1~2014.12.31)"};

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
        
       
       CustomAdapter adapter=new CustomAdapter(this, R.layout.pretwo,players); 
    
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
	    convertView=inflater.inflate(R.layout.pretwo, null);
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
	 }// TODO Auto-generated method stub
	
	

}