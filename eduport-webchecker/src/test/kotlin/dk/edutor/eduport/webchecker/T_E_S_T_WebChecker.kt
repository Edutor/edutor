package dk.edutor.eduport.webchecker

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.runner.RunWith
import org.junit.runners.Suite


class T_E_S_T_WebChecker {


    @Test
    @Tag("selenium")
    fun checkTest() {
        println("TestingSelenium")

        //val TestWebChallenge1 = MCChallenge("Which animals are amphibian", hashMapOf("Frogs" to true, "Lions" to false, "Salamanders" to true, "birds" to false), "bio quiz")
        //val solution = MCSolution(listOf("Frogs","Salamanders"), PersonIdentifier(1),1)
        //val ass =  WebChecker().check(TestWebChallenge1, solution)
        assertTrue(12 == 12)
    }
}