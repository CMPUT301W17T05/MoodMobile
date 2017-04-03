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
    /**
     * Logs when the Service is created
     */
    @Override
    public void onCreate(){
        super.onCreate();
        Log.i("DeleteJobService", "Created At:" + SystemClock.elapsedRealtime());
    }

    /**
     * Logs when the service is destroyed.
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("DeleteJobService", "Destroyed at:" + SystemClock.elapsedRealtime());
    }

    /**
     * This method Deletes the Mood from the server.
     * @param job contains the Mood that is to be Deleted
     * @return false if the job is finished
     * @return true if the job is still running.
     */
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

    /**
     * Called by the system when the job is interupted. Reschedules the job.
     * @param job contains the information needed to end the service and rescheduling.
     * @return true if the job is to be rescheduled
     * @return false if the job doesn't need to be rescheduled.
     */
    @Override
    public boolean onStopJob(JobParameters job){
        Log.i("DeleteJobService", "Stopped at:" + SystemClock.elapsedRealtime());
        return true;
    }
}
