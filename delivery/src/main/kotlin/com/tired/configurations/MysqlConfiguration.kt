package com.tired.configurations
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotliquery.HikariCP
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MysqlConfiguration {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MysqlConfiguration::class.java)
    }

    @Value("\${mysql.jdbc-url}")
    lateinit var jdbcUrl: String

    @Value("\${mysql.max-pool-size:100}")
    var maxPoolSize: Int = 100

    @Value("\${mysql.max-pool-idle:100}")
    var idlePoolSize: Int = 100

    @Value("\${mysql.idle-timeout:60000}")
    var idleTimeout: Long = 60000

    @Value("\${mysql.connection-timeout:60000}")
    var connectionTimeout: Long = 60000

    @Bean
    @Autowired
    fun defaultConnection(): HikariDataSource {
        val jdbcConfig = HikariConfig()
        jdbcConfig.maximumPoolSize = maxPoolSize
        jdbcConfig.minimumIdle = idlePoolSize
        jdbcConfig.idleTimeout = idleTimeout
        jdbcConfig.jdbcUrl = jdbcUrl
        jdbcConfig.username = "localEnv"
        jdbcConfig.password = "test"
        jdbcConfig.connectionTimeout = connectionTimeout
        jdbcConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048)
        jdbcConfig.addDataSourceProperty("prepStmtCacheSize", 256)
        jdbcConfig.addDataSourceProperty("cachePrepStmts", true)
        jdbcConfig.addDataSourceProperty("useServerPrepStmts", true)
        LOGGER.debug("Creating datasource for collaboration api")

        return HikariDataSource(jdbcConfig)
    }

}
