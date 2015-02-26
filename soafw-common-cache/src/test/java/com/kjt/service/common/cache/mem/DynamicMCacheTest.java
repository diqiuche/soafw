package com.kjt.service.common.cache.mem;

import org.junit.Test;

import com.kjt.service.common.cache.mem.impl.DynamicMemCache;

public class DynamicMCacheTest {

    @Test
    public void confgigTest() {
        DynamicMemCache cache = new DynamicMemCache();
        cache.setPrefix("COMMUNITYS");
        cache.init();
        cache.set("hello", "world");
        for (int i = 0; i < 6; i++) {
            System.out.println(cache.get("hello"));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {}

        }
    }
}
