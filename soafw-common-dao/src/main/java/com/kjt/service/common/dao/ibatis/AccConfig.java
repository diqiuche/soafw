package com.kjt.service.common.dao.ibatis;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.kjt.service.common.config.DynamicConfig;
import com.kjt.service.common.config.dict.ConfigComponent;
import com.kjt.service.common.config.dict.ConfigFileDict;

@Component(ConfigComponent.AccConfig)
public class AccConfig extends DynamicConfig {

  public AccConfig() {
  }

  @PostConstruct
  public void init(){
      setFileName(System.getProperty(ConfigFileDict.ACCESS_CONTROL_CONFIG_FILE,
          ConfigFileDict.DEFAULT_ACCESS_CONTROL_CONFIG_NAME));
      super.init();
  }

}
