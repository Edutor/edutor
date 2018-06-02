package dk.edutor.core.routes

import dk.edutor.core.model.db.*
import dk.edutor.core.view.*
import dk.edutor.eduport.*
import io.ktor.application.call
import io.ktor.content.*
import io.ktor.http.HttpStatusCode
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.routing.*
import ports
import kurt
import java.net.URLDecoder

fun Routing.quest() {
  get ("/challenge") {
    call.respond(CHALLENGES.map { it.toSummary() })
    }

  get ("/challenge/{query}") {
    val tagOrId : String? = call.parameters["query"]
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
    val dtype = call.parameters["dtype"]!!
    val solutionDetail = when (dtype) {
        Category.TEXT -> call.receive<TextSolutionDetail>()
        Category.CHOICE -> call.receive<ChoiceSolutionDetail>()
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
        is PartData.FormItem -> { if (part.name == "filename") filename = part.value }
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

  }