package org.ssutown.manna.meeting;

/**
 * Created by HyeMin on 2017. 5. 17..
 */

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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

    Random random = new Random();

    meeting_Info(){}

    meeting_Info(String name,int sY,int sM,int sD,int eY,int eM,int eD){

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

    public String getMeeting_id(){
        return this.meeting_id;
    }

    public String getMeeting_name(){
        return this.meeting_name;
    }

    public String getStartDay(){
        return String.valueOf(startYear)+String.valueOf(startMonth)+String.valueOf(startDay);
    }

    public String getEndDay(){
        return String.valueOf(endYear)+String.valueOf(endMonth)+String.valueOf(endDay);
    }

}
