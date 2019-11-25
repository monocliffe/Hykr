package ie.ul.studentmail.ronan.journeyentry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;

import java.util.List;

public class ViewJourneys extends AppCompatActivity {

    private JourneyDataBase jDB;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_journeys);

        jDB = Room.databaseBuilder(getApplicationContext(),JourneyDataBase.class,"JourneyDatabase").allowMainThreadQueries().build();
        System.out.println("db created");
        List<Journey> journeyList = jDB.getJourneyDAO().getJourneys();

        recyclerView = (RecyclerView) findViewById(R.id.journey_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = new JourneyAdapter(jDB.getJourneyDAO().getJourneys());
        recyclerView.setAdapter(mAdapter);


    }
}
