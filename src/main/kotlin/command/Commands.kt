package command

import command.impl.Help
import command.impl.Rep
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter


/*
 * @project RepCord-Bot
 * @author Patrity - https://github.com/Patrity
 * Created on - 11/6/2020
 */
enum class Commands(val description: String, val adapter: ListenerAdapter) {

    HELP( "Lists available commands", Help()),
    REP( "Adds positive reputation to a user", Rep());


    companion object {

        private val VALUES = values();

        fun isCommand(event: GuildMessageReceivedEvent) : Commands? {
            val message = event.message.contentRaw.toLowerCase()
            val prefix = Bot.config.default_prefix
            for (command in VALUES) {
                if (message.startsWith(prefix + command.toLowerName())) {
                    return command
                }
            }
            return null
        }
    }

    fun toLowerName() : String {
        return name.toLowerCase()
    }

}