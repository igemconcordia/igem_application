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

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class EmergencyActivity extends AppCompatActivity {

    private Button  call911Button;
    private Button  callEmergencyContactButton;

    private static String   emergencyContactPhone;

    AccountDBHandler accountdbhandler = new AccountDBHandler(EmergencyActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        call911Button = findViewById(R.id.call_emergency_services);
        callEmergencyContactButton = findViewById(R.id.call_emergency_contact);

        final ArrayList<HashMap<String, String>> userList = accountdbhandler.GetUsers();

        if(!userList.isEmpty()){
            String ephone = userList.get(0).get("ephone");
            String ename = userList.get(0).get("ename");
            callEmergencyContactButton.setText("CALL " + ename);
        }

        call911Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        callEmergencyContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textEmergencyPhone();
                //callEmergencyPhone();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(requestCode == 101)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                textEmergencyPhone();
                //callEmergencyPhone();
            }
        }
    }

    public void callEmergencyPhone() {
        try
        {
            if(Build.VERSION.SDK_INT > 22)
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EmergencyActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + "514-618-5033"));
                startActivity(callIntent);

            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + "514-618-5033"));
                startActivity(callIntent);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void textEmergencyPhone(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EmergencyActivity.this, new String[]{Manifest.permission.SEND_SMS}, 101);
            return;
        }
        String emergencyTextMessage = "This is a message from Quantifen.";
        try{
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage("5146185033",null,emergencyTextMessage,null,null);
            Toast.makeText(EmergencyActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(EmergencyActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }
    }
}
