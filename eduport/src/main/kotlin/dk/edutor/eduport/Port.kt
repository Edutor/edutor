package dk.edutor.eduport

import java.io.Serializable

interface Port {
  fun sayHello(text: String) : String
  fun evaluate(solution: Solution): Solution
  val key: String
  val name: String
  }

data class User (
    val id: Int,
    val code: String,
    val name: String
    ) : Serializable

class Option(val text: String, val grade: Double)

class TestCase(val input: String, val expected: String, val grade: Double, val hidden: Boolean = false, val timeOut: Long)

abstract class Challenge(
    val id: Int,
    val category: Category,
    val port: Port,
    val description: String,
    val question: String
    ) {
  abstract fun copy(id: Int): Challenge
  }

abstract class Solution(
    val id: Int,
    val category: Category,
    val solver: User
    ) {
  var feedback: Assessment? = null
  abstract val challenge: Challenge
  abstract fun copy(id: Int): Solution
  }

open class Assessment(val explanation: String, val grade: Double)

// Virtuel collection of challenges base on markdown in template
data class Quest(val id: Int, val title: String, val template: String)

data class Tag(val code: String)


class LauncherTestResult{
    var status : String = ""
    var message : String = ""
}
class WebAssessment(
        explanation: String,
        // solution: Solution, bruges ikke mere Assessment er en del af solution (feedback)
        grade : Double,
        val numberoftests: Int,
        val successful: Int,
        val failed : Int,
        val percentage : Double,
        val tests: Map<String,LauncherTestResult>
) : Assessment(explanation, grade)

