package ie.ul.studentmail.ronan.journeyentry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stepData = findViewById(R.id.stepReadout);
        itJustSaysSteps =  findViewById(R.id.justTheWordSteps);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        final Button startJourneyButton = findViewById(R.id.begin_journey_button);
        startJourneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepData.setText("0");
                journeyStartTime = LocalDateTime.now();
                journeyInfoArray[0] = stepData.getText().toString();
                journeyInfoArray[1] = journeyStartTime.format(formatter);

            }
        });

        final Button endJourneyButton = findViewById(R.id.end_journey_button);
        endJourneyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent intent = new Intent(MainActivity.this, JourneyInfo.class);
                journeyEndTime = LocalDateTime.now();
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



    }

    @Override
    //fragment!!!!! Look into this!!!
    protected void onResume() {
        super.onResume();
        Sensor countSensor = sensorManager.getDefaultSensor(19); //Type 19 is a step counter!

        if(countSensor!=null){
            sensorManager.registerListener(this, countSensor, 2); //Type 2 is delay_ui
        }else{
            Toast.makeText(this, "Sorry, no sensor found, bad luck!", Toast.LENGTH_SHORT).show();
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
        if(appRunning){
            stepData.setText(String.valueOf(sensorEvent.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
