package com.example.brainplow.stocks;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> labels = new ArrayList<String>();
    ArrayList<CandleEntry> entries = new ArrayList<>();
    int[] colors;
    private Spinner spinner1;
    CandleStickChart candleStickChart;
    RecyclerView rc_stock_list;
    List<String> stocklist = new ArrayList<>();
    HomeAdapter rc_adapter;
    Dialog d;
    TextView tv_stock_name;
    CandleDataSet dataset;


    public String URLTime;
    public String TimeSeries;
    public String func;

    SpotsDialog dialog;

    TextView start_D, end_D;
    TextView daily, weekly, monthly;

    LinearLayout intra_d;
    TextView intra_T;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);


        start_D = (TextView)findViewById(R.id.start);


        daily = (TextView)findViewById(R.id.daily);
        weekly = (TextView)findViewById(R.id.weekly);
        monthly = (TextView)findViewById(R.id.monthly);
        intra_d = (LinearLayout)findViewById(R.id.Intradaytime);

        intra_T = (TextView)findViewById(R.id.intra_Text);


        intra_T.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                daily.setTextSize(14);
                weekly.setTextSize(14);
                monthly.setTextSize(14);
                intra_T.setTextSize(18);
                intra_T.setTextColor(Color.parseColor("#78201e"));
                daily.setTextColor(Color.BLACK);
                weekly.setTextColor(Color.BLACK);
                monthly.setTextColor(Color.BLACK);
                intra_d.setVisibility(View.VISIBLE);
            }
        });

        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daily.setTextColor(Color.parseColor("#78201e"));
                daily.setTextSize(18);
                weekly.setTextSize(14);
                monthly.setTextSize(14);
                intra_T.setTextSize(14);
                intra_T.setTextColor(Color.BLACK);
                weekly.setTextColor(Color.BLACK);
                monthly.setTextColor(Color.BLACK);
                intra_d.setVisibility(View.GONE);
            }
        });

        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intra_T.setTextColor(Color.BLACK);
                weekly.setTextColor(Color.parseColor("#78201e"));
                weekly.setTextSize(18);
                daily.setTextSize(14);
                monthly.setTextSize(14);
                intra_T.setTextSize(14);
                daily.setTextColor(Color.BLACK);
                monthly.setTextColor(Color.BLACK);
                intra_d.setVisibility(View.GONE);
            }
        });

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthly.setTextColor(Color.parseColor("#78201e"));
                monthly.setTextSize(18);
                intra_T.setTextColor(Color.BLACK);
                daily.setTextSize(14);
                weekly.setTextSize(14);
                intra_T.setTextSize(14);
                weekly.setTextColor(Color.BLACK);
                daily.setTextColor(Color.BLACK);
                intra_d.setVisibility(View.GONE);
            }
        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        dialog = new SpotsDialog(this);
        dialog.show();
        candleStickChart = (CandleStickChart) findViewById(R.id.chart);
        tv_stock_name = (TextView)findViewById(R.id.tv_stock_name);

        EditText rlt_stock = (EditText) findViewById(R.id.rlt_stock);
        rlt_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d = new Dialog(MainActivity.this);
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

        spinner1 = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this,
                R.array.country_arrays, R.layout.spinner_item);

        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner1.setAdapter(adapter1);


        final int[] iCurrentSelection = {spinner1.getSelectedItemPosition()};

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (iCurrentSelection[0] != i){
                    // Your code here
                }
                iCurrentSelection[0] = i;

                if (i==0)
                {
                    URLTime ="1min";
                    func ="TIME_SERIES_INTRADAY";
                    TimeSeries = "Time Series (1min)";
                }
                else if (i==1)
                {
                    URLTime ="5min";
                    func ="TIME_SERIES_INTRADAY";
                    TimeSeries = "Time Series (5min)";
                }
                else if (i==2)
                {
                    URLTime ="15min";
                    func ="TIME_SERIES_INTRADAY";
                    TimeSeries = "Time Series (15min)";
                }

                else if (i==3)
                {
                    URLTime ="30min";
                    func ="TIME_SERIES_INTRADAY";
                    TimeSeries = "Time Series (30min)";
                }
                else if (i==4)
                {
                    URLTime ="60min";
                    func ="TIME_SERIES_INTRADAY";
                    TimeSeries = "Time Series (60min)";
                }

                else if (i==5)
                {
                    URLTime ="60min";
                    func ="TIME_SERIES_INTRADAY";
                    TimeSeries = "Time Series (60min)";
                }



            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        URLTime="1min";
        func ="TIME_SERIES_INTRADAY";
        TimeSeries = "Time Series (1min)";
        loadchartdata("AAON");


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
        dataset.setDecreasingColor(Color.parseColor("#a0db8e"));
        dataset.setDecreasingPaintStyle(Paint.Style.FILL_AND_STROKE);
        dataset.setIncreasingColor(Color.parseColor("#E74C3C"));
        dataset.setIncreasingPaintStyle(Paint.Style.FILL_AND_STROKE);
        dataset.setDrawValues(false);
        dataset.setNeutralColor(Color.parseColor("#a0db8e"));
        dataset.setShadowColorSameAsCandle(true);

        CandleData data = new CandleData(dataset);
        dataset.setColors(colors);
        candleStickChart.setData(data);
        candleStickChart.setDescription(null);
        candleStickChart.getLegend().setEnabled(false);
        dataset.notifyDataSetChanged();
        candleStickChart.notifyDataSetChanged();
        candleStickChart.invalidate();

        dialog.dismiss();
    }
    private void loadchartdata(String mystock) {


        dialog.show();
        View_User_Logs(mystock);
        tv_stock_name.setText(mystock);

    }
    public void View_User_Logs(String stockname) {




        RequestQueue queue = Volley.newRequestQueue(this);


        String url = "https://www.alphavantage.co/query?function="+func+"&symbol="+stockname+"&interval="+URLTime+"&outputsize=full&apikey=WKMM7FGAW7V19SLN";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
//                        Log.d("Response", response);
                        labels.clear();
                        entries.clear();
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

    void parseJsonData(String jsonString) {
        //                System.out.println("value "+ i + " "+key );
//                System.out.println("open value: "+((JSONObject)jsonChildObject.get(key)).get("1. open"));
//                System.out.println("high value: "+((JSONObject)jsonChildObject.get(key)).get("2. high"));
//                System.out.println("low value: "+((JSONObject)jsonChildObject.get(key)).get("3. low"));
//                System.out.println("close value: "+((JSONObject)jsonChildObject.get(key)).get("4. close"));

        String open = "";
        String high="";
        String low="";
        String close="";

        String key;
        Iterator iterator;

        try {
            int i =0;

            JSONObject jsonObject =new JSONObject(jsonString);
            JSONObject jsonChildObject = (JSONObject)jsonObject.get(TimeSeries);
            int length = jsonChildObject.length();

            colors = null;
            colors = new int[length];
            iterator = jsonChildObject.keys();

            key = null;
            while(iterator.hasNext()){
                key = (String)iterator.next();

                if (i==0)
                {
                    start_D.setText(key);
                }
                high = String.valueOf(((JSONObject)jsonChildObject.get(key)).get("2. high"));
                low = String.valueOf(((JSONObject)jsonChildObject.get(key)).get("3. low"));
                open = String.valueOf(((JSONObject)jsonChildObject.get(key)).get("1. open"));
                close = String.valueOf(((JSONObject)jsonChildObject.get(key)).get("4. close"));

                if(Float.valueOf(close) >= Float.valueOf(open)) {
                    colors[i] = Color.parseColor("#1D8348");
                }
                else {
                    colors[i] = Color.parseColor("#E74C3C");
                }

                entries.add(new CandleEntry(i,Float.valueOf(high),Float.valueOf(low),Float.valueOf(open),Float.valueOf(close)));

                i++;
            }
            setupchart();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                    loadchartdata(holder.stockname.getText().toString());


                }
            });
        }

        @Override
        public int getItemCount() {
            return trend_list.size();
        }

    }
}
