package com.example.moodmobile;

import android.app.job.JobParameters;
import android.app.job.JobService;

/**
 * Created by shingai on 02/04/17.
 */

public abstract class SyncJobService extends JobService {
    private static final String TAG = SyncJobService.class.getSimpleName();

    @Override
    public void onCreate(){
        super.onCreate();
    }
    
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    
    @Override
    public boolean onStartJob(final JobParameters params) {
        Log.i("JobService", "Started at:" + SystemClock.elapsedRealtime());
        String json = job.getExtras().getString("mood");
        Gson gson = new Gson();
        Mood mood = gson.fromJson(json, Mood.class);
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i("JobService", "Stopped at:" + SystemClock.elapsedRealtime());

    }
}
