package com.kjt.service.common.reflection.wrapper;

import com.kjt.service.common.reflection.MetaObject;

/**
 * @author Clinton Begin
 */
public interface ObjectWrapperFactory {

  boolean hasWrapperFor(Object object);
  
  ObjectWrapper getWrapperFor(MetaObject metaObject, Object object);
  
}
