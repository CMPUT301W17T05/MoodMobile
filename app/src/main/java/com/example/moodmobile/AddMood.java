package com.example.moodmobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.example.moodmobile.R.id.imageView;

public class AddMood extends AppCompatActivity {

    public static final int IMG_REQUEST = 21;
    private EditText reasonText;
    private Button publishButton;
    private Button addImageButton;
    private Spinner moodSpinner;
    private Spinner ssSpinner;
    private CheckBox locationCheckBox;
    private String Feeling;
    private String socialSituation;
    private Mood currentMood;
    private String reason;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected String mLatitudeLabel;
    protected String mLongitudeLabel;


    protected static final String TAG = "AddMood";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mood);
        final Mood currentMood = new Mood(null);

        reasonText = (EditText) findViewById(R.id.reason);
        publishButton = (Button) findViewById(R.id.publish);
        addImageButton = (Button) findViewById(R.id.addImage);
        moodSpinner = (Spinner) findViewById(R.id.moodSpinner);
        ssSpinner = (Spinner) findViewById(R.id.ssSpinner);
        locationCheckBox = (CheckBox) findViewById(R.id.checkBox);


//        buildGoogleApiClient();
//
//
//        mGoogleApiClient


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
                    //currentMood.setLocation(null);

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
                currentMood.setFeeling(Feeling);

                socialSituation = ssSpinner.getSelectedItem().toString();

                 //This is for checking the value of CurrentMood and socialSituation

                Context context = getApplicationContext();
                CharSequence text = "Selected Mood: "+Feeling+"\nSocialSituation: "+socialSituation;
                int duration = Toast.LENGTH_LONG;
//                Toast toast = Toast.makeText(context, text, duration);
//                toast.show();


                reason = reasonText.getText().toString();

                setResult(RESULT_OK);
                try {currentMood.setMessage(reason);
                } catch (ReasonTooLongException e) {

 //                   Context context = getApplicationContext();
                    CharSequence text2 = "Reason is too long.";
                    int duration2 = Toast.LENGTH_SHORT;
                    Toast toast2 = Toast.makeText(context, text, duration);
                    toast2.show();
                };
                currentMood.setSituation(socialSituation);

                addMoodTask.execute(currentMood);

                Intent MainpageIntent = new Intent(v.getContext(), MainPageActivity.class);
                startActivity(MainpageIntent);
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
                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
//        mGoogleApiClient.connect();


    }

}

//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//    }
//
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
//    }
//
//
//    @Override
//    public void onConnected(Bundle connectionHint) {
//        // Provides a simple way of getting a device's location and is well suited for
//        // applications that do not require a fine-grained location and that do not need location
//        // updates. Gets the best and most recent location currently available, which may be null
//        // in rare cases when a location is not available.
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (mLastLocation != null) {
//
//        } else {
//            CharSequence text = 'no_location_detected';
//            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        Log.i(TAG, "Connection suspended");
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult result) {
//        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
//        // onConnectionFailed.
//        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());

//    }

