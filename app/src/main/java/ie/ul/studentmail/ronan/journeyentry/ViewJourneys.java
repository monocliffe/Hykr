package ie.ul.studentmail.ronan.journeyentry;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ViewJourneys extends AppCompatActivity implements JourneyViewHolder.OnItemSelectedListener {


    JourneyAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_journeys);



        LinearLayoutManager layoutManager = new LinearLayoutManager(ViewJourneys.this);
        recyclerView = findViewById(R.id.journey_recycler_view);
        System.out.println(recyclerView);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));

        recyclerView.addItemDecoration(itemDecorator);

        List<Journey> selectableItems = generateItems();
        adapter = new JourneyAdapter(this,selectableItems,false);
        recyclerView.setAdapter(adapter);
    }

    public List<Journey> generateItems(){

        JourneyDataBase jDB = Room.databaseBuilder(getApplicationContext(), JourneyDataBase.class, "JourneyDatabase").allowMainThreadQueries().build();
        List<Journey> selectableItems;
        selectableItems = jDB.getJourneyDAO().getJourneys();
        return selectableItems;
    }

    @Override
    public void onItemSelected(SelectableJourney selectableItem) {
        String[] journeyInfo;
        List<Journey> selectedItems = adapter.getSelectedItems();
        Snackbar.make(recyclerView,"Selected item is "+selectableItem.getJourneyStart()+
                ", Total selected item count is "+selectedItems.size(),Snackbar.LENGTH_LONG).show();


        journeyInfo=selectableItem.toString().split(",");

        Bundle bundle = new Bundle();
        bundle.putStringArray("journey_info",journeyInfo);
        Intent intent = new Intent(ViewJourneys.this, ViewJourneyInfo.class );
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

