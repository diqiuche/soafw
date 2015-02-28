### 设置

+ 

+ 定位到~/.m2/settings.xml

	```
	<pluginGroups>
        <pluginGroup>com.kjt.service.common</pluginGroup>
    </pluginGroups>
    ```
+ 运行 mvn soafw-config:config -Dxxx=yyy ...

	+ xxx列表
		+ groupId
		+ artifactId
		+ startPort
		+ stopPort
		+ destDir
		+ template
		
+ 例子
	
	mvn soafw-config:config -Dtemplate=application-config.xml -DartifactId=test -DdestDir='../../test/test-web/src/main/resource/META-INF/config/spring'