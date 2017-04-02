package com.example.moodmobile;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.Date;

/**
 * This class will view the properties of, and
 * allow editing of the properties of a given mood.
 *
 * Ths mood is sent to this activity via an intent from the parent activity.
 */
public class ViewEditMood extends AppCompatActivity {

    private static final String SYNC_FILE = "syncmood.sav";
    private EditText moodEdittext;
    private EditText moodSituationEdittext;
    private EditText moodReasonEdittext;

    private Mood mood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_edit_mood);

        ArrayList<Mood> moodList = new ArrayList<>();

        Button saveButton = (Button) findViewById(R.id.moodSaveButton);
        moodEdittext = (EditText) findViewById(R.id.moodEdittext);
        moodSituationEdittext = (EditText) findViewById(R.id.moodSituationEdittext);
        moodReasonEdittext = (EditText) findViewById(R.id.moodReasonEdittext);
        //saveButton = (Button) findViewById(R.id.moodSaveButton);

        String moodID = getIntent().getStringExtra("moodID");

        ElasticsearchMoodController.GetMoodsTaskByID getMoodsTask = new ElasticsearchMoodController.GetMoodsTaskByID();
        getMoodsTask.execute(moodID);

        try {
            moodList.addAll(getMoodsTask.get());
            mood = moodList.get(0);
        } catch (Exception e) {
            Log.i("Error", "Failed to get the moods out of the async object");
        }

        /** Will throw error if moodlist is empty,
         * but non-emptyness should be guaranteed.
         */


        moodEdittext.setText(mood.getMessage());
        moodSituationEdittext.setText(mood.getSituation());
        moodReasonEdittext.setText(mood.getFeeling());

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                if (!moodEdittext.getText().toString().trim().equals("")){

                    ElasticsearchMoodController.UpdateMoodsTask updateMoodTask =
                            new ElasticsearchMoodController.UpdateMoodsTask();
                    //mood = new Mood("qwerty"); //This is why it wasnt working

                    /** Setting new values **/
                    try{

                    mood.setMessage(moodEdittext.getText().toString());
                    mood.setSituation(moodSituationEdittext.getText().toString());
                    mood.setFeeling(moodReasonEdittext.getText().toString());
                    mood.setDate(new Date());}
                    catch (ReasonTooLongException e) {

                        Context context = getApplicationContext();

                        CharSequence text = "Reason must be less than 20 characters";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }




                    /** save data to server
                     *
                     *
                     *
                     * **/
                    //setResult(RESULT_OK, intent);
                    if (IsConnected()){
                        updateMoodTask.execute(mood);
                    } else {
                        SaveToFile(mood);
                    }
                    finish();

                } else {
                    Context context = getApplicationContext();

                    CharSequence text = "You cannot leave the Mood field blank";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }

        });

    }

    private boolean IsConnected(){
        Context context = this;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void SaveToFile(Mood mood){
        SyncMood syncMood = new SyncMood(mood, 2);
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
