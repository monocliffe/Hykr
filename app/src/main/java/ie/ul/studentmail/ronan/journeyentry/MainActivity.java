package ie.ul.studentmail.ronan.journeyentry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView itJustSaysSteps;
    TextView stepData;
    SensorManager sensorManager;
    boolean appRunning = false;
    LocalDateTime journeyStartTime;
    LocalDateTime journeyEndTime;
    String[] journeyInfoArray = new String[3];
    Bundle journeyBundle = new Bundle();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    final int PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 1;
    FusedLocationProviderClient mFusedLocationProviderClient;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActivityPermission();
        stepData = findViewById(R.id.stepReadout);

        itJustSaysSteps =  findViewById(R.id.justTheWordSteps);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        final Button startJourneyButton = findViewById(R.id.begin_journey_button);
        startJourneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepData.setText("0");
                count = 0;
                journeyStartTime = LocalDateTime.now();
                journeyInfoArray[0] = "0";
                journeyInfoArray[1] = journeyStartTime.format(formatter);
               // System.out.println(locationResult.toString());

            }
        });

        final Button endJourneyButton = findViewById(R.id.end_journey_button);
        endJourneyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent intent = new Intent(MainActivity.this, JourneyInfo.class);
                journeyEndTime = LocalDateTime.now();
                journeyInfoArray[0] = Integer.toString(count);
                journeyInfoArray[2] = journeyEndTime.format(formatter);
                journeyBundle.putStringArray("journey_info", journeyInfoArray);
                intent.putExtras(journeyBundle);
                startActivity(intent);
            }
        });

        final Button viewJourneyButton = findViewById(R.id.view_journey);
        viewJourneyButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, ViewJourneys.class);
                startActivity(intent);
            }
        });


        final Button viewMapButton = findViewById(R.id.view_map_button);
        viewMapButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });


        //This is a test to determine if step sensor is on phone
//        PackageManager pm = getPackageManager();
//        if(pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)){
//
//            Toast.makeText(this, "Step sensor located!", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(this, "No step sensor!", Toast.LENGTH_SHORT).show();
//        }

    }

    /*
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    Step Sensor Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    */
    @Override
    //fragment!!!!! Look into this!!!
    protected void onResume() {
        super.onResume();
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        if(countSensor!=null){
            sensorManager.registerListener(this, countSensor, 2); //Type 2 is delay_ui

        }else{
            Toast.makeText(this, "Sorry, no sensor found!", Toast.LENGTH_SHORT).show();
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


}
