package com.example.quantifen;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CameraMenu extends AppCompatActivity {

    private Button          pictureCameraButton;
    private Button          pictureGalleryButton;
    private TabLayout       quantifenTabs;
    private TabLayout.Tab   cameraMenuTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_menu);

        pictureCameraButton = findViewById(R.id.camera_picture_button);
        pictureCameraButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                try{
                    Intent intent = new Intent(CameraMenu.this, CameraActivity.class);
                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        });

        pictureGalleryButton = findViewById(R.id.picture_gallery_button);
        pictureGalleryButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                try{
                    Intent intent = new Intent(CameraMenu.this, GalleryActivity.class);
                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        });

        quantifenTabs = findViewById(R.id.tabs);
        cameraMenuTab = quantifenTabs.getTabAt(1);
        cameraMenuTab.select();
        quantifenTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab){
                switch(quantifenTabs.getSelectedTabPosition()){
                    case 0:
                        Intent intent1 = new Intent(CameraMenu.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case 1:
                        break;
                    case 2:
                        Intent intent2 = new Intent(CameraMenu.this, LearnMenu.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(CameraMenu.this, Account.class);
                        startActivity(intent3);
                        break;
                    default:
                        Intent intentd = new Intent(CameraMenu.this, CameraMenu.class);
                        startActivity(intentd);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab){

            }

            public void onTabReselected(TabLayout.Tab tab){
                switch(quantifenTabs.getSelectedTabPosition()) {
                    case 0:
                        Intent intent1 = new Intent(CameraMenu.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(CameraMenu.this, LearnMenu.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(CameraMenu.this, Account.class);
                        startActivity(intent3);
                        break;
                    default:
                        Intent intentd = new Intent(CameraMenu.this, CameraMenu.class);
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
