package org.ssutown.manna;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ssutown.manna.MeetingSchedule.MeetingCalendarFragment;

public class ScheduleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        View view = inflater.inflate( R.layout.schedule_fragment, container, false );
        Log.i("i'm personfragment","layout_main");

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.main_container, new MeetingCalendarFragment()).commit();
            Log.i("savedinstancestate null","main_container");
        }

        return view;

    }


}
