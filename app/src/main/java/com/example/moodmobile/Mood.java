package com.example.moodmobile;

import java.util.Date;

/**
 * Created by Derek.R on 2017-03-07.
 */

public class Mood implements Moodable{
    private String message;
    private Date date;
    private String feeling;
    private String situation;


    public Mood(String message){
        this.message = message;

        this.date = new Date();
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
        this.situation = situation;
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
        return situation;
    }
}

