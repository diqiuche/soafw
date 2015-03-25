package com.kjt.service.common;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which touches a timestamp file.
 *
 * @goal config
 * 
 * @phase process-sources
 */
public class SoafwConfigMojo extends AbstractMojo {
    
    private String serviceId = "1";
    private String startPort = "8080";
    private String stopPort = "9999";
    private String servicePort = "20880";
    private String groupId = "";
    private String artifactId = "";
    private String destDir = ".";
    private String template = "";
    private String sufix = "xml";
    private String module;
    private String moduleSuffix = "";

    public void execute() throws MojoExecutionException {
        groupId = System.getProperty("groupId");
        artifactId = System.getProperty("artifactId");
        destDir = System.getProperty("destDir", destDir);
        template = System.getProperty("template", template);
        sufix = System.getProperty("sufix", sufix);
        String model = System.getProperty("model", "split");
        module = System.getProperty("genModule", "all");
        moduleSuffix = System.getProperty("moduleSuffix", "").trim();
        
        getServiceInfo();

        this.getLog().info(
                format(model, groupId, artifactId, startPort, stopPort,
                        new File(destDir).getAbsolutePath(), template));

        if ("split".equalsIgnoreCase(model)) {
            /**
             * read template
             */
            String tpl = getTemplate(template + ".tpl");
            tpl = format(tpl, "groupId", groupId);
            tpl = format(tpl, "artifactId", artifactId);
            tpl = format(tpl, "startPort", startPort);
            tpl = format(tpl, "stopPort", stopPort);
            tpl = format(tpl,"servicePort",servicePort);
            tpl = format(tpl,"serviceId",serviceId);
            this.getLog().info(tpl);
            /**
             * write to dest
             */
            write(destDir, template, tpl, sufix);
        } else {

            this.getLog().info("genModule: " + module);
            this.getLog().info("moduleSuffix: " + moduleSuffix);
            doConfig(destDir, artifactId);
        }

    }

    private String dbDriver = "com.mysql.jdbc.Driver";

    public Connection getConn() {
        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String dburl = "jdbc:mysql://192.168.1.110:3306/soafw_db";
        String dbuser = "root";
        String dbpwd = "Kjt@)!$";
        try {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println(">>dburl: " + dburl);
            System.out.println(">>dbuser: " + dbuser);
            System.out.println(">>dbpwd: " + dbpwd);
            Connection connection = DriverManager.getConnection(dburl, dbuser, dbpwd);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void getServiceInfo(){
        Connection conn = getConn();
        String checkSql = "select * from soa_provider where sp_name='" + artifactId + "'";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(checkSql);
            int id = 0;
            int start = 0;
            int stop = 0;
            int service = 0;
            if (rs.next()) {
                id = rs.getInt("id");
                serviceId = String.valueOf(id);
                start = rs.getInt("start_port");
                startPort = String.valueOf(start);
                stop = rs.getInt("stop_port");
                stopPort = String.valueOf(stop);
                service = rs.getInt("service_port");
                servicePort = String.valueOf(service);
            }
            else{
                String sql =
                        "select max(id) as id,max(start_port) as start_port,max(`stop_port`) as stop_port,max(`service_port`) as service_port from soa_provider";
                rs = st.executeQuery(sql);
                if(rs.next()){
                    id = rs.getInt("id")+1;
                    serviceId = String.valueOf(id);
                    start = rs.getInt("start_port")+1;
                    startPort = String.valueOf(start);
                    stop = rs.getInt("stop_port")+1;
                    stopPort = String.valueOf(stop);
                    service = rs.getInt("service_port")+1;
                    servicePort = String.valueOf(service);
                }
                sql = "insert into `soa_provider` (id,sp_name,start_port,stop_port,service_port,sp_description) values ("+id+",'"+artifactId+"',"+start+","+stop+","+service+",'"+artifactId+"')";
                st.execute(sql);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private String format(String... args) {
        return MessageFormat
                .format("config: flag={0},groupId={1},artifactId={2},startPort={3},stopPort={4},toDest={5},template={6}",
                        args);
    }

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private String getTemplate(String template) throws MojoExecutionException {
        this.getLog().info("template: "+template);
        InputStream is =
                this.getClass().getClassLoader()
                        .getResourceAsStream("META-INF/config/template/" + template);

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        StringBuffer stringBuffer = new StringBuffer();
        try {
            String s;
            while ((s = br.readLine()) != null) {
                stringBuffer.append(s + LINE_SEPARATOR);
            }
        } catch (IOException e) {
            throw new MojoExecutionException("获取模版文件失败");
        }
        return stringBuffer.toString();
    }

    private String format(String tpl, String pattern, String content) {
        if (content == null) {
            return tpl;
        }
        tpl = tpl.replace("#{" + pattern + "}", content);
        return tpl;
    }

    private void write(String dest, String template, String tpl, String sufix)
            throws MojoExecutionException {
        FileWriter fw = null;
        try {
            new File(dest).mkdirs();
            if (sufix.isEmpty()) {
                sufix = ".tpl";
                template = template.substring(0, template.indexOf(sufix));
            } else {
                template = template.substring(0, template.indexOf(sufix)) + sufix;
            }

            fw = new FileWriter(dest + File.separator + template);
            fw.write(tpl);
        } catch (IOException e) {
            throw new MojoExecutionException("", e);
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {}
        }
    }

    private void doConfig(String baseDir, String artifactId) throws MojoExecutionException {

        if (!moduleSuffix.isEmpty() && !moduleSuffix.startsWith("-")) {
            moduleSuffix = "-" + moduleSuffix;
        }
        PropertiesConfiguration configSetting = load();
        String modules = configSetting.getString("modules");
        String[] moduleArray = modules.split(";");
        int len = moduleArray == null ? 0 : moduleArray.length;
        for (int m = 0; m < len; m++) {
            String currentModule = moduleArray[m];
            if (!"all".equals(module) && !module.equals(currentModule)) {
                continue;
            }
            this.getLog().info("start config module: " + currentModule);// service-impl=1;2;3;4;5;6;7
            String moduleIdxs = configSetting.getString(currentModule);
            String[] moduleIdxArray = moduleIdxs.split(";");
            int iLen = moduleIdxArray == null ? 0 : moduleIdxArray.length;
            for (int i = 0; i < iLen; i++) {
                String idx = moduleIdxArray[i];
                String config = currentModule + "." + idx;
                String configs = configSetting.getString(config);

                // sufix;template;destDir
                String[] itemPattern = configs.split(";");
                String sufix = itemPattern[0];
                String templateFile = itemPattern[1] + ".tpl";
                String storeDir = itemPattern[2];

                this.getLog().info("start template: " + templateFile);

                String tpl = this.getTemplate(templateFile);
                if ("SPID.tpl".equals(templateFile)) {
                    this.getLog().info("start template: " + templateFile);
                }
                tpl = format(tpl, "groupId", groupId);
                tpl = format(tpl, "artifactId", artifactId);
                tpl = format(tpl, "startPort", startPort);
                tpl = format(tpl, "stopPort", stopPort);
                tpl = format(tpl, "moduleSuffix", moduleSuffix);
                tpl = format(tpl,"servicePort",servicePort);
                tpl = format(tpl,"serviceId",serviceId);

                this.getLog().info(tpl);

                storeDir = format(storeDir, "artifactId", artifactId);

                storeDir = format(storeDir, "moduleSuffix", moduleSuffix);

                String configToDir = baseDir + File.separator + storeDir;

                this.getLog().info(configToDir);
                
                templateFile = format(templateFile, "artifactId", artifactId);
                
                templateFile = format(templateFile, "moduleSuffix", ((moduleSuffix==null||moduleSuffix.trim().length()==0)?"":moduleSuffix.substring(1)));
                
                write(configToDir, templateFile, tpl, sufix);
            }
        }
    }

    private PropertiesConfiguration load() throws MojoExecutionException {
        PropertiesConfiguration config = new PropertiesConfiguration();
        InputStream is =
                SoafwConfigMojo.class.getClassLoader().getResourceAsStream(
                        "META-INF/config/template/template.properties");
        try {
            config.load(is);
        } catch (ConfigurationException e) {
            throw new MojoExecutionException("获取'template.properties'文件失败");
        }
        return config;
    }

    public static void main(String[] args) {

        URL resource =
                SoafwConfigMojo.class.getClassLoader().getResource(
                        "META-INF/config/template/template.properties");
        SoafwConfigMojo mojo = new SoafwConfigMojo();
        try {
            mojo.module = "all";
            mojo.moduleSuffix = "aa";
            mojo.artifactId="hello";
            mojo.getServiceInfo();
            mojo.doConfig("/Users/alexzhu/soa/projects", "hello");
        } catch (MojoExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(resource);
    }
}
