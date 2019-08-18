package com.example.quantifen;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Create an Intent that will start the Menu-Activity. */
        Intent mainIntent = new Intent(SplashScreen.this,LogJoinActivity.class);
        SplashScreen.this.startActivity(mainIntent);
        SplashScreen.this.finish();

    }
}
