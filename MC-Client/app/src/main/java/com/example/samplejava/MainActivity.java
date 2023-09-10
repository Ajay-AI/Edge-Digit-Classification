package com.example.samplejava;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.widget.ImageView;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.Request;
import com.android.volley.Response;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.samplejava.ml.ModelBl;
import com.example.samplejava.ml.ModelBr;
import com.example.samplejava.ml.ModelTl;
import com.example.samplejava.ml.ModelTr;



import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
/*
Hard code ip of all devices
check if the apps ip and port is master
if master
    send the image to the respective devices
Get response as a json
do calculations with the result and display output in master
*/

public class MainActivity extends AppCompatActivity {

    private static BluetoothAdapter mBluetoothAdapter;
    public  Bitmap decodedByteBl = null;
    public float[] confidences;
    private ImageView imageView;
    private ImageView imageView2;
    SecondActivity  b = new SecondActivity() ;
    float[] pred = new float[10];

//     Arrays.
//    Arrays.fill(pred, 1.0f);
    String currentIp = "";
    String masterTL = "192.168.0.219";//"192.168.0.219"; //POCO
    String TLPORT = "8085";

    String TR = "192.168.0.219"; //POCO
    int TRPORT = 8081;

    String BL = "192.168.0.219"; //POCO
    int BLPORT = 8081;

    String BR = "192.168.0.219"; //POCO
    int BRPORT = 8081;

    private float[][] arr;

    int counter=0;

    public  void predictImageBl(Bitmap tmp){
        try {

            ModelBl model = ModelBl.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 14, 14, 1}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 14 * 14 * 1);
            byteBuffer.order(ByteOrder.nativeOrder());
            int[] intValues = new int[14 * 14];
            tmp.getPixels(intValues, 0, tmp.getWidth(), 0, 0, tmp.getWidth(), tmp.getHeight());
            for (int pixel: intValues){
                float value = (float) Color.red(pixel)/255.0f;
                byteBuffer.putFloat(value);
            }
            inputFeature0.loadBuffer(byteBuffer);
            ModelBl.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            confidences = outputFeature0.getFloatArray();
            for (int i = 0; i < confidences.length; i++) {
                Log.d("Confidence", String.valueOf(confidences[i]));
            }
            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
    public void predictImageBr(Bitmap tmp){
        try {
            ModelBr model = ModelBr.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 14, 14, 1}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 14 * 14 * 1);
            byteBuffer.order(ByteOrder.nativeOrder());
            int[] intValues = new int[14 * 14];
            tmp.getPixels(intValues, 0, tmp.getWidth(), 0, 0, tmp.getWidth(), tmp.getHeight());
            for (int pixel: intValues){
                float value = (float) Color.red(pixel)/255.0f;
                byteBuffer.putFloat(value);
            }
            inputFeature0.loadBuffer(byteBuffer);
            ModelBr.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            confidences = outputFeature0.getFloatArray();
            for (int i = 0; i < confidences.length; i++) {
                Log.d("Confidence", String.valueOf(confidences[i]));
            }
            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
    public void predictImageTl(Bitmap tmp){
        try {
            ModelTl model = ModelTl.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 14, 14, 1}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 14 * 14 * 1);
            byteBuffer.order(ByteOrder.nativeOrder());
            int[] intValues = new int[14 * 14];
            tmp.getPixels(intValues, 0, tmp.getWidth(), 0, 0, tmp.getWidth(), tmp.getHeight());
            for (int pixel: intValues){
                float value = (float) Color.red(pixel)/255.0f;
                byteBuffer.putFloat(value);
            }
            inputFeature0.loadBuffer(byteBuffer);
            ModelTl.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            confidences = outputFeature0.getFloatArray();
            for (int i = 0; i < confidences.length; i++) {
                Log.d("Confidence", String.valueOf(confidences[i]));
            }
            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
    public void predictImageTr(Bitmap tmp){
        try {
            ModelTr model = ModelTr.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 14, 14, 1}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 14 * 14 * 1);
            byteBuffer.order(ByteOrder.nativeOrder());
            int[] intValues = new int[14 * 14];
            tmp.getPixels(intValues, 0, tmp.getWidth(), 0, 0, tmp.getWidth(), tmp.getHeight());
            for (int pixel: intValues){
                float value = (float) Color.red(pixel)/255.0f;
                byteBuffer.putFloat(value);
            }
            inputFeature0.loadBuffer(byteBuffer);
            ModelTr.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            confidences = outputFeature0.getFloatArray();
            for (int i = 0; i < confidences.length; i++) {
                Log.d("Confidence", String.valueOf(confidences[i]));
            }
            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }



    protected class WebServer extends NanoHTTPD {

        public WebServer(MainActivity mainActivity)
        {
            super(8085);
        }


        private void copyFile1(String inputPath, String inputFile, String outputPath) {

            InputStream in = null;
            OutputStream out = null;
            try {

                //create output directory if it doesn't exist
                File dir = new File (outputPath);
                if (!dir.exists())
                {
                    dir.mkdirs();
                }


                in = new FileInputStream(inputPath + inputFile);
                out = new FileOutputStream(outputPath + inputFile);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;

                // write the output file (You have now copied the file)
                out.flush();
                out.close();
                out = null;

            }  catch (FileNotFoundException fnfe1) {
                Log.e("tag", fnfe1.getMessage());
            }
            catch (Exception e) {
                Log.e("tag", e.getMessage());
            }

        }
        public void copyFileOrDirectory(String srcDir, String dstDir) {

            try {
                File src = new File(srcDir);
                File dst = new File(dstDir, src.getName());

                if (src.isDirectory()) {

                    String files[] = src.list();
                    int filesLength = files.length;
                    for (int i = 0; i < filesLength; i++) {
                        String src1 = (new File(src, files[i]).getPath());
                        String dst1 = dst.getPath();
                        copyFileOrDirectory(src1, dst1);

                    }
                } else {
                    copyFile(src, dst);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public  void copyFile(File sourceFile, File destFile) throws IOException {
            if (!destFile.getParentFile().exists())
                destFile.getParentFile().mkdirs();

            if (!destFile.exists()) {
                destFile.createNewFile();
            }

            FileChannel source = null;
            FileChannel destination = null;

            try {
                source = new FileInputStream(sourceFile).getChannel();
                destination = new FileOutputStream(destFile).getChannel();
                destination.transferFrom(source, 0, source.size());
            } finally {
                if (source != null) {
                    source.close();
                }
                if (destination != null) {
                    destination.close();
                }
            }
        }

        private  File getOutputMediaFile(String fil){
            // To be safe, you should check that the SDCard is mounted
            // using Environment.getExternalStorageState() before doing this.
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                    + "/Android/data/"
                    + getApplicationContext().getPackageName()
                    + "/" + fil  );

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

            Bitmap image = image1;
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
        public Response serve(String uri, Method method,
                              Map<String, String> header,
                              Map<String, String> parameters,
                              Map<String, String> files) {


            imageView = findViewById(R.id.caputuredImageFront);
            String answer = "asd";
            Log.d("base64 data", method.toString());
            Log.d("base64 data", files.toString());
            JSONObject jsonBody = null;
            String imageMessage = "";
            String imageType="";
            Log.d("current ip  : ", currentIp);
            if(method.toString().equals("POST") ) {
                try {
                    jsonBody = new JSONObject(files.get("postData"));
                } catch (JSONException e) {
                e.printStackTrace();
                }
                Log.d("base64 data", jsonBody.toString());
                try {
                    Log.d("base64 data", (String) jsonBody.get("message"));
                    imageMessage = (String) jsonBody.get("message");
                    imageType = (String) jsonBody.get("imageType");
                } catch (JSONException e) {
                e.printStackTrace();
                }
            }
//            Map<String, List<String>> decodedQueryParameters =
//                    decodeParameters(session.getQueryParameterString());
//            Log.i("test", String.valueOf(parameters.size()));
            if(method.toString().equals("POST")){ //parameters.size()>=2|| true
//                parameters.remove("NanoHttpd.QUERY_STRING");
//                Set<String> s  = parameters.keySet();
//                Log.d("asd", s.toString());
//                String te = parameters.get("message");
//                Log.d("asd", te);
                //te="data:image/png;base64,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";
                Log.d("JSON body print ", jsonBody.toString());
                if(jsonBody.has("confidences")){

                    Log.d("asdasdasdasdsa", jsonBody.toString());
                    String confi = "";
                    try {
                         confi = (String) jsonBody.get("confidences");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    List<String> li = Arrays.asList(confi);
//                    float [] li1 = li.get(0);
                    float[] floatArr = new float[0];

                    //                       JSONObject object = new JSONObject(Arrays.toString(confidences));

                    String value = confi;

                    String floatStr = value.replace("[", "").replace("]", "");

                    String[] valuesArr = floatStr.split(",");

                    floatArr = new float[valuesArr.length];

                    for (int i = 0; i < valuesArr.length; i++) {
                        String floatString = valuesArr[i];

                        if (TextUtils.isEmpty(floatStr) || TextUtils.isEmpty(floatStr.trim())) {
                            floatArr[i] = 0.0f;
                            continue;
                        }

                        floatArr[i] = Float.parseFloat(floatString.trim());
                    }

                    for (int i = 0; i < floatArr.length; i++) {
                        Log.d("TAG", "value : at " + i + " is " + floatArr[i]);
                    }

                    Log.d("li ", Arrays.toString(floatArr));
                    for (int i = 0; i < floatArr.length; i++) {
                        pred[i]=pred[i]*floatArr[i];

                    }
                    for (int i = 0; i < pred.length; i++) {
                        Math.pow(pred[i],1/4);

                    }
                    counter = counter+1;
                    Log.d("Counter", String.valueOf(counter));
                    int maxIndex=0;
                    if(counter==4){
                        counter=0;
                        float maxValue=0.f;
                        for (int i = 0; i < pred.length; i++) {
                            if (pred[i] > maxValue) {
                                maxIndex = i;
                                maxValue = pred[i];
                            }


                        }
                        for (int i = 0; i < pred.length; i++) {
                            pred[i]=1.0f;

                        }
                        Log.d("Predicted Value", String.valueOf(maxIndex));
                        String o = String.valueOf(maxIndex);
//                        SecondActivity temp = new SecondActivity();
//                        Bitmap p = null;

                        try {
//                            temp.storeImage(p, o);
                            String y = Environment.getExternalStorageDirectory()
                                    + "/Android/data/"
                                    + getApplicationContext().getPackageName()
                                    + "/" + "file" + "/MI_.jpg";

                            String p1 = Environment.getExternalStorageDirectory()
                                    + "/Android/data/"
                                    + getApplicationContext().getPackageName()
                                    + "/" + o;
//                            File from = new File(y);
//                            String fileName = "/myImage.jpg";
                            File file = new File(y);
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                            storeImage(bitmap, o);
//
//                            File from = new File(y);
//                            File to = new File(p1);
//                            if(!to.exists()){
//                                to.mkdirs();
//                            }
//                            from.renameTo(new File(to, from.getName()));
//                            from.renameTo(to);

//                            copyFile("");

//                            copyFile(y, "MI_.jpg", p1+ "/imagem.jpg");
//                            copyFileOrDirectory("y", p1+"/imagem.jpg");

                        }

                        catch (Exception e){
                            Log.d("svaed error","asdasdasdasd" );
                        }



                    }


                    return newFixedLengthResponse(answer);
                }
                else{
                    Log.d("tem", (String) jsonBody.toString());
                }

                byte[] decodedString = Base64.decode(imageMessage, Base64.DEFAULT);
                //caputuredImageFront

//                imageView2 = findViewById(R.id.caputuredImage2);
                decodedByteBl = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

//                imageView2.setImageBitmap(decodedByteBl);
                imageView.setImageBitmap(decodedByteBl);
                if(imageType.equals("TL")){
                    predictImageTl(decodedByteBl);
                }
                else if (imageType.equals("TR")){
                    predictImageTr(decodedByteBl);
                }
                else if (imageType.equals("BL")){
                    predictImageBl(decodedByteBl);
                }
                else if (imageType.equals("BR")){
                    predictImageBr(decodedByteBl);
                }




                //client
                Log.d("below perd", "prefgeddsdadsafdsafkbdshbfdskjhfbv");
                JSONObject jsonBody1 = new JSONObject();

                //                    jsonBody1.put("confidences", Arrays.toString(confidences));
//                    jsonBody = new JSONObject();
                try {
                    jsonBody1.put("confidences", Arrays.toString(confidences));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                postReq(jsonBody, "");
                try{
                    postReq(jsonBody1, "");
                }
                catch ( Exception e){
                    Log.d("error34", e.toString());
                    e.printStackTrace();

                }

                Log.d("below ", "prefgeddsdadsafdsafkbdshbfdskjhfbv");
                Log.d("cinfo array", jsonBody1.toString());
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                String url = "http://192.168.0.219:8085/";

                Log.d("uasdsadsaddsasdarl\n\n\n", url);


//                    confidences =
//                    for (int i = 0; i < confidences.length; i++) {
//                        Log.d("Confidence123", Arrays.toString(confidences));
//                    }

//                    jsonBody1.put("imageType", "TR");
                //jsonBody.put("category", text);


            }


//            MyRequestQueue.add(request_jsonTR);


//            try {
//                // Open file from SD Card
//                File root = Environment.getExternalStorageDirectory();
//                FileReader index = new FileReader(root.getAbsolutePath() +
//                        "/www/index.html");
//                BufferedReader reader = new BufferedReader(index);
//                String line = "";
//                while ((line = reader.readLine()) != null) {
//                    answer += line;
//                }
//                reader.close();
//
//            } catch(IOException ioe) {
//                Log.w("Httpd", ioe.toString());
//            }


            return newFixedLengthResponse(answer);
        }

    }

//    private static void SaveImage(Bitmap finalBitmap) {
//
//        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
//        File myDir = new File(root + "/saved_images");
//        myDir.mkdirs();
//
//        String fname = "Image-"+ o +".jpg";
//        File file = new File (myDir, fname);
//        if (file.exists ()) file.delete ();
//        try {
//            FileOutputStream out = new FileOutputStream(file);
//            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//            out.flush();
//            out.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void postReq(JSONObject jsonBody, String url){

        Log.e("inside postreq","insude catch");

//         jsonBody = new JSONObject();
//        try {
//            jsonBody.put("confidences", jsonBody);
//        } catch (JSONException e) {
//            Log.e("asdsa","insude catch");
//            e.printStackTrace();
//        }
//        AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
        url = "http://192.168.0.219:8085/";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request_jsonBR = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Process os success response

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("asdsadd Error: ", error.getMessage());
                // Volley send empty error since the url does not contain an SSL certificate
//                                if(error.getMessage() == ""){
//                alertDialog.setMessage("Image sssdfdsent");
//                alertDialog.show();
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

// add the request object to the queue to be executed
        queue.add(request_jsonBR);

    }


//    private String handleGet(NanoHTTPD.IHTTPSession session, Map<String, String> parms) {
//        return server.handleRequest("{'name':'status', 'value':''}");
//    }

    private WebServer server;
    /** Called when the activity is first created. */
//    @Override
////    public void onCreate(Bundle savedInstanceState)
////    {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.main);
////
////    }
//

    @Override
    public void onDestroy()
    {
        super.onDestroy();
//        unregisterReceiver(mBluetoothAdapter);
        if (server != null)
            server.stop();
    }
    @SuppressLint("MissingPermission")
    public void enableDisableBT(){
        if(mBluetoothAdapter==null){
            Log.d("MA", "no bt");
        }
        if(!mBluetoothAdapter.isEnabled()){
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mReceiver, BTIntent);
        }
        if(mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.disable();
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mReceiver, BTIntent);
        }
    }

    public final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action  = intent.getAction();
            if(action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)){
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

//                switch (state){
//
//                }

            }
        }
    };

   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        SecondActivity.postReq()


        //Bluetooth
        Arrays.fill(pred, 1.0f);

        JSONObject json = new JSONObject();
//        postReq(json, "");
        Context context = getApplicationContext();
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        Log.d("ip  ", ip);
        currentIp = ip;


        //Server
        server = new WebServer(MainActivity.this);
        try {
            server.start();
        } catch(IOException ioe) {
            Log.w("Httpd", "The server could not start.");
        }
        Log.w("Httpd", "Web server initialized.");


//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Button switcher = findViewById(R.id.FirstFragment);
        Button bthONOFF = findViewById(R.id.BthONOFF);

        switcher.setAlpha(0f);
        switcher.setTranslationY(50);
        switcher.animate().alpha(1f).translationYBy(-50).setDuration(1500);


        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        startActivity(intent);
                        //Yes button clicked
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
//        bthONOFF.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                enableDisableBT();
//            }
//        });


        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                //Start your second activity

            }
        });

    }
}

