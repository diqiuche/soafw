package com.kjt.service.common.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.kjt.service.common.config.IConfigListener;
import com.kjt.service.common.config.PoolableObjDynamicConfig;
import com.kjt.service.common.config.dict.ConfigFileTypeDict;
import com.kjt.service.common.config.utils.ConfigReloadEvent;
import com.kjt.service.common.config.utils.ConfigReloadObserver;
import com.kjt.service.common.config.utils.MailSender;
import com.kjt.service.common.util.LogUtils;
import com.kjt.service.common.util.StringUtils;
import com.kjt.service.concurrent.AsynBizExecutor;

/**
 * 支持xml、properties文件格式<br>
 * 配置文件目录：通过设置系统属性‘config.file.dir’设置，其默认值：/config <br>
 * 数据库配置文件名，通过设置系统属性‘database.config’设置，其默认值：database<br>
 * 默认配置文件格式：xml<br>
 * 即默认配置文件为：database.xml <br>
 * 
 * @author alexzhu
 *
 */
public class DynamicDataSource extends PoolableObjDynamicConfig implements
		DataSource, IConfigListener {
	protected org.slf4j.Logger _logger = org.slf4j.LoggerFactory
			.getLogger("trace");
	private DataSource delegate;
	private String hostName;

	public DynamicDataSource() {
	}

	/**
	 * 配置文件配置项key格式 <br>
	 * prefix.driver<br>
	 * prefix.url<br>
	 * prefix.username <br>
	 * prefix.password<br>
	 * prefix.initialSize <br>
	 * prefix.maxActive <br>
	 * prefix.maxWait <br>
	 * prefix.minIdle<br>
	 * prefix.maxIdle <br>
	 * prefix.poolPreparedStatements <br>
	 * prefix.defaultReadOnly<br>
	 * prefix.logAbandoned <br>
	 * prefix.removeAbandoned <br>
	 * prefix.removeAbandonedTimeout<br>
	 * prefix.testOnBorrow <br>
	 * prefix.testWhileIdle <br>
	 * prefix.validationQuery<br>
	 * prefix.minEvictableIdleTimeMillis <br>
	 * prefix.timeBetweenEvictionRunsMillis
	 * 
	 */
	protected void build(Configuration config) {

		long start = System.currentTimeMillis();

		String prefix_ = this.getPrefix();

		String driverClassName_ = config.getString(prefix_ + "driver");
		String url_ = config.getString(prefix_ + "url");
		String username_ = config.getString(prefix_ + "username");
		String password_ = config.getString(prefix_ + "password");
		Integer initialSize_ = config.getInt(prefix_ + "initialSize");
		Integer maxActive_ = config.getInt(prefix_ + "maxActive");
		Integer maxIdle_ = config.getInt(prefix_ + "maxIdle");
		Integer minIdle_ = config.getInt(prefix_ + "minIdle");
		Long maxWait_ = config.getLong(prefix_ + "maxWait");
		Integer removeAbandonedTimeout_ = config.getInt(prefix_
				+ "removeAbandonedTimeout");
		Long minEvictableIdleTimeMillis_ = config.getLong(prefix_
				+ "minEvictableIdleTimeMillis");
		Long timeBetweenEvictionRunsMillis_ = config.getLong(prefix_
				+ "timeBetweenEvictionRunsMillis");
		Boolean poolPreparedStatements_ = config.getBoolean(prefix_
				+ "poolPreparedStatements");
		Boolean defaultReadOnly_ = config.getBoolean(prefix_
				+ "defaultReadOnly");
		Boolean logAbandoned_ = config.getBoolean(prefix_ + "logAbandoned");
		Boolean removeAbandoned_ = config.getBoolean(prefix_
				+ "removeAbandoned");
		Boolean testOnBorrow_ = config.getBoolean(prefix_ + "testOnBorrow");
		Boolean testWhileIdle_ = config.getBoolean(prefix_ + "testWhileIdle");
		String validationQuery_ = config.getString(prefix_ + "validationQuery");

		BasicDataSource basicDataSource_ = new BasicDataSource();
		basicDataSource_.setDriverClassName(driverClassName_);
		basicDataSource_.setUrl(url_);
		basicDataSource_.setUsername(username_);
		basicDataSource_.setPassword(password_);
		basicDataSource_.setInitialSize(initialSize_);
		basicDataSource_.setMaxActive(maxActive_);
		basicDataSource_.setMaxIdle(maxIdle_);
		basicDataSource_.setMinIdle(minIdle_);
		basicDataSource_.setMaxWait(maxWait_);
		basicDataSource_.setPoolPreparedStatements(poolPreparedStatements_);
		basicDataSource_.setDefaultReadOnly(defaultReadOnly_);
		basicDataSource_.setLogAbandoned(logAbandoned_);
		basicDataSource_.setRemoveAbandoned(removeAbandoned_);
		basicDataSource_.setRemoveAbandonedTimeout(removeAbandonedTimeout_);
		basicDataSource_.setTestOnBorrow(testOnBorrow_);
		basicDataSource_.setValidationQuery(validationQuery_);
		basicDataSource_
				.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis_);
		basicDataSource_.setTestWhileIdle(testWhileIdle_);
		basicDataSource_
				.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis_);
		try {
			this.verify(prefix_, basicDataSource_);
			if(delegate == null){
				delegate = basicDataSource_;
			}
			else {
				final DataSource oldDataSource = this.delegate;
				this.delegate = basicDataSource_;
				LogUtils.timeused(_logger, prefix_ + "reloaded", start);
				new AsynBizExecutor(this.getClass().getName()) {
					@Override
					public void execute() {
						try {
							long start = System.currentTimeMillis();
							Thread.sleep(2000);
							((BasicDataSource) oldDataSource).close();
						} catch (Exception ex) {
						}
					}
				};
			}
		} catch (Exception ex) {
			LogUtils.error(_logger, ex);
			this.notify(prefix_, ex.getMessage(), configToString(config));
		}
	}

	private void verify(String prefix_, BasicDataSource basicDataSource_) {
		long start = System.currentTimeMillis();
		TransactionFactory transactionFactory = new JdbcTransactionFactory();
		Environment environment = new Environment(prefix_, transactionFactory,
				basicDataSource_);
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration(
				environment);
		configuration.setCacheEnabled(true);
		configuration.setLazyLoadingEnabled(false);
		configuration.setDefaultExecutorType(ExecutorType.SIMPLE);
		configuration.setUseGeneratedKeys(false);
		SqlSessionFactory factory = new SqlSessionFactoryBuilder()
				.build(configuration);
		SqlSession session = factory.openSession();
		if (session != null) {
			session.close();
		}
		LogUtils.timeused(_logger, prefix_ + "verify", start);
	}

	private void notify(String prefix_, String message, String newCfg) {
		long start = System.currentTimeMillis();
		StringBuilder reloadMsgBuffer = new StringBuilder();
		String currentTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
				.format(new Date());
		reloadMsgBuffer.append("Hi All,");
		reloadMsgBuffer.append("<br>");
		reloadMsgBuffer.append("在 ");
		reloadMsgBuffer.append(currentTime);
		reloadMsgBuffer.append("时刻，机器 " + hostName
				+ "数据库配置作了变更，但是新配置文件校验失败 ！<br>");
		reloadMsgBuffer.append("具体的失败原因如下：<br>");
		reloadMsgBuffer.append("数据源：" + this.getPrefix() + "<br>");
		reloadMsgBuffer.append("异常：" + message + "<br>");
		reloadMsgBuffer.append("该数据源配置变更如下：" + "<br>");
		reloadMsgBuffer.append(this.getConfig() + " => " + newCfg + "<br>");
		reloadMsgBuffer.append("Best Regards,<br>");
		reloadMsgBuffer.append("Anjuke API Team");

		if (StringUtils.isEmpty(dbReloadEventReceiver)) {
			return;
		}

		Map params = new HashMap();
		params.put("subject", hostName
				+ " database configure reload verify failure");
		params.put("body", reloadMsgBuffer.toString());
		LogUtils.trace(_logger, "db reload mail content:" + params);
		ConfigReloadEvent event = new ConfigReloadEvent(params);
		ConfigReloadObserver observer = new ConfigReloadObserver(
				dbReloadEventReceiver);
		observer.setSender(new MailSender());
		event.addObserver(observer);
		event.fireEvent();
		LogUtils.timeused(_logger, prefix_ + "notify", start);
	}

	public Boolean getTestWhileIdle() {
		return ((BasicDataSource) this.delegate).getTestWhileIdle();
	}

	public void setTestWhileIdle(Boolean testWhileIdle) {
		((BasicDataSource) this.delegate).setTestWhileIdle(testWhileIdle);
	}

	protected String configToString(Configuration config) {

		String prefix_ = getPrefix();

		String driverClassName_ = config.getString(prefix_ + "driver",
				this.getDriverClassName());
		String url_ = config.getString(prefix_ + "url", this.getUrl());
		String username_ = config.getString(prefix_ + "username",
				this.getUsername());
		String password_ = config.getString(prefix_ + "password",
				this.getPassword());
		Integer initialSize_ = config.getInteger(prefix_ + "initialSize",
				this.getInitialSize());
		Integer maxActive_ = config.getInteger(prefix_ + "maxActive",
				this.getMaxActive());
		Integer maxIdle_ = config.getInteger(prefix_ + "maxIdle",
				this.getMaxIdle());
		Integer minIdle_ = config.getInteger(prefix_ + "minIdle",
				this.getMinIdle());
		Long maxWait_ = config.getLong(prefix_ + "maxWait", this.getMaxWait());
		Integer removeAbandonedTimeout_ = config.getInteger(prefix_
				+ "removeAbandonedTimeout", this.getRemoveAbandonedTimeout());
		Long minEvictableIdleTimeMillis_ = config.getLong(prefix_
				+ "minEvictableIdleTimeMillis",
				this.getMinEvictableIdleTimeMillis());
		Long timeBetweenEvictionRunsMillis_ = config.getLong(prefix_
				+ "timeBetweenEvictionRunsMillis",
				this.getTimeBetweenEvictionRunsMillis());
		Boolean poolPreparedStatements_ = config.getBoolean(prefix_
				+ "poolPreparedStatements", this.getPoolPreparedStatements());
		Boolean defaultReadOnly_ = config.getBoolean(prefix_
				+ "defaultReadOnly", this.getDefaultReadOnly());
		Boolean logAbandoned_ = config.getBoolean(prefix_ + "logAbandoned",
				this.getLogAbandoned());
		Boolean removeAbandoned_ = config.getBoolean(prefix_
				+ "removeAbandoned", this.getRemoveAbandoned());
		Boolean testOnBorrow_ = config.getBoolean(prefix_ + "testOnBorrow",
				this.getTestOnBorrow());
		Boolean testWhileIdle_ = config.getBoolean(prefix_ + "testWhileIdle",
				this.getTestWhileIdle());
		String validationQuery_ = config.getString(prefix_ + "validationQuery",
				this.getValidationQuery());

		StringBuffer sb_ = new StringBuffer();
		sb_.append(driverClassName_ + "|");
		sb_.append(url_ + "|");
		sb_.append(username_ + "|");
		sb_.append(password_ + "|");
		sb_.append(initialSize_ + "|");
		sb_.append(maxActive_ + "|");
		sb_.append(maxIdle_ + "|");
		sb_.append(minIdle_ + "|");
		sb_.append(maxWait_ + "|");
		sb_.append(poolPreparedStatements_ + "|");
		sb_.append(defaultReadOnly_ + "|");
		sb_.append(logAbandoned_ + "|");
		sb_.append(removeAbandoned_ + "|");
		sb_.append(removeAbandonedTimeout_ + "|");
		sb_.append(testOnBorrow_ + "|");
		sb_.append(validationQuery_ + "|");
		sb_.append(minEvictableIdleTimeMillis_ + "|");
		sb_.append(testWhileIdle_ + "|");
		sb_.append(timeBetweenEvictionRunsMillis_);

		return sb_.toString();
	}

	private String dbReloadEventReceiver;

	public void setDbReloadEventReceiver(String dbReloadEventReceiver) {
		this.dbReloadEventReceiver = dbReloadEventReceiver;
	}

	public String getDriverClassName() {
		return ((BasicDataSource) this.delegate).getDriverClassName();
	}

	public void setDriverClassName(String driverClassName) {
		((BasicDataSource) this.delegate).setDriverClassName(driverClassName);
	}

	public String getUrl() {
		return ((BasicDataSource) this.delegate).getUrl();
	}

	public void setUrl(String url) {
		((BasicDataSource) this.delegate).setUrl(url);
	}

	public String getUsername() {
		return ((BasicDataSource) this.delegate).getUsername();
	}

	public void setUsername(String username) {
		((BasicDataSource) this.delegate).setUsername(username);
	}

	public String getPassword() {
		return ((BasicDataSource) this.delegate).getPassword();
	}

	public void setPassword(String password) {
		((BasicDataSource) this.delegate).setPassword(password);
	}

	/**
	 * 默认值为0
	 * 
	 * @return
	 */
	public Integer getInitialSize() {
		return ((BasicDataSource) this.delegate).getInitialSize();
	}

	public void setInitialSize(Integer initialSize) {
		((BasicDataSource) this.delegate).setInitialSize(initialSize);
	}

	/**
	 * 默认值：8
	 * 
	 * @return
	 */
	public Integer getMaxActive() {
		return ((BasicDataSource) this.delegate).getMaxActive();
	}

	public void setMaxActive(Integer maxActive) {
		((BasicDataSource) this.delegate).setMaxActive(maxActive);
	}

	/**
	 * 默认值：8
	 * 
	 * @return
	 */
	public Integer getMaxIdle() {
		return ((BasicDataSource) this.delegate).getMaxIdle();
	}

	public void setMaxIdle(Integer maxIdle) {
		((BasicDataSource) this.delegate).setMaxIdle(maxIdle);
	}

	/**
	 * 默认值：0
	 * 
	 * @return
	 */
	public Integer getMinIdle() {
		return ((BasicDataSource) this.delegate).getMinIdle();
	}

	public void setMinIdle(Integer minIdle) {
		((BasicDataSource) this.delegate).setMinIdle(minIdle);
	}

	/**
	 * 默认值：－1l
	 * 
	 * @return
	 */
	public Long getMaxWait() {
		return ((BasicDataSource) this.delegate).getMaxWait();
	}

	public void setMaxWait(Long maxWait) {
		((BasicDataSource) this.delegate).setMaxWait(maxWait);
	}

	/**
	 * 默认值：false
	 * 
	 * @return
	 */
	public Boolean getPoolPreparedStatements() {
		return ((BasicDataSource) this.delegate).isPoolPreparedStatements();
	}

	public void setPoolPreparedStatements(Boolean poolPreparedStatements) {
		((BasicDataSource) this.delegate)
				.setPoolPreparedStatements(poolPreparedStatements);
	}

	/**
	 * 默认值：false
	 * 
	 * @return
	 */
	public Boolean getDefaultReadOnly() {
		return ((BasicDataSource) this.delegate).getDefaultReadOnly();
	}

	/**
	 * 默认值：false
	 * 
	 * @return
	 */
	public Integer getRemoveAbandonedTimeout() {
		return ((BasicDataSource) this.delegate).getRemoveAbandonedTimeout();
	}

	public void setRemoveAbandonedTimeout(Integer removeAbandonedTimeout) {
		((BasicDataSource) this.delegate)
				.setRemoveAbandonedTimeout(removeAbandonedTimeout);
	}

	public void setDefaultReadOnly(Boolean defaultReadOnly) {
		((BasicDataSource) this.delegate).setDefaultReadOnly(defaultReadOnly);
	}

	/**
	 * 默认值：false
	 * 
	 * @return
	 */
	public Boolean getLogAbandoned() {
		return ((BasicDataSource) this.delegate).getLogAbandoned();
	}

	public void setLogAbandoned(Boolean logAbandoned) {
		((BasicDataSource) this.delegate).setLogAbandoned(logAbandoned);
	}

	/**
	 * 默认值：false
	 * 
	 * @return
	 */
	public Boolean getRemoveAbandoned() {
		return ((BasicDataSource) this.delegate).getRemoveAbandoned();
	}

	public void setRemoveAbandoned(Boolean removeAbandoned) {
		((BasicDataSource) this.delegate).setRemoveAbandoned(removeAbandoned);
	}

	/**
	 * 默认值：false
	 * 
	 * @return
	 */
	public Boolean getTestOnBorrow() {
		return ((BasicDataSource) this.delegate).getTestOnBorrow();
	}

	public void setTestOnBorrow(Boolean testOnBorrow) {
		((BasicDataSource) this.delegate).setTestOnBorrow(testOnBorrow);
	}

	/**
	 * 默认值：null
	 * 
	 * @return
	 */
	public String getValidationQuery() {
		return ((BasicDataSource) this.delegate).getValidationQuery();
	}

	public void setValidationQuery(String validationQuery) {
		((BasicDataSource) this.delegate).setValidationQuery(validationQuery);
	}

	/**
	 * 默认值：1000L
	 * 
	 * @return
	 */
	public Long getMinEvictableIdleTimeMillis() {
		return ((BasicDataSource) this.delegate)
				.getMinEvictableIdleTimeMillis();
	}

	public void setMinEvictableIdleTimeMillis(Long minEvictableIdleTimeMillis) {
		((BasicDataSource) this.delegate)
				.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
	}

	/**
	 * 默认值：－1L
	 * 
	 * @return
	 */
	public Long getTimeBetweenEvictionRunsMillis() {
		return ((BasicDataSource) this.delegate)
				.getTimeBetweenEvictionRunsMillis();
	}

	public void setTimeBetweenEvictionRunsMillis(
			Long timeBetweenEvictionRunsMillis) {
		((BasicDataSource) this.delegate)
				.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
	}

	public DataSource getDelegate() {
		return delegate;
	}

	public void setDelegate(DataSource delegate) {
		this.delegate = delegate;
	}

	public Connection getConnection() throws SQLException {
		return delegate.getConnection();
	}

	public PrintWriter getLogWriter() throws SQLException {
		return delegate.getLogWriter();
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		delegate.setLogWriter(out);
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		delegate.setLoginTimeout(seconds);
	}

	public int getLoginTimeout() throws SQLException {
		return delegate.getLoginTimeout();
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return delegate.unwrap(iface);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return delegate.isWrapperFor(iface);
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		return delegate.getConnection(username, password);
	}

	public void close() throws SQLException {

		BasicDataSource basicDataSource = (BasicDataSource) delegate;
		basicDataSource.close();
	}

	public java.util.logging.Logger getParentLogger()
			throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException("not implements");
	}

	@Override
	@PostConstruct
	public void init() {
		this.setFileName(System.getProperty(DB_CONFIG_FILE, DEFAULT_DB_CONFIG_NAME));
		this.setType(ConfigFileTypeDict.PROPERTIES);
		super.init();
		this.build(this.getConfig());
	}
}
