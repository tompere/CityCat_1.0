package com.example.citycat;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

public class PostEvent extends Activity {

	EditText eventName;
	EditText eventCity;
	Spinner eventType;
	Spinner eventCategory;
	EditText eventDescription;
	DatePicker eventDate;
	TimePicker eventTime;
	Button eventPost;
	Intent goToMain;
	Context postEventContext;
	Bundle bundle;
	double userLat = 0.0;
	double userLng = 0.0;
	String userCity = "";
	Boolean IsUserEventsPost;
	Boolean postedSuccessfuly = false;
	String objectId;
	CityCatParseCom parseCom;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_event);
		postEventContext = this;
		
		parseCom = new CityCatParseCom(postEventContext);	

		/* Initialize all form fields */

		Intent intent = getIntent();
		bundle = intent.getExtras();
		userLat = bundle.getDouble("lat");
		userLng = bundle.getDouble("lng");
		userCity = bundle.getString("city");
		IsUserEventsPost = bundle.getBoolean("IsUserEventsPost");
		
		//getting the objectId - this will help us to delete or update ( only if its the user events post and from the main activity
		// we need to vissible/unvissible the delete button..
		if (IsUserEventsPost)
			objectId = bundle.getString("objectId");
		
		eventCity = (EditText)this.findViewById(R.id.event_city_input);
		eventCity.setText(userCity);
		eventCity.setClickable(false);
		eventCity.setEnabled(false);
		eventCity.setKeyListener(null);

		eventName = (EditText)this.findViewById(R.id.event_name);
		eventType = (Spinner)this.findViewById(R.id.event_type);
		eventCategory = (Spinner)this.findViewById(R.id.event_category);
		eventDescription = (EditText)this.findViewById(R.id.event_description);
		eventDate = (DatePicker)this.findViewById(R.id.event_date);
		eventTime = (TimePicker)this.findViewById(R.id.event_time);
		eventTime.setIs24HourView(true);

		/* Initializing all intents */
		goToMain = new Intent(this, MainActivity.class);

		/* Initialize all buttons */
		clickHandler clickhandler = new clickHandler();
		eventPost = (Button)this.findViewById(R.id.event_post_button);
		eventPost.setOnClickListener(clickhandler);
			
		ArrayAdapter<CharSequence> adapter2 = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, 
				CityCatParseCom.getTypesSharedPref(this)); 
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		eventType.setAdapter(adapter2);
		
		ArrayAdapter<CharSequence> adapter3 = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,
				CityCatParseCom.getCategoriesSharedPref(this));
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		eventCategory.setAdapter(adapter3);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_post_event, menu);
		return true;
	}

	/* Internal Class to handle all click events */
	class clickHandler implements View.OnClickListener {
		public void onClick(View v)
		{
			if ((Button)v == eventPost)
			{

				boolean vaildInput = true;

				/* input validation proccess */

				Date date = new Date(eventDate.getYear()-1900, eventDate.getMonth(), eventDate.getDayOfMonth(), eventTime.getCurrentHour(), eventTime.getCurrentMinute());
				ParseGeoPoint point = new ParseGeoPoint(userLat, userLng);

				ParseObject eventParseObject = new ParseObject("Event");

				// safe fields - no need to validate input
				eventParseObject.put("city",userCity);
				eventParseObject.put("type",eventType.getSelectedItem().toString());
				eventParseObject.put("category",eventCategory.getSelectedItem().toString());
				eventParseObject.put("gps",point);
				
				SharedPreferences ref = getSharedPreferences("user_details",MODE_PRIVATE);
				
				eventParseObject.put("publisher",ref.getString("username", "/Unknown/"));

				// name
				String tempEventName = eventName.getText().toString().trim();
				if ( tempEventName.equals("") ) vaildInput = false;
				else eventParseObject.put("name",tempEventName);

				// description
				String tempDescription = eventDescription.getText().toString().trim();
				if ( tempDescription.equals("") ) vaildInput = false;
				else eventParseObject.put("description",tempDescription);

				// date and time
				Date CurrentDate = new Date();			
				if (date.before(CurrentDate)) vaildInput = false;
				else eventParseObject.put("date",date);

				if (vaildInput)
				{
					parseCom.postEvent(eventParseObject);		
				}

				else
				{
					AppAlertDialog.showNeutraAlertDialog(postEventContext, "Post Event", "Ooops! Events details are invalid.\nPlease Try again.", null);
				}
			}
		}
	}

}
