package dk.edutor.core.model.db

import dk.edutor.eduport.Quest
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object QUESTS : Table(), Iterable<Quest> {
  val id = integer("id").primaryKey().autoIncrement()
  val title = text("title")
  val template = text("template")

  override fun iterator() : Iterator<Quest> = transaction { QUESTS.selectAll().map(::quest).iterator() }

  operator fun get(id: Int?): Quest? = transaction {
    if (id == null) null
    else QUESTS.select { QUESTS.id eq id }.map(::quest).firstOrNull()
    }

  fun persist(quest: Quest): Quest {
    if (quest.id == 0) return transaction {
      val questId = QUESTS.insert {
        it[title] = quest.title
        it[template] = quest.template
        } get QUESTS.id
      if (questId == null) throw RuntimeException("Error inserting $quest")
      quest.copy(id = questId)
      }
    else return transaction {
      QUESTS.update({ id eq quest.id}) {
        it[title] = quest.title
        it[template] = quest.template
        }
      quest
      }
    }
  }

fun quest(row: ResultRow) = Quest(row[QUESTS.id], row[QUESTS.title], row[QUESTS.template])

fun Quest.persist() = QUESTS.persist(this)

