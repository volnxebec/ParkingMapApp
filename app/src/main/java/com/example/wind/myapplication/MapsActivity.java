package com.example.wind.myapplication;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;

import java.io.IOException;
import java.util.List;

import com.google.maps.android.SphericalUtil;

public class MapsActivity extends AppCompatActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private UiSettings mUiSettings;

    //Temporary Toronto LatLng
    LatLng torontoCoordinates;

    //Temporary variable for Storing Toronto Coordinates
    double centerTOLat = 43.7;
    double centerTOLng = -79.4;

    //Bounding Box for Geocoder Search
    double lowerLeftTOLat = 43.58;
    double lowerLeftTOLng = -79.55;
    double upperRightTOLat = 43.95;
    double upperRightTOLng = -79.25;

    //Current search Marker handle...
    Marker searchMarker;

    //Alert dialog
    final Context context = this;
    AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        setUpBadAddressAlertIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        setUpBadAddressAlertIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchViewAction = (SearchView) MenuItemCompat
                .getActionView(searchMenuItem);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager
                .getSearchableInfo(getComponentName());
        searchViewAction.setSearchableInfo(searchableInfo);
        searchViewAction.setIconifiedByDefault(false);

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    //Handle different intents...
    private void handleIntent(Intent intent) {

        //Handle Search Intent...
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            doLocationSearch(query);
        }
    }

    //Search up the LatLng of the input address string (within Toronto)
    //Place a marker and zoom in to that location...
    private void doLocationSearch(String address) {
        LatLng zoomAddress = getLocationFromAddress(address);
        if (zoomAddress != null) {
            if (searchMarker != null) searchMarker.remove();
            searchMarker = mMap.addMarker(new MarkerOptions().position(zoomAddress).title(address));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(zoomAddress, 15.0f));
        }
        else {
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();

            centerMapOnCity();
        }
    }

    //Search the string address using Geocoder API
    private LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        Address location;
        double old_distance, new_distance;
        LatLng p1 = null;
        LatLng p2 = null;

        old_distance = Double.POSITIVE_INFINITY;

        try {
            address = coder.getFromLocationName(strAddress, 5,
                                                lowerLeftTOLat, lowerLeftTOLng,
                                                upperRightTOLat, upperRightTOLng);
            if (address.isEmpty()) {
                return null;
            }

            //Choose the address that is closest to current selected city
            for (int i=0; i<address.size(); i++) {
                location = address.get(i);
                p1 = new LatLng((location.getLatitude()),(location.getLongitude()));
                new_distance = SphericalUtil.computeDistanceBetween(torontoCoordinates, p1);
                if (new_distance<old_distance) {
                    old_distance = new_distance;
                    p2 = p1;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return p2;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
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
        mUiSettings = mMap.getUiSettings();
        centerMapOnCity();
        mMap.setMyLocationEnabled(true);
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
    }

    private void centerMapOnCity() {
        //Zoom into desired City location at map Startup
        torontoCoordinates = new LatLng(centerTOLat,centerTOLng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(torontoCoordinates, 12.0f));
    }

    private void setUpBadAddressAlertIfNeeded() {

        if (alertDialogBuilder == null ) {
            alertDialogBuilder = new AlertDialog.Builder(context);
            // set title
            alertDialogBuilder.setTitle("Bad Address :(");
            alertDialogBuilder
                    .setMessage("Click OK to go back to Map and perform a new Search!")
                    .setCancelable(false)
                    .setNegativeButton("OK!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });
        }
    }

}
