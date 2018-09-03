package dk.edutor.core.view

import dk.edutor.eduport.*
import dk.edutor.eduport.Category.*

open class SolutionIdentifier(val id: Int)

abstract class SolutionDetail(
        id: Int,
        val challenge: ChallengeIdentifier
) : SolutionIdentifier(id)

class TextSolutionDetail(
        id: Int = 0,
        challenge: ChallengeIdentifier,
        val answer: String
) : SolutionDetail(id, challenge)

class ChoiceSolutionDetail(
        id: Int = 0,
        challenge: ChallengeIdentifier,
        val answers: List<Int>
) : SolutionDetail(id, challenge)

class ExecutableSolutionDetail(
        id: Int = 0,
        challenge: ChallengeIdentifier,
        val files: List<String>
) : SolutionDetail(id, challenge)

class UrlSolutionDetail(
        id: Int = 0,
        challenge: ChallengeIdentifier,
        val url: String
) : SolutionDetail(id, challenge)

fun SolutionDetail.toEntity(solver: User, challenge: Challenge) =
        when (this) {
            is TextSolutionDetail -> Text.Solution(this.id, solver, challenge as Text.Challenge, this.answer)
            is ChoiceSolutionDetail -> Choice.Solution(this.id, solver, challenge as Choice.Challenge, this.answers)
            is ExecutableSolutionDetail -> Executable.Solution(this.id, solver, challenge as Executable.Challenge, byteArrayOf())
            is UrlSolutionDetail -> Url.Solution(this.id, solver, challenge as Url.Challenge, this.url)
            else -> throw RuntimeException("Unknown solution type")
        }
