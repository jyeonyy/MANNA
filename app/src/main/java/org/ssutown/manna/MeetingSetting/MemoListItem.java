package org.ssutown.manna.MeetingSetting;

/**
 * Created by Jiyeon on 2017-04-16.
 */

public class MemoListItem {

    private String userID;

    public MemoListItem() {
    }

    public MemoListItem(String content) {
        this.userID = content;
    }

    public void setMemo(String memo) {
        this.userID = memo;
    }

    public String getUserID(){ return this.userID;}

}
