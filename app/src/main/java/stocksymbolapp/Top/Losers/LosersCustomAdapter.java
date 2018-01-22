package stocksymbolapp.Top.Losers;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stocksymbolapp.R;

import java.util.ArrayList;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class LosersCustomAdapter extends ArrayAdapter<LosersDataModel> implements View.OnClickListener{

    private ArrayList<LosersDataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {

        TextView asp_name;
        TextView mentions;
        TextView positive;
        TextView negative;
        TextView netural;
        TextView status ;
        TextView asp_rate;

    }

    public LosersCustomAdapter(ArrayList<LosersDataModel> data, Context context) {
        super(context, R.layout.losers_item, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        LosersDataModel dataModel=(LosersDataModel) object;




        switch (v.getId())
        {

//            case R.id.item_info:
//
//                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
//
//                break;


        }

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        LosersDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.losers_item, parent, false);
            viewHolder.asp_name = (TextView) convertView.findViewById(R.id.asp_name);
            viewHolder.mentions = (TextView)convertView.findViewById(R.id.mentions);
            viewHolder.positive = (TextView) convertView.findViewById(R.id.positive);
            viewHolder.positive.setTextColor(Color.parseColor("#8b0000"));
            viewHolder.negative = (TextView) convertView.findViewById(R.id.negative);
            viewHolder.netural = (TextView) convertView.findViewById(R.id.netural);
            viewHolder.status = (TextView) convertView.findViewById(R.id.status);
            viewHolder.asp_rate = (TextView) convertView.findViewById(R.id.asp_Rate);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.asp_name.setText(dataModel.getAsp_name());
        viewHolder.mentions.setText(dataModel.getMentions());
        viewHolder.positive.setText(dataModel.getPositive());
        viewHolder.negative.setText( dataModel.getNegative());
        viewHolder.netural.setText(dataModel.getNetural());
        viewHolder.status.setText(dataModel.getStatus());
        viewHolder.asp_rate.setText(dataModel.getAsp_rate());
        viewHolder.negative.setOnClickListener(this);
        viewHolder.negative.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }


}
