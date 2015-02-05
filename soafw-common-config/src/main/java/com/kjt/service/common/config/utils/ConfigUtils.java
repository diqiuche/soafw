package com.kjt.service.common.config.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.configuration.Configuration;

import com.kjt.service.common.config.IConfigListener;

public class ConfigUtils {
	private static ConfigUtils configUtilsSingleton = new ConfigUtils();

	private Map<String, ConfigWatchdog> watchs = new ConcurrentHashMap<String, ConfigWatchdog>();
	private Map<String, List<IConfigListener>> configListeners = new ConcurrentHashMap<String, List<IConfigListener>>();

	private ConfigUtils() {
	}

	public static ConfigUtils getConfigUtilsInstance() {
		return configUtilsSingleton;
	}

	// 启动通知 配置监听者线程

	public void addListener(IConfigListener configListener) {
		synchronized (configUtilsSingleton) {
			String configFileName = configListener.getFileName();

			// 注册配置文件监听者
			List<IConfigListener> lastConfigListenerList = configListeners.get(configFileName);
			if (lastConfigListenerList == null) {
				lastConfigListenerList = new ArrayList<IConfigListener>();
				configListeners.put(configFileName, lastConfigListenerList);
			}

			/**
			 * 一个侦听者对相同文件只能注册一次
			 */
			if (!lastConfigListenerList.contains(configListener)) {
				lastConfigListenerList.add(configListener);
			}
			/**
			 * 一个文件只有一个文件观察者
			 */
			if (!watchs.containsKey(configFileName)) {
				addWatch(configFileName);
			}
		}
	}

	private void addWatch(String configFilename) {
		ConfigWatchdog watchDog = new ConfigWatchdog(configFilename);
		watchDog.setDelay(1000);
		watchDog.start();
	}

	class ConfigWatchdog extends FileWatchdog {

		protected ConfigWatchdog(String filename) {
			super(filename);
		}

		@Override
		protected void doOnChange() {
			synchronized (configUtilsSingleton) {
				Configuration config = null;

				List<IConfigListener> listeners = configListeners.get(this.getFilename());
				if (listeners == null) {
					return;
				}
				for (int i = 0; i < listeners.size(); i++) {
					IConfigListener listener = listeners.get(i);
					if (config == null) {
						config = listener.build();
						if (config == null) {
							// LogUtil.trace("配置文件‘"+this.getFilename()+"’配置加载失败！");
							return;
						} else {
							// LogUtil.trace("配置文件‘"+this.getFilename()+"’配置加载成功！");
						}
					}
					listener.onUpdate(config);
				}
			}
		}
	}
}
