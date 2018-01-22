package stocksymbolapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stocksymbolapp.R;

import dmax.dialog.SpotsDialog;

public class GainersLosersList extends AppCompatActivity {


    DatabaseReference artistreference;
    FirebaseAuth auth;

    SpotsDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rest_list);


        dialog = new SpotsDialog(this);

        auth = FirebaseAuth.getInstance();
        dialog.show();

        artistreference = FirebaseDatabase.getInstance().getReference("gainers");


        artistreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    String value = zoneSnapshot.getValue().toString();
                    Log.i("value", value);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("gainers", "onCancelled", databaseError.toException());
            }
        });


    }


}