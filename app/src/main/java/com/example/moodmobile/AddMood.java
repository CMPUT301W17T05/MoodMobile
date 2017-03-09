package com.example.moodmobile;

import android.content.Context;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class AddMood extends AppCompatActivity {

    private EditText reasonText;
    private Button publishButton;
    private Button addImageButton;
    private Spinner moodSpinner;
    private Spinner ssSpinner;
    private CheckBox locationCheckBox;
    private String CurrentMood;
    private String socialSituation;
    private String reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mood);
        reasonText = (EditText) findViewById(R.id.reason);
        publishButton = (Button) findViewById(R.id.publish);
        addImageButton = (Button) findViewById (R.id.addImage);
        moodSpinner = (Spinner) findViewById(R.id.moodSpinner);
        ssSpinner = (Spinner) findViewById(R.id.ssSpinner);
        locationCheckBox = (CheckBox) findViewById(R.id.checkBox);







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

        //When click the add Image button
        addImageButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

            }

        });







        publishButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                CurrentMood = moodSpinner.getSelectedItem().toString();
                socialSituation = ssSpinner.getSelectedItem().toString();

                /* This is for checking the value of CurrentMood and socialSituation

                Context context = getApplicationContext();
                CharSequence text = "Selected Mood: "+CurrentMood+"\nSocialSituation: "+socialSituation;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

*/
                reason = reasonText.getText().toString();

            }
        });






    }


    @Override
    protected  void  onStart(){
        super.onStart();
    }


}
