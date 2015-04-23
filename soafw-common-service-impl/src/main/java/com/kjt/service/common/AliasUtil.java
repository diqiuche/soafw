package com.kjt.service.common;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.kjt.service.common.util.StringUtils;

public class AliasUtil {
    
    public static String KEYMAP_KEY = "@@@$$$$$@@@@";

    public static Map getAlias(String keys, ProceedingJoinPoint pjp, boolean both) {
        MethodSignature sign = (MethodSignature) pjp.getSignature();
        String methodName = sign.getName();
        String returnType = sign.getReturnType().getSimpleName();
        Class[] classes = sign.getParameterTypes();
        String[] paramNames = sign.getParameterNames();
        Object[] args = pjp.getArgs();
        Map params = getAlias(keys, sign, paramNames, classes, args, both);
        String className = pjp.getTarget().getClass().getSimpleName();
        if (both) {
            params.put("simple",
                    returnType + ":" + methodName + "@" + className + params.get("simple"));
        } 
        params.put("full",
            returnType + ":" + methodName + "@" + className + params.get("detail"));
        params.put("className", className);
        params.put("methodName", methodName);
        return params;
    }

    private static String getAlias(MethodSignature sign, String[] paramNames, Class[] classes,
            Object[] args) {
        Annotation[][] ans = sign.getMethod().getParameterAnnotations();
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < classes.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(classes[i].getSimpleName());
            sb.append(" ");
            sb.append(paramNames[i]);
            sb.append("=");
            sb.append(getAliasAccordingType(ans[i], classes[i], args[i]));
        }
        return sb.append(")").toString();
    }

    private static Map getAlias(String keys, MethodSignature sign, String[] paramNames,
            Class[] classes, Object[] args, boolean both) {
        Annotation[][] ans = sign.getMethod().getParameterAnnotations();
        StringBuilder detail = new StringBuilder("(");
        StringBuilder simple = new StringBuilder("(");
        String name = "";
        if (!StringUtils.isEmpty(keys)) {
            String[] kv = keys.split("=");
            if (kv != null || kv.length > 1) {
                if ("params".equals(kv[0])) {
                    name = kv[1];
                }
            }
        }
        Map keyMap = new HashMap();
        for (int i = 0; i < classes.length; i++) {
            String value = getAliasAccordingType(ans[i], classes[i], args[i]);
            if (paramNames[i].equalsIgnoreCase(name)) {
                keyMap.put(paramNames[i], value);
            }
            if (i > 0) {
                if (both) simple.append(",");
                detail.append(",");
            }
            if (both) {
                simple.append(classes[i].getSimpleName());
                simple.append(" ");
                simple.append(paramNames[i]);
            }
            detail.append(paramNames[i] + "=" + value);
        }
        Map params = new HashMap();
        if (keyMap.size() > 0) params.put(KEYMAP_KEY, keyMap);
        params.put("detail", detail.append(")").toString());
        if (both) params.put("simple", simple.append(")").toString());
        return params;
    }

    private static String getAliasAccordingType(Annotation[] anns, Class class1, Object object) {
        if (object == null) return String.valueOf("null");
        return object.toString();
    }

    private static String getValue(Object object) {
        String value = String.valueOf(object);
        if (value.length() > 70) {
            value = "...";
        }
        return value;
    }
}
