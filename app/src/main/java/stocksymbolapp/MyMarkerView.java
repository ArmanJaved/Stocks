package stocksymbolapp;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.stocksymbolapp.R;

import java.util.List;

/**
 * Created by BrainPlow on 12/12/2017.
 */

public class MyMarkerView extends MarkerView {

    private TextView tv_high, tv_low, tv_open, tv_close,tv_volume;
    LinearLayout llt_markerview;
    private static CountDownTimer timer;
    ActivityCommunicator communicator;
    Context context;
    public static Toast toastShow;

    public MyMarkerView(Context context, int layoutResource, List<BarEntry> volumes) {
        super(context, layoutResource);
//        this.volumes = volumes;
        communicator = (ActivityCommunicator) context;
        this.context = context;
        llt_markerview = findViewById(R.id.llt_markerview);
        tv_high = findViewById(R.id.tv_high);
        tv_low = findViewById(R.id.tv_low);
        tv_open = findViewById(R.id.tv_open);
        tv_close = findViewById(R.id.tv_close);
//        tv_volume = (TextView) findViewById(R.id.tv_volume);
    }


    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;


            tv_high.setText("High: " + ce.getHigh());
            tv_low.setText("Low: " + ce.getLow());
            tv_open.setText("Open: " + ce.getOpen());
            tv_close.setText("Close: " + ce.getClose());

//            tv_volume.setText("Volume: "+ volumes.get(ce.get);
            llt_markerview.setVisibility(View.VISIBLE);


//            showToast(context, "asdfaf");

            if (timer != null)
                timer.cancel();
            timer = new CountDownTimer(5000, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {

                    communicator.passDataToActivity();

                }
            };
            timer.start();

        }
    }


    public void showToast(Context actRef, String message) {

        if (toastShow == null
                || toastShow.getView().getWindowVisibility() != View.VISIBLE) {
            toastShow = Toast.makeText(actRef, message, Toast.LENGTH_SHORT);
            toastShow.setGravity(Gravity.CENTER, 0, 0);
            toastShow.show();
        }
    }



    public interface ActivityCommunicator {
        void passDataToActivity();
    }


}