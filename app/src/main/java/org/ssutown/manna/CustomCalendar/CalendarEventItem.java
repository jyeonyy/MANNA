package org.ssutown.manna.CustomCalendar;


/**
 * Created by Jiyeon on 2017-04-16.
 */

public class CalendarEventItem {

    private String Title;
    private String about;

    public CalendarEventItem() {
    }

    public CalendarEventItem(String title, String about) {
        this.Title = title;
        this.about = about;
    }
    public void setEvent(String a, String b) {
        this.Title = a;
        this.about = b;
    }

    public String getTitle(){
        return this.Title;
    }

    public String getAbout() {
        return this.about;
    }
}

