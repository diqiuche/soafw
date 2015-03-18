package com.kjt.service.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLPropertiesConfiguration;

import com.kjt.service.common.config.dict.ConfigFileDict;
import com.kjt.service.common.config.dict.ConfigFileTypeDict;
import com.kjt.service.common.config.utils.ConfigUtils;
import com.kjt.service.common.log.Logger;
import com.kjt.service.common.log.LoggerFactory;
import com.kjt.service.common.util.Constants;
import com.kjt.service.common.util.StringUtils;

/**
 * 动态检测&加载变化内容<br>
 * config.file.dir：配置文件路径<br>
 * 默认为没有profile<br>
 * 当没有设置profile时 <br>
 * System.getProperty(CONFIG_DIR, CONFIG_DIR_DEF) + File.separator + _settingFileName +
 * File.separator + "." + type; <br>
 * 当设置了profile时 <br>
 * System.getProperty(CONFIG_DIR, CONFIG_DIR_DEF) + File.separator + profiel+.+ _settingFileName +
 * File.separator + "." + type;
 * 
 * @author alexzhu
 * 
 */
public class DynamicConfig implements ConfigFileDict, Constants, Configuration, IConfigListener {

    protected Logger logger = LoggerFactory.getLogger(DynamicConfig.this.getClass());
    /**
     * 默认为没有profile<br>
     * 当没有设置profile时 <br>
     * System.getProperty(CONFIG_DIR, CONFIG_DIR_DEF) + File.separator + _settingFileName +
     * File.separator + "." + type; <br>
     * 当设置了profile时 <br>
     * System.getProperty(CONFIG_DIR, CONFIG_DIR_DEF) + File.separator + profiel+.+ _settingFileName
     * + File.separator + "." + type;
     */
    private String _settingFileName = "common";
    private String type = ConfigFileTypeDict.XML;
    private Configuration delegate;
    private String encoding = "utf-8";
    private boolean delimiterParsingDisabled;
    private List<String> configFiles = new ArrayList<String>();

    @PostConstruct
    public void init() {

        this.regist();

        delegate = this.build();

        ConfigUtils.getConfigUtilsInstance().addListener(this);
    }

    public CompositeConfiguration buildCompositeConfiguration() {

        CompositeConfiguration compositeConfiguration = new CompositeConfiguration();
        compositeConfiguration.setThrowExceptionOnMissing(_throwExceptionOnMissing);

        addConfig(compositeConfiguration, "/config/" + getProfile() + "%s");

        addConfig(compositeConfiguration, getAppHomeDir() +File.separator+"config"+ File.separator + getProfile()
                + "%s");
        addConfig(compositeConfiguration, "classpath:META-INF/config/local/" + getProfile() + "%s");

        return compositeConfiguration;
    }

    private void addConfig(CompositeConfiguration compositeConfiguration, String pattern) {

        PropertiesConfiguration config = new PropertiesConfiguration();

        if (this.getType() != null && this.getType().trim().equalsIgnoreCase("properties")) {
            pattern = new StringBuffer(pattern).append(".properties").toString();
        } else {
            config = new XMLPropertiesConfiguration();
            pattern = new StringBuffer(pattern).append(".xml").toString();
        }

        config.setDelimiterParsingDisabled(isDelimiterParsingDisabled());

        String[] nameArray = _settingFileName.split(",");
        for (String name : nameArray) {
            String location = String.format(pattern, name);
            InputStream resource = null;
            try {
                if(location.startsWith("classpath:")){
                    location = location.substring(10);
                    resource = this.getClass().getClassLoader().getResourceAsStream(location);
                }
                else{
                    resource = new FileInputStream(new File(location));
                }
                
                if (!StringUtils.isEmpty(this.getEncoding())) {

                    config.load(resource, this.getEncoding());
                } else {
                    config.load(resource);
                }
                
                compositeConfiguration.addConfiguration(config);
            } catch (Exception e) {
                if (logger.isInfoEnabled()) {
                    logger.info("Skip config '%s', %s", location, e.getMessage());
                }
            }
        }
    }

    /**
     * 
     * @return
     */
    private void regist() {
        String configFile = null;
        configFile =
                System.getProperty(CONFIG_DIR, CONFIG_DIR_DEF) + File.separator + this.getProfile()
                        + _settingFileName + "." + type;
        /**
         * 全局外围配置不存在 配置文件位置存放在/config
         */
        if (!configFiles.contains(configFile) && new File(configFile).exists()) {
            configFiles.add(configFile);
        }

        /**
         * 本地可动态配置 项目本地配置文件位置通过启动参数-Dapp.home.dir=xxx进行设置
         * 配置文件全路径：xxx/config/yy.type或者xxx/config/profile.yy.type
         */
        configFile =
                this.getAppHomeDir() + File.separator + "config" + File.separator
                        + this.getProfile() + _settingFileName + "." + type;

        if (!configFiles.contains(configFile) && new File(configFile).exists()) {
            configFiles.add(configFile);
        }
    }

    /**
     * 支持profile机制
     * 
     * @return
     */
    private String getProfile() {
        String profile = System.getProperty("profile");
        if (profile != null) {
            profile = profile + ".";
        } else {
            profile = "";
        }
        return profile;
    }

    private String getAppHomeDir() {
        String app$home$dir = System.getProperty("app.home.dir");
        if (app$home$dir == null) {
            app$home$dir = "";
        }
        return app$home$dir;
    }

    public Configuration build() {
        return buildCompositeConfiguration();

        /*
         * PropertiesConfiguration config = new PropertiesConfiguration();
         * 
         * if ("xml".equalsIgnoreCase(this.getType())) { config = new XMLPropertiesConfiguration();
         * }
         * 
         * config.setDelimiterParsingDisabled(isDelimiterParsingDisabled());
         * 
         * FileInputStream fileInputStream = null;
         * 
         * try { fileInputStream = new FileInputStream(getFileName()); if
         * (!StringUtils.isEmpty(this.getEncoding())) { config.load(fileInputStream,
         * this.getEncoding()); } else { config.load(fileInputStream); } return config; } catch
         * (Exception e) {
         * 
         * InputStream is = this.getClass() .getClassLoader() .getResourceAsStream(
         * "META-INF/config/local/" + this.getProfile() + this._settingFileName + "." +
         * this.getType()); if (is == null) { throw new RuntimeException(e); } try {
         * config.load(is); return config; } catch (ConfigurationException e1) { throw new
         * RuntimeException(e1); } finally { try { if (is != null) is.close(); } catch (IOException
         * ex) {} }
         * 
         * } finally { try { if (fileInputStream != null) fileInputStream.close(); } catch
         * (IOException e) {} }
         */
    }

    /**
     * 配置文件sampleName
     * 
     * @param fileName
     */
    public void setFileName(String fileName) {
        this._settingFileName = fileName;
    }

    public String[] getFileName() {
        String[] files = new String[configFiles.size()];
        return configFiles.toArray(files);
    }

    public String getType() {
        return type;
    }

    /**
     * 配置文件类型［properties/xml］,默认为xml
     * 
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public boolean isDelimiterParsingDisabled() {
        return delimiterParsingDisabled;
    }

    public void setDelimiterParsingDisabled(boolean delimiterParsingDisabled) {
        this.delimiterParsingDisabled = delimiterParsingDisabled;
    }

    public synchronized void onUpdate(Configuration delegate) {
        this.delegate = delegate;
    }

    private boolean _throwExceptionOnMissing = false;

    public void setThrowExceptionOnMissing(boolean value) {
        _throwExceptionOnMissing = value;
    }

    public Configuration subset(String prefix) {
        return delegate.subset(prefix);
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public boolean containsKey(String key) {
        return delegate.containsKey(key);
    }

    public void addProperty(String key, Object value) {
        delegate.addProperty(key, value);
    }

    public void setProperty(String key, Object value) {
        delegate.setProperty(key, value);
    }

    public void clearProperty(String key) {
        delegate.clearProperty(key);
    }

    public void clear() {
        delegate.clear();
    }

    public Object getProperty(String key) {
        return delegate.getProperty(key);
    }

    public Iterator getKeys(String prefix) {
        return delegate.getKeys(prefix);
    }

    public Iterator getKeys() {
        return delegate.getKeys();
    }

    public Properties getProperties(String key) {
        return delegate.getProperties(key);
    }

    public boolean getBoolean(String key) {
        return delegate.getBoolean(key);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return delegate.getBoolean(key, defaultValue);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        return delegate.getBoolean(key, defaultValue);
    }

    public byte getByte(String key) {
        return delegate.getByte(key);
    }

    public byte getByte(String key, byte defaultValue) {
        return delegate.getByte(key, defaultValue);
    }

    public Byte getByte(String key, Byte defaultValue) {
        return delegate.getByte(key, defaultValue);
    }

    public double getDouble(String key) {
        return delegate.getDouble(key);
    }

    public double getDouble(String key, double defaultValue) {
        return delegate.getDouble(key, defaultValue);
    }

    public Double getDouble(String key, Double defaultValue) {
        return delegate.getDouble(key, defaultValue);
    }

    public float getFloat(String key) {
        return delegate.getFloat(key);
    }

    public float getFloat(String key, float defaultValue) {
        return delegate.getFloat(key, defaultValue);
    }

    public Float getFloat(String key, Float defaultValue) {
        return delegate.getFloat(key, defaultValue);
    }

    public int getInt(String key) {
        return delegate.getInt(key);
    }

    public int getInt(String key, int defaultValue) {
        return delegate.getInt(key, defaultValue);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        return delegate.getInteger(key, defaultValue);
    }

    public long getLong(String key) {
        return delegate.getLong(key);
    }

    public long getLong(String key, long defaultValue) {
        return delegate.getLong(key, defaultValue);
    }

    public Long getLong(String key, Long defaultValue) {
        return delegate.getLong(key, defaultValue);
    }

    public short getShort(String key) {
        return delegate.getShort(key);
    }

    public short getShort(String key, short defaultValue) {
        return delegate.getShort(key, defaultValue);
    }

    public Short getShort(String key, Short defaultValue) {
        return delegate.getShort(key, defaultValue);
    }

    public BigDecimal getBigDecimal(String key) {
        return delegate.getBigDecimal(key);
    }

    public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        return delegate.getBigDecimal(key, defaultValue);
    }

    public BigInteger getBigInteger(String key) {
        return delegate.getBigInteger(key);
    }

    public BigInteger getBigInteger(String key, BigInteger defaultValue) {
        return delegate.getBigInteger(key, defaultValue);
    }

    public String getString(String key) {
        return delegate.getString(key);
    }

    public String getString(String key, String defaultValue) {
        return delegate.getString(key, defaultValue);
    }

    public String[] getStringArray(String key) {
        return delegate.getStringArray(key);
    }

    public List getList(String key) {
        return delegate.getList(key);
    }

    public List getList(String key, List defaultValue) {
        return delegate.getList(key, defaultValue);
    }

    public Configuration getConfig() {
        return delegate;
    }
}
