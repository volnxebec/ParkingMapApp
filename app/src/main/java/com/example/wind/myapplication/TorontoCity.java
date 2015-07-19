package com.example.wind.myapplication;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**public class ParsingJson extends Activity {

 //private ProgressDialog pDialog;



 // Hashmap for ListView
 HashMap<LatLng,ParkingLot> ParkingDataHash;

 @Override
 public void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 setContentView(R.layout.json_parsing);

 ParkingDataHash = new HashMap<LatLng, ParkingLot>();

 // Calling async task to get json
 new GetContacts().execute();
 }

 /**
  * Async task class to get json by making HTTP call
  * */

public class TorontoCity extends AsyncTask<HashMap, Void, Void> {

    // URL to get contacts JSON
    static String url = "http://www1.toronto.ca/City%20Of%20Toronto/Information%20&%20Technology/Open%20Data/Data%20Sets/Assets/Files/greenPParking2015.json";

    private static final String TAG_CARPARKS = "carparks";
    //private static final String TAG_ID = "id";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_LAT = "lat";
    private static final String TAG_lNG = "lng";
    private static final String TAG_RATE = "rate";
    private static final String TAG_RATE_HALF_HOUR = "rate_half_hour";
    private static final String TAG_CAPACITY = "capacity";
    private static final String TAG_MAX_HEIGHT = "max_height";
    //private static final String TAG_CARPARK_TYPE = "carpark_type";
    //private static final String TAG_CARPARK_TYPE_STR = "carpark_type_str";
    //private static final String TAG_IS_TTC = "is_ttc";
    //private static final String TAG_PAYMENT_METHODS = "payment_methods";
    //private static final String TAG_PAYMENT_OPTIONS = "payment_options";
    //private static final String TAG_RATE_DETAILS = "rate_details";
    //private static final String TAG_RATE_DETAILS_PERIODS = "periods";
    //private static final String TAG_RATE_DETAILS_PERIODS_TITLE = "title";
    //private static final String TAG_RATE_DETAILS_PERIODS_RATES = "rates";
    //private static final String TAG_RATE_DETAILS_PERIODS_RATES_WHEN = "when";
    //private static final String TAG_RATE_DETAILS_PERIODS_RATES_RATE = "rate";
    //private static final String TAG_RATE_DETAILS_PERIODS_NOTES = "notes";
    //private static final String TAG_RATE_DETAILS_ADDENDA = "addenda";
    //private static final String TAG_ENABLE_STREETVIEW = "enable_streetview";
    //private static final String TAG_STREETVIEW_LAT = "streetview_lat";
    //private static final String TAG_STREETVIEW_LONG = "streetview_long";
    //private static final String TAG_STREETVIEW_YAW = "streetview_yaw";
    //private static final String TAG_STREETVIEW_PITCH = "streetview_pitch";
    //private static final String TAG_STREETVIEW_ZOOM = "streetview_zoom";

    // parkings JSONArray
    JSONArray parkings = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(HashMap... arg0) {
        // Creating service handler class instance
        ServiceHandler sh = new ServiceHandler();

        // Making a request to url and getting response
        String jsonStr = new String ();
        try {
            jsonStr = sh.makeServiceCall(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("Response: ", "> " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                parkings = jsonObj.getJSONArray(TAG_CARPARKS);

                // looping through All Parkings
                for (int i = 0; i < parkings.length(); i++) {
                    double latitude = 0.0d,longitude = 0.0d;
                    //Get the general Parking Information
                    JSONObject c = parkings.getJSONObject(i);
                    //String id = c.getString(TAG_ID);
                    String address = c.getString(TAG_ADDRESS);
                    String lat = c.getString(TAG_LAT);
                    String lng = c.getString(TAG_lNG);
                    latitude = Double.parseDouble(lat);
                    longitude = Double.parseDouble(lng);
                    String rate_half_hour = c.getString(TAG_RATE_HALF_HOUR);
                    String capacity = c.getString(TAG_CAPACITY);
                    String max_height = c.getString(TAG_MAX_HEIGHT);
                    // tmp hashmap for single contact
                    ParkingLot Contact = new ParkingLot(address,rate_half_hour,capacity,max_height);
                    //Putting Parking info to hashmap
                    arg0[0].put(new LatLng(latitude,longitude),Contact);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        //Log.d("Parkings",arg0[0].toString());
        return null;
    }

    //@Override
    protected void onPostExecute() {
        /**new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
        /* Create an Intent that will start the Menu-Activity.
        Intent mainIntent = new Intent(ParsingJson.this, MapsActivity.class);
        ParsingJson.this.startActivity(mainIntent);
        ParsingJson.this.finish();
        }
        }, SPLASH_DISPLAY_LENGTH);
         **/
    }
}
