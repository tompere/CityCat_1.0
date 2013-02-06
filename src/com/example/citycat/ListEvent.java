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

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ListEvent extends Activity {
	ListView list;
	ArrayList<String> ListEvents;
	Context thisContext;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_event);
		ListEvents = new ArrayList<String>();
		Parse.initialize(this, "sN3Uhl2rCCJvp1rodg9hYqw9pZN8kVkYuPCCwn5D", "ECprIUSxorEFhSSzq7ani1dR7Up4gApnjAmPFjiY");

		list=(ListView) findViewById(R.id.listView1);
		//ParseObject eventParseObject = new ParseObject("Event");
		ParseQuery query = new ParseQuery("Event");
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> objects, ParseException e) {
				if (e==null && objects != null)
				{
					int i;
					for (i=0; i<objects.size(); i++) 
					{
						ListEvents.add(objects.get(i).getString("name"));
					}

				}
				else
				{
					//Log.d("The_Parse", "Error: " + e.getMessage());
				}
			}
		});
		
		ArrayAdapter <String> adapter = new ArrayAdapter<String>(thisContext,android.R.layout.simple_list_item_1,android.R.id.text2,ListEvents);
		list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list_event, menu);
		return true;
	}

}