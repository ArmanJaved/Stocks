package com.stocksymbolapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by BrainPlow on 8/25/2017.
 */

public class Indices extends ArrayAdapter<String> {

    private Activity context;
    String [] stockdatasplit;
    int i;
    DataPoint[] dataPoints;
    int[] colors;
    CandleDataSet dataset;


    private String[] resaurantsList;
    public static final String MINI = "MINI";

    public Indices(Activity context, String[] resaurantsList)
    {
        super(context, R.layout.listview_rest, resaurantsList);

        this.context = context;
        this.resaurantsList = resaurantsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View listviewitem = inflater.inflate(R.layout.listview_rest, null, true);

        TextView name = (TextView)listviewitem.findViewById(R.id.Textartistname);
        TextView date = (TextView)listviewitem.findViewById(R.id.Textartistname1);


        name.setText(resaurantsList[position]);

        String url = null;

        if (name.getText().toString().contains("SPY"))
        {
             url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%5Egspc&apikey=BZ7U23MEIMK3CFAW";
        }
        else if (name.getText().toString().contains("DOW"))
        {
             url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=^dji&apikey=BZ7U23MEIMK3CFAW";
        }
        else if (name.getText().toString().contains("NASD"))
        {
             url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=^IXIC&apikey=BZ7U23MEIMK3CFAW";
        }


        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {


                        final ArrayList<Entry> yVals = new ArrayList<Entry>();
                        String open = "";
                        String close = "";


                        try {
                            int i = 0;
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonChildObject = null;

                            jsonChildObject = (JSONObject)jsonObject.get("Time Series (Daily)");


                            int length = jsonChildObject.length();

                            colors = null;
                            colors = new int[length];
                            String key = null;

                            JSONArray objnames = jsonChildObject.names();
                            for(int j = objnames.length()-1; j > 0; j--) {
                                key = objnames.getString(j);

                                close = String.valueOf(((JSONObject) jsonChildObject.get(key)).get("4. close"));

                                yVals.add(new Entry(i, Float.valueOf(close)));


                                i++;
                            }


                            LineDataSet set1;

                            // create a dataset and give it a type
                            set1 = new LineDataSet(yVals, "DataSet 1");
                            set1.setDrawCircles(false);
                            set1.setDrawValues(false);

                            set1.setAxisDependency(YAxis.AxisDependency.RIGHT);

                            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                            dataSets.add(set1); // add the datasets



                            LineData data = new LineData();
                            data.addDataSet(set1);

                            LineChart mChart = (LineChart) listviewitem.findViewById(R.id.asdlinechart);

                            mChart.invalidate();

                            mChart.setDescription(null);

                            // enable touch gestures
                            mChart.setTouchEnabled(false);
                            mChart.setDrawGridBackground(false);
                            mChart.getLegend().setEnabled(false);

                            mChart.getAxisRight().setEnabled(false);
                            mChart.getAxisLeft().setEnabled(false);
                            mChart.invalidate();
                            mChart.getXAxis().setEnabled(false);
                            mChart.setData(data);
                            mChart.invalidate();




                        } catch (Exception e1) {

                            e1.printStackTrace();
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

        return listviewitem;

    }





}

