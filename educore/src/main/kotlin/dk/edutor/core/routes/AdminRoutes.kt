package dk.edutor.core.routes

import db
import dk.edutor.core.model.db.MySqlManager
import dk.edutor.core.model.db.USERS
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import ports
import dk.edutor.eduport.User
import io.ktor.sessions.*
import sonja


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
      call.sessions.set(sonja)
      call.respond("session was null")
      }
    else call.respond(session)
    }

  }