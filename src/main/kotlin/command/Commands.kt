package command

import Bot
import command.impl.*
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter


/*
 * @project RepCord-Bot
 * @author Patrity - https://github.com/Patrity
 * Created on - 11/6/2020
 */
enum class Commands(val description: String, val adapter: ListenerAdapter) {

    REP("Adds positive reputation to a user", Rep()),
    HELP("Lists available commands", Help()),
    VOTE("Provides details on how to vote.", Vote()),
    PREFIX("Modifies the prefix the bot responds to", Prefix()),
    LEADERBOARD("Returns global leaderboard information.", Leaderboard()),
    INVITE("Invite link to invite RepCord to your server.", Invite()),
    DONATE("Provides donation link.", Donate()),
    INFO("Displays stats about RepCord.", Info()),
    CHECK("View basic overview about a user.", Check());

    companion object {

        private val VALUES = values();

        fun isCommand(event: GuildMessageReceivedEvent): Commands? {
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