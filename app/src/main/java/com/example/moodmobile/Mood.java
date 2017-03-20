package com.example.moodmobile;

import android.location.Location;
import android.media.Image;

import java.util.Date;

import io.searchbox.annotations.JestId;

/**
 * Created by Repka on 2017-03-07.
   Modified by Jia on 2017-03-12.
 */

public class Mood implements Moodable{
    private String message;
    private Date date;
    private String feeling;
    private String socialSituation;
    private String moodImage;
//    private Location location;


    @JestId
    private String id;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Mood(String feeling){
        this.message = null;
        this.feeling = feeling;
//        this.location = null;
//        this.moodImage = null;
        this.socialSituation = null;
        this.date = new Date();
    }

    public Mood(String feeling, String moodMessage
            , Location location, Image moodImage, String socialSituation) {
        this.message = moodMessage;
        this.feeling = feeling;
        this.date = new Date();
  //      this.location = location;
    //    this.moodImage = moodImage;
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

   public String getMoodImage() {
        return moodImage;
    }
    public void setMoodImage(String moodImage) {
        this.moodImage = moodImage;
    }
    /*
    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }*/
}