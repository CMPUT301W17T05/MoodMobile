package com.example.moodmobile;

import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * Created by zindi on 3/16/17.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    ContentResolver contentResolver;

    public SyncAdapter(Context context, boolean autoInitialize){
        super(context, autoInitialize);

        contentResolver = context.getContentResolver();
    }

    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs){
        super(context, autoInitialize, allowParallelSyncs);

        contentResolver = context.getContentResolver();
    }

    @Override
    protected void onPerformSync(Account account, Bundle bundle, String string, ContentProviderClient client, SyncResult result){

    }

}
