package com.example.coups;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Tabview extends TabActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.main);
    	Resources res = getResources();
    	// Resource object to get Drawables    
    	TabHost tabHost = getTabHost();  
    	// The activity TabHost    
    	TabHost.TabSpec spec;  
    	// Resusable TabSpec for each tab    
    	Intent intent;  
    	// Reusable Intent for each tab    
    	// Create an Intent to launch an Activity for the tab (to be reused)    
    	intent = new Intent().setClass(this, TabOneActivity.class);    
    	// Initialize a TabSpec for each tab and add it to the TabHost    
    	spec = tabHost.newTabSpec("main").setIndicator("Main").setContent(intent);
    	tabHost.addTab(spec);
    	// Do the same for the other tabs    
    	intent = new Intent().setClass(this, TabTwoActivity.class);    
    	spec = tabHost.newTabSpec("info").setIndicator("∞°∏Õ¡°¡§∫∏").setContent(intent);
    	tabHost.addTab(spec);
    	intent = new Intent().setClass(this, TabThrActivity.class);
    	spec = tabHost.newTabSpec("coupon").setIndicator("µµ¿ÂƒÌ∆˘").setContent(intent);
    	tabHost.addTab(spec);
    	intent = new Intent().setClass(this, TabFouActivity.class);
    	spec = tabHost.newTabSpec("discount").setIndicator("«“¿ŒƒÌ∆˘").setContent(intent);
    	tabHost.addTab(spec); 
    	intent = new Intent().setClass(this, TabFivActivity.class);
    	spec = tabHost.newTabSpec("mypage").setIndicator("MyPage").setContent(intent);
    	tabHost.addTab(spec); 
    	tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#00BFFF"));
    	tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#00BFFF"));
    	tabHost.getTabWidget().getChildAt(2).setBackgroundColor(Color.parseColor("#00BFFF"));
    	tabHost.getTabWidget().getChildAt(3).setBackgroundColor(Color.parseColor("#00BFFF"));
    	tabHost.getTabWidget().getChildAt(4).setBackgroundColor(Color.parseColor("#00BFFF"));
    	tabHost.setCurrentTab(0);
	
	}

}