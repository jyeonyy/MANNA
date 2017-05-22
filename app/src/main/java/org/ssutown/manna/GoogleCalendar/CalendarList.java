package org.ssutown.manna.GoogleCalendar;

/**
 * Created by Jiyeon on 2017-05-22.
 */

public class CalendarList {
    private String eventname;
    private String start;
    private String end;
    private String uniquekey;

    public CalendarList(){

    }
    public CalendarList(String name, String start, String end, String key){
        this.eventname = name;
        this.start = start;
        this.end = end;
        this.uniquekey = key;
    }

    public String getEnd() {
        return end;
    }

    public String getStart() {
        return start;
    }

    public String getEventname() {
        return eventname;
    }
    public String getUniquekey() {
        return uniquekey;
    }
    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public void setStart(String start) {
        this.start = start;
    }



    public void setEnd(String end) {
        this.end = end;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }
}
