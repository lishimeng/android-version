package com.thingple.version.components;

import com.thingple.version.uuid.UUID;

/**
 *
 * Created by lism on 2017/12/8.
 */

public interface AppInfoListener {

    void onListener(UUID uuid, AppInfo appInfo);
}
