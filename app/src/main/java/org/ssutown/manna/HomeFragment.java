package org.ssutown.manna;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {

  //  private ImageView kakaoprofile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

      //  kakaoprofile = (ImageView)getView().findViewById(R.id.circle);
       // kakaoprofile.setBackground(new ShapeDrawable(new OvalShape()));
       // kakaoprofile.setClipToOutline(true);
        return inflater.inflate(R.layout.home_fragment, container, false);


    }
}