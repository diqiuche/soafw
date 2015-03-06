package com.kjt.service.common.dao.generate.tool;

import java.util.HashMap;
import java.util.Map;

public class Type {

  private String sqlType;
  private String javaType;
  private static Map<String, Type> map = new HashMap<String, Type>();
  
  private static Map<String, Type> sqlservermap = new HashMap<String, Type>();

  static {

    add(new Type("VARCHAR", "String"));
    add(new Type("CHAR", "String"));
    add(new Type("BLOB", "byte[]"));
    add(new Type("TEXT", "String"));

    add(new Type("INT", "Integer"));
    add(new Type("INT UNSIGNED", "Integer"));

    add(new Type("INTEGER", "Long"));
    add(new Type("INTEGER UNSIGNED", "Long"));

    add(new Type("TINYINT", "Integer"));
    add(new Type("TINYINT UNSIGNED", "Integer"));

    add(new Type("SMALLINT", "Integer"));
    add(new Type("SMALLINT UNSIGNED", "Integer"));

    add(new Type("MEDIUMINT", "Integer"));
    add(new Type("MEDIUMINT UNSIGNED", "Integer"));

    add(new Type("FLOAT", "Float"));
    add(new Type("FLOAT UNSIGNED", "Float"));

    add(new Type("DOUBLE", "Double"));

    add(new Type("BIT", "Boolean"));
    add(new Type("BIGINT", "java.math.BigInteger"));
    add(new Type("DECIMAL", "java.math.BigDecimal"));
    add(new Type("BOOLEAN", "Long"));
    add(new Type("DATE", "java.sql.Date"));
    add(new Type("TIME", "java.sql.Time"));
    add(new Type("DATETIME", "java.sql.Timestamp"));
    add(new Type("TIMESTAMP", "java.sql.Timestamp"));
    add(new Type("YEAR", "java.sql.Date"));
    add(new Type("OTHER", "String"));
    
    add(new Type("bigint", "java.math.BigInteger"));
    add(new Type("binary", "byte[]"));
    add(new Type("bit", "byte"));
    add(new Type("datetime", "java.sql.Timestamp"));
    add(new Type("decimal", "java.math.BigDecimal"));
    add(new Type("decimal() identity", "java.math.BigDecimal"));
    add(new Type("float", "Float"));
    add(new Type("image", "byte[]"));
    add(new Type("int", "Integer"));
    add(new Type("int identity", "Integer"));
    add(new Type("money", "java.math.BigDecimal"));
    add(new Type("nchar", "String"));
    add(new Type("ntext", "String"));
    add(new Type("numeric", "Integer"));
    add(new Type("numeric() identity", "Integer"));
    add(new Type("nvarchar", "String"));
    add(new Type("smalldatetime", "java.sql.Timestamp"));
    add(new Type("smallint", "Integer"));
    add(new Type("smallint identity", "Integer"));
    add(new Type("smallmoney", "java.math.BigDecimal"));
    add(new Type("sql_variant", "String"));
    add(new Type("sysname", "String"));
    add(new Type("text", "String"));
    add(new Type("timestamp", "Integer"));
    add(new Type("tinyint", "Integer"));
    add(new Type("tinyint identity", "Integer"));
    add(new Type("uniqueidentifier", "char"));
    add(new Type("varbinary", "String"));
    add(new Type("varchar", "String"));
    add(new Type("OTHER", "String"));
    
  }

  public Type(String sqlType, String javaType) {
    this.sqlType = sqlType.toLowerCase();
    this.javaType = javaType;
  }

  public String getSqlType() {
    return sqlType;
  }

  public void setSqlType(String sqlType) {
    this.sqlType = sqlType;
  }

  public String getJavaType() {
    return javaType;
  }

  public void setJavaType(String javaType) {
    this.javaType = javaType;
  }

  public static void add(Type type) {
    if ("mysql".equalsIgnoreCase(System.getProperty("db.type", "mysql"))) {
      map.put(type.getSqlType(), type);
    }
    else{
      sqlservermap.put(type.getSqlType(), type);
    }
  }

  public static Type get(String type) {
    Type result = null;
    if ("mysql".equalsIgnoreCase(System.getProperty("db.type", "mysql"))) {
      result = map.get(type);
    }
    else{
      result = sqlservermap.get(type);
    }
    if (result == null) {
      map.get("OTHER");
    }
    return result;
  }

}
