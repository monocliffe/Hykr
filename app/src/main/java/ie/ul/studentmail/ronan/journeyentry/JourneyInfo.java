package ie.ul.studentmail.ronan.journeyentry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.time.LocalDate;

import static java.time.LocalDate.now;

public class JourneyInfo extends AppCompatActivity {

    private JourneyDataBase jDB;
    public static LocalDate dateType;
    String journeyStartInfo;
    String journeyEndInfo;
    String[] journeyArray;
    TextView viewText;
    Intent intent;
    double distance;
    String stepCount="0";
    Bundle recieverBundle = new Bundle();
    String[] journeyInfo;

    int steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_entry);

        //create a database instance
        jDB = Room.databaseBuilder(getApplicationContext(),JourneyDataBase.class,"JourneyDatabase").allowMainThreadQueries().build();

        intent = getIntent();
        if(intent!=null) {
            recieverBundle = this.getIntent().getExtras();
            if(recieverBundle.getStringArray("journey_info") != null) {
                journeyInfo = recieverBundle.getStringArray("journey_info");
                stepCount = journeyInfo[0];
                if(stepCount!=null)
                    steps = Integer.parseInt(stepCount);
                distance = steps/1312.33595801;
                journeyStartInfo = journeyInfo[1];
                journeyEndInfo = journeyInfo[2];

            }
        }


        viewText=findViewById(R.id.stepsText);
        viewText.setText(stepCount);

        viewText=findViewById(R.id.distanceText);
        viewText.setText(String.format("%.2f km", distance));

        viewText=findViewById(R.id.startDateTimeText);
        viewText.setText(journeyStartInfo);

        viewText = findViewById(R.id.endDateTimeText);
        viewText.setText(journeyEndInfo);



    }

    public void onClickEnter(View v){

        Journey journey = new Journey(steps, String.format("%.2f km", distance), journeyStartInfo, journeyEndInfo);
        jDB.getJourneyDAO().addJourney(journey);
        Intent intent = new Intent(JourneyInfo.this, MainActivity.class);
        startActivity(intent);

        //get info from data fields and make a journey object which can then be added to the database.
    }
}
