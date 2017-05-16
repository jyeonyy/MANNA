package org.ssutown.manna;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by YNH on 2017. 5. 7..
 */

public class AddAppointActivity extends Activity {

    @Override
    protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.layout_add_appoint);

        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //ADD Button 클릭시 이벤트 추가

            }
        };

        Button addBtn = (Button)findViewById(R.id.addBtn);
        addBtn.setOnClickListener(listener);

    }

}