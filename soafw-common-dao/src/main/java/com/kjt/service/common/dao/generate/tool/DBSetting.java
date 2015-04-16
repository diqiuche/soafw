package com.kjt.service.common.dao.generate.tool;

import java.util.HashMap;
import java.util.Map;

public class DBSetting{
  
  private static Map<String,String> setting = new HashMap<String,String>();
  public static String getSetting(String key){
    return setting.get(key);
  }
  public static void setSetting(String key,String value){
    setting.put(key, value);
  }
  
  public static boolean isGenHelp(){
    return DBSetting.getSetting("genHelper")==null?false:Boolean.valueOf(DBSetting.getSetting("genHelper"));
  }
  
  public static void setGenHelp(){
    DBSetting.setSetting("genHelp","true");
  }
}
