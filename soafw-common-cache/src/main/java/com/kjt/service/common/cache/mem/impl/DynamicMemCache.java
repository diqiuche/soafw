package com.kjt.service.common.cache.mem.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.configuration.Configuration;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.kjt.service.common.cache.CacheOpParamsContext;
import com.kjt.service.common.cache.ICache;
import com.kjt.service.common.cache.annotation.CacheOpParams;
import com.kjt.service.common.config.IConfigListener;
import com.kjt.service.common.config.PoolableObjDynamicConfig;
import com.kjt.service.common.config.dict.ConfigFileTypeDict;
import com.kjt.service.common.util.DateUtil;

public class DynamicMemCache extends PoolableObjDynamicConfig implements Cache,
		ICache, IConfigListener {
	
	public static final String DEFAULT_CACHE_NAME = "defaultCache";

	private MemCachedClient _mc;
	private String sufix = null;

	private String cacheName = DEFAULT_CACHE_NAME;

	public final static int DEFAULT_INITIAL_CONNECTIONS = 1;
	public final static int DEFAULT_MIN_SPARE_CONNECTIONS = 1;
	public final static int DEFAULT_MAX_SPARE_CONNECTIONS = 10;
	public final static long DEFAULT_MAX_IDLE_TIME = 1000 * 60 * 5;
	public final static long DEFAULT_MAX_BUSY_TIME = 1000 * 30;
	public final static long DEFAULT_MAINT_THREAD_SLEEP = 1000 * 30;
	public final static int DEFAULT_SOCKET_TIMEOUT = 1000 * 30;
	public final static int DEFAULT_SOCKET_CONNECT_TIMEOUT = 1000 * 3;
	public final static boolean DEFAULT_ALIVE_CHECK = false;
	public final static boolean DEFAULT_FAILOVER = false;
	public final static boolean DEFAULT_FAILBACK = true;
	public final static boolean DEFAULT_NAGLE_ALGORITHM = true;
	public final static int DEFAULT_HASHING_ALGORITHM = SockIOPool.CONSISTENT_HASH;

	@Override
	public boolean set(String key, Object item) {
		return set(key, item, null);
	}

	@Override
	public boolean set(String key, Object item, int expiry) {
		if (expiry <= 0) {
			return this.set(key, item, null);
		} else {
			return _mc.set(key, item, new Date(System.currentTimeMillis()
					+ expiry * 1000));
		}
	}

	@Override
	public boolean set(String key, Object item, Date expiry) {
		return _mc.set(key, item, expiry);
	}

	@Override
	public boolean add(String key, Object item) {
		return add(key, item, null);
	}

	@Override
	public boolean add(String key, Object item, int expiry) {
		return add(key, item, new Date(System.currentTimeMillis() + expiry
				* 1000));
	}

	@Override
	public boolean add(String key, Object item, Date expiry) {
		return _mc.add(key, item, expiry);
	}

	@Override
	public boolean replace(String key, Object item) {
		return replace(key, item, null);
	}

	@Override
	public boolean replace(String key, Object item, int expiry) {
		return replace(key, item, new Date(System.currentTimeMillis() + expiry
				* 1000));
	}

	@Override
	public boolean replace(String key, Object item, Date expiry) {
		return _mc.replace(key, item, expiry);
	}

	@Override
	public boolean delete(String key) {
		return _mc.delete(key);
	}

	@Override
	public Object get(String key) {
		return _mc.get(key);
	}

	@Override
	public Object[] get(String[] keys) {
		return _mc.getMultiArray(keys);
	}

	@Override
	public boolean flush() {
		return _mc.flushAll();
	}

	@Override
	public long addOrIncr(String key, long incr) {
		return _mc.addOrIncr(key, incr);
	}

	@Override
	public long incr(String key, long incr) {
		return _mc.incr(key, incr);

	}

	@Override
	public boolean storeCounter(String key, Long counter) {
		return _mc.storeCounter(key, counter);
	}

	@Override
	@PostConstruct
	public void init() {
		this.setFileName(System.getProperty(CACHE_MEM_CONFIG_FILE,
				DEFAULT_CACHE_MEM_CONFIG_NAME));
		this.setType(ConfigFileTypeDict.PROPERTIES);
		super.init();
		this.build(this.getConfig());
	}

	public void build(Configuration config) {

		String prefix_ = this.getPrefix();

		String _sufix = null;

		try {

			_sufix = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
			String poolName = prefix_ + _sufix;
			SockIOPool pool = SockIOPool.getInstance(poolName);
			if (pool.isInitialized()) {
				throw new IllegalArgumentException(String.format(
						"%s has been created", poolName));
			}

			pool.setInitConn(config.getInt(prefix_
					+ "memcached.initialConnections"));
			pool.setMinConn(config.getInt(prefix_
					+ "memcached.minSpareConnections"));
			pool.setMaxConn(config.getInt(prefix_
					+ "memcached.maxSpareConnections"));
			pool.setMaxIdle(config.getInt(prefix_ + "memcached.maxIdleTime"));
			pool.setMaxBusyTime(config
					.getInt(prefix_ + "memcached.maxBusyTime"));
			pool.setMaintSleep(config.getInt(prefix_
					+ "memcached.maintThreadSleep"));
			pool.setSocketTO(config.getInt(prefix_ + "memcached.socketTimeout"));
			pool.setSocketConnectTO(this.getInt(prefix_
					+ "memcached.socketConnectTimeout"));
			pool.setAliveCheck(config.getBoolean(prefix_
					+ "memcached.aliveCheck"));
			pool.setFailover(config.getBoolean(prefix_ + "memcached.failover"));
			pool.setFailback(config.getBoolean(prefix_ + "memcached.failback"));
			pool.setNagle(config.getBoolean(prefix_
					+ "memcached.nagleAlgorithm"));
			pool.setHashingAlg(config.getInt(prefix_
					+ "memcached.hashingAlgorithm"));

			pool.setServers(config
					.getStringArray(prefix_ + "memcached.servers"));
			String[] weights = config.getStringArray(prefix_
					+ "memcached.weights");
			int len = weights == null ? 0 : weights.length;
			if (len > 0) {
				Integer[] weights_ = new Integer[len];
				for (int i = 0; i < len; i++) {
					weights_[i] = Integer.valueOf(weights[i]);
				}
				pool.setWeights(weights_);
			}

			pool.initialize();
			MemCachedClient _tmpMC = new MemCachedClient(poolName);
			this._mc = _tmpMC;

			if (sufix != null) {
				final String _poolName = prefix_ + sufix;

				sufix = _sufix;
				try {
					Thread.sleep(2000);
					SockIOPool.getInstance(_poolName).shutDown();
				} catch (Exception ex) {
				}
			}

		} catch (ParseException e) {

		}
	}

	protected String configToString(Configuration config) {

		String prefix_ = getPrefix();

		StringBuffer sb = new StringBuffer();

		sb.append(config.getInt(prefix_ + "memcached.initialConnections"))
				.append("|");
		sb.append(config.getInt(prefix_ + "memcached.minSpareConnections"))
				.append("|");
		sb.append(config.getInt(prefix_ + "memcached.maxSpareConnections"))
				.append("|");
		sb.append(config.getInt(prefix_ + "memcached.maxIdleTime")).append("|");
		sb.append(config.getInt(prefix_ + "memcached.maxBusyTime")).append("|");
		sb.append(config.getInt(prefix_ + "memcached.maintThreadSleep"))
				.append("|");
		sb.append(config.getInt(prefix_ + "memcached.socketTimeout")).append(
				"|");
		sb.append(config.getInt(prefix_ + "memcached.socketConnectTimeout"))
				.append("|");
		sb.append(config.getBoolean(prefix_ + "memcached.aliveCheck")).append(
				"|");
		sb.append(config.getBoolean(prefix_ + "memcached.failover"))
				.append("|");
		sb.append(config.getBoolean(prefix_ + "memcached.failback"))
				.append("|");
		sb.append(config.getBoolean(prefix_ + "memcached.nagleAlgorithm"))
				.append("|");
		sb.append(config.getInt(prefix_ + "memcached.hashingAlgorithm"))
				.append("|");
		sb.append(
				arrayToString(config.getStringArray(prefix_
						+ "memcached.servers"))).append("|");
		sb.append(arrayToString(config.getStringArray(prefix_
				+ "memcached.weights")));

		return sb.toString();
	}

	private String arrayToString(Object[] objs) {
		StringBuffer sb = new StringBuffer();
		int len = objs == null ? 0 : objs.length;
		for (int i = 0; i < len; i++) {
			sb.append(objs[i].toString());
			if (i < len - 1) {
				sb.append("|");
			}
		}
		return sb.toString();
	}

	// *********************以下方法遵循spring cacheManager规范********************

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	@Override
	public String getName() {
		return cacheName;
	}

	@Override
	public Object getNativeCache() {
		return _mc;
	}

	@Override
	public ValueWrapper get(Object key) {
		Object value = this._mc.get((String) key);
		return (value != null ? new SimpleValueWrapper(value) : null);
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		Object value = this._mc.get((String) key);
		if (value != null && type != null && !type.isInstance(value)) {
			throw new IllegalStateException(
					"Cached value is not of required type [" + type.getName()
							+ "]: " + value);
		}
		return (T) value;
	}

	@Override
	public void put(Object key, Object value) {
		CacheOpParams params = CacheOpParamsContext.getOpParams();
		if(params!=null){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, Long.valueOf(params.time()).intValue());
			this._mc.add((String) key, value, cal.getTime());//((String) key, value,params.time());
		}
		else{
			this._mc.add((String) key, value);
		}
	}

	@Override
	public void evict(Object key) {
		this._mc.delete((String) key);
	}

}
