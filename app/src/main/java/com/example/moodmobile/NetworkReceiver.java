package com.example.moodmobile;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * This class runs in the background is waiting for a change in the device's connection.
 * If connection is newly established it launches a SyncService in order to sync the device with the server.
 * If connection is lost, a log is made and the NetworkReceiver continues to wait.
 * Created by zindi on 3/16/17.
 */
public class NetworkReceiver extends WakefulBroadcastReceiver {
    /**
     * onReceive checks whether a new connection has been established or has been lost.
     * It logs this information.
     * If a new connection has been established it starts a SyncService in order to sync the device with the server.
     * If the connection has been lost the NetworkReciever continues to wait.
     * @param context from the device, needed to call the service.
     * @param intent from the device with information with regards to the connection.
     */
    @Override
    public void onReceive(Context context, Intent intent){
        Intent service = new Intent(context, SyncService.class);

        /*
        Code adapted from
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
