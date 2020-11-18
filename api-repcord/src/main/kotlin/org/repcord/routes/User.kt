package org.repcord.routes

import database.impl.Donator
import database.impl.Reputation
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.repcord.database.ReputationModal
import org.repcord.database.User
import org.repcord.database.UserModal

fun Route.userRoutes(){
    route("/{id}") {
        get {
            val id: String? = call.parameters["id"]

            val user: UserModal = User.doesExist(id) ?: return@get call.respond(HttpStatusCode.NotFound, "Doesn't exist.");
            user.description = database.impl.User.getDescription(user.userid)
            user.donator = Donator.isOverall(user.userid)
            user.positiveTotal = Reputation.getTotalPositiveReputationsReceived(user.userid)
            user.negativeTotal = Reputation.getTotalNegativeReputationsReceived(user.userid)
            user.totalReps = Reputation.getTotalReceivedReputations(user.userid)
            return@get call.respond(user)
        }
    }

    route("/{id}/reputations") {
        get {
            val id: String? = call.parameters["id"]
            val reputations : MutableList<ReputationModal> = org.repcord.database.Reputation.getUsersRep(id)
            call.respond(reputations)
        }
    }
}
