package com.example.citycat;

import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TabHost;

public class TabsEvents extends TabActivity {
private TabHost mTabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabs_events);
		mTabHost= getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		
		//all events tab
		intent=new Intent(this,ListEvent.class);
		spec=mTabHost.newTabSpec("all Events")
				.setIndicator("All Events")
				.setContent(intent);
		mTabHost.addTab(spec); 	
		
		//events by category
		intent=new Intent(this,ListEventsByCategory.class);
		spec=mTabHost.newTabSpec("Category")
				.setIndicator("Category")
				.setContent(intent);
		mTabHost.addTab(spec); 	
		
		//events by type
		intent=new Intent(this,ListEventsByType.class);
		spec=mTabHost.newTabSpec("Type")
				.setIndicator("Type")
				.setContent(intent);
		mTabHost.addTab(spec); 	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tabs_events, menu);
		return true;
	}

	
}
