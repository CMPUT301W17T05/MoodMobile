package com.example.moodmobile;

import android.app.job.JobParameters;
import android.app.job.JobService;

/**
 * Created by shingai on 02/04/17.
 */

public class SyncJobService extends JobService {
    private static final String TAG = SyncJobService.class.getSimpleName();

    @Override
    public boolean onStartJob(final JobParameters params) {

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        return false;
    }
}
