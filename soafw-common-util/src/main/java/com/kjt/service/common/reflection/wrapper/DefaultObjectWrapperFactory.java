package com.kjt.service.common.reflection.wrapper;

import com.kjt.service.common.reflection.MetaObject;
import com.kjt.service.common.reflection.ReflectionException;

/**
 * @author Clinton Begin
 */
public class DefaultObjectWrapperFactory implements ObjectWrapperFactory {

  public boolean hasWrapperFor(Object object) {
    return false;
  }
  
  public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
    throw new ReflectionException("The DefaultObjectWrapperFactory should never be called to provide an ObjectWrapper.");
  }
  
}
