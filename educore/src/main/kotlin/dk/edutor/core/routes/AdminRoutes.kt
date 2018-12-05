package dk.edutor.core.routes

import db
import dk.edutor.core.model.db.*
import io.ktor.application.call
import io.ktor.response.respond
import ports
import dk.edutor.eduport.User
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.routing.*
import io.ktor.sessions.*
import sonja
import java.lang.Exception


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

  post("/user") {
    val user = call.receive<User>()
    try {
      val persitedUser = user.persist()
      call.respond(persitedUser)
      }
    catch (e: Exception) {
      call.respond(HttpStatusCode.Conflict, "User exists")
      }
    }

  post("/login/{code}") {
    val code = call.parameters["code"]
    if (code == null) call.respond(HttpStatusCode.BadRequest, "No user code applied")
    else {
      val user = USERS[code]
      if (user == null) call.respond(HttpStatusCode.Unauthorized, "User code or password is wrong")
      else {
        call.sessions.set(user)
        call.respond(user)
        }
      }
    }

  }