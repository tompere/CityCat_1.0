package com.example.citycat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ListEventsByType extends Activity {
	ListView list;
	ArrayList<String> ListEventNames;
	Context thisContext;
	ArrayList<String> ListEvents;
	ArrayList<String> ListType;
	Spinner spinner;
	ArrayAdapter<String> adapter;
	CityCatParseCom parseCom;
	String selectedType;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_event_type);
		list = (ListView) findViewById(R.id.list_events_type);
		spinner = (Spinner) findViewById(R.id.spinner_type);
		thisContext = this;
		
		//Checking Network problems.this Activity works only if the Network is fine
		ConnectivityManager connMgr = (ConnectivityManager) 
				getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


		if (networkInfo != null && networkInfo.isConnected()) {
			// initialize parse object
			parseCom = new CityCatParseCom(this);

			// retrieve events types from shared prefernces and set into spinner
			ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
					CityCatParseCom.getTypesSharedPref(this));
			adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapterSpinner);				
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view, int arg2, long pos) {		
					selectedType = parent.getItemAtPosition((int)pos).toString();
					ListEvents = parseCom.getAllEventByCriteria("type",selectedType);					
					adapter = new ArrayAdapter<String>(thisContext, android.R.layout.simple_list_item_1, ListEvents);						
					list.setAdapter(adapter);
					// listener on events list - on click go to specific event screen
					list.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick (AdapterView<?> parent, View v, int position,
								long id) {
							String item = (String) adapter.getItem(position);
							Intent EventActivity = parseCom.getSpecigicEventByCriteria("name",item,false);
							startActivity(EventActivity);
						}
					});

				}

				public void onNothingSelected(AdapterView<?> arg0) {}

			});
		}

		else {
			AppAlertDialog.showNeutraAlertDialog(thisContext, "No NetWork Connection", 
					"This Activity can not be displayed as a result of NetWork Problems", null);
		}
	}

	protected void onStart(){
		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list_event, menu);
		return true;
	}

}
