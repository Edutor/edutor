package dk.edutor.eduport.webchecker

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue

class TestWebChallenge1 {
    var url = System.getProperty("WebCheckerURL")

    @Test
    fun testWebChallenge3a() {
        println("testWebChallenge3a")
        assertTrue(12 == 12)
    }

    @Test
    fun testWebChallenge3b() {
        println("testWebChallenge3b")
        assertTrue(12 == 12)
    }
}