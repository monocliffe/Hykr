
package ie.ul.studentmail.ronan.journeyentry;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class GraphActivity extends AppCompatActivity {
    private JourneyDataBase jDB;
    List<Journey> journeyList;
    OnDataPointTapListener tapListen;
    private int totalSteps, totalJourneys;
    private double totalDist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title

        getSupportActionBar().hide(); //hide the title bar
//        getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_graph);
        drawGraphs();
    }

    @Override
    protected void onResume(){
        super.onResume();
        drawGraphs();
    }

    private void drawGraphs(){

        final OnDataPointTapListener tapListen =  new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                int steps = journeyList.get((int) dataPoint.getX()).getSteps();
                double dist = journeyList.get((int) dataPoint.getX()).getDistance();
                String name = journeyList.get((int) dataPoint.getX()).getName();
                String[] date = journeyList.get((int) dataPoint.getX()).getJourneyEnd().split(" ");
                String output = "----------------------------" + "\n" +
                                "Name: " + name + "\n" +
                                "Steps: " + steps + "\n" +
                                "Distance: " + String.format(Locale.UK,"%.2f km", dist) + "\n" +
                                "Date: " + date[0] +"\n" +
                                "----------------------------";

                Toast.makeText(getApplicationContext(), output, Toast.LENGTH_SHORT).show();
            }
        };

        GraphView graphStep = findViewById(R.id.stepGraph);
        LineGraphSeries stepSeries = new LineGraphSeries<>(generateData(1));
        stepSeries.setTitle("Step Log");
        stepSeries.setColor(getColor(R.color.colorPrimary));
        stepSeries.setDrawDataPoints(true);
        stepSeries.setDataPointsRadius(15);
        stepSeries.setThickness(8);
        graphStep.addSeries(stepSeries);
        stepSeries.setOnDataPointTapListener(tapListen);
        TextView highStep = findViewById(R.id.highStep);
        String holder = getString(R.string.most_steps_taken) + " " + stepSeries.getHighestValueY();
        graphStep.getViewport().setMaxX(journeyList.size());
        graphStep.getViewport().setMaxY(stepSeries.getHighestValueY()+(stepSeries.getHighestValueY()*0.2));
        graphStep.getViewport().setYAxisBoundsManual(true);
        graphStep.getViewport().setXAxisBoundsManual(true);
        highStep.setText(holder);

        GraphView graphDist = findViewById(R.id.distGraph);
        LineGraphSeries distSeries = new LineGraphSeries<>(generateData(2));
        stepSeries.setTitle("Distance Log");
        stepSeries.setColor(getColor(R.color.colorPrimary));
        distSeries.setDrawDataPoints(true);
        distSeries.setDataPointsRadius(15);
        distSeries.setThickness(8);
        graphDist.addSeries(distSeries);
        distSeries.setOnDataPointTapListener(tapListen);
        TextView highDist = findViewById(R.id.highDist);
        holder = getString(R.string.longest_distance) + String.format(Locale.UK," %.2f km", distSeries.getHighestValueY());
        graphDist.getViewport().setMaxX(journeyList.size());
        graphDist.getViewport().setMaxY(distSeries.getHighestValueY()+(distSeries.getHighestValueY()*0.2));
        graphDist.getViewport().setYAxisBoundsManual(true);
        graphDist.getViewport().setXAxisBoundsManual(true);
        highDist.setText(holder);

        TextView totalDistView = findViewById(R.id.totalDist);
        holder = getString(R.string.totalDist) + " " + String.format(Locale.UK," %.2f km",totalDist);
        totalDistView.setText(holder);

        TextView totalStepView = findViewById(R.id.totalSteps);
        holder = getString(R.string.totalSteps) + " " + totalSteps;
        totalStepView.setText(holder);

        TextView totalJourneyView = findViewById(R.id.totalJourney);
        holder = getString(R.string.totalJourney) + " " + totalJourneys;
        totalJourneyView.setText(holder);
    }

    private DataPoint[] generateData(int type) {
        jDB = Room.databaseBuilder(getApplicationContext(),JourneyDataBase.class,"JourneyDatabase").allowMainThreadQueries().build();
        journeyList = jDB.getJourneyDAO().getJourneys();

        int count = journeyList.size();
        totalJourneys = count;
        DataPoint[] values = new DataPoint[count];

        if(type == 1) {
            for (int i = 0; i < journeyList.size(); i++) {
                values[i] = new DataPoint(i, journeyList.get(i).getSteps());
                totalSteps += journeyList.get(i).getSteps();
            }
        }

        if(type == 2) {
            for (int i = 0; i < journeyList.size(); i++) {
                values[i] = new DataPoint(i, journeyList.get(i).getDistance());
                totalDist += journeyList.get(i).getDistance();
            }
        }
        return values;
    }
}
