package org.ssutown.manna;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.ssutown.manna.CustomCalendar.MaterialCalendarFragment;

/**
 * Created by YNH on 2017. 5. 9..
 */

public class SelectCalendar extends Activity {

    int cal_num;
    Boolean select = false;
    MaterialCalendarFragment cal_frag;
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.layout_select_calendar);

        cal_frag = new MaterialCalendarFragment();

        Button gCalBtn = (Button)findViewById(R.id.select_googleCal);
        Button aCalBtn = (Button)findViewById(R.id.select_androidCal);

        View.OnClickListener listener = new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                switch (v.getId()){
                    case R.id.select_googleCal :
                        cal_num = 1;
                        sendInfo(cal_num);
                        finish();
                        break;
                    case R.id.select_androidCal :
                        cal_num = 2;
                        sendInfo(cal_num);
                        finish();
                        break;
                }
            }

        };

        gCalBtn.setOnClickListener(listener);
        aCalBtn.setOnClickListener(listener);

    }

    public int getCalNum()
    {
        return cal_num;
    }

    public void setCalNum(int num)
    {
        this.cal_num = num;
    }

    public void sendInfo(int cal_num){
        SharedPreferences selectedCalendar = getSharedPreferences("selectedCalendar", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = selectedCalendar.edit();
        editor.putInt("cal_num",cal_num);
        editor.commit();
    }
}
