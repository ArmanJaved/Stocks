package stocksymbolapp;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stocksymbolapp.R;

import java.util.List;

/**
 * Created by BrainPlow on 8/25/2017.
 */

public class RestaurantsList extends ArrayAdapter<Resaurants> {

    private Activity context;
    private List<Resaurants> resaurantsList;
    public static final String TotalAmount = "totalamount";

    public RestaurantsList(Activity context, List<Resaurants> resaurantsList)
    {
        super(context, R.layout.listview_rest, resaurantsList);

        this.context = context;
        this.resaurantsList = resaurantsList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listviewitem = inflater.inflate(R.layout.listview_rest, null, true);

        TextView name = (TextView)listviewitem.findViewById(R.id.Textartistname);
//        TextView genre = (TextView)listviewitem.findViewById(R.id.textViewgenre);
        TextView date = (TextView)listviewitem.findViewById(R.id.Textartistname1);


        Resaurants resaurants = resaurantsList.get(position);
        float check  = Float.parseFloat(resaurants.getRestamount());
        if (check > 0)
        {
            date.setTextColor(Color.parseColor("#63bd55"));
            date.setText("+"+resaurants.getRestamount()+"%");
        }
        else {

            date.setTextColor(Color.parseColor("#8b0000"));
            date.setText(resaurants.getRestamount()+"%");
        }

        name.setText(resaurants.getRestname());
//        genre.setText(resaurants.getRestamount());

//        settotalamount (amount);

        return listviewitem;

    }




}

