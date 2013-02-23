package com.example.citycat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Context;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import android.content.Intent;


public class ListEvent extends Activity {

	ListView list;
	ArrayList<String> ListEventNames;
	Context thisContext;
	ArrayList<String> ListEvents;
	ArrayList<String> ListEvents_Names;
	ArrayAdapter<String> adapter;
	CityCatParseCom parseCom;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_event);
		list = (ListView) findViewById(R.id.list_events);
		thisContext=this;
		parseCom = new CityCatParseCom(this);
		
		// get all events and set into listView
		ListEvents = parseCom.getAllEvents();		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ListEvents);
		list.setAdapter(adapter);
		
		// listener on events list - on click go to specific event screen
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick (AdapterView<?> parent, View v, int position,
					long id) {
				String item = (String) adapter.getItem(position);
				Intent EventActivity = parseCom.getSpecigicEventByCriteria("name",item);
				startActivity(EventActivity);
			}
		});

	}

	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.activity_list_event, menu);
		return true;
	}

	public ArrayAdapter<String> getListAdapter()
	{
		if (!adapter.isEmpty()) return adapter;			
		else return null;
	}

}





