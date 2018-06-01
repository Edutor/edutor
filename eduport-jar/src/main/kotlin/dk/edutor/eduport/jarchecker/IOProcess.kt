package dk.edutor.eduport.jarchecker

import java.io.*
import java.util.concurrent.TimeUnit

class IOProcess(val process: Process) {
  val writer: BufferedWriter by lazy { process.outputStream.bufferedWriter() }
  val reader: BufferedReader by lazy { process.inputStream.bufferedReader() }

  fun writeLine(line: String) {
    writer.write(line)
    writer.newLine()
    writer.flush()
    }

  fun readLine(timeOut: Long) : String {
    if (!process.waitFor(timeOut, TimeUnit.SECONDS)) {
      process.destroy()
      throw RuntimeException("Timed out")
      }
    return reader.readLine()
    }

  }

fun ByteArray.runAsJarIn(
    workingDir: File,
    script: IOProcess.() -> Unit
    ) {
  val tmp = File.createTempFile("deleteAtWill", ".jar", workingDir)
  tmp.deleteOnExit()
  tmp.writeBytes(this)
  val process = ProcessBuilder("java", "-jar", tmp.name)
      .directory(workingDir)
      .redirectInput(ProcessBuilder.Redirect.PIPE)
      .redirectOutput(ProcessBuilder.Redirect.PIPE)
      .redirectError(ProcessBuilder.Redirect.PIPE)
      .start()

  IOProcess(process).script()

  if (process.exitValue() != 0) {
      throw RuntimeException("ERROR: Process exited with ${process.exitValue()}")
      }
  }


