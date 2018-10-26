package dk.edutor.core

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import java.text.DateFormat


fun Application.main() {
  install(DefaultHeaders)
  install(CORS) { anyHost() }
  install(Compression)
  install(CallLogging)
  install(ContentNegotiation) {
    gson {
      setDateFormat(DateFormat.LONG)
      setPrettyPrinting()
      }
    }

  routing {
    get("/hello") {
      call.respond("Hello World!")
      }
    get("/hello/{name}") {
      val name = call.parameters["name"]!!
      val number = name.toIntOrNull()
      if (number == null) call.respond("Hello ${name}!")
      else if (number == 1) call.respond("Hello winner!")
      else call.respond(HttpStatusCode.BadRequest, "We don't greet loosers")
      }
    }

  }