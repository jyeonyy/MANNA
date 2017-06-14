package org.ssutown.manna.AdjstmentMeeting;

/**
 * Created by Jiyeon on 2017-06-15.
 */

public class MergeCalendar {
    int count[];
    String date;

    public MergeCalendar(int a[], String date){
        count = a;
        this.date = date;
    }

    public int[] getCount() {
        return count;
    }

    public String getDate() {
        return date;
    }

    public void setCount(int[] count) {
        this.count = count;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
