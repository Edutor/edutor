package dk.edutor.eduport.webchecker

import dk.edutor.eduport.*
import dk.edutor.eduport.Category.*
import java.io.File
import java.io.FileOutputStream



const val KEY = "WEB_CHECKER"

class WebChecker : Port {

    override val key = KEY
    override val name = "Web Checker"

    val dbwebchallengehandler = DBWebChallengeHandler()

    private fun compileJavaFileToClassFile(file: File) : File {
        println("Compiling JAVA file to CLASS file...")

        val p = Runtime.getRuntime().exec("javac -classpath " + file.absolutePath)

        p.waitFor()

        var compiledFilePath = file.absolutePath
        compiledFilePath = compiledFilePath.replace(".java", ".class")

        println("Compiled JAVA file to CLASS file")

        return File(compiledFilePath)
    }

    //FIX KOTLIN KT FILE TO CLASS COMPILATION!!!
    private fun compileKotlinFileToClassFile(file: File) : File {
        val p = Runtime.getRuntime().exec("kotlin " + file.absolutePath)

        p.waitFor()

        val compiledFilePath = file.absolutePath.replace(".java", ".class")

        return File(compiledFilePath)
    }

    fun upload(name : String, type : String, uploadFile : File)
    {
        var file = uploadFile

        /*
        if (file.name.endsWith(".java")) {
            file = compileJavaFileToClassFile(uploadFile)
        }

        if (file.name.endsWith(".kt")) {
            file = compileKotlinFileToClassFile(uploadFile)
        }
        */

        if (file.name.endsWith("class")) {
            dbwebchallengehandler.saveWebChallenge(name, type, file)

            println("Uploaded CLASS file...")
        }
    }

    override fun evaluate(solution: Solution): Solution {
        if (!(solution is Url.Solution)) throw IllegalArgumentException("Solution should be Url")

        val dbwebchallenge = DBWebChallenge()
        dbwebchallenge.webChallengeId = solution.challenge.id
        dbwebchallenge.webChallengeName = solution.challenge.name
        dbwebchallenge.webChallengeType = solution.challenge.type
        dbwebchallenge.webChallengeFileName = solution.challenge.fileName

        val file = File.createTempFile("tmp", "tmp", null)
        val fos = FileOutputStream(file)
        fos.write(solution.challenge.fileClass)
        dbwebchallenge.webChallengeFile = file

        val launcher = Launcher();
        launcher.check(solution.url, dbwebchallenge)

        for ((key, value) in launcher.tests) {
            println("Test - Name: " + key + " Status: " + value.status + " Message: " + value.message)
        }

        val score : Double = launcher.successfulPercentage
        var explanation = ""

        if (score <= 0.0) explanation = "Not good"
        else if (score < 100.0) explanation = "Some right things here"
        else explanation = "Very fine"

        solution.feedback = WebAssessment(explanation, score, launcher.tests.size, launcher.successful, launcher.failed, launcher.successfulPercentage, launcher.tests)

        return solution
    }

    override fun sayHello(text: String) = "Hello $text from $name Port"
}