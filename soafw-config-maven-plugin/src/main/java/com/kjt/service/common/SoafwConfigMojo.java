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
import java.text.MessageFormat;

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

    private String startPort = "8080";
    private String stopPort = "9999";
    private String groupId = "";
    private String artifactId = "";
    private String destDir = ".";
    private String template = "";

    public void execute() throws MojoExecutionException {
        groupId = System.getProperty("groupId");
        artifactId = System.getProperty("artifactId");
        startPort = System.getProperty("port", startPort);
        stopPort = System.getProperty("stopPort", stopPort);
        destDir = System.getProperty("destDir", destDir);
        template = System.getProperty("template", template);
        this.getLog().info(
                format(groupId, artifactId, startPort, stopPort,
                        new File(destDir).getAbsolutePath(), template));

        /**
         * read template
         */
        String tpl = getTemplate(template+".tpl");
        tpl = format(tpl, "groupId", groupId);
        tpl = format(tpl, "artifactId", artifactId);
        tpl = format(tpl, "startPort", startPort);
        tpl = format(tpl, "stopPort", stopPort);
        
        this.getLog().info(tpl);
        /**
         * write to dest
         */
        write(destDir, template, tpl);
    }

    private String format(String... args) {
        return MessageFormat
                .format("config: groupId={0},artifactId={1},startPort={2},stopPort={3},toDest={4},template={5}",
                        args);
    }

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private String getTemplate(String template) throws MojoExecutionException {

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
        if (content == null || content.length() == 0) {
            return tpl;
        }
        tpl = tpl.replace("#{" + pattern + "}", content);
        return tpl;
    }

    private void write(String dest, String template, String tpl) throws MojoExecutionException {
        FileWriter fw = null;
        try {
            new File(dest).mkdirs();
            fw = new FileWriter(dest + File.separator + template);
            fw.write(tpl);
        } catch (IOException e) {
            throw new MojoExecutionException("", e);
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
