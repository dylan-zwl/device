package com.tapc.platform.ui.activity.start;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.tapc.platform.R;
import com.tapc.platform.ui.fragment.selectmode.SelectModeFragment;

public class StartActivity extends Activity {
    protected static FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mFragmentManager = getFragmentManager();
        replaceFragment(R.id.start_mode_fragment, Fragment.instantiate(this, SelectModeFragment.class.getName()),
                mFragmentManager);
    }

    public static void replaceFragment(int id, Fragment fragment, FragmentManager fragmentManager) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
//        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        ft.replace(id, fragment);
        ft.commit();
    }

    public static void replaceFragment(int id, Fragment fragment) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
//        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        ft.replace(id, fragment);
        ft.commit();
    }

}
