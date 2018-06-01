package dk.edutor.eduport.simple

import org.junit.Assert.*
import org.junit.Test

class SimpleCheckerTest {

  @Test
  fun testSayHelloKurt() {
    assertEquals("Hello Kurt from Simple Checker Port", SimpleChecker().sayHello("Kurt"))
    }

  }