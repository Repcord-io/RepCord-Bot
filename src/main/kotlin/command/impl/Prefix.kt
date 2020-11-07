package command.impl

import database.impl.Prefix
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import utils.Helper


/*
 * @project RepCord-Bot
 * @author Patrity - https://github.com/Patrity
 * Created on - 11/6/2020
 */
class Prefix : ListenerAdapter(){

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {

        var currentPrefix = Prefix.getPrefix(event.guild.id)
        var command = event.message.contentRaw.toLowerCase().split(" ")

        if (!event.message.member?.hasPermission(Permission.ADMINISTRATOR)!! || command.size != 2) {
            val eb = Helper.createEmbed("Server Prefix")
            eb.setDescription("Current prefix: $currentPrefix")
            eb.addField("Only Administrators can change the prefix", "Format: `${currentPrefix}prefix [New_Prefix]`", false)
            event.channel.sendMessage(eb.build()).queue()
            return
        }
        Prefix.setPrefix(event, command[1])
    }
}