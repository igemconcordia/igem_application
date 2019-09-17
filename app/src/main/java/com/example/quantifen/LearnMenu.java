package com.example.quantifen;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LearnMenu extends AppCompatActivity {

    private Button      locations;
    private Button      igemConcordiaButton;
    private TabLayout   quantifenTabs;
    private TabLayout.Tab   learnMenuTab;

    private TextView    factText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_menu);

        factText = findViewById(R.id.fact_text);
        igemConcordiaButton = findViewById(R.id.igem_concordia_button);

        locations = findViewById(R.id.maps);
        locations.setOnClickListener(new View.OnClickListener(){
           public void onClick(View view){
               Intent intent = new Intent(LearnMenu.this, MapsActivity.class);
               startActivity(intent);
           }
        });

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
