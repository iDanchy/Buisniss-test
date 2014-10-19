package com.example.testapp;

import java.io.Serializable;

public class Coordinats implements Serializable {
	private static final long serialVersionUID = 1L;
	private double latitude,longitude;
	
	public Coordinats(double latitude, double longitude){
		this.latitude=latitude;
		this.longitude=longitude;
		
	}
	  @Override
	  public String toString() {
		  return ""+latitude+"\n"+longitude+"\n";
	  }

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
