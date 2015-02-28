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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which gen a timestamp file.
 *
 * @goal gen
 * 
 * @phase process-sources
 */
public class SoafwTesterMojo extends AbstractMojo {

    
    /**
     *
     * @parameter property="project.artifactId"
     */
    private String artifactId;
    
    /**
     * @parameter property="project.basedir"
     * @required
     * @readonly
     */
    private File basedir;

    public void execute() throws MojoExecutionException {
        this.getLog().info("hello,tester..............: " + artifactId);
        this.getLog().info("hello,tester..............: " + basedir.getAbsolutePath());
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
            } catch (IOException e) {}
        }
    }
}
