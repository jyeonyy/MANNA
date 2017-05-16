package org.ssutown.manna.CustomCalendar;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ssutown.manna.AddAppointActivity;
import org.ssutown.manna.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Maximilian on 9/1/14.
 */
public class MaterialCalendarFragment extends Fragment implements View.OnClickListener, GridView.OnItemClickListener{
    // Variables
    //Views
    ImageView mPrevious;
    ImageView mNext;
    TextView mMonthName;
    GridView mCalendar;
    Button mAdd;

    public static ArrayList<String> nameOfEvent = new ArrayList<String>();
    public static ArrayList<String> startDates = new ArrayList<String>();
    public static ArrayList<String> endDates = new ArrayList<String>();
    public static ArrayList<String> descriptions = new ArrayList<String>();

    // Calendar Adapter
    private MaterialCalendarAdapter mMaterialCalendarAdapter;

    // Saved Events Adapter
    protected static SavedEventsAdapter mSavedEventsAdapter;
    protected static ListView mSavedEventsListView;

    protected static ArrayList<HashMap<String, Integer>> mSavedEventsPerDay;
    protected static ArrayList<Integer> mSavedEventDays;

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
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        if (rootView != null) {
            // Get Calendar info
            // Get Calendar info
            MaterialCalendar.getInitialCalendarInfo();
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
                mMaterialCalendarAdapter = new MaterialCalendarAdapter(getActivity());
                mCalendar.setAdapter(mMaterialCalendarAdapter);


                // Set current day to be auto selected when first opened
                if (MaterialCalendar.mCurrentDay != -1 && MaterialCalendar.mFirstDay != -1){
                    int startingPosition = 6 + MaterialCalendar.mFirstDay;
                    int currentDayPosition = startingPosition + MaterialCalendar.mCurrentDay;

//                    Log.d("INITIAL_SELECTED_POSITION", String.valueOf(currentDayPosition));
                    mCalendar.setItemChecked(currentDayPosition, true);

                    if (mMaterialCalendarAdapter != null) {
                        mMaterialCalendarAdapter.notifyDataSetChanged();
                    }
                }
            }

            mAdd = (Button)rootView.findViewById(R.id.add_appointment);
            if(mAdd != null){
                mAdd.setOnClickListener(this);

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
            mSavedEventsAdapter = new SavedEventsAdapter(getActivity());
            mSavedEventsListView.setAdapter(mSavedEventsAdapter);
            mSavedEventsListView.setOnItemClickListener(this);
            Log.d("EVENTS_ADAPTER", "set adapter");

            // Show current day saved events on load
            int today = MaterialCalendar.mCurrentDay + 6 + MaterialCalendar.mFirstDay;
            showSavedEventsListView(today);
        }
    }

//    public void func(){
//        Toast toast = Toast.makeText(getActivity(),"hi",Toast.LENGTH_SHORT);
//        toast.show();
//    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.material_calendar_previous:
                    MaterialCalendar.previousOnClick(mPrevious, mMonthName, mCalendar, mMaterialCalendarAdapter);
                    break;

                case R.id.material_calendar_next:
                    MaterialCalendar.nextOnClick(mNext, mMonthName, mCalendar, mMaterialCalendarAdapter);
                    break;

                case R.id.add_appointment:
                    Intent intent = new Intent(getActivity(),AddAppointActivity.class);
                    startActivity(intent);
                    break;

                default:
                    break;
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
//
//        ad.setTitle("Appointment");       // 제목 설정
//        ad.setMessage("Do you want to add appointment?");   // 내용 설정
//
//        // 확인 버튼 설정
//        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Log.v("TAG","Yes Btn Click");
//                dialog.dismiss();     //닫기
//                // Event
//            }
//        });
//
//
//        // 취소 버튼 설정
//        ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Log.v("TAG","No Btn Click");
//                dialog.dismiss();     //닫기
//                // Event
//            }
//        });
//
//        // 창 띄우기
 //       ad.show();


        switch (parent.getId()) {
            case R.id.material_calendar_gridView:
                MaterialCalendar.selectCalendarDay(mMaterialCalendarAdapter, position);

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

        mSavedEventsPerDay = new ArrayList<HashMap<String, Integer>>();

        /**
         * Make sure to use this variable name or update in CalendarAdapter 'setSavedEvent'
         */
        mSavedEventDays = new ArrayList<Integer>();

        // This is just used for testing purposes to show saved events on the calendar

        Random random = new Random();

        for (int i = 0; i < mNumEventsOnDay; i++) {
            int day = MaterialCalendar.mNumDaysInMonth + 1;
            int eventPerDay = random.nextInt(5 - 1) + 1;

            HashMap<String, Integer> dayInfo = new HashMap<String, Integer>();
            dayInfo.put("day" + day, eventPerDay);

            mSavedEventDays.add(day);
            mSavedEventsPerDay.add(dayInfo);

            Log.d("EVENTS_PER DAY", String.valueOf(dayInfo));
        }

        Log.d("SAVED_EVENT_DATES", String.valueOf(mSavedEventDays));
    }

    protected static void showSavedEventsListView(int position) {
        Boolean savedEventsOnThisDay = false;
        int selectedDate = -1;

        if (MaterialCalendar.mFirstDay != -1 && mSavedEventDays != null && mSavedEventDays.size
                () > 0) {
            selectedDate = position - (6 + MaterialCalendar.mFirstDay);
            Log.d("SELECTED_SAVED_DATE", String.valueOf(selectedDate));

            for (int i = 0; i < mSavedEventDays.size(); i++) {
                if (selectedDate == mSavedEventDays.get(i)) {
                    savedEventsOnThisDay = true;
                }
            }
        }

        Log.d("SAVED_EVENTS_BOOL", String.valueOf(savedEventsOnThisDay));

        if (savedEventsOnThisDay) {
            Log.d("POS", String.valueOf(selectedDate));
            if (mSavedEventsPerDay != null && mSavedEventsPerDay.size() > 0) {
                for (int i = 0; i < mSavedEventsPerDay.size(); i++) {
                    HashMap<String, Integer> x = mSavedEventsPerDay.get(i);
                    if (x.containsKey("day" + selectedDate)) {
                        mNumEventsOnDay = mSavedEventsPerDay.get(i).get("day" + selectedDate);
                        Log.d("NUM_EVENT_ON_DAY", String.valueOf(mNumEventsOnDay));
                    }
                }
            }
        } else {
            mNumEventsOnDay = -1;
        }

        if (mSavedEventsAdapter != null && mSavedEventsListView != null) {
            mSavedEventsAdapter.notifyDataSetChanged();

            // Scrolls back to top of ListView before refresh
            mSavedEventsListView.setSelection(0);
        }
    }

    public ArrayList<String> readCalendar(Context context){
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation" }, null,
                        null, null);
        cursor.moveToFirst();

        String CNames[] = new String[cursor.getCount()];

        nameOfEvent.clear();
        startDates.clear();
        endDates.clear();
        descriptions.clear();

        for(int i = 0 ; i<CNames.length; i++){
            nameOfEvent.add(cursor.getString(1));
            startDates.add(getDate(Long.parseLong(cursor.getString(3))));
            endDates.add(getDate(Long.parseLong(cursor.getString(4))));
            descriptions.add(cursor.getString(2));
            CNames[i] = cursor.getString(1);
            cursor.moveToNext();
        }

        return nameOfEvent;
    }

    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


}


