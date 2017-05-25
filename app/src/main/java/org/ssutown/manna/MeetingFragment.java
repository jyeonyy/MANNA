package org.ssutown.manna;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.solver.SolverVariable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import org.ssutown.manna.meeting.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MeetingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View view = inflater.inflate( R.layout.meeting_fragment, container, false );

        final ListViewAdapter adapter;
        adapter = new ListViewAdapter();
        ListView listview = (ListView)view.findViewById(R.id.listview);
        listview.setAdapter(adapter);

        final ArrayList<String> meetinglist = new ArrayList<>();

        Button addMeeting = (Button)view.findViewById(R.id.addmeeting);

        //long userID = ((MainActivity)getActivity()).getUserID();
        long userID = 398410773;

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("userList").child(String.valueOf(userID)).child("personalMeetingList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d(TAG, "userlist onDataChange: " + ds.getValue(meetingList.class).getMeetingID());
                    meetinglist.add(ds.getValue(meetingList.class).getMeetingID());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



       addMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Toast.makeText(getActivity(),"addButton",Toast.LENGTH_SHORT).show();
               Intent i = new Intent(getActivity(), add_Meeting.class);
                startActivity(i);
            }
        });


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