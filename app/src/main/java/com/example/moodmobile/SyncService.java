package com.example.moodmobile;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
 * TODO Default File Template
 * Created by zindi on 3/16/17.
 */

public class SyncService extends IntentService {
    private ArrayList<SyncMood> syncList;
    private static final String SYNC_FILE = "syncmood.sav";
    private Intent serviceIntent;

    public SyncService() {
        super("SyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        serviceIntent = intent;

        LoadFromFile();

        SyncWithServer();

        EndService(true);
    }

    private void LoadFromFile(){
        try {
            FileInputStream fis = openFileInput(SYNC_FILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<SyncMood>>(){}.getType();
            syncList = gson.fromJson(in, listType);
        } catch (FileNotFoundException e) {
            syncList = new ArrayList<>();
        }
    }

    private void SaveToFile(){
        try {
            FileOutputStream fos = openFileOutput(SYNC_FILE, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(syncList, writer);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }


    private void AddToServer(Mood currentMood){
        ElasticsearchMoodController.AddMoodsTask addMoodTask =
                new ElasticsearchMoodController.AddMoodsTask();
        if (IsConnected()) {
            addMoodTask.execute(currentMood);
        } else {
            EndService(false);
        }
    }

    private void UpdateOnServer(Mood currentMood){
        ElasticsearchMoodController.UpdateMoodsTask updateMoodsTask =
                new ElasticsearchMoodController.UpdateMoodsTask();
        if (IsConnected()) {
            updateMoodsTask.execute(currentMood);
        } else {
            EndService(false);
        }
    }

    private void DeleteFromServer(Mood currentMood){
        ElasticsearchMoodController.DeleteMoodsTask deleteMoodsTask =
                new ElasticsearchMoodController.DeleteMoodsTask();
        if (IsConnected()) {
            deleteMoodsTask.execute(currentMood);
        } else {
            EndService(false);
        }
    }

    private void SyncWithServer(){
        while (syncList.size() != 0){
            SyncMood currentSyncMood = syncList.get(0);
            if (currentSyncMood.getSyncTask() == 1){
                AddToServer(currentSyncMood.getSyncMood());
            }else if (currentSyncMood.getSyncTask() == 2){
                UpdateOnServer(currentSyncMood.getSyncMood());
            } else if (currentSyncMood.getSyncTask() == 3) {
                DeleteFromServer(currentSyncMood.getSyncMood());
            }
            syncList.remove(0);
        }
    }

    private boolean IsConnected(){
        Context context = this;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void EndService(Boolean isFinished){
        if (isFinished){
            Log.i("SyncService", "Completed service @ " + SystemClock.elapsedRealtime());
            NetworkReceiver.completeWakefulIntent(serviceIntent);
        }
        else{
            SaveToFile();
            Log.i("SyncService", "Service Disrupted @ " + SystemClock.elapsedRealtime());
            NetworkReceiver.completeWakefulIntent(serviceIntent);
        }
    }
}
