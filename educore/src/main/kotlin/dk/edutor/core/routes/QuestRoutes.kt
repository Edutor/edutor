package dk.edutor.core.routes

import dk.edutor.core.model.db.*
import dk.edutor.core.view.*
import dk.edutor.core.view.markdown.*
import dk.edutor.eduport.*
import dk.edutor.eduport.Category.Url
import dk.edutor.eduport.webchecker.WebChecker
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import ports
import java.io.File
import java.net.URLDecoder

fun Routing.quest() {

    get("/quest/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) call.respond(HttpStatusCode.BadRequest, "Id should exist and be an integer")
        else {
          val quest = QUESTS[id]
          if (quest == null) call.respond(HttpStatusCode.NotFound, "No such quest: $id")
          else call.respond(parse(quest.id, quest.title, quest.template))
          }
        }

    post("/quest") {
      if (!call.request.isMultipart()) call.respond(HttpStatusCode.BadRequest, "Should be multipart")
      else {
        var id = 0
        var title = "No title"
        var template : String? = null
        val multipart = call.receiveMultipart()
        multipart.forEachPart { part ->
          when (part) {
            is PartData.FormItem -> {
              when (part.name) {
                "id" -> id = part.value.toIntOrNull() ?: 0
                "title" -> title = part.value
                }
              }
            is PartData.FileItem -> {
              part.streamProvider().use { input ->
                template = input.reader().readText()
                }
              }
            }
          }
        if (template == null) call.respond(HttpStatusCode.BadRequest, "No file specified")
        else {
          val quest = Quest(id, title, template!!).persist()
          call.respond("${quest.id}: $title\n$template")
          }
        }
      }

    get("/query/{query}") {
        val user = call.sessions.get<User>()
        if (user == null) {
          call.respond(HttpStatusCode.Unauthorized, "User must be logged in")
          return@get
          }
        val query = call.parameters["query"]
        if (query == null) call.respond(HttpStatusCode.BadRequest, "Missing query")
        else {
            val id = query.toIntOrNull()
            if (id == null) call.respond(HttpStatusCode.NotImplemented, "only ids allowed so far")
            else {
                val challenge = CHALLENGES[id]
                if (challenge == null) call.respond(HttpStatusCode.NotFound, "No such challenge #${id}")
                else {
                  val solution = SOLUTIONS.latestFor(id, user.id)
                  call.respond(QuestionDetail(challenge.toDetail(), solution?.toDetail()))
                  }
                }
            }
        }

    get("/solution") {
        call.respond(SOLUTIONS.map { it.toDetail() })
        }

    get("/choice/{test}") {
        val test = call.parameters["test"] ?: "false"
        when (test) {
            "false" -> call.respond(false)
            "true" -> call.respond(true)
            else -> call.respond(HttpStatusCode.NotImplemented, "only 'true' and 'false' implemented so far")
            }
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
                }
            else {
                val challenge = CHALLENGES[id]
                if (challenge == null) call.respond(HttpStatusCode.NotFound, "No such challenge #${id}")
                else call.respond(challenge.toDetail())
                }
            }
        }

    post("/evaluate/{dtype}") {
        val user = call.sessions.get<User>()
        if (user == null) {
          call.respond(HttpStatusCode.Unauthorized, "User must be logged in")
          return@post
          }
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
            val solution = solutionDetail.toEntity(user, challenge)
            val port = ports[challenge.port.key]
            if (port == null) call.respond(HttpStatusCode.BadRequest, "Unknown port #${challenge.port.key}")
            else {
                val result = port.evaluate(solution)
                result.persist()
                val assesment = result.feedback
                if (assesment == null) call.respond(HttpStatusCode.BadRequest, "Feedback unavailable")
                else call.respond(QuestionDetail(challenge.toDetail(), solution.toDetail()))
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

    get("/urlchallenges/{type}") {
        val type: String? = call.parameters["type"]
        if (type != null)
        {
            call.respond(URL_CHALLENGES[type].map { it.toDetail() })
        }
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