package ie.ul.studentmail.ronan.journeyentry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
//import android.location.LocationListener;
import com.google.android.gms.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MainActivity extends AppCompatActivity implements SensorEventListener,
                                                               BasicStepListener,
                                                               GoogleApiClient.ConnectionCallbacks,
                                                               GoogleApiClient.OnConnectionFailedListener,
                                                               LocationListener{

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 1;
    private boolean mLocationPermissionGranted;


    SharedPreferences sharedpreferences;
    public static final String BUTTON_STATE_DAY = "Button_State_Day";
    public static final String MyPREFERENCES = "MyPrefs" ;

    String[] journeyInfoArray = new String[8];
    String startLat, startLong, endLat, endLong;
    LocalDateTime journeyStartTime;
    LocalDateTime journeyEndTime;
    boolean appRunning = false;
    private Location mLastKnownLocation;

    int count = 0;

    GoogleApiClient googleApiClient;
    Bundle journeyBundle = new Bundle();
    SensorManager sensorManager;

    private BasicStepDetector simpleStepDetector;

    TextView itJustSaysSteps;
    TextView stepData;

    //The ofPattern method below requires api 26 or higher!
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    Button endButton;

    LocationRequest mLocationRequest;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Shared preferences are used to retrieve the previous light/dark mode settings
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        boolean lastDayButtonState = sharedpreferences.getBoolean(BUTTON_STATE_DAY, false);
        if(lastDayButtonState)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setContentView(R.layout.activity_main);

        getLocationPermission();

        //mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        startLocationUpdates();

        //getDeviceLocation();

        //this is to disable end journey untill begin journey has been pressed
        endButton = findViewById(R.id.end_journey_button);
        endButton.setEnabled(false);
        endButton.setAlpha(.5f);

        stepData = findViewById(R.id.stepReadout);
        itJustSaysSteps =  findViewById(R.id.justTheWordSteps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
    }




    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        mLastKnownLocation = location;
    }













    /*
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    Click Handling Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    */
    public void onPreferencesClicked(View v){
        Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
        startActivity(intent);
    }

    public void beginJourneyClicked(View v){
        //getActivityPermission();
        endButton.setEnabled(true);
        endButton.setAlpha(1.0f);
//        String[] lngLat = getLocation();
//        startLat=lngLat[0];
//        startLong=lngLat[1];
        //getDeviceLocation();
        startLat=String.valueOf(mLastKnownLocation.getLatitude());
        startLong=String.valueOf(mLastKnownLocation.getLongitude());
        stepData.setText("0");
        count = 0;
        journeyStartTime = LocalDateTime.now();
        journeyInfoArray[0] = "0";
        journeyInfoArray[1] = journeyStartTime.format(formatter);
        journeyInfoArray[3] = startLat;
        journeyInfoArray[4] = startLong;

    }

    public void endJourneyClicked(View v){
//        String[] lngLat = getLocation();
//        endLat=lngLat[0];
//        endLong=lngLat[1];

//        getDeviceLocation();
        endLat=String.valueOf(mLastKnownLocation.getLatitude());
        endLong=String.valueOf(mLastKnownLocation.getLongitude());

        Intent intent = new Intent(MainActivity.this, JourneyEntryConfirm.class);
        journeyEndTime = LocalDateTime.now();
        journeyInfoArray[0] = Integer.toString(count);
        journeyInfoArray[2] = journeyEndTime.format(formatter);
        journeyInfoArray[5] = endLat;
        journeyInfoArray[6] = endLong;

        journeyBundle.putStringArray("journey_info", journeyInfoArray);
        intent.putExtras(journeyBundle);
        startActivity(intent);
    }

    public void viewJourneysClicked(View v){
        Intent intent = new Intent(MainActivity.this, ViewJourneys.class);
        startActivity(intent);
    }

    public void viewMapClicked(View v){
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        //intent.putExtra("loc_permission",mLocationPermissionGranted);
        startActivity(intent);
    }

    public void viewGraphClicked(View v){
        Intent intent = new Intent(MainActivity.this, GraphActivity.class);
        startActivity(intent);
    }

    /*
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    Step Sensor Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    */
    @Override
    protected void onResume() {

        super.onResume();
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        //If step_detector present use it.
        if(countSensor!=null){
            sensorManager.registerListener(this, countSensor, 2); //Type 2 is delay_ui

        }
        //Else use accelerometer
        else {
            Toast.makeText(this, "Sorry, no STEP_DETECTOR found!\nUsing Accelerometer instead.\nResults may be inaccurate!", Toast.LENGTH_SHORT).show();

            Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            simpleStepDetector = new BasicStepDetector();
            simpleStepDetector.registerListener(this);
            sensorManager.registerListener(MainActivity.this, accelSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        appRunning=false;

        //Un-commment below to make the app pause step count when paused.
        //sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    sensorEvent.timestamp, sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);

        } else if(sensorEvent.values[0] == (float) 1.0){
            count++;
            stepData.setText(Integer.toString(count));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }




    //in api 29 permission needs to be requested in order to access activity recognition
    //this is required to use the TYPE_STEP_COUNTER
    private void getActivityPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACTIVITY_RECOGNITION)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                    PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION);
        }
    }

    /*
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    Permission Requests
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        if(requestCode==PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION){
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            }
        }
    }


    @Override
    public void step(long timeNs) {
        count++;
        stepData.setText(Integer.toString(count));
    }

//    public String[] getLocation(){
//        String[] retVals = new String[2];
//        String lat,lng;
//        if(mLocationPermissionGranted) {
//            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//            if (location != null) {
//                lat = String.valueOf(location.getLatitude());
//                lng = String.valueOf(location.getLongitude());
//                //System.out.println(endLat);
//            }else{
//                lat=getString(R.string.not_available);
//                lng=getString(R.string.not_available);
//            }
//
//        }else{
//            lat=getString(R.string.not_available);
//            lng=getString(R.string.not_available);
//        }
//        retVals[0]=lat;
//        retVals[1]=lng;
//        return retVals;
//    }


    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }





//    @Override
//    public void onStatusChanged(String s, int i, Bundle bundle) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String s) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String s) {
//
//    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


//    private void getDeviceLocation() {
//        /*
//         * Get the best and most recent location of the device, which may be null in rare
//         * cases when a location is not available.
//         */
//
//        if (mLocationPermissionGranted) {
//            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
//            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
//                @Override
//                public void onComplete(@NonNull Task<Location> task) {
//                    if (task.isSuccessful()) {
//                        // Set the map's camera position to the current location of the device.
//                        mLastKnownLocation = task.getResult();
//
//
//                    } else {
//
//                        mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//                    }
//                }
//            });
//        }
//
//    }



}

