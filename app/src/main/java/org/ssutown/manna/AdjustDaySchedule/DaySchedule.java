package org.ssutown.manna.AdjustDaySchedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.ssutown.manna.R;


/**
 * The launcher activity of the sample app. It contains the links to visit all the example screens.
 * Created by Raquib-ul-Alam Kanak on 7/21/2014.
 * Website: http://alamkanak.github.io
 */
public class DaySchedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_main);

        findViewById(R.id.buttonBasic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DaySchedule.this, BasicActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.buttonAsynchronous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DaySchedule.this, AsynchronousActivity.class);
                startActivity(intent);
            }
        });
    }

}
