<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns="http://www.springframework.org/schema/beans" xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:context="http://www.springframework.org/schema/context" xmlns:tx="http://www.springframework.org/schema/tx"
	xmlns:mvc="http://www.springframework.org/schema/mvc" xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
	xmlns:cache="http://www.springframework.org/schema/cache"
	xsi:schemaLocation="http://www.springframework.org/schema/beans 
    http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
    http://www.springframework.org/schema/aop
    http://www.springframework.org/schema/aop/spring-aop-4.0.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context-4.0.xsd
    http://code.alibabatech.com/schema/dubbo
    http://code.alibabatech.com/schema/dubbo/dubbo.xsd"
	default-autowire="byName">
	
	<!--框架配置：该设置请不要轻易改变-->
	<import resource="classpath*:/META-INF/config/spring/spring-rpc.xml"/>
	<import resource="classpath*:/META-INF/config/spring/spring-mq.xml"/>
	
	<bean id="dubbo" class="com.kjt.service.common.config.ConfigurationFactoryBean">
		<property name="name" value="dubbo" />
        <property name="encoding" value="utf8" />
	</bean>
	
	<bean id="job" class="com.kjt.service.common.config.ConfigurationFactoryBean">
		<property name="name" value="job" />
        <property name="encoding" value="utf8" />
        <property name="type" value="xml" />
	</bean>
	
	<bean id="dubboPlaceholder"
		class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
		<property name="properties">
			<bean id="propertiesConfigurationFactoryBean"
				class="com.kjt.service.common.config.CommonsConfigurationFactoryBean">
				<property name="configurations">  
			       	<list>  
			       		<ref bean="dubbo" />
			       		<ref bean="job" />
			        </list>  
			    </property>				
			</bean>
		</property>
	</bean>
	
	<!-- 消费方应用名，用于计算依赖关系，不是匹配条件，不要与提供方一样 -->
	<dubbo:application name="kjt-#{artifactId}-job#{moduleSuffix}"/>
	 
    <!-- 使用zookeeper发现服务地址 -->
    <!-- 多注册中心配置，竖号分隔表示同时连接多个不同注册中心，同一注册中心的多个集群地址用逗号分隔 -->
    <dubbo:registry protocol="zookeeper" address="${#{artifactId}.service.registry.address}"/>
    
    <!-- 服务消费者定义 -->
    <dubbo:consumer timeout="${#{artifactId}.job#{moduleSuffix}.timeout}" init="true" check="false"/>
    
    <!-- 使用监控中心 -->
    <dubbo:monitor protocol="registry"/>
    
    <!--框架配置：该设置请不要轻易改变-->
    <context:component-scan base-package="com.kjt.service.#{artifactId}" />
    
    <!--扩展区域-->
    
    <!--job 定义-->
    <import resource="classpath*:/META-INF/config/spring/job-task.xml"/>
    <!--服务查询定义信息请在该备注以下添加-->
    <!--
	<dubbo:reference id="xxxService" interface="com.kjt.service.#{artifactId}.IXxxxService" />
	-->
</beans>