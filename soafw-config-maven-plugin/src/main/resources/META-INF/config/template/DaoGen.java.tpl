package com.kjt.service.#{artifactId}.dao;

import com.kjt.service.common.dao.generate.tool.DaoGenFromDB;

public class DaoGen {


    public static void main(String[] args) {
        try {
        	/**
        	 * sql server 数据访问层代码生成器
        	 */
        	//DaoGenFromDB.generateSQLSvrDAO("spring-db.xml中的bean id", "表名", "classpath*:/META-INF/config/spring/spring-db.xml", "com.kjt.common.tsl", "src/main/java/", "src/main/resources/");
        	/**
        	 * my sql 数据访问层代码生成器
        	 */
            DaoGenFromDB.generateDAO("spring-db.xml中的bean id", "表名", "classpath*:/META-INF/config/spring/spring-db.xml","com.kjt.common.tsl", "src/main/java/", "src/main/resources/");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
