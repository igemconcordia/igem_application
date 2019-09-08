package com.example.quantifen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

public class CropActivity extends AppCompatActivity {

    private ImageCroppingView patchPicture;

    private Button cropButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        patchPicture = findViewById(R.id.patch_picture);

        cropButton = findViewById(R.id.crop_button);

        Intent intent = getIntent();

        if(intent.hasExtra("image")) {
            String image_path = intent.getStringExtra("image");
            Uri selectedImage = Uri.parse(image_path);
            patchPicture.setImageURI(selectedImage);
        }else if (intent.hasExtra("data")) {
            Bitmap bitmap = (Bitmap) intent.getParcelableExtra("data");
            patchPicture.setImageBitmap(bitmap);
        }

        patchPicture.setDrawingCacheEnabled(true);
        patchPicture.buildDrawingCache(true);

        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap target = patchPicture.getCroppedImage();
                try {
                    //Write file
                    String filename = "bitmap.png";
                    FileOutputStream stream = openFileOutput(filename, Context.MODE_PRIVATE);
                    target.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    //Cleanup
                    stream.close();
                    target.recycle();

                    //Pop intent
                    Intent in1 = new Intent(CropActivity.this, PictureAnalysisActivity.class);
                    in1.putExtra("image", filename);
                    startActivity(in1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
