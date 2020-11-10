package command.impl

import database.impl.Leaderboard
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import utils.Helper

class Scoreboard : ListenerAdapter() {

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        val embed = Helper.createEmbed("Scoreboard - ${event.guild.name}")
        embed.setDescription("Displaying the top ten repped users in ${event.guild.name}!")
        val results = Leaderboard.getGuildResults(event.guild.id);
        var index = 1
        for (result in results) {
            embed.addField("(${index++}) ${result.username}", result.rep.toString(), false)
        }
        Helper.queueEmbed(event, embed)
    }
}