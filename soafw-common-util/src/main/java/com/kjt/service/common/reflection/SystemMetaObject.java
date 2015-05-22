package com.kjt.service.common.reflection;

import com.kjt.service.common.reflection.factory.DefaultObjectFactory;
import com.kjt.service.common.reflection.factory.ObjectFactory;
import com.kjt.service.common.reflection.wrapper.DefaultObjectWrapperFactory;
import com.kjt.service.common.reflection.wrapper.ObjectWrapperFactory;

/**
 * @author Clinton Begin
 */
public class SystemMetaObject {

  public static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
  public static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
  public static final MetaObject NULL_META_OBJECT = MetaObject.forObject(NullObject.class, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);

  private static class NullObject {
  }
  
  public static MetaObject forObject(Object object) {
    return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
  }
  
}