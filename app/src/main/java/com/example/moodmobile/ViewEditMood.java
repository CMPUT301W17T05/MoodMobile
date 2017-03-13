package com.example.moodmobile;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

/**
 * This class will view the properties of, and
 * allow editing of the properties of a given mood.
 *
 * Ths mood is sent to this activity via an intent from the parent activity.
 */
public class ViewEditMood extends AppCompatActivity {

    private EditText moodEdittext;
    private EditText moodSituationEdittext;
    private EditText moodReasonEdittext;

    private Button saveButton;

    private Mood mood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_edit_mood);

        moodEdittext = (EditText) findViewById(R.id.moodEdittext);
        moodSituationEdittext = (EditText) findViewById(R.id.moodSituationEdittext);
        moodReasonEdittext = (EditText) findViewById(R.id.moodReasonEdittext);

        saveButton = (Button) findViewById(R.id.moodSaveButton);

        //Mood mood = new Mood("Happy"); // Change this to the mood passed by intent
        mood = (Mood) getIntent().getSerializableExtra("moodToEdit");

        moodEdittext.setText(mood.getMessage());
        moodSituationEdittext.setText(mood.getSituation());
        moodReasonEdittext.setText(mood.getFeeling());


        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                if (!moodEdittext.getText().toString().trim().equals("")){

                    ElasticsearchMoodController.AddMoodsTask addMoodTask =
                            new ElasticsearchMoodController.AddMoodsTask();

                    /** Setting new values **/
                    mood.setMessage(moodEdittext.getText().toString());
                    mood.setSituation(moodSituationEdittext.getText().toString());
                    mood.setFeeling(moodReasonEdittext.getText().toString());
                    mood.setDate(new Date());


                    /** save data to server
                     *
                     *
                     *
                     * **/
                    //setResult(RESULT_OK, intent);
                    addMoodTask.execute(currentMood);
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

}
