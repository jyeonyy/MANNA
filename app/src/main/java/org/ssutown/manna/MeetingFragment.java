package org.ssutown.manna;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class MeetingFragment extends Fragment {

    static final String[] LIST_MENU = {"LIST1","LIST2","LIST3"};
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

        ArrayAdapter Adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU);

        ListView listview = (ListView)view.findViewById(R.id.listview);
        listview.setAdapter(Adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                // get TextView's Text.
                String strText = (String) parent.getItemAtPosition(position) ;

                Toast.makeText(getActivity().getApplication(),"gg",Toast.LENGTH_SHORT).show();
            }
        }) ;


        return view;
    }





}