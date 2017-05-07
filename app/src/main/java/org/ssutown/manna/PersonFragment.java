package org.ssutown.manna;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ssutown.manna.CustomCalendar.MaterialCalendarFragment;

/**
 * Created by Maximilian on 9/1/14.
 */
public class PersonFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        View view = inflater.inflate( R.layout.layout_main, container, false );

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.main_container, new MaterialCalendarFragment()).commit();
        }

        return view;
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


}



