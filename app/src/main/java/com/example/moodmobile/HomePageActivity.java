package com.example.moodmobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;

public class HomePageActivity extends AppCompatActivity {

    final Context context = this;
    final ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }
    
    /*
    //Checks if internet connection exists
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if (isConnected == true){
                    //setContentView(R.layout.activity_main1);
                    textView1.setText("Connected");
                }
                else{
                    //setContentView(R.layout.activity_main2);
                    textView1.setText("Disconnected");
    
    
    //taken from http://stackoverflow.com/questions/9570237/android-check-internet-connection
    //check that there is connection to the internet and not just some network
    //ex. home router is working but web connection is down.
    public boolean internetAvaliable(){
        try{
            InetAddress inetAddress = InetAdress.getByName("google.com);
            return !inetAddress.equals("");
        } catch (Exception e){
            return false;
        }
    }    
        
    
    */
}
