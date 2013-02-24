package com.example.citycat;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.parse.ParseGeoPoint;

public class MainActivity extends Activity {

	Button postEvent;
	Button  ChooseEvent;
	Intent goToPostEvent;
	Intent goToChooseEvent;
	Intent goToPreviousEvents;
	TextView PreviosEvents;
	Context context;
	ArrayList<String> hotEvents;
	TextSwitcher mSwitcher;
	CityCatParseCom parseCom;
	int switchCounter = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		PreviosEvents = (Button) this.findViewById(R.id.YourEvent);
		postEvent = (Button) this.findViewById(R.id.location);
		ChooseEvent = (Button) this.findViewById(R.id.ChooseEvent);
		goToPostEvent = new Intent(context, LocationMap.class);   
		goToChooseEvent = new Intent(context, TabsEvents.class);
		goToPreviousEvents = new Intent(context, ListPreviosEvents.class);

		/* retrieve events types and categories */
		parseCom = new CityCatParseCom(context);
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
		
		// get current user location based on GPS or network connection
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		// make sure there's an available way to get position 
		if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			String msg = "Your Network is't working.\nPlease turn it on.";
			AppAlertDialog.showNeutraAlertDialog(this, "Location/Map Issue", msg, null);
		}
		else {
			Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);			
			ParseGeoPoint userLocation = new ParseGeoPoint(networkLocation.getLatitude(), networkLocation.getLongitude());
			hotEvents = parseCom.getNearByEvents(userLocation);
			mSwitcher = (TextSwitcher) findViewById(R.id.hotEventsSwitcher);
			Animation in = AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
			Animation out = AnimationUtils.loadAnimation(this,android.R.anim.fade_out);
			mSwitcher.setInAnimation(in);
			mSwitcher.setOutAnimation(out);
			// set click listener for switching activities
			mSwitcher.setOnClickListener(new OnClickListener() {		
				public void onClick(View v) {
					switchCounter++;
					if (hotEvents.size() == 0) mSwitcher.setCurrentText("No Hot Events...");
					else mSwitcher.setCurrentText(hotEvents.get(hotEventCounter()));
				}
			});
			// set long click listener for seeing event details
			mSwitcher.setOnLongClickListener(new OnLongClickListener() {		
				public boolean onLongClick(View arg0) {
					if (hotEvents.size() == 0) return false;
					String eventName = hotEvents.get(hotEventCounter()).split(", ")[0];
					startActivity(parseCom.getSpecigicEventByCriteria("name",eventName,false));
					return false;
				}
			});
		
			clickHandler click = new clickHandler();
			postEvent.setOnClickListener(click);
			ChooseEvent.setOnClickListener(click);
			PreviosEvents.setOnClickListener(click);	
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	/* internal function to track over current hot Event presented/clicked */
	private int hotEventCounter(){
		return switchCounter % Math.min(hotEvents.size(), 5);	
	}
	
	/* override back button, so it will be disabled */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    return false;    
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
