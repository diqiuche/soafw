package com.kjt.service.common.config.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


public class ConfigGeneatorPlugin extends AbstractMojo {
    /**
     *
     * @parameter property="project.groupId"
     */
    private String groupId;

    /**
     *
     * @parameter property="project.artifactId"
     */
    private String artifactId;
    /**
     * jetty port
     * @parameter property="port"
     */
    private String port="8080";
    /**
     * jetty stopPort
     * @parameter property="stopPort"   
     */
    private String stopPort="9999";
    /**
     * 生成的目标目录
     * @parameter property="destDir"   
     */
    private String destDir=".";
    /**
     * 模版文件名
     * @parameter property="template"   
     */
    private String template;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        groupId = System.getProperty("groupId", groupId);
        artifactId = System.getProperty("artifactId", artifactId);
        port = System.getProperty("port",port);
        stopPort = System.getProperty("stopPort",stopPort);
        destDir = System.getProperty("destDir",destDir);
        template = System.getProperty("destDir",template);
        
        /**
         * read template
         */
        
        /**
         * write to dest
         */
    }
    
    public void gen() throws MojoExecutionException, MojoFailureException{
        execute();
    }

}
