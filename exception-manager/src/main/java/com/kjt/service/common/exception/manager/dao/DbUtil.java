package com.kjt.service.common.exception.manager.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by kevin on 15/1/6.
 */
public class DbUtil {

    private static Properties dbProperties=null;
    private static final String DB_DRIVER="jdbc.driver";
    private static final String DB_USER="jdbc.user";
    private static final String DB_URL="jdbc.url";
    private static final String DB_PWD="jdbc.pwd";
    private static final String DB_CONFIG="/database.properties";
    private static String getConfig(String key){
        if(dbProperties==null){
            loadProperties();
        }
        return dbProperties.getProperty(key);
    }
    private static void loadProperties(){
        dbProperties=new Properties();
        try {
            dbProperties.load(DbUtil.class.getResourceAsStream(DB_CONFIG));
        } catch (IOException e) {
           // e.printStackTrace();
        }
    }

    public static void closeConnection(Connection connection){
        if(connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
               // e.printStackTrace();
            }
        }
    }

    public static Connection getConnection(){
        try {
            Class.forName(getConfig(DB_DRIVER));
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        }
        try {
            Connection connection= DriverManager.getConnection(
                    getConfig(DB_URL), getConfig(DB_USER), getConfig(DB_PWD));
            return  connection;
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        return null;
    }
}
