package com.conventry.university.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.conventry.university.directions.DirectionsJSONParser;
import com.conventry.university.R;
import com.conventry.university.utils.AppConstants;
import com.conventry.university.utils.AppUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

public class CampusMapActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnTouchListener {

    private GoogleMap mMap;

    private GoogleApiClient googleApiClient;

    private final int PUBLIC_STORAGE_PERMISSION_CONSTANT = 100;

    private Geocoder geocoder;

    private EditText sourceEditText,destinationEditText;

    private int PLACE_AUTOCOMPLETE_REQUEST_CODE_ONE = 1;

    private int PLACE_AUTOCOMPLETE_REQUEST_CODE_TWO=2;

    private Button findPath,showHide;

    private PolylineOptions lineOptions = null;

    private ArrayList<LatLng> poly;

    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        findPath= (Button) findViewById(R.id.findPath);
        showHide= (Button) findViewById(R.id.hideShow);
        sourceEditText=(EditText)findViewById(R.id.sourceLocation);
        destinationEditText=(EditText)findViewById(R.id.destLocation);
        sourceEditText.setOnTouchListener(this);
        destinationEditText.setOnTouchListener(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getPermissions();
        }
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }


    public void getPermissions() {
        if (ActivityCompat.checkSelfPermission(CampusMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CampusMapActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PUBLIC_STORAGE_PERMISSION_CONSTANT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PUBLIC_STORAGE_PERMISSION_CONSTANT:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                } else {
                    Toast.makeText(CampusMapActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        LatLng conventryLatLng=new LatLng(52.407220, -1.504068);
        CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(conventryLatLng,15);
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location==null){
            Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    public void drawPolyLine(LatLng sourceLatLng,LatLng destLatLng) {
        String url = getDirectionsUrl(sourceLatLng, destLatLng);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
    }

    public void hideShow(View view) {
        showHide.setVisibility(View.GONE);
        sourceEditText.setVisibility(View.VISIBLE);
        destinationEditText.setVisibility(View.VISIBLE);
        findPath.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId()==R.id.sourceLocation && event.getAction()== MotionEvent.ACTION_DOWN){
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_ONE);
            } catch (GooglePlayServicesRepairableException e) {

            } catch (GooglePlayServicesNotAvailableException e) {
            }

        }

        else if(v.getId()==R.id.destLocation && event.getAction()== MotionEvent.ACTION_DOWN){
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_TWO);
            } catch (GooglePlayServicesRepairableException e) {

            } catch (GooglePlayServicesNotAvailableException e) {
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PLACE_AUTOCOMPLETE_REQUEST_CODE_ONE){
            if(resultCode==RESULT_OK){
                Place place=PlaceAutocomplete.getPlace(this,data);
                sourceEditText.setText(place.getName());
            }
        }

        else if(requestCode==PLACE_AUTOCOMPLETE_REQUEST_CODE_TWO ){
            if(resultCode==RESULT_OK){
                Place place=PlaceAutocomplete.getPlace(this,data);
                destinationEditText.setText(place.getName());
            }
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                AppUtils.doLogError(AppConstants.ERROR_TAG, e.getMessage());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                AppUtils.doLogError(AppConstants.ERROR_TAG, e.getMessage());
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points=new ArrayList();
            try {
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();
                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);
                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }
                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(12);
                    lineOptions.color(getColor(R.color.colorPrimary));
                }
                mMap.addPolyline(lineOptions);
            }catch (Exception e){
                AppUtils.doLogError(AppConstants.ERROR_TAG, e.getMessage());
            }
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb  = new StringBuffer();
            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        }catch(Exception e){
            AppUtils.doLogError(AppConstants.ERROR_TAG, e.getMessage());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public void goToLocation(View view) {
        try {
            if(mMap!=null) {
                mMap.clear();
            }
            geocoder=new Geocoder(getApplicationContext());
            List<Address> sourceAddressList=geocoder.getFromLocationName(sourceEditText.getText().toString(),1);
            if (sourceAddressList.size()==0){
                Toast.makeText(this, "Source not found", Toast.LENGTH_SHORT).show();
                return;
            }
            Address sourceAddress=sourceAddressList.get(0);
            LatLng sourceLatLng=new LatLng(sourceAddress.getLatitude(),sourceAddress.getLongitude());
            List<Address> destinationAddressList=geocoder.getFromLocationName(destinationEditText.getText().toString(),1);
            if (destinationAddressList.size()==0){
                Toast.makeText(this, "Destination not found", Toast.LENGTH_SHORT).show();
                return;
            }
            Address destinationAddress=destinationAddressList.get(0);
            LatLng destLatLng=new LatLng(destinationAddress.getLatitude(),destinationAddress.getLongitude());;
            poly=new ArrayList<>();
            poly.add(new LatLng(52.413530, -1.498692));
            poly.add(new LatLng(52.403580, -1.490576));
            poly.add(new LatLng(52.400472, -1.516160));
            poly.add(new LatLng(52.405699, -1.527196));
            poly.add(new LatLng(52.411042, -1.524225));
            poly.add(new LatLng(52.415407, -1.515254));
            poly.add(new LatLng(52.415407, -1.503426));
            if(! com.google.maps.android.PolyUtil.containsLocation(sourceLatLng,poly,true)){
                Toast.makeText(this, "Source outside campus", Toast.LENGTH_SHORT).show();
                return;
            }

            else if( !com.google.maps.android.PolyUtil.containsLocation(destLatLng,poly,true)){
                Toast.makeText(this, "Destination outside campus", Toast.LENGTH_SHORT).show();
                return;
            }
            drawPolyLine(sourceLatLng,destLatLng);
            CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(sourceLatLng,15);
            mMap.animateCamera(cameraUpdate);
            MarkerOptions sourceMarkerOptions=new MarkerOptions().position(sourceLatLng);
            MarkerOptions destinationMarkerOptions=new MarkerOptions().position(destLatLng);
            mMap.addMarker(sourceMarkerOptions).showInfoWindow();
            mMap.addMarker(destinationMarkerOptions).showInfoWindow();
            sourceEditText.setVisibility(GONE);
            destinationEditText.setVisibility(GONE);
            findPath.setVisibility(GONE);
            showHide.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            AppUtils.doLogError(AppConstants.ERROR_TAG, e.getMessage());
        }
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){
        StringBuffer url = new StringBuffer("https://maps.googleapis.com/maps/api/directions/json?");
        url.append("origin="+origin.latitude+","+origin.longitude);
        url.append("&destination="+dest.latitude+","+dest.longitude);
        url.append("&sensor=true");
        return url.toString();
    }

}
