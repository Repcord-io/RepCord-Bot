import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    Bot.main(null)
    embeddedServer(Netty, port = 8000) {
        routing {
            get ("/") { call
                call.respondText("Hello, world!")
            }
        }
    }.start(wait = true)
}