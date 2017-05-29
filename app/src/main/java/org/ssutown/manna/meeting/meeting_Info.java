package org.ssutown.manna.meeting;

/**
 * Created by HyeMin on 2017. 5. 17..
 */

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class meeting_Info implements Serializable{

    private String meeting_name;
    private String meeting_id;

    private int startYear;
    private int startMonth;
    private int startDay;

    private int endYear;
    private int endMonth;
    private int endDay;

    public meeting_Info(){};


    public meeting_Info(String name,String id,int sY,int sM,int sD,int eY,int eM,int eD){

        Random random = new Random();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        Date date = new Date();
        String today = df.format(date);
        String ran = String.valueOf(random.nextInt()%9000+10000);

        meeting_id = today+ran;
        meeting_name = name;
        startYear = sY;
        startMonth = sM;
        startDay = sD;
        endYear = eY;
        endMonth = eM;
        endDay = eD;

    }

    public String getMeeting_id() {
        return meeting_id;
    }

    public String getMeeting_name() {return meeting_name;}

    public int getStartYear() {
        return startYear;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public int getStartDay(){
        return startDay;
    }

    public int getEndYear() {
        return endYear;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public int getEndDay(){
        return endDay;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    public void setMeeting_id(String meeting_id) {
        this.meeting_id = meeting_id;
    }

    public void setMeeting_name(String meeting_name) {
        this.meeting_name = meeting_name;
    }

/*    public String getFullStartDay(){
        return String.valueOf(startYear)+String.valueOf(startMonth)+String.valueOf(startDay);
    }

    public String getFullEndDay(){
        return String.valueOf(endYear)+String.valueOf(endMonth)+String.valueOf(endDay);
    }
*/
}
