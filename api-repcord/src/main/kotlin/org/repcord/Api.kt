package org.repcord

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.features.*
import org.slf4j.event.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import io.ktor.gson.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.repcord.routes.userAuthentication
import org.repcord.routes.userRoutes

fun main() {
    Bot.main(null)
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        anyHost() // @TODO: Add domain in production.
    }

    install(Authentication) {
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        userAuthentication()
        route("/user") {
            userRoutes()
        }
    }
}

