package org.ssutown.manna;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.app.Fragment;

/**
 * Created by Jiyeon on 2017-03-25.
 */

public class MainActivity extends Activity{
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void ChangeFragment(View view){
        Fragment fr = new Fragment();
        if(view == findViewById(R.id.button0)){
            fr = new PersonFragment();

        }else if(view == findViewById(R.id.button1)){
            fr = new MeetingFragment();

        }else if(view == findViewById(R.id.button2)){
            fr = new HomeFragment();

        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, fr);
        fragmentTransaction.commit();
    }
}
