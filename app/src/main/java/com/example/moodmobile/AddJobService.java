package com.example.moodmobile;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by shingai on 02/04/17.
 */

public class AddJobService extends JobService {
    @Override
    public void onCreate(){
        super.onCreate();
        Log.i("AddJobService", "Created At:" + SystemClock.elapsedRealtime());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("AddJobService", "Destroyed at:" + SystemClock.elapsedRealtime());
    }

    @Override
    public boolean onStartJob(JobParameters job){
        Log.i("AddJobService", "Started at:" + SystemClock.elapsedRealtime());
        String json = job.getExtras().getString("mood");
        Gson gson = new Gson();
        Mood mood = gson.fromJson(json, Mood.class);

        ElasticsearchMoodController.AddMoodsTask addMoodTask =
                new ElasticsearchMoodController.AddMoodsTask();
        addMoodTask.execute(mood);

        Log.i("AddJobService", "Finished at:" + SystemClock.elapsedRealtime());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job){
        Log.i("AddJobService", "Stopped at:" + SystemClock.elapsedRealtime());
        return true;
    }
}
