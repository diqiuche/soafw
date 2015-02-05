package com.kjt.service.common.util;

import java.util.UUID;

public class RequestID {

    private static final ThreadLocal<String> REQUEST_ID =
        new ThreadLocal<String>();

    public static void set(String rid) {
        String uuid = "";
        if (null == rid) {
            uuid = UUID.randomUUID().toString();
        } else {
            uuid = rid;
        }
        REQUEST_ID.set(uuid);
    }

    public static void unset() {
        REQUEST_ID.remove();
    }

    public static String get() {
        return REQUEST_ID.get();
    }
}
