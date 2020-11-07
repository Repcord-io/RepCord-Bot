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

        val command = Commands.isCommand(event)

        command?.adapter!!.onGuildMessageReceived(event)

    }
}