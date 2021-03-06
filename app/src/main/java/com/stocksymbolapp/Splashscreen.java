package com.stocksymbolapp;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Splashscreen extends FragmentActivity {
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    /** Called when the activity is first created. */
    Thread splashTread;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        StartAnimations();
    }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 2000) {
                        sleep(100);
                        waited += 100;
                    }


                    Intent init = new Intent(getApplicationContext(), GainernLosers.class);
                    init.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    getApplicationContext().startActivity(init);
                } catch (InterruptedException e) {
                    // do nothing

                } finally {
                    Splashscreen.this.finish();
                }

            }
        };
        splashTread.start();

    }


}