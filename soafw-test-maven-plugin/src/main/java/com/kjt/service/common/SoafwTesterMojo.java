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
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.NotFoundException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

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

    public void execute() throws MojoExecutionException {

        this.getLog().info("start unit test file gen&check: " + artifactId);

        this.getLog().info("" + this.getPluginContext());

        String basedPath = basedir.getAbsolutePath();

        project = (MavenProject) getPluginContext().get("project");

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
        } catch (Exception e2) {
            e2.printStackTrace();
        }

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

            this.getLog().info("开始生成" + className + "Test单元测试类");

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
            for (int j = 0; j < len; j++) {
                /**
                 * 接口类方法
                 */
                Class interCls = inters[j];
                this.getLog().info("interface: " + interCls.getName());

                Method[] methods = interCls.getMethods();

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

                            addMethod(methodBuf, method, cnt);
                        }
                    }

                    testJBuf.append(methodBuf);
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

    private void appendTest(String className) {
        /**
         * 需要检查实现类与测试类单元测试实现范围比较 当发现实现类的public
         * 方法没有单元测试实现时则抛出异常MojoExecutionException，不做覆盖（保证已经实现的不被覆盖）
         */
        try {

            Map<String, Method> defs = new HashMap<String, Method>();// Key:为当前方法类型＋变量，...字符串md5值
            Class cls = cl.loadClass(className);// 加载业务实现类
            Method[] methods = cls.getMethods();
            int len = 0;
            if (methods != null && (len = methods.length) > 0) {
                /**
                 * 提起所有public的方法
                 */
                for (int m = 0; m < len; m++) {
                    Method tmp = methods[m];
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

            Map<String, Method> tsts = new HashMap<String, Method>();
            Class tstCls = cl.loadClass(className + "Test");// 加载单元测试的实现类
            Method[] tstMethods = tstCls.getMethods();
            if (tstMethods != null && (len = tstMethods.length) > 0) {
                /**
                 * 提起所有public的方法
                 */
                for (int m = 0; m < len; m++) {
                    Method tmp = tstMethods[m];
                    int modf = tmp.getModifiers();
                    if (modf == 1) {
                        /**
                         * 公共方法
                         */
                        Test tst = tmp.getAnnotation(Test.class);
                        if (tst == null) {
                            continue;
                        }
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
                    String tmpFile = pkgPath.replaceAll(File.separator, ".");

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

    private void addMethod(StringBuffer methodBuffer, Method method, int cnt)
            throws NotFoundException {
        String methodName = method.getName();

        if (cnt > 0) {
            methodName = methodName + "$" + cnt;
        }

        methodBuffer.append("\t@Test\n");
        methodBuffer.append("\tpublic void " + methodName + "() {\n");
        Class[] types = method.getParameterTypes();
        int size = types == null ? 0 : types.length;
        if (size > 0) {
            methodBuffer.append("\t\t//待测试方法参数类型定义参考： ");
        }
        for (int i = 0; i < size; i++) {
            methodBuffer.append(types[i].getSimpleName() + "\t");
        }
        methodBuffer.append("\n");
        methodBuffer.append("\t\tthrow new RuntimeException(\"Test not implemented\");\n");
        methodBuffer.append("\t}\n");
    }

    private StringBuffer createTestJHeadByClass(Class cls) {
        StringBuffer jHeadBuf = new StringBuffer();

        jHeadBuf.append("package " + cls.getPackage().getName() + "; \n");
        jHeadBuf.append("\n");
        jHeadBuf.append("import org.junit.Test;\n");
        jHeadBuf.append("import org.junit.runner.RunWith;\n");
        jHeadBuf.append("import org.springframework.test.context.ContextConfiguration;\n");
        jHeadBuf.append("import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;\n");
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
        this.getLog().info("ProjectName: " + name);
        jHeadBuf.append("@ContextConfiguration( locations = { \"classpath*:/META-INF/config/spring/spring-"
                + suffix + ".xml\"})\n");

        jHeadBuf.append("public class " + cls.getSimpleName() + "Test {\n");
        return jHeadBuf;
    }
}
