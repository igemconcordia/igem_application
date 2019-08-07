package com.example.quantifen;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button          statusButton;
    private Button          addDeviceButton;
    private TabLayout       quantifenTabs;
    private TabLayout.Tab   statusTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        addDeviceButton = findViewById(R.id.add_device_button);
        addDeviceButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                try{
                    Intent intent = new Intent(MainActivity.this, DevicePairingActivity.class);
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

    }

    public void onPause(){
        super.onPause();
        overridePendingTransition(0, 0);
    }

}
