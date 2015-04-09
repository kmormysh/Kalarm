package com.samples.katy.kalarm.models.pojo;

public class SocialUser {
    private String userName;
    private String userId;

    public SocialUser() {}

    public SocialUser(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
