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
	
	<!--框架配置：该import设置请不要轻易改变-->
	<import resource="classpath*:/META-INF/config/spring/spring-db.xml"/>
	<import resource="classpath*:/META-INF/config/spring/spring-rpc.xml"/>
	<import resource="classpath*:/META-INF/config/spring/spring-mq.xml"/>
	
	<bean id="kjtService$Config" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
		
		<property name="order" value="2" />
		<property name="ignoreUnresolvablePlaceholders" value="true" />
		<property name="locations">
			<list>
				<value>classpath*:/META-INF/config/local/dubbo.properties</value>
				<!--  
				<value>file:/config/dubbo.properties</value>
				-->
			</list>
		</property>
	</bean>
	
	<!--dubbo服务发布配置-->
	
	<!-- 提供方应用信息，用于计算依赖关系 -->
	<!--   
	<dubbo:application name="kjt-#{artifactId}-service"/>
	--> 
    <!-- 使用multicast广播注册中心暴露服务地址 --> 
    <dubbo:registry address="${#{artifactId}.registry.address}"/>
    <!-- 使用监控中心 
    <dubbo:monitor protocol="registry"/>
    -->
    <!-- 用dubbo协议在20880端口暴露服务 -->
    <!--
    <dubbo:protocol name="dubbo" port="${#{artifactId}.protocol.dubbo.port}"/>
    -->
    
    <!--服务注册信息请在该备注以下添加-->
    
    <!-- 声明需要暴露的服务接口 -->
    <!--
    <dubbo:service interface="com.kjt.service.#{artifactId}.IXxxService" ref="xxxService"/>
    -->
    <!-- 和本地bean一样实现服务 -->
    <!--
    <bean id="xxxService" class="com.kjt.service.#{artifactId}.impl.XxxServiceImpl"/>
    -->
    
</beans>