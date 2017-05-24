package org.ssutown.manna;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Created by Jiyeon on 2017-03-25.
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

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
        Fragment fr = new Fragment();

        if(view == findViewById(R.id.button0)){
            fr = new org.ssutown.manna.PersonFragment();

        } else if(view == findViewById(R.id.button2)) {
            fr = new org.ssutown.manna.HomeFragment();

        }else if(view == findViewById(R.id.button3)){
            fr = new org.ssutown.manna.ScheduleFragment();
        } else if (view == findViewById(R.id.button4)) {
            Log.i("clickbutton4","D");
            fr = new org.ssutown.manna.SettingFragment();

        }
        else if(view == findViewById(R.id.button1)){
            fr = new org.ssutown.manna.MeetingFragment();
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, fr);
        fragmentTransaction.commit();



    }
}
