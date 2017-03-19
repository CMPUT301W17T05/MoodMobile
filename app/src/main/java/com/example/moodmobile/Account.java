package com.example.moodmobile;

import java.util.Date;

import io.searchbox.annotations.JestId;

/**
 * Created by Derek.R on 2017-03-07.
 */

public class Account{
    private String profileImage;
    private String username;
    private String nickname;
    private String gender;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    private String region;

    @JestId
    private String id;

    public Account(String username) {
        this.username = username;
    }



    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}