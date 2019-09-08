package com.example.quantifen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.util.Arrays;

public class PictureAnalysisActivity extends AppCompatActivity {

    private ImageView       patchPicture;

    private Button          processButton;

    private TextView        colorText;

    private Bitmap          bitmap2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_analysis);

        patchPicture = findViewById(R.id.patch_picture);

        colorText = findViewById(R.id.color_value);

        processButton = findViewById(R.id.analyse_button);

        Intent intent = getIntent();

        Bitmap bmp = null;
        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        patchPicture.setImageBitmap(bmp);

        patchPicture.setDrawingCacheEnabled(true);
        patchPicture.buildDrawingCache(true);

        processButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String analyze = "Analyzing your patch...";
                colorText.setText(analyze);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {

                                bitmap2 = patchPicture.getDrawingCache();
                                int pixel = bitmap2.getPixel(0,0);
                                int imageWidth = bitmap2.getWidth();
                                int imageHeight = bitmap2.getHeight();

                                int[][][] RGB = new int[imageHeight][imageWidth][3];

                                int r = Color.red(pixel);
                                int g = Color.green(pixel);
                                int b = Color.blue(pixel);
                                double count = 0;
                                double gcount = 0;

                                for (int w = 0; w < imageWidth; w++){
                                    for (int h = 0; h < imageHeight; h++){

                                        pixel = bitmap2.getPixel(w, h);

                                        r = Color.red(pixel);
                                        g = Color.green(pixel);
                                        b = Color.blue(pixel);

                                        RGB[h][w][0] = r;
                                        RGB[h][w][1] = g;
                                        RGB[h][w][2] = b;

                                        /*
                                        //Detecting green
                                        if(r < 155 && r > -1){
                                            if(g < 256 && g > 99){
                                                if(b < 171 && b > -1){
                                                    gcount++;
                                                }
                                            }
                                        }
                                        */

                                        if(r < 187 && r > -1){
                                            if(g < 113 && g > -1 ){
                                                if(b < 256 && b > 127){
                                                    gcount++;
                                                }
                                            }
                                        }

                                        count++;
                                    }
                                }
                                double percentGreen = gcount/count * 100;
                                String greenPercent = percentGreen + "";
                                colorText.setText(greenPercent);
                                System.out.println(gcount+"\n");
                                System.out.println(count+"\n");
                            }
                        }, 50);
            }
        });


    }
}
