<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<parent>
		<groupId>com.kjt.service.#{artifactId}</groupId>
		<artifactId>#{artifactId}</artifactId>
		<version>1.0-SNAPSHOT</version>
	</parent>

	<artifactId>#{artifactId}-web</artifactId>
	<packaging>war</packaging>
	<name>#{artifactId}-web</name>
	<url>http://maven.apache.org</url>
	<properties>
		<jetty.port>#{startPort}</jetty.port>
		<!-- 异常生成定义 -->
		<exception.level>CONTROLLER</exception.level>
		<!--AUTO ?groupId + . + service+ . + CommunityServiceExceptionMessage -->
		<exception.enum.class>AUTO</exception.enum.class>
		<!-- 异常生成定义 END -->
		
		<spring.version>4.0.5.RELEASE</spring.version>
		<zookeeper.version>3.4.6</zookeeper.version>
		<zkclient.version>0.4</zkclient.version>
		<junit.version>4.11</junit.version>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

	</properties>
	<dependencies>
            <dependency>
				<groupId>com.kjt.service.common</groupId>
				<artifactId>soafw-common-web</artifactId>
			</dependency>
			
			<dependency>
				<groupId>net.sf.json-lib</groupId>
				<artifactId>json-lib</artifactId>
				<version>2.2.3</version>
				<classifier>jdk15</classifier>
			</dependency>
	
			<!-- spring相关jar包 -->
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-context</artifactId>
				<version>${spring.version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-context-support</artifactId>
				<version>${spring.version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-jdbc</artifactId>
				<version>${spring.version}</version>
			</dependency>
	
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-tx</artifactId>
				<version>${spring.version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-web</artifactId>
				<version>${spring.version}</version>
			</dependency>
	
			<dependency>
				<groupId>javax.servlet</groupId>
				<artifactId>javax.servlet-api</artifactId>
				<version>3.1.0</version>
				<scope>compile</scope>
			</dependency>
	
			<dependency>
				<groupId>javax.servlet.jsp</groupId>
				<artifactId>jsp-api</artifactId>
				<version>2.2</version>
				<scope>compile</scope>
			</dependency>
	
			<dependency>
				<groupId>javax.servlet.jsp.jstl</groupId>
				<artifactId>javax.servlet.jsp.jstl-api</artifactId>
				<version>1.2.1</version>
				<scope>compile</scope>
			</dependency>

	</dependencies>

	<build>
		<finalName>#{artifactId}-web</finalName>
		<plugins>

			<plugin>
				<groupId>org.mortbay.jetty</groupId>
				<artifactId>jetty-maven-plugin</artifactId>
				<version>8.1.6.v20120903</version>

				<configuration>
					<connectors>
						<connector implementation="org.eclipse.jetty.server.nio.SelectChannelConnector">
							<port>${jetty.port}</port>
						</connector>
					</connectors>
					<scanIntervalSeconds>10</scanIntervalSeconds>
					<stopKey>foo</stopKey>
					<stopPort>#{stopPort}</stopPort>
					<webAppConfig>
						<contextPath>/soj-web</contextPath>
					</webAppConfig>
				</configuration>
				<executions>
					<execution>
						<id>start-jetty</id>
						<phase>pre-integration-test</phase>
						<goals>
							<goal>run</goal>
						</goals>
						<configuration>
							<scanIntervalSeconds>0</scanIntervalSeconds>
							<daemon>true</daemon>
						</configuration>
					</execution>
					<execution>
						<id>stop-jetty</id>
						<phase>post-integration-test</phase>
						<goals>
							<goal>stop</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.1</version>
				<configuration>
					<source>1.7</source>
					<target>1.7</target>
				</configuration>
			</plugin>
		</plugins>
	</build>
</project>
