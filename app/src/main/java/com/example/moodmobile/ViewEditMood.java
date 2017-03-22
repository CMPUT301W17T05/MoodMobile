package com.example.moodmobile;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
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

        ArrayList<Mood> moodList = new ArrayList<>();

        moodEdittext = (EditText) findViewById(R.id.moodEdittext);
        moodSituationEdittext = (EditText) findViewById(R.id.moodSituationEdittext);
        moodReasonEdittext = (EditText) findViewById(R.id.moodReasonEdittext);

        saveButton = (Button) findViewById(R.id.moodSaveButton);

        String moodID = getIntent().getStringExtra("moodID");

        //ElasticsearchMoodController.GetMoodsTaskByID getMoodsTask = new ElasticsearchMoodController.GetMoodsTaskByID();
        //getMoodsTask.execute(moodID);

        ElasticsearchMoodController.GetMoodsTask getMoodsTask = new ElasticsearchMoodController.GetMoodsTask();
        getMoodsTask.execute("");

        try {
            moodList.addAll(getMoodsTask.get());
        } catch (Exception e) {
            Log.i("Error", "Failed to get the moods out of the async object");
        }

        /** Will throw error if moodlist is empty,
         * but non-emptyness should be guaranteed.
         */
        mood = moodList.get(0);

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
                        mood.setDate(new Date());

                    }
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
                    updateMoodTask.execute(mood);
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
