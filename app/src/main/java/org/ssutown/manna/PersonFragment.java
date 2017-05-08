package org.ssutown.manna;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

public class PersonFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.person_fragment, container, false);
    }

}
