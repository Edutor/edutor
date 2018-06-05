package dk.edutor.eduport.webchecker

import java.sql.*
import javax.sql.DataSource

class DBConnector
{
    private var dataSource: DataSource? = null
    private var connection: Connection? = null

    fun setDataSource(dataSource: DataSource) {
        this.dataSource = dataSource
    }

    @Throws(SQLException::class)
    fun open() {
        if (connection == null || connection!!.isClosed()) {
            connection = dataSource!!.getConnection()
        }
    }

    @Throws(SQLException::class)
    fun close() {
        if (connection != null && !connection!!.isClosed()) {
            connection!!.close()
            connection = null
        }
    }

    @Throws(SQLException::class)
    fun preparedStatement(sql: String): PreparedStatement {
        return connection!!.prepareStatement(sql)
    }
}