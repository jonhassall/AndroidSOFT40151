package com.jonhassall.mapstest;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/* Class My Location Listener */
public class MyLocationListener implements LocationListener
{
    @Override
    public void onProviderDisabled(String provider)
    {
        Log.d("MyLocationListener", "GPS Disabled");
    }

    @Override
    public void onLocationChanged(Location loc) {
        loc.getLatitude();
        loc.getLongitude();
        String Text = "My current location is: " +
                "Latitude = " + loc.getLatitude() +
                "Longitude = " + loc.getLongitude();
        Log.d("MyLocationListener", Text);
    }

    @Override
    public void onProviderEnabled(String provider)
    {
        Log.d("MyLocationListener", "GPS Enabled");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {


    }

}/* End of Class MyLocationListener */