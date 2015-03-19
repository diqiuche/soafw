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

#cache
cache_db.driver = ${jdbc.driver}
cache_db.url = jdbc:mysql://192.168.1.110:3306/soafw_db?${db.conn.str}
cache_db.username = root
cache_db.password = Kjt@)!$
cache_db.initialSize = ${dbcp.initialSize}
cache_db.maxActive = ${dbcp.maxActive}
cache_db.maxIdle = 5
cache_db.minIdle = ${dbcp.minIdle}
cache_db.maxWait = ${dbcp.maxWait}
cache_db.removeAbandonedTimeout = ${dbcp.removeAbandonedTimeout}
cache_db.minEvictableIdleTimeMillis= 1000
cache_db.timeBetweenEvictionRunsMillis = 500
cache_db.poolPreparedStatements = ${dbcp.poolPreparedStatements}
cache_db.defaultReadOnly = ${dbcp.defaultReadOnly}
cache_db.logAbandoned = ${dbcp.logAbandoned}
cache_db.removeAbandoned = ${dbcp.removeAbandoned}
cache_db.testOnBorrow = ${dbcp.testOnBorrow}
cache_db.testWhileIdle = ${dbcp.testWhileIdle}
cache_db.validationQuery = ${dbcp.validationQuery}

#cache_db_slave
cache_db_slave.driver = ${jdbc.driver}
cache_db_slave.url = jdbc:mysql://192.168.1.110:3306/soafw_db?${db.conn.str}
cache_db_slave.username = root
cache_db_slave.password = Kjt@)!$
cache_db_slave.initialSize = ${dbcp.initialSize}
cache_db_slave.maxActive = ${dbcp.maxActive}
cache_db_slave.maxIdle = ${dbcp.maxIdle}
cache_db_slave.minIdle = ${dbcp.minIdle}
cache_db_slave.maxWait = ${dbcp.maxWait}
cache_db_slave.removeAbandonedTimeout = ${dbcp.removeAbandonedTimeout}
cache_db_slave.minEvictableIdleTimeMillis = ${dbcp.minEvictableIdleTimeMillis}
cache_db_slave.timeBetweenEvictionRunsMillis = ${dbcp.timeBetweenEvictionRunsMillis}
cache_db_slave.poolPreparedStatements = ${dbcp.poolPreparedStatements}
cache_db_slave.defaultReadOnly = ${dbcp.defaultReadOnly}
cache_db_slave.logAbandoned = ${dbcp.logAbandoned}
cache_db_slave.removeAbandoned = ${dbcp.removeAbandoned}
cache_db_slave.testOnBorrow = ${dbcp.testOnBorrow}
cache_db_slave.testWhileIdle = ${dbcp.testWhileIdle}
cache_db_slave.validationQuery = ${dbcp.validationQuery}

#cache_db_map_query
cache_db_map_query.driver = ${jdbc.driver}
cache_db_map_query.url = jdbc:mysql://192.168.1.110:3306/soafw_db?${db.conn.str}
cache_db_map_query.username = root
cache_db_map_query.password = Kjt@)!$
cache_db_map_query.initialSize = ${dbcp.initialSize}
cache_db_map_query.maxActive = ${dbcp.maxActive}
cache_db_map_query.maxIdle = ${dbcp.maxIdle}
cache_db_map_query.minIdle = ${dbcp.minIdle}
cache_db_map_query.maxWait = ${dbcp.maxWait}
cache_db_map_query.removeAbandonedTimeout = ${dbcp.removeAbandonedTimeout}
cache_db_map_query.minEvictableIdleTimeMillis = ${dbcp.minEvictableIdleTimeMillis}
cache_db_map_query.timeBetweenEvictionRunsMillis = ${dbcp.timeBetweenEvictionRunsMillis}
cache_db_map_query.poolPreparedStatements = ${dbcp.poolPreparedStatements}
cache_db_map_query.defaultReadOnly = ${dbcp.defaultReadOnly}
cache_db_map_query.logAbandoned = ${dbcp.logAbandoned}
cache_db_map_query.removeAbandoned = ${dbcp.removeAbandoned}
cache_db_map_query.testOnBorrow = ${dbcp.testOnBorrow}
cache_db_map_query.testWhileIdle = ${dbcp.testWhileIdle}
cache_db_map_query.validationQuery = ${dbcp.validationQuery}

##{artifactId}
#{artifactId}.driver = ${jdbc.driver}
#{artifactId}.url = jdbc:mysql://192.168.1.110:3306/#{artifactId}_db?${db.conn.str}
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