package org.repcord.routes

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

private data class LoginDetails(
        val username: String,
        val password: String
)

fun Route.userAuthentication() {
    route("/login") {
        post {
            val login = call.receive<LoginDetails>()
            println(login.username)
            println(login.password)
        }
    }
}

