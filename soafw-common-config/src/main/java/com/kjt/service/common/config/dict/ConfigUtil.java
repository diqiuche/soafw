package com.kjt.service.common.config.dict;

import com.kjt.service.common.util.StringUtil;

public class ConfigUtil {
    
    public static String getSysConfigDir(){
        return System.getProperty(ConfigFileDict.SYS_CONFIG_DIR,"/config");
    }
    
    public static String getAppHomeDir(){
        return System.getProperty(ConfigFileDict.APP_HOME_DIR);
    }
    
    public static String getAppConfigDir(){
        return StringUtil.isEmpty(getAppHomeDir())?getSysConfigDir():getAppHomeDir()+"/config";
    }
}
