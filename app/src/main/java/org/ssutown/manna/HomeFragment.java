package org.ssutown.manna;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kakao.util.exception.KakaoException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class HomeFragment extends Fragment {
  //  private ImageView kakaoprofile;
    String userID;
    private KakaoProfile profile;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference memodatabase = database.getReference("Memo");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate( R.layout.home_fragment, container, false );
        userID = "11111";


        final MemoListAdapter adapter;
        adapter = new MemoListAdapter();
        final ListView listview = (ListView)view.findViewById(R.id.memolistview);
        listview.setAdapter(adapter);


       memodatabase.child(userID.toString()).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               adapter.clear();
               for(DataSnapshot ds: dataSnapshot.getChildren()) {
                   adapter.addItem(ds.getValue(MemoListItem.class).getMemo());
               }adapter.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

// Write a message to the database


        final Button addmemo =(Button)view.findViewById(R.id.memoplus);
        addmemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editmemo = (EditText)getView().findViewById(R.id.memoedit);
                MemoListItem memo = new MemoListItem(editmemo.getText().toString());
                String key = memodatabase.child(userID.toString()).push().getKey();
                memodatabase.child(userID.toString()).child(key).setValue(memo);
                editmemo.setText("");

            }

        });

    /*    memodatabase.child("Memolist").child(userID.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot memo : dataSnapshot.getChildren()){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/


        return view;
        //return inflater.inflate(R.layout.home_fragment, container, false);



    }


}