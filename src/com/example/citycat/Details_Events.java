package com.example.citycat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Details_Events extends Activity {

	Bundle bundle;
	String name;
	String category;
	String type;
	String time;
	String description;
	String city;
	String dateFormat;
	String gps;
	String objectId;
	boolean IsUserEvents;
	boolean IsUserEventsPost;
	TextView tv_name;
	TextView tv_category;
	TextView tv_type;
	TextView tv_time;
	TextView tv_description;
	TextView tv_city;
	TextView tv_dateFormat;
	Button btn_Back;
	Button btn_GPS;
	Button btn_UPDATE;
	Context thiscontext;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details__events);
		
		// Initialize
		thiscontext=this;
		Intent intent = getIntent();
		
		// retrieve bundle data (from events list)
		bundle = intent.getExtras();
		category = bundle.getString("category");
		type = bundle.getString("type");
		time = bundle.getString("time");
		name = bundle.getString("name");
		description = bundle.getString("description");
		city = bundle.getString("city");
		objectId = bundle.getString("objectId");
		dateFormat = bundle.getString("dateFormat");
		IsUserEvents =bundle.getBoolean("IsUserEvents");
		
		// initialize all layout elements (buttons and text views)
		tv_name=(TextView)findViewById(R.id.event_name_user);
		tv_category=(TextView)findViewById(R.id.event_category_user);
		tv_type=(TextView)findViewById(R.id.event_type_user);
		tv_time=(TextView)findViewById(R.id.event_time_user);
		tv_description=(TextView)findViewById(R.id.event_description_user);
		tv_city=(TextView)findViewById(R.id.event_city_user);
		tv_dateFormat=(TextView)findViewById(R.id.event_Date_user);
		btn_Back=(Button)findViewById(R.id.btn_Back);
		btn_GPS=(Button)findViewById(R.id.Btn_gps);
		btn_UPDATE=(Button)findViewById(R.id.btn_Update);
		
		if (IsUserEvents)
		{
			//if its the user call - let him update the event if he want
			btn_UPDATE.setVisibility(0);
			IsUserEventsPost=true;
		}
		else
		{
			//else
			btn_UPDATE.setVisibility(4);
			IsUserEventsPost=false;
		}
		
		//set text
		tv_name.setText(name);
		tv_category.setText(category);
		tv_type.setText(type);
		tv_description.setText(description);
		tv_city.setText(city);
		tv_dateFormat.setText(dateFormat);
		tv_time.setText(time);

		clickHandler handler = new clickHandler();
		btn_Back.setOnClickListener(handler);
		btn_GPS.setOnClickListener(handler);
		
		
	}

	class clickHandler implements View.OnClickListener {
		public void onClick(View v)
		{
			if  ( (Button)v == btn_Back)
			{
				Log.d("succeed","secceed");
				Details_Events.this.finish();
			}
			
			if ((Button)v == btn_GPS)
			{
				//tom : change this constructor if u need to
				Intent intent = new Intent(thiscontext,LocationMap.class); 
				intent.putExtra("gpsLatitude", bundle.getDouble("gpsLat"));
				intent.putExtra("gpsLongtitude", bundle.getDouble("gpsLng"));
				startActivity(intent);
				
			}
			if ((Button)v == btn_UPDATE)
			{
				Intent intent = new Intent(thiscontext,PostEvent.class); 
				//information about the original call ( to know if it postEvent is for new or update event
				intent.putExtra("IsUserEventsPost", IsUserEventsPost);
				intent.putExtra("objectId", objectId);
				startActivity(intent);
			}
				
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.details__events, menu);
		return true;
	}

}
