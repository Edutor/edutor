package dk.edutor.core.model.db

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object TAGS : Table() {
  val code = varchar("code", 16).primaryKey()
  }

fun tag(row: ResultRow) = Tag()