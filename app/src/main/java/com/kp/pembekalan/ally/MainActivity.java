package com.kp.pembekalan.ally;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Network;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidhiddencamera.CameraConfig;
import com.androidhiddencamera.CameraError;
import com.androidhiddencamera.HiddenCameraActivity;
import com.androidhiddencamera.HiddenCameraFragment;
import com.androidhiddencamera.HiddenCameraUtils;
import com.androidhiddencamera.config.CameraFacing;
import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraResolution;
import com.androidhiddencamera.config.CameraRotation;
import com.kp.pembekalan.ally.interfaces.APIServices;
import com.kp.pembekalan.ally.interfaces.Recommendation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends HiddenCameraActivity {
    private static Retrofit retrofit;
    public static String BASE_URL = "http://54.169.68.116:4000/";
    private static final int REQ_CODE_CAMERA_PERMISSION = 1253;

    private CameraConfig mCameraConfig;
    private APIServices service;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl( BASE_URL )
                    .addConverterFactory( GsonConverterFactory.create() )
                    .build();
        }
        return retrofit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

//        if (android.os.Build.VERSION.SDK_INT > 9) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }

        mCameraConfig = new CameraConfig()
                .getBuilder( this )
                .setCameraFacing( CameraFacing.FRONT_FACING_CAMERA )
                .setCameraResolution( CameraResolution.HIGH_RESOLUTION )
                .setImageFormat( CameraImageFormat.FORMAT_JPEG )
                .setImageRotation( CameraRotation.ROTATION_270 )
                //  .setCameraFocus(CameraFocus.AUTO)
                .build();
        //Check for the camera permission for the runtime
        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.CAMERA )
                == PackageManager.PERMISSION_GRANTED) {
            //Start camera preview
            startCamera( mCameraConfig );
        } else {
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.CAMERA},
                    REQ_CODE_CAMERA_PERMISSION );
        }

        service = getRetrofitInstance().create( APIServices.class);


        //Take a picture
        findViewById( R.id.cam_prev ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent( getApplication(), KatalogActivity.class );
                //Take picture using the camera without preview.
                takePicture();
//                   / Intent intent = new Intent( getApplication(), KatalogActivity.class );
            }
        } );
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQ_CODE_CAMERA_PERMISSION) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera( mCameraConfig );
            } else {
                Toast.makeText( this, R.string.error_camera_permission_denied, Toast.LENGTH_LONG ).show();
            }
        } else {
            super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        }
    }

    private class NetworkCall extends AsyncTask<Call<List<Recommendation>>, Void, List<Recommendation>> {
        @Override
        protected List<Recommendation> doInBackground(Call<List<Recommendation>>... params) {
            try {
                Call call = params[0];

                return (List<Recommendation>)call.execute().body();
//                call.enqueue(new Callback<List<Recommendation>>() {
//                    @Override
//                    public void onResponse(Call<List<Recommendation>> call, Response<List<Recommendation>> response) {
//                        for (Recommendation r: response.body()) {
//                            System.out.println(r.getId());
//                            System.out.println(r.getName());
//                            System.out.println(r.getDescription());
//                            System.out.println(r.getPrice());
//                            System.out.println(r.getImages());
//
//                   /* Bitmap bitmap = getImageBitmap( BASE_URL + r.getImages() );
//                    ((ImageView) findViewById( R.id.cam_prev )).setImageBitmap( bitmap );*/
//                        }
//
//                        return response.body();
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<Recommendation>> call, Throwable t) {
//                        System.out.println("onFailure");
//                    }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Recommendation> result) {
            Intent intent = new Intent( MainActivity.this, RekomendasiActivity.class );
            intent.putExtra( "recommendations", new ArrayList<>(result) );
            startActivity( intent );
        }
    }

    @Override
    public void onImageCapture(@NonNull File imageFile) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile( imageFile.getAbsolutePath(), options );
//        FaceCropper mFaceCropper = new FaceCropper();
//        bitmap = mFaceCropper.getCroppedImage(bitmap);
        bitmap = Bitmap.createScaledBitmap(bitmap, 255, 257, false);

//        File file = new File(path, "FitnessGirl"+counter+".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
        OutputStream fOut = null;
        try {
            fOut = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashMap<String, RequestBody> map = new HashMap<>();
//        map.put("latitude", createPartFromString(location.getLatitude() + ""));
//        map.put("longitude", createPartFromString(location.getLongitude() + ""));
//        map.put("altitude", createPartFromString(location.getAltitude() + ""));

        RequestBody reqFile = RequestBody.create( MediaType.parse("image/jpeg"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", imageFile.getName(), reqFile);

        Call<List<Recommendation>> call = service.uploadImage(body, map);

        new NetworkCall().execute( call );

        //Display the image to the image view
//        ((ImageView) findViewById( R.id.cam_prev )).setImageBitmap( bitmap );
    }

    @Override
    public void onCameraError(@CameraError.CameraErrorCodes int errorCode) {
        switch (errorCode) {
            case CameraError.ERROR_CAMERA_OPEN_FAILED:
                //Camera open failed. Probably because another application
                //is using the camera
                Toast.makeText( this, R.string.error_cannot_open, Toast.LENGTH_LONG ).show();
                break;
            case CameraError.ERROR_IMAGE_WRITE_FAILED:
                //Image write failed. Please check if you have provided WRITE_EXTERNAL_STORAGE permission
                Toast.makeText( this, R.string.error_cannot_write, Toast.LENGTH_LONG ).show();
                break;
            case CameraError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE:
                //camera permission is not availab      le
                //Ask for the camera permission before initializing it.
                Toast.makeText( this, R.string.error_cannot_get_permission, Toast.LENGTH_LONG ).show();
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION:
                //Display information dialog to the user with steps to grant "Draw over other app"
                //permission for the app.
                HiddenCameraUtils.openDrawOverPermissionSetting( this );
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA:
                Toast.makeText( this, R.string.error_not_having_camera, Toast.LENGTH_LONG ).show();
                break;
        }
    }
}
