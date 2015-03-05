<?xml version="1.0" encoding="UTF-8" ?>
<web-app>



	<display-name>Service #{artifactId}</display-name>

	<context-param>
		<param-name>logbackConfigLocation</param-name>
		<param-value>WEB-INF/logback.xml</param-value>
	</context-param>
	<listener>
		<listener-class>ch.qos.logback.ext.spring.web.LogbackConfigListener
		</listener-class>
	</listener>

	<!-- 字符编码过滤器 用于解决post乱码问题 -->
	<filter>
		<filter-name>encodingFilter</filter-name>
		<filter-class>org.springframework.web.filter.CharacterEncodingFilter
		</filter-class>
		<init-param>
			<param-name>encoding</param-name>
			<param-value>UTF-8</param-value>
		</init-param>
		<init-param>
			<param-name>forceEncoding</param-name>
			<param-value>true</param-value>
		</init-param>
	</filter>

	<filter-mapping>
		<filter-name>encodingFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>





	<!-- spring context 上下文内容的 对象 service 层 dao层 以及其他spring管理的对象 -->

	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:/META-INF/config/spring/spring-web.xml
		</param-value>
	</context-param>
	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener
		</listener-class>
	</listener>


	<!--spring MVC 配置 -->
	<servlet>
		<servlet-name>springmvc</servlet-name>
		<servlet-class>com.kjt.service.common.web.servlet.ServiceDispatcherServlet
		</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>classpath:/META-INF/config/spring/spring-mvc.xml</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>springmvc</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>
	<session-config>
		<session-timeout>30</session-timeout>
	</session-config>

	<welcome-file-list>
		<welcome-file>index.html</welcome-file>
	</welcome-file-list>

</web-app>
