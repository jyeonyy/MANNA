package org.ssutown.manna.Meeting_details;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ssutown.manna.MeetingActivity;
import org.ssutown.manna.R;

import static org.ssutown.manna.HomeFragment.userID;


public class AnnouncementFragment extends Fragment {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference databaseReference = firebaseDatabase.getReference();

    String meeting_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        Intent intent = getActivity().getIntent();
        meeting_id = intent.getExtras().getString("meetingId");

        View view = inflater.inflate( R.layout.announcement_fragment, container, false );

        final AnnounceListAdapter adapter;
        adapter = new AnnounceListAdapter();
        final ListView listview = (ListView)view.findViewById(R.id.announcementlist);
        listview.setAdapter(adapter);

        final Context context = getActivity();
        final ImageButton addannounce =(ImageButton) view.findViewById(R.id.addannounce);



        addannounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder ad = new AlertDialog.Builder(context);

                ad.setTitle("공지사항 추가");       // 제목 설정
                ad.setMessage("공지사항 내용 입력");   // 내용 설정

// EditText 삽입하기
                final EditText et = new EditText(context);
                ad.setView(et);

// 확인 버튼 설정
                ad.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Text 값 받아서 로그 남기기
                        String value = et.getText().toString();
                        AnnounceListItem announce = new AnnounceListItem(userID, value);
                        Toast.makeText(getActivity(),announce.getContent(),Toast.LENGTH_SHORT).show();
                        databaseReference.child("MeetingDetails").child(meeting_id).child("Announcement").push().setValue(announce);
                        //adapter.addItem(userID,value);
                        //adapter.notifyDataSetChanged();
                        // Event
                    }
                });

// 취소 버튼 설정
                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });

// 창 띄우기
                ad.show();
            }

        });

        databaseReference.child("MeetingDetails").child(meeting_id).child("Announcement").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    adapter.addItem(ds.getValue(AnnounceListItem.class).getUserID(),ds.getValue(AnnounceListItem.class).getContent());
                }adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }

/*    public void addAnnouncementToFB(AnnounceListItem announceListItem){
        Toast.makeText(getActivity(),((MeetingActivity)getActivity()).getMeeting_id(),Toast.LENGTH_SHORT).show();
        databaseReference.child("MeetingDetails").child(((MeetingActivity)getActivity()).getMeeting_id()).child("Announcement").push().setValue(announceListItem);
    }*/
}
