package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.util.IOUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.*;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.opencv.imgcodecs.Imgcodecs.IMREAD_UNCHANGED;

public class MainActivity extends AppCompatActivity {

    //Checking if OpenCV is loaded
    private static String TAG= "MainActivity";
    static{
        if(OpenCVLoader.initDebug())
        {
            Log.d(TAG, "OpenCV is Configured or Connected Successfully.");
        }
        else Log.d(TAG, "OpenCV is not working");
    }


    //initializing variables
    private static Mat door_open;
    private static Mat door_close;
    private static Mat door_close_tester;
    private static Mat door_open_tester;
    private Imgcodecs imageCodecs = new Imgcodecs();
    InputStream bit = null;
    private ImageView imageView;


   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        load_image1();
        load_image2();
        load_image3();
        load_image4();

        //All test cases for template matching
        Mat template_output = new Mat();
        int matchMethod = Imgproc.TM_CCORR_NORMED;
        Imgproc.matchTemplate(door_close_tester,door_close,template_output,matchMethod);
        //Imgproc.matchTemplate(door_close_tester,door_open,template_output,matchMethod);
        //Imgproc.matchTemplate(door_open_tester,door_close,template_output,matchMethod);
        //Imgproc.matchTemplate(door_open_tester,door_open,template_output,matchMethod);
        Core.MinMaxLocResult mmr = Core.minMaxLoc(template_output);
        System.out.println("*******\n*********\n");
        System.out.println(mmr.maxVal);
        System.out.println("*******\n*********\n");


    }

    public void load_image1(){
        //reading image by going to the path in asset folder
        InputStream bit=null;
        try {
            bit=getAssets().open("door_open_template.jpeg");
            BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
            bmpFactoryOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bmp = BitmapFactory.decodeStream(bit, null, bmpFactoryOptions);
            Mat ImageMat = new Mat();
            Utils.bitmapToMat(bmp, ImageMat);
            door_open = new Mat();
            Imgproc.cvtColor(ImageMat,door_open,Imgproc.COLOR_RGBA2GRAY);
            System.out.println("**********\n");
            System.out.println(door_open);
            imageView.setImageBitmap(bmp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bit!=null) {
                try {
                    bit.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void load_image2(){
        //reading image by going to the path in asset folder
        InputStream bit=null;
        try {
            bit=getAssets().open("door_closed_template.jpeg");
            BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
            bmpFactoryOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bmp = BitmapFactory.decodeStream(bit, null, bmpFactoryOptions);
            Mat ImageMat = new Mat();
            Utils.bitmapToMat(bmp, ImageMat);
            door_close = new Mat();
            Imgproc.cvtColor(ImageMat,door_close,Imgproc.COLOR_RGBA2GRAY);
            System.out.println("**********\n");
            System.out.println(door_close);
            imageView.setImageBitmap(bmp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bit!=null) {
                try {
                    bit.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void load_image3(){
        //reading image by going to the path in asset folder
        InputStream bit=null;
        try {
            bit=getAssets().open("door_open_tester.jpeg");
            BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
            bmpFactoryOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bmp = BitmapFactory.decodeStream(bit, null, bmpFactoryOptions);
            Mat ImageMat = new Mat();
            Utils.bitmapToMat(bmp, ImageMat);
            door_open_tester = new Mat();
            Imgproc.cvtColor(ImageMat,door_open_tester,Imgproc.COLOR_RGBA2GRAY);
            System.out.println("**********\n");
            System.out.println(door_open_tester);
            imageView.setImageBitmap(bmp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bit!=null) {
                try {
                    bit.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void load_image4(){
        //reading image by going to the path in asset folder
        InputStream bit=null;
        try {
            bit=getAssets().open("door_close_tester.jpeg");
            BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
            bmpFactoryOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bmp = BitmapFactory.decodeStream(bit, null, bmpFactoryOptions);
            Mat ImageMat = new Mat();
            Utils.bitmapToMat(bmp, ImageMat);
            door_close_tester = new Mat();
            Imgproc.cvtColor(ImageMat,door_close_tester,Imgproc.COLOR_RGBA2GRAY);
            System.out.println("**********\n");
            System.out.println(door_open);
            imageView.setImageBitmap(bmp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bit!=null) {
                try {
                    bit.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}