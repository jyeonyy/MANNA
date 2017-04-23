package org.ssutown.manna;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Jiyeon on 2017-04-16.
 */

public class MemoListItem {

    private String memo;

    public MemoListItem() {
    }

    public MemoListItem(String content) {
        this.memo = content;
    }

    public void setMemo(String memo) {
        this.memo = memo;

    }

    public String getMemo() {
        return this.memo;
    }
}
