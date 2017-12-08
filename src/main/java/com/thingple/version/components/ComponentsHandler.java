package com.thingple.version.components;

import android.content.Context;

import com.thingple.json.JsonConvertor;
import com.thingple.rest.RestClient;
import com.thingple.version.uuid.GlobalKeyHandler;
import com.thingple.version.uuid.UUID;

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
        UUID uuid = globalKeyHandler.get();
        if (uuid != null && uuid.uuid != null) {
            final String uri = uuid.uri;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String, String> params = new HashMap();
                    String text = RestClient.newInstance().get(uri, params);
                    final AppInfo info = JsonConvertor.convert2Object(text, AppInfo.class);
                    if (listener != null) {
                        listener.onData(info);
                    }
                }
            }).start();
        }
    }
}
