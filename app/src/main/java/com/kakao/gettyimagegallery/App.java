package com.kakao.gettyimagegallery;

import android.app.Application;

import com.kakao.gettyimagegallery.net.NetworkConnectivityManager;

/**
 * Created by khan.moon on 2018. 3. 12..
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        NetworkConnectivityManager.getInstance().init(getApplicationContext());
    }
}
