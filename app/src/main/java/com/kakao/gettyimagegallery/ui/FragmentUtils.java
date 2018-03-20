package com.kakao.gettyimagegallery.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

/**
 * Created by khan.moon on 2018. 3. 20..
 */

public class FragmentUtils {
    public static final String TAG = "FragmentUtils";

    public static Fragment getLastFragment(FragmentActivity activity) {
        if (activity == null) {
            return null;
        }

        FragmentManager fm = activity.getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();

        if (count == 0) {
            return null;
        }

        FragmentManager.BackStackEntry backStackEntry = fm.getBackStackEntryAt(count - 1);
        return fm.findFragmentByTag(backStackEntry.getName());
    }

    public static void close(FragmentActivity activity, String name, int flag) {
        if (activity == null) {
            return;
        }

        FragmentManager fm = activity.getSupportFragmentManager();
        fm.popBackStackImmediate(name, flag);
    }
}
