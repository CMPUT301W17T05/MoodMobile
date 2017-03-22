package com.example.moodmobile;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Derek.R on 2017-03-22.
 */

public class CustomAdapterHelper {

    private final String[] reasons;
    private final Integer[] emoteids;
    private final String[] usernames;
    private final String[] locations;
    private final Bitmap[] images;
    private Integer position;

    public CustomAdapterHelper(ArrayList<Mood> MoodsList){
        position = 0;
        for (Mood mood : MoodsList){
            reasons[position] = mood.getMessage();

        }
    }
}
