package org.ssutown.manna.CustomCalendar;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ssutown.manna.AddAppointActivity;
import org.ssutown.manna.GoogleCalendar.CalendarList;
import org.ssutown.manna.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static org.ssutown.manna.HomeFragment.userID;
import static org.ssutown.manna.PersonFragment.descriptions;
import static org.ssutown.manna.PersonFragment.endDates;
import static org.ssutown.manna.PersonFragment.nameOfEvent;
import static org.ssutown.manna.PersonFragment.startDates;

/**
 * Created by Maximilian on 9/1/14.
 */
public class MaterialCalendarFragment extends Fragment
        implements View.OnClickListener, GridView.OnItemClickListener, EasyPermissions.PermissionCallbacks  {
    // Variables
    //Views
    ImageView mPrevious;
    ImageView mNext;
    TextView mMonthName;
    GridView mCalendar;
    Button addAppoint;
    // Calendar Adapter
    private MaterialCalendarAdapter mMaterialCalendarAdapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference calendardb = database.getReference("userList");
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;

    private static final String PREF_ACCOUNT_NAME = "accountName";

    // Saved Events Adapter
    protected static CalendarEventsAdapter mSavedEventsAdapter;
    protected static ListView mSavedEventsListView;

    protected static ArrayList<HashMap<String, Integer>> mSavedEventsPerDay; //string이 며칠(string)에 몇개(integer)의 일정이 있는지
    protected static ArrayList<Integer> mSavedEventDays; //일정이 있는 날을 표시 날! day


//    protected static ArrayList<HashMap<String, Integer>> mSavedEventsPerYear;
//    protected static ArrayList<HashMap<String, Integer>> mSavedEventsPerMonth;
//    protected static ArrayList<HashMap<String, Integer>> mSavedEventsPerDate;
    protected static ArrayList<CalendarList> mSavedEvents;
    protected static ArrayList<Boolean> mSavedEventDay;
    protected static ArrayList<String> mSaveTest;
    protected static ArrayList<String> mSaveTestday;

    protected static SharedPreferences selectedCalendar;
    protected static int select;
    protected static int mNumEventsOnDay = 0;

    GoogleAccountCredential mCredential;
    private static final String[] SCOPES = { CalendarScopes.CALENDAR };

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


        mCredential = GoogleAccountCredential.usingOAuth2(
                getActivity(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        getResultsFromApi();

// ListView for saved events in calendar
        mSavedEventsListView = (ListView) rootView.findViewById(R.id.saved_events_listView);



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

            addAppoint = (Button)rootView.findViewById(R.id.add_appointment);
            if(addAppoint != null){
                addAppoint.setOnClickListener(this);
            }

            // GridView for custom Calendar
            mCalendar = (GridView) rootView.findViewById(R.id.material_calendar_gridView); //캘린더 내용
            if (mCalendar != null) {
                mCalendar.setOnItemClickListener(this);
                mMaterialCalendarAdapter = new MaterialCalendarAdapter(getActivity());
                mCalendar.setAdapter(mMaterialCalendarAdapter);


                // Set current day to be auto selected when first opened
                if (MaterialCalendar.mCurrentDay != -1 && MaterialCalendar.mFirstDay != -1) {
                    int startingPosition = 6 + MaterialCalendar.mFirstDay;
                    int currentDayPosition = startingPosition + MaterialCalendar.mCurrentDay;

                    mCalendar.setItemChecked(currentDayPosition, true);

                    if (mMaterialCalendarAdapter != null) {
                        mMaterialCalendarAdapter.notifyDataSetChanged();
                    }
                }
            }


        }
        mSavedEvents = new ArrayList<CalendarList>();
        mSaveTest = new ArrayList<String>();
        mSaveTestday = new ArrayList<String>();

        calendardb.child(String.valueOf(userID)).child("calendar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mSavedEvents.clear();
                mSaveTest.clear();
                mSaveTestday.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    mSavedEvents.add(ds.getValue(CalendarList.class));
                    //들어감
                }
                for(int i=0;i<mSavedEvents.size();i++) {
                    String a = "year" + mSavedEvents.get(i).getStartyear() + "month" + mSavedEvents.get(i).getStartmonth() +
                            "day" + mSavedEvents.get(i).getStartday();
                    //들어감
                    mSaveTest.add(a);
                    mSaveTestday.add(a);
                }
//                for(int i=0;i<mSavedEvents.size();i++) {
//                    String a = "year" + mSavedEvents.get(i).getStartyear() + "month" + mSavedEvents.get(i).getStartmonth() +
//                            "day" + mSavedEvents.get(i).getStartday();
//                    //들어감
//                    if (mSaveTest.size() == 0) {
//                        HashMap<String, Integer> event = new HashMap<String, Integer>();
//                        event.put(a, 1);
//                        mSaveTest.add(event);
//                        mSaveTestday.add(a);
//                    } else{
//                        for (int j = 0; j < mSaveTest.size(); j++) {
//
//
//                            if (mSaveTest.get(j).containsKey(a)) {
//                                int temp = mSaveTest.get(j).get(a);
//                                mSaveTest.get(j).put(a, temp + 1);
//
//                            } else if (j == mSaveTest.size()-1) {
//                                HashMap<String, Integer> event = new HashMap<String, Integer>();
//                                event.put(a, 1);
//                                mSaveTest.add(event);
//                                mSaveTestday.add(a);
//                                break;
//                            }
//                        }
//
//                    }
//                }
                mMaterialCalendarAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return rootView;
    }

    private void getResultsFromApi() {


        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        }
//        else
        selectedCalendar = getActivity().getSharedPreferences("selectedCalendar", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = selectedCalendar.edit();
        select = selectedCalendar.getInt("cal", 0);
        Log.i("wldus", "1. select"+String.valueOf(select));

        if (mCredential.getSelectedAccountName() == null) {
            Toast.makeText(getActivity(), "나는 ㅠㅠ ", Toast.LENGTH_LONG).show();
            chooseAccount();

        }
//        else if (! isDeviceOnline()) {
//            //mOutputText.setText("No network connection available.");
//        }
        else {
            Toast.makeText(getActivity(), "나는 ㅎㅎ ", Toast.LENGTH_LONG).show();
            if(select == 0){
//                Toast toast = Toast.makeText(getActivity(),mCredential.getSelectedAccountName(),Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.TOP,0,0);
//                toast.show();
                new MakeRequestTask(mCredential).execute(); //주석없앰
            }
            editor.putInt("cal", 1);
            editor.apply();
            Log.i("wldus", "2. select"+String.valueOf(select));
        }
    }
    /**
     * An asynchronous task that ha+ndles the Google Calendar API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {

        private com.google.api.services.calendar.Calendar mService = null;
        //        private com.google.api.services.calendar.Calendar service = null;
        private Exception mLastError = null;


        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();

//            service = new com.google.api.services.calendar.Calendar.Builder(
//                    transport, jsonFactory, credential)
//                    .setApplicationName("Google Calendar API Android Quickstart")
//                    .build();

        }

        /**
         * Background task to call Google Calendar API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
//                insertEvent(mService);
                Log.i("wldus", "ondoinbackground");
                return getDataFromApi();

            } catch (Exception e) {
                Log.i("wldus", "ondoinbackground error");
                mLastError = e;
                Log.i("wldus", e.toString());
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of the next 10 events from the primary calendar.
         * @return List of Strings describing returned events.
         * @throws IOException
         */
        private List<String> getDataFromApi() throws IOException {


//            insertEvent(mService);
            Log.i("wldus", "getdatafromapi");
            // List the next 15 events from the primary calendar.
            DateTime now = new DateTime(System.currentTimeMillis());
            Log.i("wldus", "complete datatime");
            List<String> eventStrings = new ArrayList<String>();
            Log.i("wldus", "complete eventstring");
            Events events = mService.events().list("primary")
                    .setMaxResults(50)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            Log.i("wldus", "complete events");
            List<Event> items = events.getItems();
            Log.i("wldus", "getdatafromapi");
//            items.add(insertEvent(mService));



//            items.add(setDateToApi());
            int count = 0;
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                DateTime end = event.getEnd().getDateTime();
                if (start == null) {
                    // All-day events don't have start times, so just use
                    // the start date.
                    start = event.getStart().getDate();
                    end = event.getEnd().getDate();
                }
                Log.i("wldus", "inforevent"+String.valueOf(count));
                count++;
                eventStrings.add(
                        String.format("%s (%s ~ %s)", event.getSummary(), start,end));

                saveEventtoFirebase(event.getSummary(), start.toString(), end.toString());

            }
            return eventStrings;
        }

        public void saveEventtoFirebase(String event, String start, String end){
            String key = calendardb.child(String.valueOf(userID)).child("calendar").push().getKey();
            String eventname = event;
            String eventstart = start;
            String eventend = end;
            Log.i("wldus", "insaveeventtofirebase");
            if(eventstart.contains("T")){
                String tempstart[] = eventstart.split("T");
                String tempstart1 = tempstart[0];
                String tempstart2 = tempstart[1];

                String startday1[] = tempstart1.split("-");
                String starttime[] = tempstart2.split(":");

                String startyear = startday1[0];
                String startmonth = startday1[1];
                String startday = startday1[2];

                String starthour = starttime[0];
                String startminute = starttime[1];

                String tempend[] = eventend.split("T");
                String tempend1 = tempend[0];
                String tempend2 = tempend[1];


                String endday1[] = tempend1.split("-");
                String endtime[] = tempend2.split(":");

                String endyear = endday1[0];
                String endmonth = endday1[1];
                String endday = endday1[2];

                String endhour = endtime[0];
                String endminute = endtime[1];

                CalendarList list = new CalendarList(eventname,eventstart,eventend,key, startyear, startmonth,
                        startday,starthour,startminute, endyear, endmonth, endday, endhour, endminute);

                calendardb.child(String.valueOf(userID)).child("calendar").child(key).setValue(list);
            }
            else{
                String tempstart[] = eventstart.split("-");
                String startyear = tempstart[0];
                String startmonth = tempstart[1];
                String startday = tempstart[2];

                String tempend[] = eventend.split("-");
                String endyear = tempend[0];
                String endmonth = tempend[1];
                String endday = tempend[2];


                CalendarList list = new CalendarList(eventname,eventstart,eventend,key, startyear, startmonth,
                        startday,"","", endyear, endmonth, endday, "", "");

                calendardb.child(String.valueOf(userID)).child("calendar").child(key).setValue(list);
            }


        }
//        private Event setDateToApi()
//        {
//
//            Event event = new Event()
//                    .setSummary("Google I/O 2015")
//                    .setLocation("800 Howard St., San Francisco, CA 94103")
//                    .setDescription("A chance to hear more about Google's developer products.");
//
//            DateTime startDateTime = new DateTime("2017-05-28T09:00:00-07:00");
//            EventDateTime start = new EventDateTime()
//                    .setDateTime(startDateTime)
//                    .setTimeZone("Korea/Seoul");
//            event.setStart(start);
//
//            DateTime endDateTime = new DateTime("2017-05-28T17:00:00-07:00");
//            EventDateTime end = new EventDateTime()
//                    .setDateTime(endDateTime)
//                    .setTimeZone("Korea/Seoul");
//            event.setEnd(end);
//
//            String calendarId = "primary";
//            event = service.events().insert(calendarId, event).execute();
//
//            return event;
//        }

    }


    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                getActivity(), Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getActivity().getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);

//            startActivityForResult(             //로그인 한번 하려면 이거 주석하고 밑에 else풀어라. 이건 노력한 나헤언니가 알려준것. 노고가 들어가시아
//                    mCredential.newChooseAccountIntent(),
//                    REQUEST_ACCOUNT_PICKER);

            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                sendAccountName(accountName);
                getResultsFromApi();

            }
            else {
//                 Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    public void sendAccountName(String select_accountName) {
        SharedPreferences selectedAccountName = getActivity().getSharedPreferences("selectedAccountName", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = selectedAccountName.edit();
        editor.putString("accountName_a", select_accountName);
        editor.commit();
    }
    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(getActivity());
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                getActivity(),
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }


    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */

    @Override
    public void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
//                    mOutputText.setText(
//                            "This app requires Google Play Services. Please install " +
//                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        sendAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mSavedEventsListView != null) {
            mSavedEventsAdapter = new CalendarEventsAdapter();
            mSavedEventsListView.setAdapter(mSavedEventsAdapter);
            mSavedEventsListView.setOnItemClickListener(this);
            Log.d("EVENTS_ADAPTER", "set adapter");

            // Show current day saved events on load
            int today = MaterialCalendar.mCurrentDay + 6 + MaterialCalendar.mFirstDay;
            showSavedEventsListView(today);
        }
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.material_calendar_previous:
                    MaterialCalendar.previousOnClick(mPrevious, mMonthName, mCalendar, mMaterialCalendarAdapter);
                    break;

                case R.id.material_calendar_next:
                    org.ssutown.manna.CustomCalendar.MaterialCalendar.nextOnClick(mNext, mMonthName, mCalendar, mMaterialCalendarAdapter);
                    break;

                case R.id.add_appointment:

//                    Toast toast = Toast.makeText(getActivity(),"hi",Toast.LENGTH_SHORT);
//                    toast.show();
                    Intent intent = new Intent(getActivity(),AddAppointActivity.class);
                    startActivity(intent);


                    MaterialCalendar.nextOnClick(mNext, mMonthName, mCalendar, mMaterialCalendarAdapter);

                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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

        mSavedEventsPerDay = new ArrayList<HashMap<String, Integer>>(); //string이 며칠(string)에 몇개(integer)의 일정이 있는지

        /**
         * Make sure to use this variable name or update in CalendarAdapter 'setSavedEvent'
         */
        mSavedEventDays = new ArrayList<Integer>();//일정이 있는 day를 저장시킴

        // This is just used for testing purposes to show saved events on the calendar
        Random random = new Random();
        int randomNumOfEvents = random.nextInt(10 - 1) + 1;
        int EventNum=0;

//        for (int i = 0; i < randomNumOfEvents; i++) {
//            int day = random.nextInt(MaterialCalendar.mNumDaysInMonth - 1) + 1;
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
//
//        Log.d("SAVED_EVENT_DATES", String.valueOf(mSavedEventDays));
    }

    protected static void showSavedEventsListView(int position) {

        Boolean savedEventsOnThisDay = false;
        int selectedDate = -1;
        int selectedMonth= -1;
        int selectedYear= -1;
        String a="";
//        if (MaterialCalendar.mFirstDay != -1 && mSavedEventDays != null && mSavedEventDays.size
//                () > 0) {
//            selectedDate = position - (6 + MaterialCalendar.mFirstDay);
//            selectedMonth = MaterialCalendar.mMonth+1;
//            selectedYear = MaterialCalendar.mYear;
//
//            for (int i = 0; i < mSavedEventDays.size(); i++) {
//                if (selectedDate == mSavedEventDays.get(i)) {
//                    savedEventsOnThisDay = true;                //일정이 저장된 날과 선택된 날이 같으면 true로 바꿔준다.
//                }
//            }
//        }

//        Log.i("mSaveTestday", mSaveTestday.get(0).toString());

        if (MaterialCalendar.mFirstDay != -1 && mSaveTestday != null && mSaveTestday.size
                () > 0) {
            selectedDate = position - (6 + MaterialCalendar.mFirstDay);
            selectedMonth = MaterialCalendar.mMonth+1;
            selectedYear = MaterialCalendar.mYear;
            if ((selectedMonth <10) && (selectedDate < 10)){
                a = "year"+selectedYear+"month0"+selectedMonth+"day0"+selectedDate;

            }else if((selectedMonth<10)){
                a = "year"+selectedYear+"month0"+selectedMonth+"day"+selectedDate;
            }else if(selectedDate < 10){
                a = "year"+selectedYear+"month"+selectedMonth+"day0"+selectedDate;
            }else {
                a = "year"+selectedYear+"month"+selectedMonth+"day"+selectedDate;
            }


            for (int i = 0; i < mSaveTestday.size(); i++) {
                if (a.equals(mSaveTestday.get(i))) {
                    savedEventsOnThisDay = true;//일정이 저장된 날과 선택된 날이 같으면 true로 바꿔준다.

                }
            }
        }
//        Log.d("SAVED_EVENTS_BOOL", String.valueOf(savedEventsOnThisDay));
//
//        if (savedEventsOnThisDay) {     //선택된 날에 일정이 있으면
//            Log.d("POS", String.valueOf(selectedDate));
//            if (mSavedEventsPerDay != null && mSavedEventsPerDay.size() > 0) {
//                for (int i = 0; i < mSavedEventsPerDay.size(); i++) {
//                    HashMap<String, Integer> x = mSavedEventsPerDay.get(i);
//                    if (x.containsKey("day" + selectedDate)) {
//                        mNumEventsOnDay = mSavedEventsPerDay.get(i).get("day" + selectedDate);
//                        Log.d("NUM_EVENT_ON_DAY", String.valueOf(mNumEventsOnDay));
//                    }
//                }
//            }
//        } else {
//            mNumEventsOnDay = -1;
//        }
//
//        if (mSavedEventsAdapter != null && mSavedEventsListView != null) {
//            mSavedEventsAdapter.notifyDataSetChanged();
//
//             Scrolls back to top of ListView before refresh
//            mSavedEventsListView.setSelection(0);
//        }

        if (savedEventsOnThisDay) {     //선택된 날에 일정이 있으면
            if (mSaveTest != null && mSaveTest.size() > 0) {
                for (int i = 0; i < mSaveTest.size(); i++) {
                    String x = mSaveTest.get(i);
                    if (x.equals(a)) {
//                        mNumEventsOnDay = Integer.valueOf(mSaveTest.get(i));
                        Log.d("NUM_EVENT_ON_DAY", String.valueOf(mNumEventsOnDay));
                    }
                }
            }
        } else {
            mNumEventsOnDay = -1;
        }

        if (mSavedEventsAdapter != null && mSavedEventsListView != null) {

            mSavedEventsAdapter.clear();
            for(int i=0;i<mSaveTest.size(); i++){
                if(mSaveTest.get(i).equals(a)){
                    mSavedEventsAdapter.addItem(mSavedEvents.get(i).getEventname(), String.valueOf(mSavedEvents.get(i).getStart())+String.valueOf(mSavedEvents.get(i).getEnd()));
                    Log.i("adaptertest", "gg");
                }
            }
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


