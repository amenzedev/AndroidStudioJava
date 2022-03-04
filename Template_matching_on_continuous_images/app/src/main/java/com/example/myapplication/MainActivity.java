package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static String TAG = "MainActivity";
    static{
        if(OpenCVLoader.initDebug())
        {
            Log.d(TAG,"Opencv installed successfully");
        }
        else{
            Log.d(TAG,"opencv not installed");
        }

    }

    //initializing variables
    private static Mat door_open;
    private static Mat door_close;
    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;
    Size size = new Size(700,1000);


   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraBridgeViewBase = (JavaCameraView) findViewById(R.id.CameraView);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);


        //System.loadLibrary(Core.Native_library_name);
        baseLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                super.onManagerConnected(status);
                switch(status)
                {
                    case BaseLoaderCallback.SUCCESS:
                        cameraBridgeViewBase.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }
            }
        };

        load_image1();
        load_image2();




    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        Mat frame = inputFrame.rgba();
        System.out.println("*******\n***\n");
        System.out.println(frame);
        System.out.println("*******\n***\n");
        System.out.println(MainActivity.door_open);
        Core.rotate(frame,frame,Core.ROTATE_90_CLOCKWISE);
        Mat input_image = new Mat();
        Imgproc.cvtColor(frame,input_image,Imgproc.COLOR_RGBA2GRAY);
        int matchMethod = Imgproc.TM_CCORR_NORMED;
        double accuracy_open=0,accuracy_close=0;
        Mat matching_output = new Mat();
        Mat door_open_template = new Mat();
        door_open_template = MainActivity.door_open;

        Imgproc.matchTemplate(input_image,door_open_template,matching_output,matchMethod);
        Core.MinMaxLocResult result = Core.minMaxLoc(matching_output);
        accuracy_open=result.maxVal;

        Mat door_close_template = new Mat();
        door_close_template = MainActivity.door_close;
        Imgproc.matchTemplate(input_image,door_close_template,matching_output,matchMethod);
        result = Core.minMaxLoc(matching_output);
        accuracy_close=result.maxVal;
        String door_status_text = "";

        if(accuracy_open > accuracy_close)
        {
            door_status_text="Door Open! "+ accuracy_open+ " > "+accuracy_close;
        }
        else door_status_text = "Door Closed! "+accuracy_close+ " > "+accuracy_open;

        // Adding Text
        Imgproc.putText (
                frame,                          // Matrix obj of the image
                door_status_text,          // Text to be added
                new Point(10, 50),               // point
                Core.FONT_HERSHEY_SIMPLEX ,      // front face
                1,                               // front scale
                new Scalar(0, 0, 0),             // Scalar object for color
                4                                // Thickness
        );

        return frame;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(),"There's a problem, yo!", Toast.LENGTH_SHORT).show();
        }

        else
        {
            baseLoaderCallback.onManagerConnected(baseLoaderCallback.SUCCESS);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(cameraBridgeViewBase!=null){

            cameraBridgeViewBase.disableView();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraBridgeViewBase!=null){
            cameraBridgeViewBase.disableView();
        }
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
            Imgproc.resize(door_open,door_open,size);
            System.out.println("**********\n");
            System.out.println(door_open);
            //imageView.setImageBitmap(bmp);
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
            Imgproc.resize(door_close,door_close,size);
            System.out.println("**********\n");
            System.out.println(door_close);
            //imageView.setImageBitmap(bmp);
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