package dk.edutor.eduport.webchecker

import com.mysql.cj.jdbc.MysqlDataSource

class DBDataSource
{
    private val dataSource = MysqlDataSource()

    init {
        dataSource.setServerName("localhost")
        dataSource.setPort(3306)
        dataSource.setDatabaseName("edutor")
        dataSource.setUser("root")
        dataSource.setPassword("admin")
    }

    fun getDataSource(): MysqlDataSource {
        return dataSource
    }
}