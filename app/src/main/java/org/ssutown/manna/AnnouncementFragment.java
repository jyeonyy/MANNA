package org.ssutown.manna;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import static org.ssutown.manna.HomeFragment.userID;

public class AnnouncementFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate( R.layout.announcement_fragment, container, false );

        final org.ssutown.manna.AnnounceListAdapter adapter;
        adapter = new org.ssutown.manna.AnnounceListAdapter();
        final ListView listview = (ListView)view.findViewById(R.id.announcementlist);
        listview.setAdapter(adapter);


        final Context context = getActivity();
        final ImageButton addannounce =(ImageButton) view.findViewById(R.id.addannounce);
        addannounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(context);

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
                        AnnounceListItem announce = new AnnounceListItem(userID, value,"");
                        adapter.addItem(userID,value,"");
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

        return view;
    }
}
