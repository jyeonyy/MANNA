package org.ssutown.manna;

/**
 * Created by Jiyeon on 2017-04-16.
 */

public class MemoListItem {

    private String memo;
    private String uniquekey;

    public MemoListItem() {
    }

    public MemoListItem(String content, String unique) {
        this.memo = content;
        this.uniquekey = unique;
    }

    public void setMemo(String memo, String uniquekey) {
        this.memo = memo;
        this.uniquekey = uniquekey;


    }
    public void setUniquekey(String unique){
        this.uniquekey = unique;
    }
    public String getUniquekey(){
        return this.uniquekey;
    }

    public String getMemo() {
        return this.memo;
    }
}
