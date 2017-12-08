package com.thingple.version;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.thingple.version.components.AppComponent;
import com.thingple.version.components.AppInfoListener;
import com.thingple.version.components.ComponentsHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 版本控制
 */
public class VersionManager {

    private static String TAG = VersionManager.class.getSimpleName();

    private static VersionManager ins;
    private Context context;

    private ApplicationHandler applicationHandler;

    private Map<String, AppComponent> packageCacke = new HashMap<>();
    private Map<Long, String> downloadList = new HashMap<>();
    private Map<String, Uri> completeList = new HashMap<>();
    private boolean installing = false;

    public VersionManager(Context context) {
        this.context = context;
        applicationHandler = new ApplicationHandler(context);
    }

    public static VersionManager shareInstance() {
        return ins;
    }

    public static void init(Context context) {
        if (ins == null) {
            ins = new VersionManager(context);
        }
    }

    public void getUpdateInfo(AppInfoListener listener) {
        installing = false;
        ComponentsHandler componentsHandler = new ComponentsHandler(context);
        componentsHandler.getGlobalConfig(listener);
    }

    public void updateComponents(List<AppComponent> components) {
        if (components != null) {
            for (AppComponent component : components) {
                updateComponent(component);
            }
        }
    }

    private void updateComponent(AppComponent component) {
        String packageName = component.packageName;
        int version = component.version;
        Log.d(TAG, packageName + ":" + version);
        Version currentVersion = getVersion(getPackage(packageName));
        if (currentVersion == null || currentVersion.versionCode < version) {
            download(component);
        }
    }

    /**
     * 检查版本号
     * @return version
     */
    public Version getVersion(PackageInfo packageInfo) {

        Version version = null;
        if (packageInfo != null) {
            version = new Version();
            String[] subs = packageInfo.versionName.split("\\.");
            if (subs.length > 1) {
                version.major = parseInt(subs[0]);
                version.minor = parseInt(subs[1]);
            }
            version.versionCode = packageInfo.versionCode;
            version.versionName = packageInfo.versionName;
        }
        return version;
    }

    private static int parseInt(String value) {
        Integer number = 0;

        try {
            number = Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return number;
    }


    /**
     * 检查package安装情况
     * @param packageName package
     * @return app的安装包信息,未安装时返回null
     */
    public PackageInfo getPackage(String packageName) {
        PackageInfo packageInfo = null;
        PackageManager pm = context.getPackageManager();
        try {
            packageInfo = pm.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG + "#getPackage", "APP未安装:" + packageName);
        }
        return packageInfo;
    }

    public void download(AppComponent app) {
        packageCacke.put(app.packageName, app);
        String title = "download component";
        String desc = "downloading " + app.name;
        long downloadId = applicationHandler.download(app.uri, title, desc, app.name);
        downloadList.put(downloadId, app.packageName);
    }

    private void installApp() {
        Log.d(TAG, "检查是否有需要安装的app");
        if (!completeList.isEmpty()) {
            Set<String> packages = completeList.keySet();
            Uri apk = null;
            for (String packageName : packages) {
                apk = completeList.get(packageName);
                completeList.remove(packageName);
                if (apk != null) {
                    Log.d(TAG, "准备安装:" + packageName);
                    break;
                }
            }
            if (apk != null) {
                installing = true;
                applicationHandler.install(apk);
            }
        }
    }

    public void onAppDownloaded(long downloadId, Uri apk) {
        if (downloadList.containsKey(downloadId)) {
            String packageName = downloadList.get(downloadId);
            Log.d(TAG, "下载完成,package name:" + packageName);
            downloadList.remove(downloadId);
            completeList.put(packageName, apk);
        }
        if (!installing) {// 现在是空闲的
            installApp();
        }
    }

    public void appInstalled(String packageName) {
        Log.d(TAG, "收到app安装完成的提醒:" + packageName);
        installing = false;
        if (packageCacke.keySet().contains(packageName)) {
            Log.d(TAG, "是我们的app,删除package安装记录");
            packageCacke.remove(packageName);// 安装完成, 删除记录
        }
        installApp();// 安装下一个下载完成的
    }

}
