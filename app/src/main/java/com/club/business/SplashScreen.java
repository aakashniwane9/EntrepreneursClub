package com.club.business;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    ImageView splash_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splash_image = (ImageView)findViewById(R.id.imageview);

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);

        splash_image.startAnimation(myanim);

        final Intent i = new Intent(this,HomePage.class);
        Thread timer = new Thread()
        {
            public void run()
            {
                try{
                    sleep(1);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}
