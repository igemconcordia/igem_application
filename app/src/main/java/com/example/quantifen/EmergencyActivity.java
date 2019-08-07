/*
This activity is the emergency calling activity.
Components:
    - Emergency Services Call Button (e.g. 911 in North America)
        - OnClick: Confirmation dialog (Yes/No)
            - If Yes, call emergency services
            - If No, close Confirmation dialog
    - Emergency Contact Call Button (e.g. Mom)
        - OnClick: Confirmation dialog (Yes/No)
            - If Yes, call emergency contact
            - If No, close Confirmation dialog
    - Return to Status Button
        - OnClick: Go to MainActivity
Accessed From:
    - Emergency Cross Button
        - In MainActivity
    - Red Status Button
        - On All Action Bars
 */

package com.example.quantifen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EmergencyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
    }
}
