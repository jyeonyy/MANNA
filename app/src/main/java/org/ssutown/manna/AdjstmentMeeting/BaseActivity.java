package org.ssutown.manna.AdjstmentMeeting;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ssutown.manna.AllMeetingSchedule.AllMeetingCalendarFragment;
import org.ssutown.manna.GoogleCalendar.CalendarList;
import org.ssutown.manna.MeetingActivity;
import org.ssutown.manna.Meeting_details.AnnounceListItem;
import org.ssutown.manna.R;
import org.ssutown.manna.meeting.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static org.ssutown.manna.HomeFragment.userID;

/**
 * This is a base activity which contains week view and all the codes necessary to initialize the
 * week view.
 * Created by Raquib-ul-Alam Kanak on 1/3/2014.
 * Website: http://alamkanak.github.io
 */
public abstract class BaseActivity extends AppCompatActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
//    private int mWeekViewType = TYPE_WEEK_VIEW;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private static ArrayList<HashMap<String, String>> mSavedEvents;
    private WeekView mWeekView;
    ArrayList<User> userinfo = MeetingActivity.getUserlist();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userdb = database.getReference("userList");
    protected ArrayList<MergeCalendar> mergeCalendars;
    boolean complete = false;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference databaseReference = firebaseDatabase.getReference();


    int and= 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        mWeekView.setNumberOfVisibleDays(7);
        mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        mWeekView.goToToday();

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);
        mSavedEvents = new ArrayList<HashMap<String, String>>();
        mSavedEvents.clear();
        getCalendar();


        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), ApplyMeetingFilter.class);
                startActivity(intent);
            }
        };

        Button filter = (Button)findViewById(R.id.filter);
        filter.setOnClickListener(listener);

    }





    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     * @param shortDate True if the date values should be short.
     */

    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    public void getCalendar(){

        for(int i=0;i<userinfo.size();i++) {
            Log.i("infori", String.valueOf(userinfo.size()));
            userdb.child(userinfo.get(i).getUserID()).
                    child("calendar").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i("onDataChange", String.valueOf(userinfo.size()));
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        HashMap<String, String> temp = new HashMap<String, String>();
                        temp.put("year" + ds.getValue(CalendarList.class).getStartyear()
                                        + "month" + ds.getValue(CalendarList.class).getStartmonth()
                                        + "day" + ds.getValue(CalendarList.class).getStartday(),
                                ds.getValue(CalendarList.class).getStarthour()
                                        + ":" + ds.getValue(CalendarList.class).getStartminute()
                                        + "-" + ds.getValue(CalendarList.class).getEndhour()
                                        + ":" + ds.getValue(CalendarList.class).getEndminute());

                        mSavedEvents.add(temp);
                        Log.i("usercalenadr2", mSavedEvents.toString());
                    }
                    Log.i("endfor", String.valueOf(and));
                    and++;
                    if(and == userinfo.size()){
                        mergeCalendar();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void mergeCalendar(){
        Log.i("dkssud",String.valueOf(mSavedEvents.size()));
        String a[] = {"year2017month07day11", "year2017month07day12"};
        // 여기는 나중에 날짜 받으면 수정해주면 될듯


        mergeCalendars = new ArrayList<MergeCalendar>();
        mergeCalendars.clear();
        int[] count;
        int daysum = 2;
        for(int i = 0;i<2;i++){
            count = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
//            count = new int[24];
            Log.i("infori", String.valueOf(i));
            Log.i("infori", String.valueOf(mSavedEvents.size()));
            for(int j=0;j<mSavedEvents.size(); j++){
                Log.i("inforj", String.valueOf(mSavedEvents.size()));
                //여기서 string잘못뽑아서 에러날수도 있어
                String yearmonthday[] = mSavedEvents.get(j).toString().split("=");
                yearmonthday[0] = yearmonthday[0].replace("{", "");
                Log.i("mSavedEvents", yearmonthday[0]);
                Log.i("a[]", a[i]);

                if(yearmonthday[0].equals(a[i])){
                    //종일 이벤트는 처리 안해줌
                    String dayParsetime[] = mSavedEvents.get(j).toString().split("=");
                    String startParseend[] = dayParsetime[1].split("-");
                    String starttime[] = startParseend[0].split(":");
                    String endtime[] = startParseend[1].split(":");
                    int starthour = Integer.valueOf(starttime[0]);
                    endtime[0] = endtime[0].replace("}", "");
                    int endhour = Integer.valueOf(endtime[0]);
                    Log.i("forhyemin", "i: "+String.valueOf(i)+ "j :"+String.valueOf(j)+
                            "starthour: "+String.valueOf(starthour)+"endhour: "+String.valueOf(endhour));
                    for(int k=starthour; k<endhour; k++) {
                        count[k]++;
                    }
//                    MergeCalendar mergecal = new MergeCalendar(count, a[i]);
//                    mergeCalendars.add(mergecal);
//                    Log.i("forhyemin1", String.valueOf(mergeCalendars.size()));

                }
            }

            MergeCalendar mergecal = new MergeCalendar(count, a[i]);
            mergeCalendars.add(mergecal);
            Log.i("forhyemin1", String.valueOf(mergeCalendars.size()));
        }

        complete = true;

        int dkssud1[] = mergeCalendars.get(0).getCount();
        int dkssud2[] = mergeCalendars.get(1).getCount();

        Log.i("forhyemin", mergeCalendars.get(0).getDate());
        for(int i=0;i<24;i++) {
            Log.i("forhyemin 1번째 i : ", String.valueOf(i) + "count : " + String.valueOf(dkssud1[i]));
        }
        Log.i("forhyemin", mergeCalendars.get(1).getDate());
        for(int i=0;i<24;i++) {
            Log.i("forhyemin 2번째 i : ", String.valueOf(i) + "count : " + String.valueOf(dkssud2[i]));
        }
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(BaseActivity.this);

        String starttemp[] = event.getStartTime().toString().split(",");
        String startmonth1[] = starttemp[8].split("=");
        String startday1[] = starttemp[11].split("=");
        String starthour1[] = starttemp[16].split("=");


        int startmonth = Integer.valueOf(startmonth1[1])+1;
        int startday = Integer.valueOf(startday1[1]);
        int starthour = Integer.valueOf(starthour1[1]);


        String endtemp[] = event.getEndTime().toString().split(",");
        String endmonth1[] = endtemp[8].split("=");
        String endday1[] = endtemp[11].split("=");
        String endhour1[] = endtemp[16].split("=");

        int endmonth = Integer.valueOf(endmonth1[1]);
        int endday = Integer.valueOf(endday1[1]);
        int endhour = Integer.valueOf(endhour1[1]);
        final String value = event.getName()+": "+startmonth+"/"+startday+" "+starthour+"-"+endhour+" 미팅";


        String a1;
        if ((startmonth <10) && (startday < 10)){
            a1 = "year2017"+"month0"+startmonth+"day0"+startday+"start"+starthour+"end"+endhour;

        }else if((startmonth<10)){
              a1 = "year2017"+"month0"+startmonth+"day"+startday+"start"+starthour+"end"+endhour;
        }else if(startday < 10){
             a1 = "year2017"+"month"+startmonth+"day0"+startday+"start"+starthour+"end"+endhour;
        }else {
             a1 = "year2017"+"month"+startmonth+"day"+startday+"start"+starthour+"end"+endhour;
        }

        final String a = a1;

        alert_confirm.setMessage("이 날로 지정하겠습니까?").setCancelable(false).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'YES'

                        AnnounceListItem announce = new AnnounceListItem(userID, value);
                        Toast.makeText(getApplicationContext(),announce.getContent(),Toast.LENGTH_SHORT).show();
                        String meetingid = MeetingActivity.getMeeting_id();
                        databaseReference.child("MeetingDetails").child(meetingid).child("Announcement").push().setValue(announce);
                        HashMap<String, String> temp = new HashMap<String, String>();
                        temp.put(a, value);
                        AllMeetingCalendarFragment.mSavedEventsPerDay.add(temp);
                        AllMeetingCalendarFragment.mSavedEventDays.add(a);

                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'No'
                        return;
                    }
                });
        AlertDialog alert = alert_confirm.create();
        alert.show();

        Toast.makeText(this, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
    }

    public WeekView getWeekView() {
        return mWeekView;
    }
}
