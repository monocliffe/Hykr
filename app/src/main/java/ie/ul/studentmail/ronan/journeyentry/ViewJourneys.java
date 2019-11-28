package ie.ul.studentmail.ronan.journeyentry;

/*import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

//import android.os.AsyncTask;
import android.os.Bundle;

import java.util.List;

public class ViewJourneys extends AppCompatActivity {

    List<Journey> journeyList;
    List<SelectableJourney> selectableJourneyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_journeys);

        JourneyDataBase jDB = Room.databaseBuilder(getApplicationContext(), JourneyDataBase.class, "JourneyDatabase").allowMainThreadQueries().build();
        System.out.println("db created");


        RecyclerView recyclerView = findViewById(R.id.journey_recycler_view);
        //new getJourneysASyncTask().execute();
        journeyList = jDB.getJourneyDAO().getJourneys();
        for(int i=0; i<journeyList.size(); i++)
            selectableJourneyList.add(new SelectableJourney(journeyList.get(i),false));

        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));

        recyclerView.addItemDecoration(itemDecorator);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        RecyclerView.Adapter mAdapter = new JourneyAdapter(this,journeyList,false);
        recyclerView.setAdapter(mAdapter);

    }



}





<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorPrimaryDark"
    tools:context=".ViewJourneys">



    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/journey_recycler_view"
        android:layout_width="341dp"
        android:layout_height="644dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.609" />
</androidx.constraintlayout.widget.ConstraintLayout>








*/


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

