package dk.edutor.core.model.db

import dk.edutor.eduport.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object USERS : Table(), Iterable<User> {
  val id = integer("id").autoIncrement().primaryKey()
  val code = varchar("code", 16).uniqueIndex()
  val name = text("name")

  override fun iterator(): Iterator<User> = transaction { USERS.selectAll().map(::user).iterator() }

  operator fun get(id: Int?): User? = transaction {
    if (id == null) null
    else USERS.select { USERS.id eq id }.map(::user).firstOrNull()
    }

  operator fun get(code: String?): User? = transaction {
    if (code == null) null
    else USERS.select { USERS.code eq code }.map(::user).firstOrNull()
    }

  fun persist(user: User): User {
    if (user.id == 0) return transaction {
      val userId = USERS.insert { row ->
        row[code] = user.code
        row[name] = user.name
        } get USERS.id
      if (userId == null) throw RuntimeException("Error inserting User")
      user.copy(id = userId)
      }
    else return transaction {
      USERS.update({ USERS.id eq user.id}) { row ->
        row[code] = user.code
        row[name] = user.name
        }
      user
      }
    }

  }

fun user(row: ResultRow) = User(row[USERS.id], row[USERS.code], row[USERS.name])

fun User.persist() = USERS.persist(this)