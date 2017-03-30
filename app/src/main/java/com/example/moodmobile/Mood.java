package com.example.moodmobile;

import android.location.Location;
import android.media.Image;

import org.osmdroid.util.GeoPoint;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import io.searchbox.annotations.JestId;

/**
 * Created by Repka on 2017-03-07.
   Modified by Jia on 2017-03-12.
   Modified by Repka on 2017-03-21.
   Modified by Haozhou on 2017-03-27.

 */

public class Mood implements Moodable{
    private String message;
    private Date date;
    private String feeling;
    private String socialSituation;
    private String moodImage;
    private Double latitude;
    private Double longitude;


    private GeoPoint geoPoint;
    private Location location;
    private String username;


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
        this.location = null;
        this.moodImage = null;
        this.socialSituation = null;
        this.date = new Date();
        this.username = null;
    }

    public Mood(String feeling, String moodMessage
            , Location location, String moodImage, String socialSituation, String userName) {
        this.message = moodMessage;
        this.feeling = feeling;
        this.date = new Date();
        this.location = location;
        this.moodImage = moodImage;
        this.socialSituation = socialSituation;
        this.username = userName;
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

    public Location getLocation() {return location;}
    public void setLocation(Location location) {this.location = location;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }
}