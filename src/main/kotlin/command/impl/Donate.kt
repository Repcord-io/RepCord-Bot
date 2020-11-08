package command.impl

import database.impl.Prefix
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import utils.Helper

class Donate : ListenerAdapter() {
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        val embed = Helper.createEmbed("Donate")
        embed.setDescription("https://repcord.io/subscriptions")
        Helper.queueEmbed(event, embed)
    }
}
