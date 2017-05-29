package org.ssutown.manna.Meeting_details;

/**
 * Created by Jiyeon on 2017-05-23.
 */

public class AnnounceListItem {
    private long userID;
    private String content;

    AnnounceListItem(){};

    public AnnounceListItem(long u, String c){
        this.userID = u;
        this.content = c;
    }

    public String getContent() {
        return content;
    }

    public long getUserID() {
        return userID;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void setAnnouncement(long a, String b){
        this.userID = a;
        this.content = b;
    }

}
