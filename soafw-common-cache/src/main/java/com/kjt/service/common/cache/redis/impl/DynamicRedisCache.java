package com.kjt.service.common.cache.redis.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.configuration.Configuration;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.kjt.service.common.cache.ICache;
import com.kjt.service.common.config.IConfigListener;
import com.kjt.service.common.config.PoolableObjDynamicConfig;
import com.kjt.service.common.config.dict.ConfigFileTypeDict;
import com.kjt.service.common.util.DateUtil;

public class DynamicRedisCache extends PoolableObjDynamicConfig implements
		Cache, ICache, IConfigListener {
	private ShardedJedisPool delegate;

	public static final String DEFAULT_CACHE_NAME = "defaultRedisCache";

	@Override
	@PostConstruct
	public void init() {
		this.setFileName(System.getProperty(CACHE_REDIS_CONFIG,
				DEFAULT_CACHE_REDIS_CONFIG_NAME));
		this.setType(ConfigFileTypeDict.PROPERTIES);
		super.init();
		this.build(this.getConfig());
	}

	@Override
	protected String configToString(Configuration config) {
		String prefix_ = getPrefix();

		StringBuffer sb = new StringBuffer();
		sb.append(config.getInt(prefix_ + "redis.timeout")).append("|");
		sb.append(config.getInt(prefix_ + "redis.maxTotal")).append("|");
		sb.append(config.getInt(prefix_ + "redis.minIdle")).append("|");
		sb.append(config.getInt(prefix_ + "redis.maxIdle")).append("|");
		sb.append(config.getInt(prefix_ + "redis.minEvictableIdleTimeMillis"))
				.append("|");
		sb.append(config.getInt(prefix_ + "redis.numTestsPerEvictionRun"))
				.append("|");
		sb.append(
				config.getInt(prefix_ + "redis.softMinEvictableIdleTimeMillis"))
				.append("|");
		sb.append(config.getBoolean(prefix_ + "redis.testOnBorrow"))
				.append("|");
		sb.append(config.getBoolean(prefix_ + "redis.testOnReturn"))
				.append("|");
		sb.append(config.getBoolean(prefix_ + "redis.testWhileIdle")).append(
				"|");
		sb.append(
				config.getLong(prefix_ + "redis.timeBetweenEvictionRunsMillis"))
				.append("|");
		String servers[] = config.getStringArray(prefix_ + "redis.servers");
		String ports[] = config.getStringArray(prefix_ + "redis.ports");
		sb.append(arrayToString(servers)).append("|");
		sb.append(arrayToString(ports));

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

	@Override
	protected void build(Configuration config) {
		String prefix_ = this.getPrefix();

		JedisPoolConfig redisCfg = new JedisPoolConfig();
		redisCfg.setMaxTotal(config.getInt(prefix_ + "redis.maxTotal"));
		redisCfg.setMinIdle(config.getInt(prefix_ + "redis.minIdle"));
		redisCfg.setMaxIdle(config.getInt(prefix_ + "redis.maxIdle"));
		redisCfg.setMinEvictableIdleTimeMillis(config.getInt(prefix_
				+ "redis.minEvictableIdleTimeMillis"));
		redisCfg.setNumTestsPerEvictionRun(config.getInt(prefix_
				+ "redis.numTestsPerEvictionRun"));
		redisCfg.setSoftMinEvictableIdleTimeMillis(config.getInt(prefix_
				+ "redis.softMinEvictableIdleTimeMillis"));
		redisCfg.setTestOnBorrow(config.getBoolean(prefix_
				+ "redis.testOnBorrow"));
		redisCfg.setTestOnReturn(config.getBoolean(prefix_
				+ "redis.testOnReturn"));
		redisCfg.setTestWhileIdle(config.getBoolean(prefix_
				+ "redis.testWhileIdle"));
		redisCfg.setTimeBetweenEvictionRunsMillis(config.getLong(prefix_
				+ "redis.timeBetweenEvictionRunsMillis"));
		String servers[] = config.getStringArray(prefix_ + "redis.servers");
		String ports[] = config.getStringArray(prefix_ + "redis.ports");

		int ssize = servers.length;
		List<JedisShardInfo> list = new LinkedList<JedisShardInfo>();
		for (int i = 0; i < ssize; i++) {
			JedisShardInfo jedisShardInfo = null;
			String server = (String) servers[i];
			String port = (String) ports[i];
			jedisShardInfo = new JedisShardInfo(server, port);
			jedisShardInfo.setTimeout(config.getInt(prefix_ + "redis.timeout"));
			list.add(jedisShardInfo);
		}
		ShardedJedisPool jedis = new ShardedJedisPool(redisCfg, list);
		if (delegate == null) {
			delegate = jedis;
		} else {
			ShardedJedisPool old = delegate;
			delegate = jedis;
			old.destroy();
		}
	}

	@Override
	public boolean set(String key, Object item) {
		ShardedJedis _jedis = null;
		try {
			_jedis = delegate.getResource();
			_jedis.set(toBytes(key), toBytes(item));
			return true;
		} finally {
			if (_jedis != null) {
				delegate.returnResource(_jedis);
			}
		}
	}
	
	public boolean set(String key, String item) {
		ShardedJedis _jedis = null;
		try {
			_jedis = delegate.getResource();
			_jedis.set(key, item);
			return true;
		} finally {
			if (_jedis != null) {
				delegate.returnResource(_jedis);
			}
		}
	}

	@Override
	public boolean set(String key, Object item, int seconds) {
		ShardedJedis _jedis = null;
		try {
			_jedis = delegate.getResource();
			_jedis.set(toBytes(key), toBytes(item));
			_jedis.expire(toBytes(key), seconds);
			return true;
		} finally {
			if (_jedis != null) {
				delegate.returnResource(_jedis);
			}
		}
	}

	@Override
	public boolean set(String key, Object item, Date expiry) {
		if (expiry == null) {
			return set(key, item);
		} else {
			ShardedJedis _jedis = null;
			try {
				_jedis = delegate.getResource();
				_jedis.set(toBytes(key), toBytes(item));
				_jedis.expireAt(toBytes(key), DateUtil.getDateUnixTime(expiry));
				return true;
			} finally {
				if (_jedis != null) {
					delegate.returnResource(_jedis);
				}
			}
		}
	}

	@Override
	public boolean add(String key, Object item) {
		return set(key, item);
	}

	@Override
	public boolean add(String key, Object item, int seconds) {
		return set(key, item, seconds);
	}

	@Override
	public boolean add(String key, Object item, Date expiry) {
		return set(key, item, expiry);
	}

	@Override
	public boolean storeCounter(String key, Long counter) {
		return this.set(key, counter);
	}

	@Override
	public long addOrIncr(String key, long incr) {
		ShardedJedis _jedis = null;
		try {
			_jedis = delegate.getResource();
			return _jedis.incrBy(key, incr);
		} finally {
			if (_jedis != null) {
				delegate.returnResource(_jedis);
			}
		}
	}

	@Override
	public long incr(String key, long incr) {
		return addOrIncr(key, incr);
	}

	@Override
	public boolean replace(String key, Object item) {
		return this.set(key, item);
	}

	@Override
	public boolean replace(String key, Object item, int expiry) {
		return this.set(key, item, expiry);
	}

	public void sadd(String key, String val) {

		ShardedJedis _jedis = null;
		try {
			_jedis = delegate.getResource();
			_jedis.sadd(key, val);
		} finally {
			if (_jedis != null) {
				delegate.returnResource(_jedis);
			}
		}
	}
	
	public void sadd(String key, String val,int expire) {
		ShardedJedis _jedis = null;
		try {
			_jedis = delegate.getResource();
			_jedis.sadd(key, val);
			_jedis.expire(key, expire);
		} finally {
			if (_jedis != null) {
				delegate.returnResource(_jedis);
			}
		}
	}

	public Set<String> sget(String key) {
		ShardedJedis _jedis = null;
		try {
			_jedis = delegate.getResource();
			return _jedis.smembers(key);
		} finally {
			if (_jedis != null) {
				delegate.returnResource(_jedis);
			}
		}
	}

	@Override
	public boolean replace(String key, Object item, Date expiry) {
		return this.set(key, item, expiry);
	}

	@Override
	public boolean delete(String key) {
		ShardedJedis _jedis = null;
		try {
			_jedis = delegate.getResource();
			_jedis.del(toBytes(key));
			return true;
		} finally {
			if (_jedis != null) {
				delegate.returnResource(_jedis);
			}
		}
	}

	@Override
	public String get(String key) {
		ShardedJedis _jedis = null;
		try {
			_jedis = delegate.getResource();
			return _jedis.get(key);
		} finally {
			if (_jedis != null) {
				delegate.returnResource(_jedis);
			}
		}
	}

	@Override
	public Object[] get(String[] keys) {
		return null;
	}

	@Override
	public boolean flush() {
		return false;
	}

	private String cacheName = DEFAULT_CACHE_NAME;

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	@Override
	public String getName() {
		return cacheName;
	}

	@Override
	public Object getNativeCache() {
		return delegate;
	}

	public Object llen(Object key) {
		ShardedJedis _jedis = null;
		try {
			_jedis = delegate.getResource();
			long length = _jedis.llen(toBytes(key));
			return new SimpleValueWrapper(Long.valueOf(length));
		} finally {
			if (_jedis != null) {
				delegate.returnResource(_jedis);
			}
		}
	}

	@Override
	public ValueWrapper get(Object key) {
		ShardedJedis _jedis = null;
		try {
			_jedis = delegate.getResource();
			byte[] datas = _jedis.get(toBytes(key));
			if (datas == null)
				return null;
			Object result = toObject(datas);
			return new SimpleValueWrapper(result);
		} finally {
			if (_jedis != null) {
				delegate.returnResource(_jedis);
			}
		}
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		ShardedJedis _jedis = null;
		try {
			_jedis = delegate.getResource();
			byte[] datas = _jedis.get(toBytes(key));
			if (datas == null)
				return null;
			Object result = toObject(datas);
			if (datas != null && type != null && !type.isInstance(result)) {
				throw new IllegalStateException(
						"Cached value is not of required type ["
								+ type.getName() + "]: " + result);
			}
			return (T) result;
		} finally {
			if (_jedis != null) {
				delegate.returnResource(_jedis);
			}
		}

	}

	@Override
	public void put(Object key, Object value) {
		ShardedJedis _jedis = null;
		try {
			_jedis = delegate.getResource();
			_jedis.set(toBytes(key), toBytes(value));
		} finally {
			if (_jedis != null) {
				delegate.returnResource(_jedis);
			}
		}
	}

	@Override
	public void evict(Object key) {
		ShardedJedis _jedis = null;
		try {
			_jedis = delegate.getResource();
			_jedis.del(toBytes(key));
		} finally {
			if (_jedis != null) {
				delegate.returnResource(_jedis);
			}
		}
	}

	private static byte[] toBytes(Object obj) {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (IOException e) {
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	private static Object toObject(byte[] datas) {
		if (datas == null)
			return null;
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			bais = new ByteArrayInputStream(datas);
			ois = new ObjectInputStream(bais);
			Object data = ois.readObject();
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	public static void main(String[] args) {
		byte[] a = { 50 };
		byte[] b = toBytes(1L);
	}

}
