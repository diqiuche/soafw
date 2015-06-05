package com.kjt.service.common.dao.generate.tool;

import java.util.HashMap;
import java.util.Map;

public class DBSetting{
  
  private static Map<String,String> setting = new HashMap<String,String>();
  public static String getSetting(String key){
    return setting.get(key);
  }
  
  public static void setSQLSvr(){
    setting.put("type", "sqlserver");
  }
  
  public static String getType(){
    return setting.get("type")==null?"mysql":setting.get("type");
  }
  
  public static void setSetting(String key,String value){
    setting.put(key, value);
  }
  
  public static boolean isGenHelp(){
    return DBSetting.getSetting("genHelper")==null?false:Boolean.valueOf(DBSetting.getSetting("genHelper"));
  }
  public static boolean isMysql(){
    return getSetting("type")==null||"mysql".equalsIgnoreCase(getSetting("type"));
  }
  public static void setGenHelp(){
    DBSetting.setSetting("genHelp","true");
  }
  
  public static String getDatabaseName(String url){
    String database = null;
    if(url.indexOf("mysql")!=-1){
      //jdbc:mysql://192.168.1.110:3306/soafw_db?${db.conn.str}
      String tmp = url.substring(0, url.indexOf("?")).substring(url.lastIndexOf("/")+1);
      database= tmp;
    }
    else{
      //jdbc:sqlserver://10.10.2.16:1433; DatabaseName=dropship
      String tmp = url.substring(url.indexOf("DatabaseName")+13);
      int idx = tmp.indexOf(";");
      if(idx!=-1){
        tmp = tmp.substring(0, idx).trim();
      }
      database= tmp;
    }
    return database;
  }
  
  public static void main(String[] args){
    //System.out.println(getDatabaseName("jdbc:mysql://192.168.1.110:3306/soafw_db?${db.conn.str}"));
    
    System.out.println(getDatabaseName("jdbc:sqlserver://10.10.2.16:1433; DatabaseName=dropship;"));
  }
}
