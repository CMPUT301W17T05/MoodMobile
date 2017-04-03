package com.example.moodmobile;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by shingai on 02/04/17.
 */

public class UpdateJobService extends SyncJobService {
    Mood mood;
    String moodID;

    /**
     * Logs when the service is created.
     */
    @Override
    public void onCreate(){
        super.onCreate();
        Log.i("UpdateJobService", "Created At:" + SystemClock.elapsedRealtime());
    }

    /**
     * Logs when the service is destroyed.
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("UpdateJobService", "Destroyed at:" + SystemClock.elapsedRealtime());
    }

    /**
     * Updates the Mood on the server.
     * @param job contains the mood that is to be updated.
     * @return true if the job is still running.
     * @return false if the job is finished.
     */
    @Override
    public boolean onStartJob(JobParameters job){
        Log.i("UpdateJobService", "Started at:" + SystemClock.elapsedRealtime());
        String json = job.getExtras().getString("mood");
        Gson gson = new Gson();
        mood = gson.fromJson(json, Mood.class);
        moodID = mood.getId();

        if (checkByID(moodID)){
            ElasticsearchMoodController.UpdateMoodsTask updateMoodTask =
                    new ElasticsearchMoodController.UpdateMoodsTask();
            updateMoodTask.execute(mood);
            Log.i("UpdateJobService", "Finished at:" + SystemClock.elapsedRealtime());

        } else {
            Log.i("UpdateJobService", "Mood does not exist.");
            Log.i("UpdateJobService", "Aborted at" + SystemClock.elapsedRealtime());
        }

        return false;
    }

    /**
     * Called by the system if the job is interupted.
     * @param job contains the information needed to stop the job and reschedule it.
     * @return true if the job is to be rescheduled.
     * @return false if the job doesn't need to be rescheduled.
     */
    @Override
    public boolean onStopJob(JobParameters job){
        Log.i("UpdateJobService", "Stopped at:" + SystemClock.elapsedRealtime());
        return true;
    }

    public boolean checkByID(String moodID){
        return super.checkById(moodID);
    }
}
