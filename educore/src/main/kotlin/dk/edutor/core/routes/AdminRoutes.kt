package dk.edutor.core.routes

import db
import dk.edutor.core.model.db.MySqlManager
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import ports
import dk.edutor.eduport.User
import io.ktor.sessions.*


fun Routing.admin() {

  get("/port") {
    call.respond(ports)
    }

  get("/clean") {
    db.cleanDatabase()
    call.respond("Database clean")
    }

  get("/drop") {
    db.drop()
    call.respond("Tables dropped")
    }

  get("/updateVersion") {
    db.update(1, 2)
    call.respond("Tables dropped and created")
    }

  get("/user") {
    val session: User? = call.sessions.get()
    if (session == null) {
      call.sessions.set(User(id = 17, code = "sonja", name = "Sonja Jensen"))
      call.respond("session is null")
      }
    else call.respond(session)
    }

  }