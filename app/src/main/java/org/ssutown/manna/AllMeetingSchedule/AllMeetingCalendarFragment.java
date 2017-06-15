package org.ssutown.manna.AllMeetingSchedule;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ssutown.manna.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by YNH on 2017. 5. 30..
 */

public class AllMeetingCalendarFragment extends Fragment implements View.OnClickListener, GridView.OnItemClickListener{
    // Variables
    //Views
    ImageView mPrevious;
    ImageView mNext;
    TextView mMonthName;
    GridView mCalendar;

    // Calendar Adapter
    private AllMeetingCalendarAdapter mMeetingCalendarAdapter;

    // Saved Events Adapter
    protected static AllCalendarEventsAdapter mSavedEventsAdapter;
    protected static ListView mSavedEventsListView;

    public static ArrayList<HashMap<String, String>> mSavedEventsPerDay = new ArrayList<HashMap<String, String>>();
    public static ArrayList<String> mSavedEventDays = new ArrayList<String>();

    protected static int mNumEventsOnDay = 0;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.all_fragment_calendar, container, false);
        if (rootView != null) {
            // Get Calendar info
            // Get Calendar info
            AllMeetingCalendar.getInitialCalendarInfo();
            getSavedEventsForCurrentMonth();

            // Previous ImageView
            mPrevious = (ImageView) rootView.findViewById(R.id.material_calendar_previous);
            if (mPrevious != null) {
                mPrevious.setOnClickListener(this);
            }

            // Month name TextView
            mMonthName = (TextView) rootView.findViewById(R.id.material_calendar_month_name);
            if (mMonthName != null) {
                Calendar cal = Calendar.getInstance();
                if (cal != null) {
                    mMonthName.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG,
                            Locale.getDefault()) + " " + cal.get(Calendar.YEAR));
                }
            }

            // Next ImageView
            mNext = (ImageView) rootView.findViewById(R.id.material_calendar_next);
            if (mNext != null) {
                mNext.setOnClickListener(this);
            }

            // GridView for custom Calendar
            mCalendar = (GridView) rootView.findViewById(R.id.material_calendar_gridView);
            if (mCalendar != null) {
                mCalendar.setOnItemClickListener(this);
                mMeetingCalendarAdapter = new AllMeetingCalendarAdapter(getActivity());
                mCalendar.setAdapter(mMeetingCalendarAdapter);

                // Set current day to be auto selected when first opened
                if (AllMeetingCalendar.mCurrentDay != -1 && AllMeetingCalendar.mFirstDay != -1){
                    int startingPosition = 6 + AllMeetingCalendar.mFirstDay;
                    int currentDayPosition = startingPosition + AllMeetingCalendar.mCurrentDay;

//                    Log.d("INITIAL_SELECTED_POSITION", String.valueOf(currentDayPosition));

                    mCalendar.setItemChecked(currentDayPosition, true);

                    if (mMeetingCalendarAdapter != null) {
                        mMeetingCalendarAdapter.notifyDataSetChanged();
                    }
                }
            }


            // ListView for saved events in calendar
            mSavedEventsListView = (ListView) rootView.findViewById(R.id.saved_events_listView);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mSavedEventsListView != null) {
            mSavedEventsAdapter = new AllCalendarEventsAdapter();
            mSavedEventsListView.setAdapter(mSavedEventsAdapter);
            mSavedEventsListView.setOnItemClickListener(this);
            Log.d("EVENTS_ADAPTER", "set adapter");

            // Show current day saved events on load
            int today = AllMeetingCalendar.mCurrentDay + 6 + AllMeetingCalendar.mFirstDay;
            showSavedEventsListView(today);
        }
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.material_calendar_previous:
                    AllMeetingCalendar.previousOnClick(mPrevious, mMonthName, mCalendar, mMeetingCalendarAdapter);
                    break;

                case R.id.material_calendar_next:
                    AllMeetingCalendar.nextOnClick(mNext, mMonthName, mCalendar, mMeetingCalendarAdapter);
                    break;


                default:
                    break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Toast toast = Toast.makeText(getActivity(),"hi", Toast.LENGTH_SHORT);
        toast.show();

        switch (parent.getId()) {
            case R.id.material_calendar_gridView:
                AllMeetingCalendar.selectCalendarDay(mMeetingCalendarAdapter, position);

                // Reset event list
                mNumEventsOnDay = -1;

                showSavedEventsListView(position);
                break;

            default:
                break;
        }
    }

    // Saved Events
    protected static void getSavedEventsForCurrentMonth() {
        /**
         *  -- IMPORTANT --
         *  This is where you get saved event info
         */

        // -- Ideas on what could be done here --
        // Probably pull from some database
        // cross check event dates with current calendar month and year

        // For loop adding each event date to ArrayList
        // Also get ArrayList<SavedEvents>

//        mSavedEventsPerDay = new ArrayList<HashMap<String, String>>();

        /**
         * Make sure to use this variable name or update in CalendarAdapter 'setSavedEvent'
         */
//        mSavedEventDays = new ArrayList<String>();

        // This is just used for testing purposes to show saved events on the calendar
//        Random random = new Random();
//        int randomNumOfEvents = random.nextInt(10 - 1) + 1;
//
//        for (int i = 0; i < randomNumOfEvents; i++) {
//            int day = random.nextInt(AllMeetingCalendar.mNumDaysInMonth - 1) + 1;
//            int eventPerDay = random.nextInt(5 - 1) + 1;
//
//            HashMap<String, Integer> dayInfo = new HashMap<String, Integer>();
//            dayInfo.put("day" + day, eventPerDay);
//
//            mSavedEventDays.add(day);
//            mSavedEventsPerDay.add(dayInfo);
//
//            Log.d("EVENTS_PER DAY", String.valueOf(dayInfo));
//        }

//        Log.d("SAVED_EVENT_DATES", String.valueOf(mSavedEventDays));
    }

    protected static void showSavedEventsListView(int position) {
        Boolean savedEventsOnThisDay = false;
        int selectedDate = -1;
        int selectedMonth= -1;
        int selectedYear= -1;
        String a="";

        if (AllMeetingCalendar.mFirstDay != -1 && mSavedEventDays != null && mSavedEventDays.size
                () > 0) {
            selectedDate = position - (6 + AllMeetingCalendar.mFirstDay);
            selectedMonth = AllMeetingCalendar.mMonth+1;
            selectedYear = AllMeetingCalendar.mYear;
            if ((selectedMonth <10) && (selectedDate < 10)){
                a = "year"+selectedYear+"month0"+selectedMonth+"day0"+selectedDate;

            }else if((selectedMonth<10)){
                a = "year"+selectedYear+"month0"+selectedMonth+"day"+selectedDate;
            }else if(selectedDate < 10){
                a = "year"+selectedYear+"month"+selectedMonth+"day0"+selectedDate;
            }else {
                a = "year"+selectedYear+"month"+selectedMonth+"day"+selectedDate;
            }

            for (int i = 0; i < mSavedEventDays.size(); i++) {
                String rea[] = mSavedEventDays.get(i).split("start");
                Log.i("allfragment", rea[0]);

                if (a.equals(rea[0])) {
                    savedEventsOnThisDay = true;//일정이 저장된 날과 선택된 날이 같으면 true로 바꿔준다.

                }
            }
        }

        if (mSavedEventsAdapter != null && mSavedEventsListView != null) {
            mSavedEventsAdapter.clear();
            for(int i=0;i<mSavedEventDays.size(); i++){
                String rea[] = mSavedEventDays.get(i).split("start");
                String temp[] = rea[1].split("end");
                String start = temp[0];
                String end = temp[1];
                Log.i("allfragment:start", start);
                Log.i("allfragment: end", end);

                if(rea[0].equals(a)){
                    mSavedEventsAdapter.addItem("미팅 확정", start+"시 부터 "+end+"시 까지");
                    Log.i("adaptertest", "gg");
                }
            }
            mSavedEventsAdapter.notifyDataSetChanged();

            // Scrolls back to top of ListView before refresh
            mSavedEventsListView.setSelection(0);
        }
    }

}
