package stocksymbolapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.stocksymbolapp.R;

import stocksymbolapp.Top.Losers.TopLosersActivity;
import stocksymbolapp.TopGainers.TopGainersActivity;

/**
 * Created by BrainPlow on 12/8/2017.
 */

@NonReusable
@Layout(R.layout.drawer_header)
public class DrawerHeader {


    private Context mContext;
    @View(R.id.nameTxt)
    private TextView nameTxt;

    @View(R.id.emailTxt)
    private TextView emailTxt;


    @View(R.id.top_gainer)
    private TextView top_gainers;

    @View(R.id.top_losers)
    private TextView top_losers;

    @View(R.id.help)
    private TextView help;


    @View(R.id.terms_priv)
    private TextView terms_privacy;

    public DrawerHeader(Context context ) {
        mContext = context;

    }

    @Resolve
    private void onResolved() {

        nameTxt.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Log.i("Message", "asdaffas");
            }
        });

        top_gainers.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                Intent init = new Intent(mContext, TopGainersActivity.class);
                init.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                mContext.startActivity(init);
            }
        });
        top_losers.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                Intent init = new Intent(mContext, TopLosersActivity.class);
                init.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                mContext.startActivity(init);
            }
        });

        help.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                Intent init = new Intent(mContext, Help.class);
                init.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                mContext.startActivity(init);

            }
        });

        terms_privacy.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                Intent init = new Intent(mContext, TermsPrivacy.class);
                init.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                mContext.startActivity(init);

            }
        });
    }
}