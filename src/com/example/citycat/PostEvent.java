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
				String tempEventName = eventName.getText().toString().trim();
				if ( tempEventName != "" ) eventParseObject.put("name",tempEventName);
				else vaildInput = false;

				// description
				String tempDescription = eventDescription.getText().toString().trim();
				if ( tempDescription != "" ) eventParseObject.put("description",tempDescription);
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
								showNeutraAlertDialog(postEventContext, "Post Event", "Success!\nYou've posted an event", goToMain);

							} // failure to save object in parse.com
							else {
								showNeutraAlertDialog(postEventContext, "Post Event", "Ooops! Something went wrong.\nplease try again.", null);
							}
						}			   
					});						
				}

				else
				{
					showNeutraAlertDialog(postEventContext, "Post Event", "Ooops! Events details are invalid.\nPlease Try again.", null);
				}
			}
		}
	}

	/* generic function to handle AlertDialog */
	public void showNeutraAlertDialog(Context context, String title, String msg, Intent goToActivity)
	{
		final Intent whatTodo = goToActivity;

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

		// set title
		alertDialogBuilder.setTitle(title);

		// set dialog message
		alertDialogBuilder
		//.setMessage("Ooops! One of the events details are invalid. Please Try again.")
		.setMessage(msg)
		.setCancelable(false)
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {				
				if (whatTodo == null) dialog.cancel(); //cancel dialog
				else startActivity(whatTodo); //go to chosen activity
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}



}
