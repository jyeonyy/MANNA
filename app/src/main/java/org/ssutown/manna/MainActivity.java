package org.ssutown.manna;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ssutown.manna.meeting.User;
import org.ssutown.manna.meeting.meetingList;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

/**
 * Created by Jiyeon on 2017-03-25.
 */

public class MainActivity extends Activity {

    FragmentManager fm = getFragmentManager();

    HomeFragment homeFragment = new HomeFragment();

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences login_State = getSharedPreferences("login_State", Activity.MODE_PRIVATE);
        if(login_State.getBoolean("login_State",false)){

            login();
        }

        getInvitation();

        getFragmentManager().beginTransaction().add(R.id.main_fragment, homeFragment).commit();

    }

    public long getUserID(){
        SharedPreferences KAKAO_ID = getSharedPreferences("login", Activity.MODE_PRIVATE);
        return KAKAO_ID.getLong("KAKAO_ID",0);
    }

    public void login(){
        Intent loginIntent = new Intent(getApplicationContext(), org.ssutown.manna.LoginActivity.class);
        startActivityForResult(loginIntent,0);
    }

    public void getInvitation(){

        final String meeting_id;
        String meeting_name;

        if(getIntent() != null){
            Uri uri = getIntent().getData();
            if(uri != null){
                Log.d(TAG, "invitation : " + uri.getQueryParameter("meeting_info"));
                String[] meeting = uri.getQueryParameter("meeting_info").split(Pattern.quote(":"));
                meeting_id = meeting[0];
                meeting_name = meeting[1];

                AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
                alertdialog.setMessage(meeting_name+"으로 초대되었습니다\n 가입을 원하십니까?");
                alertdialog.setPositiveButton("가입", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference();

                        User user = new User(String.valueOf(getUserID()));
                        databaseReference.child("MeetingDetails").child(meeting_id).child("Users").push().setValue(user);
                    }
                });

                alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = alertdialog.create();
                alert.setTitle("MANNA");
                alert.show();

            }
        }


    }


    public void ChangeFragment(View view){

        if(view == findViewById(R.id.button0)){
            PersonFragment personFragment = new PersonFragment();
            fm.beginTransaction().replace(R.id.main_fragment, personFragment).commit();
        } else if(view == findViewById(R.id.button2)) {
            fm.beginTransaction().replace(R.id.main_fragment, homeFragment).commit();
        }else if(view == findViewById(R.id.button3)){
            ScheduleFragment scheduleFragment = new ScheduleFragment();
            fm.beginTransaction().replace(R.id.main_fragment, scheduleFragment).commit();
        } else if (view == findViewById(R.id.button4)) {
            SettingFragment settingFragment = new SettingFragment();
            fm.beginTransaction().replace(R.id.main_fragment, settingFragment).commit();
        }
        else if(view == findViewById(R.id.button1)){
            MeetingFragment meetingFragment = new MeetingFragment();
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
