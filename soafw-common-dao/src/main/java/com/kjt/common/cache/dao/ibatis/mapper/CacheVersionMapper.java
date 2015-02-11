package com.kjt.common.cache.dao.ibatis.mapper;

import com.kjt.common.cache.dao.model.CacheVersion;
import com.kjt.service.common.dao.ibatis.ISMapper;

public interface CacheVersionMapper extends ISMapper<CacheVersion> {

  int incrVersion(CacheVersion objName);

  int incrRecVersion(CacheVersion objName);

  int incrTabVersion(CacheVersion objName);
}
