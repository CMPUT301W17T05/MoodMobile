package com.example.moodmobile;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;

/**
 * This class is controlled By the system and runs when a job is scheduled to run.
 * It adds the Mood to the server.
 * Created by shingai on 02/04/17.
 */

public class AddJobService extends SyncJobService {
    private Mood mood;
    private String moodID;

    /**
     * Logs the time the Job was created.
     */
    @Override
    public void onCreate(){
        super.onCreate();
        Log.i("AddJobService", "Created At:" + SystemClock.elapsedRealtime());
    }

    /**
     * Logs when the Job is destroyed.
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("AddJobService", "Destroyed at:" + SystemClock.elapsedRealtime());
    }

    /**
     * The Mood passed is added to the server.
     * Mood is passed as json and converted to Mood type.
     * @param job Contains the Mood that needs to be added as a Json String.
     * @return False if job has completed.
     * @return True if job is still running in background.
     */
    @Override
    public boolean onStartJob(JobParameters job){
        Log.i("AddJobService", "Started at:" + SystemClock.elapsedRealtime());
        String json = job.getExtras().getString("mood");
        Gson gson = new Gson();
        mood = gson.fromJson(json, Mood.class);
        moodID = mood.getId();

        ElasticsearchMoodController.AddMoodsTask addMoodTask =
                new ElasticsearchMoodController.AddMoodsTask();
        addMoodTask.execute(mood);

        Log.i("AddJobService", "Finished at:" + SystemClock.elapsedRealtime());
        return false;
    }

    /**
     * This method ends the Job and restarts it if necessary.
     * @param job Contains the information passed into the Job. To be used for rescheduling.
     * @return true if the job is to be rescheduled
     * @return false if the job doesn't need to be rescheduled.
     */
    @Override
    public boolean onStopJob(JobParameters job){
        Log.i("AddJobService", "Stopped at:" + SystemClock.elapsedRealtime());
        return true;
    }

    public boolean checkByID(String moodID){
        return super.checkById(moodID);
    }
}
