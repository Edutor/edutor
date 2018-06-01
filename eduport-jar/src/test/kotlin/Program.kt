import dk.edutor.eduport.jarchecker.runAsJarIn
import java.io.File

fun main(args: Array<String>) {
  val org = File("/Users/AKA/Edutor/edutor-jar-tor/ExampleJars/AddGood.jar")
  val bytes = org.readBytes()
  bytes.runAsJarIn(File("/Users/AKA/tmp")) {
    writeLine("2 2")
    val result = readLine(20)
    if (result == "4") println("Hurra")
    else println("Øv")
    }
  }
