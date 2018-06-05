package dk.edutor.eduport.webchecker

import java.io.File
import java.io.IOException
import java.io.FileInputStream

class ClassFileLoader : ClassLoader() {

    @Throws(Exception::class)
    private fun getBytes(file: File): ByteArray {
        println("GetBytes...")

        val len = file.length()

        val raw = ByteArray(len.toInt())

        val fin = FileInputStream(file)

        val r = fin.read(raw)
        if (r.toLong() != len) {
            throw IOException("Can't read all, $r != $len")
        }

        fin.close()

        return raw
    }

    @Throws(Exception::class)
    fun loadClass(file: File): Class<*> {
        //println("LoadClass...")

        //println("ClassNameFound: " + file.javaClass.`package`.name)

        //val className = "basics.Basics"
        val fileBytes = getBytes(file)

        return defineClass(null, fileBytes, 0, fileBytes.size)
    }
}