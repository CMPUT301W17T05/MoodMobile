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

    /**
     * Gets profile image.
     *
     * @return the profile image
     */
    public String getProfileImage() {
        return profileImage;
    }

    /**
     * Sets profile image.
     *
     * @param profileImage the profile image
     */
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }



    @JestId
    private String id;

    /**
     * Instantiates a new Account.
     *
     * @param username the username
     */
    public Account(String username) {
        this.username = username;
    }

    /**
     * Instantiates a new Account.
     */
    public Account(){}

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }


    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets nickname.
     *
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets nickname.
     *
     * @param nickname the nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets gender.
     *
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets gender.
     *
     * @param gender the gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets region.
     *
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * Sets region.
     *
     * @param region the region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Gets following.
     *
     * @return the following
     */
    public ArrayList<String> getFollowing() {return following;}

    /**
     * Sets following.
     *
     * @param following the following
     */
    public void setFollowing(ArrayList<String> following) {this.following = following;}

    /**
     * Gets follow requests.
     *
     * @return the follow requests
     */
    public ArrayList<String> getFollowRequests() {return followRequests;}

    /**
     * Sets follow requests.
     *
     * @param followRequests the follow requests
     */
    public void setFollowRequests(ArrayList<String> followRequests) {this.followRequests = followRequests;}


    /**
     * Gets imei.
     *
     * @return the imei
     */
    public String getIMEI() {return IMEI;}

    /**
     * Sets imei.
     *
     * @param IMEI the imei
     */
    public void setIMEI(String IMEI) {this.IMEI = IMEI;}

}