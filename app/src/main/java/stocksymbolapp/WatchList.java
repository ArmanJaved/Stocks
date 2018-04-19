package stocksymbolapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
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
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.stocksymbolapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static stocksymbolapp.Setting.MINI;

/**
 * Created by BrainPlow on 8/25/2017.
 */

public class WatchList extends ArrayAdapter<Watch> {

    private Activity context;
    String [] stockdatasplit;
    int i;
    DataPoint[] dataPoints;
    int[] colors;
    CandleDataSet dataset;


    private List<Watch> resaurantsList;
    public static final String MINI = "MINI";

    public WatchList(Activity context, List<Watch> resaurantsList)
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

        final LineChart mChart = (LineChart) listviewitem.findViewById(R.id.asdlinechart);

        mChart.setNoDataText("");

        final Watch resaurants = resaurantsList.get(position);
        final float check  = Float.parseFloat(resaurants.getRestamount().trim());
        if (check > 0)
        {
            date.setTextColor(Color.parseColor("#63bd55"));
            date.setText("+"+resaurants.getRestamount()+"%");

        }
        else {

            date.setTextColor(Color.parseColor("#E74C3C"));
            date.setText(resaurants.getRestamount()+"%");
        }

        name.setText(resaurants.getRestname());


        String symbol = resaurants.getRestname();


        SharedPreferences prefs = context.getSharedPreferences(MINI, MODE_PRIVATE);
        String mini_variable = prefs.getString("ministatus", "");

        if (mini_variable.equals("on"))
        {

            mChart.setNoDataText("loading");


            String url = "https://api.iextrading.com/1.0/stock/"+symbol+"/chart/3m";
            final ArrayList<String> xVals = new ArrayList<String>();
            final ArrayList<Entry> yVals = new ArrayList<Entry>();

            RequestQueue queue = Volley.newRequestQueue(context);

            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {

                            JSONArray json = null;

                            try {
                                int i = 0;
                                json = new JSONArray(response);
                                for (int j = 0; j < json.length(); j++) {
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    JSONObject e = json.getJSONObject(j);


                                    String close = e.getString("close");
                                    String time = e.getString("date");

                                    String [] split = time.split("-");




                                    xVals.add(split[2]);
                                    yVals.add(new Entry(i, Float.valueOf(close)));



                                    i++;
                                }


                                mChart.setVisibility(View.VISIBLE);

                                LineDataSet set1;

                                // create a dataset and give it a type
                                set1 = new LineDataSet(yVals, "DataSet 1");
                                set1.setDrawCircles(false);
                                set1.setDrawValues(false);

                                if (check> 0 ) {
                                    set1.setColor(Color.parseColor("#63bd55"));
                                }
                                else {
                                    set1.setColor(Color.parseColor("#E74C3C"));
                                }
                                set1.setAxisDependency(YAxis.AxisDependency.RIGHT);

                                ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                                dataSets.add(set1); // add the datasets



                                LineData data = new LineData();
                                data.addDataSet(set1);

                                mChart.invalidate();

                                mChart.setDescription(null);
                                mChart.setNoDataText("loading");

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

                            }catch (Exception e)
                            {}
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

        }
        else
        {
//            graph.setVisibility(View.INVISIBLE);
//            date.setVisibility(View.INVISIBLE);
        }

        return listviewitem;

    }

    private void setupchart(){


    }





}

