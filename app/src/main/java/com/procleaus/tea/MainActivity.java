package com.procleaus.tea;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btneonclick(View v) {
        Intent i = new Intent(MainActivity.this, EncryptActivity.class);
        startActivity(i);

    }

    public void btn2eonclick(View v) {
        Intent i = new Intent(MainActivity.this, SencryptActivity.class);
        startActivity(i);

    }

    public void btndoclick(View v) {
        Intent i = new Intent(MainActivity.this, DecryptActivity.class);
        startActivity(i);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}


