package database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection


/*
 * @project RepCord-Bot
 * @author Patrity - https://github.com/Patrity
 * Created on - 11/6/2020
 */
object Database {

    var datasource: HikariDataSource? = null

    fun connect() {
        val config = HikariConfig()
        config.jdbcUrl = Bot.config.sql_host
        config.username = Bot.config.sql_username
        config.password = Bot.config.sql_password
        config.addDataSourceProperty("cachePrepStmts", "true")
        config.addDataSourceProperty("prepStmtCacheSize", "250")
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        config.setDriverClassName("com.mysql.cj.jdbc.Driver")
        config.isAutoCommit = false
        datasource = HikariDataSource(config)

    }

    fun get(): Connection {
        return datasource!!.connection
    }


}