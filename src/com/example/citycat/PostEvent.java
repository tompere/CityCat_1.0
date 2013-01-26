package com.example.citycat;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.SaveCallback;

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

	Boolean postedSuccessfuly = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_event);
		postEventContext = this;
		/* Parse.com - Initialize */
		Parse.initialize(this, "sN3Uhl2rCCJvp1rodg9hYqw9pZN8kVkYuPCCwn5D", "ECprIUSxorEFhSSzq7ani1dR7Up4gApnjAmPFjiY"); 

		/* Initialize all form fields */

		Intent intent = getIntent();
		bundle = intent.getExtras();
		userLat = bundle.getDouble("lat");
		userLng = bundle.getDouble("lng");
		userCity = bundle.getString("city");

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
		//eventGetLocation = (Button)this.findViewById(R.id.postEvent_LocationButton);
		eventPost.setOnClickListener(clickhandler);
		//eventGetLocation.setOnClickListener(clickhandler);

		String eventTypesList[] = {"Beach", "Music", "Street Event", "Shopping"}; 
		ArrayAdapter<CharSequence> adapter2 = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,eventTypesList);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		eventType.setAdapter(adapter2);

		String eventCategorysList[] = {"Go Extreme", "Spend Money", "Sex, Drugs & RockN'Roll ", "Lose Yourself"}; 
		ArrayAdapter<CharSequence> adapter3 = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,eventCategorysList);
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

				Boolean vaildInput = true;

				/* input validation proccess */

				Date date = new Date(eventDate.getYear()-1900, eventDate.getMonth(), eventDate.getDayOfMonth(), eventTime.getCurrentHour(), eventTime.getCurrentMinute());
				ParseGeoPoint point = new ParseGeoPoint(userLat, userLng);

				ParseObject eventParseObject = new ParseObject("Event");
				
				// safe fields - no need to validate input
				eventParseObject.put("city",userCity);
				eventParseObject.put("type",eventType.getSelectedItem().toString());
				eventParseObject.put("category",eventCategory.getSelectedItem().toString());
				eventParseObject.put("gps",point);
				
				// name
				if (eventName.getText().toString().trim() != "") eventParseObject.put("name",eventName.getText().toString());
				else vaildInput = false;

				// description
				if (eventDescription.getText().toString().trim() != "") eventParseObject.put("description",eventDescription.getText().toString());
				else vaildInput = false;

				// date and time
				Date CurrentDate = new Date();			
				if (date.before(CurrentDate)) eventParseObject.put("date",date);
				else vaildInput = false;

				if (vaildInput)
				{
					eventParseObject.saveInBackground(new SaveCallback() {
						public void done(ParseException e) {			    	
							// success
							if (e == null) {
								postedSuccessfully();

							} // failure to save object in parse.com
							else {
								postFailed();
							}
						}			   
					});						
				}
				
				else
				{
					invalidInput();
				}
			}
		}
	}

	/* handle case where an event was posted successfully */
	public void postedSuccessfully()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Post An Event");

		// set dialog message
		alertDialogBuilder
		.setMessage("Success! You've posted an event")
		.setCancelable(false)
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, close
				// current activity
				startActivity(goToMain);
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	/* handle case where an event was NOT posted successfully */
	public void postFailed()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Post An Event");

		// set dialog message
		alertDialogBuilder
		.setMessage("Ooops! Something went wrong. please try posting the event again.")
		.setCancelable(false)
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, close
				// current activity
				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	
	/* handle case where an event was NOT posted successfully */
	public void invalidInput()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Post An Event");

		// set dialog message
		alertDialogBuilder
		.setMessage("Ooops! One of the events details are invalid. Please Try again.")
		.setCancelable(false)
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}


}
