package com.example.citycat;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ChooseLocation extends Activity {

	MapView mapView;
	GoogleMap googleMap;
	Intent goToPostEvent;
	Button btnSumbit;
	LatLng userLocation;
	Context chooseLocationContext;
	String city;
	Location gpsLocation;
	Location networkLocation;
	LocationManager locationManager;
	Marker currentMarker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_location);

		chooseLocationContext = this;

		btnSumbit = (Button)this.findViewById(R.id.btn_sumbit_location);
		ClickHandler clickHandler = new ClickHandler();
		btnSumbit.setOnClickListener(clickHandler);
		
		goToPostEvent = new Intent(this, PostEvent.class);
		
		// Google map initialization */
		mapView = (MapView)findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		googleMap = mapView.getMap();   
		
		// zoom in
		try {
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(new Float(3.0))); 
		}
		catch (Exception e) {
			// do nothing
		}
				
		// set listener for user long clicks
		googleMap.setOnMapLongClickListener(new LongClickHandler());

		// get current user location based on GPS or network connection
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		final boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		// make sure there's an available way to get position 
		if (!gpsEnabled && !networkEnabled) {
			enableLocationSettings();
		}
		else {		
			// get position according to GPS
			if (gpsEnabled) userLocation = new LatLng(gpsLocation.getLatitude(), gpsLocation.getLongitude());			
			// get location according to network
			else userLocation = new LatLng(networkLocation.getLatitude(), networkLocation.getLongitude());
			currentMarker = addMarkerToMap(userLocation);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_choose_location, menu);
		return true;
	}

	public void onResume(){
		super.onResume();
		mapView.onResume();
	}

	private void enableLocationSettings() {
		Context context = getApplicationContext();
		CharSequence text = "Your GPS and Network aren't working.\nPlease turn on one of them in order to determine your location.";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		/*
    	Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    	startActivity(settingsIntent);
    	*/
	}

	public void onPause(){
		super.onPause();
		mapView.onPause();
	}

	public void onDestroy(){
		super.onDestroy();
		mapView.onDestroy();
	}

	/* Internal function to handle adding markers to map */
	public Marker addMarkerToMap(LatLng latlng){
		// get city (by name) according to GPS/Network coordinate, and add marker
		try {
			Geocoder gcd = new Geocoder(chooseLocationContext);
			List<Address> addresses = gcd.getFromLocation(latlng.latitude, latlng.longitude, 1);
			city = addresses.get(0).getLocality();
			String address = "";
			if (addresses.get(0).getMaxAddressLineIndex() > -1)
			{
				address = addresses.get(0).getAddressLine(0) + "; ";
			}
			return googleMap.addMarker(new MarkerOptions().position(latlng).title(address + city + "; " + addresses.get(0).getCountryName()));

		} catch (Exception e) {
			city = "[N/A]";
			return googleMap.addMarker(new MarkerOptions().position(latlng).title("Not Available..."));
		}
	}

	/* handle general events of clicks */
	class ClickHandler implements View.OnClickListener {
		public void onClick(View v)
		{
			if ((Button)v == btnSumbit)
			{				
				goToPostEvent.removeExtra("lat");
				goToPostEvent.removeExtra("lng");
				goToPostEvent.removeExtra("city");
				goToPostEvent.putExtra("lat",currentMarker.getPosition().latitude);
				goToPostEvent.putExtra("lng",currentMarker.getPosition().longitude);
				goToPostEvent.putExtra("city",city);
				
				startActivity(goToPostEvent);
			}
		}
	}

	/* handle events of long click on map */
	class LongClickHandler implements GoogleMap.OnMapLongClickListener{
		public void onMapLongClick(LatLng point) {
			currentMarker.remove();
			currentMarker = addMarkerToMap(point);
		}
	}
}
