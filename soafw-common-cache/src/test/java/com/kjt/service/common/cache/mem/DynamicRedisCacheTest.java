package com.kjt.service.common.cache.mem;

import org.junit.Test;

import com.kjt.service.common.cache.redis.impl.DynamicRedisCache;

public class DynamicRedisCacheTest {
	
	@Test
	public void confgigTest() {
		DynamicRedisCache cache = new DynamicRedisCache();
		cache.setPrefix("COMMUNITYS");
		cache.init();
		cache.set("hello1", "ddworld");
		for (int i = 0; i < 6; i++) {
			System.out.println(cache.get("hello1"));
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}

		}
	}
}
