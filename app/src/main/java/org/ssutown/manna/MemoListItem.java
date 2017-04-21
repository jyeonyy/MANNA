package org.ssutown.manna;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Jiyeon on 2017-04-16.
 */

public class MemoListItem {
    private String memo;
    public MemoListItem(){}
    public MemoListItem(String content){
        this.memo = content;
    }
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("memo");
    public void setMemo(String newmemo){
        this.memo = newmemo;
    }

    public String getMemo(){
        return this.memo;
    }
    public void writeNewMemo(String content){
        MemoListItem newmemo = new MemoListItem(content);
        mDatabase.child(memo);
    }



}
