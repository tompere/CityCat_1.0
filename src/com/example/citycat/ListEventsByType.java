package com.example.citycat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ListEventsByType extends Activity {
	ListView list;
	ArrayList<String> ListEventNames;
	Context thisContext;
	ArrayList<String> ListEvents;
	ArrayList<String> ListType;
	Spinner spinner;
	ArrayAdapter<String> adapter;
	CityCatParseCom parseCom;
	String selectedType;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_event_type);
		list = (ListView) findViewById(R.id.list_events_type);
		spinner = (Spinner) findViewById(R.id.spinner_type);
		// initialize parse object
		parseCom = new CityCatParseCom(this);
		thisContext = this;
		
		// retrieve events types from shared prefernces and set into spinner
		ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				CityCatParseCom.getTypesSharedPref(this));
		spinner.setAdapter(adapterSpinner);				
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent, View view, int arg2, long pos) {		
						selectedType = parent.getItemAtPosition((int)pos).toString();
						ListEvents = parseCom.getAllEventByCriteria("type",selectedType);					
						adapter = new ArrayAdapter<String>(thisContext, android.R.layout.simple_list_item_1, ListEvents);						
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
		
					public void onNothingSelected(AdapterView<?> arg0) {}
					
				});

	}



	protected void onStart(){
		super.onStart();

	}


	/*
	private void GetCategoryEvents(String Category_type) {

		ListEvents.clear();
		ParseQuery query = new ParseQuery("Event");
		query.whereEqualTo("type", Category_type);
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> objects, ParseException e) {

				if (e == null) {
					String name_event;

					int i;
					for (i = 0; i < objects.size(); i++) {
						ParseObject ParseEvent = objects.get(i);
						Date date = ParseEvent.getDate("date");
						int Year = date.getYear()-100+2000;

						String dateFormat = date.getDay() + 
								"/" + date.getMonth() + 
								"/" + Year;

						name_event= ParseEvent.getString("name").toString() + "   " + dateFormat;
						ListEvents.add(name_event);

					}
					AdapterEvent();

				} else {
				}

			}

		});
		
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick (AdapterView<?> parent, View v, int position,
					long id) {
				String item = (String) getListAdapter().getItem(position);
				ParseQuery query = new ParseQuery("Event");
				query.whereEqualTo("name", item);
				query.findInBackground(new FindCallback() {
					public void done(List<ParseObject> objects, ParseException e) {

						if (e == null) {
							ParseObject ParseEvent = objects.get(0);
							String name= ParseEvent.getString("name");
							String category= ParseEvent.getString("category");
							String type= ParseEvent.getString("type");
							Date date = ParseEvent.getDate("date");
							int Year = date.getYear()-100+2000;
							int Month=date.getMonth()+1;
							String dateFormat = date.getDay() + 
									"/" + Month + 
									"/" + Year;
							String time=date.getHours()+ ":"+date.getMinutes();
							String description=ParseEvent.getString("description");
							String city= ParseEvent.getString("city");
							ParseGeoPoint gps_parse=ParseEvent.getParseGeoPoint("gps");
							String gps= gps_parse.toString();
							Intent intent = new Intent(thisContext,Details_Events.class);
							intent.putExtra("name",name);
							intent.putExtra("category",category);
							intent.putExtra("type",type);
							intent.putExtra("dateFormat",dateFormat);
							intent.putExtra("time",time);
							intent.putExtra("description",description);
							intent.putExtra("city",city);
							intent.putExtra("gps", gps);
							intent.putExtra("IsUserEvents", false);
							startActivity(intent);

						}

					}

				});
			
			}
		});
	}

	private void AdapterEvent() {
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ListEvents);
		list.setAdapter(adapter);
	}

	public ArrayAdapter<String> getListAdapter()
	{
		if (!adapter.isEmpty())
			return adapter;
		else
		{
			return null;
		}
	}
	
	*/


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list_event, menu);
		return true;
	}

}
