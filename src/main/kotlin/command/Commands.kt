package command

import Bot
import command.impl.*
import database.impl.Guild
import database.impl.User
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter


/*
 * @project RepCord-Bot
 * @author Patrity - https://github.com/Patrity
 * Created on - 11/6/2020
 */
enum class Commands(val description: String, val adapter: ListenerAdapter) {

    REP("Adds positive reputation to a user.", Rep()),
    NEG("Adds negative reputation to a user.", Neg()),
    CHECK("View basic overview about a user.", Check()),
    VOTE("Provides details on how to vote.", Vote()),
    PREFIX("Modifies the prefix the bot responds to.", Prefix()),
    LEADERBOARD("Returns global leaderboard information.", Leaderboard()),
    SCOREBOARD("Returns guild scoreboard information.", Scoreboard()),
    INVITE("Invite link to invite RepCord to your server.", Invite()),
    INFO("Displays stats about RepCord.", Info()),
    DONATE("Provides donation link.", Donate()),
    HELP("Lists available commands.", Help());

    companion object {

        private val VALUES = values()

        fun isCommand(event: GuildMessageReceivedEvent): Commands? {
            User.cacheUser(event.author)
            Guild.addGuild(event.guild)
            val message = event.message.contentRaw.toLowerCase()
            val prefix = database.impl.Prefix.getPrefix(event)
            for (command in VALUES) {
                if (message.startsWith("$prefix${command.toLowerName()}") || message.startsWith("${Bot.config.default_prefix}help") && command.toLowerName() == "help") {
                    return command
                }
            }
            return null
        }

        fun containsCommand(event: GuildMessageReceivedEvent): Boolean {
            val message = event.message.contentRaw.toLowerCase()
            for (command in VALUES) {
                if (message.contains(command.toLowerName()))
                    return true
            }
            return false
        }
    }

    fun toLowerName(): String {
        return name.toLowerCase()
    }

}