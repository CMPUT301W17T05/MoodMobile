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

import java.util.ArrayList;

/**
 * Created by zindi on 3/16/17.
 */

public class SyncService extends IntentService {
    ArrayList<Mood> moodList;
    Mood mood;
    JobScheduler scheduler;

    public SyncService() {
        super("SyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        Bundle bundle = intent.getExtras();
        LoadFromFile();
        if (bundle == null) {
            mood = (Mood) bundle.get("mood");
            moodList.add(mood);
        }

        //getmethod
        Context context = this;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected == true){
            //TODO Post Moods


        }
        else{
            //TODO Save Moods
            SaveToFile();
        }

        Log.i("SyncService", "Completed service @ " + SystemClock.elapsedRealtime());
        //TODO How to Complete Service? Back to Reciever vs Back to JobService
        //jobFinished(mJobParams, false);
        NetworkReceiver.completeWakefulIntent(intent);
    }

    private void LoadFromFile(){
        //TODO Load Saved Moods From File
    }

    private void SaveToFile(){
        //TODO Save Moods to File
    }
}

/*
ComponentName serviceName = new CompenentName(this, SyncService.java);

JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

JobInfo jobInfo = new JobInfo.Builder(jobNumber, serviceName)
    .serRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
    .isPersisted()
    .build();

scheduler.schedule(jobInfo);
 */
