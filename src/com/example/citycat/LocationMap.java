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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationMap extends Activity {

	MapView mapView;
	GoogleMap googleMap;
	Intent goToPostEvent;
	Button btnSumbit;
	Button refresh;
	LatLng userLocation;
	Context chooseLocationContext;
	String city;
	Location gpsLocation;
	Location networkLocation;
	LocationManager locationManager;
	Marker currentMarker;
	int mapMode;
	double gpsLatitude;
	double gpsLongtitude;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_location);

		chooseLocationContext = this;

		mapMode = 0;
		gpsLatitude = getIntent().getDoubleExtra("gpsLatitude", -1.0);
		gpsLongtitude = getIntent().getDoubleExtra("gpsLongtitude", -1.0);
		if (gpsLatitude > 0 && gpsLongtitude > 0){
			mapMode = 1;
		}

		ClickHandler clickHandler = new ClickHandler();
		refresh = (Button)this.findViewById(R.id.refresh_button);
		btnSumbit = (Button)this.findViewById(R.id.btn_sumbit_location);	
		refresh.setOnClickListener(clickHandler);

		// Google map initialization */
		mapView = (MapView)findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		googleMap = mapView.getMap();
		// set listener for user long clicks
		googleMap.setOnMapLongClickListener(new LongClickHandler());

		if (mapMode == 0){ // in case mode is PostEvent

			btnSumbit.setOnClickListener(clickHandler);
			goToPostEvent = new Intent(this, PostEvent.class);

			// get current user location based on GPS or network connection
			locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			// make sure there's an available way to get position 
			if (!gpsEnabled && !networkEnabled) {
				String msg = "Your GPS and Network aren't working.\nPlease turn on one of them and refresh the map in order to determine your location.";
				AppAlertDialog.showNeutraAlertDialog(this, "Location/Map Issue", msg, null);
			}

			// get position according to GPS
			if (gpsEnabled && gpsLocation != null) {
				gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				userLocation = new LatLng(gpsLocation.getLatitude(), gpsLocation.getLongitude());			
			}

			// get location according to network
			else {
				networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);			
				userLocation = new LatLng(networkLocation.getLatitude(), networkLocation.getLongitude());
			}

			currentMarker = addMarkerToMap(userLocation);

			// zoom in on location coordinate
			zoomInMap((float)18.0, userLocation);
		}

		else if (mapMode == 1) { // in case mode is view specific event location		
			// hide submit button
			btnSumbit.setVisibility(View.INVISIBLE);

			// add location of event on map
			currentMarker = addMarkerToMap(new LatLng(gpsLatitude, gpsLongtitude));		

			// zoom in on location coordinate
			zoomInMap((float)18.0, new LatLng(gpsLatitude, gpsLongtitude));
		}		
	}

	/* Internal function to handle adding markers to map */
	public Marker addMarkerToMap(LatLng latlng){
		// get city (by name) according to GPS/Network coordinate, and add marker
		String address = "";
		int i = 0;
		while (i < 3){
			try {
				Geocoder gcd = new Geocoder(chooseLocationContext);
				List<Address> addresses = gcd.getFromLocation(latlng.latitude, latlng.longitude, 1);
				city = addresses.get(0).getLocality();
				if (addresses.get(0).getMaxAddressLineIndex() > -1)
				{
					address = addresses.get(0).getAddressLine(0) + "; ";
				}
				i = 3;
				address = address + city + "; " + addresses.get(0).getCountryName();

			} catch (Exception e) {
				city = "[N/A]";
				if (i == 2) address = "Not Available...";
				else i++;
			}	
		}
		
		return googleMap.addMarker(new MarkerOptions().position(latlng).title(address));
	}

	/* Internal handle to general events of clicks */
	class ClickHandler implements View.OnClickListener {
		public void onClick(View v)
		{
			if ((Button)v == refresh) onRestart();

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

	/* Internal class to handle events of long click on map */
	class LongClickHandler implements GoogleMap.OnMapLongClickListener{
		public void onMapLongClick(LatLng point) {
			// allow moving only in case of postEvent
			if (mapMode == 0){
				currentMarker.remove();
				currentMarker = addMarkerToMap(point);	
			}
		}
	}

	/* Internal function to perform zoom in on map */
	private void zoomInMap(float range, LatLng coordinate){
		// zoom in
		try {
			MapsInitializer.initialize(this);
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, range));
			//googleMap.animateCamera(CameraUpdateFactory.zoomTo(range)); 
		}
		catch (Exception e) {
			Log.d("LocationMap", e.getMessage());
		}
	}

	protected void onRestart() {
		super.onRestart();
		Intent i = new Intent(this, LocationMap.class);
		if (mapMode == 1){
			i.putExtra("gpsLatitude", gpsLatitude);
			i.putExtra("gpsLongtitude", gpsLongtitude);
		}
		startActivity(i);
		finish();

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_choose_location, menu);
		return true;
	}

	public void onResume(){
		super.onResume();
		mapView.onResume();
	}

	public void onPause(){
		super.onPause();
		mapView.onPause();
	}

	public void onDestroy(){
		super.onDestroy();
		mapView.onDestroy();
	}

}