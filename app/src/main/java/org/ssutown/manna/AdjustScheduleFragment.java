package org.ssutown.manna;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ssutown.manna.AdjstmentMeeting.BasicActivity;
import org.ssutown.manna.Meeting_details.AnnouncementFragment;

public class AdjustScheduleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        View view = inflater.inflate( R.layout.adjustschedule_fragment, container, false );
        Log.i("i'm personfragment","layout_main");

        if (savedInstanceState == null) {
            Intent intent = new Intent(getActivity(), BasicActivity.class);
            startActivity(intent);
        }

        Fragment fr = new Fragment();
        fr = new AnnouncementFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.meet_fragment, fr);
        fragmentTransaction.commit();

        return view;

    }

}