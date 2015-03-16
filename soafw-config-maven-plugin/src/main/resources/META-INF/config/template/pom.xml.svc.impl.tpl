<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<parent>
		<groupId>com.kjt.service.#{artifactId}</groupId>
		<artifactId>#{artifactId}</artifactId>
		<version>1.0-SNAPSHOT</version>
		<relativePath>../pom.xml</relativePath>
	</parent>

	<artifactId>#{artifactId}-service-impl</artifactId>
	<name>#{artifactId}-service-impl</name>
	<url>http://maven.apache.org</url>
	
	<dependencies>
		<dependency>
			<groupId>com.kjt.service.common</groupId>
			<artifactId>soafw-common-service-impl</artifactId>
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
		
		<!-- dubbo -->
		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>dubbo</artifactId>
			<exclusions>
				<exclusion>
					<groupId>org.springframework</groupId>
					<artifactId>spring</artifactId>
				</exclusion>
				<!-- 指定版本的netty -->
				<exclusion>
					<groupId>io.netty</groupId>
					<artifactId>netty</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
		
		<!-- zookeeper -->
		<dependency>
			<groupId>org.apache.zookeeper</groupId>
			<artifactId>zookeeper</artifactId>
			<exclusions>
				<exclusion>
					<groupId>javax.mail</groupId>
					<artifactId>mail</artifactId>
				</exclusion>
			</exclusions>
		</dependency>

		<!-- zkClient -->
		<dependency>
			<groupId>com.101tec</groupId>
			<artifactId>zkclient</artifactId>
		</dependency>
	</dependencies>
	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-jar-plugin</artifactId>
				<configuration>
					<archive>
						<manifest>
							<addClasspath>true</addClasspath>
							<mainClass>com.kjt.service.#{artifactId}.Startup</mainClass>
						</manifest>
					</archive>
				</configuration>
			</plugin>
			<plugin>
				<artifactId>maven-assembly-plugin</artifactId>
				<configuration>
					<descriptors>
						<descriptor>src/main/assemble/dist.xml</descriptor>
					</descriptors>
				</configuration>
				<executions>
					<execution>
						<id>make-assembly</id>
						<phase>dist</phase>
						<goals>
							<goal>directory</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
</project>
