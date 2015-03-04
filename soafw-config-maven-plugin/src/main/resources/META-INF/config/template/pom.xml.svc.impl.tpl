<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<parent>
		<groupId>com.kjt.service.#{artifactId}</groupId>
		<artifactId>#{artifactId}</artifactId>
		<version>1.0-SNAPSHOT</version>
	</parent>

	<artifactId>#{artifactId}-service-impl</artifactId>
	<name>#{artifactId}-service-impl</name>
	<url>http://maven.apache.org</url>
	
	<dependencies>
		<dependency>
			<groupId>com.kjt.service.common</groupId>
			<artifactId>soafw-common-service-impl</artifactId>
			<version>${soafw-common.version}</version>
		</dependency>
		
		<dependency>
			<groupId>com.kjt.service.#{artifactId}</groupId>
			<artifactId>#{artifactId}-service</artifactId>
		</dependency>
		
		<dependency>
			<groupId>com.kjt.service.#{artifactId}</groupId>
			<artifactId>#{artifactId}-dao</artifactId>
		</dependency>
		
		<dependency>
			<groupId>com.kjt.service.#{artifactId}</groupId>
			<artifactId>#{artifactId}-rpc</artifactId>
		</dependency>
		
		<dependency>
			<groupId>com.kjt.service.#{artifactId}</groupId>
			<artifactId>#{artifactId}-mq</artifactId>
		</dependency>
		
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<scope>test</scope>
		</dependency>
		
	</dependencies>

</project>
