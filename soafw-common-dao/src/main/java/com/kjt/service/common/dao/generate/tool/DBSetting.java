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
}
