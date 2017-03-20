package com.example.moodmobile;

import android.app.IntentService;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by zindi on 3/16/17.
 */

public class SyncService extends IntentService {
    private ArrayList<Mood> saveList;
    private ArrayList<Mood> updateList;
    private ArrayList<Mood> deleteList;
    private static final String SAVE_FILE = "savemood.sav";
    private static final String UPDATE_FILE = "updatemood.sav";
    private static final String DELETE_FILE = "deletemood.sav";

    public SyncService() {
        super("SyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        //Files
        //TODO How does updating moods work? Just update or delete + save?


        Bundle bundle = intent.getExtras();
        LoadAllFromFile();


        //getmethod
        Context context = this;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected == true){
            //TODO Post Moods

            SaveAllToFile();
        }
        else{
            //TODO Save Moods
            SaveAllToFile();
        }

        Log.i("SyncService", "Completed service @ " + SystemClock.elapsedRealtime());
        NetworkReceiver.completeWakefulIntent(intent);
    }

    private void LoadFromFile(String file, ArrayList<Mood> list){
        try {
            FileInputStream fis = openFileInput(file);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Mood>>(){}.getType();
            list = gson.fromJson(in, listType);
        } catch (FileNotFoundException e) {
            list = new ArrayList<Mood>();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void LoadAllFromFile(){
        LoadFromFile(SAVE_FILE, saveList);
        LoadFromFile(UPDATE_FILE, updateList);
        LoadFromFile(DELETE_FILE, deleteList);
    }

    private void SaveToFile(String file, ArrayList<Mood> list){
        try {
            FileOutputStream fos = openFileOutput(file, 0);
            OutputStreamWriter witer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(list, witer);
            witer.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void SaveAllToFile(){
        SaveToFile(SAVE_FILE, saveList);
        SaveToFile(UPDATE_FILE, updateList);
        SaveToFile(DELETE_FILE, deleteList);
    }
}
