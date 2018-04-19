package stocksymbolapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stocksymbolapp.R;

import java.text.SimpleDateFormat;
import java.util.zip.Inflater;

public class AddData {

    FirebaseAuth auth;
    Button watch_L;
    DatabaseReference artistreference;
    Context context;
    boolean flag;


    public AddData(Context context)
    {

        auth = FirebaseAuth.getInstance();
        this.context = context;

        String android_user_Id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        artistreference = FirebaseDatabase.getInstance().getReference("WatchListsymbol").child(android_user_Id);

    }


    public void addartist (final String res, String amount)
    {
        final String name= res;
        final String strintg_genres = amount;
        if (!TextUtils.isEmpty(name))
        {

            artistreference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                        Watch message = messageSnapshot.getValue(Watch.class);

                        if (message.getRestname().equals(res))
                        {
                            flag = true;
                        }

                    }

                    if (!flag)
                    {

                        long date = System.currentTimeMillis(); SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                        String dateString = sdf.format(date);
                        String id = artistreference.push().getKey();
                        Watch resaurants = new Watch(id, name, strintg_genres,dateString);
                        String as = resaurants.getRestname();
                        artistreference.child(id).setValue(resaurants);
                        Toast.makeText(context, "Stock symbol is added to Watchlist", Toast.LENGTH_LONG).show();





                    }
                    else if (flag){

                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }




    }


}
