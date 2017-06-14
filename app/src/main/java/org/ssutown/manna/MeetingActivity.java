package org.ssutown.manna;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ssutown.manna.Meeting_details.AnnouncementFragment;
import org.ssutown.manna.meeting.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jiyeon on 2017-03-25.
 */

public class MeetingActivity extends Activity {

    String meeting_id;
    String meeting_name;

    FirebaseDatabase database1 = FirebaseDatabase.getInstance();
    DatabaseReference MeetingDetails = database1.getReference("MeetingDetails");
    private static ArrayList<User> userlist;



    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_meeting);

        Intent i = getIntent();
        meeting_id = i.getExtras().getString("meetingId");
        meeting_name = i.getExtras().getString("meetingName");

        userlist = new ArrayList<User>();
        MeetingDetails.child("201706100136337400").child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userlist.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    userlist.add(ds.getValue(User.class));
                    Log.i("userinfo", String.valueOf(userlist.size()));
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static ArrayList<User> getUserlist() {
        return userlist;
    }

    public String getMeeting_id(){
        return meeting_id;
    }
    public void ChangeMeetFragment(View view){
        Fragment fr = new Fragment();
        if(view == findViewById(R.id.button10)){
            fr = new AnnouncementFragment();
        }
        else if(view == findViewById(R.id.button11)){
            fr = new AdjustScheduleFragment();

        }
        else if(view == findViewById(R.id.button12)){
            fr = new SettingMeetFragment();
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.meet_fragment, fr);
        fragmentTransaction.commit();

    }



}