package com.example.moodmobile;

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
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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

public class AddMood extends AppCompatActivity implements LocationListener {

    private Intent getUsernameIntent;
    private String username;
    private static final int IMG_REQUEST = 21;
    private EditText reasonText;
    private Spinner moodSpinner;
    private Spinner ssSpinner;
    private CheckBox locationCheckBox;
    private String Feeling;
    private String socialSituation;
    private String reason;
    private GeoPoint geoPoint;
    private Location location;
    private double latitude; // Latitude
    private double longitude; // Longitude
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60;
    private LocationManager locationManager;
    private  String encodeImage;
    ImageButton ivCamera;
    private static final String SYNC_FILE = "syncmood.sav";


    protected static final String TAG = "AddMood";

    final int CAMERA_REQUEST = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mood);
        final Mood currentMood = new Mood(null);
        getUsernameIntent = getIntent();

        reasonText = (EditText) findViewById(R.id.reason);
        Button publishButton = (Button) findViewById(R.id.publish);
        ImageButton addImageButton = (ImageButton) findViewById(R.id.ivGallery);
        moodSpinner = (Spinner) findViewById(R.id.moodSpinner);
        ssSpinner = (Spinner) findViewById(R.id.ssSpinner);
        locationCheckBox = (CheckBox) findViewById(R.id.checkBox);
        ivCamera = (ImageButton) findViewById(R.id.ivCamera);

        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, this);
        //location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        //longitude = location.getLongitude();
        //latitude  = location.getLatitude();
        //Log.d(TAG,"Location longitude:"+ longitude +" latitude: "+ latitude );

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


        locationCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationCheckBox.isChecked()){
                    //Checked test
                    Context context = getApplicationContext();
                    CharSequence text = "Checked";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else{
                    //Unchecked test
                    Context context = getApplicationContext();
                    CharSequence text = "Unchecked";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });



        //Check the location box
        if (locationCheckBox.isChecked()) {
            Context context = getApplicationContext();
            CharSequence text = "Checked";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }


        publishButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                ElasticsearchMoodController.AddMoodsTask addMoodTask =
                        new ElasticsearchMoodController.AddMoodsTask();

                Feeling = moodSpinner.getSelectedItem().toString();
                currentMood.setUsername(username);
                currentMood.setFeeling(Feeling);

                socialSituation = ssSpinner.getSelectedItem().toString();

                //This is for checking the value of CurrentMood and socialSituation

                Context context = getApplicationContext();
                CharSequence text = "Selected Mood: "+Feeling+"\nSocialSituation: "+socialSituation;
                int duration = Toast.LENGTH_LONG;

                currentMood.setMoodImage(encodeImage);

                reason = reasonText.getText().toString();

                setResult(RESULT_OK);
                try {currentMood.setMessage(reason);
                } catch (ReasonTooLongException e) {

                    CharSequence text2 = "Reason is too long.";
                    int duration2 = Toast.LENGTH_SHORT;
                    Toast toast2 = Toast.makeText(context, text2, duration2);
                    toast2.show();
                }
                currentMood.setSituation(socialSituation);


                currentMood.setUsername(getIntent().getStringExtra("username"));

                // Set the location if box is checked.
                if(locationCheckBox.isChecked()){
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if(ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    geoPoint = new GeoPoint(latitude, longitude);

                    currentMood.setLocation(geoPoint);

                }

                if (IsConnected()) {
                    addMoodTask.execute(currentMood);
                } else {
                    SaveToFile(currentMood);
                }

                Toast toast = Toast.makeText(context, "Mood Created!", Toast.LENGTH_LONG);
                toast.show();

                finish();


            }

        });


    }

    //When click the add Image button
    public void addImage(View v){
        Intent intent = new Intent(Intent.ACTION_PICK);
        File pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String path = pictureDir.getPath();
        Uri data = Uri.parse(path);
        intent.setDataAndType(data, "image/*");
        startActivityForResult(intent, IMG_REQUEST);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){

        if (resultCode == RESULT_OK){
            if (requestCode == IMG_REQUEST){
                Uri imageUri = data.getData();

                InputStream inputStream;

                try{
                    inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Bitmap resized = Bitmap.createScaledBitmap(bitmap, 320, 240,true);
                    ImageView moodImage = (ImageView) findViewById(R.id.moodImage);
                    moodImage.setImageBitmap(resized);
                    encodeImage = getEncoded64ImageStringFromBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
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

    public void takeAPhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, CAMERA_REQUEST);
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP);
    }

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude  = location.getLatitude();
        Log.d(TAG,"Location longitude:"+ longitude +" latitude: "+ latitude );
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

    private boolean IsConnected(){
        Context context = this;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        try {
            //TODO get server address
            InetAddress inetAddress = InetAddress.getByName("google.com");
            return !inetAddress.equals("");
        } catch (Exception e) {
            return false;
        }
    }

    private void SaveToFile(Mood mood){
        SyncMood syncMood = new SyncMood(mood, 1);
        ArrayList<SyncMood> syncList;

        try {
            FileInputStream fis = openFileInput(SYNC_FILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<SyncMood>>(){}.getType();
            syncList = gson.fromJson(in, listType);

            syncList.add(syncMood);

            FileOutputStream fos = openFileOutput(SYNC_FILE, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            gson = new Gson();
            gson.toJson(syncList, writer);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}