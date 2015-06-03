package com.kjt.service.common.config.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.configuration.Configuration;

import com.kjt.service.common.config.IConfigListener;
import com.kjt.service.common.config.dict.ConfigFileDict;
import com.kjt.service.common.util.StringUtil;

public class ConfigUtil {
    
    private static ConfigUtil configUtilsSingleton = new ConfigUtil();

    private Map<String, ConfigWatchdog> watchs = new ConcurrentHashMap<String, ConfigWatchdog>();
    private Map<String, List<IConfigListener>> configListeners =
            new ConcurrentHashMap<String, List<IConfigListener>>();

    private ConfigUtil() {}

    public static ConfigUtil getConfigUtilsInstance() {
        return configUtilsSingleton;
    }

    // 启动通知 配置监听者线程
    public void addListener(IConfigListener configListener) {

        synchronized (configUtilsSingleton) {
            /**
             * 一个文件观察者可以观察多个不同的文件
             */
            String[] configFileName = configListener.getFileName();
            int size = configFileName == null ? 0 : configFileName.length;
            for (int i = 0; i < size; i++) {

                String tempConfigFile = configFileName[i];
                List<IConfigListener> lastConfigListenerList = configListeners.get(tempConfigFile);
                if (lastConfigListenerList == null) {// 没有注册过
                    lastConfigListenerList = new ArrayList<IConfigListener>();
                    configListeners.put(tempConfigFile, lastConfigListenerList);
                }
                // 注册配置文件监听者
                lastConfigListenerList = configListeners.get(tempConfigFile);
                /**
                 * 一个侦听者对相同文件只能注册一次
                 */
                if (!lastConfigListenerList.contains(configListener)) {
                    lastConfigListenerList.add(configListener);
                }
                /**
                 * 一个文件只有一个文件观察者
                 */
                if (!watchs.containsKey(tempConfigFile)) {
                    addWatch(tempConfigFile);
                }
            }

        }
    }

    private void addWatch(String configFilename) {
        ConfigWatchdog watchDog = new ConfigWatchdog(configFilename);
        watchDog.setDelay(1000);
        watchDog.start();
        watchs.put(configFilename, watchDog);
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
    
    public static String getSysConfigDir(){
        return System.getProperty(ConfigFileDict.SYS_CONFIG_DIR,"/config");
    }
    
    public static String getAppHomeDir(){
        return System.getProperty(ConfigFileDict.APP_HOME_DIR);
    }
    
    public static String getAppConfigDir(){
        return StringUtil.isEmpty(getAppHomeDir())?getSysConfigDir():getAppHomeDir()+"/config";
    }
}
