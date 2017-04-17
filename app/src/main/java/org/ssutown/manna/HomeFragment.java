package org.ssutown.manna;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {
  //  private ImageView kakaoprofile;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate( R.layout.home_fragment, container, false );


        final MemoListAdapter adapter;
        adapter = new MemoListAdapter();
        ListView listview = (ListView)view.findViewById(R.id.memolistview);
        listview.setAdapter(adapter);

// Write a message to the database

//        DatabaseReference myRef = database.getReference("message");
        final DatabaseReference memodatabase = database.getReference("memo");

//        memodatabase.child("element1").setValue("element1");
//        memodatabase.push();
//        memodatabase.child("daga").setValue("element24");
//        memodatabase.push().setValue("element2");
//        memodatabase.push().setValue("element3");



        int check = 0;
        while(check != 5){
            if(memodatabase.push().getKey() == null)
                break;
            memodatabase.push();
            adapter.addItem(memodatabase.child("daga").getKey());
            check ++;
        }

        Button addmemo =(Button)view.findViewById(R.id.memoplus);
        addmemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editmemo = (EditText)getView().findViewById(R.id.memoedit);
                String memo = editmemo.getText().toString();
                memodatabase.push().setValue(memo);
                adapter.addItem(memo);
            }

        });
        // Read from the database
        /*myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);

                Log.d(value, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });*/


        return view;
        //return inflater.inflate(R.layout.home_fragment, container, false);



    }


}