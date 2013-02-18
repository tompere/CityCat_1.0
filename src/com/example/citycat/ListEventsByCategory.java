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
				ParseObject ParseCategory;
				String Category_event="";
				if (e == null) {

					int i;
					for (i = 0; i < objects.size(); i++) {
						ParseCategory = objects.get(i);
						Category_event = ParseCategory.getString("category")
								.toString();
						ListCategory.add(Category_event);

					}
					AdapterCategory();
					GetCategoryEvents(Category_event);
				} else
					Log.d("Error", e.getMessage());


			}


		});




	}



	protected void onStart(){
		super.onStart();

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			private String selectedCategory = "";
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int arg2, long pos) {

				selectedCategory = parent.getItemAtPosition((int)pos).toString();
				Log.d("Selected Value is:", selectedCategory);
				GetCategoryEvents(selectedCategory);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}



	private void GetCategoryEvents(String Category_event) {

		ListEvents.clear();
		Log.d("checking","checking");
		ParseQuery query = new ParseQuery("Event");
		query.whereEqualTo("category", Category_event);
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
					Log.d("dddd", "ssss");
					AdapterEvent();

				} else {
					Log.d("Error", e.getMessage());
				}

			}

		});
		
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick (AdapterView<?> parent, View v, int position,
					long id) {
				String item = (String) getListAdapter().getItem(position);
				Log.d("item", "item is: "+ item);
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
							Log.d("item", "beforeintent");
							Intent intent = new Intent(thisContext,Details_Events.class);
							intent.putExtra("name",name);
							intent.putExtra("category",category);
							intent.putExtra("type",type);
							intent.putExtra("dateFormat",dateFormat);
							intent.putExtra("time",time);
							intent.putExtra("description",description);
							intent.putExtra("city",city);
							Log.d("item", "afterintent");
							startActivity(intent);

						}

					}

				});
			
			}
		});
	}

	private void AdapterCategory() {
		ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,ListCategory);
		spinner.setAdapter(adapterSpinner);
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
			Log.d("Error","Error is: There is not exsisting adapter");
			return null;
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list_event, menu);
		return true;
	}

}
