package com.thingple.library.version.uuid;

/**
 *
 * Created by lism on 2017/12/7.
 */

public class UUID {

    /**
     * uuid
     */
    public String uuid;

    /**
     * check uri
     */
    public String uri;

    /**
     * 类型
     */
    public String vendor;

    public UUID() {

    }

    public UUID(String encoded) {
        if (encoded != null) {
            String[] datas = encoded.split(";");
            if (datas.length == 3) {
                uuid = datas[0];
                uri = datas[1];
                vendor = datas[2];
            }
        }
    }

    @Override
    public String toString() {
        return uuid + ";" + uri + ";" + vendor;
    }
}
