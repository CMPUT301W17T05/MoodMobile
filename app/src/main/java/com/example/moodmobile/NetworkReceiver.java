package com.example.moodmobile;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by zindi on 3/16/17.
 */

public class NetworkReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        Intent service = new Intent(context, SyncService.class);

        /*
        Code Addapted from
        http://stackoverflow.com/questions/31689513/broadcastreceiver-to-detect-network-is-connected
        on Mar 16, 2017
         */
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                Log.i("NetworkReciever", "Connected @ " + SystemClock.elapsedRealtime());
                Log.i("SyncService", "Starting service @ " + SystemClock.elapsedRealtime());
                startWakefulService(context, service);
            } else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
                Log.i("NetworkReciever", "Disconnected @ " + SystemClock.elapsedRealtime());
            }
        }


    }
}
