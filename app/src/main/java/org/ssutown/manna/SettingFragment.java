package org.ssutown.manna;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.ssutown.manna.GoogleCalendar.saveGoogleCalendarActivity;

public class SettingFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        View view = inflater.inflate( R.layout.setting_fragment, container, false );

        Button button = (Button) view.findViewById(R.id.getCalendar1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),saveGoogleCalendarActivity.class);
            startActivity(intent);
            }
        });


        return view;
    }

}
