package com.example.videoframesinassetfoldertracker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Bitmap[] bitmaps,bitmaps2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        bitmaps=new Bitmap[300];
        bitmaps2=new Bitmap[300];
        get_bitmaps();


    }

    public void get_bitmaps()
    {

        for(int i=0;i<bitmaps.length;i++)
        {
            InputStream bit = null;
            try{
                String x= i+".jpg";
                System.out.println(x);
                bit = getAssets().open(x);
                bitmaps[i]= BitmapFactory.decodeStream(bit);
                //imageView.setImageBitmap(bitmaps[i]);

            }catch(IOException e)
            {
                e.printStackTrace();
            }finally {
                //wait(10000);
                if(bit != null)
                {
                    try {
                        bit.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        bit.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

//        for(int i=0;i<bitmaps2.length;i++)
//        {
//            InputStream bit = null;
//            try{
//                String x= i+bitmaps.length+".jpg";
//                System.out.println(x);
//                bit = getAssets().open(x);
//                bitmaps2[i]= BitmapFactory.decodeStream(bit);
//                //imageView.setImageBitmap(bitmaps[i]);
//                wait(10000);
//            }catch(IOException e)
//            {
//                e.printStackTrace();
//            }finally {
//                //wait(10000);
//                if(bit != null)
//                {
//                    try {
//                        bit.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else{
//                    try {
//                        bit.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
    }

    public void wait(int millisec) {
        try {
            Thread.currentThread().sleep(millisec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}