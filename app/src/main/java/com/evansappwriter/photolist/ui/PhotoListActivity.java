package com.evansappwriter.photolist.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.evansappwriter.photolist.R;
import com.evansappwriter.photolist.util.Utils;

/**
 * Created by markevans on 6/20/16.
 */
public class PhotoListActivity extends BaseActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {

        Bundle b = getIntent().getExtras();

        int code = (getClass().getName() + "MovieListFragment").hashCode();
        Utils.printLogInfo("FRAG", "id: ", code);

        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentByTag("f" + code) == null) // first time in the activity
        {
            Utils.printLogInfo("FRAG", 'f', code);
            Fragment f = PhotoListFragment.newInstance(b);

            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.placeholder, f, "f" + code);
            ft.commit();
        }
    }
}
