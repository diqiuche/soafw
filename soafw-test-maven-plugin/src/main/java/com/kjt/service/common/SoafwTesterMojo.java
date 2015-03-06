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
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

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
    private int lenght;
    private String classesDir = null;
    /**
     * 未实现测试&符合正常命名规范的类
     */
    private Map<String, Integer> defines = new HashMap<String, Integer>();
    /**
     * 符合正常命名规范的类&&已经实现过测试用咧
     */
    private Map<String, Integer> testImpl = new HashMap<String, Integer>();

    private Map<String, Integer> errorImpl = new HashMap<String, Integer>();

    private ClassLoader cl = null;
    
    public void execute() throws MojoExecutionException {
        this.getLog().info("start unit test file gen&check: " + artifactId);
        String basedPath = basedir.getAbsolutePath();
        String testJFilePath;
        classesDir = basedPath + File.separator + "target" + File.separator + "classes";
        lenght = classesDir.length() + 1;
        load(new File(classesDir));//得到三个map
        StringBuffer testJBuf=new StringBuffer();
        StringBuffer methodBuf=new StringBuffer();
        int size = defines.size();
        String[] definesArray = new String[size];
        defines.keySet().toArray(definesArray);
        this.cl=ClassLoader.getSystemClassLoader();
        for (int i = 0; i < size; i++) {

            if (!testImpl.containsKey(definesArray[i] + "Test")) {// 该文件没有建立对应测试用例 为实现测试
                
                try {
                    Class cls = cl.loadClass(definesArray[i]);
                    Method[] methods = cls.getMethods();
                    
                    String pkgPath = cls.getPackage().getName().replace(".", File.separator);
                    
                    
                    testJFilePath=basedPath+File.separator+"src"+File.separator
                            +"test"+File.separator+"java"+File.separator+pkgPath;
                    
                    String  testJFileName=cls.getSimpleName()+"Test.java";
                    
                    
                    StringBuffer jHeadBuf=createTestJHeadByClass(cls);
                    testJBuf.append(jHeadBuf);
                    int len = 0;
                    if (methods != null && (len = methods.length) > 0) {

                        for (int m = 0; m < len; m++) {
                            int modf = methods[m].getModifiers();
                            if (modf == 1) {// 公共方法需要生成
                                // TODO
                                /**
                                 * 单元测试实现的类名＝实现类名＋Test 单元测试方法名=方法名+Test
                                 * 单元测试文件生成到:basedPath+File.separator
                                 * +src+File.separator+test+File.separator+pkg+definesArray[i]+Test+.java
                                 */
                                addMethod(methodBuf,methods[m].getName());

                            }
                        }
                    }
                    String testJFile=testJBuf.append(methodBuf).append("}").toString();
                    write(testJFilePath, testJFileName, testJFile);
                    
                    
                } catch (Exception e) {
                    this.getLog().info(e.getMessage());
                } catch (Error er) {
                    this.getLog().info(er.getMessage());
                }

                
            } else {// 已经有实现过的测试类
                /**
                 * 需要检查实现类与测试类单元测试实现范围比较 当发现实现类的public
                 * 方法没有单元测试实现时则抛出异常MojoExecutionException，不做覆盖（保证已经实现的不被覆盖）
                 */
                try {
                    
                    Map<String,Method> defs = new HashMap<String,Method>();//Key:为当前方法类型＋变量，...字符串md5值
                    Class cls = cl.loadClass(definesArray[i]);// 加载业务实现类
                    Method[] methods = cls.getMethods();
                    int len = 0;
                    if (methods != null && (len = methods.length) > 0) {
                        /**
                         * 提起所有public的方法
                         */
                        for (int m = 0; m < len; m++) {
                            Method tmp = methods[i];
                            int modf = tmp.getModifiers();
                            if (modf == 1) {
                                /**
                                 * 公共方法
                                 */
                                tmp.getParameterTypes();
                                tmp.getTypeParameters();
                                /**
                                 * 构造方法唯一标示
                                 */
                            }
                        }
                    }
                    
                    Map<String,Method> tsts = new HashMap<String,Method>();
                    Class tstCls = cl.loadClass(definesArray[i] + "Test");// 加载单元测试的实现类
                    Method[] tstMethods = tstCls.getMethods();
                    if (tstMethods != null && (len = tstMethods.length) > 0) {
                        /**
                         * 提起所有public的方法
                         */
                        for (int m = 0; m < len; m++) {
                            Method tmp = tstMethods[i];
                            int modf = tmp.getModifiers();
                            if (modf == 1) {
                                /**
                                 * 公共方法
                                 */
                                //tmp.getAnnotation(org.junit.Test.class);
                                tmp.getParameterTypes();
                                tmp.getTypeParameters();
                                /**
                                 * 构造方法唯一标示
                                 */
                            }
                        }
                    }

                } catch (Exception e) {
                    this.getLog().info(e.getMessage());
                } catch (Error er) {
                    this.getLog().info(er.getMessage());
                }
            }
        }

    }
   
    /**
     * 非接口&&抽象类 并且是 public 或者 protected
     * 
     * @param file
     * @return
     */
    private Map<String, Integer> load(File file) {

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            int len = files.length;
            for (int i = 0; i < len; i++) {
                load(files[i]);
            }
        } else {
            String path = file.getAbsolutePath();
            if(path.indexOf(classesDir)==0 && path.length()>lenght){
                String pkgPath = path.substring(lenght);
                String tmpFile = pkgPath.replaceAll(File.separator, ".");
                try {
                    if (tmpFile.endsWith(".class")) {
                        // && !tmpFile.endsWith("Mapper.class")
                        String clsName = tmpFile.replaceAll(".class", "");
                        Class cls = cl.loadClass(clsName);
                        int modf = cls.getModifiers();
                        if (modf != 1537 && modf != 1025) {// 非接口 && 抽象类
                            if (clsName.endsWith("ImplTest")) {
                                testImpl.put(clsName, 1);
                            } else if (clsName.endsWith("Impl")) {
                                defines.put(clsName, 1);
                            } else {// 异常命名的类
                                errorImpl.put(clsName, 1);
                            }
                        }
                    }
                } catch (Exception e) {
                    this.getLog().info(e.getMessage());
                } catch (Error er) {
                    this.getLog().info(er.getMessage());
                }
            }
        }
        return defines;
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
    private void addMethod(StringBuffer methodBuffer,String methodName){
        methodBuffer.append("  @Test\n");
        methodBuffer.append("  public void "+methodName+"() {\n");
        methodBuffer.append("    throw new RuntimeException(\"Test not implemented\");");
        methodBuffer.append(" }\n");
        methodBuffer.append("\n");
        
        
    }
    private StringBuffer createTestJHeadByClass(Class cls){
        StringBuffer jHeadBuf=new StringBuffer();
        jHeadBuf.append("package " + cls.getPackage().getName() +"; \n");
        jHeadBuf.append("import org.testng.annotations.Test; \n");
        jHeadBuf.append("public class "+cls.getSimpleName()+"Test {");
        return jHeadBuf;
    }

    public static void main(String[] args) {

        SoafwTesterMojo mojo = new SoafwTesterMojo();

        String basedPath = "/Users/alexzhu/soa/soafw/soafw-common-dao";

        mojo.classesDir = basedPath + File.separator + "target" + File.separator + "classes";
        
        try {
            mojo.cl = new URLClassLoader(new URL[] {new File(mojo.classesDir).toURI().toURL()});
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mojo.lenght = mojo.classesDir.length() + 1;
        mojo.load(new File(mojo.classesDir));
        System.out.println(mojo.defines);
        System.out.println(mojo.testImpl);
        int size = mojo.defines.size();
        String[] definesArray = new String[size];
        mojo.defines.keySet().toArray(definesArray);
        Class cls = null;
        int val = 0;
        for (int i = 0; i < size; i++) {
            try {
                cls = mojo.cl.loadClass(definesArray[i]);
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            val = cls.getModifiers();

            System.out.println(definesArray[i] + " Modifiers: " + val);

            if (!mojo.testImpl.containsKey(definesArray[i] + "Test")) {

                try {
                    cls = mojo.cl.loadClass(definesArray[i]);
                    val = cls.getModifiers();
                    System.out.println(definesArray[i] + " Modifiers: " + val);
                    if (val != 1025) {
                        // TODO 生成测试框架代码
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } catch (Error er) {
                    System.err.println(er.getMessage());
                }
            }
        }
    }
}
