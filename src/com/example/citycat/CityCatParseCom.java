package com.example.citycat;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class CityCatParseCom {

	Context activity;
	protected ArrayList<String> typesList;
	protected ArrayList<String> categoryList;
	protected ArrayList<String> eventList;

	public CityCatParseCom(Context contxt){
		activity = contxt;
		Parse.initialize(activity, "sN3Uhl2rCCJvp1rodg9hYqw9pZN8kVkYuPCCwn5D", "ECprIUSxorEFhSSzq7ani1dR7Up4gApnjAmPFjiY");
	}
	
	/* post an event */
	public void postEvent(ParseObject event){
		event.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {			    	
				// success
				if (e == null) {
					Intent goToMain = new Intent(activity, MainActivity.class);
					AppAlertDialog.showNeutraAlertDialog(activity, "Post Event", "Success!\nYou've posted an event", goToMain);

				} // failure to save object in parse.com
				else {
					AppAlertDialog.showNeutraAlertDialog(activity, "Post Event", "Ooops! Something went wrong.\nplease try again.", null);
				}
			}			   
		});
	}
	
	/* Retrieve a list of all events categories from parse.com */
	public ArrayList<String> getCategoriesOnline(){
		ParseQuery queryCategory = new ParseQuery("Categories");
		categoryList = new ArrayList<String>();
		try {
			for (ParseObject obj : queryCategory.find()){
				categoryList.add(obj.getString("category").toString());
			}
		} catch (ParseException e) {}

		return categoryList;

	}
	
	/* Retrieve a list of all events types from parse.com */
	public ArrayList<String> getTypesOnline(){
		ParseQuery queryType = new ParseQuery("Types");
		typesList = new ArrayList<String>();
		try {
			for (ParseObject obj : queryType.find()){
				typesList.add(obj.getString("TypeName").toString());
			}
		} catch (ParseException e) {}

		return typesList;			
	}
	
	/* Retrieve a list of all events from parse.com (no criteria at all) */
	public ArrayList<String> getAllEvents(){
		ParseQuery queryEvent = new ParseQuery("Event");
		eventList = new ArrayList<String>();
		try {
			for (ParseObject obj : queryEvent.find()){
				Date date = obj.getDate("date");
				int Year = date.getYear()-100+2000;
				String dateFormat = date.getDate() + 
						"/" + date.getMonth() + 
						"/" + Year;
				eventList.add(obj.getString("name").toString()  + ", " + dateFormat);
			}
		} catch (ParseException e) {}

		return eventList;			
	}
	
	/* Retrieve a specific event from parse.com according to its name */
	public Intent getSpecigicEventByCriteria(String key, String value, boolean isUpdate){
		Intent ans = new Intent(activity, Details_Events.class);
		ParseQuery query = new ParseQuery("Event");
		// split the displayed name according to the fomar [name], [date]
		query.whereEqualTo(key, value.split(",")[0].trim()); 
		try{
			ParseObject e = query.find().get(0);
			ans.putExtra("name",e.getString("name"));
			ans.putExtra("category",e.getString("category"));
			ans.putExtra("type",e.getString("type"));
			ans.putExtra("description",e.getString("description"));
			ans.putExtra("city",e.getString("city"));
			// location
			ParseGeoPoint gps_parse = e.getParseGeoPoint("gps");
			double gpsLat = gps_parse.getLatitude();
			double gpsLng = gps_parse.getLongitude();
			ans.putExtra("gpsLat", gpsLat);
			ans.putExtra("gpsLng", gpsLng);
			// date & time
			Date date = e.getDate("date");
			int Year = date.getYear()-100+2000;
			int Month = date.getMonth()+1;
			String dateFormat = date.getDate() + 
					"/" + Month + 
					"/" + Year;
			ans.putExtra("dateFormat",dateFormat);
			String time = date.getHours() + ":" + date.getMinutes();
			ans.putExtra("time",time);
			// flag
			ans.putExtra("IsUserEvents", isUpdate);					
		
		} catch (Exception e) { }

		return ans;

	}
	
	/* Retrieve a list of all events from parse.com according to a given criteria (type/category) */
	public ArrayList<String> getAllEventByCriteria(String key, String value){
		ArrayList<String> ans = new ArrayList<String>();
		ParseQuery query = new ParseQuery("Event");
		query.whereEqualTo(key, value);
		try{
			for (ParseObject e : query.find()){
				Date date = e.getDate("date");
				int Year = date.getYear()-100+2000;
				String dateFormat = date.getDate() + 
						"/" + (date.getMonth() + 1) + 
						"/" + Year;
				ans.add(e.getString("name").toString() + ", " + dateFormat);
			}

		} catch (Exception e) {}

		//Log.d("CityCatParseCom",ans.size() + "");
		return ans;

	}
	
	/* delete a specific event from parse.com */
	public void deleteEvent(String eventName){	
		ParseQuery query = new ParseQuery("Event");
		
		query.whereEqualTo("name", eventName);
		try{
			ParseObject e = query.find().get(0);
			e.deleteInBackground();
			AppAlertDialog.showNeutraAlertDialog(activity, "Delete Event", "Success! This event was delete", null);
		} catch (Exception e) {}
	}
	
	/* Retrieve a list of all events from parse.com which are located next to the user location and happing today/tomorrow */
	public ArrayList<String> getNearByEvents(ParseGeoPoint point){
		ArrayList<String> ans = new ArrayList<String>();
		Date currentDate = new Date();
		ParseQuery query = new ParseQuery("Event");
		query.whereWithinKilometers("gps", point, 10);
		query.whereGreaterThan("date", currentDate);
		try{
			for (ParseObject e : query.find()){			
				Date eventDate = e.getDate("date");
				int today = Math.abs(currentDate.getDate() - eventDate.getDate());
				if ((currentDate.getYear() == eventDate.getYear()) 
						&& (currentDate.getMonth() == eventDate.getMonth())
						&& (today <= 1)){
							String dateFormat = eventDate.getDate() + 
									"/" + eventDate.getMonth();
							ans.add(e.getString("name").toString() + ", " + dateFormat  + "\n" + e.getString("city").toString());
				}
			}

		} catch (Exception e) {}

		return ans;
	}
	
	public static String[] getCategoriesSharedPref(Context cntxt){
		SharedPreferences ref = cntxt.getSharedPreferences("local_parseCom",0);
		return ref.getString("events_categories", "").split(";"); 
	}

	public static String[] getTypesSharedPref(Context cntxt){
		SharedPreferences ref = cntxt.getSharedPreferences("local_parseCom",0);
		return ref.getString("events_types", "").split(";");   	
	}



}