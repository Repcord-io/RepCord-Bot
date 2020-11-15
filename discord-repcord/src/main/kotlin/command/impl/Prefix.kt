package command.impl

import database.impl.Prefix
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import utils.Helper


/*
 * @project RepCord-Bot
 * @author Patrity - https://github.com/Patrity
 * Created on - 11/6/2020
 */
class Prefix : ListenerAdapter() {

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        val currentPrefix = Prefix.getPrefix(event)
        val command = event.message.contentRaw.toLowerCase().split(" ")

        if (!event.message.member?.hasPermission(Permission.ADMINISTRATOR)!! || command.size != 2) {
            val eb = Helper.createEmbed("Server Prefix")
            eb.setDescription("Current prefix: $currentPrefix")
            eb.addField(
                "Only Administrators can change the prefix",
                "Format: `${currentPrefix}prefix [New_Prefix]`",
                false
            )
            Helper.queueEmbed(event, eb)
            return
        }
        Prefix.setPrefix(event.guild.id, command[1])
        val embed = Helper.createEmbed("Prefix Updated!")
            .setDescription("New prefix: `${command[1]}`")
        event.channel.sendMessage(embed.build()).queue()
    }
}