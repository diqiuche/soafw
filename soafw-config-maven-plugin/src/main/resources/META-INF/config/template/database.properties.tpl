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

##{artifactId}
#{artifactId}.driver = ${jdbc.driver}
#{artifactId}.url = jdbc:mysql://192.168.1.110:3306/oper_db?${db.conn.str}
#{artifactId}.username = root
#{artifactId}.password = Kjt@)!$
#{artifactId}.initialSize = ${dbcp.initialSize}
#{artifactId}.maxActive = ${dbcp.maxActive}
#{artifactId}.maxIdle = 5
#{artifactId}.minIdle = ${dbcp.minIdle}
#{artifactId}.maxWait = ${dbcp.maxWait}
#{artifactId}.removeAbandonedTimeout = ${dbcp.removeAbandonedTimeout}
#{artifactId}.minEvictableIdleTimeMillis= 1000
#{artifactId}.timeBetweenEvictionRunsMillis = 500
#{artifactId}.poolPreparedStatements = ${dbcp.poolPreparedStatements}
#{artifactId}.defaultReadOnly = ${dbcp.defaultReadOnly}
#{artifactId}.logAbandoned = ${dbcp.logAbandoned}
#{artifactId}.removeAbandoned = ${dbcp.removeAbandoned}
#{artifactId}.testOnBorrow = ${dbcp.testOnBorrow}
#{artifactId}.testWhileIdle = ${dbcp.testWhileIdle}
#{artifactId}.validationQuery = ${dbcp.validationQuery}

##{artifactId}_SLAVE
#{artifactId}_SLAVE.driver = ${jdbc.driver}
#{artifactId}_SLAVE.url = jdbc:mysql://192.168.1.110:3306/#{artifactId}?${db.conn.str}
#{artifactId}_SLAVE.username = root
#{artifactId}_SLAVE.password = Kjt@)!$
#{artifactId}_SLAVE.initialSize = ${dbcp.initialSize}
#{artifactId}_SLAVE.maxActive = ${dbcp.maxActive}
#{artifactId}_SLAVE.maxIdle = ${dbcp.maxIdle}
#{artifactId}_SLAVE.minIdle = ${dbcp.minIdle}
#{artifactId}_SLAVE.maxWait = ${dbcp.maxWait}
#{artifactId}_SLAVE.removeAbandonedTimeout = ${dbcp.removeAbandonedTimeout}
#{artifactId}_SLAVE.minEvictableIdleTimeMillis = ${dbcp.minEvictableIdleTimeMillis}
#{artifactId}_SLAVE.timeBetweenEvictionRunsMillis = ${dbcp.timeBetweenEvictionRunsMillis}
#{artifactId}_SLAVE.poolPreparedStatements = ${dbcp.poolPreparedStatements}
#{artifactId}_SLAVE.defaultReadOnly = ${dbcp.defaultReadOnly}
#{artifactId}_SLAVE.logAbandoned = ${dbcp.logAbandoned}
#{artifactId}_SLAVE.removeAbandoned = ${dbcp.removeAbandoned}
#{artifactId}_SLAVE.testOnBorrow = ${dbcp.testOnBorrow}
#{artifactId}_SLAVE.testWhileIdle = ${dbcp.testWhileIdle}
#{artifactId}_SLAVE.validationQuery = ${dbcp.validationQuery}

##{artifactId}_MAPQUERY
#{artifactId}_MAPQUERY.driver = ${jdbc.driver}
#{artifactId}_MAPQUERY.url = jdbc:mysql://192.168.1.110:3306/#{artifactId}?${db.conn.str}
#{artifactId}_MAPQUERY.username = root
#{artifactId}_MAPQUERY.password = Kjt@)!$
#{artifactId}_MAPQUERY.initialSize = ${dbcp.initialSize}
#{artifactId}_MAPQUERY.maxActive = ${dbcp.maxActive}
#{artifactId}_MAPQUERY.maxIdle = ${dbcp.maxIdle}
#{artifactId}_MAPQUERY.minIdle = ${dbcp.minIdle}
#{artifactId}_MAPQUERY.maxWait = ${dbcp.maxWait}
#{artifactId}_MAPQUERY.removeAbandonedTimeout = ${dbcp.removeAbandonedTimeout}
#{artifactId}_MAPQUERY.minEvictableIdleTimeMillis = ${dbcp.minEvictableIdleTimeMillis}
#{artifactId}_MAPQUERY.timeBetweenEvictionRunsMillis = ${dbcp.timeBetweenEvictionRunsMillis}
#{artifactId}_MAPQUERY.poolPreparedStatements = ${dbcp.poolPreparedStatements}
#{artifactId}_MAPQUERY.defaultReadOnly = ${dbcp.defaultReadOnly}
#{artifactId}_MAPQUERY.logAbandoned = ${dbcp.logAbandoned}
#{artifactId}_MAPQUERY.removeAbandoned = ${dbcp.removeAbandoned}
#{artifactId}_MAPQUERY.testOnBorrow = ${dbcp.testOnBorrow}
#{artifactId}_MAPQUERY.testWhileIdle = ${dbcp.testWhileIdle}
#{artifactId}_MAPQUERY.validationQuery = ${dbcp.validationQuery}