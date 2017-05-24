package org.ssutown.manna;

/**
 * Created by Jiyeon on 2017-05-23.
 */

public class AnnounceListItem {
    private long userID;
    private String content;
    private String uniqueKey;

    public AnnounceListItem(long u, String c, String uni){
        this.userID = u;
        this.content = c;
        this.uniqueKey= uni;
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
    public String getUniquekey(){
        return uniqueKey;
    }
    public void setUniqueKey(String unique){
        this.uniqueKey = unique;
    }
    public void setAnnouncement(long a, String b, String c){
        this.uniqueKey = c;
        this.userID = a;
        this.content = b;
    }
}
