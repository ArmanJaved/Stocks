package com.stocksymbolapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.series.DataPoint;
import com.mindorks.placeholderview.PlaceHolderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class MainActivity extends AppCompatActivity implements OnChartGestureListener,
        OnChartValueSelectedListener  {
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

    public static int  pagenumber =0;
    int stocksymindex;

    public String URLTime;
    public String TimeSeries;
    public String func;

    String [] stockdatasplit;

    TextView start_D, end_D;

    String finalweekdata;
    public LineChart mChart;
    String Stock_N;
    DataPoint[] dataPoints;

    TextView d1,w1,m3,y1,y5, all ;

    ArrayList<String> Xaxis_value = new ArrayList<String>();

    TextView percen_Symbol;
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private PlaceHolderView mGalleryView;

    BarChart chart;
    Character S_C_T;

    String Previous_Working_day;

    Character signal_api_call;
    static CountDownTimer timer;


    String percentageSymbol;
    TextView  tv_high ;
    TextView  tv_low  ;
    TextView  tv_open ;
    TextView  tv_close;
    TextView  tv_volume;

    TextView x_time;
    LinearLayout llt_markerview;

    private DatabaseReference mDatabase;

    String [] horizolab ;

    private ArrayAdapter<String> adapter;

    Button watch_L;
    private List<String> liste;
    final String NewsList[] = {"Yahoo News", "Market Watch", "Bloomberg", "Seeking Alpha", "Stock Twits", "Twitter"};
    final String Fundamentname[] = {"Market Cap", "Revenue (Year)", "Profit Margin", "P/E", "Basic EPS", "Cash Div/Share", "Short Interest", "Basic Shares OS"};

    public static final String CHART = "chart";

    ListView simpleList;
    private TextView mTextView;
    private static final String KEY_TEXT_VALUE = "textValue";

    DatabaseReference artistreference;

    EditText firstName, lastName, title, department;

    private static final String KEY_FIRSTNAME= "firstname_key";
    private static final String KEY_LASTNAME = "lastname_key";
    private static final String KEY_TITLE = "title_key";



    private String mFirstName, mLastName, mTitle;
    TextView first, last, mTit;



    ListView listViewfun;


    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);


        listViewfun = (ListView)findViewById(R.id.list);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = "Check out the great new stock market charting app! It has so many great features! http://splashcharts.com/download";
                String sharesub = "Stock App";
                intent.putExtra(Intent.EXTRA_SUBJECT, sharesub);
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, "Invite friends via..."));
            }
        });


        firstName = (EditText) findViewById(R.id.edit_employee_firstname);
        lastName = (EditText) findViewById(R.id.edit_employee_lastname);
        title = (EditText) findViewById(R.id.edit_employee_title);

        tv_stock_name = (TextView)findViewById(R.id.tv_stock_name);
        first = (TextView) findViewById(R.id.empFirst);
        last = (TextView) findViewById(R.id.empLast);
        mTit = (TextView) findViewById(R.id.empTitle);


        if (savedInstanceState != null) {
            String savedFirstName = savedInstanceState.getString(KEY_FIRSTNAME);
            tv_stock_name.setText(savedFirstName);

            String savedLastName = savedInstanceState.getString(KEY_LASTNAME);
            last.setText(savedLastName);

            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            mTit.setText(savedTitle);

        }else{
//            Toast.makeText(this, "New entry", Toast.LENGTH_SHORT).show();
        }


//        String sa = getMonth(8);

//        AddData obj = new AddData(getApplicationContext());
//        obj.addartist (String.valueOf("G"), "1.0");

        handleSSLHandshake();

//        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Previous_Working_day = sdf.format(lastDateOfPreviousWeek);

        getSupportActionBar().hide();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.setTitle(getString(R.string.app_name));
//        mToolbar.setNavigationIcon(R.drawable.backbutton);

        ImageView backbtn = (ImageView)findViewById(R.id.backbutton);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        startService(new Intent(this, LinkService.class));
        simpleList = (ListView)findViewById(R.id.simpleListView);
        final TextView funda = (TextView)findViewById(R.id.funda);
        final TextView news = (TextView)findViewById(R.id.news);


                funda.setBackgroundResource(R.drawable.border_set1);
                news.setBackgroundResource(R.drawable.border_set);
                simpleList.setVisibility(View.GONE);
                listViewfun.setVisibility(View.VISIBLE);

                funda.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        funda.setBackgroundResource(R.drawable.border_set1);
                        news.setBackgroundResource(R.drawable.border_set);
                        simpleList.setVisibility(View.GONE);
                        listViewfun.setVisibility(View.VISIBLE);

                    }
                });

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                funda.setBackgroundResource(R.drawable.border_set);
                news.setBackgroundResource(R.drawable.border_set1);
                simpleList.setVisibility(View.VISIBLE);
                listViewfun.setVisibility(View.GONE);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.activity_listview, R.id.textView, NewsList);
                simpleList.setAdapter(arrayAdapter);

                simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String text = NewsList[position];
                        Intent intent = new Intent(getBaseContext(), Webpage.class);
                        intent.putExtra("news", text);
                        intent.putExtra("symbol", Stock_N);
                        startActivity(intent);
                    }
                });
            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference();

        llt_markerview = (LinearLayout) findViewById(R.id.llt_markerview);
        tv_high = (TextView) findViewById(R.id.tv_high);
        x_time = (TextView)findViewById(R.id.x_time);
        tv_low = (TextView) findViewById(R.id.tv_low);
        tv_open = (TextView) findViewById(R.id.tv_open);
        tv_close = (TextView) findViewById(R.id.tv_close);
        tv_volume = (TextView) findViewById(R.id.tv_volume);
        percen_Symbol = (TextView)findViewById(R.id.percentage_symbol);


        S_C_T ='I';

        signal_api_call='M';


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        start_D = (TextView)findViewById(R.id.start);

        d1= (TextView)findViewById(R.id.d1);
        w1= (TextView)findViewById(R.id.w1);
        m3= (TextView)findViewById(R.id.m1);
        y1= (TextView)findViewById(R.id.y1);
        y5= (TextView)findViewById(R.id.y5);
        all= (TextView)findViewById(R.id.all);

        m3.setBackgroundResource(R.drawable.border_set1);



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
        watch_L = (Button)findViewById(R.id.watch_list);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);




        candleStickChart = (CandleStickChart) findViewById(R.id.chart);
        candleStickChart.setNoDataText("loading...");

        candleStickChart.setOnChartValueSelectedListener(this);


        mChart = (LineChart) findViewById(R.id.asdlinechart);




        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String chartstatus = preferences.getString("chartstatus", "");

        if (chartstatus.equals("c")) {

            candleStickChart.setVisibility(View.VISIBLE);
            mChart.setVisibility(View.INVISIBLE);
        }
        else{

            mChart.setVisibility(View.VISIBLE);
            candleStickChart.setVisibility(View.INVISIBLE);
        }



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
                            Foundamentals();

                        }
                        else{
                            stocklist.clear();
                            loadstockdialog("", false);
                            Foundamentals();
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


                data_symbolwatchlist();
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
            loadchartdata("A");
            GetPercentage("A");
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
        chart.setNoDataText("loading...");


        setcompanydetails();

        Foundamentals();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString(KEY_FIRSTNAME, tv_stock_name.getText().toString());
        savedInstanceState.putString(KEY_LASTNAME, last.getText().toString());
        savedInstanceState.putString(KEY_TITLE, mTit.getText().toString());


        super.onSaveInstanceState(savedInstanceState);
    }

    public void saveView(View view){
        first.setText(firstName.getText().toString().trim());
        last.setText(lastName.getText().toString().trim());
        mTit.setText(title.getText().toString().trim());
    }


    public void setcompanydetails ()
    {
        TextView symbnam = (TextView)findViewById(R.id.symbolname);
        TextView exname = (TextView)findViewById(R.id.exchangenam);
        final TextView comnam = (TextView)findViewById(R.id.compnam);
        TextView sec = (TextView)findViewById(R.id.sector);
        TextView indus = (TextView)findViewById(R.id.industry);
        TextView country = (TextView)findViewById(R.id.country);

        InputStreamReader is = null;
        try {
            is = new InputStreamReader(getAssets()
                    .open("companylist.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(is);
        try {
            reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line;
        String [] Data = new String[0];


        try {
            while ((line = reader.readLine()) != null) {


                Data = line.split(",");
                if (Stock_N.equals(Data[0]))
                {
                    break;
                }

            }

            symbnam.setText(Data[0]);
            exname.setText("["+Data[7]+"]");

            String compnam= Data[1];

            compnam =compnam.substring(1);
            country.setText(Data[4]);
            sec.setText(Data[5]);
            indus.setText(Data[6]);



        } catch (IOException e) {
            e.printStackTrace();
        }



        String url = "https://api.intrinio.com/companies?identifier="+Stock_N;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {




                        try {
                            String contacts = null;
                            JSONObject object = null;
                            object = new JSONObject(response);
                            contacts = object.getString("name");
                            comnam.setText(contacts);
//                            Toast.makeText(getApplicationContext(), contacts, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR","error => "+error.toString());
//                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

                        comnam.setText("N/A");

                        String aasd= error.toString();
                    }
                }
        ) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s","788e3c656c0e4e0b579cad93b9efd853","c1c2a2c03556ee69689e4ac572892d53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
                params.put("Authorization", auth);
                return params;
            }

        };

        postRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        queue.add(postRequest);
    }


    public void data_symbolwatchlist()
    {


        AddData obj = new AddData(getApplicationContext());
        obj.addartist (String.valueOf(Stock_N), percentageSymbol);
        String asd = "";
        watch_L.setEnabled(false);
        watch_L.setTextColor(Color.parseColor("#000000"));
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




    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1].substring(0,3) ;
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

    private void loadchartdata(final String mystock) {



        tv_stock_name.setText(mystock);
        View_User_Logs(mystock);
        Stock_N = mystock;




    }
    public void View_User_Logs(String stockname) {


        candleStickChart.setNoDataText("loading");

        String url = null;

        if (signal_api_call=='I')
        {
            url = "https://stockapps.herokuapp.com/API/get/1min/"+stockname+"/?format=json";
            WeeklyYearlyAPi(url);
        }
        else if (signal_api_call=='W')
        {
            url = "https://stockapps.herokuapp.com/API/get/intra/"+stockname+"/?format=json";
            WeeklyYearlyAPi(url);
        }

        if (signal_api_call=='M')
        {
            url = "https://stockapps.herokuapp.com/API/result/monthly/"+stockname+"/?format=json";
            WeeklyYearlyAPi(url);

        }
        else if (signal_api_call=='Y')
        {
            url = "https://stockapps.herokuapp.com/API/result/1year/"+stockname+"/?format=json";
            WeeklyYearlyAPi(url);
        }

        else if (signal_api_call=='Z')
        {

            url = "https://stockapps.herokuapp.com/API/result/5year/"+stockname+"/?format=json";
            WeeklyYearlyAPi(url);

        }

        else if (signal_api_call=='A')
        {
            url = "https://stockapps.herokuapp.com/API/result/all/"+stockname+"/?format=json";
            WeeklyYearlyAPi(url);
        }


    }


    public void volleycallmethodapi (String url)
    {


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

                        if (signal_api_call=='M' || signal_api_call=='Y')
                        {
                            parseJsonData(response);
                        }
                        else if (signal_api_call=='I' || signal_api_call=='W')
                        {
                            mindatastocksymbol(response);
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        labels.clear();
                        Xaxis_value.clear();
                        entries.clear();
                        entries1.clear();
                        labelsforvolume.clear();
                    }


                }
        ) {

        };

        postRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        queue.add(postRequest);
    }


    public void WeeklyYearlyAPi( String url) {




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

                        String datawee = null;
                        try {
                            JSONArray contacts = null;
                            JSONObject object = null;
                            object = new JSONObject(response);
                            contacts = object.getJSONArray("Result");
                            int s = contacts.length();
                            datawee = String.valueOf(contacts);
                            Weekly_yearly_parseJsonData(datawee);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pagenumber++;




                        // response
                        Log.d("Response", response);



                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR","error => "+error.toString());
                        labels.clear();
                        Xaxis_value.clear();
                        entries.clear();
                        entries1.clear();
                        labelsforvolume.clear();

                        String aasd= error.toString();
                    }
                }
        ) {



        };

        postRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        queue.add(postRequest);

    }





    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }



    public void GetPercentage(String stockname) {


        String url = "https://api.intrinio.com/prices?identifier="+stockname+"&frequency=daily";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {




                        try {
                            JSONArray contacts = null;
                            JSONObject object = null;
                            object = new JSONObject(response);
                            contacts = object.getJSONArray("data");
                            int s = contacts.length();
                            String asd = String.valueOf(contacts);
                            CalculatePercentage(asd);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }


                }

        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s","788e3c656c0e4e0b579cad93b9efd853","c1c2a2c03556ee69689e4ac572892d53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
                params.put("Authorization", auth);
                return params;
            }

        };

        postRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        queue.add(postRequest);

    }



    void CalculatePercentage(String jsonString) {


        String current_close = null;
        String yesterday_close = null;

        float tot_difference;
        float tot_percent;


        JSONArray json = null;


        try {
            int i = 0;
            json = new JSONArray(jsonString);

            int length = json.length();
            for (int j = 0; j <3 ; j++) {
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject e = json.getJSONObject(j);

                String clos = e.getString("close");

                if (j ==0 )
                {
                    current_close = clos;
                }
                if (j ==1 )
                {
                    yesterday_close = clos;
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


    public void Weekly_yearly_parseJsonData( String jsonString)
    {
        JSONArray json = null;
        ArrayList<CandleEntry> entries = new ArrayList<>();

        final ArrayList<String> xVals = new ArrayList<String>();
        final ArrayList<Entry> yVals = new ArrayList<Entry>();

        try {
            int i =0;
            json = new JSONArray(jsonString);

            int length = json.length();
            colors = null;
            colors = new int[length];
            Date d1;
            Date d2;
            xVals.clear();
            yVals.clear();
            for(int j=json.length()-1;j>=0;j--) {
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject e = json.getJSONObject(j);

                String open = e.getString("open");
                String high = e.getString("high");
                String low = e.getString("low");
                String close = e.getString("close");
                String time = e.getString("date");

                String vol = e.getString("volume");
                String [] split = time.split("-");


                    xVals.add(split[1]);
                    yVals.add(new Entry(i, Float.valueOf(close)));
                    labels.add("abc");
                    entries1.add(new BarEntry(i, Float.valueOf(vol)));
                    labelsforvolume.add(Float.valueOf(vol));
                    Xaxis_value.add(time);

                    if (Float.valueOf(close) >= Float.valueOf(open)) {
                        colors[i] = Color.parseColor("#1D8348");
                    } else {
                        colors[i] = Color.parseColor("#E74C3C");
                    }

                    entries.add(new CandleEntry(i, Float.valueOf(high), Float.valueOf(low), Float.valueOf(open), Float.valueOf(close)));
                    i++;


            }




            LineDataSet set1;
            set1 = new LineDataSet(yVals, "DataSet 1");
            set1.setFillAlpha(110);
            set1.setFillColor(Color.RED);
            set1.setDrawValues(false);
            set1.setDrawCircles(false);
            set1.setColors(colors);
            set1.setAxisDependency(YAxis.AxisDependency.RIGHT);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets
            LineData lidata = new LineData();
            lidata.addDataSet(set1);

            mChart.invalidate();

            mChart.setDescription(null);
            mChart.setNoDataText("You need to provide data for the chart.");

            // enable touch gestures
            mChart.setTouchEnabled(true);
            mChart.setDrawGridBackground(false);
            mChart.getLegend().setEnabled(false);

            mChart.getAxisRight().setEnabled(true);
            mChart.getAxisLeft().setEnabled(false);
            mChart.invalidate();
            mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            mChart.setData(lidata);
            mChart.invalidate();

            XAxis xAxis = mChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));


            int si = Xaxis_value.size();
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
            candleStickChart.invalidate();

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


            XAxis xAxis1 = candleStickChart.getXAxis();
            xAxis1.setValueFormatter(new IndexAxisValueFormatter(Xaxis_value));
            xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis1.setTextSize(5);


            YAxis leftYAxis = candleStickChart.getAxisLeft();
            leftYAxis.setEnabled(false);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        BarDataSet set = new BarDataSet(entries1, "");
        set.setDrawValues(false);
        set.setColors(colors);
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
        rightYAxis.setEnabled(false);
//
    }



    private static int differenceInMonths(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        int diff = 0;
        if (c2.after(c1)) {
            while (c2.after(c1)) {
                c1.add(Calendar.MONTH, 1);
                if (c2.after(c1)) {
                    diff++;
                }
            }
        } else if (c2.before(c1)) {
            while (c2.before(c1)) {
                c1.add(Calendar.MONTH, -1);
                if (c1.before(c2)) {
                    diff--;
                }
            }
        }
        return diff;
    }


    void parseJsonData(String jsonString) {


        JSONArray json = null;
        ArrayList<CandleEntry> entries = new ArrayList<>();

        final ArrayList<String> xVals = new ArrayList<String>();
        final ArrayList<Entry> yVals = new ArrayList<Entry>();


        try {
            int i = 0;
            json = new JSONArray(jsonString);

            int length = json.length();
            colors = null;
            colors = new int[length];

            xVals.clear();
            yVals.clear();
            for (int j = 0; j < json.length(); j++) {
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject e = json.getJSONObject(j);


                String open = e.getString("open");
                String high = e.getString("high");
                String low = e.getString("low");
                String close = e.getString("close");
                String time = e.getString("date");

                String [] split = time.split("-");


                xVals.add(split[2]);

                yVals.add(new Entry(i, Float.valueOf(close)));

                String vol = e.getString("volume");

                labels.add("abc");
                entries1.add(new BarEntry(i, Float.valueOf(vol)));
                labelsforvolume.add(Float.valueOf(vol));
                Xaxis_value.add(time);

                if (Float.valueOf(close) >= Float.valueOf(open)) {
                    colors[i] = Color.parseColor("#1D8348");
                } else {
                    colors[i] = Color.parseColor("#E74C3C");
                }

                entries.add(new CandleEntry(i, Float.valueOf(high), Float.valueOf(low), Float.valueOf(open), Float.valueOf(close)));
                i++;
            }

            LineDataSet set1;
            set1 = new LineDataSet(yVals, "DataSet 1");
//            set1.setFillAlpha(110);
            set1.setFillColor(Color.RED);
            set1.setDrawValues(false);
            set1.setDrawCircles(false);
            set1.setColors(colors);
            set1.setAxisDependency(YAxis.AxisDependency.RIGHT);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets
            LineData lidata = new LineData();
            lidata.addDataSet(set1);

            mChart.invalidate();

            mChart.setDescription(null);
            mChart.setNoDataText("You need to provide data for the chart.");

            // enable touch gestures
            mChart.setTouchEnabled(true);
            mChart.setDrawGridBackground(false);
            mChart.getLegend().setEnabled(false);

            mChart.getAxisRight().setEnabled(true);
            mChart.getAxisLeft().setEnabled(false);
            mChart.invalidate();
            mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            mChart.setData(lidata);
            mChart.invalidate();

            XAxis xAxis = mChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));



            int si = Xaxis_value.size();
            dataset = new CandleDataSet(entries, "Entries");
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
            candleStickChart.invalidate();

            candleStickChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    if (e instanceof CandleEntry) {
                        CandleEntry ce = (CandleEntry) e;


                        String asdf = String.valueOf(ce.getX());
                        tv_high.setText("High: " + ce.getHigh());
                        tv_low.setText("Low: " + ce.getLow());
                        tv_open.setText("Open: " + ce.getOpen());
                        tv_close.setText("Close: " + ce.getClose());
                        tv_volume.setText("Volume: " + getStringofvolume(labelsforvolume.get(candleStickChart.getCandleData().getDataSetForEntry(e).getEntryIndex(ce))));
                        x_time.setText("Time: " + Xaxis_value.get(candleStickChart.getCandleData().getDataSetForEntry(e).getEntryIndex(ce)));

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

            XAxis xAxis1 = candleStickChart.getXAxis();
            xAxis1.setValueFormatter(new IndexAxisValueFormatter(Xaxis_value));
            xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis1.setTextSize(5);


            YAxis leftYAxis = candleStickChart.getAxisLeft();
            leftYAxis.setEnabled(false);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        BarDataSet set = new BarDataSet(entries1, "");
        set.setDrawValues(false);
        set.setColors(colors);
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
        rightYAxis.setEnabled(false);


    }

    public void mindatastocksymbol (String jsonString)
    {

        final ArrayList<String> xVals = new ArrayList<String>();
        final ArrayList<Entry> yVals = new ArrayList<Entry>();

        String open = "";
        String high = "";
        String low = "";
        String close = "";
        String volume = "";


        try {
            int i = 0;
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject jsonChildObject = null;
            if (signal_api_call=='I')
            {
                jsonChildObject = (JSONObject)jsonObject.get("Time Series (1min)");
            }

            if (signal_api_call=='W')
            {
                 jsonChildObject = (JSONObject)jsonObject.get("Time Series (15min)");

            }
            int length = jsonChildObject.length();

            colors = null;
            colors = new int[length];
            String key = null;

            JSONArray objnames = jsonChildObject.names();
            for(int j = objnames.length()-1; j > 0; j--) {
                key = objnames.getString(j);
                Xaxis_value.add(key);

                high = String.valueOf(((JSONObject) jsonChildObject.get(key)).get("2. high"));
                low = String.valueOf(((JSONObject) jsonChildObject.get(key)).get("3. low"));
                open = String.valueOf(((JSONObject) jsonChildObject.get(key)).get("1. open"));
                close = String.valueOf(((JSONObject) jsonChildObject.get(key)).get("4. close"));
                volume = String.valueOf(((JSONObject) jsonChildObject.get(key)).get("5. volume"));


                String [] split = key.split(" ");


                xVals.add(split[1]);

                yVals.add(new Entry(i, Float.valueOf(close)));
                entries.add(new CandleEntry(i, Float.valueOf(high), Float.valueOf(low), Float.valueOf(open), Float.valueOf(close)));
                labels.add(key);
                entries1.add(new BarEntry(i, Float.valueOf(volume)));
                labelsforvolume.add(Float.valueOf(volume));

                if (Float.valueOf(close) >= Float.valueOf(open)) {
                    colors[i] = Color.parseColor("#1D8348");
                }
                else {
                    colors[i] = Color.parseColor("#E74C3C");
                }

                i++;
            }



            LineDataSet set1;
            set1 = new LineDataSet(yVals, "DataSet 1");
//            set1.setFillAlpha(110);
            set1.setFillColor(Color.RED);
            set1.setDrawValues(false);
            set1.setDrawCircles(false);
            set1.setColors(colors);
            set1.setAxisDependency(YAxis.AxisDependency.RIGHT);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets
            LineData lidata = new LineData();
            lidata.addDataSet(set1);

            mChart.invalidate();

            mChart.setDescription(null);
            mChart.setNoDataText("You need to provide data for the chart.");

            // enable touch gestures
            mChart.setTouchEnabled(true);
            mChart.setDrawGridBackground(false);
            mChart.getLegend().setEnabled(false);
            mChart.getAxisRight().setEnabled(true);
            mChart.getAxisLeft().setEnabled(false);
            mChart.invalidate();
            mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            mChart.setData(lidata);
            mChart.invalidate();

            XAxis xAxis = mChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));

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
            candleStickChart.invalidate();

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


            XAxis xAxis1 = candleStickChart.getXAxis();
            xAxis1.setValueFormatter(new IndexAxisValueFormatter(Xaxis_value));
            xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis1.setTextSize(5);


            YAxis leftYAxis = candleStickChart.getAxisLeft();
            leftYAxis.setEnabled(false);




        } catch (Exception e1) {

            mChart.setNoDataText("No data Available");
            candleStickChart.setNoDataText("No data Available");
            chart.setNoDataText("No data Available");

            labels.clear();
            Xaxis_value.clear();
            entries.clear();
            entries1.clear();
            labelsforvolume.clear();

            dataset.setDrawValues(false);

            Legend leg = candleStickChart.getLegend();
            leg.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Please check your internet connection and try again", Toast.LENGTH_LONG).show();
        }


        BarDataSet set = new BarDataSet(entries1, "");
        set.setDrawValues(false);
        set.setColors(colors);
        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refre
        chart.setDescription(null);// Hide the description
        chart.getLegend().setEnabled(false);


        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(false);
        xAxis.setDrawAxisLine(false);

        YAxis leftYAxis = chart.getAxisLeft();
        leftYAxis.setEnabled(false);

        YAxis rightYAxis = chart.getAxisRight();
        rightYAxis.setEnabled(false);
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
                    setcompanydetails();
                    Foundamentals();

                }
            });
        }

        @Override
        public int getItemCount() {
            return trend_list.size();
        }

    }





    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }


    public void Foundamentals() {

        String url = "https://api.intrinio.com/data_point?identifier="+Stock_N+"&item=marketcap,totalrevenue,profitmargin,pricetoearnings,basiceps,cashdividendspershare,short_interest,weightedavebasicsharesos";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {


                        String asd1= response;
                        Log.d("Response", response);

                        try {
                            JSONArray contacts = null;
                            JSONObject object = null;
                            object = new JSONObject(response);
                            contacts = object.getJSONArray("data");


                            ArrayList<DataModelFund> dataModels;
                            CustomAdaptorFund adapter;


                            dataModels= new ArrayList<>();

                            for(int i=0; i<contacts.length(); i++){
                                JSONObject json_data = contacts.getJSONObject(i);

                                String Item = json_data.getString("item");
                                String value = json_data.getString("value");

                                dataModels.add(new DataModelFund(Fundamentname[i], "asd", value,"September"));
                            }

                            adapter= new CustomAdaptorFund(dataModels,getApplicationContext());

                            listViewfun.setAdapter(adapter);
                            listViewfun.setOnTouchListener(new ListView.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    int action = event.getAction();
                                    switch (action) {
                                        case MotionEvent.ACTION_DOWN:
                                            // Disallow ScrollView to intercept touch events.
                                            v.getParent().requestDisallowInterceptTouchEvent(true);
                                            break;

                                        case MotionEvent.ACTION_UP:
                                            // Allow ScrollView to intercept touch events.
                                            v.getParent().requestDisallowInterceptTouchEvent(false);
                                            break;
                                    }

                                    // Handle ListView touch events.
                                    v.onTouchEvent(event);
                                    return true;
                                }
                            });
//                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                                    DataModelFund dataModel= dataModels.get(position);
//
//                                    Snackbar.make(view, dataModel.getName()+"\n"+dataModel.getType()+" API: "+dataModel.getVersion_number(), Snackbar.LENGTH_LONG)
//                                            .setAction("No action", null).show();
//                                }
//                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR","error => "+error.toString());
                    }
                }
        ) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s","788e3c656c0e4e0b579cad93b9efd853","c1c2a2c03556ee69689e4ac572892d53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
                params.put("Authorization", auth);
                return params;
            }

        };

        postRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        queue.add(postRequest);

    }

    public void Fundadetails( String jsonString) {

        jsonString = jsonString.substring(1, jsonString.length()-1);
        JSONArray json = null;

        try {
            json = new JSONArray(jsonString);

            for (int j =  1; j > json.length()-1; j++) {
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject e = json.getJSONObject(j);

                String asdmark = e.getString("item");
                asdmark = "asdf";


            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
