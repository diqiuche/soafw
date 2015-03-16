package com.kjt.service.common.dao.generate.tool;

import java.util.HashMap;
import java.util.Map;

public class Type {

  private String sqlType;
  private String javaType;
  private static Map<String, Type> map = new HashMap<String, Type>();
  
  static {
    //mysql
    if ("mysql".equalsIgnoreCase(System.getProperty("db.type", "mysql"))){
      map = new MySqlType();
    }
    else{
    //sql server
      map = new SqlServerType();
    }
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
    map.put(type.getSqlType(), type);
  }

  public static Type get(String type) {
    Type result = null;
    result = map.get(type);
    if (result == null) {
      map.get("OTHER");
    }
    return result;
  }

}
