package com.kjt.service.common.dao.ibatis;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sqlmap工具.
 *
 * TODO: ThreadLocal SqlSession
 */
public final class SqlmapUtils {

  /**
   * Add mapper to defined SqlSessionFactory.
   *
   * @param mapperType
   * @param definition
   */
  public static void addMapper(Class<?> mapperType, DataSource dataSource) {

    SqlSessionFactory factory = getFactory(dataSource);
    Configuration configuration = factory.getConfiguration();
    if (!configuration.hasMapper(mapperType)) {
      configuration.addMapper(mapperType);
    }
  }

  /**
   * Add mapper to defined SqlSessionFactories.
   *
   * @param mapperType
   * @param definitions
   */
  public static void addMapper(Class<?> mapperType, DataSource[] dataSources) {
    if (dataSources == null || dataSources.length <= 0) {
      return;
    }
    for (DataSource dataSource : dataSources) {
      addMapper(mapperType, dataSource);
    }
  }

  /**
   * 返回SqlSessionWrapper.
   *
   * @param definition
   * @return
   */
  public static SqlSession openSession(DataSource dataSource) {
    SqlSessionFactory factory = getFactory(dataSource);
    return factory.openSession();
  }

  /**
   * 返回SqlSessionWrapper.
   *
   * @param definition
   * @param executorType
   * @return
   */
  public static SqlSession openSession(DataSource dataSource, ExecutorType executorType) {
    SqlSessionFactory factory = getFactory(dataSource);
    return factory.openSession(executorType);
  }

  //

  /**
   * 返回所需的SqlSessionFactoryWrapper.
   *
   * @param definition
   *          需要的数据源定要.
   * @return SqlSessionFactory
   */
  public static SqlSessionFactory getFactory(DataSource definition) {
    if (_factories.containsKey(definition)) {
      return _factories.get(definition);
    }
    return createSqlSessionFactory(definition);
  }

  //

  private static synchronized SqlSessionFactory createSqlSessionFactory(DataSource dataSource) {
    SqlSessionFactory factory = _factories.get(dataSource);
    if (factory != null) {
      return factory;
    }
    /**
     * Ｈost Datasource
     */
    TransactionFactory transactionFactory = new JdbcTransactionFactory();
    Environment environment = new Environment("Default", transactionFactory, dataSource);
    Configuration configuration = new Configuration(environment);
    configuration.addInterceptor(new StatementHandlerPlugin());
    factory = new SqlSessionFactoryBuilder().build(configuration);
    _factories.put(dataSource, factory);

    return factory;
  }

  //

  private static Map<DataSource, SqlSessionFactory> _factories = new HashMap<DataSource, SqlSessionFactory>();

  private transient static Logger _logger = LoggerFactory.getLogger(SqlmapUtils.class);

  private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
  private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

  public static MetaObject getMetaObject(Object obj) {
    return MetaObject.forObject(obj, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
  }
}
