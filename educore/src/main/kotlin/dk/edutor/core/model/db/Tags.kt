package dk.edutor.core.model.db

import dk.edutor.eduport.Challenge
import dk.edutor.eduport.Tag
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object TAGS : Table() {
  val code = varchar("code", 16).primaryKey()

  operator fun get(key: String?) : Tag? = transaction {
    if (key == null) null
    else TAGS.select { code eq key }.map(::tag).firstOrNull()
    }

  fun persist(tag: Tag): Tag {
    return transaction {
      if (TAGS.select { code eq tag.code }.empty()) {
        TAGS.insert { it[code] = tag.code }
        }
      tag
      }
    }
  }

object CHALLENGES_HAVE_TAGS: Table() {
  val challengeId = integer("challengeId").references(CHALLENGES.id)
  val tagCode = varchar("code", 16).references(TAGS.code)

  operator fun get(tag: Tag) : List<Challenge> = transaction {
    CHALLENGES_HAVE_TAGS_JOINED.select { tagCode eq tag.code }.map(::challenge)
    }

  operator fun get(challenge: Challenge): List<Tag> = transaction{
    CHALLENGES_HAVE_TAGS.select { challengeId eq challenge.id }.map(::tag)
    }

  operator fun set(challenge: Challenge, tags: List<Tag>) {
    transaction {
      CHALLENGES_HAVE_TAGS.deleteWhere { challengeId eq challenge.id }
      tags.forEach { tag ->
        TAGS.persist(tag)
        CHALLENGES_HAVE_TAGS.insert {
          it[challengeId] = challenge.id
          it[tagCode] = tag.code
          }
        }
      }
    }

  }

val CHALLENGES_HAVE_TAGS_JOINED =
    CHALLENGES_HAVE_TAGS innerJoin
    CHALLENGES leftJoin
    TEXT_CHALLENGES leftJoin
    CHOICE_CHALLENGES leftJoin
    EXECUTABLE_CHALLENGES // innerJoin TAGS

fun tag(row: ResultRow) = Tag(row[TAGS.code])

fun Tag.persist() = TAGS.persist(this)

var Challenge.tags: List<Tag>
  get() = CHALLENGES_HAVE_TAGS[this]
  set(values) { CHALLENGES_HAVE_TAGS[this] = values }
