package org.ssutown.manna;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static org.ssutown.manna.SelectCalendar.REQUEST_ACCOUNT_PICKER;
import static org.ssutown.manna.SelectCalendar.REQUEST_GOOGLE_PLAY_SERVICES;
import static org.ssutown.manna.SelectCalendar.REQUEST_PERMISSION_GET_ACCOUNTS;


/**
 * Created by YNH on 2017. 5. 7..
 */

public class AddAppointActivity extends Activity {

    HttpTransport transport = AndroidHttp.newCompatibleTransport();
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    final String[] SCOPES = { CalendarScopes.CALENDAR };


    GoogleAccountCredential mCredential ;

    private static final String PREF_ACCOUNT_NAME = "accountName";

    static final int REQUEST_AUTHORIZATION = 1001;
//
//    private Exception mLastError = null;
//

//
    String accountName;

    String name;
    String start;
    String end;


    @Override
    protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.layout_add_appoint);

//        Intent intent = getIntent();
//
//        mCredential.setSelectedAccount((Account) intent.getSerializableExtra("credential"));
//
//
//        SharedPreferences selectedAccountName = getApplicationContext().getSharedPreferences("selectedAccountName", Context.MODE_PRIVATE);
//        accountName = selectedAccountName.getString("accountName","");

        mCredential = GoogleAccountCredential.usingOAuth2(
                    AddAppointActivity.this,
                    Collections.singleton("https://www.googleapis.com/auth/contacts.readonly"));




        View.OnClickListener listener = new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {
                //ADD Button 클릭시 이벤트 추가
//                GoogleCalendarActivity google_cal = new GoogleCalendarActivity();
//                Bundle name = new Bundle();
//                name.putChar("NameOfAppoint",getText(name));
//                google_cal.setArguments(name);


//                SharedPreferences selectedAccountName = getApplicationContext().getSharedPreferences("selectedAccountName", Context.MODE_PRIVATE);
//                accountName = selectedAccountName.getString("accountName","");

//                String accountName = getPreferences(Context.MODE_PRIVATE)
//                        .getString(PREF_ACCOUNT_NAME, null);
//
//                mCredential.setSelectedAccountName(accountName);

                if (EasyPermissions.hasPermissions(
                        getApplicationContext(), android.Manifest.permission.GET_ACCOUNTS)) {
//                    String accountName = getPreferences(Context.MODE_PRIVATE)
//                            .getString(PREF_ACCOUNT_NAME, null);

//                    startActivityForResult(
//                            mCredential.newChooseAccountIntent(),
//                            REQUEST_ACCOUNT_PICKER);

                    SharedPreferences selectedAccountName = getSharedPreferences("selectedAccountName", Context.MODE_PRIVATE);
                    accountName = selectedAccountName.getString("accountName_a","");

                    if (accountName != null) {
                        mCredential.setSelectedAccountName(accountName);
//                        accountName 에 계정이 들어간 것을 확인 할 수 있었음
                        Toast toast = Toast.makeText(getApplicationContext(),accountName,Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,0);
                        toast.show();
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
                            android.Manifest.permission.GET_ACCOUNTS);
                }
//

                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss");

                final EditText edit_name = (EditText)findViewById(R.id.AppointName);

                final DatePicker startDatePicker = (DatePicker)findViewById(R.id.datePicker_start);
                final DatePicker endDatePicker = (DatePicker)findViewById(R.id.datePicker_end);
                final TimePicker startTimePicker = (TimePicker)findViewById(R.id.timePicker_start);
                final TimePicker endTimePicker = (TimePicker)findViewById(R.id.timePicker_end);

                String startDate = dateformat.format
                        (new java.sql.Date(startDatePicker.getYear()-1900, startDatePicker.getMonth(), startDatePicker.getDayOfMonth()));
                String endDate = dateformat.format
                        (new java.sql.Date(endDatePicker.getYear()-1900, endDatePicker.getMonth(), endDatePicker.getDayOfMonth()));
                String startTime = timeformat.format
                        (new java.sql.Time(startTimePicker.getHour(), startTimePicker.getMinute(),0));
                String endTime = timeformat.format
                        (new java.sql.Time(endTimePicker.getHour(), endTimePicker.getMinute(),0));

                name = edit_name.getText().toString();

                start = new StringBuilder()
                        .append(startDate)
                        .append('T')
                        .append(startTime)
                        .append("+09:00")
                        .toString();
                end = new StringBuilder()
                        .append(endDate)
                        .append('T')
                        .append(endTime)
                        .append("+09:00")
                        .toString();

//                Intent intent = new Intent(getApplicationContext(), GoogleCalendarActivity.class);
//
//                intent.putExtra("NameOfAppoint",name.getText().toString());
//                intent.putExtra("Start",start);
//                intent.putExtra("End",end);


//                Toast toast = Toast.makeText(getApplicationContext(),start,Toast.LENGTH_SHORT);
//                toast.show();
//

                new MakeRequestTask(mCredential).execute();


                finish();

            }
        };

        Button addBtn = (Button)findViewById(R.id.addBtn);
        addBtn.setOnClickListener(listener);


    }

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
                insertEvent(mService,name,start,end);
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of the next 10 events from the primary calendar.
         * @return List of Strings describing returned events.
         * @throws IOException
         */

        public void insertEvent(com.google.api.services.calendar.Calendar mService,String name_a,String start_a,String end_a) throws IOException {

//            HttpTransport transport = AndroidHttp.newCompatibleTransport();
//            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//            com.google.api.services.calendar.Calendar service = new com.google.api.services.calendar.Calendar.Builder(
//                    transport, jsonFactory, mCredential)
//                    .setApplicationName("Google Calendar API Android Quickstart")
//                    .build();

//            Toast toast = Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_SHORT);
//            toast.show();

            try {
                com.google.api.services.calendar.model.Calendar calendar =
                        mService.calendars().get("primary").execute();

            } catch (IOException e) {
                e.printStackTrace();
            }


            Event event = new Event()
                    .setSummary(name_a)
                    .setLocation("Dhaka")
                    .setDescription("New Event 1");

            DateTime startDateTime = new DateTime(start_a);
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone("Asia/Seoul");
            event.setStart(start);

            DateTime endDateTime = new DateTime(end_a);
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone("Asia/Seoul");
            event.setEnd(end);

            String calendarId = "primary";
            try {
                mService.events().insert(calendarId, event).execute();
//                mService.events().update(calendarId,event.getId(),event).execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private List<String> getDataFromApi() throws IOException {


//            insertEvent(mService);

            // List the next 15 events from the primary calendar.
            DateTime now = new DateTime(System.currentTimeMillis());
            List<String> eventStrings = new ArrayList<String>();
            Events events = mService.events().list("primary")
                    .setMaxResults(20)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();

//            items.add(insertEvent(mService));

//            items.add(setDateToApi());

            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                DateTime end = event.getEnd().getDateTime();
                if (start == null) {
                    // All-day events don't have start times, so just use
                    // the start date.
                    start = event.getStart().getDate();
                    end = event.getEnd().getDate();
                }
                eventStrings.add(
                        String.format("%s (%s ~ %s)", event.getSummary(), start,end));
            }
            return eventStrings;
        }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(List<String> output) {

        }

        @Override
        protected void onCancelled() {

            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            AddAppointActivity.REQUEST_AUTHORIZATION);
                }
            }



        }

        void showGooglePlayServicesAvailabilityErrorDialog(
                final int connectionStatusCode) {
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            Dialog dialog = apiAvailability.getErrorDialog(
                    AddAppointActivity.this,
                    connectionStatusCode,
                    REQUEST_GOOGLE_PLAY_SERVICES);
            dialog.show();
        }

    }

}