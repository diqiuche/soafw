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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.NotFoundException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import com.kjt.service.common.annotation.SoaFwTest;
import com.kjt.service.common.util.MD5Util;

/**
 * Goal which gen a timestamp file.
 *
 * @goal gen
 * @requiresDependencyResolution compile+runtime
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
    /**
     * 未实现测试&符合正常命名规范的类
     */
    private Map<String, Integer> defines = new HashMap<String, Integer>();
    /**
     * 符合正常命名规范的类&&已经实现过测试用咧
     */
    private Map<String, Integer> testImpl = new HashMap<String, Integer>();

    private Map<String, Integer> errorImpl = new HashMap<String, Integer>();
    private MavenProject project = null;
    private ClassLoader cl = null;
    private String basedPath = null;

    public void execute() throws MojoExecutionException {

        this.getLog().info("start unit test file gen&check: " + artifactId);

        this.getLog().info("" + this.getPluginContext());

        basedPath = basedir.getAbsolutePath();

        project = (MavenProject) getPluginContext().get("project");

        this.getLog().info("ProjectName: " + project.getName());

        try {
            List<String> classpaths = project.getCompileClasspathElements();
            URL[] runtimeUrls = new URL[classpaths.size() + 1];
            for (int i = 0; i < classpaths.size(); i++) {
                String classpath = (String) classpaths.get(i);
                runtimeUrls[i] = new File(classpath).toURI().toURL();
            }
            // 单元测试用例classpath
            runtimeUrls[classpaths.size()] =
                    new File(basedPath + File.separator + "target" + File.separator
                            + "test-classes").toURI().toURL();

            this.cl =
                    new URLClassLoader(runtimeUrls, Thread.currentThread().getContextClassLoader());
        } catch (Exception e2) {}

        String classesDir = basedPath + File.separator + "target" + File.separator + "classes";

        int length = classesDir.length() + 1;

        load(classesDir, new File(classesDir), length);// 得到三个map
        /**
         * 
         */
        String testClassesDir =
                basedPath + File.separator + "target" + File.separator + "test-classes";

        length = testClassesDir.length() + 1;

        load(testClassesDir, new File(testClassesDir), length);// 得到三个map

        int size = defines.size();
        String[] definesArray = new String[size];
        defines.keySet().toArray(definesArray);

        if (size == 0) {
            this.getLog().info("未发现实现接口类");
        }

        for (int i = 0; i < size; i++) {
            boolean hasmethod = false;
            if (!testImpl.containsKey(definesArray[i] + "Test")) {
                genTest(basedPath, definesArray[i]);
            } else {// 已经有实现过的测试类
                appendTest(definesArray[i]);
            }
        }

        for (int i = 0; i < size; i++) {
            if (testImpl.containsKey(definesArray[i] + "Test")) {
                defines.remove(definesArray[i]);
            }
        }

        this.getLog().info("未实现测试&符合正常命名规范的类：" + defines);

        this.getLog().info("符合正常命名规范的类||已经实现过测试用咧：" + testImpl);

        this.getLog().error("异常命名类：" + errorImpl);
    }

    private void genTest(String basedPath, String className) {
        // 该文件没有建立对应测试用例 为实现测试
        this.getLog().info("检测到" + className + "没有测试实现");
        Map<String, Integer> methodCnt = new HashMap<String, Integer>();
        boolean hasmethod = false;
        try {

            Class cls = cl.loadClass(className);

            String testJFileName = cls.getSimpleName() + "Test.java";
            String pkgPath = cls.getPackage().getName().replace(".", File.separator);

            String testJFilePath =
                    basedPath + File.separator + "src" + File.separator + "test" + File.separator
                            + "java" + File.separator + pkgPath;

            int len = 0;
            Class[] inters = cls.getInterfaces();
            if (inters == null || (len = inters.length) == 0) {
                return;
            }

            /**
             * package import
             */
            StringBuffer jHeadBuf = createTestJHeadByClass(cls);

            StringBuffer testJBuf = new StringBuffer();

            testJBuf.append(jHeadBuf);
            Map<String, String> methodDefs = new HashMap<String, String>();
            for (int j = 0; j < len; j++) {
                /**
                 * 接口类方法
                 */
                Class interCls = inters[j];

                this.getLog().info("@interface: " + interCls.getName());

                String name = project.getName();

                Method[] methods = null;

                if (name.endsWith("-dao")) {

                    methods = interCls.getDeclaredMethods();
                } else {
                    methods = interCls.getMethods();
                }

                int mlen = 0;
                if (methods != null && (mlen = methods.length) > 0) {

                    this.getLog().info("开始生成" + className + "Test单元测试类");

                    StringBuffer methodBuf = new StringBuffer();

                    for (int m = 0; m < mlen; m++) {
                        Method method = methods[m];
                        int modf = method.getModifiers();
                        if (modf == 1025) {// 公共方法需要生成
                            hasmethod = true;
                            /**
                             * 单元测试实现的类名＝实现类名＋Test 单元测试方法名=方法名+Test
                             * 单元测试文件生成到:basedPath+File.separator
                             * +src+File.separator+test+File.separator
                             * +pkg+definesArray[i]+Test+.java
                             */
                            if (methodCnt.containsKey(method.getName())) {
                                methodCnt
                                        .put(method.getName(), methodCnt.get(method.getName()) + 1);
                            } else {
                                methodCnt.put(method.getName(), 0);
                            }
                            int cnt = methodCnt.get(method.getName());

                            addMethod(methodDefs, methodBuf, method, cnt);
                        }
                    }

                    testJBuf.append(methodBuf);
                } else {
                    this.getLog().info(className + "没有待测试方法");
                }
            }

            String testJFile = testJBuf.append("}").toString();
            if (hasmethod) {
                write(testJFilePath, testJFileName, testJFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
            this.getLog().info(e.getMessage());
        } catch (Error er) {
            er.printStackTrace();
            this.getLog().info(er.getMessage());
        }

    }

    /**
     * 
     * @param className 待追加实现的类
     */
    private void appendTest(String className) {
        /**
         * 需要检查实现类与测试类单元测试实现范围比较 当发现实现类的public
         * 方法没有单元测试实现时则抛出异常MojoExecutionException，不做覆盖（保证已经实现的不被覆盖）
         */
        try {

            Map<String, Integer> methodCnt = new HashMap<String, Integer>();
            boolean hasmethod = false;

            Map<String, String> methodDefs = new HashMap<String, String>();
            Class cls = cl.loadClass(className);// 加载业务实现类
            Class[] inters = cls.getInterfaces();
            int len = 0;
            if (inters == null || (len = inters.length) == 0) {
                return;
            }
            for (int i = 0; i < len; i++) {

                Class interCls = inters[i];

                this.getLog().info("@interface: " + interCls.getName());

                String name = "tsl-service-impl";//project.getName();

                Method[] methods = null;

                if (name.endsWith("-dao")) {
                    methods = interCls.getDeclaredMethods();
                } else {
                    methods = interCls.getMethods();
                }

                int mlen = 0;
                if (methods != null && (mlen = methods.length) > 0) {

                    StringBuffer methodBuf = new StringBuffer();

                    for (int m = 0; m < mlen; m++) {
                        Method method = methods[m];
                        int modf = method.getModifiers();
                        if (modf == 1025) {// 公共方法需要生成
                            hasmethod = true;
                            /**
                             * 单元测试实现的类名＝实现类名＋Test 单元测试方法名=方法名+Test
                             * 单元测试文件生成到:basedPath+File.separator
                             * +src+File.separator+test+File.separator
                             * +pkg+definesArray[i]+Test+.java
                             */
                            if (methodCnt.containsKey(method.getName())) {
                                methodCnt
                                        .put(method.getName(), methodCnt.get(method.getName()) + 1);
                            } else {
                                methodCnt.put(method.getName(), 0);
                            }
                            int cnt = methodCnt.get(method.getName());

                            addMethod(methodDefs, methodBuf, method, cnt);
                        }
                    }
                }
            }

            Class tstCls = cl.loadClass(className + "Test");// 测试实现类

            Method[] methods = tstCls.getDeclaredMethods();
            len = methods == null ? 0 : methods.length;
            this.getLog().info("测试类" + tstCls.getSimpleName() + "的方法数量：" + len);

            /**
             * 提起所有public的方法
             */
            for (int m = 0; m < len; m++) {

                Method method = methods[m];
                SoaFwTest test = method.getAnnotation(SoaFwTest.class);
                if (test == null) {
                    this.getLog().info(tstCls.getSimpleName()+" method "+method.getName()+"没有声明SoaFwTest");
                    continue;
                }

                String id = test.id();

                if (methodDefs.containsKey(id)) {
                    methodDefs.remove(id);
                }
            }

            if ((len = methodDefs.size()) == 0) {
                return;
            }

            String[] methodImpls = new String[len];
            methodDefs.keySet().toArray(methodImpls);
            // TODO 加载 单元测试源代码
            this.getLog().info("加载单元测试源代码");

            StringBuilder src = new StringBuilder();

            String srcs = readTestSrc(className);

            int index = srcs.lastIndexOf("}");

            this.getLog().info(srcs);
            this.getLog().info("lastIndexOf(}):" + index);
            String impls = srcs.substring(0, index - 1);

            src.append(impls);

            src.append("\n");

            StringBuilder appends = new StringBuilder();
            this.getLog().info("开始追加测试框架代码");
            for (int i = 0; i < len; i++) {
                String methodId = methodImpls[i];
                String method = methodDefs.get(methodId);
                appends.append(method);
                appends.append("\n");
            }

            src.append(appends.toString());

            src.append("}");

            Package pkg = tstCls.getPackage();
            String pkgName = pkg.getName();
            String pkgPath = pkgName.replace(".", File.separator);

            String testBaseSrcPath =
                    basedPath + File.separator + "src" + File.separator + "test" + File.separator
                            + "java";
            String testSrcFullPath = testBaseSrcPath + File.separator + pkgPath;

            write(testSrcFullPath, tstCls.getSimpleName() + ".java", src.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error er) {
            er.printStackTrace();
        }
    }

    private String readTestSrc(String className) {
        BufferedReader br = null;
        String test = className + "Test";
        StringBuilder testSrc = new StringBuilder();
        try {

            Class tstCls = cl.loadClass(test);

            Package pkg = tstCls.getPackage();
            String pkgName = pkg.getName();

            String relativePath = pkgName.replace(".", File.separator);

            String testBaseSrcPath =
                    basedPath + File.separator + "src" + File.separator + "test" + File.separator
                            + "java";

            String testSrcFullPath =
                    testBaseSrcPath + File.separator + relativePath + File.separator
                            + tstCls.getSimpleName() + ".java";

            br =
                    new BufferedReader(new InputStreamReader(new FileInputStream(testSrcFullPath),
                            "UTF-8"));

            String line = null;
            while ((line = br.readLine()) != null) {
                testSrc.append(line);
                testSrc.append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {}
            }
            System.out.println(testSrc);
            return testSrc.toString();
        }

    }

    /**
     * 非接口&&抽象类 并且是 public 或者 protected
     * 
     * @param file
     * @return
     */
    private Map<String, Integer> load(String classesDir, File file, int length) {

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            int len = files.length;
            for (int i = 0; i < len; i++) {
                load(classesDir, files[i], length);
            }
        } else {
            String path = file.getAbsolutePath();
            if (path.indexOf(classesDir) == 0 && path.length() > length) {
                String pkgPath = path.substring(length);
                try {
                    String os = System.getProperty("os.name");
                    String exp = File.separator;
                    if (os.toLowerCase().indexOf("window") >= 0) {
                        exp = "\\\\";
                    }
                    String tmpFile = pkgPath.replaceAll(exp, ".");

                    if (tmpFile.endsWith(".class") && !tmpFile.endsWith("Mapper.class")) {

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
                    e.printStackTrace();
                    this.getLog().info(e.getMessage());
                } catch (Error er) {
                    er.printStackTrace();
                    this.getLog().info(er.getMessage());
                }
            }
        }
        return defines;
    }

    private void write(String dest, String template, String tpl) throws MojoExecutionException {
        // FileWriter fw = null;
        OutputStreamWriter osw = null;
        try {
            new File(dest).mkdirs();
            osw =
                    new OutputStreamWriter(new FileOutputStream(dest + File.separator + template),
                            "UTF-8");
            // fw = new FileWriter(dest + File.separator + template);
            osw.write(tpl);
        } catch (IOException e) {
            throw new MojoExecutionException("", e);
        } finally {
            try {
                if (osw != null) {
                    osw.close();
                }
            } catch (IOException e) {}
        }
    }

    private void addMethod(Map<String, String> methodMap, StringBuffer methodBuffer, Method method,
            int cnt) throws NotFoundException {
        String methodName = method.getName();

        StringBuilder tmpMethodBuffer = new StringBuilder();

        tmpMethodBuffer.append("\t@Test\n");

        Class[] types = method.getParameterTypes();
        int size = types == null ? 0 : types.length;
        StringBuffer paramsSb = new StringBuffer("\"" + methodName + "\"");
        for (int i = 0; i < size; i++) {
            paramsSb.append(",");
            paramsSb.append("\"" + types[i].getSimpleName() + "\"");
        }

        String id = MD5Util.md5Hex("\"" + methodName + "\"" + paramsSb.toString() + ")");

        tmpMethodBuffer.append("\t@SoaFwTest(id=\"" + id + "\", method=\"" + methodName
                + "\", params={" + paramsSb.toString() + "})\n");

        if (cnt > 0) {
            methodName = methodName + "$" + cnt;
        }
        tmpMethodBuffer.append("\tpublic void " + methodName + "() {\n");
        size = types == null ? 0 : types.length;
        if (size > 0) {
            tmpMethodBuffer.append("\t\t//待测试方法参数类型定义参考： ");
        }
        for (int i = 0; i < size; i++) {
            tmpMethodBuffer.append(types[i].getSimpleName() + "\t");
        }
        tmpMethodBuffer.append("\n");
        tmpMethodBuffer.append("\t\tthrow new RuntimeException(\""+methodName+" test not implemented\");\n");
        tmpMethodBuffer.append("\t}\n");

        methodMap.put(id, tmpMethodBuffer.toString());

        methodBuffer.append(tmpMethodBuffer);
    }

    private StringBuffer createTestJHeadByClass(Class cls) {
        StringBuffer jHeadBuf = new StringBuffer();

        jHeadBuf.append("package " + cls.getPackage().getName() + "; \n");
        jHeadBuf.append("\n");
        jHeadBuf.append("import org.junit.Test;\n");
        jHeadBuf.append("import org.junit.runner.RunWith;\n");
        jHeadBuf.append("import org.springframework.test.context.ContextConfiguration;\n");
        jHeadBuf.append("import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;\n\n");
        jHeadBuf.append("import com.kjt.service.common.annotation.SoaFwTest;\n");
        jHeadBuf.append("@RunWith(SpringJUnit4ClassRunner.class)\n");
        String name = project.getName();
        String suffix = "dao";
        if (name.endsWith("-common")) {
            suffix = "common";
        } else if (name.endsWith("-config")) {
            suffix = "config";
        } else if (name.endsWith("-cache")) {
            suffix = "cache";
        } else if (name.endsWith("-dao")) {
            suffix = "dao";
        } else if (name.endsWith("-mq")) {
            suffix = "mq";
        } else if (name.endsWith("-rpc")) {
            suffix = "rpc";
        } else if (name.endsWith("-job")) {
            suffix = "job";
        } else if (name.endsWith("-web")) {
            suffix = "dubbo";
        } else if (name.endsWith("-domain") || name.endsWith("-service")
                || name.endsWith("-service-impl")) {
            suffix = "service";
        }

        jHeadBuf.append("@ContextConfiguration( locations = { \"classpath*:/META-INF/config/spring/spring-"
                + suffix + ".xml\"})\n");

        jHeadBuf.append("public class " + cls.getSimpleName() + "Test {\n");
        return jHeadBuf;
    }

    public static void main(String[] args) {
        String[] path =
                {
                        "/Users/alexzhu/soa/projects/tsl/tsl-service-impl/target/classes",
                        "/Users/alexzhu/.m2/repository/com/kjt/service/common/soafw-common-service-impl/1.0-SNAPSHOT/soafw-common-service-impl-1.0-SNAPSHOT.jar",
                        "/Users/alexzhu/.m2/repository/com/kjt/service/common/soafw-common-dao/1.0-SNAPSHOT/soafw-common-dao-1.0-SNAPSHOT.jar",
                        "/Users/alexzhu/.m2/repository/com/kjt/service/common/soafw-common-config/1.0-SNAPSHOT/soafw-common-config-1.0-SNAPSHOT.jar",
                        "/Users/alexzhu/.m2/repository/org/apache/maven/maven-plugin-api/2.0/maven-plugin-api-2.0.jar",
                        "/Users/alexzhu/.m2/repository/com/kjt/service/common/soafw-common-log/1.0-SNAPSHOT/soafw-common-log-1.0-SNAPSHOT.jar",
                        "/Users/alexzhu/.m2/repository/ch/qos/logback/logback-classic/1.1.2/logback-classic-1.1.2.jar",
                        "/Users/alexzhu/.m2/repository/ch/qos/logback/logback-core/1.1.2/logback-core-1.1.2.jar",
                        "/Users/alexzhu/.m2/repository/com/kjt/service/common/soafw-common-cache/1.0-SNAPSHOT/soafw-common-cache-1.0-SNAPSHOT.jar",
                        "/Users/alexzhu/.m2/repository/com/danga/memcached/2.0.1/memcached-2.0.1.jar",
                        "/Users/alexzhu/.m2/repository/redis/clients/jedis/2.6.2/jedis-2.6.2.jar",
                        "/Users/alexzhu/.m2/repository/org/apache/commons/commons-pool2/2.0/commons-pool2-2.0.jar",
                        "/Users/alexzhu/.m2/repository/org/aspectj/aspectjrt/1.8.4/aspectjrt-1.8.4.jar",
                        "/Users/alexzhu/.m2/repository/org/aspectj/aspectjweaver/1.8.4/aspectjweaver-1.8.4.jar",
                        "/Users/alexzhu/.m2/repository/aopalliance/aopalliance/1.0/aopalliance-1.0.jar",
                        "/Users/alexzhu/.m2/repository/org/mybatis/mybatis/3.2.8/mybatis-3.2.8.jar",
                        "/Users/alexzhu/.m2/repository/commons-pool/commons-pool/1.5.4/commons-pool-1.5.4.jar",
                        "/Users/alexzhu/.m2/repository/commons-dbcp/commons-dbcp/1.4/commons-dbcp-1.4.jar",
                        "/Users/alexzhu/.m2/repository/org/mybatis/mybatis-spring/1.2.2/mybatis-spring-1.2.2.jar",
                        "/Users/alexzhu/.m2/repository/org/freemarker/freemarker/2.3.16/freemarker-2.3.16.jar",
                        "/Users/alexzhu/.m2/repository/org/springframework/spring-core/4.0.7.RELEASE/spring-core-4.0.7.RELEASE.jar",
                        "/Users/alexzhu/.m2/repository/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar",
                        "/Users/alexzhu/.m2/repository/org/springframework/spring-context/4.0.7.RELEASE/spring-context-4.0.7.RELEASE.jar",
                        "/Users/alexzhu/.m2/repository/org/springframework/spring-aop/4.0.7.RELEASE/spring-aop-4.0.7.RELEASE.jar",
                        "/Users/alexzhu/.m2/repository/org/springframework/spring-beans/4.0.7.RELEASE/spring-beans-4.0.7.RELEASE.jar",
                        "/Users/alexzhu/.m2/repository/org/springframework/spring-expression/4.0.7.RELEASE/spring-expression-4.0.7.RELEASE.jar",
                        "/Users/alexzhu/.m2/repository/org/springframework/spring-test/4.0.7.RELEASE/spring-test-4.0.7.RELEASE.jar",
                        "/Users/alexzhu/.m2/repository/org/springframework/spring-jdbc/4.0.7.RELEASE/spring-jdbc-4.0.7.RELEASE.jar",
                        "/Users/alexzhu/.m2/repository/org/springframework/spring-tx/4.0.7.RELEASE/spring-tx-4.0.7.RELEASE.jar",
                        "/Users/alexzhu/.m2/repository/com/kjt/service/common/soafw-common-concurrent/1.0-SNAPSHOT/soafw-common-concurrent-1.0-SNAPSHOT.jar",
                        "/Users/alexzhu/.m2/repository/com/kjt/service/common/soafw-common-mq/1.0-SNAPSHOT/soafw-common-mq-1.0-SNAPSHOT.jar",
                        "/Users/alexzhu/.m2/repository/com/rabbitmq/amqp-client/3.5.0/amqp-client-3.5.0.jar",
                        "/Users/alexzhu/.m2/repository/org/springframework/amqp/spring-amqp/1.4.3.RELEASE/spring-amqp-1.4.3.RELEASE.jar",
                        "/Users/alexzhu/.m2/repository/org/springframework/amqp/spring-rabbit/1.4.3.RELEASE/spring-rabbit-1.4.3.RELEASE.jar",
                        "/Users/alexzhu/.m2/repository/org/springframework/spring-messaging/4.1.3.RELEASE/spring-messaging-4.1.3.RELEASE.jar",
                        "/Users/alexzhu/.m2/repository/org/springframework/retry/spring-retry/1.1.2.RELEASE/spring-retry-1.1.2.RELEASE.jar",
                        "/Users/alexzhu/.m2/repository/com/kjt/service/common/soafw-common-rpc/1.0-SNAPSHOT/soafw-common-rpc-1.0-SNAPSHOT.jar",
                        "/Users/alexzhu/.m2/repository/com/kjt/service/common/soafw-common-service/1.0-SNAPSHOT/soafw-common-service-1.0-SNAPSHOT.jar",
                        "/Users/alexzhu/.m2/repository/com/kjt/service/common/soafw-common-domain/1.0-SNAPSHOT/soafw-common-domain-1.0-SNAPSHOT.jar",
                        "/Users/alexzhu/.m2/repository/com/kjt/service/common/soafw-common-util/1.0-SNAPSHOT/soafw-common-util-1.0-SNAPSHOT.jar",
                        "/Users/alexzhu/.m2/repository/commons-lang/commons-lang/2.5/commons-lang-2.5.jar",
                        "/Users/alexzhu/.m2/repository/commons-configuration/commons-configuration/1.6/commons-configuration-1.6.jar",
                        "/Users/alexzhu/.m2/repository/commons-collections/commons-collections/3.2.1/commons-collections-3.2.1.jar",
                        "/Users/alexzhu/.m2/repository/commons-digester/commons-digester/1.8/commons-digester-1.8.jar",
                        "/Users/alexzhu/.m2/repository/commons-beanutils/commons-beanutils/1.7.0/commons-beanutils-1.7.0.jar",
                        "/Users/alexzhu/.m2/repository/commons-beanutils/commons-beanutils-core/1.8.0/commons-beanutils-core-1.8.0.jar",
                        "/Users/alexzhu/.m2/repository/javax/mail/mail/1.4.1/mail-1.4.1.jar",
                        "/Users/alexzhu/.m2/repository/javax/activation/activation/1.1/activation-1.1.jar",
                        "/Users/alexzhu/.m2/repository/org/springframework/spring-context-support/4.0.7.RELEASE/spring-context-support-4.0.7.RELEASE.jar",
                        "/Users/alexzhu/.m2/repository/org/apache/curator/curator-framework/2.4.0/curator-framework-2.4.0.jar",
                        "/Users/alexzhu/.m2/repository/org/apache/curator/curator-client/2.4.0/curator-client-2.4.0.jar",
                        "/Users/alexzhu/.m2/repository/com/google/guava/guava/18.0/guava-18.0.jar",
                        "/Users/alexzhu/.m2/repository/org/apache/curator/curator-recipes/2.4.0/curator-recipes-2.4.0.jar",
                        "/Users/alexzhu/.m2/repository/javax/validation/validation-api/1.1.0.Final/validation-api-1.1.0.Final.jar",
                        "/Users/alexzhu/.m2/repository/com/kjt/service/tsl/tsl-service/1.0-SNAPSHOT/tsl-service-1.0-SNAPSHOT.jar",
                        "/Users/alexzhu/.m2/repository/com/kjt/service/tsl/tsl-domain/1.0-SNAPSHOT/tsl-domain-1.0-SNAPSHOT.jar",
                        "/Users/alexzhu/.m2/repository/com/kjt/service/tsl/tsl-dao/1.0-SNAPSHOT/tsl-dao-1.0-SNAPSHOT.jar",
                        "/Users/alexzhu/.m2/repository/com/kjt/service/tsl/tsl-cache/1.0-SNAPSHOT/tsl-cache-1.0-SNAPSHOT.jar",
                        "/Users/alexzhu/.m2/repository/com/kjt/service/tsl/tsl-rpc/1.0-SNAPSHOT/tsl-rpc-1.0-SNAPSHOT.jar",
                        "/Users/alexzhu/.m2/repository/com/kjt/service/tsl/tsl-config/1.0-SNAPSHOT/tsl-config-1.0-SNAPSHOT.jar",
                        "/Users/alexzhu/.m2/repository/com/kjt/service/tsl/tsl-common/1.0-SNAPSHOT/tsl-common-1.0-SNAPSHOT.jar",
                        "/Users/alexzhu/.m2/repository/com/kjt/service/tsl/tsl-mq/1.0-SNAPSHOT/tsl-mq-1.0-SNAPSHOT.jar",
                        "/Users/alexzhu/.m2/repository/com/alibaba/dubbo/2.5.3/dubbo-2.5.3.jar",
                        "/Users/alexzhu/.m2/repository/org/javassist/javassist/3.15.0-GA/javassist-3.15.0-GA.jar",
                        "/Users/alexzhu/.m2/repository/org/jboss/netty/netty/3.2.5.Final/netty-3.2.5.Final.jar",
                        "/Users/alexzhu/.m2/repository/org/apache/zookeeper/zookeeper/3.4.6/zookeeper-3.4.6.jar",
                        "/Users/alexzhu/.m2/repository/org/slf4j/slf4j-api/1.7.5/slf4j-api-1.7.5.jar",
                        "/Users/alexzhu/.m2/repository/jline/jline/0.9.94/jline-0.9.94.jar",
                        "/Users/alexzhu/.m2/repository/io/netty/netty/3.7.0.Final/netty-3.7.0.Final.jar",
                        "/Users/alexzhu/.m2/repository/com/101tec/zkclient/0.4/zkclient-0.4.jar",
                        "/Users/alexzhu/.m2/repository/log4j/log4j/1.2.14/log4j-1.2.14.jar",
                        "/Users/alexzhu/.m2/repository/commons-net/commons-net/3.3/commons-net-3.3.jar"};
        String basedPath = "/Users/alexzhu/soa/projects/tsl/tsl-service-impl";
        
        URL[] runtimeUrls = new URL[path.length + 1];
        try {
        for(int i=0;i<path.length;i++){
                runtimeUrls[i] = new File(path[i]).toURI().toURL();
                // 单元测试用例classpath
        }
        
        runtimeUrls[path.length] =
                new File(basedPath + File.separator + "target" + File.separator
                        + "test-classes").toURI().toURL();
        SoafwTesterMojo mojo = new SoafwTesterMojo();
        
        mojo.cl =
                new URLClassLoader(runtimeUrls, Thread.currentThread().getContextClassLoader());
        mojo.appendTest("com.kjt.service.tsl.CfOrderInfoServiceImpl");
        } catch (Exception e2) {}
        
    }
}
