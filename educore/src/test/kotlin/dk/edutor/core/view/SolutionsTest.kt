package dk.edutor.core.view

import com.nhaarman.mockitokotlin2.*
import dk.edutor.eduport.*
import org.junit.Test
import dk.edutor.core.model.db.CHALLENGES.dtype
import org.junit.Assert.*

class SolutionsTest {


  @Test
  fun testSolutionToIdentifier() {
    val challenge = mock<Category.Choice.Challenge>()
    val user = mock<User>()
    val solution = Category.Choice.Solution(7, user, challenge, listOf(7, 9, 13))

    val solutionIdentifier = solution.toIdentifier()

    assertEquals(solutionIdentifier.id, 7)
    }

//  @Test
//  fun testSolutionToTextDetail() {
//    val challenge = mock<Category.Text.Challenge> {
//      on { id } doReturn 4711
//      }
//    given(challenge.toIdentifier()).willReturn(ChallengeIdentifier(4711, "TEXT"))
//    val user = mock<User>()
//    val solution = Category.Text.Solution(7, user, challenge, "42")
//
//    val solutionDetail = solution.toDetail()
//
//    assertTrue(solutionDetail is TextSolutionDetail)
//    assertEquals(solutionDetail.id, 7)
//    assertEquals(solutionDetail.challenge.id, 4711)
//    }

  }