package com.stocksymbolapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MailSenderActivity extends AppCompatActivity {

    String[] web = {
            "Add a ticker",
            "Report abuse",
            "App issue",
            "Employment",
            "Newsletter",
            "Other"

    } ;
    int[] imageId = {
            R.drawable.user,
            R.drawable.user,
            R.drawable.user,
            R.drawable.user,
            R.drawable.user,
            R.drawable.user,


    };

    private int previousSelectedPosition = -1;
    GridView grid;

    private Button txtDate;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_sender);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contact");


        final EditText name = (EditText)findViewById(R.id.name);
        final EditText email = (EditText)findViewById(R.id.email);
        final EditText message = (EditText)findViewById(R.id.message);





        CustomGrid adapter = new CustomGrid(MailSenderActivity.this, web, imageId);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);

        final GridView gv = (GridView) findViewById(R.id.grid);
        final TextView tv_message = (TextView) findViewById(R.id.tv_message);

        // Initializing a new String Array


        // Populate a List from Array elements
        final List<String> plantsList = new ArrayList<String>(Arrays.asList(web));

        // Data bind GridView with ArrayAdapter (String Array elements)
        gv.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, plantsList){
            public View getView(int position, View convertView, ViewGroup parent) {

                // Return the GridView current item as a View
                View view = super.getView(position,convertView,parent);

                // Convert the view as a TextView widget
                TextView tv = (TextView) view;

                // set the TextView text color (GridView item color)
                tv.setTextColor(Color.parseColor("#ffffff"));

                // Set the layout parameters for TextView widget
                RelativeLayout.LayoutParams lp =  new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT
                );
                tv.setLayoutParams(lp);

                // Get the TextView LayoutParams
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv.getLayoutParams();

                // Set the width of TextView widget (item of GridView)
                params.width = getPixelsFromDPs( 80);

                // Set the TextView height (GridView item/row equal height)
                params.height = getPixelsFromDPs(25);

                // Set the TextView layout parameters
                tv.setLayoutParams(params);

                // Display TextView text in center position
                tv.setGravity(Gravity.CENTER);

                // Set the TextView text font family and text size
                tv.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
                // Set the TextView text (GridView item text)
                tv.setText(plantsList.get(position));

//                tv.setPadding();
                // Set the TextView background color
                tv.setBackgroundColor(Color.parseColor("#2196f3"));

                // Return the TextView widget as GridView item
                return tv;
            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text
                String selectedItem = parent.getItemAtPosition(position).toString();

                // Display the selected item text to app interface
                tv_message.setText("Selected Topic : " + selectedItem);

                // Get the current selected view as a TextView
                TextView tv = (TextView) view;

                // Set the current selected item background color
                tv.setBackgroundColor(Color.parseColor("#607d8b"));

                // Set the current selected item text color
                tv.setTextColor(Color.parseColor("#ffffff"));

                // Get the last selected View from GridView
                TextView previousSelectedView = (TextView) gv.getChildAt(previousSelectedPosition);

                // If there is a previous selected view exists
                if (previousSelectedPosition != -1)
                {
                    // Set the last selected View to deselect
                    previousSelectedView.setSelected(false);

                    // Set the last selected View background color as deselected item
                    previousSelectedView.setBackgroundColor(Color.parseColor("#2196f3"));

                    // Set the last selected View text color as deselected item
                    previousSelectedView.setTextColor(Color.parseColor("#ffffff"));
                }

                // Set the current selected view position as previousSelectedPosition
                previousSelectedPosition = position;
            }
        });

        final Button send = (Button) this.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub



                if (name.getText().toString().trim().equalsIgnoreCase("")) {
                    name.setError("Please Enter Your Name");
                    return;
                }
                else if (email.getText().toString().trim().equalsIgnoreCase("")) {
                    email.setError("Please Enter Your Email");
                    return;
                }
                else if (message.getText().toString().trim().equalsIgnoreCase("")) {
                    message.setError("Please Enter Your Message");
                    return;
                }
                else if (tv_message.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Your Topic", Toast.LENGTH_LONG).show();
                    return;
                }
                boolean emailvalid = isValidEmail(email.getText().toString().trim());
                if (!emailvalid) {
                    email.setError("Please Enter a Valid Email Address");
                    return;
                }

                try {

                    BackgroundMail.newBuilder(MailSenderActivity.this)
                            .withUsername("devil.drac.cht@gmail.com")
                            .withPassword("Incorrect12")
                            .withMailto("mfhawley5@gmail.com")
                            .withType(BackgroundMail.TYPE_PLAIN)
                            .withSubject("Get in Touch")
                            .withBody(name.getText().toString().trim() +"\n"+
                                    email.getText().toString().trim() +"\n"+
                                    tv_message.getText().toString().trim() +"\n"+
                                    message.getText().toString().trim() +"\n"
                            )
                            .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                                @Override
                                public void onSuccess() {
                                    //do some magic

                                    name.setText("");
                                    email.setText("");
                                    tv_message.setText("");
                                    message.setText("");
                                }
                            })
                            .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                                @Override
                                public void onFail() {
                                    //do some magic
                                }
                            })
                            .send();

                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                    Toast.makeText(getApplicationContext(), "Error ", Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public final static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    protected int getPixelsFromDPs(int dps){
        Resources r = getApplicationContext().getResources();
        int  px = (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
        return px;
    }
}

