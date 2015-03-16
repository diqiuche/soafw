### 部署规范


+ 部署用户

	+ deployer
	
+ 部署应用程序目录结构

	+ ~/apps
	
			|
			start.sh service version
		 	order
		 	merchant
		 	#{artifactId}
		 	  |
		 	  current
		 	  v1
		 	  v2
		 	  v3
		 	  ...
		 	  vn
		 	  	|
		 	  	pom.xml
		 	  	#{artifactId}-cache
		 	  	#{artifactId}-common
		 	  	#{artifactId}-config
		 	  	#{artifactId}-dao
		 	  	#{artifactId}-domain
		 	  	#{artifactId}-job
		 	  		|
		 	  		start.sh
		 	  	#{artifactId}-mq
		 	  	#{artifactId}-rpc
		 	  	#{artifactId}-service
		 	  	#{artifactId}-service-impl
		 	  		|
		 	  		start.sh
		 	  	#{artifactId}-web
		 	  		|
		 	  		start.sh
		 	  		pom.xml
		 	  		target
		 	  			|
		 	  			#{artifactId}-web.war
		 	  			或者
		 	  			#{artifactId}-web
	+ 日志目录结构
		+ /data1/logs/service/#{artifactId}/
		
								|
								dlog.log
								elog.log
								request.log
	＋ 全局配置文件目录
		+ /config