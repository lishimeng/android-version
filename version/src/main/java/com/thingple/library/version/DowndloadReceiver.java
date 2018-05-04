package com.thingple.library.version;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class DowndloadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DownloadManager manager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager == null) {
            return;
        }
        if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())){
            DownloadManager.Query query = new DownloadManager.Query();
            //在广播中取出下载任务的id
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            query.setFilterById(id);
            Cursor c = manager.query(query);
            if(c.moveToFirst()) {
                //获取文件下载路径
                Uri uri = getDownloadFile(context, c);
                if (uri != null) {
                    VersionManager.shareInstance().onAppDownloaded(id, uri);
                }
            }
        }else if(DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.getAction())) {
            long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
            //点击通知栏取消下载
            manager.remove(ids);
            Toast.makeText(context, "已经取消下载", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getDownloadFile(Context context, Cursor c) {
        int fileUriIdx = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
        String fileUri = c.getString(fileUriIdx);
        String filename = null;
        Uri uri = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (fileUri != null) {
                uri = Uri.parse(fileUri);
            }
        } else {
            int fileNameIdx = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
            filename = c.getString(fileNameIdx);
            uri = Uri.fromFile(new File(filename));
        }

        Toast.makeText(context, "下载:" + filename, Toast.LENGTH_SHORT).show();
        Log.d("Download", "下载:" + filename);
        return uri;
    }
}
