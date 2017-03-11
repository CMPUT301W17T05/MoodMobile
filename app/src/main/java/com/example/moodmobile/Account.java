package com.example.moodmobile;

import java.util.Date;

import io.searchbox.annotations.JestId;

public class Account {
    private Date nickname;
    private String gender;
    private String region;

    @JestId
    private String username;


    public Account(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getNickname() {
        return nickname;
    }

    public void setNickname(Date nickname) {
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