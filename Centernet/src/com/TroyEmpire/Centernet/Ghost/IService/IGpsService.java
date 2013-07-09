package com.TroyEmpire.Centernet.Ghost.IService;

import com.TroyEmpire.Centernet.Entity.LocationPosition;

import android.location.Location;

public interface IGpsService {
	public double getCurrentLatitute();

	public double getCurrentLongitude();

	public Location getCurrentLocation();

	public LocationPosition getLocationPosition();

	public String getProvider();
}
