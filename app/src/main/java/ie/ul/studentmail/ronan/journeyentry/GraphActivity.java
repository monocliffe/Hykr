package ie.ul.studentmail.ronan.journeyentry;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.view.View;
import android.widget.TextView;

import java.util.List;

public class GraphActivity extends AppCompatActivity {
    private JourneyDataBase jDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GraphView graphStep = findViewById(R.id.stepGraph);
        LineGraphSeries stepSeries = new LineGraphSeries<>(generateData(1));
        stepSeries.setTitle("Step Log");
        stepSeries.setColor(Color.BLUE);
        stepSeries.setDrawDataPoints(true);
        stepSeries.setDataPointsRadius(10);
        stepSeries.setThickness(8);
        graphStep.addSeries(stepSeries);

        TextView highStep = findViewById(R.id.highStep);
        highStep.setText("Highest Steps Taken: " + stepSeries.getHighestValueY());

        GraphView graphDist = findViewById(R.id.distGraph);
        LineGraphSeries distSeries = new LineGraphSeries<>(generateData(2));
        stepSeries.setTitle("Distance Log");
        distSeries.setColor(Color.BLUE);
        distSeries.setDrawDataPoints(true);
        distSeries.setDataPointsRadius(10);
        distSeries.setThickness(8);
        graphDist.addSeries(distSeries);

        TextView highDist = findViewById(R.id.highDist);
        highDist.setText("Highest Distance: " + String.format("%.2f km", distSeries.getHighestValueY()));
    }

    private DataPoint[] generateData(int type) {
        jDB = Room.databaseBuilder(getApplicationContext(),JourneyDataBase.class,"JourneyDatabase").allowMainThreadQueries().build();
        List<Journey> journeyList = jDB.getJourneyDAO().getJourneys();

        int count = journeyList.size();
        DataPoint[] values = new DataPoint[count];

        if(type == 1) {
            for (int i = 0; i < journeyList.size(); i++) {
                DataPoint v = new DataPoint(i, journeyList.get(i).getSteps());
                values[i] = v;
            }
        }

        if(type == 2) {
            for (int i = 0; i < journeyList.size(); i++) {
                DataPoint v = new DataPoint(i, journeyList.get(i).getDistance());
                values[i] = v;
            }
        }
        return values;
    }
}
