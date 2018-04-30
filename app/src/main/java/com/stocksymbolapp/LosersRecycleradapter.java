package com.stocksymbolapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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


public class LosersRecycleradapter extends RecyclerView.Adapter<LosersRecycleradapter.MyViewHolder> {

    private List<String> moviesList;

    int i;
    int[] colors;
    CandleDataSet dataset;


    private Activity context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView symnam, compnam, persymb;
        public CandleStickChart candleStickChart ;
        private RequestQueue requestQueue;
        public MyViewHolder(View view) {
            super(view);

            candleStickChart = (CandleStickChart) view.findViewById(R.id.chart);
            symnam = (TextView) view.findViewById(R.id.symbname);
            compnam = (TextView) view.findViewById(R.id.companyname);
            persymb = (TextView) view.findViewById(R.id.percentage_symbol);
        }
    }

    public LosersRecycleradapter(Activity applicationContext, List<String> moviesList) {
        this.moviesList = moviesList;
        this.context = applicationContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gainernloseradaptor, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        handleSSLHandshake();
        String data = moviesList.get(position);
        Log.d("dat", data);

//        holder.genre.setText(movie.getGenre());
//        holder.year.setText(movie.getYear());


        String[] Split = data.split(",");
        final String namesymb = Split[0];
        holder.symnam.setText(namesymb);


        String url1 = "https://api.intrinio.com/companies?identifier="+namesymb;
        if (holder.requestQueue == null) {
            holder.requestQueue = Volley.newRequestQueue(context);
        }

        StringRequest postRequest = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {




                        try {
                            String contacts = null;
                            JSONObject object = null;
                            object = new JSONObject(response);
                            contacts = object.getString("name");
                            holder.compnam.setText(contacts);
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

                        holder.compnam.setText("N/A");

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
        holder.requestQueue.add(postRequest);


        ////////////////////////CHART DISPLAY
        final ArrayList<String> Xaxis_value = new ArrayList<String>();

        String url = "https://api.intrinio.com/prices?identifier="+namesymb+"&frequency=daily";



        if (holder.requestQueue == null) {
            holder.requestQueue = Volley.newRequestQueue(context);
        }
        postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {


                        holder.candleStickChart.clear();
                        Xaxis_value.clear();

                        try {
                            JSONArray contacts = null;
                            JSONObject object = null;
                            object = new JSONObject(response);
                            contacts = object.getJSONArray("data");
                            int s = contacts.length();
                            String asd = String.valueOf(contacts);


                            JSONArray json = null;
                            ArrayList<CandleEntry> entries = new ArrayList<>();

                            String current_close = null;
                            String yesterday_close = null;
                            float tot_difference;
                            float tot_percent;

                            try {
                                int i =0;
                                json = new JSONArray(asd);

                                int length = json.length();
                                Date c = Calendar.getInstance().getTime();
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                String currentdate = df.format(c);
                                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                                colors = null;
                                colors = new int[length];
                                Date d1;
                                Date d2;
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


                                    if (j ==0 )
                                    {
                                        current_close = close;
                                    }
                                    if (j ==1)
                                    {
                                        yesterday_close = close;
                                    }


                                    d1 = f.parse(time);
                                    d2 = f.parse(currentdate);
                                    int n = differenceInMonths(d1, d2);
                                    if (n<3 ) {

                                        Xaxis_value.add(time);

                                        if (Float.valueOf(close) >= Float.valueOf(open)) {
                                            colors[i] = Color.parseColor("#1D8348");
                                        } else {
                                            colors[i] = Color.parseColor("#E74C3C");
                                        }

                                        entries.add(new CandleEntry(i, Float.valueOf(high), Float.valueOf(low), Float.valueOf(open), Float.valueOf(close)));
                                        i++;
                                    }
                                }




                                try {

                                    tot_difference = (Float.parseFloat(current_close) - Float.parseFloat(yesterday_close));
                                    tot_percent = ((tot_difference / Float.parseFloat(yesterday_close)) * 100);
                                    DecimalFormat f1 = new DecimalFormat("##.00");

                                    float value = Float.parseFloat(f1.format(tot_difference));

                                    float total_per = Float.parseFloat(f1.format(tot_percent));
                                    if (tot_difference > 0) {
//                                    String percentageSymbol =  String.valueOf(total_per);
                                        holder.persymb.setText("+" + String.valueOf(value) + " (+" + String.valueOf(total_per) + "%)");
                                        holder.persymb.setTextColor(Color.parseColor("#63bd55"));
                                    } else {
//                                    percentageSymbol = String.valueOf(total_per);
                                        holder.persymb.setText(String.valueOf(value) + " (" + String.valueOf(total_per) + "%)");
                                        holder.persymb.setTextColor(Color.parseColor("#8b0000"));
                                    }
                                } catch (Exception e) {
                                    return ;
                                }


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

                                if (entries.isEmpty()) {
                                    holder.candleStickChart.clear();
                                } else {
                                    // set data
                                    holder.candleStickChart.setData(data);
                                }
                                holder.candleStickChart.setDescription(null);
                                holder.candleStickChart.getLegend().setEnabled(false);
                                dataset.notifyDataSetChanged();
                                holder.candleStickChart.notifyDataSetChanged();
                                holder.candleStickChart.invalidate();



                                XAxis xAxis1 = holder.candleStickChart.getXAxis();
                                xAxis1.setValueFormatter(new IndexAxisValueFormatter(Xaxis_value));
                                xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
                                xAxis1.setTextSize(5);


                                YAxis leftYAxis = holder.candleStickChart.getAxisLeft();
                                leftYAxis.setEnabled(false);

                                holder.candleStickChart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(context, MainActivity.class);
                                        intent.putExtra("symbol", namesymb);
                                        context.startActivity(intent);

                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
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
        holder.requestQueue.add(postRequest);


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


    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    /**
     * Enables https connections
     */
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



}