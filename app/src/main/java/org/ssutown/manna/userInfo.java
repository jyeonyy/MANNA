package org.ssutown.manna;

import java.util.ArrayList;

/**
 * Created by HyeMin on 2017. 5. 10..
 */

public class userInfo {

    long userID;
    ArrayList<String> meetinglist = new ArrayList<>();

    public userInfo() { }

    public userInfo(long id) {
        this.userID = id;
    }

    public void setuserID(Long userID) {
        this.userID = userID;
    }

    public void addMeetingList(String meetingid){
        meetinglist.add(meetingid);
    }

    public long getuserID() {
        return userID;
    }

}
