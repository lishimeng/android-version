package com.thingple.library.version.components;

import android.content.Context;
import android.util.Log;


import com.thingple.json.JsonConvertor;
import com.thingple.library.version.uuid.GlobalKeyHandler;
import com.thingple.library.version.uuid.UUID;
import com.thingple.rest.RestClient;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by lism on 2017/12/8.
 */

public class ComponentsHandler {

    private Context context;

    public ComponentsHandler(Context context) {
        this.context = context;
    }

    public void getGlobalConfig(final AppInfoListener listener) {
        GlobalKeyHandler globalKeyHandler = new GlobalKeyHandler();
        final UUID uuid = globalKeyHandler.get();
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppInfo info = null;
                try {
                    Map<String, String> params = new HashMap();
                    String text = RestClient.newInstance().get(uuid.uri, params);
                    info = JsonConvertor.convert2Object(text, AppInfo.class);
                } catch (Exception e) {
                    Log.e("#getGlobalConfig", e.getMessage(), e);
                }
                if (listener != null) {
                    listener.onListener(uuid, info);
                }
            }
        }).start();
    }
}
