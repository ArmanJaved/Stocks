package stocksymbolapp;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
public class AddData {

    FirebaseAuth auth;
    DatabaseReference artistreference;
    Context context;


    public AddData(Context context)
    {

        auth = FirebaseAuth.getInstance();
        this.context = context;
        artistreference = FirebaseDatabase.getInstance().getReference("WatchListsymbol").child("user");

    }


    public void addartist (String res, String amount)
    {
        String name= res;
        String strintg_genres = amount;
        if (!TextUtils.isEmpty(name))
        {

            long date = System.currentTimeMillis(); SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
            String dateString = sdf.format(date);

            String id = artistreference.push().getKey();
            Resaurants resaurants = new Resaurants(id, name, strintg_genres,dateString);
            artistreference.child(id).setValue(resaurants);
            Toast.makeText(context, "Stock symbol is added to Watchlist", Toast.LENGTH_LONG).show();


        }
        else
        {
           // Toast.makeText(context, "Please add Resaurants name  ", Toast.LENGTH_LONG).show();
            return;
        }

    }


}
