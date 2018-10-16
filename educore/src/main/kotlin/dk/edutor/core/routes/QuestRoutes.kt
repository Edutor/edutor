package dk.edutor.core.routes

import dk.edutor.core.model.db.*
import dk.edutor.core.view.*
import dk.edutor.eduport.*
import dk.edutor.eduport.Category.*
import dk.edutor.eduport.webchecker.WebChecker
import io.ktor.application.call
import io.ktor.content.*
import io.ktor.http.HttpStatusCode
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.routing.*
import ports
import kurt
import java.io.File
import java.net.URLDecoder

fun Routing.quest() {

    get("/quest/{id}") {
        val id = (call.parameters["id"] ?: "7").toIntOrNull() ?: 7
        val doc = MdSection( MdText("Overordnede spørgsmål"),
          MdText("Svar på så mange af nedenstaående spørgsmål som muligt, bla bla bla"),
          MdQuery("#1"),
          MdQuery("#2"),
          MdSection( MdText("Hvis du har tid:"),
            MdQuery("#3"),
            MdChoice("!#3",
               MdQuery("#4")
               )
            )
          )
        call.respond(doc)
        }

    get("/challenge") {
        call.respond(CHALLENGES.map { it.toSummary() })
        }

    get("/challenge/{query}") {
        val tagOrId: String? = call.parameters["query"]
        if (tagOrId == null) call.respond(CHALLENGES.map { it.toSummary() })
        else {
            val id = tagOrId.toIntOrNull()
            if (id == null) {
                val tag = Tag(URLDecoder.decode(tagOrId, "UTF-8"))
                call.respond(CHALLENGES_HAVE_TAGS[tag].map { it.toSummary() })
            } else {
                val challenge = CHALLENGES[id]
                if (challenge == null) call.respond(HttpStatusCode.NotFound, "No such challenge #${id}")
                else call.respond(challenge.toDetail())
            }
        }
    }

    post("/evaluate/{dtype}") {
        val dtype = call.parameters["dtype"]!!
        val solutionDetail = when (dtype) {
            Category.TEXT -> call.receive<TextSolutionDetail>()
            Category.CHOICE -> call.receive<ChoiceSolutionDetail>()
            Category.URL -> call.receive<UrlSolutionDetail>()
            else -> throw RuntimeException("Dont know type $dtype")
        }

        val challengeId = solutionDetail.challenge.id
        val challenge = CHALLENGES[challengeId]

        if (challenge == null) call.respond(HttpStatusCode.BadRequest, "No such challenge #$challengeId")
        else {
            val solution = solutionDetail.toEntity(kurt, challenge) // TODO: use logged in person
            val port = ports[challenge.port.key]
            if (port == null) call.respond(HttpStatusCode.BadRequest, "Unknown port #${challenge.port.key}")
            else {
                val result = port.evaluate(solution)
                result.persist()
                val assesment = result.feedback
                if (assesment == null) call.respond(HttpStatusCode.BadRequest, "Feedback unavailable")
                else call.respond(assesment)
                // else call.respond(result)
            }
        }
    }

    post("/evaluate/FILE-MP") {
        println("Uploading")
        val multiPart = call.receiveMultipart()
        var filename = ""
        var fileContent = ""
        // var file: File? = null
        multiPart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    if (part.name == "filename") filename = part.value
                }
                is PartData.FileItem -> {
                    val bin = part.streamProvider().bufferedReader()
                    fileContent = bin.readText()
                }
            }
        }
        println(fileContent)
        call.respond(fileContent)
    }

    post("/evaluate/FILE") {
        val fileContent = call.receiveStream().bufferedReader().readText()
        call.respond(fileContent)
    }

    post("/webchecker/upload") {
        val multipart = call.receiveMultipart()
        val parts = mutableMapOf<String, String>()
        var file = File("")

        if (!call.request.isMultipart()) {
            call.respond("Not a multipart request")
        } else {
            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        parts["" + part.name] = part.value
                    }
                    is PartData.FileItem -> {
                        file = File(part.originalFileName)
                        part.streamProvider().use { its -> file.outputStream().buffered().use { its.copyTo(it) } }
                    }
                }
                part.dispose()
            }

            Url.Challenge(
                    0,
                    WebChecker(),
                    parts["webchallengedescription"]!!,
                    parts["webchallengequestion"]!!,
                    parts["webchallengename"]!!,
                    parts["webchallengetype"]!!,
                    file.name,
                    file.readBytes()
            )
                    .persist()
                    .tags = listOf(Tag("WEB"), Tag("Forms"), Tag("Links"))

            val webChecker = WebChecker()
            webChecker.upload(parts["webchallengename"]!!, parts["webchallengetype"]!!, file)

            call.respond("{\"status\":\"uploaded\"}");
        }
    }
}