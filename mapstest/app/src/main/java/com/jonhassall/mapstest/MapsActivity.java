package com.jonhassall.mapstest;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity {

    //http://www.firstdroid.com/2010/04/29/android-development-using-gps-to-get-current-location-2/
    
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        /* Use the LocationManager class to obtain GPS locations */
        //Context context = getContext();

        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener mlocListener = new MyLocationListener();
        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        final LatLng HOLLYWOOD = new LatLng(34.0937508, -118.3264781);
        mMap.addMarker(new MarkerOptions().position(HOLLYWOOD).title("Hollywood, CA"));

        final LatLng HOLLYWOOD2 = new LatLng(35.0937508, -117.3264781);
        MarkerOptions myMarker = new MarkerOptions();
        //Set marker position
        myMarker.position(HOLLYWOOD2);
        //Set marker title
        myMarker.title("Hollywood 2");
        //Set marker colour
        myMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        //Set opacity
        myMarker.alpha(0.5f);
        //Set marker to be flat
        myMarker.flat(true);

        myMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ntu_64px));

        mMap.addMarker(myMarker);

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);

        mMap.setOnMarkerClickListener(omclistener);

        //Make a polygon on map
        //Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(35, -117))
                .add(new LatLng(37, -117))
                .add(new LatLng(37, -115))
                .add(new LatLng(35, -115))
                .add(new LatLng(35, -117)); // Closes the polyline.

        Polyline polyline = mMap.addPolyline(rectOptions);

        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(36, -116))
                .radius(1000); // In meters

        // Get back the mutable Circle
        Circle circle = mMap.addCircle(circleOptions);
    }

    GoogleMap.OnMarkerClickListener omclistener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Log.d("Marker", "Marker pressed. Title: " + marker.getTitle() );
            return false;
        }
    };
}
