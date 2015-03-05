<?xml version="1.0" encoding="UTF-8"?>
<beans
	xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:aop="http://www.springframework.org/schema/aop" 
	xmlns:tx="http://www.springframework.org/schema/tx" 
	xmlns:dubbo="http://code.alibabatech.com/schema/dubbo" 
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	 http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
	 http://www.springframework.org/schema/aop
	 http://www.springframework.org/schema/aop/spring-aop-3.0.xsd
     http://www.springframework.org/schema/tx 
     http://www.springframework.org/schema/tx/spring-tx-3.0.xsd
     http://www.springframework.org/schema/context 
     http://www.springframework.org/schema/context/spring-context-3.0.xsd
 	 http://code.alibabatech.com/schema/dubbo  
 	 http://code.alibabatech.com/schema/dubbo/dubbo.xsd">
	
	<context:component-scan base-package="com.kjt.service.#{artifactId}.*" />
	
	<import resource="classpath*:/META-INF/config/local/spring-dao.xml"/>
	<import resource="classpath*:/META-INF/config/local/spring-rpc.xml"/>
	<import resource="classpath*:/META-INF/config/local/spring-mq.xml"/>
	
	<!--dubbo服务发布配置-->
	
	<!-- 提供方应用信息，用于计算依赖关系 -->
	<!--   
	<dubbo:application name="kjt-#{artifactId}-service"/>
	--> 
    <!-- 使用multicast广播注册中心暴露服务地址 --> 
    <dubbo:registry address="${registry.address}"/>
    <!-- 使用监控中心 
    <dubbo:monitor protocol="registry"/>
    -->
    <!-- 用dubbo协议在20880端口暴露服务 -->
    <!--
    <dubbo:protocol name="dubbo" port="${protocol.port}"/>
    -->
    <!-- 声明需要暴露的服务接口 -->
    <!--
    <dubbo:service interface="com.kjt.service.#{artifactId}.IXxxService" ref="xxxService"/>
    -->
    <!-- 和本地bean一样实现服务 -->
    <!--
    <bean id="xxxService" class="com.kjt.service.#{artifactId}.impl.XxxServiceImpl"/>
    -->
    
</beans>