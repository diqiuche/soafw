package com.kjt.service.common.dao.ibatis;

import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;

import com.kjt.service.common.util.ContextHolder;
import com.kjt.service.common.util.SPUtil;

@Intercepts({ @Signature(method = "prepare", type = StatementHandler.class, args = { Connection.class }) })
public class StatementHandlerPlugin implements Interceptor {

  private static String pid = "unknow";
  static {
    pid = ManagementFactory.getRuntimeMXBean().getName();
  }

  public Object intercept(Invocation invocation) throws Throwable {

    StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

    MetaObject metaStatementHandler = SqlmapUtils.getMetaObject(statementHandler);

    Configuration configuration = (Configuration) metaStatementHandler
        .getValue("delegate.configuration");

    BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");

    metaStatementHandler.setValue("delegate.boundSql.sql",
        buildSql(boundSql.getSql(), configuration));

    // 将执行权交给下一个拦截器
    return invocation.proceed();
  }

  private String buildSql(String sql, Configuration configuration) {

    if (sql.indexOf(" #from_api:") != -1) {
      return sql;
    } else {
      String db = null;
      Environment env = null;
      if (configuration != null) {
        env = configuration.getEnvironment();
      }
      if (env != null) {
        db = env.getId();
      }
      StringBuilder sb = new StringBuilder(sql);
      sb.append(" #from_api:");
      sb.append(ContextHolder.getReqId());
      sb.append(pid);
      sb.append(" ");
      sb.append(SPUtil.getSpid());
      sb.append(" ");
      sb.append(db);
      sb.append("\n");
      sql = sb.toString();
      return sql;
    }
  }

  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  public void setProperties(Properties properties) {
  }
}
