package database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource


/*
 * @project RepCord-Bot
 * @author Patrity - https://github.com/Patrity
 * Created on - 11/6/2020
 */
class Database(val host: String, val user: String, val password: String) {

    var datasource: HikariDataSource? = null

    fun connect() {
        val config = HikariConfig()
        config.jdbcUrl = host
        config.username = user
        config.password = password
        config.addDataSourceProperty("cachePrepStmts", "true")
        config.addDataSourceProperty("prepStmtCacheSize", "250")
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        config.setDriverClassName("com.mysql.cj.jdbc.Driver")
        datasource = HikariDataSource(config)
    }

}