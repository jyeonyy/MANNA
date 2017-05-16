package org.ssutown.manna;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {
  //  private ImageView kakaoprofile;
    long userID;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference memodatabase = database.getReference("Memo");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate( R.layout.home_fragment, container, false );

        userID = 1111;
     //  userID = getUserID();

        final org.ssutown.manna.MemoListAdapter adapter;
        adapter = new org.ssutown.manna.MemoListAdapter();
        final ListView listview = (ListView)view.findViewById(R.id.memolistview);
        listview.setAdapter(adapter);


       memodatabase.child(String.valueOf(userID)).addValueEventListener(new ValueEventListener() {
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
                String key = memodatabase.child(String.valueOf(userID)).push().getKey();
                memodatabase.child(String.valueOf(userID)).child(key).setValue(memo);

                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editmemo.getWindowToken(), 0);

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

    public long getUserID(){
        return ((MainActivity)getActivity()).getUserID();
    }


}