package command.impl

import command.Commands
import database.impl.Guild
import database.impl.Prefix
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import utils.Helper


/*
 * @project RepCord-Bot
 * @author Patrity - https://github.com/Patrity
 * Created on - 11/6/2020
 */
class Help : ListenerAdapter() {

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        val embed = Helper.createEmbed("Help")
        embed.setDescription("RepCord is a cross-server reputation bot.\nKnow who your dealing with, before the first hello!")
        val prefix = Prefix.getPrefix(event)
        for(command in Commands.values()) {
            embed.addField("${prefix}${command.toLowerName()}", command.description, false)
        }
        Helper.queueEmbed(event, embed)
    }
}