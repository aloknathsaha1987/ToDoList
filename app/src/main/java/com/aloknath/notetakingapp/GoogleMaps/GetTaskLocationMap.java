package com.aloknath.notetakingapp.GoogleMaps;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.aloknath.notetakingapp.Json.JSONParser;
import com.aloknath.notetakingapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALOKNATH on 2/17/2015.
 */
public class GetTaskLocationMap extends Activity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

    GoogleMap mMap;
    Marker marker;
    LocationClient mLocationClient;
    boolean mShowMap;
    private double dstlatitude;
    private double dstlongitude;
    private double latitude;
    private double longitude;
    private String location;
    private String locationCountry;
    private static final float DEFAULTZOOM = 15;
    private static final int GPS_ERRORDIALOG_REQUEST = 9001;
    ArrayList<Marker> markers = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = this.getIntent();
        location = intent.getStringExtra("location");

        if (servicesOK()) {
            setContentView(R.layout.display_google_map);

            if (initMap()) {

                LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    mLocationClient = new LocationClient(GetTaskLocationMap.this, GetTaskLocationMap.this, GetTaskLocationMap.this);
                    mLocationClient.connect();
                    mShowMap = true;
                }else{
                    Toast.makeText(this, "Location Manager Not Available", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Map not available!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void geoLocate(String destination){
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> listAddress = geocoder.getFromLocationName(destination, 1);
            Address add = listAddress.get(0);
            dstlatitude = add.getLatitude();
            dstlongitude = add.getLongitude();
            locationCountry = add.getCountryName();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void drawPath(String result) {
        try{
            // Transform the String to a JSON Object
            final JSONObject jsonObject = new JSONObject(result);
            JSONArray routeArray = jsonObject.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolyLines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolyLines.getString("points");

            List<LatLng> list = decodePoly(encodedString);

            for(int z =0; z < list.size() -1; z++){
                LatLng src = list.get(z);
                LatLng dst = list.get(z+1);
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude))
                        .add(new LatLng(dst.latitude, dst.longitude))
                        .width(5)
                        .color(Color.BLUE)
                        .geodesic(true));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index =0;
        int len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len){
            int b, shift =0, result =0;
            do{
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result>>1) : (result >> 1));

            lat += dlat;

            shift =0;
            result =0;
            do{
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result>>1) : (result >> 1));

            lng += dlng;

            LatLng latLng = new LatLng((((double)lat / 1E5)), (((double)lng / 1E5)));
            poly.add(latLng);

        }
        return poly;
    }

    private class connectAsyncTask extends AsyncTask<Void, Void, String>{

        private ProgressDialog progressDialog;
        String url;
        connectAsyncTask(String urlPass){
            url = urlPass;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(GetTaskLocationMap.this);
            progressDialog.setMessage("Fetching route from your location to the Task's Location");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            JSONParser jsonParser = new JSONParser();
            String json = jsonParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            if(result != null){
                drawPath(result);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return false;
    }

    public boolean servicesOK() {
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        }
        else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERRORDIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this, "Can't connect to Google Play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean initMap() {
        if(mMap == null){
            MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
            mMap = mapFragment.getMap();
        }
        return (mMap != null);
    }

    @Override
    public void onConnected(Bundle bundle) {
//        try {
        Location mLocation = mLocationClient.getLastLocation();
        if(mLocation == null){
            Toast.makeText(this, "My Location is not available", Toast.LENGTH_SHORT).show();
        }else {
            geoLocate(location);
            connectAsyncTask newAsyncTask = new connectAsyncTask(makeURL(mLocation.getLatitude(), mLocation.getLongitude(), dstlatitude, dstlongitude));
            newAsyncTask.execute();
            try {
                gotoCurrentLocation();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            //displayMyLocation();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void displayMyLocation() throws IOException {

        if(mShowMap){
            if(mLocationClient.isConnected()) {
                Location mLocation = mLocationClient.getLastLocation();
                if (mLocation == null) {
                    Toast.makeText(this, "My Location is not available", Toast.LENGTH_LONG).show();
                } else {
                    gotoCurrentLocation();
                    Toast.makeText(this, "I'm Here", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this, "LocationClient is Not Connected", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void gotoCurrentLocation() throws IOException {

        Location mLocation = mLocationClient.getLastLocation();
        if(mLocation == null){
            Toast.makeText(this, "My Location is not available", Toast.LENGTH_SHORT).show();
        }else{
            LatLng latLng = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULTZOOM);
            mMap.animateCamera(cameraUpdate);
            Geocoder gc = new Geocoder(this);
            List<Address> list = gc.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(),1);
            Address add = list.get(0);
            String locality = "This is Me";
            String country = add.getCountryName();
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
            setMarker(country, locality, mLocation.getLatitude(),mLocation.getLongitude());
            setMarker(locationCountry, location,dstlatitude, dstlongitude );
        }
    }

    public void setMarker(String country, String locality, double lat, double lng) {
        if(markers.size() == 2){
            removeEverything();
        }
        MarkerOptions markerOptions = new MarkerOptions()
                .title(locality)
                .position(new LatLng(lat, lng))
                .anchor(.5f, .5f)
                .icon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_CYAN
                ))
                .draggable(true);
        if (country.length() > 0) {
            markerOptions.snippet(country);
        }
        markers.add(mMap.addMarker(markerOptions));
    }

    private void removeEverything() {

        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this,"Disconnected from the location services", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = "Location" + location.getLatitude() + "," + location.getLongitude();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"Connection the location services Failed", Toast.LENGTH_SHORT).show();
    }

    // Get Directions Between two points

    public String makeURL(double sourceLat, double sourceLng, double destLat, double destLng){
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");
        urlString.append(Double.toString(sourceLat));
        urlString.append(",");
        urlString.append(Double.toString(sourceLng));
        urlString.append("&destination=");
        urlString.append(Double.toString(destLat));
        urlString.append(",");
        urlString.append(Double.toString(destLng));
        urlString.append("&sensor=false&mode=driving&alternatives=true");

        return urlString.toString();


    }
}
