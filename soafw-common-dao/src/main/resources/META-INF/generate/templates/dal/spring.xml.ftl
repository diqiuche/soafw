<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">
	
	<bean id="${masterDataSource}" class="com.kjt.service.common.datasource.DynamicDataSource">
		<property name="prefix" value="${upperDataSource}" />
	</bean>

	<bean id="${slaveDataSource}" class="com.kjt.service.common.datasource.DynamicDataSource">
		<property name="prefix" value="${upperDataSource}_SLAVE" />
	</bean>

	<bean id="${mapQueryDataSource}" class="com.kjt.service.common.datasource.DynamicDataSource">
		<property name="prefix" value="${upperDataSource}_MAPQUERY" />
	</bean>
</beans>

