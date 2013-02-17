package com.example.citycat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class ListPreviosEvents extends Activity {
	ListView list;
	ArrayList<String> ListEventNames;
	Context thisContext;
	ArrayList<String> ListEvents;
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_previos_events);
		Log.d("check", "check1" );
		list=(ListView) findViewById(R.id.list_previos_events);
		ListEvents=new ArrayList<String>();
		Parse.initialize(this, "sN3Uhl2rCCJvp1rodg9hYqw9pZN8kVkYuPCCwn5D", "ECprIUSxorEFhSSzq7ani1dR7Up4gApnjAmPFjiY");
		Log.d("check", "check2" );
		 SharedPreferences ref = getSharedPreferences("user_details",MODE_PRIVATE);
		 Log.d("check", "check3" );
		String user_name=ref.getString("username","unknown");
		Log.d("publisher", "publisher is" + user_name );
	
		ParseQuery query = new ParseQuery("Event");
		query.whereEqualTo("publisher", user_name);
		
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
						String dateFormat = date.getDay() + 
								"/" + date.getMonth() + 
								"/" + date.getYear();
						name_event= ParseEvent.getString("name").toString() + " | " + dateFormat;
						
						Log.d("check name ",name_event);
						ListEvents.add(name_event);
						
					}
					Log.d("dddd", "ssss");
					AdapterList();
			
				}
				else
				{
					Log.d("ddddaaaa", "ssssaaaaa");
				}
			
		
			}
		
			
		});
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list_event, menu);
		return true;
	}

	
	public void AdapterList()
	{
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ListEvents);
		Log.d("dddd", "ssss1111");
		list.setAdapter(adapter);
		Log.d("dddd", "ssss2222");
	}
	


}
