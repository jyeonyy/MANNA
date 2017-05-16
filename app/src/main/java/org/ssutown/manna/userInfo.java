package org.ssutown.manna;

/**
 * Created by HyeMin on 2017. 5. 10..
 */

public class userInfo {

    long userID;

    public userInfo() { }

    public userInfo(long id) {
        this.userID = id;
    }

    public void setuserID(Long userID) {
        this.userID = userID;
    }

    public long getuserID() {
        return userID;
    }



}
