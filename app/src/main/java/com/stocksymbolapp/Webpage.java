package com.stocksymbolapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import dmax.dialog.SpotsDialog;

public class Webpage extends AppCompatActivity {


    private WebView mWebview ;
    String link ;



    SpotsDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webpage);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new SpotsDialog(this);
        dialog.show();
        String s = getIntent().getStringExtra("news");
        String symbol = getIntent().getStringExtra("symbol");

        if (s.toLowerCase().contains("yahoo"))
        {
            link = "https://finance.yahoo.com/quote/"+symbol+"/news?p="+symbol;
        }
        else if (s.toLowerCase().contains("market"))
        {
            link = "https://www.marketwatch.com/investing/stock/"+symbol+"/news";
        }
        else if (s.toLowerCase().contains("bloomberg"))
        {
            link = "https://www.bloomberg.com/quote/"+symbol+":US";
        }
        else if (s.toLowerCase().contains("seeking"))
        {
            link = "https://seekingalpha.com/symbol/"+symbol+"/news";
        }
        else if (s.toLowerCase().contains("stock"))
        {
            link = "https://stocktwits.com/symbol/"+symbol;
        }
        else if (s.toLowerCase().contains("twitter"))
        {
            link = "https://twitter.com/search?vertical=news&q=%24"+symbol+"&src=typd&lang=en";
        }



        mWebview  = new WebView(this);

        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

        final Activity activity = this;

        mWebview.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                dialog.dismiss();
            }
        });



        mWebview .loadUrl(link);
        setContentView(mWebview );


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }







}
