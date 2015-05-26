package com.kjt.service.#{artifactId}.dao;

import com.kjt.service.common.dao.generate.tool.DaoGenFromDB;

public class DaoGen {


    public static void main(String[] args) {
        try {
        	/**
        	 * sql server 数据访问层代码生成器
        	 */
        	//DaoGenFromDB.generateSQLSvrDAO("#{artifactId}_db", "表名", "classpath*:/META-INF/config/spring/spring-dao.xml", "com.kjt.service.#{artifactId}", "src/main/java/", "src/main/resources/");
        	/**
        	 * my sql 数据访问层代码生成器
        	 */
            DaoGenFromDB.generateDAO("#{artifactId}_db", "表名", "classpath*:/META-INF/config/spring/spring-dao.xml","com.kjt.service.#{artifactId}", "src/main/java/", "src/main/resources/");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
