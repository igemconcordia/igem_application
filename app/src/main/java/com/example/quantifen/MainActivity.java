package com.example.quantifen;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.quantifen.BluetoothLeService.concentrationData;

public class MainActivity extends AppCompatActivity {

    private Button          statusButton;
    private TabLayout       quantifenTabs;
    private TabLayout.Tab   statusTab;

    private TextView        seeGraphsLink;
    private TextView        addDeviceLink;
    private TextView        fentanylData;
    private TextView        temperatureData;

    private Toolbar         mainToolbar;

    private static final int STATE_MESSAGE_RECEIVED = 1;

    AccountDBHandler accountdbhandler = new AccountDBHandler(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<HashMap<String, String>> userList = accountdbhandler.GetUsers();

        fentanylData = findViewById(R.id.fen_data);
        temperatureData = findViewById(R.id.temp_data);

        mainToolbar = findViewById(R.id.status_toolbar);

        mainToolbar.setTitle("Hello " + userList.get(User.getId()).get("gname") + "!");

        statusButton = findViewById(R.id.status_button);
        statusButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                try {
                    Intent intent = new Intent(MainActivity.this, EmergencyActivity.class);
                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        });

        addDeviceLink = findViewById(R.id.add_device_link);
        addDeviceLink.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                try{
                    Intent intent = new Intent(MainActivity.this, BluetoothActivity.class);
                    startActivity(intent);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        });

        quantifenTabs = findViewById(R.id.tabs);
        statusTab = quantifenTabs.getTabAt(0);
        statusTab.select();
        quantifenTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab){
                switch(quantifenTabs.getSelectedTabPosition()){
                    case 0:
                        break;
                    case 1:
                        Intent intent1 = new Intent(MainActivity.this, CameraMenu.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(MainActivity.this, LearnMenu.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(MainActivity.this, Account.class);
                        startActivity(intent3);
                        break;
                    default:
                        Intent intentd = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intentd);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab){

            }

            public void onTabReselected(TabLayout.Tab tab){
                switch(quantifenTabs.getSelectedTabPosition()) {
                    case 1:
                        Intent intent1 = new Intent(MainActivity.this, CameraMenu.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(MainActivity.this, LearnMenu.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(MainActivity.this, Account.class);
                        startActivity(intent3);
                        break;
                    default:
                        Intent intentd = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intentd);
                }
            }

        });

        final Handler dataHandler = new Handler();
        final int delay = 1000; //milliseconds

        dataHandler.postDelayed(new Runnable(){
            public void run(){
                //do something
                //System.out.println(concentrationData+"\n");
                if(!concentrationData.trim().isEmpty()) {
                    fentanylData.setText(concentrationData.trim() + " ppm");
                }
                if(!BluetoothLeService.temperatureData.trim().isEmpty()){
                    temperatureData.setText(BluetoothLeService.temperatureData.trim() + "°C");
                }

                if(!BluetoothLeService.color.equals("")) {
                    fentanylData.setTextColor(Color.parseColor(BluetoothLeService.color.trim()));
                    statusButton.setBackgroundColor(Color.parseColor(BluetoothLeService.color.trim()));
                }
                dataHandler.postDelayed(this, delay);
            }
        }, delay);

    }


    public void onPause(){
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public void logout(){
        Intent intent = new Intent(MainActivity.this, LogJoinActivity.class);
        startActivity(intent);
    }

}
