package com.procleaus.tea;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashScreen extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    SharedPreferences sharedPreferences;
    ImageView mImageView;   //reference to the ImageView
    int xDim, yDim;     //stores ImageView dimensions
    private bitmapCreate bitmap;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.activity_splash);
        /*
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        */
        mImageView=(ImageView)findViewById(R.id.splashscreen_logo);
        xDim=200;
        yDim=200;
        mImageView.setImageBitmap(bitmap.decodeSampledBitmapFromResource(getResources(), R.drawable.logo2, xDim, yDim));

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                    Intent mainIntent = new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(mainIntent);
                    SplashScreen.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences("com.procleus.brime", MODE_PRIVATE);
    }


}
