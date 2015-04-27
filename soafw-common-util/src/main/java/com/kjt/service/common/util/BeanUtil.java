package com.kjt.service.common.util;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
	
	public static Map<String, Object> beanToMap(Object model) {
        Field[] fields = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
        Map<String, Object> map = new HashMap<String, Object>();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(model);
                if (value == null) {
                    continue;
                } else { 
                    String type = field.getGenericType().toString();    //获取属性的类型
                     if(type.equals("class java.util.Date")){
                         map.put(field.getName(), DateUtil.getDateFormat((Date)field.get(model), "yyyy-MM-dd HH:mm:ss"));  
                     } else {  
                         map.put(field.getName(), field.get(model));
                     }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
	
	public static void main(String[] args){
	}
}
