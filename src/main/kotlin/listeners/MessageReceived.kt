package listeners

import command.Commands
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter


/*
 * @project RepCord-Bot
 * @author Patrity - https://github.com/Patrity
 * Created on - 11/6/2020
 *
 * This class handles incoming messages and determines if they
 * are commands that should be processed by the bot.
 */
class MessageReceived : ListenerAdapter() {

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {

        /*
         * Check if the message was sent by a bot, or if we can talk in the channel.
         * Return if not.
         */

        if (event.author.isBot || !event.channel.canTalk()) {
            return
        }

        /*
         * Check if the message sent contains any strings containing
         * command names in order to reduce SQL queries on every message.
         */
        if (!Commands.containsCommand(event)) {
            return;
        }

        /*
         * Check if the command in the string also starts with the prefix.
         */
        val command = Commands.isCommand(event)

        /*
         * Return the command class corresponding to the command requested.
         */
        command?.adapter?.onGuildMessageReceived(event)

    }
}