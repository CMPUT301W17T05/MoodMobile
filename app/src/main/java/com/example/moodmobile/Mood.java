package com.example.moodmobile;

import android.media.Image;

import java.util.Date;

/**
 * Created by Jia on 2017-03-07.
 */

public class Mood implements Moodable{
    private String message;
    private Date date;
    private String feeling;
    private String socialSituation;
    private Image moodImage;
    private String location;


    public Mood(String message){
        this.message = message;
        this.location = null;
        this.moodImage = null;
        this.socialSituation = null;
        this.date = new Date();
    }

    public Mood(String moodMessage, String location, Image moodImage, String socialSituation) {
        this.message = moodMessage;
        this.date = new Date();
        this.location = location;
        this.moodImage = moodImage;
        this.socialSituation = socialSituation;
    }

    @Override
    public String toString(){
        return message;
    }

    public void setMessage(String message) throws ReasonTooLongException {
        if (message.length() > 20){
            //Do Something!
            throw new ReasonTooLongException();
        }
        this.message = message;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public void setSituation(String situation) {
        this.socialSituation = situation;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public String getFeeling() {
        return feeling;
    }

    public String getSituation() {
        return socialSituation;
    }

    public Image getMoodImage() {
        return moodImage;
    }

    public void setMoodImage(Image moodImage) {
        this.moodImage = moodImage;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}