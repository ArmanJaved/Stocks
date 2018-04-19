package stocksymbolapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;


public class LinkService extends Service {


    private MyThread mythread;

    private static final String SYSTEM_PACKAGE_NAME = "android";
    private PackageManager mPackageManager = null;
    boolean isRunningnow=true;
    String android_user_Id;
    String user_Id_firebase;
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private DatabaseReference predictionDB;

    String currentDateTime;


    boolean checktraining;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();



//        showToast("Services is started ");

        mythread = new MyThread();


        // To avoid cpu-blocking, we create a background handler to run our service
        HandlerThread thread = new HandlerThread("TutorialService", Process.THREAD_PRIORITY_BACKGROUND);
        // start the new handler thread
        thread.start();

        mServiceLooper = thread.getLooper();
        // start the service using the background handler
        mServiceHandler = new ServiceHandler(mServiceLooper);





    }

    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Well calling mServiceHandler.sendMessage(message); from onStartCommand,
            // this method will be called.

            // Add your cpu-blocking activity here
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Toast.makeText(LogsService.this,"Service Started",Toast.LENGTH_LONG).show();
        if(!mythread.isAlive()) {
            mythread.start();
            // Location_log();F
        }

        mPackageManager = getPackageManager();

//        Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show();

        // call a new service handler. The service ID can be used to identify the service
        Message message = mServiceHandler.obtainMessage();
        message.arg1 = startId;
        mServiceHandler.sendMessage(message);

        return START_STICKY;

    }



    protected void showToast(final String msg){
        //gets the main thread
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // run this code in the main thread
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }




    public void GainerCall() {



        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://gainerstocksymbol.herokuapp.com/pai/gainers";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {


                        // response
                        Log.d("Response", response);
                        String as= response.toString();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR","error => "+error.toString());

                        String aasd= error.toString();
//                        Toast.makeText(getApplicationContext(), "Your Password is incorrect, please try again.", Toast.LENGTH_LONG).show();

                    }
                }
        ) {


        };

        int x=2;// retry count
        postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(postRequest);

    }

    public void LoserCall() {



        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://gainerstocksymbol.herokuapp.com/pai/losers";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {


                        // response
                        Log.d("Response", response);
                        String as= response.toString();


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




        };

        int x=2;// retry count
        postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(postRequest);

    }

    public void removenodesofsymbol ()
    {
         DatabaseReference database = FirebaseDatabase.getInstance().getReference();
         database.child("Symbols").removeValue();

    }






    class MyThread extends Thread {
        //900000
        static final long DELAY = 500;

        @Override
        public void run() {
            while (isRunningnow) {
                try {


                    Calendar now = Calendar.getInstance();
                    int currentmin = now.get(Calendar.MINUTE);
                    int sec = now.get(Calendar.SECOND);
                    int hr = now.get(Calendar.HOUR_OF_DAY);

                    if (currentmin == 15 && sec ==10 || currentmin == 30 && sec ==10 || currentmin == 45 && sec ==10 || currentmin ==00 && sec ==10 )
                    {
                        LoserCall();
                        GainerCall();
                    }

                    if (hr == 1 && currentmin==1 && sec == 10 || hr == 2 && currentmin==1 && sec == 10 || hr == 3 && currentmin==1 && sec == 10  )
                    {
                        removenodesofsymbol();

                    }


                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }







}
