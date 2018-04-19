package stocksymbolapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.PlaceHolderView;
import com.stocksymbolapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class WatchlistActivity extends AppCompatActivity {



    DatabaseReference artistreference;
    FirebaseAuth auth;
    ListView listViewartists;
    List<Watch> resaurantsList;
    SpotsDialog dialog ;

    TextView hold_down ;

    private PlaceHolderView mDrawerView;
    WatchlistActivity.HomeAdapter rc_adapter;
    RecyclerView rc_stock_list;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private PlaceHolderView mGalleryView;
    Dialog d;
    List<String> stocklist = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watchlist);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dialog = new SpotsDialog(this);
        hold_down = (TextView)findViewById(R.id.holddown);
        hold_down.setVisibility(View.INVISIBLE);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().hide();
        LinearLayout stock= (LinearLayout)findViewById(R.id.stocksymbol);

        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WatchlistActivity.this, MainActivity.class));

            }
        });



        auth = FirebaseAuth.getInstance();


        EditText rlt_stock = (EditText) findViewById(R.id.rlt_stock);
        rlt_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                d = new Dialog(WatchlistActivity.this);
                d.setContentView(R.layout.dialog_stocks_list);
                d.setTitle("Select Stock");
                rc_stock_list = (RecyclerView) d.findViewById(R.id.rc_stock);
                EditText et_stock_search = (EditText)d.findViewById(R.id.et_stock_name);
                et_stock_search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(charSequence.toString().trim().length() > 0) {
                            stocklist.clear();
                            loadstockdialog(charSequence.toString().trim(), true);

                        }
                        else{
                            stocklist.clear();
                            loadstockdialog("", false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                rc_stock_list.setLayoutManager(new LinearLayoutManager(WatchlistActivity.this,LinearLayoutManager.VERTICAL,false));
                rc_adapter = new WatchlistActivity.HomeAdapter(stocklist,WatchlistActivity.this);
                rc_stock_list.setAdapter(rc_adapter);
                stocklist.clear();
                loadstockdialog("",false);
                d.show();
            }
        });

        dialog.show();
       String android_user_Id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        artistreference = FirebaseDatabase.getInstance().getReference("WatchListsymbol").child(android_user_Id);
        listViewartists = (ListView)findViewById(R.id.listviewitems);



        resaurantsList = new ArrayList<>();
        data_artist();

        mDrawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView)findViewById(R.id.drawerView);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mGalleryView = (PlaceHolderView)findViewById(R.id.galleryView);
        setupDrawer();


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

    private void setupDrawer(){
        mDrawerView
                .addView(new DrawerHeader(this.getApplicationContext()))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_PROFILE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_REQUESTS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_MESSAGE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_GROUPS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_NOTIFICATIONS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_TERMS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_SETTINGS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_LOGOUT));

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }


    private void loadstockdialog(String stockname, boolean isname) {

        BufferedReader reader = null;
        String [] list;
        try {
            list = getAssets().list("stock");
            if (list.length > 0) {
                // This is a folder

                try {
                    reader = new BufferedReader(
                            new InputStreamReader(getAssets().open("stock/"+list[0]), "UTF-8"));
                    int k = 0;
                    String mLine;
                    while ((mLine = reader.readLine()) != null) {
                        String[] myarr = mLine.split(",");
                        if(k>0){
                            if(isname){
                                if(myarr[0].toLowerCase().startsWith(stockname.toLowerCase())){
                                    stocklist.add(myarr[0]);
                                    rc_adapter.notifyDataSetChanged();
                                }
                            }else{
                                stocklist.add(myarr[0]);
                                rc_adapter.notifyDataSetChanged();
                            }
                        }
                        k++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            //log the exception
                        }
                    }
                }

            }
        } catch (IOException e) {

        }
    }

    public class HomeAdapter extends RecyclerView.Adapter<WatchlistActivity.HomeAdapter.MyViewHolder> {

        public Context context;
        public List<String> trend_list;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView stockname;

            public MyViewHolder(View view) {
                super(view);
                stockname = (TextView)view.findViewById(R.id.tv_stock_name);

            }
        }

        public HomeAdapter(List<String> trendList, Context context) {
            this.trend_list = trendList;
            this.context = context;
        }

        @Override
        public WatchlistActivity.HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_stoeck_name, parent, false);

            return new WatchlistActivity.HomeAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final WatchlistActivity.HomeAdapter.MyViewHolder holder, int position) {
            holder.stockname.setText(trend_list.get(position));
            holder.stockname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(d!=null) {
                        if (d.isShowing())
                            d.dismiss();
                    }

                    loadchartdata(holder.stockname.getText().toString());

                }
            });
        }


        @Override
        public int getItemCount() {
            return trend_list.size();
        }

    }

    private void loadchartdata(final String mystock) {


        Intent intent = new Intent(WatchlistActivity.this, MainActivity.class);
        intent.putExtra("symbol", mystock);
        startActivity(intent);
        WatchlistActivity.this.finish();


    }

    public void data_artist()
    {

        artistreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                        dialog.dismiss();

                        resaurantsList.clear();
                        resaurantsList.size();

                        WatchList adaptor = null;
                        for (DataSnapshot artistsnapshot: dataSnapshot.getChildren())
                        {

                            Watch resaurants = artistsnapshot.getValue(Watch.class);
                            resaurantsList.add(resaurants);

                             adaptor = new WatchList(WatchlistActivity.this, resaurantsList);
                            listViewartists.setAdapter(adaptor);
                        }

                        hold_down.setVisibility(View.VISIBLE);
                        listViewartists.setAdapter(adaptor);
                        listViewartists.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                // TODO Auto-generated method stub


                                String symbol = resaurantsList.get(position).getRestname();
                                Intent intent = new Intent(WatchlistActivity.this, MainActivity.class);
                                intent.putExtra("symbol", symbol);
                                startActivity(intent);
                                WatchlistActivity.this.finish();


                            }
                        });

                        listViewartists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                           final int pos, long id) {
                                // TODO Auto-generated method stub

//                                Toast.makeText(getApplicationContext(), "Long click ", Toast.LENGTH_LONG).show();

                                AlertDialog.Builder builder = new AlertDialog.Builder(WatchlistActivity.this);

//                                // Set a title for alert dialog
                                builder.setTitle("Are you sure you watch to remove from watchlist?");

//                                // Setting Icon to Dialog
//                                builder.setIcon(R.drawable.delete);

                                // Show a message on alert dialog
//                                builder.setMessage("Are you sure you want to become a monkey?");

                                // Set the positive button
                                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int which) {

                                        // Write your code here to invoke YES event
//                                        Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                        Watch symbolobj = resaurantsList.get(pos);
                                        DeleteSymbol(symbolobj.getRestId());

                                    }
                                });

                                // Set the negative button
                                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to invoke NO event
//                                        Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }
                                });

                                // Set the neutral button
//                                builder.setNeutralButton("Cancel", null);


                                // Create the alert dialog
                                AlertDialog dialog = builder.create();

                                // Finally, display the alert dialog
                                dialog.show();

                                // Get the alert dialog buttons reference
                                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                                Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

                                // Change the alert dialog buttons text and background color
                                positiveButton.setTextColor(Color.parseColor("#FFFFFF"));
                                positiveButton.setBackgroundColor(Color.parseColor("#1DC8A6"));

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                params.setMargins(20,0,0,0);
                                positiveButton.setLayoutParams(params);
                                positiveButton.setWidth(50);
                                positiveButton.setHeight(30);
                                positiveButton.setHeight(30);

                                negativeButton.setTextColor(Color.parseColor("#FFFFFF"));
                                negativeButton.setBackgroundColor(Color.parseColor("#E04C5E"));
                                negativeButton.setLayoutParams(params);
                                negativeButton.setWidth(50);
                                negativeButton.setHeight(30);

                                return true;
                            }
                        });


                        if(resaurantsList.isEmpty()){
                            hold_down.setVisibility(View.VISIBLE);
                            hold_down.setText("No Records Available.");
                        }
                        //0 is the default value.



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void DeleteSymbol(String symbolid) {

        String android_user_Id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        DatabaseReference databaseArtists = FirebaseDatabase.getInstance().getReference("WatchListsymbol").child(android_user_Id).child(symbolid);
        DatabaseReference databaseTracks = FirebaseDatabase.getInstance().getReference("WatchListsymbol").child(android_user_Id).child(symbolid);

        databaseArtists.removeValue();
        databaseTracks.removeValue();




    }





}
