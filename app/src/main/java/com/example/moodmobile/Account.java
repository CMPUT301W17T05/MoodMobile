package com.example.moodmobile;

import java.util.ArrayList;

import io.searchbox.annotations.JestId;

/**
 * TODO Default File Template
 * Created by Derek.R on 2017-03-07.
 */

public class Account{
    private String profileImage;
    private String username;
    private String nickname;
    private String gender;
    private String region;
    private ArrayList<String> following;
    private ArrayList<String> followRequests;
    private String IMEI;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }



    @JestId
    private String id;

    public Account(String username) {
        this.username = username;
    }

    public Account(){}

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

    public ArrayList<String> getFollowing() {return following;}

    public void setFollowing(ArrayList<String> following) {this.following = following;}

    public ArrayList<String> getFollowRequests() {return followRequests;}

    public void setFollowRequests(ArrayList<String> followRequests) {this.followRequests = followRequests;}


    public String getIMEI() {return IMEI;}

    public void setIMEI(String IMEI) {this.IMEI = IMEI;}

}