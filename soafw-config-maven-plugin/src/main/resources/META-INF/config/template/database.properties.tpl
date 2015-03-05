javaService=http://192.168.201.190:8080/
#
iDatabase=true
jdbc.driver = com.mysql.jdbc.Driver
sql.jdbc.driver=com.microsoft.sqlserver.jdbc.SQLServerDriver
#
dbcp.initialSize = 0
dbcp.maxActive = 40
dbcp.maxIdle = 10
dbcp.minIdle = 0
dbcp.maxWait = 3000
dbcp.testOnBorrow=false
dbcp.testWhileIdle=true
dbcp.validationQuery = select now()
dbcp.removeAbandonedTimeout = 300
dbcp.minEvictableIdleTimeMillis = 2000
dbcp.timeBetweenEvictionRunsMillis = 1000
dbcp.poolPreparedStatements=true
dbcp.defaultReadOnly=false
dbcp.logAbandoned=true
dbcp.removeAbandoned=true
db.conn.str=useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&elideSetAutoCommits=true&cacheServerConfiguration=true

##{artifactId}_db
#{artifactId}_db.driver = ${jdbc.driver}
#{artifactId}_db.url = jdbc:mysql://192.168.1.110:3306/oper_db?${db.conn.str}
#{artifactId}_db.username = root
#{artifactId}_db.password = Kjt@)!$
#{artifactId}_db.initialSize = ${dbcp.initialSize}
#{artifactId}_db.maxActive = ${dbcp.maxActive}
#{artifactId}_db.maxIdle = 5
#{artifactId}_db.minIdle = ${dbcp.minIdle}
#{artifactId}_db.maxWait = ${dbcp.maxWait}
#{artifactId}_db.removeAbandonedTimeout = ${dbcp.removeAbandonedTimeout}
#{artifactId}_db.minEvictableIdleTimeMillis= 1000
#{artifactId}_db.timeBetweenEvictionRunsMillis = 500
#{artifactId}_db.poolPreparedStatements = ${dbcp.poolPreparedStatements}
#{artifactId}_db.defaultReadOnly = ${dbcp.defaultReadOnly}
#{artifactId}_db.logAbandoned = ${dbcp.logAbandoned}
#{artifactId}_db.removeAbandoned = ${dbcp.removeAbandoned}
#{artifactId}_db.testOnBorrow = ${dbcp.testOnBorrow}
#{artifactId}_db.testWhileIdle = ${dbcp.testWhileIdle}
#{artifactId}_db.validationQuery = ${dbcp.validationQuery}

##{artifactId}_slave
#{artifactId}_slave.driver = ${jdbc.driver}
#{artifactId}_slave.url = jdbc:mysql://192.168.1.110:3306/#{artifactId}_db?${db.conn.str}
#{artifactId}_slave.username = root
#{artifactId}_slave.password = Kjt@)!$
#{artifactId}_slave.initialSize = ${dbcp.initialSize}
#{artifactId}_slave.maxActive = ${dbcp.maxActive}
#{artifactId}_slave.maxIdle = ${dbcp.maxIdle}
#{artifactId}_slave.minIdle = ${dbcp.minIdle}
#{artifactId}_slave.maxWait = ${dbcp.maxWait}
#{artifactId}_slave.removeAbandonedTimeout = ${dbcp.removeAbandonedTimeout}
#{artifactId}_slave.minEvictableIdleTimeMillis = ${dbcp.minEvictableIdleTimeMillis}
#{artifactId}_slave.timeBetweenEvictionRunsMillis = ${dbcp.timeBetweenEvictionRunsMillis}
#{artifactId}_slave.poolPreparedStatements = ${dbcp.poolPreparedStatements}
#{artifactId}_slave.defaultReadOnly = ${dbcp.defaultReadOnly}
#{artifactId}_slave.logAbandoned = ${dbcp.logAbandoned}
#{artifactId}_slave.removeAbandoned = ${dbcp.removeAbandoned}
#{artifactId}_slave.testOnBorrow = ${dbcp.testOnBorrow}
#{artifactId}_slave.testWhileIdle = ${dbcp.testWhileIdle}
#{artifactId}_slave.validationQuery = ${dbcp.validationQuery}

##{artifactId}_map_query
#{artifactId}_map_query.driver = ${jdbc.driver}
#{artifactId}_map_query.url = jdbc:mysql://192.168.1.110:3306/#{artifactId}_db?${db.conn.str}
#{artifactId}_map_query.username = root
#{artifactId}_map_query.password = Kjt@)!$
#{artifactId}_map_query.initialSize = ${dbcp.initialSize}
#{artifactId}_map_query.maxActive = ${dbcp.maxActive}
#{artifactId}_map_query.maxIdle = ${dbcp.maxIdle}
#{artifactId}_map_query.minIdle = ${dbcp.minIdle}
#{artifactId}_map_query.maxWait = ${dbcp.maxWait}
#{artifactId}_map_query.removeAbandonedTimeout = ${dbcp.removeAbandonedTimeout}
#{artifactId}_map_query.minEvictableIdleTimeMillis = ${dbcp.minEvictableIdleTimeMillis}
#{artifactId}_map_query.timeBetweenEvictionRunsMillis = ${dbcp.timeBetweenEvictionRunsMillis}
#{artifactId}_map_query.poolPreparedStatements = ${dbcp.poolPreparedStatements}
#{artifactId}_map_query.defaultReadOnly = ${dbcp.defaultReadOnly}
#{artifactId}_map_query.logAbandoned = ${dbcp.logAbandoned}
#{artifactId}_map_query.removeAbandoned = ${dbcp.removeAbandoned}
#{artifactId}_map_query.testOnBorrow = ${dbcp.testOnBorrow}
#{artifactId}_map_query.testWhileIdle = ${dbcp.testWhileIdle}
#{artifactId}_map_query.validationQuery = ${dbcp.validationQuery}