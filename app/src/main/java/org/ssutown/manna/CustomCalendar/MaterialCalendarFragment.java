package org.ssutown.manna.CustomCalendar;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
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

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;

import org.ssutown.manna.AddAppointActivity;
import org.ssutown.manna.R;

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
    Button mAdd;

    public static ArrayList<String> nameOfEvent = new ArrayList<String>();
    public static ArrayList<String> startDates = new ArrayList<String>();
    public static ArrayList<String> endDates = new ArrayList<String>();
    public static ArrayList<String> descriptions = new ArrayList<String>();

    // Calendar Adapter
    private MaterialCalendarAdapter mMaterialCalendarAdapter;

    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;

    private static final String PREF_ACCOUNT_NAME = "accountName";

    // Saved Events Adapter
    protected static SavedEventsAdapter mSavedEventsAdapter;
    protected static ListView mSavedEventsListView;

    protected static ArrayList<HashMap<String, Integer>> mSavedEventsPerDay;
    protected static ArrayList<Integer> mSavedEventDays;

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
        Log.i("i'm matcalendarFrag", "");

        mCredential = GoogleAccountCredential.usingOAuth2(
                getActivity(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        getResultsFromApi();


//        SharedPreferences selectedCalendar = getActivity().getSharedPreferences("selectedCalendar", Context.MODE_PRIVATE);
//        int select = selectedCalendar.getInt("cal_num",0);
//        Toast.makeText(getActivity(),String.valueOf(select),Toast.LENGTH_SHORT).show();
//
////        if(select == 0) {
//            Intent intent = new Intent(getActivity(), SelectCalendar.class);
//            startActivity(intent);
////        }

//        SharedPreferences selectedAccountName = getActivity().getSharedPreferences("selectedAccountName", Context.MODE_PRIVATE);
//        String accountName = selectedAccountName.getString("accountName","");
//
//
//        Toast toast = Toast.makeText(getActivity(),accountName,Toast.LENGTH_LONG);
//        toast.show();

//        select = getArguments().getInt("num");


        if (rootView != null) {
            // Get Calendar info
            // Get Calendar info
            org.ssutown.manna.CustomCalendar.MaterialCalendar.getInitialCalendarInfo();
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
                if (org.ssutown.manna.CustomCalendar.MaterialCalendar.mCurrentDay != -1 && org.ssutown.manna.CustomCalendar.MaterialCalendar.mFirstDay != -1){
                    int startingPosition = 6 + org.ssutown.manna.CustomCalendar.MaterialCalendar.mFirstDay;
                    int currentDayPosition = startingPosition + org.ssutown.manna.CustomCalendar.MaterialCalendar.mCurrentDay;

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

    private void getResultsFromApi() {


//        if (! isGooglePlayServicesAvailable()) {
//            acquireGooglePlayServices();
//        }
//        else
        if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        }

//        else if (! isDeviceOnline()) {
//            //mOutputText.setText("No network connection available.");
//        }
//        else {
////            new LoginActivity.MakeRequestTask(mCredential).execute();
//        }
    }

//    private boolean isGooglePlayServicesAvailable() {
//        GoogleApiAvailability apiAvailability =
//                GoogleApiAvailability.getInstance();
//        final int connectionStatusCode =
//                apiAvailability.isGooglePlayServicesAvailable(this);
//        return connectionStatusCode == ConnectionResult.SUCCESS;
//    }
//
//
//    private void acquireGooglePlayServices() {
//        GoogleApiAvailability apiAvailability =
//                GoogleApiAvailability.getInstance();
//        final int connectionStatusCode =
//                apiAvailability.isGooglePlayServicesAvailable(this);
//        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
//            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
//        }
//    }

//    private boolean isDeviceOnline() {
//        ConnectivityManager connMgr =
//                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        return (networkInfo != null && networkInfo.isConnected());
//    }

//    void showGooglePlayServicesAvailabilityErrorDialog(
//            final int connectionStatusCode) {
//        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//        Dialog dialog = apiAvailability.getErrorDialog(
//                MaterialCalendarFragment.this,
//                connectionStatusCode,
//                REQUEST_GOOGLE_PLAY_SERVICES);
//        dialog.show();
//    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                getActivity(), Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getActivity().getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);

            startActivityForResult(
                    mCredential.newChooseAccountIntent(),
                    REQUEST_ACCOUNT_PICKER);

            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                sendAccountName(accountName);
//                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
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
//                    getResultsFromApi();
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
///                        getResultsFromApi();
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
            mSavedEventsAdapter = new SavedEventsAdapter(getActivity());
            mSavedEventsListView.setAdapter(mSavedEventsAdapter);
            mSavedEventsListView.setOnItemClickListener(this);
            Log.d("EVENTS_ADAPTER", "set adapter");

            // Show current day saved events on load
            int today = org.ssutown.manna.CustomCalendar.MaterialCalendar.mCurrentDay + 6 + org.ssutown.manna.CustomCalendar.MaterialCalendar.mFirstDay;
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
                    org.ssutown.manna.CustomCalendar.MaterialCalendar.previousOnClick(mPrevious, mMonthName, mCalendar, mMaterialCalendarAdapter);
                    break;

                case R.id.material_calendar_next:
                    org.ssutown.manna.CustomCalendar.MaterialCalendar.nextOnClick(mNext, mMonthName, mCalendar, mMaterialCalendarAdapter);
                    break;

                case R.id.add_appointment:

//                    Toast toast = Toast.makeText(getActivity(),"hi",Toast.LENGTH_SHORT);
//                    toast.show();
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
            int day = org.ssutown.manna.CustomCalendar.MaterialCalendar.mNumDaysInMonth + 1;
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

