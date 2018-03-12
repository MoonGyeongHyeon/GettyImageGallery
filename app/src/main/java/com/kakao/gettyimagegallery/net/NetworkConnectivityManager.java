package com.kakao.gettyimagegallery.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by khan.moon on 2018. 3. 12..
 */

public class NetworkConnectivityManager {

    private static NetworkConnectivityManager instance = new NetworkConnectivityManager();

    private ConnectivityManager connectivityManager;

    private NetworkConnectivityManager() {
    }

    public static NetworkConnectivityManager getInstance() {
        return instance;
    }

    public void init(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public boolean checkNetworkConnected() {
        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            return false;
        }

        return networkInfo.isConnected();
    }

    public boolean isConnected() {
        return checkNetworkConnected();
    }
}
