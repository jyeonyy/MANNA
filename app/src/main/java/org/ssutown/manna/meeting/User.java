package org.ssutown.manna.meeting;

/**
 * Created by HyeMin on 2017. 5. 28..
 */

public class User {
    private String userID;

    public User(){}

    public User(String id){
        userID = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
