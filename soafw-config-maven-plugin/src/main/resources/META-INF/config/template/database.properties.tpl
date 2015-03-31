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

##{artifactId}_db
#{artifactId}_db.driver = ${jdbc.driver}
#{artifactId}_db.url = jdbc:mysql://192.168.1.110:3306/#{artifactId}_db?${db.conn.str}
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

##{artifactId}_db_slave
#{artifactId}_db_slave.driver = ${jdbc.driver}
#{artifactId}_db_slave.url = jdbc:mysql://192.168.1.110:3306/#{artifactId}_db?${db.conn.str}
#{artifactId}_db_slave.username = root
#{artifactId}_db_slave.password = Kjt@)!$
#{artifactId}_db_slave.initialSize = ${dbcp.initialSize}
#{artifactId}_db_slave.maxActive = ${dbcp.maxActive}
#{artifactId}_db_slave.maxIdle = ${dbcp.maxIdle}
#{artifactId}_db_slave.minIdle = ${dbcp.minIdle}
#{artifactId}_db_slave.maxWait = ${dbcp.maxWait}
#{artifactId}_db_slave.removeAbandonedTimeout = ${dbcp.removeAbandonedTimeout}
#{artifactId}_db_slave.minEvictableIdleTimeMillis = ${dbcp.minEvictableIdleTimeMillis}
#{artifactId}_db_slave.timeBetweenEvictionRunsMillis = ${dbcp.timeBetweenEvictionRunsMillis}
#{artifactId}_db_slave.poolPreparedStatements = ${dbcp.poolPreparedStatements}
#{artifactId}_db_slave.defaultReadOnly = ${dbcp.defaultReadOnly}
#{artifactId}_db_slave.logAbandoned = ${dbcp.logAbandoned}
#{artifactId}_db_slave.removeAbandoned = ${dbcp.removeAbandoned}
#{artifactId}_db_slave.testOnBorrow = ${dbcp.testOnBorrow}
#{artifactId}_db_slave.testWhileIdle = ${dbcp.testWhileIdle}
#{artifactId}_db_slave.validationQuery = ${dbcp.validationQuery}

##{artifactId}_db_map_query
#{artifactId}_db_map_query.driver = ${jdbc.driver}
#{artifactId}_db_map_query.url = jdbc:mysql://192.168.1.110:3306/#{artifactId}_db?${db.conn.str}
#{artifactId}_db_map_query.username = root
#{artifactId}_db_map_query.password = Kjt@)!$
#{artifactId}_db_map_query.initialSize = ${dbcp.initialSize}
#{artifactId}_db_map_query.maxActive = ${dbcp.maxActive}
#{artifactId}_db_map_query.maxIdle = ${dbcp.maxIdle}
#{artifactId}_db_map_query.minIdle = ${dbcp.minIdle}
#{artifactId}_db_map_query.maxWait = ${dbcp.maxWait}
#{artifactId}_db_map_query.removeAbandonedTimeout = ${dbcp.removeAbandonedTimeout}
#{artifactId}_db_map_query.minEvictableIdleTimeMillis = ${dbcp.minEvictableIdleTimeMillis}
#{artifactId}_db_map_query.timeBetweenEvictionRunsMillis = ${dbcp.timeBetweenEvictionRunsMillis}
#{artifactId}_db_map_query.poolPreparedStatements = ${dbcp.poolPreparedStatements}
#{artifactId}_db_map_query.defaultReadOnly = ${dbcp.defaultReadOnly}
#{artifactId}_db_map_query.logAbandoned = ${dbcp.logAbandoned}
#{artifactId}_db_map_query.removeAbandoned = ${dbcp.removeAbandoned}
#{artifactId}_db_map_query.testOnBorrow = ${dbcp.testOnBorrow}
#{artifactId}_db_map_query.testWhileIdle = ${dbcp.testWhileIdle}
#{artifactId}_db_map_query.validationQuery = ${dbcp.validationQuery}