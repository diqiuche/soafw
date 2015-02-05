### job包

### 所有job采用spring quartz 框架
### 所有job必须继承AbsJob
### 所有job都必须有一个job name
### 所有job自己的个性化配置保持在以其名称的配置文件中，同时配置支持动态加载
### 