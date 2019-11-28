package ie.ul.studentmail.ronan.journeyentry;

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

import java.util.List;

public class GraphActivity extends AppCompatActivity {
    private JourneyDataBase jDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GraphView graph = (GraphView) findViewById(R.id.stepGraph);
        LineGraphSeries series1 = new LineGraphSeries<>(generateData(1));
        graph.addSeries(series1);

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
        return values;
    }
}
