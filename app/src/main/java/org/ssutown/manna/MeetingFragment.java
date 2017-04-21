package org.ssutown.manna;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class MeetingFragment extends Fragment {

    static final String[] LIST_MENU = {"LIST1","LIST2","LIST3"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View view = inflater.inflate( R.layout.meeting_fragment, container, false );



        ListViewAdapter adapter;
        adapter = new ListViewAdapter();
        ListView listview = (ListView)view.findViewById(R.id.listview);
        listview.setAdapter(adapter);

        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.calendar1), "first");
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.home1), "second");
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.person3), "third");


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                ListViewItem item = (ListViewItem)parent.getItemAtPosition(position);
                String titleStr = item.getTitle();
               // Toast.makeText(getActivity().getApplication(),"gg",Toast.LENGTH_SHORT).show();

                if(titleStr.equals("first") == true){
                    Intent i = new Intent(getActivity(), MeetingActivity.class);
                    startActivity(i);

                }else if(titleStr.equals("second") == true){
                    Intent i = new Intent(getActivity(), MeetingActivity.class);
                    startActivity(i);

                }else if(titleStr.equals("third") == true){
                    Intent i = new Intent(getActivity(), MeetingActivity.class);
                    startActivity(i);

                }

            }
        }) ;


        return view;
    }





}