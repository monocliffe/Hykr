package ie.ul.studentmail.ronan.journeyentry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class ViewJourneyInfo extends AppCompatActivity {

    Bundle recieverBundle;
    String[] journeyInfo;
    String journeyReadout;
    String[] startTimeInfo;
    String[] endTimeInfo;
    JourneyDataBase jDB;
    List<Journey> journeyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_journey_info);
        //hide the title bar
        getSupportActionBar().hide();

        recieverBundle = this.getIntent().getExtras();
        if(recieverBundle.getStringArray("journey_info") != null) {
            journeyInfo = recieverBundle.getStringArray("journey_info");
        }

        startTimeInfo = journeyInfo[3].split(" ");
        endTimeInfo = journeyInfo[4].split(" ");

        journeyReadout = journeyInfo[0] + "\n\n" +
                         "Steps: " + journeyInfo[1] + "\n" +
                         "Steps Distance: " + journeyInfo[2] + "\n\n";

        if(startTimeInfo[0].equals(endTimeInfo[0])){
            journeyReadout += "Date: " + startTimeInfo[0] + "\n";
        }else{
            journeyReadout += "Dates: " + startTimeInfo[0] + " to " + endTimeInfo[0] + "\n";
        }

        journeyReadout += "Time Started: " + startTimeInfo[1] + "\n" +
                          "Time Ended:   " + endTimeInfo[1] + "\n\n" +

                          "Start@: " + journeyInfo[5] + ", " + journeyInfo[6] + "\n" +
                          "End@:   " + journeyInfo[7] + ", " + journeyInfo[8] + "\n\n" +
                          "Distance Covered: " + journeyInfo[9] + "\n";




        TextView textView = findViewById(R.id.textView);
        textView.setText(journeyReadout);

    }


}
