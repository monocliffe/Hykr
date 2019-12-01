package ie.ul.studentmail.ronan.journeyentry;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class OptionsActivity extends AppCompatActivity {
    // this will be key for the key value pair
    public static final String BUTTON_STATE_NIGHT = "Button_State_Night";
    public static final String BUTTON_STATE_DAY = "Button_State_Day";
    // this is name of shared preferences file, must be same whenever accessing
    // the key value pair.
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        setContentView(R.layout.activity_options);
        boolean lastDayButtonState = sharedpreferences.getBoolean(BUTTON_STATE_DAY, false);
        boolean lastNightButtonState = sharedpreferences.getBoolean(BUTTON_STATE_NIGHT, false);
        final RadioGroup rg = findViewById(R.id.radio_group);
        final RadioButton dayModeRButton = findViewById(R.id.day_mode);
        final RadioButton nightModeButton = findViewById(R.id.night_mode);

        dayModeRButton.setChecked(lastDayButtonState);
        nightModeButton.setChecked(lastNightButtonState);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected

                if(checkedId == R.id.day_mode) {

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    Boolean isChecked = dayModeRButton.isChecked();
                    // use this to add the new state
                    System.out.println("isCheckedDay: " + isChecked);
                    editor.putBoolean(BUTTON_STATE_DAY, isChecked);
                    editor.putBoolean(BUTTON_STATE_NIGHT, !isChecked);
                    // save
                    editor.apply();
                    if(isChecked) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        nightModeButton.setChecked(false);
                    }
                    Intent intent = new Intent(OptionsActivity.this, MainActivity.class);
                    startActivity(intent);


                } else if(checkedId == R.id.night_mode) {

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    boolean isChecked = nightModeButton.isChecked();
                    // use this to add the new state
                    System.out.println("isCheckedNight: " + isChecked);
                    editor.putBoolean(BUTTON_STATE_NIGHT, isChecked);
                    editor.putBoolean(BUTTON_STATE_DAY, !isChecked);
                    // save
                    editor.apply();

                    if (isChecked) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        dayModeRButton.setChecked(false);
                    }
                    Intent intent = new Intent(OptionsActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

        });


    }
    public void onClickDeleteAll(View v){
        new AlertDialog.Builder(this)
                .setTitle("Title")
                .setMessage("Do you really want to delete all journey info?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        JourneyDataBase jDB = Room.databaseBuilder(getApplicationContext(), JourneyDataBase.class, "JourneyDatabase").allowMainThreadQueries().build();
                        jDB.getJourneyDAO().delete();

                        Toast.makeText(OptionsActivity.this, "All Journeys Deleted!", Toast.LENGTH_SHORT).show();



                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
}
