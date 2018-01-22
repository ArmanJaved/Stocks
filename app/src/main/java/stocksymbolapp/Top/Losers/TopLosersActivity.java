package stocksymbolapp.Top.Losers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stocksymbolapp.R;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import stocksymbolapp.ClicktoSymbol;
import stocksymbolapp.MainActivity;
import stocksymbolapp.WatchlistActivity;

public class TopLosersActivity extends AppCompatActivity {


    ArrayList<LosersDataModel> dataModels;
    ListView listView;
    private static LosersCustomAdapter adapter;

    DatabaseReference artistreference;
    FirebaseAuth auth;

    SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.losers);
        dataModels= new ArrayList<>();
        listView=(ListView)findViewById(R.id.list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = new SpotsDialog(this);
        dialog.show();

        auth = FirebaseAuth.getInstance();

        artistreference = FirebaseDatabase.getInstance().getReference("Losers");


        artistreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    String value = zoneSnapshot.getValue().toString();

                    String[] Gainer_Values= value.split(",");
                    dataModels.add(new LosersDataModel(Gainer_Values[0],"", Gainer_Values[2], Gainer_Values[3], Gainer_Values[4], "", Gainer_Values[1] ));
                    Log.i("value", value);
                }
                dialog.dismiss();

                adapter = new LosersCustomAdapter(dataModels, getApplicationContext());

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        LosersDataModel dataModel = dataModels.get(position);
                        String symbol = dataModel.getAsp_name();

                        Intent intent = new Intent(TopLosersActivity.this, ClicktoSymbol.class);
                        intent.putExtra("symbol", symbol);
                        startActivity(intent);
                        TopLosersActivity.this.finish();


//                        Toast.makeText(getApplicationContext(), symbol, Toast.LENGTH_LONG).show();


                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("gainers", "onCancelled", databaseError.toException());
            }
        });






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
