package com.example.coups;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TabThrActivity extends ListActivity {

	ArrayList<HashMap<String, Object>> searchResults;
	ArrayList<HashMap<String, Object>> players;
	LayoutInflater inflater;
	Button adjust;
	/** Called when the activity is first created. */
	@Override 
	
	 
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.adjust);

	    final EditText searchBox=(EditText) findViewById(R.id.searchBox);
	    ListView playersListView=(ListView) findViewById(android.R.id.list);
	    
	    ListView lv = getListView();  
    	lv.setTextFilterEnabled(true);  
    	lv.setOnItemClickListener(new OnItemClickListener() {    
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {      
    			// When clicked, show a toast with the TextView text      
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
        
        //these arrays are just the data that 
        //I'll be using to populate the ArrayList
        //You can use our own methods to get the data
        String names[]={"한경카페","안성카페","오구쌀피자","토마토",};
        
        String teams[]={"유효기간 (2014.9.1~2014.12.31)","유효기간 (2014.9.1~2014.12.31)","유효기간 (2014.9.1~2015.9.1)","유효기간 (2014.9.1~2015.9.1)"};
        
        String stamps[]={"5/10","2/10","7/10","1/10"};

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
	     temp.put("stamp", stamps[i]);  
	      
	     //add the row to the ArrayList
	     players.add(temp);        
       }
        
       searchResults=new ArrayList<HashMap<String,Object>>(players);
       final CustomAdapter adapter=new CustomAdapter(this, R.layout.tabthr,searchResults);
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
	   TextView name,team,stamp;
	 
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
	     viewHolder.team=(TextView) convertView.findViewById(R.id.team);
	     viewHolder.stamp=(TextView) convertView.findViewById(R.id.stamp);
	 
	      //link the cached views to the convertview
	      convertView.setTag(viewHolder);
	 
	   }
	   else
	    viewHolder=(ViewHolder) convertView.getTag();
	 
	
	 
	   //set the data to be displayed
	   viewHolder.name.setText(searchResults.get(position).get("name").toString());
	   viewHolder.team.setText(searchResults.get(position).get("team").toString());
	   viewHolder.stamp.setText(searchResults.get(position).get("stamp").toString());
	 
	   //return the view to be displayed
	   return convertView;
	  }
	 
	 }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
       switch(keyCode) {
         case KeyEvent.KEYCODE_BACK:
        	 Toast.makeText(this, "뒤로가기버튼 눌림", Toast.LENGTH_SHORT).show();
           new AlertDialog.Builder(this)
           .setTitle("프로그램 종료")
           .setMessage("프로그램을 종료 하시겠습니까?")
           .setPositiveButton("예", new DialogInterface.OnClickListener() {
        	   @Override
        	   public void onClick(DialogInterface dialog, int whichButton) {
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
