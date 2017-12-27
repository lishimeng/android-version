package com.thingple.version;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;


/**
 *
 * Created by lism on 2017/12/7.
 */

class ApplicationHandler {

    private Context context;
    private DownloadManager downloadManager;

    ApplicationHandler(Context context) {
        this.context = context;
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    /**
     * 安装APP<br/>
     * APK需要预先存储在本地文件中
     * @param uri apk
     */
    void install(Uri uri) {

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        String applicationContentType = "application/vnd.android.package-archive";
        i.setDataAndType(uri, applicationContentType);// 启动安装器
        context.startActivity(i);
    }

    void clearInstall(long downloadId) {
        downloadManager.remove(downloadId);
    }

    /**
     * @param uri url
     * @param title notification title
     * @param description notification desc
     * @return download id
     */
    long download(String uri, String title, String description, String appName) {
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(uri));
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //file:///storage/emulated/0/Android/data/your-package/files/Download/update.apk
        //req.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, appName+".apk");
        req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, appName+".apk");

        // 设置一些基本显示信息
        req.setTitle(title);
        req.setDescription(description);
        return downloadManager.enqueue(req);//异步
    }
}
