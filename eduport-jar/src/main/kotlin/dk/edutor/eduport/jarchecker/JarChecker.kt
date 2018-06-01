package dk.edutor.eduport.jarchecker

import dk.edutor.eduport.*

const val KEY = "JAR"

class JarChecker : Port {

  override fun evaluate(solution: Solution): Solution {
    TODO("not implemented")
    }

  override val key = KEY

  override val name = "Java Application JAR Checker"

  override fun sayHello(text: String) = "Hello $text from $name Port"

  }
