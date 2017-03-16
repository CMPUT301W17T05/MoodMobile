package com.example.moodmobile;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

/**
 * Created by zindi on 3/16/17.
 */

public class SyncJob extends JobService {
    @Override
    public boolean onStartJob(JobParameters params){
        Intent intent = new Intent(this, SyncService.class);
        intent.putExtra("JobParams", params);
        this.startService(intent);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params){


        return true;
    }
}
