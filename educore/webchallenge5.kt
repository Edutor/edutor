package dk.edutor.eduport.webchecker.webchallenges

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach


class webchallenge5 {
    var url = System.getProperty("WebCheckerURL")

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            println("testWebChallenge2beforeAll")
        }
    }

    @BeforeEach
    fun before() {
        println("testWebChallenge2before" + url)

    }

    @Test
    fun testWebChallenge2a() {
        println("testWebChallenge2a" + url)
        assertTrue(12 == 12)
    }

    @Test
    fun testWebChallenge2b() {
        println("testWebChallenge2b" + url)
        assertTrue(11 == 12)
    }
}