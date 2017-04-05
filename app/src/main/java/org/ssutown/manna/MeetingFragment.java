package org.ssutown.manna;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MeetingFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View view = inflater.inflate( R.layout.meeting_fragment, container, false );


        Button button = (Button) view.findViewById(R.id.meetingroom);   //selectroom
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), MeetingActivity.class);
                FragmentManager fragmentManager = getFragmentManager();

                startActivity(i);

             }
        });


        return view;
    }





}