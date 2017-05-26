package org.ssutown.manna.meeting;

/**
 * Created by HyeMin on 2017. 5. 23..
 */

public class meetingList {

    private String  meetingID;

    public meetingList(){}

    public meetingList(String meetingID) {
        this.meetingID = meetingID;
    }

    public String getMeetingID(){
        return meetingID;
    }
}
