package com.example.citycat;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_event_type);
		list = (ListView) findViewById(R.id.list_events_type);
		ListEvents = new ArrayList<String>();
		ListType = new ArrayList<String>();
		spinner = (Spinner) findViewById(R.id.spinner_type);
		Parse.initialize(this, "sN3Uhl2rCCJvp1rodg9hYqw9pZN8kVkYuPCCwn5D",
				"ECprIUSxorEFhSSzq7ani1dR7Up4gApnjAmPFjiY");

		ParseQuery queryCategory = new ParseQuery("Types");
		queryCategory.findInBackground(new FindCallback() {
			public void done(List<ParseObject> objects, ParseException e) {
				ParseObject ParseType;
				 String Type_event="";
				if (e == null) {
					
					int i;
					for (i = 0; i < objects.size(); i++) {
						ParseType = objects.get(i);
						Type_event = ParseType.getString("TypeName")
								.toString();
						ListType.add(Type_event);
						
					}
					AdapterType();
					GetCategoryEvents(Type_event);
				} else
					Log.d("Error", e.getMessage());
				
				
			}
			
			
		});

		
		

	}



	protected void onStart(){
		super.onStart();
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			private String selectedType = "";
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int arg2, long pos) {
				
				selectedType = parent.getItemAtPosition((int)pos).toString();
				Log.d("Selected Value is:", selectedType);
				GetCategoryEvents(selectedType);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	

	private void GetCategoryEvents(String Category_type) {

		ListEvents.clear();
		Log.d("checking","checking");
		ParseQuery query = new ParseQuery("Event");
		query.whereEqualTo("type", Category_type);
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> objects, ParseException e) {

				if (e == null) {
					String name_event;

					int i;
					for (i = 0; i < objects.size(); i++) {
						ParseObject ParseEvent = objects.get(i);
						name_event = ParseEvent.getString("name").toString();

						
						ListEvents.add(name_event);

					}
					Log.d("dddd", "ssss");
					AdapterEvent();

				} else {
					Log.d("Error", e.getMessage());
				}

			}

		});
	}
	
	private void AdapterType() {
		ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,ListType);
		spinner.setAdapter(adapterSpinner);
	}
	
		
	
	private void AdapterEvent() {
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ListEvents);
		list.setAdapter(adapter);
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list_event, menu);
		return true;
	}

}
