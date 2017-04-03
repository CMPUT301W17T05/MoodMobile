package com.example.moodmobile;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by shingai on 02/04/17.
 */

public class DeleteJobService extends JobService {
    @Override
    public void onCreate(){
        super.onCreate();
        Log.i("DeleteJobService", "Created At:" + SystemClock.elapsedRealtime());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("DeleteJobService", "Destroyed at:" + SystemClock.elapsedRealtime());
    }

    @Override
    public boolean onStartJob(JobParameters job){
        Log.i("DeleteJobService", "Started at:" + SystemClock.elapsedRealtime());
        String json = job.getExtras().getString("mood");
        Gson gson = new Gson();
        Mood mood = gson.fromJson(json, Mood.class);

        ElasticsearchMoodController.DeleteMoodsTask deleteMoodsTask =
                new ElasticsearchMoodController.DeleteMoodsTask();
        deleteMoodsTask.execute(mood);

        Log.i("DeleteJobservice", "Finished at:" + SystemClock.elapsedRealtime());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job){
        Log.i("DeleteJobService", "Stopped at:" + SystemClock.elapsedRealtime());
        return true;
    }
}
