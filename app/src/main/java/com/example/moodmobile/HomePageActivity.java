package com.example.moodmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.login_screen);

        Intent intent = new Intent(HomePageActivity.this, ViewEditMood.class);
        startActivity(intent);

    }
}
