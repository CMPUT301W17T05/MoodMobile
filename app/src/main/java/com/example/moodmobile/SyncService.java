package com.example.moodmobile;

import android.app.IntentService;
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
    private ArrayList<Mood> addList;
    private ArrayList<Mood> updateList;
    private ArrayList<Mood> deleteList;
    private static final String ADD_FILE = "addmood.sav";
    private static final String UPDATE_FILE = "updatemood.sav";
    private static final String DELETE_FILE = "deletemood.sav";
    private Intent serviceIntent;

    public SyncService() {
        super("SyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        serviceIntent = intent;

        LoadAllFromFile();

        SyncWithServer();

        EndService(true);
    }

    private ArrayList<Mood> LoadFromFile(String file){
        try {
            FileInputStream fis = openFileInput(file);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Mood>>(){}.getType();
            return gson.fromJson(in, listType);
        } catch (FileNotFoundException e) {
            return new ArrayList<Mood>();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void LoadAllFromFile(){
        addList = LoadFromFile(ADD_FILE);
        updateList =  LoadFromFile(UPDATE_FILE);
        deleteList = LoadFromFile(DELETE_FILE);
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
        SaveToFile(ADD_FILE, addList);
        SaveToFile(UPDATE_FILE, updateList);
        SaveToFile(DELETE_FILE, deleteList);
    }

    private void AddToServer(){
        ElasticsearchMoodController.AddMoodsTask addMoodTask =
                new ElasticsearchMoodController.AddMoodsTask();
        while (addList.size() != 0) {
            if (IsConnected() == true) {
                Mood currentMood = addList.get(0);
                addMoodTask.execute(currentMood);
                addList.remove(0);
            } else {
                EndService(false);
            }
        }
    }

    private void UpdateOnServer(){
        ElasticsearchMoodController.UpdateMoodsTask updateMoodsTask =
                new ElasticsearchMoodController.UpdateMoodsTask();
        while (updateList.size() != 0){
            if (IsConnected() == true) {
                Mood currentMood = updateList.get(0);
                updateMoodsTask.execute(currentMood);
                updateList.remove(0);
            } else {
                EndService(false);
            }
        }
    }

    private void DeleteFromServer(){
        ElasticsearchMoodController.DeleteMoodsTask deleteMoodsTask =
                new ElasticsearchMoodController.DeleteMoodsTask();
        while (deleteList.size() != 0){
            if (IsConnected() == true) {
                Mood currentMood = deleteList.get(0);
                deleteMoodsTask.execute(currentMood);
                deleteList.remove(0);
            } else {
                EndService(false);
            }
        }
    }

    private void SyncWithServer(){
        DeleteFromServer();
        AddToServer();
        UpdateOnServer();
    }

    private boolean IsConnected(){
        Context context = this;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private void EndService(Boolean isFinished){
        if (isFinished == true){
            Log.i("SyncService", "Completed service @ " + SystemClock.elapsedRealtime());
            NetworkReceiver.completeWakefulIntent(serviceIntent);
        }
        else{
            SaveAllToFile();
            Log.i("SyncService", "Service Disrupted @ " + SystemClock.elapsedRealtime());
            NetworkReceiver.completeWakefulIntent(serviceIntent);
        }
    }
}
