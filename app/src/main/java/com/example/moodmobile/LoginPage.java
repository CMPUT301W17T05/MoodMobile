package com.example.moodmobile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginPage extends AppCompatActivity {
    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        final ArrayList<Account> accountList = new ArrayList<>();

        Button loginButton = (Button) findViewById(R.id.loginBot);
        username = (EditText) findViewById(R.id.username);

        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Account newUser = new Account(username.getText().toString());

                ElasticsearchAccountController.GetUser getUser = new ElasticsearchAccountController.GetUser();
                getUser.execute(username.getText().toString());


                try {
                    accountList.clear();
                    accountList.addAll(getUser.get());
                } catch (Exception e) {
                    Log.i("Error", "Failed to get the tweets out of asyc object");
                }
                //Context context = getApplicationContext();
                //Toast.makeText(context, accountList.get(0).getUsername(), Toast.LENGTH_SHORT).show();
                if (accountList.size() == 0) {

                    Context context = getApplicationContext();
                    Toast.makeText(context, "username does not exist!", Toast.LENGTH_SHORT).show();
                }

                else {
                    Context context = getApplicationContext();
                    Toast.makeText(context, "Log in successfully!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    Intent mainIntent = new Intent(LoginPage.this, MainPageActivity.class);
                    startActivity(mainIntent);

                }


            }
        });
    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

    }

    public void createAccount(View view) {
        Intent intent = new Intent(this, CreateAccount.class);
        startActivity(intent);
    }

}