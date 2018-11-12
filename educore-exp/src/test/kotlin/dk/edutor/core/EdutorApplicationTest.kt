package dk.edutor.core

/*

import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import kotlin.test.Test
import kotlin.test.assertEquals


class EdutorApplicationTest {

  @Test
  fun testGetHello() {
    withTestApplication(Application::main) {
      with(handleRequest(HttpMethod.Get, "/hello")) {
        assertEquals(HttpStatusCode.OK, response.status())
        assertEquals("Hello World!", response.content)
        }
      }
    }

  @Test
  fun testGetHelloNameString() {
    withTestApplication(Application::main) {
      val name = "Kurt"
      with(handleRequest(HttpMethod.Get, "/hello/$name")) {
        assertEquals("Hello $name!", response.content)
        }
      with(handleRequest(HttpMethod.Get, "/hello/2")) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
        }
      }
    }

  }

*/