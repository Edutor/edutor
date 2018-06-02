package dk.edutor.core.model.db

import dk.edutor.core.model.PersistenceManager
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.transactions.transaction
import ports
import java.util.*


operator fun Properties.get(key: String, default: String) = this.getProperty(key, default)

object PROPERTIES : Table() {
  val code = varchar("code", 16).primaryKey()
  val text = text("text")
  operator fun get(key: String) = transaction {
    PROPERTIES.select { code eq key} .map { it[text] }.firstOrNull()
    }
  operator fun set(key: String, value: String) {
    transaction {
      if (PROPERTIES.update({ code eq key}) { it[text] = value } < 1)
          PROPERTIES.insert { it[code] = key; it[text] = value }
      }
    }
  var version: Int
    get() = (this["VERSION"] ?: "0").toIntOrNull() ?: 0
    set(value) { this["VERSION"] = value.toString() }
  }

class MySqlManager(val version: Int) : PersistenceManager {
  val configuration = Properties()

  init {
    configuration.load(this.javaClass.classLoader.getResourceAsStream("exposed.properties"))
    Database.connect("jdbc:mysql://localhost:3306/edutor",
        driver = "com.mysql.jdbc.Driver",
        user = configuration["user", ""],
        password = configuration["password", ""]
        )
    val actualVersion = transaction {
      create(PROPERTIES)
      PROPERTIES.version
      }
    if (actualVersion == 0) create(version)
    else if (actualVersion < version) update(actualVersion, version)
    }

  fun create(version: Int) {
    transaction {
      create(USERS)
      create(TAGS)
      create(QUESTS)

      create(CHALLENGES)
      create(TEXT_CHALLENGES, CHOICE_CHALLENGES, EXECUTABLE_CHALLENGES)

      create(SOLUTIONS)
      create(TEXT_SOLUTIONS, CHOICE_SOLUTIONS, EXECUTABLE_SOLUTIONS)

      create(CHALLENGES_HAVE_TAGS)

      PROPERTIES.version = version
      }
    }

  fun update(oldVersion: Int, newVersion: Int) {
    drop()
    create(newVersion)
    }

  fun drop() {
    transaction {
      drop(CHALLENGES_HAVE_TAGS)
      drop(EXECUTABLE_SOLUTIONS, CHOICE_SOLUTIONS, TEXT_SOLUTIONS)
      drop(SOLUTIONS)
      drop(EXECUTABLE_CHALLENGES, CHOICE_CHALLENGES, TEXT_CHALLENGES)
      drop(CHALLENGES)
      drop(USERS)
      drop(TAGS)
      drop(QUESTS)
      }
    }

  fun cleanDatabase() {
    transaction {
      CHOICE_CHALLENGES.deleteAll()
      TEXT_CHALLENGES.deleteAll()
      CHALLENGES.deleteAll()

      CHOICE_SOLUTIONS.deleteAll()
      TEXT_SOLUTIONS.deleteAll()
      SOLUTIONS.deleteAll()
      }
    }

  }

fun portFromKey(key: String) = ports[key]!! // TODO get rid of !!
