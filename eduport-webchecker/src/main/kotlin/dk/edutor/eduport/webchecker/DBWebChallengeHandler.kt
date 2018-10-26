package dk.edutor.eduport.webchecker

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class DBWebChallengeHandler {
    val dbconnector = DBConnector()

    init {
        dbconnector.setDataSource(DBDataSource().getDataSource())
    }

    fun saveWebChallenge(name: String, type: String, file: File) {
        dbconnector.open()
        val preparedStatement = dbconnector.preparedStatement("insert into webchallenge values(null, ?, ?, ?, ?)")
        val fileStream = FileInputStream(file)
        preparedStatement.setString(1, name)
        preparedStatement.setString(2, type)
        preparedStatement.setString(3, file.name)
        preparedStatement.setBinaryStream(4, fileStream, file.length())
        preparedStatement.executeUpdate()
        dbconnector.close()
    }

    fun getWebChallenge(WebChallengeId: Int): DBWebChallenge {
        dbconnector.open()

        val dbwebchallenge: DBWebChallenge = DBWebChallenge()

        val preparedStatement = dbconnector.preparedStatement("select * from webchallenge where id = $WebChallengeId")
        val resultSet = preparedStatement.executeQuery()

        if (resultSet.next()) {
            dbwebchallenge.webChallengeId = resultSet.getInt("id")
            dbwebchallenge.webChallengeName = resultSet.getString("name")
            dbwebchallenge.webChallengeType = resultSet.getString("type")
            dbwebchallenge.webChallengeFileName = resultSet.getString("filename")

            val inStream = resultSet.getBinaryStream("file")
            val file = File(dbwebchallenge.webChallengeFileName)
            val outStream = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var bytesRead: Int = 0
            while ({ bytesRead = inStream.read(buffer); bytesRead }() != -1) { outStream.write(buffer, 0, bytesRead) }
            inStream.close()
            outStream.close()
            dbwebchallenge.webChallengeFile = file
        }

        dbconnector.close()

        return dbwebchallenge
    }
}