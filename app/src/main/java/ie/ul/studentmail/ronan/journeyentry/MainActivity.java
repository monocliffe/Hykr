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
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity implements SensorEventListener,
                                                               BasicStepListener,
                                                               GoogleApiClient.ConnectionCallbacks,
                                                               GoogleApiClient.OnConnectionFailedListener,
                                                               LocationListener{

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    SharedPreferences sharedpreferences;
    public static final String BUTTON_STATE_DAY = "Button_State_Day";
    public static final String MyPREFERENCES = "MyPrefs" ;

    String[] journeyInfoArray = new String[8];
    String startLat, startLong, endLat, endLong;
    LocalDateTime journeyStartTime;
    LocalDateTime journeyEndTime;
    boolean appRunning = false;

    int count = 0;

    Location location;
    GoogleApiClient googleApiClient;
    Bundle journeyBundle = new Bundle();
    SensorManager sensorManager;

    private BasicStepDetector simpleStepDetector;
    private Sensor accel;

    TextView itJustSaysSteps;
    TextView stepData;


    //The ofPattern method below requires api 26 or higher!
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    final int PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Shared preferences are used to retrieve the previous light/dark mode settings
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Boolean lastDayButtonState = sharedpreferences.getBoolean(BUTTON_STATE_DAY, false);
        if(lastDayButtonState)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setContentView(R.layout.activity_main);
        getActivityPermission();

        stepData = findViewById(R.id.stepReadout);
        itJustSaysSteps =  findViewById(R.id.justTheWordSteps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        getLocationPermission();
        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();


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
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(location!=null){
            startLat = String.valueOf(location.getLatitude());
            startLong = String.valueOf(location.getLongitude());
        }
        stepData.setText("0");
        count = 0;
        journeyStartTime = LocalDateTime.now();
        journeyInfoArray[0] = "0";
        journeyInfoArray[1] = journeyStartTime.format(formatter);
        journeyInfoArray[3] = startLat;
        journeyInfoArray[4] = startLong;
    }

    public void endJourneyClicked(View v){
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(location!=null){
            endLat = String.valueOf(location.getLatitude());
            endLong = String.valueOf(location.getLongitude());
        }
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
    Step Sensor Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    */
    @Override
    //fragment!!!!! Look into this!!!
    protected void onResume() {
//        super.onResume();
//        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
//
//        if(countSensor!=null){
//            sensorManager.registerListener(this, countSensor, 2); //Type 2 is delay_ui
//
//        }else{
//            //Toast.makeText(this, "Sorry, no sensor found!", Toast.LENGTH_SHORT).show();
//        }

        super.onResume();
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        if(countSensor!=null){
            sensorManager.registerListener(this, countSensor, 2); //Type 2 is delay_ui

        }else{
            Toast.makeText(this, "Sorry, no STEP_DETECTOR found!\nUsing Accelerometer instead.\nResults may be inaccurate!", Toast.LENGTH_SHORT).show();
            accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            simpleStepDetector = new BasicStepDetector();
            simpleStepDetector.registerListener(this);
            sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
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

        //if(appRunning){
            if(sensorEvent.values[0] == (float) 1.0){
                count++;
                stepData.setText(Integer.toString(count));
            }
            //stepData.setText(String.valueOf(sensorEvent.values[0]));
            //Toast.makeText(this, "Something happened: " + String.valueOf(sensorEvent.values[0]), Toast.LENGTH_SHORT).show();
        //}
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //in api 29 permission needs to be requested in order to access activity recognition
    //this is required to use the TYPE_STEP_COUNTER
    private void getActivityPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACTIVITY_RECOGNITION)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                    PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION);
        }
    }


    /**
     * Prompts the user for permission to use the device location.
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

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
//        updateLocationUI();
    }


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



    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void step(long timeNs) {

    }
}


/*
    <?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_gravity="center"
        android:background="@color/colorPrimaryDark"
        android:orientation="vertical"
        tools:context=".MainActivity">

<TextView
        android:id="@+id/justTheWordSteps"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="84dp"
                android:gravity="center"
                android:text="@string/stepTv"
                android:textSize="30sp"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

<TextView
        android:id="@+id/stepReadout"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginBottom="64sp"
                android:gravity="center"
                android:text="@string/initial_counter_value"
                android:textSize="120sp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/justTheWordSteps"
                app:layout_constraintVertical_bias="0.0" />


<Button
        android:id="@+id/begin_journey_button"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginStart="24dp"
                android:layout_marginEnd="24dp"
                android:layout_marginBottom="24dp"
                android:layout_weight="1"
                android:background="@drawable/custom_button"
                android:gravity="center"
                android:onClick="beginJourneyClicked"
                android:text="@string/begin_button"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:layout_constraintVertical_bias="0.59" />

<Button
        android:id="@+id/end_journey_button"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginStart="24dp"
                android:layout_marginTop="24dp"
                android:layout_marginEnd="24dp"
                android:layout_marginBottom="24dp"
                android:layout_weight="1"
                android:background="@drawable/custom_button"
                android:gravity="center"
                android:onClick="endJourneyClicked"
                android:text="@string/end_journey"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintHorizontal_bias="0.0"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:layout_constraintVertical_bias="0.714" />

<Button
        android:id="@+id/view_journey"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginBottom="24dp"
                android:background="@drawable/custom_button"
                android:gravity="center"
                android:onClick="viewJourneysClicked"
                android:text="@string/view_journeys"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintHorizontal_bias="0.498"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/stepReadout"
                app:layout_constraintVertical_bias="0.857" />

<Button
        android:id="@+id/view_map_button"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="24dp"
                android:background="@drawable/custom_button"
                android:onClick="viewMapClicked"
                android:text="@string/view_map"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintHorizontal_bias="0.0"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/begin_journey_button"
                app:layout_constraintVertical_bias="0.686" />

<ImageButton
        android:id="@+id/preferences"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="16dp"
                android:layout_marginEnd="16dp"
                android:onClick="onPreferencesClicked"
                android:background="@drawable/custom_button2"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintHorizontal_bias="1.0"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:layout_constraintVertical_bias="0.0"
                app:srcCompat="@android:drawable/ic_menu_preferences" />


</androidx.constraintlayout.widget.ConstraintLayout>*/