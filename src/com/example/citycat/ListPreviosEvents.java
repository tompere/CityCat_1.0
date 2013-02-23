package com.example.citycat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class ListPreviosEvents extends Activity {
	ListView list;
	ArrayList<String> ListEventNames;
	Context thisContext;
	ArrayList<String> ListEvents;
	ArrayAdapter<String> adapter;
	CityCatParseCom parseCom;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_previos_events);
		list = (ListView) findViewById(R.id.list_previos_events);
		ListEvents = new ArrayList<String>();
		parseCom = new CityCatParseCom(this);
		
		SharedPreferences ref = getSharedPreferences("user_details",MODE_PRIVATE);
		String usermame = ref.getString("username","unknown");
		
		// get all events and set into listView
		ListEvents = parseCom.getAllEventByCriteria("publisher", usermame);	
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list_event, menu);
		return true;
	}

}
