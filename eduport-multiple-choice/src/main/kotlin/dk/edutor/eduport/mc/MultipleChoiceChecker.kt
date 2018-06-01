package dk.edutor.eduport.mc

import dk.edutor.eduport.*
import dk.edutor.eduport.Category.*

const val KEY = "MULTIPLE_CHOICE"

class MultipleChoiceChecker : Port {
    override fun evaluate(solution: Solution): Solution {
        if (!(solution is Choice.Solution)) throw IllegalArgumentException("Solution should be Multiple Choice")
        val score = solution.answers.sumByDouble { index -> solution.challenge.options[index].grade }
        if (score <= 0.0) solution.feedback = Assessment("Not too good", 0.0)
        else if (score < 100.0) solution.feedback = Assessment("Some right things here", score)
        else solution.feedback = Assessment("Very fine", 100.0)
      return solution
      }

    override val key = KEY

    override val name = "Multiple Choice Checker"

    override fun sayHello(text: String) = "Hello $text from $name Port"

}