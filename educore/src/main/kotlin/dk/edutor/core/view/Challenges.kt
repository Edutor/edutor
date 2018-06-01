package dk.edutor.core.view

import dk.edutor.eduport.*
import dk.edutor.eduport.Category.*

open class ChallengeIdentifier(val id: Int, val dtype: String)

class ChallengeSummary(
    id: Int,
    dtype: String,
    val description: String,
    var question: String
    ) : ChallengeIdentifier(id, dtype)

abstract class ChallengeDetail(
    id: Int,
    dtype: String,
    val portKey: String,
    val template: String
    ) : ChallengeIdentifier(id, dtype)


class TextChallengeDetail(
    id: Int,
    portKey: String,
    val description: String,
    val question: String
    ) : ChallengeDetail(id, Text().dtype, portKey, "text")

class ChoiceChallengeDetail(
    id: Int,
    portKey: String,
    var description: String,
    var question: String,
    var answers: List<String>
    ) : ChallengeDetail(id, Choice().dtype, portKey, "checkbox")

class ExecutableChallengeDetail(
    id: Int,
    portKey: String,
    var description: String,
    var question: String,
    var testCases: List<String>
    ) : ChallengeDetail(id, Choice().dtype, portKey, "file")

abstract class ChallengeAdminDetail(
    id: Int,
    dtype: String,
    var portKey: String
    ) : ChallengeIdentifier(id, dtype)

class TextChallengeAdminDetail(
    id: Int,
    portKey: String,
    var description: String,
    var question: String,
    var answer: String
    ) : ChallengeAdminDetail(id, Text().dtype, portKey)

class ChoiceChallengeAdminDetail(
    id: Int,
    portKey: String,
    var description: String,
    var question: String,
    var answers: List<Option>
    ) : ChallengeAdminDetail(id, Choice().dtype, portKey)

fun Challenge.toSummary() =
    ChallengeSummary(
        this.id,
        this.category.dtype,
        this.description,
        this.question
        )

fun Challenge.toDetail() =
    when (this) {
      is Text.Challenge -> TextChallengeDetail(
          this.id,
          this.port.key,
          this.description,
          this.question
          )
      is Choice.Challenge -> ChoiceChallengeDetail(
          this.id,
          this.port.key,
          this.description,
          this.question,
          this.options.map { it.text }
          )
      is Executable.Challenge -> ExecutableChallengeDetail(
          this.id,
          this.port.key,
          this.description,
          this.question,
          this.testCases.filter { !it.hidden } .map { "${it.input} --> ${it.expected}"}
          )
      else -> throw RuntimeException("challenge type not defined")
      }

fun Challenge.toAdminDetail() =
    when (this) {
      is Text.Challenge ->  TextChallengeAdminDetail(
          this.id,
          this.port.key,
          this.description,
          this.question,
          this.answer
          )
      is Choice.Challenge -> ChoiceChallengeAdminDetail(
          this.id,
          this.port.key,
          this.description,
          this.question,
          this.options
          )
      else -> throw RuntimeException("challenge type not defined")
      }

