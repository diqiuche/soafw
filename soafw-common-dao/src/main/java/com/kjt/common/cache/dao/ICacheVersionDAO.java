package com.kjt.common.cache.dao;

import com.kjt.common.cache.dao.model.CacheVersion;
import com.kjt.service.common.dao.IDAO;
import com.kjt.service.common.dao.IFKDAO;
import com.kjt.service.common.dao.ISDAO;

public interface ICacheVersionDAO<T extends CacheVersion> extends ISDAO<T>, IFKDAO<T>, IDAO<T> {

  public int incrObjVersion(String objName, String tabNameSuffix);

  public int incrObjRecVersion(String objName, String tabNameSuffix);

  public int incrObjTabVersion(String objName, String tabNameSuffix);
}
