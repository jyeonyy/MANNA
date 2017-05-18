package org.ssutown.manna;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import org.ssutown.manna.GoogleCalendar.GoogleCalendarActivity;

import java.text.SimpleDateFormat;

/**
 * Created by YNH on 2017. 5. 7..
 */

public class AddAppointActivity extends Activity {

    @Override
    protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.layout_add_appoint);

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

                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss");

                final EditText name = (EditText)findViewById(R.id.AppointName);

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

                String start = new StringBuilder()
                        .append(startDate)
                        .append('T')
                        .append(startTime)
                        .append("+09:00")
                        .toString();
                String end = new StringBuilder()
                        .append(endDate)
                        .append('T')
                        .append(endTime)
                        .append("+09:00")
                        .toString();

                Intent intent = new Intent(getApplicationContext(), GoogleCalendarActivity.class);

                intent.putExtra("NameOfAppoint",name.getText().toString());
                intent.putExtra("Start",start);
                intent.putExtra("End",end);

                startActivity(intent);


//                Toast toast = Toast.makeText(getApplicationContext(),start,Toast.LENGTH_SHORT);
//                toast.show();



            }
        };

        Button addBtn = (Button)findViewById(R.id.addBtn);
        addBtn.setOnClickListener(listener);

    }

//    protected int getHour(int baseLine,int hour)
//    {
//        if(baseLine == 1900)
//            return 1;
//    }

}