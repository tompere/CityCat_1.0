package com.example.citycat;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ListEventsByCategory extends Activity {
	ListView list;
	ArrayList<String> ListEventNames;
	Context thisContext;
	ArrayList<String> ListEvents;
	ArrayList<String> ListCategory;
	Spinner spinner;
	ArrayAdapter<String> adapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_event_category);
		list = (ListView) findViewById(R.id.list_events_category);
		ListEvents = new ArrayList<String>();
		ListCategory = new ArrayList<String>();
		spinner = (Spinner) findViewById(R.id.spinner);
		Parse.initialize(this, "sN3Uhl2rCCJvp1rodg9hYqw9pZN8kVkYuPCCwn5D",
				"ECprIUSxorEFhSSzq7ani1dR7Up4gApnjAmPFjiY");

		ParseQuery queryCategory = new ParseQuery("Categories");
		queryCategory.findInBackground(new FindCallback() {
			public void done(List<ParseObject> objects, ParseException e) {

				if (e == null) {
					String Category_event;
					int i;
					for (i = 0; i < objects.size(); i++) {
						ParseObject ParseCategory = objects.get(i);
						Category_event = ParseCategory.getString("category")
								.toString();
						ListCategory.add(Category_event);

					}
					CategoryAdapter();
				} else
					Log.d("Error", e.getMessage());
			}

			
		});

		ParseQuery query = new ParseQuery("Event");
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> objects, ParseException e) {

				if (e == null) {
					String name_event;

					int i;
					for (i = 0; i < objects.size(); i++) {
						ParseObject ParseEvent = objects.get(i);
						name_event = ParseEvent.getString("name").toString();

						Log.d("check", name_event);
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
	
	private void CategoryAdapter() {
		ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,ListCategory);
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
