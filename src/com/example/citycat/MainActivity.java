package com.example.citycat;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button postEvent;
	Button  ChooseEvent;
	Intent goToPostEvent;
	Intent goToChooseEvent;
	Intent goToPreviousEvents;
	TextView PreviosEvents;
	Context context;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
    	PreviosEvents = (Button) this.findViewById(R.id.YourEvent);
    	PreviosEvents.setVisibility(4);
 		postEvent = (Button) this.findViewById(R.id.location);
 		postEvent.setVisibility(4);
 		ChooseEvent = (Button) this.findViewById(R.id.ChooseEvent);
 		ChooseEvent.setVisibility(4);
 		
 		goToPostEvent = new Intent(context, LocationMap.class);   
 		goToChooseEvent = new Intent(context, TabsEvents.class);
 		goToPreviousEvents = new Intent(context, ListPreviosEvents.class);
		
   	 	/* retrieve events types and categories */
		CityCatParseCom parseCom = new CityCatParseCom(context);
		SharedPreferences ref = getSharedPreferences("local_parseCom",MODE_PRIVATE);
		SharedPreferences.Editor ed = ref.edit();
		
		// for types
		String concatedTypes = "";
		for (String type : parseCom.getTypesOnline()){
			concatedTypes+= type + ";";
		}
		ed.putString("events_types", concatedTypes);
		
		// for categories
		String concatedCategories = "";
		for (String category : parseCom.getCategoriesOnline()){
			concatedCategories+= category + ";";
		}
		ed.putString("events_categories", concatedCategories);		
		ed.commit();
		
 		clickHandler click = new clickHandler();
 		postEvent.setVisibility(0);
 		postEvent.setOnClickListener(click);
 		ChooseEvent.setVisibility(0);
 		ChooseEvent.setOnClickListener(click);
 		PreviosEvents.setVisibility(0);
 		PreviosEvents.setOnClickListener(click);		
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	class clickHandler implements View.OnClickListener {
		public void onClick(View v)
		{
			if ((Button)v == postEvent) startActivity(goToPostEvent);
			if ((Button)v == ChooseEvent) startActivity(goToChooseEvent);
			if ((Button)v == PreviosEvents) startActivity(goToPreviousEvents);
		}
	}


}
