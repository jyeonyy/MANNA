package org.ssutown.manna;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.ssutown.manna.CustomCalendar.MaterialCalendarFragment;

/**
 * Created by YNH on 2017. 5. 9..
 */

public class SelectCalendar extends Activity {

    int cal_num;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.layout_select_calendar);

        MaterialCalendarFragment cal_frag = new MaterialCalendarFragment();
        Bundle num = new Bundle();
        num.putInt("num",getCalNum());
        cal_frag.setArguments(num);

        Button gCalBtn = (Button)findViewById(R.id.select_googleCal);
        Button oCalBtn = (Button)findViewById(R.id.select_outlookCal);
        Button aCalBtn = (Button)findViewById(R.id.select_androidCal);

        View.OnClickListener listener = new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                switch (v.getId()){
                    case R.id.select_googleCal :
                        cal_num = 1;
                        finish();
                        break;
                    case R.id.select_outlookCal :
                        cal_num = 2;
                        finish();
                        break;
                    case R.id.select_androidCal :
                        cal_num = 3;
                        finish();
                        break;
                }


            }

        };

        gCalBtn.setOnClickListener(listener);
        oCalBtn.setOnClickListener(listener);
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
}
