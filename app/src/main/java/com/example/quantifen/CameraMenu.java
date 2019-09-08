package com.example.quantifen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraMenu extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

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
                    captureFromCamera();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        });

        pictureGalleryButton = findViewById(R.id.picture_gallery_button);
        pictureGalleryButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                pickFromGallery();
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

    private void pickFromGallery(){
        try{
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");

            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);

            startActivityForResult(intent,GALLERY_REQUEST_CODE);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void captureFromCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    Intent intent = new Intent(CameraMenu.this, CropActivity.class);
                    intent.putExtra("image",selectedImage.toString());
                    startActivity(intent);
                    break;
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    Intent intentc = new Intent(CameraMenu.this, CropActivity.class);
                    intentc.putExtra("data",imageBitmap);
                    startActivity(intentc);
            }
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

}
