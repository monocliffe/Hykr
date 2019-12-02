package ie.ul.studentmail.ronan.journeyentry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MainActivity extends AppCompatActivity implements SensorEventListener,
                                                               BasicStepListener,
                                                               LocationListener{

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
//    private static final int PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 1;
    private boolean mLocationPermissionGranted;
//    private boolean mActivityPermissionGranted;

    SharedPreferences sharedpreferences;
    public static final String DISPLAY_MODE_STATE = "Button_State_Day";
    public static final String MyPREFERENCES = "MyPrefs" ;

    private BasicStepDetector simpleStepDetector;
    private Location mLastKnownLocation;
    LocationRequest mLocationRequest;

    String[] journeyInfoArray = new String[8];
    String startLat, startLong, endLat, endLong;
    LocalDateTime journeyStartTime;
    LocalDateTime journeyEndTime;
    boolean appRunning = false;
    int count = 0;

    Bundle journeyBundle = new Bundle();
    SensorManager sensorManager;
    TextView stepData;
    Button endButton;

    //The ofPattern() method below requires api 26 or higher!
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Shared preferences are used to retrieve the previous light/dark mode settings
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        boolean lastDayButtonState = sharedpreferences.getBoolean(DISPLAY_MODE_STATE, false);
        if(lastDayButtonState)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setContentView(R.layout.activity_main);


        getLocationPermission();

        //if(mLocationPermissionGranted)
            startLocationUpdates();

        //this is to disable end journey until begin journey has been pressed
        endButton = findViewById(R.id.end_journey_button);
        endButton.setEnabled(false);
        endButton.setAlpha(.5f);

        stepData = findViewById(R.id.stepReadout);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }


   /*
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    Location Update Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    */
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

        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
            @Override
                public void onLocationResult(LocationResult locationResult) {

                    onLocationChanged(locationResult.getLastLocation());
                }
            },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
//        String msg = "Updated Location: " +
//                (location.getLatitude()) + "," +
//                (location.getLongitude());
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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

        endButton.setEnabled(true);
        endButton.setAlpha(1.0f);
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
        startActivity(intent);
    }

    public void viewGraphClicked(View v){
        Intent intent = new Intent(MainActivity.this, GraphActivity.class);
        startActivity(intent);
    }

    /*
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    Activity Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    */
    @Override
    protected void onResume() {

        super.onResume();

        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        //If step_detector present use it.
        if (countSensor != null) {
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
            stepData.setText(String.valueOf(count));
        }
    }

    @Override
    public void step(long timeNs) {
        count++;
        stepData.setText(String.valueOf(count));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

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

}

