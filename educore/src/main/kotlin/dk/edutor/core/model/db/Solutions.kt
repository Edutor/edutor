package dk.edutor.core.model.db

import dk.edutor.eduport.*
import dk.edutor.eduport.Category.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.sql.Blob
import javax.sql.rowset.serial.SerialBlob

object SOLUTIONS : Table(), Iterable<Solution> {
  val id = integer("id").autoIncrement().primaryKey()
  val dtype = varchar("dtype", 20)
  val challengeId = integer("challengeId").references(CHALLENGES.id)
  val solverId = integer("solverId").references(USERS.id)
  val explanation = text("explanation").nullable()
  val grade = decimal("grade", 5, 2).nullable()

  override fun iterator() = transaction {
      SOLUTIONS_JOINED.selectAll().map(::solution).iterator()
      }
  operator fun get(id: Int?): Solution? = transaction {
    if (id == null) null
    else SOLUTIONS_JOINED.select { SOLUTIONS.id eq id }.map(::solution).firstOrNull()
    }

  }

object TEXT_SOLUTIONS : Table() {
  val id = integer("id").references(SOLUTIONS.id).primaryKey()
  val answer = text("answer")
  }

object CHOICE_SOLUTIONS : Table() {
  val id = integer("id").references(SOLUTIONS.id).primaryKey()
  val answers = text("answers")
  }

object EXECUTABLE_SOLUTIONS : Table() {
  val id = integer("id").references(SOLUTIONS.id).primaryKey()
  val bytes = blob("bytes")
  }

val SOLUTIONS_JOINED =
    (SOLUTIONS innerJoin USERS innerJoin CHALLENGES_JOINED) leftJoin
    TEXT_SOLUTIONS leftJoin
    CHOICE_SOLUTIONS leftJoin
    EXECUTABLE_SOLUTIONS

fun textSolution(row: ResultRow) = Text.Solution(
    row[SOLUTIONS.id],
    user(row),
    textChallenge(row),
    row[TEXT_SOLUTIONS.answer]
    )

fun choiceSolution(row: ResultRow) = Choice.Solution(
    row[SOLUTIONS.id],
    user(row),
    choiceChallenge(row),
    row[CHOICE_SOLUTIONS.answers].split(";").map { it.toInt() }
    )

fun Blob.toByteArray() = this.getBytes(0, this.length().toInt())

fun executableSolution(row: ResultRow) = Executable.Solution(
    row[SOLUTIONS.id],
    user(row),
    executableChallenge(row),
    row[EXECUTABLE_SOLUTIONS.bytes].toByteArray()
    )

fun solution(row: ResultRow): Solution {
  val dtype = row[SOLUTIONS.dtype]
  val solution =  when(dtype) {
    Category.TEXT -> textSolution(row)
    Category.CHOICE -> choiceSolution(row)
    Category.EXECUTABLE -> executableSolution(row)
    else -> throw RuntimeException("No such solution type defined: $dtype")
    }
  val explanation = row[SOLUTIONS.explanation]
  solution.feedback =
      if (explanation == null) null
      else Assessment(explanation, (row[SOLUTIONS.grade]?: BigDecimal.ZERO).toDouble())
  return solution
  }

fun SOLUTIONS.persist(solution: Solution): Solution {
  if (solution.id == 0) return transaction {
    val solutionId = SOLUTIONS.insert {
      it[dtype] = solution.category.dtype
      it[challengeId] = solution.challenge.id
      it[solverId] = solution.solver.id
      it[explanation] = solution.feedback?.explanation
      it[grade] = solution.feedback?.grade?.toBigDecimal()
      } get SOLUTIONS.id
    if (solutionId == null) throw RuntimeException("DB error")
    when (solution) {
      is Text.Solution -> TEXT_SOLUTIONS.insert {
        it[id] = solutionId
        it[answer] = solution.answer
        }
      is Choice.Solution -> CHOICE_SOLUTIONS.insert {
        it[id] = solutionId
        it[answers] = solution.answers.joinToString(separator = ";") { it.toString() }
        }
      is Executable.Solution -> EXECUTABLE_SOLUTIONS.insert {
        it[id] = solutionId
        it[bytes] = SerialBlob(solution.bytes)
        }
      else -> throw RuntimeException("Unknown solution type")
      }
    solution.copy(solutionId)
    }
  else return transaction {
    SOLUTIONS.update({ SOLUTIONS.id eq solution.id }) {
      it[challengeId] = solution.challenge.id
      it[solverId] = solution.solver.id
      it[explanation] = solution.feedback?.explanation
      it[grade] = solution.feedback?.grade?.toBigDecimal()
      }
    when (solution) {
      is Text.Solution -> TEXT_SOLUTIONS.update({ id eq solution.id}) {
        it[answer] = solution.answer
        }
      is Choice.Solution -> CHOICE_SOLUTIONS.update({ id eq solution.id}) {
        it[answers] = solution.answers.joinToString(separator = ";") { it.toString() }
        }
      is Executable.Solution -> EXECUTABLE_SOLUTIONS.update({ id eq solution.id }) {
        it[bytes] = SerialBlob(solution.bytes)
        }
      }
    solution
    }
  }

fun Solution.persist() = SOLUTIONS.persist(this)

