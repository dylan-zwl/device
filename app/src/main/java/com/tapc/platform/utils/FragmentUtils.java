package com.tapc.platform.utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

/**
 * Created by Administrator on 2017/9/6.
 */

public class FragmentUtils {
    public static void replaceFragment(Context context, FragmentManager fragmentManager, int id, Class<?> cls) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
//        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        ft.replace(id, Fragment.instantiate(context, cls.getName()));
        ft.commit();
    }

    public static void replaceFragment(Context context, FragmentManager fragmentManager, int id, Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(id, fragment);
        ft.commit();
    }
}
