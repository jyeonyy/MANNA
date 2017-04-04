package org.ssutown.manna;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * Created by Jiyeon on 2017-03-25.
 */

public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

        login();

    }

    public void login(){
        Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivityForResult(loginIntent,0);
    }



    public void ChangeFragment(View view){
        Fragment fr = new Fragment();
        if(view == findViewById(R.id.button0)){
            fr = new PersonFragment();

        }else if(view == findViewById(R.id.button1)){
            fr = new MeetingFragment();

        }else if(view == findViewById(R.id.button2)) {
            fr = new HomeFragment();

        }else if(view == findViewById(R.id.button3)){
            fr = new ScheduleFragment();
        } else if (view == findViewById(R.id.button4)) {
            fr = new SettingFragment();
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, fr);
        fragmentTransaction.commit();
    }
}
