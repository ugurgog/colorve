package com.example.colorve.model;

import java.io.Serializable;

public class LocationModel implements Serializable{

	private static final long serialVersionUID = -5305037032732004883L;
	
	private double longitude;
	private double latitude;
	
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
}
