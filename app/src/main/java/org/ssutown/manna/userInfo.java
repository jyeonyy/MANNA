package org.ssutown.manna;

import java.util.ArrayList;

/**
 * Created by HyeMin on 2017. 5. 10..
 */

public class userInfo {

    long userID;
    private ArrayList<String> meetinglist = new ArrayList<>();


    public userInfo() {
        meetinglist.add("1");
    }

    public userInfo(long id) {
        this.userID = id;
        meetinglist.add("1");
    }

    public void setuserID(Long userID) {
        this.userID = userID;
    }

    public void addMeetingList(String meetingid){
        meetinglist.add(meetingid);
    }

    public void getMeetingList(ArrayList<String> list){
        for(int i = 0; i<meetinglist.size();i++){
            list.add(meetinglist.get(i));
        }
    }

    public long getuserID() {
        return userID;
    }

}
