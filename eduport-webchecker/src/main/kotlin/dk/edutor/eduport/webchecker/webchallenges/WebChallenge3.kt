package dk.edutor.eduport.webchecker.webchallenges

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach


class WebChallenge4
{
    var url = System.getProperty("WebCheckerURL")

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            println("testWebChallenge4beforeAll")
        }
    }

    @BeforeEach
    fun before() {
        println("testWebChallenge4before" + url)

    }

    @Test
    fun testWebChallenge4a() {
        println("testWebChallenge4a" + url)
        assertTrue(12 == 12)
    }

    @Test
    fun testWebChallenge4b() {
        println("testWebChallenge4b" + url)
        assertTrue(11 == 12)
    }
}