package org.ssutown.manna;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ssutown.manna.meeting.meetingList;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Jiyeon on 2017-03-25.
 */

public class MainActivity extends Activity {

    FragmentManager fm = getFragmentManager();

    HomeFragment homeFragment = new HomeFragment();
    PersonFragment personFragment = new PersonFragment();
    MeetingFragment meetingFragment = new MeetingFragment();
    ScheduleFragment scheduleFragment = new ScheduleFragment();
    SettingFragment settingFragment = new SettingFragment();

    long userID = 398410773;
    ArrayList<String> meetinglist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("userList").child(String.valueOf(userID)).child("personalMeetingList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot user : dataSnapshot.getChildren()) {
                    Log.d(TAG, "userlist onDataChange: " + user.getValue(meetingList.class).getMeetingID());
                    meetinglist.add(user.getValue(meetingList.class).getMeetingID());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getFragmentManager().beginTransaction().add(R.id.main_fragment, homeFragment).commit();

    }

    public long getUserID(){
        Intent intent = getIntent();
        return intent.getExtras().getLong("userID");
    }

    public void login(){
        Intent loginIntent = new Intent(getApplicationContext(), org.ssutown.manna.LoginActivity.class);
        startActivityForResult(loginIntent,0);
    }


    public void ChangeFragment(View view){

        if(view == findViewById(R.id.button0)){
            fm.beginTransaction().replace(R.id.main_fragment, personFragment).commit();
        } else if(view == findViewById(R.id.button2)) {
            fm.beginTransaction().replace(R.id.main_fragment, homeFragment).commit();
        }else if(view == findViewById(R.id.button3)){
            fm.beginTransaction().replace(R.id.main_fragment, scheduleFragment).commit();
        } else if (view == findViewById(R.id.button4)) {
            fm.beginTransaction().replace(R.id.main_fragment, settingFragment).commit();
        }
        else if(view == findViewById(R.id.button1)){
            fm.beginTransaction().replace(R.id.main_fragment, meetingFragment).commit();
        }
//        FragmentManager fm = getFragmentManager();
//
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        fragmentTransaction.detach(pre_fr);
//        fragmentTransaction.replace(R.id.main_fragment, fr);
//        fragmentTransaction.commit();



    }
}
