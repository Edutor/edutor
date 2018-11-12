package dk.edutor.core

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing

val host = io.ktor.server.netty.DevelopmentEngine

/*
  Main entry for application.
  TODO: Use Application approach
  */
fun Application.edutor() {
  routing {
    get("/hi") {
      val url = environment.config.property("exposed.url").getString()
      call.respond(url)
      }
    }
  }