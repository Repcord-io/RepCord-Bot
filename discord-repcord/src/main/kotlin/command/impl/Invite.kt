package command.impl

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import utils.Helper

class Invite : ListenerAdapter() {
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        val embed = Helper.createEmbed("Invite")
        embed.setDescription("https://repcord.io/invite")
        Helper.queueEmbed(event, embed)
    }
}