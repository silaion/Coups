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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

public class BuyActivity extends ListActivity {

	/** Called when the activity is first created. */
	ArrayList<HashMap<String, Object>> searchResults; 
	ArrayList<HashMap<String, Object>> players;
	LayoutInflater inflater;
	Button buy;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.buy);
	    final EditText searchBox=(EditText) findViewById(R.id.searchBox);
	    
	    ListView playersListView=(ListView) findViewById(android.R.id.list);
	    ListView lv = getListView();  
    	lv.setTextFilterEnabled(true);  

    	buy = (Button)findViewById(R.id.buy);
    	buy.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(BuyActivity.this, BuytwoActivity.class);
				startActivity(intent);
			}
		});
    	inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        //these arrays are just the data that 
        //I'll be using to populate the ArrayList
        //You can use our own methods to get the data
        String names[]={"�Ѱ�ī�� ���̱� ���� 30% ���α�","�Ѱ�ī�� ��� ���� 10% ���α�","�ȼ�ī�� ��� ���� 20% ���α�","���ī�� ��� ���ͷ� 10% ���α�",};
        
        String teams[]={"��ȿ�Ⱓ (2014.9.1~2014.12.31)","��ȿ�Ⱓ (2014.9.1~2014.12.31)","��ȿ�Ⱓ (2014.9.1~2014.12.31)","��ȿ�Ⱓ (2014.9.1~2014.12.31)"};

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
       
       searchResults=new ArrayList<HashMap<String,Object>>(players);
       final CustomAdapter adapter=new CustomAdapter(this, R.layout.discountsell,searchResults);
       playersListView.setAdapter(adapter);
       searchBox.addTextChangedListener(new TextWatcher() {

    	   public void onTextChanged(CharSequence s, int start, int before, int count) {
    		   //get the text in the EditText
    		   String searchString=searchBox.getText().toString();
    		   int textLength=searchString.length();

    		   //clear the initial data set
    		   searchResults.clear();

    		   for(int i=0;i<players.size();i++)
    		   {
    			   String playerName=players.get(i).get("name").toString();
    			   if(textLength<=playerName.length()){
    				   //compare the String in EditText with Names in the ArrayList
    				   if(searchString.equalsIgnoreCase(playerName.substring(0,textLength)))
    					   searchResults.add(players.get(i));
    			   }
    		   }

    		   adapter.notifyDataSetChanged();
    	   }

    	   public void beforeTextChanged(CharSequence s, int start, int count,
    			   int after) {


    	   }


    	   @Override
    	   public void afterTextChanged(Editable s) {
    		   // TODO Auto-generated method stub

    	   }
       });
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
	   TextView name,team;
	    
	  }
	   
	  ViewHolder viewHolder;
	 
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	   
	   if(convertView==null)
	   {        
	                                //inflate the custom layout                        
	    convertView=inflater.inflate(R.layout.discountsell, null);
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
