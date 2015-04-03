package com.kjt.service.common.dao.generate.tool;

import java.util.HashMap;

public class SqlServerType extends HashMap {
  {
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

  private void add(Type type) {
    put(type.getSqlType(), type);
  }

  public Type getType(String type) {
    Type result = null;
    result = (Type)get(type);
    if (result == null) {
      result = (Type) get("other");
    }
    return result;
  }
}
