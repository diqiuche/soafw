package com.kjt.service.common.util;

import java.lang.reflect.Field;

import com.kjt.service.common.annotation.JField;

public class BeanUtil {

	public static String getJField(Class model,String property,Class<JField> annotation){
		Field tmp = null;
		try {
			tmp = model.getDeclaredField(property);
			JField ann = tmp.getAnnotation(annotation);
			if(ann != null){
				return ann.name();
			}
			throw new RuntimeException(annotation.getCanonicalName()+" does't declared");
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	public class Model{
		@JField(name="_name")
		private String name;
		
		@JField(name="local")
		private String address;
	}
	
	public static void main(String[] args){
	}
}
