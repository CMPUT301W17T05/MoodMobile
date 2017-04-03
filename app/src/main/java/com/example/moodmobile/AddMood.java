package com.example.moodmobile;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.osmdroid.util.GeoPoint;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * The type Add mood.
 */
public class AddMood extends AppCompatActivity implements LocationListener {

    private Intent getUsernameIntent;
    private String username;
    /**
     * The constant IMG_REQUEST.
     */
    private static final int IMG_REQUEST = 21;
    private static final int MY_PERMISSIONS_REQUEST_FOR_LOCATION = 1;

    private EditText reasonText;
    private Spinner moodSpinner;
    private Spinner ssSpinner;
    private CheckBox locationCheckBox;
    private ImageButton ivCamera;
    private String feeling;
    private String socialSituation;
    private String reason;

    private Mood newMood = new Mood(null);
    private Location mlocation;

    private double latitude; // Latitude
    private double longitude; // Longitude
    /*private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60;*/
    private LocationManager locationManager;
    private String encodeImage;

    private static final String SYNC_FILE = "syncmood.sav";

    /**
     * The constant TAG.
     */
    protected static final String TAG = "AddMood";

    /**
     * The Camera request.
     */
    final int CAMERA_REQUEST = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mood);
        getUsernameIntent = getIntent();

        reasonText = (EditText) findViewById(R.id.reason);
        Button publishButton = (Button) findViewById(R.id.publish);
        ImageButton addImageButton = (ImageButton) findViewById(R.id.ivGallery);
        moodSpinner = (Spinner) findViewById(R.id.moodSpinner);
        ssSpinner = (Spinner) findViewById(R.id.ssSpinner);
        locationCheckBox = (CheckBox) findViewById(R.id.checkBox);
        ivCamera = (ImageButton) findViewById(R.id.ivCamera);

        //Check the permission for Access_Fine_Location and Permission_Granted
        if (ContextCompat.checkSelfPermission(AddMood.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddMood.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                Toast.makeText(AddMood.this, "MoMo need the permission to access your location.!", Toast.LENGTH_SHORT).show();

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(AddMood.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FOR_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, this);
        mlocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        longitude = mlocation.getLongitude();
        latitude  = mlocation.getLatitude();


        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAPhoto();
            }
        });


        // Create an ArrayAdapter using the mood_array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mood_array, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        moodSpinner.setAdapter(adapter);


        // Create another spinner about Social Situation
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.situation_array, android.R.layout.simple_spinner_dropdown_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ssSpinner.setAdapter(adapter2);

        /*
        * Toast if the location box is checked or not checked.
        * */
        locationCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationCheckBox.isChecked()){
                    //Checked test
                    Context context = getApplicationContext();
                    CharSequence text = "Checked";
                    Toast toast = Toast.makeText(context, text,Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    //Unchecked test
                    Context context = getApplicationContext();
                    CharSequence text = "Unchecked";
                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        /*
        * The following is when the publishButton is pushed.
        * T
        * */
        publishButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                ElasticsearchMoodController.AddMoodsTask addMoodTask =
                        new ElasticsearchMoodController.AddMoodsTask();

                feeling = moodSpinner.getSelectedItem().toString();
                newMood.setUsername(username);
                newMood.setFeeling(feeling);

                socialSituation = ssSpinner.getSelectedItem().toString();

                //This is for checking the value of CurrentMood and socialSituation

                Context context = getApplicationContext();

                //Encode the Image
                newMood.setMoodImage(encodeImage);

                reason = reasonText.getText().toString();

                setResult(RESULT_OK);
                try {
                    newMood.setMessage(reason);
                } catch (ReasonTooLongException e) {
                    CharSequence text2 = "Reason is too long.";
                    Toast toast2 = Toast.makeText(context, text2, Toast.LENGTH_LONG);
                    toast2.show();
                }
                newMood.setSituation(socialSituation);
                newMood.setUsername(getIntent().getStringExtra("username"));

                // Set the location if box is checked.
                if(locationCheckBox.isChecked()){
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if(ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        newMood.setLatitude(mlocation.getLatitude());
                        newMood.setLongitude(mlocation.getLongitude());
                        newMood.setLocation(mlocation.getLatitude() + ", " + mlocation.getLongitude());
                    }
                }

                Gson gson = new Gson();
                String json = gson.toJson(newMood);
                PersistableBundle bundle = new PersistableBundle();
                bundle.putString("mood", json);
                int jobid = (int) System.currentTimeMillis();
                JobInfo job = new JobInfo.Builder(jobid, new ComponentName(context, AddJobService.class))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setPersisted(true)
                        .setExtras(bundle)
                        .build();
                JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                scheduler.schedule(job);

                Toast toast = Toast.makeText(context, "Mood Created!", Toast.LENGTH_LONG);
                toast.show();
                finish();


            }

        });


    }

    /**
     * Add image.
     *
     * @param v the v
     */
//When click the add Image button
    public void addImage(View v){
        Intent intent = new Intent(Intent.ACTION_PICK);
        //Add a pictire from local
        File pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String path = pictureDir.getPath();
        Uri data = Uri.parse(path);
        intent.setDataAndType(data, "image/*");
        startActivityForResult(intent, IMG_REQUEST);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            //If the request is to get image from gallery.
            if (requestCode == IMG_REQUEST){
                Uri imageUri = data.getData();
                InputStream inputStream;
                try{
                    inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    //Resize the image to addressed format.
                    Bitmap resized = Bitmap.createScaledBitmap(bitmap, 320, 240,true);

                    //Compress the image.
                    ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
                    resized.compress(Bitmap.CompressFormat.JPEG, 5 , bmpStream);
                    byte[] bitmapdata = bmpStream.toByteArray();
                    resized = BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata .length);

                    ImageView moodImage = (ImageView) findViewById(R.id.moodImage);
                    moodImage.setImageBitmap(resized);
                    encodeImage = getEncoded64ImageStringFromBitmap(resized);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            //If the request is camera request.
            else if (requestCode == CAMERA_REQUEST) {
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 320, 240,true);
                ImageView moodImage = (ImageView) findViewById(R.id.moodImage);
                moodImage.setImageBitmap(resized);
                encodeImage = getEncoded64ImageStringFromBitmap(bitmap);
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        username = getUsernameIntent.getStringExtra("username");
    }

    /**
     * Take a photo.
     */
    public void takeAPhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, CAMERA_REQUEST);
    }

    /**
     * Gets encoded 64 image string from bitmap.
     *
     * @param bitmap the bitmap
     * @return the encoded 64 image string from bitmap
     */
    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP);
    }

    /*
    * When the app detect location changes. It will change the longitude and latitude and also print out a log/
    * */
    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude  = location.getLatitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //Check the permission Result.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FOR_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AddMood.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddMood.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }
}