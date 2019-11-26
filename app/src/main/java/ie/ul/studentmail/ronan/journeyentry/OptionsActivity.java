package ie.ul.studentmail.ronan.journeyentry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
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
        Boolean lastDayButtonState = sharedpreferences.getBoolean(BUTTON_STATE_DAY, false);
        Boolean lastNightButtonState = sharedpreferences.getBoolean(BUTTON_STATE_NIGHT, false);


        final RadioGroup rg = findViewById(R.id.radio_group);
        final RadioButton dayModeRButton = findViewById(R.id.day_mode);
        final RadioButton nightModeButton = findViewById(R.id.night_mode);



        dayModeRButton.setChecked(lastDayButtonState);
        System.out.println("DaybuttonState: " + lastDayButtonState);

        nightModeButton.setChecked(lastNightButtonState);

        System.out.println("NightbuttonState: " + lastNightButtonState);



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
                    Boolean isChecked = nightModeButton.isChecked();
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
}
