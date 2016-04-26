package com.btran.bu.sudokusolver;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class SplashActivity extends Activity
{
    private static final int SPLASH_DISPLAY_LENGTH = 2000;

    private static final float ANIMATION_ALPHA_FROM = 0.0f;
    private static final float ANIMATION_ALPHA_TO = 1.0f;
    private static final int ANIMATION_ALPHA_START_OFFSET = 250;
    private static final int ANIMATION_ALPHA_DURATION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // fade in the logo on the splash screen
        AlphaAnimation alphaAnimation = new AlphaAnimation(ANIMATION_ALPHA_FROM, ANIMATION_ALPHA_TO);
        alphaAnimation.setStartOffset(ANIMATION_ALPHA_START_OFFSET);
        alphaAnimation.setDuration(ANIMATION_ALPHA_DURATION);
        alphaAnimation.setFillAfter(true);

        // set the animation onto the image
		ImageView title = (ImageView) findViewById(R.id.sudokuTitle);
        title.startAnimation(alphaAnimation);

        // set a delayed function call, which will trigger an intent to go to the Main Activity
		new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                // Create an Intent that will start the MainActivity
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish(); // immediately stop the thread
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
