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


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_event);
		list=(ListView) findViewById(R.id.list_events);
		thisContext=this;
		ListEvents=new ArrayList<String>();
		Parse.initialize(this, "sN3Uhl2rCCJvp1rodg9hYqw9pZN8kVkYuPCCwn5D", "ECprIUSxorEFhSSzq7ani1dR7Up4gApnjAmPFjiY");



		ParseQuery query = new ParseQuery("Event");
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> objects, ParseException e) {

				if (e==null )
				{
					String name_event;
					int i;
					for (i=0; i<objects.size(); i++) 
					{
						ParseObject ParseEvent = objects.get(i);
						Date date = ParseEvent.getDate("date");
						int Year = date.getYear()-100+2000;
						int Month=date.getMonth()+1;
						//	String dateFormat = date.getDay() + 
						//			"/" + Month + 
						//			"/" + Year;
						String name=ParseEvent.getString("name").toString();
						//	name_event= name + "   " + dateFormat;
						ListEvents.add(name);
						//	ListEvents_Names.add(name);
					}
					Log.d("dddd", "ssss");
					AdapterList();

				}
				else
				{

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
							Log.d("item", "afterintent");
							startActivity(intent);

						}

					}

				});

			}
		});



	}










	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list_event, menu);
		return true;
	}


	public void AdapterList()
	{
		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ListEvents);
		//adapter_parse=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ListEvents_Names);
		Log.d("dddd", "ssss1111");
		list.setAdapter(adapter);
		Log.d("dddd", "ssss2222");

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

}





