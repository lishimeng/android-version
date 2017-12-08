package com.thingple.version

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AppInstallReceiver : BroadcastReceiver() {

    private val TAG = AppInstallReceiver::class.java.simpleName

    override fun onReceive(context: Context, intent: Intent) {

        Log.d(TAG, "有APP改动")
        val action = intent.action
        val data = intent.data
        if (action == null || data == null) {
            return
        }
        val packageName = data.schemeSpecificPart
        Log.d(TAG, "action:" + action)
        Log.d(TAG, "package name:" + packageName)
        if (action == Intent.ACTION_PACKAGE_ADDED || action == Intent.ACTION_PACKAGE_REPLACED) {
            alertInstallComplete(packageName)
        }
    }

    private fun alertInstallComplete(packageName: String) {
        val versionManager = VersionManager.shareInstance()
        versionManager.appInstalled(packageName)
    }
}
