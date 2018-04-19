package stocksymbolapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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

public class GainernLosers extends AppCompatActivity  {




    FirebaseAuth auth;
    ListView listViewartists;
    ListView loserlist;
    Dialog d;
    private PlaceHolderView mDrawerView;
    HomeAdapter rc_adapter;
    RecyclerView rc_stock_list;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private PlaceHolderView mGalleryView;
    List<String> stocklist = new ArrayList<>();

    public static final String CheckLogin = "ChkLogin";
    private GainerRecycleradapter recyclergaineradapter;

    private LosersRecycleradapter recyclerloseradapter;

    RecyclerView recyclerViewgainer;
    RecyclerView recyclerViewlosers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gainernlosermain);




        // set up the RecyclerView
        recyclerViewgainer = (RecyclerView) findViewById(R.id.gainerlist);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(GainernLosers.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewgainer.setLayoutManager(horizontalLayoutManagaer);


        // set up the RecyclerView
        recyclerViewlosers = (RecyclerView) findViewById(R.id.loserlist);
        horizontalLayoutManagaer = new LinearLayoutManager(GainernLosers.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewlosers.setLayoutManager(horizontalLayoutManagaer);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();

        mDrawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView)findViewById(R.id.drawerView);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mGalleryView = (PlaceHolderView)findViewById(R.id.galleryView);
        setupDrawer();

        LinearLayout watchlist= (LinearLayout)findViewById(R.id.watchlistsymobol);

        watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GainernLosers.this, WatchlistActivity.class));
                GainernLosers.this.finish();

            }
        });

        EditText rlt_stock = (EditText) findViewById(R.id.rlt_stock);
        rlt_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                d = new Dialog(GainernLosers.this);
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
                rc_stock_list.setLayoutManager(new LinearLayoutManager(GainernLosers.this,LinearLayoutManager.VERTICAL,false));
                rc_adapter = new GainernLosers.HomeAdapter(stocklist,GainernLosers.this);
                rc_stock_list.setAdapter(rc_adapter);
                stocklist.clear();
                loadstockdialog("",false);
                d.show();
            }
        });



        final Button topgain = (Button)findViewById(R.id.topgainers);
        final Button toplos = (Button)findViewById(R.id.toplosers) ;

        toplos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text=toplos.getText().toString();
                if (text.toLowerCase().contains("losers"))
                {
                    topgain.setText("Top Losers");
                    toplos.setText("Top Gainers");
                    recyclerViewlosers.setVisibility(View.VISIBLE);
                    recyclerViewgainer.setVisibility(View.GONE);
                }
                else if (text.toLowerCase().contains("gainers")){
                    topgain.setText("Top Gainers");
                    toplos.setText("Top Losers");
                    recyclerViewlosers.setVisibility(View.GONE);
                    recyclerViewgainer.setVisibility(View.VISIBLE);
                }



            }
        });
        gainers();
        losers();


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

    public class HomeAdapter extends RecyclerView.Adapter<GainernLosers.HomeAdapter.MyViewHolder> {

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
        public GainernLosers.HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_stoeck_name, parent, false);

            return new GainernLosers.HomeAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
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


        Intent intent = new Intent(GainernLosers.this, MainActivity.class);
        intent.putExtra("symbol", mystock);
        startActivity(intent);
        GainernLosers.this.finish();


    }

    public void gainers()
    {

        final List<String> resaurantsList;
        resaurantsList = new ArrayList<>();

        DatabaseReference artistreference = FirebaseDatabase.getInstance().getReference("Gainers");
        artistreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {


                        resaurantsList.clear();
                        resaurantsList.size();


                        for (DataSnapshot artistsnapshot: dataSnapshot.getChildren())
                        {

                            String key = artistsnapshot.getKey();
                            String value = String.valueOf(artistsnapshot.getValue());
                            resaurantsList.add(value);
                        }


                        recyclergaineradapter = new GainerRecycleradapter(GainernLosers.this, resaurantsList);
                        recyclerViewgainer.setAdapter(recyclergaineradapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void losers()
    {
        final List<String> resaurantsList;
        resaurantsList = new ArrayList<>();

        DatabaseReference artistreference = FirebaseDatabase.getInstance().getReference("Losers");
        artistreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {


                resaurantsList.clear();
                resaurantsList.size();

                for (DataSnapshot artistsnapshot: dataSnapshot.getChildren())
                {

                    String key = artistsnapshot.getKey();
                    String value = String.valueOf(artistsnapshot.getValue());
                    resaurantsList.add(value);

                }

                recyclerloseradapter = new LosersRecycleradapter(GainernLosers.this, resaurantsList);
                recyclerViewlosers.setAdapter(recyclerloseradapter);





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }






}
