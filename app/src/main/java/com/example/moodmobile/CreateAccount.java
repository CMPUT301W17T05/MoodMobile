package com.example.moodmobile;

import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateAccount extends AppCompatActivity {
    private EditText newUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        final ArrayList<Account> accountList = new ArrayList<>();

        Button saveAccount = (Button) findViewById(R.id.SaveBot);
        newUserName = (EditText) findViewById(R.id.username);
        saveAccount.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Account newUser = new Account(newUserName.getText().toString());
                newUser.setProfileImage(null);
                newUser.setNickname(null);
                newUser.setGender(null);
                newUser.setRegion(null);
                newUser.setFollowing(new ArrayList<String>());
                newUser.setFollowRequests(new ArrayList<String>());
                String myAndroidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                newUser.setIMEI(myAndroidDeviceId);

                ElasticsearchAccountController.GetUser getUser = new ElasticsearchAccountController.GetUser();
                getUser.execute(newUserName.getText().toString());


                try {
                    accountList.clear();
                    accountList.addAll(getUser.get());
                } catch (Exception e) {
                    Log.i("Error", "Failed to get the tweets out of asyc object");
                }
                //Context context = getApplicationContext();
                //Toast.makeText(context, accountList.get(0).getUsername(), Toast.LENGTH_SHORT).show();
                if (accountList.size() == 0) {
                    ElasticsearchAccountController.AddUser addUser = new ElasticsearchAccountController.AddUser();
                    addUser.execute(newUser);
                    Context context = getApplicationContext();
                    Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }

                else{
                    Context context = getApplicationContext();
                    Toast.makeText(context, "username already exist", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


    }



}

