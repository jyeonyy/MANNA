package org.ssutown.manna;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;

/**
 * Created by YNH on 2017. 5. 7..
 */

public class AddAppointActivity extends Activity {

    HttpTransport transport = AndroidHttp.newCompatibleTransport();
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    final String[] SCOPES = { CalendarScopes.CALENDAR };


    GoogleAccountCredential mCredential =
                   GoogleAccountCredential.usingOAuth2(
                       AddAppointActivity.this,
                       Collections.singleton(
                           "https://www.googleapis.com/auth/contacts.readonly")
                    );



    private com.google.api.services.calendar.Calendar mService = null;
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


                SharedPreferences selectedAccountName = getApplicationContext().getSharedPreferences("selectedAccountName", Context.MODE_PRIVATE);
                accountName = selectedAccountName.getString("accountName","");

                mCredential.setSelectedAccountName(accountName);
              //
                mService = new com.google.api.services.calendar.Calendar.Builder(transport, jsonFactory, mCredential)
                                      .setApplicationName("Google Calendar API Android Quickstart")
                                      .build();
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

                name = edit_name.toString();

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

//                new AddAppointActivity.MakeRequestTask(mCredential).execute();

//                Intent intent = new Intent(getApplicationContext(), GoogleCalendarActivity.class);
//
//                intent.putExtra("NameOfAppoint",name.getText().toString());
//                intent.putExtra("Start",start);
//                intent.putExtra("End",end);


//                Toast toast = Toast.makeText(getApplicationContext(),start,Toast.LENGTH_SHORT);
//                toast.show();
//
                try {
                    insertEvent(mService,name,start,end);
                } catch (IOException e) {
                    e.printStackTrace();
//                    Toast toast = Toast.makeText(getApplicationContext(),"NO",Toast.LENGTH_SHORT);
//                    toast.show();
                }


            }
        };

        Button addBtn = (Button)findViewById(R.id.addBtn);
        addBtn.setOnClickListener(listener);

    }

    public void insertEvent(com.google.api.services.calendar.Calendar Service,String insert_name,String insert_start,String insert_end) throws IOException {

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
                    Service.calendars().get("primary").execute();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Event event = new Event()
                .setSummary(insert_name);
//                .setLocation("Dhaka")
//                .setDescription("New Event 1");

        DateTime startDateTime = new DateTime(insert_start);
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Asia/Seoul");
        event.setStart(start);

        DateTime endDateTime = new DateTime(insert_end);
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Asia/Seoul");
        event.setEnd(end);

        String calendarId = "primary";
        try {
            Service.events().insert(calendarId, event).execute();
//                mService.events().update(calendarId,event.getId(),event).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
//            System.out.printf("Event created: %s\n", event.getHtmlLink());

//            try {
//                mService.events().update("primary", event.getId(), event).execute();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//            return event;

    }

//    public class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
//
////        private com.google.api.services.calendar.Calendar mService = null;
////        //        private com.google.api.services.calendar.Calendar service = null;
////        private Exception mLastError = null;
//
//
//        MakeRequestTask(GoogleAccountCredential credential) {
//            HttpTransport transport = AndroidHttp.newCompatibleTransport();
//            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//            mService = new com.google.api.services.calendar.Calendar.Builder(
//                    transport, jsonFactory, credential)
//                    .setApplicationName("Google Calendar API Android Quickstart")
//                    .build();
//
////            service = new com.google.api.services.calendar.Calendar.Builder(
////                    transport, jsonFactory, credential)
////                    .setApplicationName("Google Calendar API Android Quickstart")
////                    .build();
//
//        }

        /**
         * Background task to call Google Calendar API.
         * @param params no parameters needed for this task.
         */
//        @Override
//        protected List<String> doInBackground(Void... params) {
//            try {
//                insertEvent(mService);
////                return getDataFromApi();
//                return null;
//            } catch (Exception e) {
//                mLastError = e;
//                cancel(true);
//                return null;
//            }
//        }

        /**
         * Fetch a list of the next 10 events from the primary calendar.
         * @return List of Strings describing returned events.
         * @throws IOException
         */
//        private List<String> getDataFromApi() throws IOException {
//
//
////            insertEvent(mService);
//
//            // List the next 15 events from the primary calendar.
//            DateTime now = new DateTime(System.currentTimeMillis());
//            List<String> eventStrings = new ArrayList<String>();
//            Events events = mService.events().list("primary")
//                    .setMaxResults(50)
//                    .setOrderBy("startTime")
//                    .setSingleEvents(true)
//                    .execute();
//            List<Event> items = events.getItems();
//
////            items.add(insertEvent(mService));
//
//
//
////            items.add(setDateToApi());
//
//            for (Event event : items) {
//                DateTime start = event.getStart().getDateTime();
//                DateTime end = event.getEnd().getDateTime();
//                if (start == null) {
//                    // All-day events don't have start times, so just use
//                    // the start date.
//                    start = event.getStart().getDate();
//                    end = event.getEnd().getDate();
//                }
//                eventStrings.add(
//                        String.format("%s (%s ~ %s)", event.getSummary(), start,end));
//            }
//            return eventStrings;
//        }
//
////        private Event setDateToApi()
////        {
////
////            Event event = new Event()
////                    .setSummary("Google I/O 2015")
////                    .setLocation("800 Howard St., San Francisco, CA 94103")
////                    .setDescription("A chance to hear more about Google's developer products.");
////
////            DateTime startDateTime = new DateTime("2017-05-28T09:00:00-07:00");
////            EventDateTime start = new EventDateTime()
////                    .setDateTime(startDateTime)
////                    .setTimeZone("Korea/Seoul");
////            event.setStart(start);
////
////            DateTime endDateTime = new DateTime("2017-05-28T17:00:00-07:00");
////            EventDateTime end = new EventDateTime()
////                    .setDateTime(endDateTime)
////                    .setTimeZone("Korea/Seoul");
////            event.setEnd(end);
////
////            String calendarId = "primary";
////            event = service.events().insert(calendarId, event).execute();
////
////            return event;
////        }

//        public void insertEvent(com.google.api.services.calendar.Calendar mService) throws IOException {
//
////            HttpTransport transport = AndroidHttp.newCompatibleTransport();
////            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
////            com.google.api.services.calendar.Calendar service = new com.google.api.services.calendar.Calendar.Builder(
////                    transport, jsonFactory, mCredential)
////                    .setApplicationName("Google Calendar API Android Quickstart")
////                    .build();
//
////            Toast toast = Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_SHORT);
////            toast.show();
//
//            try {
//                com.google.api.services.calendar.model.Calendar calendar =
//                        mService.calendars().get("primary").execute();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            Event event = new Event()
//                    .setSummary(name)
//                    .setLocation("Dhaka")
//                    .setDescription("New Event 1");
//
//            DateTime startDateTime = new DateTime(start);
//            EventDateTime start = new EventDateTime()
//                    .setDateTime(startDateTime)
//                    .setTimeZone("Asia/Seoul");
//            event.setStart(start);
//
//            DateTime endDateTime = new DateTime(end);
//            EventDateTime end = new EventDateTime()
//                    .setDateTime(endDateTime)
//                    .setTimeZone("Asia/Seoul");
//            event.setEnd(end);
//
//            String calendarId = "primary";
//            try {
//                mService.events().insert(calendarId, event).execute();
////                mService.events().update(calendarId,event.getId(),event).execute();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
////            System.out.printf("Event created: %s\n", event.getHtmlLink());
//
////            try {
////                mService.events().update("primary", event.getId(), event).execute();
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//
////            return event;
//
//        }

//        private void updateEvent(GoogleAccountCredential mCredential)
//        {
//
//            HttpTransport transport = AndroidHttp.newCompatibleTransport();
//            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//            com.google.api.services.calendar.Calendar service = new com.google.api.services.calendar.Calendar.Builder(
//                    transport, jsonFactory, mCredential)
//                    .setApplicationName("Google Calendar API Android Quickstart")
//                    .build();
//
//
//            Event event = service.events().get('primary', "eventId").execute();
//
//
//
//            event.setSummary("Appointment at Somewhere");
//
//
//            Event updatedEvent = service.events().update("primary", event.getId(), event).execute();
//
//            System.out.println(updatedEvent.getUpdated());
//        }


//        @Override
//        protected void onPreExecute() {
//            mOutputText.setText("");
//            mProgress.show();
//        }

//        @Override
//        protected void onPostExecute(List<String> output) {
//            mProgress.hide();
//            if (output == null || output.size() == 0) {
//                mOutputText.setText("No results returned.");
//            } else {
//                output.add(0, "Data retrieved using the Google Calendar API:");
//                mOutputText.setText(TextUtils.join("\n", output));
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mProgress.hide();
//            if (mLastError != null) {
//                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
//                    showGooglePlayServicesAvailabilityErrorDialog(
//                            ((GooglePlayServicesAvailabilityIOException) mLastError)
//                                    .getConnectionStatusCode());
//                } else if (mLastError instanceof UserRecoverableAuthIOException) {
//                    startActivityForResult(
//                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
//                            GoogleCalendarActivity.REQUEST_AUTHORIZATION);
//                } else {
//                    mOutputText.setText("The following error occurred:\n"
//                            + mLastError.getMessage());
//                }
//            } else {
//                mOutputText.setText("Request cancelled.");
//            }
//        }

//    }

}