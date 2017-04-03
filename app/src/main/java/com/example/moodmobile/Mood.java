
package com.example.moodmobile;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import io.searchbox.annotations.JestId;

/**
 * This is the main class used to implement the moods in the application.
 * The main parameters used for this class are as follows:
 * <ul>
 *     <li>message: Optional reason</li>
 *     <li>date: Date when the mood was posted</li>
 *     <li>feeling: Mandatory emotion</li>
 *     <li>socialSituation: Optional situation</li>
 *     <li>moodImage: Optional image to be attached</li>
 *     <li>username: Name of the user that posted the mood</li>
 *     <li>location: Location stored as string</li>
 *     <li>latitude: Latitude of the mood</li>
 *     <li>longitude: Longitude of the mood</li>
 *     <li>id: ID of the mood returned by ElasticSearch</li>
 * </ul>
 * The location is stored as a string so that it can be stored as a geo_point
 * type object in ElasticSearch to use when searching for nearby moods.
 *
 * @author Repka
 * @version 1.4
 */

public class Mood implements Moodable{
    private String message;
    private Date date;
    private String feeling;
    private String socialSituation;
    private String moodImage;
    private String location;
    private String username;
    private Double latitude;
    private Double longitude;

    @JestId
    private String id;

    /**
     * The constructor for the Mood class. If a just an emotion is provided,
     * then it is set as the mood's emotion and the current date is used.
     * Alternatively, all parameters can be provided to avoid using the setters.
     * @param feeling input message
     */
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
            , String location, String moodImage, String socialSituation, String userName) {
        this.message = moodMessage;
        this.feeling = feeling;
        this.date = new Date();
        this.location = location;
        this.moodImage = moodImage;
        this.socialSituation = socialSituation;
        this.username = userName;
    }

    /**
     * The following setters and getters simply either set the parameter or get
     * the value stored in it.
     * The exception is with the message setter, in which checks the input message
     * to ensure it is either under 20 characters, or contains 3 or less words.
     */
    public void setMessage(String message) throws ReasonTooLongException {
        if (message.length() > 20 || StringUtils.countMatches(message, " ") > 2){
            //Do Something!
            throw new ReasonTooLongException();
        }
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate() {
        return date;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }
    public String getFeeling() {
        return feeling;
    }

    public void setSituation(String situation) {
        this.socialSituation = situation;
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

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

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

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

}