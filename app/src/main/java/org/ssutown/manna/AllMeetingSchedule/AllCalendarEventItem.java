package org.ssutown.manna.AllMeetingSchedule;


/**
 * Created by Jiyeon on 2017-04-16.
 */

public class AllCalendarEventItem {

    private String Title;
    private String about;

    public AllCalendarEventItem() {
    }

    public AllCalendarEventItem(String title, String about) {
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

