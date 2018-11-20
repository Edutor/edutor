package dk.edutor.core

import com.auth0.jwt.*
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Payload
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
//import io.ktor.
import java.text.DateFormat

fun Application.main() {
  val jwtIssuer = environment.config.property("jwt.issuer").getString()
  val jwtAudience = environment.config.property("jwt.audience").getString()
  val jwtRealm = environment.config.property("jwt.realm").getString()

  install(Authentication) {
    jwt {
        realm = jwtRealm
        verifier(makeJwtVerifier(jwtIssuer, jwtAudience))
        validate { credential ->
            if (credential.payload.audience.contains(jwtAudience))
                JWTPrincipal(credential.payload)
            else null
        }
      }
    }
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

    authenticate {
      route("/who") {
        handle {
          val principal = call.authentication.principal<JWTPrincipal>()
          val subjectString = principal!!.payload.subject.removePrefix("auth0|")
          call.respondText("Success, $subjectString")
          }
        }
      }
    get("/login") {
      //JWTPrincipal()
      }

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

private val algorithm = Algorithm.HMAC256("edutor") // TODO: change to dynamic
private fun makeJwtVerifier(issuer: String, audience: String): JWTVerifier = JWT
    .require(algorithm)
    .withAudience(audience)
    .withIssuer(issuer)
    .build()

