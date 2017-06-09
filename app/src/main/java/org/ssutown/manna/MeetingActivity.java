package org.ssutown.manna;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.ssutown.manna.Meeting_details.AnnouncementFragment;

/**
 * Created by Jiyeon on 2017-03-25.
 */

public class MeetingActivity extends Activity {

    String meeting_id;
    String meeting_name;
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_meeting);

        Intent i = getIntent();
        meeting_id = i.getExtras().getString("meetingId");
        meeting_name = i.getExtras().getString("meetingName");


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