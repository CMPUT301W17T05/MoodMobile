package com.example.moodmobile;

/**
 * Created by zindi on 3/23/17.
 */

public class SyncMood {
    private Mood syncMood;
    private int syncTask;

    public SyncMood(Mood mood, int task){
        syncMood = mood;
        syncTask = task;
    }

    public void setSyncMood(Mood mood){
        syncMood = mood;
    }

    public void setSyncTask(int task){
        syncTask = task;
    }

    public Mood getSyncMood(){
        return syncMood;
    }

    public int getSyncTask(){
        return syncTask;
    }
}
