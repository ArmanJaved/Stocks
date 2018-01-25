package stocksymbolapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stocksymbolapp.R;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class WatchlistActivity extends AppCompatActivity {



    DatabaseReference artistreference;
    FirebaseAuth auth;
    ListView listViewartists;
    List<Watch> resaurantsList;


    public static final String CheckLogin = "ChkLogin";

    SpotsDialog dialog ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rest_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dialog = new SpotsDialog(this);

        LinearLayout stock= (LinearLayout)findViewById(R.id.stocksymbol);

        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WatchlistActivity.this, MainActivity.class));

            }
        });



        auth = FirebaseAuth.getInstance();



        dialog.show();

        artistreference = FirebaseDatabase.getInstance().getReference("WatchListsymbol").child("user");
        listViewartists = (ListView)findViewById(R.id.listviewitems);



        resaurantsList = new ArrayList<>();
        data_artist();


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

    public void data_artist()
    {

        artistreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        dialog.dismiss();

                        resaurantsList.clear();

                        resaurantsList.size();
//
                        WatchList adaptor = null;
                        for (DataSnapshot artistsnapshot: dataSnapshot.getChildren())
                        {

                            Watch resaurants = artistsnapshot.getValue(Watch.class);
                            resaurantsList.add(resaurants);

                             adaptor = new WatchList(WatchlistActivity.this, resaurantsList);
                            listViewartists.setAdapter(adaptor);
                        }

                        listViewartists.setAdapter(adaptor);
                        listViewartists.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                // TODO Auto-generated method stub


                                SharedPreferences.Editor editor = getSharedPreferences(CheckLogin, MODE_PRIVATE).edit();
                                editor.putInt("login_id", 1);
                                editor.apply();


//                                Toast.makeText(WatchlistActivity.this, resaurantsList.get(position).getRestname(), Toast.LENGTH_SHORT).show();

                                String symbol = resaurantsList.get(position).getRestname();
                                Intent intent = new Intent(WatchlistActivity.this, MainActivity.class);
                                intent.putExtra("symbol", symbol);
                                startActivity(intent);
                                WatchlistActivity.this.finish();


                            }
                        });



                        if(resaurantsList.isEmpty()){
                            AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(WatchlistActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(WatchlistActivity.this);
                            }
                            builder.setTitle("Error Fetching Resturants List")
                                    .setMessage("Not enough data")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            finish();

                                        }
                                    })
                                    .show();
                        }
                        //0 is the default value.

                    }
                }, 1000);






            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }





//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater mi = getMenuInflater();
//        mi.inflate(R.menu.extra,menu);
////        menu.add(menuactivity.NONE, MENU_DELETE, menuactivity.NONE, "Delete");
//        return super.onCreateOptionsMenu(menu);
//
//    }


}
