package org.ssutown.manna.CustomCalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.ssutown.manna.R;

import java.util.ArrayList;

/**
 * Created by Jiyeon on 2017-04-16.
 */

public class CalendarEventsAdapter extends BaseAdapter {
    static ArrayList<CalendarEventItem> listViewItemList = new ArrayList<CalendarEventItem>();

    public CalendarEventsAdapter(){} @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_material_saved_event_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        final TextView mTitle = (TextView) convertView.findViewById(R.id.saved_event_title_textView);
        final TextView mAbout = (TextView) convertView.findViewById(R.id.saved_event_about_textView);
        final ImageView mImageView = (ImageView) convertView.findViewById(R.id.saved_event_imageView);
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final CalendarEventItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        mTitle.setText(listViewItem.getTitle());
        mAbout.setText(listViewItem.getAbout());
        mImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_calendar));
    /*    convertView.findViewById(R.id.memodelete).setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listViewItemList.remove(pos);

                    }
                }
        );
*/


        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.

    public void addItem(String title, String about) {
        CalendarEventItem item = new CalendarEventItem(title, about);
        item.setEvent(title, about);
        listViewItemList.add(item);
    }


    public void clear(){
        listViewItemList.clear();
    }

}