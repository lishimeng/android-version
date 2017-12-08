package com.thingple.version.components;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by lism on 2017/12/6.
 */

public class AppInfo extends AppComponent {

    /**
     * uuid
     */
    public String uuid;

    /**
     * 初始化参数
     */
    public Map<String, String> settings;

    /**
     * 模块
     */
    public List<AppComponent> components;
}
