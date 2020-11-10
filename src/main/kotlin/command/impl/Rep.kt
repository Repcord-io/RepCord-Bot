package command.impl

import database.impl.Donator
import database.impl.Prefix
import database.impl.Reputation
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import utils.Helper
import utils.Helper.errorResponse

/*
 * @project RepCord-Bot
 * @author Patrity - https://github.com/Patrity
 * Created on - 11/6/2020
 */

class Rep : ListenerAdapter() {

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        run {
            val command = event.message.contentRaw.split(" ")

            if (command.size < 3) {
                errorResponse(event, "Correct Syntax: `::rep @user [Brief Comment About User]`")
                return
            }

            var target: User? = null
            if (event.message.mentionedUsers.isEmpty()) {
                if (command.isEmpty()) {
                    errorResponse(event, "Please mention a user")
                    return
                }
                try {
                    target = event.jda.retrieveUserById(command[1]).complete()
                } catch (ignored: Exception) {
                    errorResponse(event, "Please mention a valid user.")
                    return
                }

            } else {
                target = event.message.mentionedUsers[0]
            }

            target?.let {
                if (target.isBot) {
                    errorResponse(event, "You cannot give reputation to a bot.")
                    return
                }
                if (event.author.id == target.id) {
                    errorResponse(event, "You cannot give reputation to yourself.")
                    return
                }

                val comment = command.drop(1).drop(1).joinToString(" ")

                if (comment.length > 190) {
                    errorResponse(event, "Your comment is too long. Limit to 190 characters.")
                    return
                }
                if (comment.length < 5) {
                    errorResponse(event, "Your comment is too short. Please use at least 5 characters.")
                    return
                }

                if (Reputation.checkIfRepped(event.author.id, target.id)) {
                    Reputation.modifyRep(event.author.id, target.id, true, comment)

                    val embed = Helper.createEmbed("Reputation Modified")
                    embed.setDescription("${event.author.asTag} repped ${target.asTag}")
                    embed.addField("Comment:", comment, false)
                    Helper.queueEmbed(event, embed)
                    return;
                }
                val cooldown = Reputation.getReputationCooldown(event.author.id)
                if (cooldown > 0 && !Donator.guild(event.guild.id)) {
                    val embed = Helper.createEmbed("Cooldown")
                    embed.setDescription("Please wait another $cooldown minutes before using ::rep again!")
                    embed.setFooter("Donators are immune to cooldown.")
                    Helper.queueEmbed(event, embed)
                    return
                }

                val amount = Reputation.repPower(event.author.id)
                // Caches user who got repped.
                database.impl.User.cacheUser(target)
                Reputation.rep(target.id, amount, comment, event.author.id, event.guild.id)

                val embed = Helper.embed(event, "User Repped")
                embed.setDescription("${event.author.asTag} repped ${target.asTag} for $amount points!")
                embed.addField("Comment:", comment, false)
                embed.setFooter("Enjoying RepCord? Vote for us and get a +2 rep power boost for 12 hours! - ${Prefix.getPrefix(event)}vote")
                Helper.queueEmbed(event, embed)

                //TODO: Impl auto kick/ban check

            }


        }
    }
}