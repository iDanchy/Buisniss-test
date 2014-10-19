///////////////////////////////////
//klasa za dobivanje GPS lkacije//
/////////////////////////////////
package com.example.testapp;

import android.provider.Settings;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;

public class TrackerGPS extends Service implements LocationListener{
	private static final long MIN_DISTANCE_UPDATE=10;
	private static final long MIN_TIME_UPDATE=1000*60*1;
	
	private boolean isGPSEnabeled=false;
	private boolean isNetworkEnabeled=false;
	private boolean canGetLocation=false;
	private double latitude,longditude;
	private static Context context;
	private Location location;
	
	
	protected LocationManager locationManager;
	
	
	public TrackerGPS(Context cont){
		this.context=cont;
		getLocation();
	}
	
	public Location getLocation(){
		try{
			
			locationManager=(LocationManager)context.getSystemService(LOCATION_SERVICE);
			isGPSEnabeled=locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
			isNetworkEnabeled=locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER);
			if(!isGPSEnabeled && !isNetworkEnabeled){
				
			}else{
				this.canGetLocation=true;
				if(isNetworkEnabeled){
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
																			MIN_TIME_UPDATE,
																			MIN_DISTANCE_UPDATE,this);
					if(locationManager!=null){
						location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if(location!=null){
							latitude=location.getLatitude();
							longditude=location.getLongitude();
						}
					}
				}
				if(isGPSEnabeled){
					if(location==null){
						locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
																				MIN_TIME_UPDATE,
																				MIN_DISTANCE_UPDATE,this);
						if(locationManager != null){
							location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if(locationManager!=null){
								latitude=location.getLatitude();
								longditude=location.getLongitude();
							}
						}
					}
				}	
		   }			
		}catch(Exception e){
			e.printStackTrace();
		}
		return location;
		
	}
	
	public void stopUsingGPS(){
		if(locationManager!=null){
			locationManager.removeUpdates(TrackerGPS.this);
		}
	}
	public double getLatitude(){
		if(location != null){
			latitude=location.getLatitude();
		}
		return latitude;
	}
	public double getLongitude(){
		if(locationManager!=null){
			longditude=location.getLongitude();
		}
		return longditude;
	}
	public boolean canGetLocation(){
		return this.canGetLocation;
	}
	public void showSettingsAlert(){
		AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
		alertDialog.setTitle("GPS settings");
		alertDialog.setMessage("GPS is not enabeled, Do you want to go to settings menu?");
		alertDialog.setPositiveButton("Settings",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				context.startActivity(intent);
				
			}
		});
		alertDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				
			}
			
		});
		alertDialog.show();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
