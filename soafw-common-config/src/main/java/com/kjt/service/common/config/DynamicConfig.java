package com.kjt.service.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLPropertiesConfiguration;

import com.kjt.service.common.config.dict.ConfigFileDict;
import com.kjt.service.common.config.dict.ConfigFileTypeDict;
import com.kjt.service.common.config.utils.ConfigUtils;
import com.kjt.service.common.util.Constants;
import com.kjt.service.common.util.StringUtils;

/**
 * config.file.dir：配置文件路径
 * 
 * @author alexzhu
 * 
 */
public class DynamicConfig extends ConfigFileDict implements Constants, Configuration, IConfigListener {
	/**
	 * System.getProperty(CONFIG_DIR, CONFIG_DIR_DEF) + File.separator + _settingFileName + File.separator + "." + type;
	 */
	private String fileName = null;
	private String _settingFileName = "common";
	private String type = ConfigFileTypeDict.XML;
	private Configuration delegate;
	private String encoding = "utf-8";
	private boolean delimiterParsingDisabled;

	@PostConstruct
	public void init() {
		this.fileName = System.getProperty(CONFIG_DIR, CONFIG_DIR_DEF) + File.separator + _settingFileName + "." + type;
		delegate = this.build();
		ConfigUtils.getConfigUtilsInstance().addListener(this);
	}

	public Configuration build() {

		PropertiesConfiguration config = new PropertiesConfiguration();

		if ("xml".equalsIgnoreCase(this.getType())) {
			config = new XMLPropertiesConfiguration();
		}

		config.setDelimiterParsingDisabled(isDelimiterParsingDisabled());

		FileInputStream fileInputStream = null;

		try {
			fileInputStream = new FileInputStream(getFileName());
			if (!StringUtils.isEmpty(this.getEncoding())) {
				config.load(fileInputStream, this.getEncoding());
			} else {
				config.load(fileInputStream);
			}
			return config;
		} catch (Exception e) {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("META-INF/config/local/"+this._settingFileName+"."+this.getType());
			if(is == null){
				throw new RuntimeException(e);
			}
			try {
				config.load(is);
				return config;
			} catch (ConfigurationException e1) {
				throw new RuntimeException(e1);
			}
			finally{
				try {
					if (is != null)
						is.close();
				} catch (IOException ex) {
				}
			}
			
		} finally {
			try {
				if (fileInputStream != null)
					fileInputStream.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 配置文件sampleName
	 * 
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this._settingFileName = fileName;
	}

	public String getFileName() {
		return fileName;
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

	public Configuration getConfig(){
		return delegate;
	}
}
