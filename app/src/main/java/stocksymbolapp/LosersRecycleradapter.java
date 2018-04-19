package stocksymbolapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.stocksymbolapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class LosersRecycleradapter extends RecyclerView.Adapter<LosersRecycleradapter.MyViewHolder> {

    private List<String> moviesList;

    String [] stockdatasplit;
    int i;
    DataPoint[] dataPoints;
    int[] colors;
    CandleDataSet dataset;

    private Activity context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView symnam, compnam, persymb;
        public CandleStickChart candleStickChart ;
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
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        handleSSLHandshake();
        String data = moviesList.get(position);
        Log.d("dat", data);

//        holder.genre.setText(movie.getGenre());
//        holder.year.setText(movie.getYear());


        String[] Split = data.split(",");
        final String namesymb = Split[0];
        holder.symnam.setText(namesymb);

        String per = Split[1];

        per= per.substring(0, per.length() - 1);

        final float check  = Float.parseFloat(per.trim());
        if (check > 0)
        {
            holder.persymb.setTextColor(Color.parseColor("#63bd55"));
            holder.persymb.setText("+"+per+"%");

        }
        else {

            holder.persymb.setTextColor(Color.parseColor("#8b0000"));
            holder.persymb.setText(per+"%");
        }


        ////////////////////////Company NAME

        InputStreamReader is = null;
        try {
            is = new InputStreamReader(context.getAssets()
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
                if (namesymb.equals(Data[0]))
                {
                    break;
                }

            }

            String compnam= Data[1];

            compnam =compnam.substring(1);
            holder.compnam.setText(compnam);



        } catch (IOException e) {
            e.printStackTrace();
        }



        ////////////////////////CHART DISPLAY

        String url = "https://api.iextrading.com/1.0/stock/"+namesymb+"/chart/3m";

        final ArrayList<String> xVals = new ArrayList<String>();
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {


                        JSONArray json = null;
                        ArrayList<CandleEntry> entries = new ArrayList<>();

                        try {
                            int i =0;
                            json = new JSONArray(response);

                            int length = json.length();
                            colors = null;
                            colors = new int[length];
                            for(int j=0;j<json.length();j++) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                JSONObject e = json.getJSONObject(j);


                                String open = e.getString("open");
                                String high = e.getString("high");
                                String low = e.getString("low");
                                String close = e.getString("close");
                                String time = e.getString("date");

                                xVals.add(time);
                                if (Float.valueOf(close) >= Float.valueOf(open)) {
                                    colors[i] = Color.parseColor("#1D8348");
                                } else {
                                    colors[i] = Color.parseColor("#E74C3C");
                                }

                                entries.add(new CandleEntry(i, Float.valueOf(high), Float.valueOf(low), Float.valueOf(open), Float.valueOf(close)));
                                i++;
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
                            holder.candleStickChart.setData(data);
                            holder.candleStickChart.setDescription(null);
                            holder.candleStickChart.getLegend().setEnabled(false);
                            dataset.notifyDataSetChanged();
                            holder.candleStickChart.notifyDataSetChanged();
                            holder.candleStickChart.invalidate();

                            XAxis xAxis1 = holder.candleStickChart.getXAxis();
                            xAxis1.setValueFormatter(new IndexAxisValueFormatter(xVals));
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
                                    context.finish();
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        String es = error.toString();
                    }


                }

        ) {

        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy
                (DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
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