import dk.edutor.core.model.db.*
import dk.edutor.core.routes.admin
import dk.edutor.core.routes.quest
import dk.edutor.eduport.*
import dk.edutor.eduport.Category.*
import dk.edutor.eduport.simple.SimpleChecker
import dk.edutor.eduport.mc.MultipleChoiceChecker
import dk.edutor.eduport.jarchecker.JarChecker
import dk.edutor.eduport.webchecker.WebChecker
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.content.*
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.http.content.*
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.sessions.*
import java.nio.file.Files
import java.nio.file.Paths
import java.text.DateFormat

fun entry(port: Port) = port.key to port
val SimpleChecker = SimpleChecker()
val MultipleChoiceChecker = MultipleChoiceChecker()
val JarChecker = JarChecker()
val WebChecker = WebChecker()

val ports = mapOf(entry(SimpleChecker), entry(MultipleChoiceChecker), entry(JarChecker), entry(WebChecker))
val db = MySqlManager(version = 4)

// data class UserSession(val user: String)

val kurt: User = USERS["kurt"] ?: User(0, "kurt", "Kurt Hansen").persist()
val sonja: User = USERS["sonja"] ?: User(0, "sonja", "Sonja Jensen").persist()

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, 8080) {
        install(DefaultHeaders)
        install(CORS){
            anyHost()
            }
        install(Compression)
        install(CallLogging)
        install(ContentNegotiation) {
            gson {
                setDateFormat(DateFormat.LONG)
                setPrettyPrinting()
                }
            }
        install(Sessions)    {
            cookie<User>("SESSION", storage = SessionStorageMemory())
            }

        routing {
//            get("/hi") {
//                val url = environment.config.propertyOrNull("exposed.url")?.getString() ?: "none"
//                call.respond(url)
//                }
//          endpoint til at teste om serveren er opppe
            get("/hello") {
                call.respond(ports.values.map { it.sayHello("Edutor") })
                }
            get("/run-only-once") {
                Text.Challenge(0, SimpleChecker, "First challenge", "Who is first?", "me")
                  .persist()
                  .tags = listOf(Tag("Java"), Tag("Narcissism"))
                Text.Challenge(0, SimpleChecker, "Second challenge", "Who is next?", "you")
                  .persist()
                  .tags = listOf(Tag("C++"), Tag("Narcissism"))
                Choice.Challenge(
                    0,
                    MultipleChoiceChecker,
                    "Third challenge",
                    "Who is not IT-Seminar university",
                    listOf(
                        Option("Haaga-Helia", -40.0),
                        Option("ITU", 100.0),
                        Option("Hesso-Valais", -40.0),
                        Option("UE", -40.0)
                        )
                    )
                  .persist()
                  .tags = listOf(Tag("Java"), Tag("IoT"), Tag("3D"))
                Choice.Challenge(
                    0,
                    MultipleChoiceChecker,
                    "Fourth challenge",
                    "Which is not a Java keyword",
                    listOf(
                        Option("public", -40.0),
                        Option("private", -40.0),
                        Option("secret", 100.0),
                        Option("static", -40.0)
                        )
                    )
                  .persist()
                  .tags = listOf(Tag("Java"))
                call.respond("Test data created - do not run again!")
                }

            quest()
            admin()

            // experimental()

            static("") {
                resources("www")
                defaultResource("index.html", "www")
            }

        }

    }
    server.start(wait = true)
}
