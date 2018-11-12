package dk.edutor.core

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.Application
import io.ktor.auth.Principal
import io.ktor.http.ContentType
import java.util.*

class User(val id: Long, val name: String) : Principal

class JwtConfigurator(val application: Application) {

    private val secret = "zAP5MBA4B4Ijz0MZaS48"
    private val issuer = application.environment.config.property("jwt.issuer").getString()
    private val validityInMs = 36_000_00 * 10 // 10 hours
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun makeToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("id", user.id)
        // .withArrayClaim("countries", user.countries.toTypedArray())
        .withExpiresAt(Date(System.currentTimeMillis() + validityInMs))
        .sign(algorithm)


  }