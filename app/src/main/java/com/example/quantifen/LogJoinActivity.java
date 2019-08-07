/*
This activity is the Log and Join Activity.
Purpose:
    - Provides a choice for the user to either Join Quantifen or Log into their Quantifen account
Components:
    - Join Quantifen Button
        - OnClick: Choose Device Activity
    - Log In Button
        - OnClick: Log In Activity
    - Quantifen Logo
Accessed From:
    - Startup after Splash Screen if user is not logged in
    - From Log Out Button
        - In Account Activity
        - In Settings Activity
 */

package com.example.quantifen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogJoinActivity extends AppCompatActivity {

    private Button  joinButton;
    private Button  loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_join);

        joinButton = findViewById(R.id.join_button);
        joinButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                try {
                    Intent intent = new Intent(LogJoinActivity.this, SignupActivity.class);
                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        });

        loginButton = findViewById(R.id.log_in_button);
        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                try {
                    Intent intent = new Intent(LogJoinActivity.this, LoginActivity.class);
                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        });
    }


}
