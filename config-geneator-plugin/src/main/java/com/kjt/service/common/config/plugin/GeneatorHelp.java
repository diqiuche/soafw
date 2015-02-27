package com.kjt.service.common.config.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Created by kevin on 14/12/31.
 * @goal help
 */
public class GeneatorHelp  extends AbstractMojo {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        getLog().info("run command : mvn config-geneator:gen -DgroupId=? -DartifactId=? -Dport=? -DstopPort=? -DdestDir=? -Dtemplate=?");
    }
}
