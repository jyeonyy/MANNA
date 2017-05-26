package org.ssutown.manna.meeting;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.ssutown.manna.R;
import org.ssutown.manna.meeting.*;
import org.ssutown.manna.userInfo;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.ssutown.manna.meeting.*;
import com.google.firebase.database.ValueEventListener;
import org.ssutown.manna.userInfo;

public class add_Meeting extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 2;
    static final int REQUEST_IMAGE_CROP = 3;

    userInfo userInfo;

    long meetingRoomID;

    EditText meetingName;
    DatePicker startDay;
    DatePicker endDay;
    Button addMeeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__meeting);

        meetingName = (EditText) findViewById(R.id.meeting_name);
        startDay = (DatePicker)findViewById(R.id.startDay);
        endDay = (DatePicker)findViewById(R.id.endDay);
        addMeeting = (Button)findViewById(R.id.add);

        //미팅추가하기
        addMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final meeting_Info meeting_info = new meeting_Info(meetingName.getText().toString(),startDay.getYear(),startDay.getMonth(),startDay.getDayOfMonth(),endDay.getYear(),endDay.getMonth(),endDay.getDayOfMonth());
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference();
                databaseReference.child("MeetingList").push().setValue(meeting_info);

                SharedPreferences kakao_id = getSharedPreferences("KAKAO_ID", Activity.MODE_PRIVATE);
                final long userID = kakao_id.getLong("KAKAO_ID",0);

                //Toast.makeText(getApplication(),String.valueOf(userID),Toast.LENGTH_SHORT).show();
                //String key = databaseReference.child("userList").child(String.valueOf(userID)).push().getKey();

                meetingList meetingList = new meetingList(meeting_info.getMeeting_id());

                databaseReference.child("userList").child(String.valueOf(userID)).child("personalMeetingList").push().setValue(meetingList);

                //databaseReference.child("userList").child(String.valueOf(userID)).
                //Toast.makeText(getApplication(),key,Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }




}
