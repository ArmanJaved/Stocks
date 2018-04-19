package stocksymbolapp;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.stocksymbolapp.R;


public class Setting extends AppCompatActivity {



    public static final String MINI = "MINI";
    public static final String INDICES = "INDICES";
    public static final String CHART = "chart";

    private int FM_NOTIFICATION_ID =200;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        // Array of choices
        String colors[] = {"Line","Candle"};

// Selection of the spinner
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerchart);

// Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, colors);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);



        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String chartstatus = preferences.getString("chartstatus", "");


        if (chartstatus.equals("c")) {

            spinner.setSelection(0, true);
            View v = spinner.getSelectedView();
            ((TextView)v).setText("Candle");

        }
        else {

            spinner.setSelection(0, true);
            View v = spinner.getSelectedView();
            ((TextView)v).setText("Line");

        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                String name = preferences.getString("chartstatus", "");

                if (position ==1)
                {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("chartstatus","c");
                    editor.apply();
//                    removeNotification();

                }
                else
                {

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("chartstatus","l");
                    editor.apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        android.support.v7.widget.SwitchCompat mini = (android.support.v7.widget.SwitchCompat) findViewById(R.id.mini);
        android.support.v7.widget.SwitchCompat indices = (android.support.v7.widget.SwitchCompat) findViewById(R.id.indices);


        SharedPreferences prefs = getSharedPreferences(MINI, MODE_PRIVATE);
        String mini_variable = prefs.getString("ministatus", "");


        if (mini_variable.equals("on"))
        {
            mini.setChecked(true);
        }
        else
        {
            mini.setChecked(false);
        }

        SharedPreferences prefs1 = getSharedPreferences(INDICES, MODE_PRIVATE);
        String indices_variable = prefs1.getString("indicestatus", "");

        if (indices_variable.equals("on"))
        {
            indices.setChecked(true);
        }
        else
        {
            indices.setChecked(false);
        }


        mini.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                SharedPreferences.Editor editor = getSharedPreferences(MINI, MODE_PRIVATE).edit();
                if (isChecked) {
                    //do stuff when Switch is ON

                    editor.putString("ministatus", "on");
                    editor.commit();
                } else {

                    editor.putString("ministatus", "off");
                    editor.commit();
                    removeNotification();
                    //do stuff when Switch if OFF
                }
            }
        });


        indices.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                SharedPreferences.Editor editor = getSharedPreferences(INDICES, MODE_PRIVATE).edit();
                if (isChecked) {
                    //do stuff when Switch is ON

                    editor.putString("indicestatus", "on");
                    editor.commit();
                } else {

                    editor.putString("indicestatus", "off");
                    editor.commit();
                    removeNotification();
                    //do stuff when Switch if OFF
                }
            }
        });





    }



    private void removeNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(FM_NOTIFICATION_ID);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }








}
