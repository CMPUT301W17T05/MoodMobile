package com.example.moodmobile;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This class will view the properties of, and
 * allow editing of the properties of a given selectedMood.
 *
 * Ths selectedMood is sent to this activity via an intent from the parent activity.
 */
public class ViewEditMood extends AppCompatActivity {

    private static final String SYNC_FILE = "syncmood.sav";

    private Spinner moodSpinner;
    private Spinner ssSpinner;
    private EditText moodReason;
    private EditText moodDate;
    private EditText moodLocation;

    private Mood selectedMood;
    private Geocoder gcd;
    private List<Address> addresses;
    private Context context = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_edit_mood);

        ArrayList<Mood> moodList = new ArrayList<>();

        Button saveButton = (Button) findViewById(R.id.moodSaveButton);
        moodSpinner = (Spinner) findViewById(R.id.moodSpinner);
        ssSpinner = (Spinner) findViewById(R.id.moodSituationSpinner);
        moodReason= (EditText) findViewById(R.id.moodReasonEdittext);
        moodDate = (EditText) findViewById(R.id.dateEditText);
        moodLocation = (EditText) findViewById(R.id.locationEditText);
        gcd = new Geocoder(this, Locale.getDefault());

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


        String moodID = getIntent().getStringExtra("moodID");

        ElasticsearchMoodController.GetMoodsTaskByID getMoodsTask = new ElasticsearchMoodController.GetMoodsTaskByID();
        getMoodsTask.execute(moodID);

        try {
            moodList.addAll(getMoodsTask.get());
            selectedMood = moodList.get(0);
        } catch (Exception e) {
            Log.i("Error", "Failed to get the moods out of the async object");
        }

        /** Will throw error if moodlist is empty,
         * but non-emptyness should be guaranteed.
         */

        try {
            moodReason.setText(selectedMood.getMessage());
            moodDate.setText(selectedMood.getDate().toString());
            moodSpinner.setSelection(adapter.getPosition(selectedMood.getFeeling()));
            ssSpinner.setSelection(adapter2.getPosition(selectedMood.getSituation()));
        } catch (NullPointerException e){
            //Do Nothing
        }
        try {
            addresses = gcd.getFromLocation(selectedMood.getLatitude(), selectedMood.getLongitude(), 1);
            moodLocation.setText(addresses.get(0).getLocality());
        } catch (Exception e) {
            moodLocation.setText("");
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){


            ElasticsearchMoodController.UpdateMoodsTask updateMoodTask =
                    new ElasticsearchMoodController.UpdateMoodsTask();
            //selectedMood = new Mood("qwerty"); //This is why it wasnt working

            /** Setting new values **/
            try{

                selectedMood.setMessage(moodReason.getText().toString());
                selectedMood.setSituation(ssSpinner.getSelectedItem().toString());
                selectedMood.setFeeling(moodSpinner.getSelectedItem().toString());
                selectedMood.setDate(new Date(moodDate.getText().toString()));
                if (selectedMood.getLocation() != null) {
                    if (addresses.get(0).getLocality() != moodLocation.getText().toString()) {
                        List<Address> addresses = gcd.getFromLocationName(moodLocation.getText().toString(), 1);
                        selectedMood.setLatitude(addresses.get(0).getLatitude());
                        selectedMood.setLongitude(addresses.get(0).getLongitude());
                        selectedMood.setLocation(selectedMood.getLatitude() + ", " + selectedMood.getLongitude());
                    }
                }
            }
            catch (ReasonTooLongException e) {

                Context context = getApplicationContext();

                CharSequence text = "Reason must no more than 20 characters or 3 words. Mood not saved.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                finish();

            } catch (IOException e){
                Context context = getApplicationContext();

                CharSequence text = "Invalid Location. Mood not saved";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                finish();

            } catch (IllegalArgumentException e){
                Context context = getApplicationContext();

                CharSequence text = "Wrong Date Format. Mood not saved.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                finish();
            }




            /** save data to server
             *
             *
             *
             * **/
            //setResult(RESULT_OK, intent);

                Gson gson = new Gson();
                String json = gson.toJson(selectedMood);
                PersistableBundle bundle = new PersistableBundle();
                bundle.putString("mood", json);
                int jobid = (int) System.currentTimeMillis();
                JobInfo job = new JobInfo.Builder(jobid, new ComponentName(context, UpdateJobService.class))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setPersisted(true)
                        .setExtras(bundle)
                        .build();
                JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                scheduler.schedule(job);
            finish();
            }

        });
    }
}
