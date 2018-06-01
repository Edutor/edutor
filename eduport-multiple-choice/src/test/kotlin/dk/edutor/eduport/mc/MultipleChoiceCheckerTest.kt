package dk.edutor.eduport.mc

import dk.edutor.eduport.*
import dk.edutor.eduport.Category.*
import org.junit.Test

import org.junit.Assert.*

class MultipleChoiceCheckerTest {
  val dummyUser = User(7, "kurt", "Kurt Hansen")

  @Test
  fun testEvaluateCorrectSolution() {
    //println("Checking the Multiple Choice Checker. Check function")
    val challenge = Choice.Challenge(
        0,
        MultipleChoiceChecker(),
        "Some animals live in the water, and on land",
        "Which animals are amphibian",
        listOf(
            Option("Frogs", 50.0),
            Option("Lions", -100.0),
            Option("Salamanders", 50.0),
            Option("birds", -50.0)
            )
        )
    val solution = Choice.Solution(0, dummyUser, challenge, listOf(0, 2))
    val feedback =  MultipleChoiceChecker().evaluate(solution).feedback

    if (feedback == null) fail()
    else assertEquals(feedback.grade, 100.0, 0.001)
    }

  @Test
  fun testEvaluateIncorrectSolution() {
    val challenge = Choice.Challenge(
        0,
        MultipleChoiceChecker(),
        "Some animals live in the water, and on land",
        "Which animals are amphibian",
        listOf(
            Option("Frogs", 50.0),
            Option("Lions", -100.0),
            Option("Salamanders", 50.0),
            Option("birds", -50.0)
            )
        )
    val solution = Choice.Solution(0, dummyUser, challenge, listOf(0, 1))
    val feedback =  MultipleChoiceChecker().evaluate(solution).feedback
    if (feedback == null) fail()
    else assertEquals(feedback.grade, 0.0, 0.001)
    }

  }
