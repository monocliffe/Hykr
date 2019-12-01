package ie.ul.studentmail.ronan.journeyentry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;


public class JourneyEntryConfirm extends AppCompatActivity {

    private JourneyDataBase jDB;
    String[] journeyInfo;
    String journeyStartInfo;
    String journeyEndInfo;
    String stepCount="0";
    double distance;
    int steps;

    Bundle recieverBundle = new Bundle();
    TextView viewText;
    EditText editText;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_entry);
        getSupportActionBar().hide(); //hide the title bar
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
        viewText.setText("Steps: " + stepCount);

        viewText=findViewById(R.id.distanceText);
        viewText.setText(String.format(Locale.UK,"Step Distance: %.2f km", distance));

        viewText=findViewById(R.id.startDateTimeText);
        viewText.setText("From: " + journeyStartInfo);

        viewText = findViewById(R.id.endDateTimeText);
        viewText.setText("Untill: " + journeyEndInfo);

        if(journeyInfo[3]==null)
            journeyInfo[3] = "0.0";
        if(journeyInfo[4]==null)
            journeyInfo[4] = "0.0";
        if(journeyInfo[5]==null)
            journeyInfo[5] = "0.0";
        if(journeyInfo[6]==null)
            journeyInfo[6] = "0.0";


        for(int i=0; i<journeyInfo.length; i++){
            System.out.println("i= " + i + " - " + journeyInfo[i]);
        }

    }

    public void onClickEnter(View v){

        editText=findViewById(R.id.editText);
        journeyInfo[7]=editText.getText().toString();

        if(journeyInfo[7].length()==0)
            journeyInfo[7]="Untitled";

        Journey journey = new Journey(journeyInfo[7],
                                      steps,
                                      String.format(Locale.UK, "%.2f km", distance),
                                      journeyStartInfo,
                                      journeyEndInfo,
                                      journeyInfo[3],
                                      journeyInfo[4],
                                      journeyInfo[5],
                                      journeyInfo[6]);

        new InsertJourneyAsyncTask().execute(journey);
        Intent intent = new Intent(JourneyEntryConfirm.this, MainActivity.class);
        startActivity(intent);

        //get info from data fields and make a journey object which can then be added to the database.
    }

    public void onClickDoNotEnter(View v){

        Intent intent = new Intent(JourneyEntryConfirm.this, MainActivity.class);
        startActivity(intent);

    }

    private class InsertJourneyAsyncTask extends AsyncTask<Journey, Void, Void>{

        @Override
        protected Void doInBackground(Journey... journeys) {
            jDB.getJourneyDAO().addJourney(journeys[0]);
            return null;
        }
    }


}
