package com.example.quantifen;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Account extends AppCompatActivity {

    private Button          logoutButton;
    private TabLayout       quantifenTabs;
    private TabLayout.Tab   accountTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                try {
                    Intent intent = new Intent(Account.this, LogJoinActivity.class);
                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        });

        quantifenTabs = findViewById(R.id.tabs);
        accountTab = quantifenTabs.getTabAt(3);
        accountTab.select();
        quantifenTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab){
                switch(quantifenTabs.getSelectedTabPosition()){
                    case 0:
                        Intent intent1 = new Intent(Account.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case 1:
                        Intent intent2 = new Intent(Account.this, CameraMenu.class);
                        startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3 = new Intent(Account.this, LearnMenu.class);
                        startActivity(intent3);
                        break;
                    case 3:
                        break;
                    default:
                        Intent intentd = new Intent(Account.this, Account.class);
                        startActivity(intentd);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab){

            }

            public void onTabReselected(TabLayout.Tab tab){
                switch(quantifenTabs.getSelectedTabPosition()) {
                    case 0:
                        Intent intent1 = new Intent(Account.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case 1:
                        Intent intent2 = new Intent(Account.this, LearnMenu.class);
                        startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3 = new Intent(Account.this, Account.class);
                        startActivity(intent3);
                        break;
                    default:
                        Intent intentd = new Intent(Account.this, Account.class);
                        startActivity(intentd);
                }
            }

        });
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

}
