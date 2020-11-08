package command.impl

import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import utils.Helper
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class Check : ListenerAdapter() {
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        run {
            val command = event.message.contentRaw.split(" ")

            var target: User = event.author
            if (command.size > 1) {
                target = if(event.message.mentionedUsers.isEmpty()) {
                    try {
                        event.jda.retrieveUserById(command[1]).complete()
                    } catch (ignored: Exception) {
                        Helper.errorResponse(event, "Please mention a valid user.")
                        return
                    }
                }else {
                    event.message.mentionedUsers[0]
                }
            }

            if(target.isBot) {
                Helper.errorResponse(event, "Bots cannot be given reputation.")
                return;
            }
            // TODO: Guild premium and normal premium should give donator badge
            val donator: Boolean = database.impl.Donator.user(target.id)
            val userTitle: String = database.impl.User.getTitle(target.id)
            val userDescription: String = database.impl.User.getDescription(target.id)
            val activeVote: Boolean = database.impl.Vote.active(target.id)
            val rank: Int = database.impl.Reputation.getRank(target.id)
            val score: Int = database.impl.Reputation.getScore(target.id)
            val guildScore: Int = database.impl.Reputation.getGuildScore(target.id, event.guild.id)
            val receivedRepsTotal: Int = database.impl.Reputation.getTotalReceivedReputations(target.id)
            val receivedPositiveRepsTotal: Int = database.impl.Reputation.getTotalPositiveReputationsReceived(target.id)
            val receivedNegativeRepsTotal: Int = database.impl.Reputation.getTotalNegativeReputationsReceived(target.id)
            val totalRepPower: Int = database.impl.Reputation.repPower(target.id)
            val totalGivenReps: Int = database.impl.Reputation.getTotalGivenReps(target.id)

            val embed = Helper.createEmbed("Reputation information")
            embed.setDescription(target.asTag)
            embed.setThumbnail("https://repcord.io/logo.png")
            embed.setImage(target.avatarUrl)
            embed.addField("Account creation", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC")).format(target.timeCreated.toInstant()), true)
            embed.addField("Rep Score", score.toString(), true)
            embed.addField("Guild Rep Score", guildScore.toString(), true)
            embed.addField("Total Received Reps", receivedRepsTotal.toString(), true)
            embed.addField("Positive Reps", receivedPositiveRepsTotal.toString(), true)
            embed.addField("Negative Reps", receivedNegativeRepsTotal.toString(), true)
            embed.addField("Rep Power", totalRepPower.toString(), true)
            embed.addField("Rank", rank.toString(), true)
            embed.addField("Total Given Reps", totalGivenReps.toString(), true)
            embed.addField("Title", if(userTitle.isNotEmpty()) userTitle else "None", true)
            embed.addField("Donator", if (donator) "Yes" else "No", true)
            embed.addField("Active Vote", if(activeVote) "Yes" else "No", true)
            embed.addField("Description", if(userDescription.isNotEmpty()) userDescription else "None", true)


            embed.addField("Profile", "To see more information about this user, visit https://repcord.io/user/${target.id}", false)
            Helper.queueEmbed(event, embed)
        }
    }
}
