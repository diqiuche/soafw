package com.kjt.service.tsl.dao;

import com.kjt.service.common.dao.generate.tool.DaoGenFromDB;

public class DaoGen {


    public static void main(String[] args) {
        try {
            DaoGenFromDB.generateDAO("spring-db.xml中的bean id", "表名", "classpath*:/META-INF/config/spring/spring-db.xml",
                    "com.kjt.common.tsl", "src/main/java/", "src/main/resources/");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
