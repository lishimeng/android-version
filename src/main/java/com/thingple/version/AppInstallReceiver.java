package com.thingple.version;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class AppInstallReceiver extends BroadcastReceiver {

    private static String TAG = AppInstallReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "有APP改动");
        String action = intent.getAction();
        Uri data = intent.getData();
        if (action == null || data == null) {
            return;
        }
        String packageName = data.getSchemeSpecificPart();
        Log.d(TAG, "action:" + action);
        Log.d(TAG, "package name:" + packageName);
        if (action.equals(Intent.ACTION_PACKAGE_ADDED) || action.equals(Intent.ACTION_PACKAGE_REPLACED)) {
            alertInstallComplete(packageName);
        }
    }

    private void alertInstallComplete(String packageName) {
        VersionManager versionManager = VersionManager.shareInstance();
        versionManager.appInstalled(packageName);
    }
}
