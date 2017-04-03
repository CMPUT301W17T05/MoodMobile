package com.example.moodmobile;

/**
 * This class packages a mood with a task.
 * This information will be used by the SyncService to appropriately sync with the server.
 * Created by zindi on 3/23/17.
 */

public class SyncMood {
    private Mood syncMood;
    private int syncTask;

    /**
     * Creates the SyncMood from the given mood and task.
     * @param mood a Mood to be synced
     * @param task corresponds to the type of update to the server (Add, Update, Delete).
     */
    public SyncMood(Mood mood, int task){
        syncMood = mood;
        syncTask = task;
    }

    /**
     * Changes the SyncMood's Mood.
     * @param mood
     */
    public void setSyncMood(Mood mood){
        syncMood = mood;
    }

    /**
     * Changes the SyncMood's Task.
     * @param task
     */
    public void setSyncTask(int task){
        syncTask = task;
    }

    /**
     * @return syncMood. The Mood of the SyncMood.
     */
    public Mood getSyncMood(){
        return syncMood;
    }

    /**
     * @return syncTask. The Integer task of the SyncMood.
     */
    public int getSyncTask(){
        return syncTask;
    }
}
