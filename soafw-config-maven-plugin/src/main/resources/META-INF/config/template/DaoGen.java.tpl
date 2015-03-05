package com.kjt.service.tsl.dao;

import com.kjt.service.common.dao.generate.tool.DaoGenFromDB;

public class DaoGen {


    public static void main(String[] args) {
        try {
            DaoGenFromDB.generateDAO("数据库名", "表名", "classpath:spring-dao.gen.xml",
                    "com.kjt.common.tsl", "src/main/java/", "src/main/resources/");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
