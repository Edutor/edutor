package dk.edutor.core.model.db

import dk.edutor.eduport.*
import dk.edutor.eduport.Category.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


object CHALLENGES : Table(), Iterable<Challenge> {
  val id = integer("id").autoIncrement().primaryKey()
  val dtype = varchar("dtype", 20)
  val portKey = varchar("portKey", 20)
  val description = text("description")
  val question = text("question")

  override fun iterator(): Iterator<Challenge> {
    return transaction { CHALLENGES_JOINED.selectAll().map(::challenge) }.iterator()
    }

  operator fun get(id: Int?): Challenge? = transaction {
    if (id == null) null
    else CHALLENGES_JOINED.select { CHALLENGES.id eq id }.map(::challenge).firstOrNull()
    }

  }

object TEXT_CHALLENGES : Table() {
  val id = integer("id").references(CHALLENGES.id).primaryKey()
  val answer = text("answer")
  }

object CHOICE_CHALLENGES : Table() {
  val id = integer("id").references(CHALLENGES.id).primaryKey()
  val options = text("options")
  }

object EXECUTABLE_CHALLENGES : Table() {
  val id = integer("id").references(CHALLENGES.id).primaryKey()
  val testCases = text("testCases")
  }

val CHALLENGES_JOINED =
    CHALLENGES leftJoin
    TEXT_CHALLENGES leftJoin
    CHOICE_CHALLENGES leftJoin
    EXECUTABLE_CHALLENGES

fun stringFromOptionList(list: List<Option>) =
    list.map { "${it.text}:${it.grade}"}.joinToString(separator = ";")

fun optionListFromString(text: String) =
    text.split(";")
        .map {
            val p = it.split(":")
            Option(p[0], p[1].toDouble())
            }

fun TestCase.toText() = "$input:$expected:$grade:$hidden:$timeOut"

fun testCase(text: String): TestCase {
  val parts = text.split(":")
  return TestCase(parts[0], parts[1], parts[2].toDouble(), parts[3].toBoolean(), parts[4].toLong())
  }

fun textChallenge(row: ResultRow) =
    Text.Challenge(
        row[CHALLENGES.id],
        portFromKey(row[CHALLENGES.portKey]),
        row[CHALLENGES.description],
        row[CHALLENGES.question],
        row[TEXT_CHALLENGES.answer]
        )

fun choiceChallenge(row: ResultRow) =
    Choice.Challenge(
        row[CHALLENGES.id],
        portFromKey(row[CHALLENGES.portKey]),
        row[CHALLENGES.description],
        row[CHALLENGES.question],
        optionListFromString(row[CHOICE_CHALLENGES.options])
        )

fun executableChallenge(row: ResultRow) =
    Executable.Challenge(
        row[CHALLENGES.id],
        portFromKey(row[CHALLENGES.portKey]),
        row[CHALLENGES.description],
        row[CHALLENGES.question],
        row[EXECUTABLE_CHALLENGES.testCases].split(";").map(::testCase)
        )

fun challenge(row: ResultRow): Challenge {
  val dtype = row[CHALLENGES.dtype]
  return when (dtype) {
    Category.TEXT -> textChallenge(row)
    Category.CHOICE -> choiceChallenge(row)
    Category.EXECUTABLE -> executableChallenge(row)
    else -> throw RuntimeException("No such challenge type defined: $dtype")
    }
  }

fun CHALLENGES.persist(challenge: Challenge): Challenge {
  if (challenge.id == 0) return transaction {
    val challengeId = CHALLENGES.insert {
      it[dtype] = challenge.category.dtype
      it[portKey] = challenge.port.key
      it[description] = challenge.description
      it[question] = challenge.question
      } get CHALLENGES.id
    if (challengeId == null) throw RuntimeException("oups")
    when (challenge) {
      is Category.Text.Challenge -> TEXT_CHALLENGES.insert {
        it[id] = challengeId
        it[answer] = challenge.answer
        }
      is Category.Choice.Challenge -> CHOICE_CHALLENGES.insert {
        it[id] = challengeId
        it[options] = stringFromOptionList(challenge.options)
        }
      is Category.Executable.Challenge -> EXECUTABLE_CHALLENGES.insert {
        it[id] = challengeId
        it[testCases] = challenge.testCases.joinToString(separator = ";") { it.toString() }
        }
      else -> throw RuntimeException("Unknown type")
      }
    challenge.copy(id = challengeId)
    }
  else return transaction {
    CHALLENGES.update({ CHALLENGES.id eq challenge.id }) {
      it[portKey] = challenge.port.key
      it[description] = challenge.description
      it[question] = challenge.question
      }
    when (challenge) {
      is Category.Text.Challenge -> TEXT_CHALLENGES.update({ id eq challenge.id }) {
        it[answer] = challenge.answer
        }
      is Category.Choice.Challenge -> CHOICE_CHALLENGES.update({ id eq challenge.id }) {
        it[options] = stringFromOptionList(challenge.options)
        }
      is Category.Executable.Challenge -> EXECUTABLE_CHALLENGES.update({ id eq challenge.id }) {
        it[testCases] = challenge.testCases.joinToString(separator = ";") { it.toString() }
        }
      else -> throw RuntimeException("Unknown type")
      }
    challenge
    }
  }

fun Challenge.persist() = CHALLENGES.persist(this)



