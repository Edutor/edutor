package dk.edutor.core.routes

import com.google.gson.GsonBuilder
import dk.edutor.eduport.*
import dk.edutor.eduport.simple.SimpleChecker
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.content.*
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.routing.get
/*
val gson = GsonBuilder().setPrettyPrinting().create()

val jarChecker = JarChecker()
val simpleChecker = SimpleChecker()
val mcChecker = MCChecker()


fun Routing.experimental() {
            get("/hello") {
                call.respond("goodbye")
            }
            get("/tag"){
                call.respond(allTags)
            }
            get("/challenge/tag/{tagname}"){
                val tagname = call.parameters["tagname"]?:"" //Should maybe change to !! (double bang) to get an exception when tagname is null?
                call.respond(getChallengeSet(listOf(tagname)))
            }
            post("/postdemo/"){
                val multipart = call.receiveMultipart()

                    if (!call.request.isMultipart()) {
                        call.respond ("Not a multipart request")
                    } else {
                                    val assessmentList: MutableList<Assessment> = mutableListOf<Assessment>()
                        while (true) {
                            val part = multipart.readPart() ?: break

                            when (part) {
                                is PartData.FormItem -> {
                                    println(part.value)
                                    val jsonSol = part.value
                                    val solution: MCSolution = gson.fromJson(jsonSol, MCSolution::class.java)
                                    val result = mcChecker.check(getChallengeById(solution.id.toInt())!!, MCSolution(solution.answers, PersonIdentifier(1),solution.id))
                                    assessmentList.add(result)
                                    println("Resultatet er kommet. grade = ${result.grade}")
                                }
//                                is PartData.FileItem -> call.respond("File field: ${part.partName} -> ${part.originalFileName} of ${part.contentType}")
                            }
                            part.dispose()
                        }
                        call.respond(assessmentList)
                    }
                }

            post("/challenge/submit"){
                throw UnsupportedOperationException("This method is not implemented yet")
            }
            get("/sayHello/{text}") {
                // a ?: b  ->  if (a == null) b else a (java: (a == null) ? b : a )
                val text = call.parameters["text"] ?: ""
                val result = arrayOf(jarChecker.sayHello(text), simpleChecker.sayHello(text))
                call.respond(result)
            }
            get("/check/{answer}") {
                val challenge = StringChallenge("42", "What is 7 multiplied by 6", listOf())
                val answer = call.parameters["answer"] ?: "I don't know"
                val solution = StringSolution(answer, PersonIdentifier(1), 1)
                val assesment = simpleChecker.check(challenge, solution)
                call.respond(assesment)
            }
  }

  //Remove the solution from challenge
data class ChallengeWrapper(val id: Int, val question:String, val choices: List<String>)
fun MCChallenge.removeSolution(): ChallengeWrapper {
    return ChallengeWrapper(this.id, this.question, this.answers.keys.toList())
}

fun getChallengeSet(tags: List<String>): List<ChallengeWrapper> {
    val listAll: MutableList<ChallengeWrapper> = ArrayList()
    for (c in allChallenges.values)
        if(c.tags.intersect(tags).size >= 1){
            when(c){
                is MCChallenge -> listAll.add(c.removeSolution())
            }
        }
    return listAll
}
fun getChallengeById(id: Int):challenge? = allChallenges.get(id)

val allChallenges = mapOf<Int, challenge>(
       1 to MCChallenge(1, answers = mapOf("3" to false, "4" to true, "5" to false), description = "", question = "What is 2 + 2", tags = listOf("Math","Addition")),
       2 to MCChallenge(2, answers = mapOf("0" to false, "18" to false, "20" to true), description = "", question = "What is 2 * 10", tags = listOf("Math", "Multiplication")),
       3 to MCChallenge(3, answers = mapOf("Babirusa" to true, "Crocodile" to false, "Camel" to true), description = "", question = "What animals are mammals", tags = listOf("Bio")),
       4 to MCChallenge(4, answers = mapOf("Beethoven" to true, "Einstein" to false, "Mozart" to true), description = "", question = "Who were great composers", tags = listOf("Music"))
)
val allTags = listOf<String>(
        "Math", "Multiplication", "Bio", "Music"
)

*/
