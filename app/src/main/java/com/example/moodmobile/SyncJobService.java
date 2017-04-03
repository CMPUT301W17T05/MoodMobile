package com.example.moodmobile;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by shingai on 02/04/17.
 */

public abstract class SyncJobService extends JobService {
    private Mood mood;
    private String moodID;

    public boolean checkById(String moodID){
        ArrayList<Mood> moodsList = new ArrayList<Mood>();
        ElasticsearchMoodController.GetMoodsTaskByID getMoodsTaskByID
                = new ElasticsearchMoodController.GetMoodsTaskByID();
        getMoodsTaskByID.execute(moodID);

        try {
            moodsList.addAll(getMoodsTaskByID.get());
            Mood foundMood = moodsList.get(0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
