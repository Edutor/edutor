package dk.edutor.core.view

import dk.edutor.eduport.*
import dk.edutor.eduport.Category.*

open class SolutionIdentifier(val id: Int)

abstract class SolutionDetail(
        id: Int,
        val challenge: ChallengeIdentifier,
        val solver: UserSummary,
        val grade: Double?,
        val explanation: String?
) : SolutionIdentifier(id)

class TextSolutionDetail(
        id: Int = 0,
        challenge: ChallengeIdentifier,
        solver: UserSummary,
        grade: Double?,
        explanation: String?,
        val answer: String
) : SolutionDetail(id, challenge, solver, grade, explanation)

class ChoiceSolutionDetail(
        id: Int = 0,
        challenge: ChallengeIdentifier,
        solver: UserSummary,
        grade: Double?,
        explanation: String?,
        val answers: List<Int>
) : SolutionDetail(id, challenge, solver, grade, explanation)

class ExecutableSolutionDetail(
        id: Int = 0,
        challenge: ChallengeIdentifier,
        solver: UserSummary,
        grade: Double?,
        explanation: String?,
        val files: List<String>
) : SolutionDetail(id, challenge, solver, grade, explanation)

class UrlSolutionDetail(
        id: Int = 0,
        challenge: ChallengeIdentifier,
        solver: UserSummary,
        grade: Double?,
        explanation: String?,
        val url: String
) : SolutionDetail(id, challenge, solver, grade, explanation)

fun SolutionDetail.toEntity(solver: User, challenge: Challenge) =
        when (this) {
            is TextSolutionDetail -> Text.Solution(this.id, solver, challenge as Text.Challenge, this.answer)
            is ChoiceSolutionDetail -> Choice.Solution(this.id, solver, challenge as Choice.Challenge, this.answers)
            is ExecutableSolutionDetail -> Executable.Solution(this.id, solver, challenge as Executable.Challenge, byteArrayOf())
            is UrlSolutionDetail -> Url.Solution(this.id, solver, challenge as Url.Challenge, this.url)
            else -> throw RuntimeException("Unknown solution type")
        }

fun Solution.toIdentifier() = SolutionIdentifier(this.id)

fun Solution.toDetail() =
        when (this) {
            is Text.Solution -> TextSolutionDetail(
                    this.id,
                    this.challenge.toIdentifier(),
                    this.solver.toSummary(),
                    this.feedback?.grade,
                    this.feedback?.explanation,
                    this.answer
            )
            is Choice.Solution -> ChoiceSolutionDetail(
                    this.id,
                    this.challenge.toIdentifier(),
                    this.solver.toSummary(),
                    this.feedback?.grade,
                    this.feedback?.explanation,
                    this.answers
            )
            is Url.Solution -> UrlSolutionDetail(
                    this.id,
                    this.challenge.toIdentifier(),
                    this.solver.toSummary(),
                    this.feedback?.grade,
                    this.feedback?.explanation,
                    this.url
            )
            else -> TODO("Implement Executable and URL solutions")
        }

