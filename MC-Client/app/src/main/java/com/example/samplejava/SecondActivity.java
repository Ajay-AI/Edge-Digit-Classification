package com.example.samplejava;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.ByteArrayOutputStream;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.android.volley.Request;
import com.android.volley.Response;
import com.example.samplejava.ml.ModelBr;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.android.LoaderCallbackInterface;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;


public class SecondActivity extends Activity {

    private ImageView imageView;
    private Button button;
    private Button switcher;
    private Button send;
    Mat image;
    Bitmap bl = null;
    Bitmap br = null;
    Bitmap tl = null;
    Bitmap tr = null;

    String encodedImageBL;
    String encodedImageBR;
    String encodedImageTL;
    String encodedImageTR;



    String masterTL = "192.168.0.219"; //S22
    String TLPORT = "8085";

    String TR = "192.168.0.41";//"192.168.0.207"; //Oneplus 6T
    String TRPORT = "8085";

    String BL = "192.168.0.197"; //POCO
    String BLPORT = "8085";

    String BR = "192.168.0.161"; //Pixel 7
    String BRPORT = "8085";
    Bitmap photo = null;

//    Bitmap b = null;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                    image=new Mat();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        imageView = findViewById(R.id.caputuredImage);
        button  = findViewById(R.id.openCamera);
        switcher = findViewById(R.id.FirstFragment);
        send = findViewById(R.id.send);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(open_camera, 100);
            }
        });

    }
    private  File getOutputMediaFile(String fil){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/" + fil);

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_" +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public void storeImage( Bitmap image1, String fil ) {

        Bitmap image = photo;
        File pictureFile = getOutputMediaFile(fil);
        if (pictureFile == null) {
            Log.d("TAG",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("TAG", "Error accessing file: " + e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED){
            if (requestCode == 100) {
                AlertDialog alertDialog = new AlertDialog.Builder(SecondActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("No pic clicked use open camera");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });


                 photo = (Bitmap) data.getExtras().get("data");
                try {
                    Log.d("eroor asd", "e.toString()");
                    storeImage(photo, "file");
                }catch (Exception e){
                    Log.d("eroor asd", e.toString());
                }
                if (OpenCVLoader.initDebug()) {
                    Boolean temp = OpenCVLoader.initDebug();
                    Log.d("encoded", temp.toString());
                    image=new Mat();
                    Utils.bitmapToMat(photo, image);
                    Mat grey = new Mat();
                    Imgproc.cvtColor(image, grey, Imgproc.COLOR_BGR2GRAY);
                    Mat th = new Mat();
                    Imgproc.threshold(grey, th, 75, 255, Imgproc.THRESH_BINARY_INV);
                    Size size = new Size(20, 20);
                    Mat resized_digit = new Mat();
                    Imgproc.resize(th, resized_digit, size);
                    Mat padded_digit = new Mat(resized_digit.rows() + 8, resized_digit.cols() + 8, CvType.CV_32F, new Scalar(0));
                    Core.copyMakeBorder(resized_digit, padded_digit, 4, 4, 4, 4, Core.BORDER_CONSTANT);
                    Mat img = padded_digit.clone();
                    int height = img.rows();
                    int width = img.cols();
                    int w2 = width / 2;
                    Mat left = img.submat(0, height, 0, w2);
                    Mat right = img.submat(0, height, w2, width);
                    int h2 = height / 2;
                    Mat topleft = left.submat(0, h2, 0, w2);
                    Mat bottomleft = left.submat(h2, height, 0, w2);
                    Mat topright = right.submat(0, h2, 0, w2);
                    Mat bottomright = right.submat(h2, height, 0, w2);
//                    Bitmap tl = null;
                    tl = Bitmap.createBitmap(topleft.cols(), topleft.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(topleft, tl);
//                    Bitmap tr = null;
                    tr = Bitmap.createBitmap(topright.cols(), topright.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(topright, tr);
                    bl = Bitmap.createBitmap(bottomleft.cols(), bottomleft.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(bottomleft, bl);
//                    Bitmap br = null;
                    br = Bitmap.createBitmap(bottomright.cols(), bottomright.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(bottomright, br);
                    Log.d("height: ", String.valueOf(br.getHeight()));
                    imageView.setImageBitmap(tl);
//                    predictImage(br);

                    //tl tr bl br
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bl.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                     encodedImageBL = Base64.encodeToString(imageBytes,  Base64.DEFAULT);

                    baos = new ByteArrayOutputStream();
                    br.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                    imageBytes = baos.toByteArray();
                    byte[] imageBytesBR = baos.toByteArray();
                     encodedImageBR = Base64.encodeToString(imageBytesBR,  Base64.DEFAULT);


                    baos = new ByteArrayOutputStream();
                    tl.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytesTL = baos.toByteArray();
                    //imageBytesTL = baos.toByteArray();
                    encodedImageTL = Base64.encodeToString(imageBytesTL,  Base64.DEFAULT);


                    baos = new ByteArrayOutputStream();
                    tr.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    //imageBytes = baos.toByteArray();
                    byte[] imageBytesTR = baos.toByteArray();
                    encodedImageTR = Base64.encodeToString(imageBytesTR,  Base64.DEFAULT);


                }





                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bl.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes,  Base64.DEFAULT);
                Log.d("encoded", encodedImage);

                Spinner dropdown = findViewById(R.id.planets_spinner);
                String[] items = new String[]{"Indoor", "Outdoor", "Portrait"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
                dropdown.setAdapter(adapter);

                if(encodedImage != null){
                    send.setVisibility(View.VISIBLE); //SHOW the button
                    dropdown.setVisibility(View.GONE);
                }
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(Objects.equals(encodedImage, "")){
                            alertDialog.show();
                            return;
                        }

                        // add button to send
                        String text = dropdown.getSelectedItem().toString();
                        Log.d("dropdown text", text);

                        RequestQueue queue = Volley.newRequestQueue(SecondActivity.this);
                        JSONObject jsonBody = new JSONObject();

                        try {
                            jsonBody.put("message", encodedImageTL);
                            jsonBody.put("imageType", "TL");
                            //jsonBody.put("category", text);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        final String requestBody = jsonBody.toString();
                        Log.d("volleyJson", requestBody);
                        String url = "http://" + masterTL + ":" + TLPORT +  "/"; //"http://192.168.0.219:8080"; //"http://alfred1.ddns.net:30081";/ http://alfred1.ddns.net:30081/ or your server ip
                        Log.d("URL", url);
                        RequestQueue MyRequestQueue = Volley.newRequestQueue(SecondActivity.this);

                        JsonObjectRequest request_jsonTL = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        //Process os success response

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.e("second Error: ", error.getMessage());
                                // Volley send empty error since the url does not contain an SSL certificate
//                                if(error.getMessage() == ""){
                                alertDialog.setMessage("Image sent");
                                alertDialog.show();
//                                }
                            }

                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("Content-Type","application/json");
                                params.put("Accept", "application/json");
                                return params;
                            }
                        };
                        try {
                            jsonBody.put("message", encodedImageTR);
                            jsonBody.put("imageType", "TR");
                            //jsonBody.put("category", text);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        url = "http://" + TR + ":" + TRPORT +  "/";
                        JsonObjectRequest request_jsonTR = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        //Process os success response

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.e("second Error: ", error.getMessage());
                                // Volley send empty error since the url does not contain an SSL certificate
//                                if(error.getMessage() == ""){
                                alertDialog.setMessage("Image sent");
                                alertDialog.show();
//                                }
                            }

                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("Content-Type","application/json");
                                params.put("Accept", "application/json");
                                return params;
                            }
                        };
                        try {
                            jsonBody.put("message", encodedImageBR);
                            jsonBody.put("imageType", "BR");
                            //jsonBody.put("category", text);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        url = "http://" + BR + ":" + BRPORT +  "/";
                        JsonObjectRequest request_jsonBR = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        //Process os success response

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.e("second Error: ", error.getMessage());
                                // Volley send empty error since the url does not contain an SSL certificate
//                                if(error.getMessage() == ""){
                                alertDialog.setMessage("Image sent");
                                alertDialog.show();
//                                }
                            }

                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("Content-Type","application/json");
                                params.put("Accept", "application/json");
                                return params;
                            }
                        };

                        try {
                            jsonBody.put("message", encodedImageBL);
                            jsonBody.put("imageType", "BL");
                            //jsonBody.put("category", text);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        url = "http://" + BL + ":" + BLPORT +  "/";
                        JsonObjectRequest request_jsonBL = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        //Process os success response

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.e("Second activity sssError: ", error.getMessage());
                                // Volley send empty error since the url does not contain an SSL certificate
//                                if(error.getMessage() == ""){
                                alertDialog.setMessage("Image sent");
                                alertDialog.show();
//                                }
                            }

                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("Content-Type","application/json");
                                params.put("Accept", "application/json");
                                return params;
                            }
                        };

                        MyRequestQueue.add(request_jsonTL);
                        MyRequestQueue.add(request_jsonTR);
                        MyRequestQueue.add(request_jsonBL);
                        MyRequestQueue.add(request_jsonBR);

                    }
                });


            }
        }
    }
}
