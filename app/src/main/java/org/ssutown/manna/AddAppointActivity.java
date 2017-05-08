package org.ssutown.manna;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by YNH on 2017. 5. 7..
 */

public class AddAppointActivity extends Activity {

    @Override
    protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.layout_add_appoint);

    }

//    protected void addOnClick(){
//
//        Toast toast = Toast.makeText(this,"hi",Toast.LENGTH_SHORT);
//        toast.show();
//
//        Intent intent = new Intent(this,AddAppointActivity.class);
//        startActivity(intent);
//    }
}