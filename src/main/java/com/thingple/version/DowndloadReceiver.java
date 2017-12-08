package com.thingple.version;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
                String filename = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));

                if(filename != null){
                    Toast.makeText(context, "下载:" + filename, Toast.LENGTH_SHORT).show();
                    Log.d("Download", "下载:" + filename);
                    Uri uri = Uri.fromFile(new File(filename));
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
}
