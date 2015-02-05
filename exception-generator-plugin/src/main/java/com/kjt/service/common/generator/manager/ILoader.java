package com.kjt.service.common.generator.manager;

import com.kjt.service.common.generator.ExceptionMessage;

import org.apache.maven.plugin.MojoExecutionException;

import java.util.List;

/**
 * 配置文件加载
 */
public interface ILoader {
    /**
     * 获取要生成的内容
     *
     * @return 生成内容列表
     * @throws MojoExecutionException
     */
    List<ExceptionMessage> getExceptionMessages() throws MojoExecutionException;
}
