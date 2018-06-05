package dk.edutor.eduport

sealed class Category(val dtype: String) {
    companion object {
        const val TEXT = "TEXT"
        const val CHOICE = "CHOICE"
        const val EXECUTABLE = "EXECUTABLE"
        const val URL = "URL"
    }

    class Text() : Category(TEXT) {
        class Challenge(
                id: Int,
                port: Port,
                description: String,
                question: String,
                val answer: String
        ) : dk.edutor.eduport.Challenge(id, Text(), port, description, question) {
            override fun copy(id: Int) = Challenge(id, port, description, question, answer)
        }

        class Solution(
                id: Int,
                solver: User,
                override val challenge: Challenge,
                val answer: String
        ) : dk.edutor.eduport.Solution(id, Text(), solver) {
            override fun copy(id: Int) = Solution(id, solver, challenge, answer)
        }
    }

    class Choice() : Category(CHOICE) {
        class Challenge(
                id: Int,
                port: Port,
                description: String,
                question: String,
                val options: List<Option>
        ) : dk.edutor.eduport.Challenge(id, Choice(), port, description, question) {
            override fun copy(id: Int) = Challenge(id, port, description, question, options)
        }

        class Solution(
                id: Int,
                solver: User,
                override val challenge: Challenge,
                val answers: List<Int>
        ) : dk.edutor.eduport.Solution(id, Choice(), solver) {
            override fun copy(id: Int) = Solution(id, solver, challenge, answers)
        }
    }

    class Executable() : Category(EXECUTABLE) {
        class Challenge(
                id: Int,
                port: Port,
                description: String,
                question: String,
                val testCases: List<TestCase>
        ) : dk.edutor.eduport.Challenge(id, Executable(), port, description, question) {
            override fun copy(id: Int) = Challenge(id, port, description, question, testCases)
        }

        class Solution(
                id: Int,
                solver: User,
                override val challenge: Challenge,
                val bytes: ByteArray
        ) : dk.edutor.eduport.Solution(id, Executable(), solver) {
            override fun copy(id: Int) = Solution(id, solver, challenge, bytes)
        }
    }

    class Url() : Category(URL) {
        class Challenge(
                id: Int,
                port: Port,
                description: String,
                question: String,
                val name : String,
                val type : String,
                val fileName: String,
                val fileClass: ByteArray
        ) : dk.edutor.eduport.Challenge(id, Url(), port, description, question) {
            override fun copy(id: Int) = Challenge(id, port, description, question, name, type, fileName, fileClass)
        }

        class Solution(
                id: Int,
                solver: User,
                override val challenge: Challenge,
                val url: String
        ) : dk.edutor.eduport.Solution(id, Url(), solver) {
            override fun copy(id: Int) = Solution(id, solver, challenge, url)
        }
    }
}
