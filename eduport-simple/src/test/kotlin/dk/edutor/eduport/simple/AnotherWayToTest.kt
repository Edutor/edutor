package dk.edutor.eduport.simple

import dk.edutor.eduport.*
import dk.edutor.eduport.Category.*
import io.kotlintest.*
import io.kotlintest.matchers.plusOrMinus
import io.kotlintest.specs.StringSpec

/*
 * See: https://github.com/kotlintest/kotlintest
 */

val dummyUser = User(7, "kurt", "Kurt Hansen")

class AnotherWayToTest : StringSpec({

  "say hello to kurt should give message with Kurt" {
    SimpleChecker().sayHello("Sonja") shouldBe "Hello Sonja from Simple Checker Port"
    }

  "Answering 42 to HHGttG challenge should give grade 12" {
    val challenge = Text.Challenge(7, SimpleChecker(), "Hitchhikers Guide question", "What is the answer to life, universe and everything", "42")
    val solution = Text.Solution(1, dummyUser, challenge, "42")
    val result = SimpleChecker().evaluate(solution)
    val feedback = result.feedback

    feedback shouldNotBe null
    feedback?.grade shouldBe (100.0 plusOrMinus 0.001)
    }

  "Answering 7 to HHGttG challenge should give grade 0" {
    val challenge = Text.Challenge(7, SimpleChecker(), "Hitchhikers Guide question", "What is the answer to life, universe and everything", "42")
    val solution = Text.Solution(1, dummyUser, challenge, "7")
    val result = SimpleChecker().evaluate(solution)
    val feedback = result.feedback

    feedback shouldNotBe null
    feedback?.grade shouldBe (0.0 plusOrMinus 0.001)
    }

  })
