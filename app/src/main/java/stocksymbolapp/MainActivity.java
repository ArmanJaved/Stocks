package stocksymbolapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.PlaceHolderView;
import com.stocksymbolapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    ArrayList<String> labels = new ArrayList<String>();
    ArrayList<Float> labelsforvolume = new ArrayList<Float>();
    ArrayList<CandleEntry> entries = new ArrayList<>();
    int[] colors;
    private Spinner spinner1;
    CandleStickChart candleStickChart;
    RecyclerView rc_stock_list;
    List<String> stocklist = new ArrayList<>();
    HomeAdapter rc_adapter;
    Dialog d;
    TextView tv_stock_name;
    List<BarEntry> entries1 = new ArrayList<>();
    CandleDataSet dataset;


    public String URLTime;
    public String TimeSeries;
    public String func;

    SpotsDialog dialog;

    TextView start_D, end_D;
    TextView daily, weekly, monthly;

    LinearLayout intra_d;
    TextView intra_T;
    String Stock_N;

    TextView d1,w1,m3,y1,y5, all ;

    ArrayList<String> Xaxis_value = new ArrayList<String>();

    TextView percen_Symbol;
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private android.support.v7.widget.Toolbar mToolbar;
    private PlaceHolderView mGalleryView;

    BarChart chart;
    Character S_C_T;

    String Previous_Working_day;

    Character signal_api_call;
    static CountDownTimer timer;

    String Current_Date;

    String percentageSymbol;
    TextView  tv_high ;
    TextView  tv_low  ;
    TextView  tv_open ;
    TextView  tv_close;
    TextView  tv_volume;

    TextView x_time;
    LinearLayout llt_markerview;

    private DatabaseReference mDatabase;

    TextView volumetext;

    private ArrayAdapter<String> adapter;
    private List<String> liste;
    final String NewsList[] = {"Yahoo News", "Market Watch", "Bloomberg", "Seeking Alpha", "Stock Twits", "Twitter"};
    final String Fundament[] = {"Coming Soon!"};


    ListView simpleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);


        simpleList = (ListView)findViewById(R.id.simpleListView);



        final TextView funda = (TextView)findViewById(R.id.funda);
        final TextView news = (TextView)findViewById(R.id.news);

        funda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                funda.setBackgroundResource(R.drawable.border_set1);
                news.setBackgroundResource(R.drawable.border_set);
                simpleList.setVisibility(View.VISIBLE);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.activity_listview, R.id.textView, Fundament);
                simpleList.setAdapter(arrayAdapter);

            }
        });
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                funda.setBackgroundResource(R.drawable.border_set);
                news.setBackgroundResource(R.drawable.border_set1);
                simpleList.setVisibility(View.VISIBLE);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.activity_listview, R.id.textView, NewsList);
                simpleList.setAdapter(arrayAdapter);

                simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String text = NewsList[position];
                        Intent intent = new Intent(getBaseContext(), Webpage.class);
                        intent.putExtra("news", text);
                        startActivity(intent);


                    }
                });
            }
        });





        volumetext = (TextView)findViewById(R.id.verticaltextview);
// ...
        mDatabase = FirebaseDatabase.getInstance().getReference();


        llt_markerview = (LinearLayout) findViewById(R.id.llt_markerview);
        tv_high = (TextView) findViewById(R.id.tv_high);
        x_time = (TextView)findViewById(R.id.x_time);
        tv_low = (TextView) findViewById(R.id.tv_low);
        tv_open = (TextView) findViewById(R.id.tv_open);
        tv_close = (TextView) findViewById(R.id.tv_close);
        tv_volume = (TextView) findViewById(R.id.tv_volume);
        percen_Symbol = (TextView)findViewById(R.id.percentage_symbol);

        Date today = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -3);
        } else if (dayOfWeek == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, -2);
        } else {
            calendar.add(Calendar.DATE, -1);
        }

        Date previousBusinessDay = calendar.getTime();

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Today                : " + sdf.format(today));
        System.out.println("Previous business day: " + sdf.format(previousBusinessDay));

        Previous_Working_day = sdf.format(previousBusinessDay);


        S_C_T ='I';

        signal_api_call='I';

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        start_D = (TextView)findViewById(R.id.start);

        d1= (TextView)findViewById(R.id.d1);
        w1= (TextView)findViewById(R.id.w1);
        m3= (TextView)findViewById(R.id.m1);
        y1= (TextView)findViewById(R.id.y1);
        y5= (TextView)findViewById(R.id.y5);
        all= (TextView)findViewById(R.id.all);

        d1.setBackgroundResource(R.drawable.border_set1);

        d1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                percen_Symbol.setText("");
                URLTime="1min";
                S_C_T ='I';
                signal_api_call = 'I';
                TimeSeries = "Time Series (1min)";
                loadchartdata(Stock_N);
                GetPercentage(Stock_N);
                d1.setBackgroundResource(R.drawable.border_set1);
                w1.setBackgroundResource(R.drawable.border_set);
                m3.setBackgroundResource(R.drawable.border_set);
                y1.setBackgroundResource(R.drawable.border_set);
                y5.setBackgroundResource(R.drawable.border_set);
                all.setBackgroundResource(R.drawable.border_set);
            }
        });


        w1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                percen_Symbol.setText("");
                URLTime="5min";
                S_C_T ='I';
                signal_api_call = 'W';
                TimeSeries = "Time Series (5min)";
                loadchartdata(Stock_N);
                GetPercentage(Stock_N);
                w1.setBackgroundResource(R.drawable.border_set1);
                d1.setBackgroundResource(R.drawable.border_set);
                m3.setBackgroundResource(R.drawable.border_set);
                y1.setBackgroundResource(R.drawable.border_set);
                y5.setBackgroundResource(R.drawable.border_set);
                all.setBackgroundResource(R.drawable.border_set);
            }
        });

        m3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                percen_Symbol.setText("");
                S_C_T = 'D';
                signal_api_call ='M';
                GetPercentage(Stock_N);
                loadchartdata(Stock_N);
                m3.setBackgroundResource(R.drawable.border_set1);
                d1.setBackgroundResource(R.drawable.border_set);
                w1.setBackgroundResource(R.drawable.border_set);
                y1.setBackgroundResource(R.drawable.border_set);
                y5.setBackgroundResource(R.drawable.border_set);
                all.setBackgroundResource(R.drawable.border_set);
            }
        });
        y1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                percen_Symbol.setText("");
                S_C_T = 'D';
                signal_api_call ='Y';
                GetPercentage(Stock_N);
                loadchartdata(Stock_N);
                y1.setBackgroundResource(R.drawable.border_set1);
                m3.setBackgroundResource(R.drawable.border_set);
                d1.setBackgroundResource(R.drawable.border_set);
                w1.setBackgroundResource(R.drawable.border_set);
                y5.setBackgroundResource(R.drawable.border_set);
                all.setBackgroundResource(R.drawable.border_set);
            }
        });
        y5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                percen_Symbol.setText("");
                S_C_T = 'W';
                signal_api_call ='Z';
                GetPercentage(Stock_N);
                loadchartdata(Stock_N);
                y5.setBackgroundResource(R.drawable.border_set1);
                m3.setBackgroundResource(R.drawable.border_set);
                d1.setBackgroundResource(R.drawable.border_set);
                w1.setBackgroundResource(R.drawable.border_set);
                y1.setBackgroundResource(R.drawable.border_set);
                all.setBackgroundResource(R.drawable.border_set);
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                percen_Symbol.setText("");
                S_C_T = 'M';
                signal_api_call ='A';
                GetPercentage(Stock_N);
                loadchartdata(Stock_N);
                all.setBackgroundResource(R.drawable.border_set1);
                m3.setBackgroundResource(R.drawable.border_set);
                d1.setBackgroundResource(R.drawable.border_set);
                w1.setBackgroundResource(R.drawable.border_set);
                y1.setBackgroundResource(R.drawable.border_set);
                y5.setBackgroundResource(R.drawable.border_set);
            }
        });
        final Button watch_L = (Button)findViewById(R.id.watch_list);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mDrawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView)findViewById(R.id.drawerView);
        mToolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        mGalleryView = (PlaceHolderView)findViewById(R.id.galleryView);
        setupDrawer();


        dialog = new SpotsDialog(this);
        dialog.show();
        candleStickChart = (CandleStickChart) findViewById(R.id.chart);
        tv_stock_name = (TextView)findViewById(R.id.tv_stock_name);
        candleStickChart.setOnChartValueSelectedListener(this);



        EditText rlt_stock = (EditText) findViewById(R.id.rlt_stock);
        rlt_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d = new Dialog(MainActivity.this);

                watch_L.setText("Add to Watchlist");
                watch_L.setEnabled(true);
                watch_L.setTextColor(Color.parseColor("#000000"));
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

                        }else{
                            stocklist.clear();
                            loadstockdialog("", false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                rc_stock_list.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false));
                rc_adapter = new HomeAdapter(stocklist,MainActivity.this);
                rc_stock_list.setAdapter(rc_adapter);
                stocklist.clear();
                loadstockdialog("",false);
                d.show();
            }
        });






        Stock_N = "AAME";
        watch_L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                watch_L.setText("Added");
                watch_L.setEnabled(false);
                watch_L.setTextColor(Color.parseColor("#000000"));
                AddData obj = new AddData(getApplicationContext());
                obj.addartist (String.valueOf(Stock_N), percentageSymbol);
            }
        });
        URLTime="1min";
        func ="TIME_SERIES_INTRADAY";
        TimeSeries = "Time Series (1min)";

        String extraStr;
        try {
            extraStr = getIntent().getExtras().getString("symbol");
            loadchartdata(extraStr);
            GetPercentage(extraStr);
        } catch (NullPointerException e ) {
//            extraStr = "something_else";
            loadchartdata("AB");
            GetPercentage("AB");
        }

//        dialog.dismiss();


        LinearLayout watchlist= (LinearLayout)findViewById(R.id.watchlistsymobol);

        watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WatchlistActivity.class));
                MainActivity.this.finish();

            }
        });

        chart = (BarChart) findViewById(R.id.chart1);



    }
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //your code
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //your code

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

    private void setupchart(){
        dataset = new CandleDataSet(entries,"Entries");
        dataset.setDecreasingColor(Color.parseColor("#E74C3C"));
        dataset.setDecreasingPaintStyle(Paint.Style.FILL_AND_STROKE);
        dataset.setIncreasingColor(Color.parseColor("#1D8348"));
        dataset.setIncreasingPaintStyle(Paint.Style.FILL_AND_STROKE);
        dataset.setDrawValues(false);
        dataset.setNeutralColor(Color.parseColor("#1D8348"));
        dataset.setShadowColorSameAsCandle(true);

        CandleData data = new CandleData(dataset);
        dataset.setColors(colors);
        candleStickChart.setData(data);
        candleStickChart.setDescription(null);
        candleStickChart.getLegend().setEnabled(false);
        dataset.notifyDataSetChanged();
        candleStickChart.notifyDataSetChanged();

        candleStickChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e instanceof CandleEntry) {
                    CandleEntry ce = (CandleEntry) e;


                    String asdf= String.valueOf( ce.getX());
                    tv_high.setText("High: " + ce.getHigh());
                    tv_low.setText("Low: " + ce.getLow());
                    tv_open.setText("Open: " + ce.getOpen());
                    tv_close.setText("Close: " + ce.getClose());
                    tv_volume.setText("Volume: " +  getStringofvolume(labelsforvolume.get(candleStickChart.getCandleData().getDataSetForEntry(e).getEntryIndex(ce))));
                    x_time.setText("Time: " +  Xaxis_value.get(candleStickChart.getCandleData().getDataSetForEntry(e).getEntryIndex(ce)));

                    llt_markerview.setVisibility(View.VISIBLE);

                    if (timer != null)
                        timer.cancel();
                    timer = new CountDownTimer(5000, 1000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {

                            llt_markerview.setVisibility(View.GONE);

                        }
                    };
                    timer.start();
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        candleStickChart.invalidate();


        XAxis xAxis1 = candleStickChart.getXAxis();
        xAxis1.setValueFormatter(new IndexAxisValueFormatter(Xaxis_value));
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis1.setTextSize(5);
        dialog.dismiss();


        YAxis leftYAxis = candleStickChart.getAxisLeft();
        leftYAxis.setEnabled(false);


    }
    private void loadchartdata(final String mystock) {


        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference users = root.child("Symbols");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(Stock_N).child(String.valueOf(signal_api_call)).exists()) {
                    // run some code
//                    Toast.makeText(getApplicationContext(), "Good JOB", Toast.LENGTH_LONG).show();
                    labels.clear();
                    Xaxis_value.clear();
                    entries.clear();
                    entries1.clear();
                    labelsforvolume.clear();

                    FirebaseDataFetch();
                    String asf ="";
                }

                else if (!snapshot.child(Stock_N).child(String.valueOf(signal_api_call)).exists()) {
                    // run some code
                    View_User_Logs(mystock);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

//                Toast.makeText(getApplicationContext(), "Good", Toast.LENGTH_LONG).show();


            }

        });
        dialog.show();
        tv_stock_name.setText(mystock);

        Stock_N = mystock;




    }
    public void View_User_Logs(String stockname) {


        String url = null;

        if ( S_C_T =='D')
        {

            TimeSeries = "Time Series (Daily)";
            url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="+stockname+"&outputsize=full&apikey=WKMM7FGAW7V19SLN";
        }
        else if ( S_C_T =='W')
        {
            TimeSeries = "Weekly Time Series";
            url = "https://www.alphavantage.co/query?function=TIME_SERIES_WEEKLY&symbol="+stockname+"&apikey=WKMM7FGAW7V19SLN";
        }
        else if ( S_C_T =='M')
        {
            TimeSeries = "Monthly Time Series";
            url = "https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY&symbol="+stockname+"&apikey=WKMM7FGAW7V19SLN";
        }
        else if (S_C_T =='I')
        {
            url = "https://www.alphavantage.co/query?function="+func+"&symbol="+stockname+"&interval="+URLTime+"&outputsize=full&apikey=WKMM7FGAW7V19SLN";
        }


        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        labels.clear();
                        Xaxis_value.clear();
                        entries.clear();
                        entries1.clear();
                        labelsforvolume.clear();

                        parseJsonData(response.toString());

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }


                }

        ) {

        };

        int x=2;// retry count
        postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }




    public void GetPercentage(String stockname) {


        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="+stockname+"&outputsize=full&apikey=U3BSFX821P0F5N5W";


        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        CalculatePercentage(response.toString());

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }


                }

        ) {

        };

        int x=2;// retry count
        postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }



    void CalculatePercentage(String jsonString) {


        String close = "";
        String current_close = null;
        String yesterday_close = null;

        float tot_difference;
        float tot_percent;


        String key = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONObject jsonChildObject = (JSONObject)jsonObject.get("Time Series (Daily)");

            JSONArray objnames = jsonChildObject.names();
            for(int j = 0 ; j < 3; j++) {
                key = objnames.getString(j);

                close = String.valueOf(((JSONObject) jsonChildObject.get(key)).get("4. close"));
                if (j ==0 )
                {
                    current_close = close;
                }
                if (j ==1)
                {
                    yesterday_close = close;
                }
            }


            tot_difference = (Float.parseFloat(current_close) - Float.parseFloat(yesterday_close));


            tot_percent =((tot_difference/Float.parseFloat(yesterday_close))*100);


            DecimalFormat f = new DecimalFormat("##.00");

            float value= Float.parseFloat(f.format(tot_difference));

            float total_per= Float.parseFloat(f.format(tot_percent));
            if (tot_difference > 0)
            {
                percentageSymbol =  String.valueOf(total_per);
                percen_Symbol.setText("+"+String.valueOf(value) +" (+" + String.valueOf(total_per)+"%)");
                percen_Symbol.setTextColor(Color.parseColor("#63bd55"));
            }
            else {
                percentageSymbol = String.valueOf(total_per);
                percen_Symbol.setText(String.valueOf(value) +" (" + String.valueOf(total_per)+"%)");
                percen_Symbol.setTextColor(Color.parseColor("#8b0000"));
            }







        } catch (JSONException e) {
            e.printStackTrace();
        }





    }




    void parseJsonData(String jsonString) {
        String open = "";
        String high = "";
        String low = "";
        String close = "";
        String volume = "";

        String []chose_week;
        int week_date = 0;
        int month_date = 0;

        int year = 0;


        String[] split;


        try {
            int i = 0;
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject jsonChildObject = (JSONObject)jsonObject.get(TimeSeries);
            int length = jsonChildObject.length();

            colors = null;
            String date= "";
            colors = new int[length];
            Iterator iterator = jsonChildObject.keys();
            String key = null;

            JSONArray objnames = jsonChildObject.names();
            for(int j = objnames.length()-1; j > 0; j--) {
                key = objnames.getString(j);

                if (S_C_T == 'D' || S_C_T == 'W' || S_C_T == 'M') {
                    Xaxis_value.add(String.valueOf(key));
                    chose_week = key.split("-");
                    month_date = Integer.parseInt(chose_week[1]);
                    year = Integer.parseInt(chose_week[0]);


                } else if (S_C_T == 'I') {
                    split = key.split(" ");
                    date = split[0];

                    chose_week = date.split("-");
                    week_date = Integer.parseInt(chose_week[2]);

                    Xaxis_value.add(String.valueOf(split[1]));
                }

                high = String.valueOf(((JSONObject) jsonChildObject.get(key)).get("2. high"));
                low = String.valueOf(((JSONObject) jsonChildObject.get(key)).get("3. low"));
                open = String.valueOf(((JSONObject) jsonChildObject.get(key)).get("1. open"));
                close = String.valueOf(((JSONObject) jsonChildObject.get(key)).get("4. close"));
                volume = String.valueOf(((JSONObject) jsonChildObject.get(key)).get("5. volume"));


                if (Float.valueOf(close) >= Float.valueOf(open)) {
                    colors[i] = Color.parseColor("#1D8348");
                }
                else {
                    colors[i] = Color.parseColor("#E74C3C");
                }

                if (date.contains(Previous_Working_day) && signal_api_call == 'I') {

                    mDatabase.child("Symbols").child(Stock_N).child(String.valueOf(signal_api_call)).child(key).setValue(high + "," + low  + "," + open + "," + close + "," + volume);
                    entries.add(new CandleEntry(i, Float.valueOf(high), Float.valueOf(low), Float.valueOf(open), Float.valueOf(close)));
                    labels.add(key);
                    entries1.add(new BarEntry(i, Float.valueOf(volume)));
                    labelsforvolume.add(Float.valueOf(volume));
                }

                else if (signal_api_call=='W' && 22 - week_date <=5)
                {

                    mDatabase.child("Symbols").child(Stock_N).child(String.valueOf(signal_api_call)).child(key).setValue(high + "," + low  + "," + open + "," + close + "," + volume);
                    entries.add(new CandleEntry(i, Float.valueOf(high), Float.valueOf(low), Float.valueOf(open), Float.valueOf(close)));
                    labels.add(key);
                    entries1.add(new BarEntry(i, Float.valueOf(volume)));
                    labelsforvolume.add(Float.valueOf(volume));
                }

                else if (signal_api_call=='M' && 12 - month_date <=3  && year ==2017)
                {
                    mDatabase.child("Symbols").child(Stock_N).child(String.valueOf(signal_api_call)).child(key).setValue(high + "," + low  + "," + open + "," + close + "," + volume);
                    entries.add(new CandleEntry(i, Float.valueOf(high), Float.valueOf(low), Float.valueOf(open), Float.valueOf(close)));
                    labels.add(key);
                    entries1.add(new BarEntry(i, Float.valueOf(volume)));
                    labelsforvolume.add(Float.valueOf(volume));
                }

                else if (signal_api_call=='Y' && 12 - month_date <=10 && year ==2017)
                {
                    mDatabase.child("Symbols").child(Stock_N).child(String.valueOf(signal_api_call)).child(key).setValue(high + "," + low  + "," + open + "," + close + "," + volume);
                    entries.add(new CandleEntry(i, Float.valueOf(high), Float.valueOf(low), Float.valueOf(open), Float.valueOf(close)));
                    labels.add(key);
                    entries1.add(new BarEntry(i, Float.valueOf(volume)));
                    labelsforvolume.add(Float.valueOf(volume));
                }


                else if (signal_api_call=='Z' && 2017 - year <=5)
                {
                    mDatabase.child("Symbols").child(Stock_N).child(String.valueOf(signal_api_call)).child(key).setValue(high + "," + low  + "," + open + "," + close + "," + volume);
                    entries.add(new CandleEntry(i, Float.valueOf(high), Float.valueOf(low), Float.valueOf(open), Float.valueOf(close)));
                    labels.add(key);
                    entries1.add(new BarEntry(i, Float.valueOf(volume)));
                    labelsforvolume.add(Float.valueOf(volume));
                }

                else if (signal_api_call=='A')
                {
                    mDatabase.child("Symbols").child(Stock_N).child(String.valueOf(signal_api_call)).child(key).setValue(high + "," + low  + "," + open + "," + close + "," + volume);
                    entries.add(new CandleEntry(i, Float.valueOf(high), Float.valueOf(low), Float.valueOf(open), Float.valueOf(close)));
                    labels.add(key);
                    entries1.add(new BarEntry(i, Float.valueOf(volume)));
                    labelsforvolume.add(Float.valueOf(volume));
                }

                float a = Collections.max(labelsforvolume);

                if (a >1000)
                {
                    volumetext.setText("ThOUSAND");
                }
                else if (a >100000)
                {
                    volumetext.setText("MILLION");
                }

                i++;
            }
            setupchart();

        } catch (JSONException e1) {
            e1.printStackTrace();

        }



        BarDataSet set = new BarDataSet(entries1, "");
        set.setValueTextSize(2);
        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refre
        chart.setDescription(null);
        chart.setDescription(null);    // Hide the description
        chart.getLegend().setEnabled(false);


        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(false);
        xAxis.setDrawAxisLine(false);

        YAxis leftYAxis = chart.getAxisLeft();
        leftYAxis.setEnabled(false);

        YAxis rightYAxis = chart.getAxisRight();
        rightYAxis.setEnabled(true);
//                    rightYAxis.setMaxWidth(15);

            rightYAxis.setLabelCount(2, true);
//            rightYAxis.setSpaceMax(3);
            rightYAxis.setAxisLineWidth(3);
            rightYAxis.setAxisMaximum(3);
            rightYAxis.setMaxWidth(3);

    }

    void FirebaseDataFetch ()
    {

        String asd ="";
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child("Symbols").child(Stock_N).child(String.valueOf(signal_api_call)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialog.dismiss();

                String open = "";
                String high = "";
                String low = "";
                String close = "";
                String volume = "";

                String []chose_week;
                int week_date = 0;
                int month_date = 0;

                int year = 0;


                String[] split;

                String [] stockdatasplit;

                int i = 0;
                colors = null;
                String date = "";
                colors = new int[1000];
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                    String key = noteDataSnapshot.getKey();
                    String value = String.valueOf(noteDataSnapshot.getValue());



                    try {



                        if (S_C_T == 'D' || S_C_T == 'W' || S_C_T == 'M') {
                            Xaxis_value.add(String.valueOf(key));
                            chose_week = key.split("-");
                            month_date = Integer.parseInt(chose_week[1]);
                            year = Integer.parseInt(chose_week[0]);


                        } else if (S_C_T == 'I') {
                            split = key.split(" ");
                            date = split[0];

                            chose_week = date.split("-");
                            week_date = Integer.parseInt(chose_week[2]);
                            Xaxis_value.add(String.valueOf(split[1]));
                        }


                        stockdatasplit = value.split(",");

                        high = stockdatasplit[0];
                        low = stockdatasplit[1];
                        open = stockdatasplit[2];
                        close = stockdatasplit[3];
                        volume = stockdatasplit[4];
                        if (Float.valueOf(close) >= Float.valueOf(open)) {
                            colors[i] = Color.parseColor("#1D8348");
                        } else {
                            colors[i] = Color.parseColor("#E74C3C");
                        }

                        if (date.contains(Previous_Working_day) && signal_api_call == 'I') {

//                            mDatabase.child("Symbols").child(Stock_N).child("I").child(key).setValue(high + "," + low + "," + open + "," + close);
                            entries.add(new CandleEntry(i, Float.valueOf(high), Float.valueOf(low), Float.valueOf(open), Float.valueOf(close)));
                            labels.add(key);
                            entries1.add(new BarEntry(i, Float.valueOf(volume)));
                            labelsforvolume.add(Float.valueOf(volume));

                        } else if (signal_api_call == 'W' && 22 - week_date <= 5) {
                            entries.add(new CandleEntry(i, Float.valueOf(high), Float.valueOf(low), Float.valueOf(open), Float.valueOf(close)));
                            labels.add(key);
                            entries1.add(new BarEntry(i, Float.valueOf(volume)));
                            labelsforvolume.add(Float.valueOf(volume));
                        } else if (signal_api_call == 'M' && 12 - month_date <= 3 && year == 2017) {
                            entries.add(new CandleEntry(i, Float.valueOf(high), Float.valueOf(low), Float.valueOf(open), Float.valueOf(close)));
                            labels.add(key);
                            entries1.add(new BarEntry(i, Float.valueOf(volume)));
                            labelsforvolume.add(Float.valueOf(volume));
                        } else if (signal_api_call == 'Y' && 12 - month_date <= 10 && year == 2017) {
                            entries.add(new CandleEntry(i, Float.valueOf(high), Float.valueOf(low), Float.valueOf(open), Float.valueOf(close)));
                            labels.add(key);
                            entries1.add(new BarEntry(i, Float.valueOf(volume)));
                            labelsforvolume.add(Float.valueOf(volume));
                        } else if (signal_api_call == 'Z' && 2017 - year <= 5) {
                            entries.add(new CandleEntry(i, Float.valueOf(high), Float.valueOf(low), Float.valueOf(open), Float.valueOf(close)));
                            labels.add(key);
                            entries1.add(new BarEntry(i, Float.valueOf(volume)));
                            labelsforvolume.add(Float.valueOf(volume));
                        } else if (signal_api_call == 'A') {
                            entries.add(new CandleEntry(i, Float.valueOf(high), Float.valueOf(low), Float.valueOf(open), Float.valueOf(close)));
                            labels.add(key);
                            entries1.add(new BarEntry(i, Float.valueOf(volume)));
                            labelsforvolume.add(Float.valueOf(volume));
                        }

                        float a = Collections.max(labelsforvolume);

                        if (a >1000)
                        {
                            volumetext.setText("ThOUSAND");
                        }
                        else if (a >100000)
                        {
                            volumetext.setText("MILLION");
                        }
                        i++;
                    }
                    catch (Exception e){
                    }

                    setupchart();


                    BarDataSet set = new BarDataSet(entries1, "");
                    set.setValueTextSize(2);
                    BarData data = new BarData(set);
                    data.setBarWidth(0.9f); // set custom bar width
                    chart.setData(data);
                    chart.setFitBars(true); // make the x-axis fit exactly all bars
                    chart.invalidate(); // refre
                    chart.setDescription(null);
                    chart.setDescription(null);    // Hide the description
                    chart.getLegend().setEnabled(false);


                    XAxis xAxis = chart.getXAxis();
                    xAxis.setEnabled(false);
                    xAxis.setDrawAxisLine(false);

                    YAxis leftYAxis = chart.getAxisLeft();
                    leftYAxis.setEnabled(false);


                    YAxis rightYAxis = chart.getAxisRight();
                    rightYAxis.setEnabled(true);
//                    rightYAxis.setMaxWidth(15);



                        rightYAxis.setLabelCount(2, true);
//                        rightYAxis.setSpaceMax(3);
//                        rightYAxis.setAxisLineWidth(3);
                        rightYAxis.setAxisMaximum(3);
                        rightYAxis.setMaxWidth(3);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }

    public String getStringofvolume(float value){
        String str=String.valueOf(value);
        if (str.contains(","))
            str = str.replace(",","");
        if(str.length() == 4){
            String temp = str.substring(1,2);
            String main = str.substring(0,1);
            if(temp.equalsIgnoreCase("0"))
                str = main + "K";
            else
                str = main + "." + temp + "K";
        }
        else  if(str.length() == 5){
            String temp = str.substring(2,3);
            String main = str.substring(0,2);
            if(temp.equalsIgnoreCase("0"))
                str = main + "K";
            else
                str = main + "." + temp + "K";
        }
        else  if(str.length() == 6){
            String temp = str.substring(3,4);
            String main = str.substring(0,3);
            if(temp.equalsIgnoreCase("0"))
                str = main + "K";
            else
                str = main + "." + temp + "K";
        }
        else  if(str.length() == 7){
            String temp = str.substring(1,2);
            String main = str.substring(0,1);
            if(temp.equalsIgnoreCase("0"))
                str = main + "M";
            else
                str =main + "." + temp + "M";
        }
        else  if(str.length() == 8){
            String temp = str.substring(2,3);
            String main = str.substring(0,2);
            if(temp.equalsIgnoreCase("0"))
                str = main + "M";
            else
                str =main + "." + temp + "M";
        }
        else  if(str.length() == 9){
            String temp = str.substring(3,4);
            String main = str.substring(0,3);
            if(temp.equalsIgnoreCase("0"))
                str = main + "M";
            else
                str = main + "." + temp + "M";
        }
        else  if(str.length() == 10){
            String temp = str.substring(1,2);
            String main = str.substring(0,1);
            if(temp.equalsIgnoreCase("0"))
                str = main + "B";
            else
                str = main + "." + temp + "B";
        }else  if(str.length() == 11){
            String temp = str.substring(2,3);
            String main = str.substring(0,2);
            if(temp.equalsIgnoreCase("0"))
                str = main + "B";
            else
                str = main + "." + temp + "B";
        }
        else  if(str.length() == 12){
            String temp = str.substring(3,4);
            String main = str.substring(0,3);
            if(temp.equalsIgnoreCase("0"))
                str = main + "B";
            else
                str = main + "." + temp + "B";
        }
        else  if(str.length() == 13){
            String temp = str.substring(1,2);
            String main = str.substring(0,1);
            if(temp.equalsIgnoreCase("0"))
                str = main + "T";
            else
                str = main + temp + "T";
        }else  if(str.length() == 14){
            String temp = str.substring(2,3);
            String main = str.substring(0,2);
            if(temp.equalsIgnoreCase("0"))
                str = main + "T";
            else
                str = main + "." + temp + "T";
        }
        else  if(str.length() == 15){
            String temp = str.substring(3,4);
            String main = str.substring(0,3);
            if(temp.equalsIgnoreCase("0"))
                str = main + "T";
            else
                str = main + "." + temp + "T";
        }
        return str;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {


//        Toast.makeText(this, "Stock Data"+ e.toString() + " H " + h.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

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
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_stoeck_name, parent, false);

            return new MyViewHolder(itemView);
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
                    percen_Symbol.setText("");
                    loadchartdata(holder.stockname.getText().toString());
                    GetPercentage(holder.stockname.getText().toString());

                }
            });
        }

        @Override
        public int getItemCount() {
            return trend_list.size();
        }

    }


}
