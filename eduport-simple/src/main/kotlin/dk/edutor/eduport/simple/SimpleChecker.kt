package dk.edutor.eduport.simple

import dk.edutor.eduport.*
import dk.edutor.eduport.Category.*

const val KEY = "SIMPLE"

class SimpleChecker : Port {
  override fun evaluate(solution: Solution): Solution {
    if (!(solution is Text.Solution)) throw IllegalArgumentException("Solution should be Text")
    solution.feedback =
        if (solution.challenge.answer == solution.answer) Assessment("Impressive!", 100.0)
        else Assessment("Too bad", 0.0)
    return solution
    }
  override val key = KEY

  override val name = "Simple Checker"

  override fun sayHello(text: String) = "Hello $text from $name Port"

  }
