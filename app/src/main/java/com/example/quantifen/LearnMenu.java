package com.example.quantifen;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LearnMenu extends AppCompatActivity {

    private TabLayout   quantifenTabs;
    private TabLayout.Tab   learnMenuTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_menu);

        quantifenTabs = findViewById(R.id.tabs);
        learnMenuTab = quantifenTabs.getTabAt(2);
        learnMenuTab.select();
        quantifenTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab){
                switch(quantifenTabs.getSelectedTabPosition()){
                    case 0:
                        Intent intent1 = new Intent(LearnMenu.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case 1:
                        Intent intent2 = new Intent(LearnMenu.this, CameraMenu.class);
                        startActivity(intent2);
                        break;
                    case 2:
                        break;
                    case 3:
                        Intent intent3 = new Intent(LearnMenu.this, Account.class);
                        startActivity(intent3);
                        break;
                    default:
                        Intent intentd = new Intent(LearnMenu.this, LearnMenu.class);
                        startActivity(intentd);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab){

            }

            public void onTabReselected(TabLayout.Tab tab){
                switch(quantifenTabs.getSelectedTabPosition()) {
                    case 0:
                        Intent intent1 = new Intent(LearnMenu.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case 1:
                        Intent intent2 = new Intent(LearnMenu.this, LearnMenu.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(LearnMenu.this, Account.class);
                        startActivity(intent3);
                        break;
                    default:
                        Intent intentd = new Intent(LearnMenu.this, LearnMenu.class);
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
