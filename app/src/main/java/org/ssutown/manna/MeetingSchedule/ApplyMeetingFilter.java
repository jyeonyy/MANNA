package org.ssutown.manna.MeetingSchedule;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import org.ssutown.manna.R;

/**
 * Created by YNH on 2017. 5. 30..
 */

public class ApplyMeetingFilter extends Activity{


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.meeting_filter);

        NumberPicker numPicker = (NumberPicker)findViewById(R.id.person_num);

        numPicker.setMinValue(2);

        //나중에 최대값은 임시디비에서 가져올게욥

        numPicker.setMaxValue(5);

        numPicker.setWrapSelectorWheel(false);

        View.OnClickListener listener = new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                finish();
            }

        };

        Button filterBnt = (Button)findViewById(R.id.apply_btn);
        filterBnt.setOnClickListener(listener);

    }


}
