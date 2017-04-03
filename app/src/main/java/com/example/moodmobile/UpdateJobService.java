package com.example.moodmobile;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by shingai on 02/04/17.
 */

public class UpdateJobService extends JobService {
    @Override
    public void onCreate(){
        super.onCreate();
        Log.i("UpdateJobService", "Created At:" + SystemClock.elapsedRealtime());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("UpdateJobService", "Destroyed at:" + SystemClock.elapsedRealtime());
    }

    @Override
    public boolean onStartJob(JobParameters job){
        Log.i("UpdateJobService", "Started at:" + SystemClock.elapsedRealtime());
        String json = job.getExtras().getString("mood");
        Gson gson = new Gson();
        Mood mood = gson.fromJson(json, Mood.class);

        ElasticsearchMoodController.UpdateMoodsTask updateMoodTask =
                new ElasticsearchMoodController.UpdateMoodsTask();
        updateMoodTask.execute(mood);

        Log.i("UpdateJobService", "Finished at:" + SystemClock.elapsedRealtime());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job){
        Log.i("UpdateJobService", "Stopped at:" + SystemClock.elapsedRealtime());
        return true;
    }
}
