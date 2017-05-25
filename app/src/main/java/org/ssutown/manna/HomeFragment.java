package org.ssutown.manna;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class HomeFragment extends Fragment {
  //  private ImageView kakaoprofile;
    public static long userID;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference memodatabase = database.getReference("Memo");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate( R.layout.home_fragment, container, false );


        userID = 398410773;
       //userID = getUserID();

        Toast.makeText(getActivity(),"dksjfiowje",Toast.LENGTH_SHORT).show();

        final org.ssutown.manna.MemoListAdapter adapter;
        adapter = new org.ssutown.manna.MemoListAdapter();
        final ListView listview = (ListView)view.findViewById(R.id.memolistview);
        listview.setAdapter(adapter);

       memodatabase.child(String.valueOf(userID)).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
//               MemoListItem memoitem = dataSnapshot.getValue(MemoListItem.class);
               for(DataSnapshot ds: dataSnapshot.getChildren()) {
//                   adapter.addItem(memoitem.getMemo());
                   adapter.addItem(ds.getValue(MemoListItem.class).getMemo(), ds.getValue(MemoListItem.class).getUniquekey());
               }adapter.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
        final Context context = getActivity();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                MemoListItem item = (MemoListItem)parent.getItemAtPosition(position);
                final String string = item.getUniquekey();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // 제목셋팅
                alertDialogBuilder.setTitle("메모 삭제");

                // AlertDialog 셋팅
                alertDialogBuilder
                        .setMessage("메모를 삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("삭제",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // 프로그램을 종료한다
                                        memodatabase.child(String.valueOf(userID)).child(string).setValue(null);

                                    }
                                })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // 다이얼로그를 취소한다
                                        dialog.cancel();
                                    }
                                });

                // 다이얼로그 생성
                AlertDialog alertDialog = alertDialogBuilder.create();

                // 다이얼로그 보여주기
                alertDialog.show();




//                memodatabase.child(String.valueOf(userID)).child(string).setValue(null);


            }
        }) ;

        final ImageButton addmemo =(ImageButton) view.findViewById(R.id.memoplus);
        addmemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editmemo = (EditText)getView().findViewById(R.id.memoedit);
                String key = memodatabase.child(String.valueOf(userID)).push().getKey();

                MemoListItem memo = new MemoListItem(editmemo.getText().toString(), key);

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