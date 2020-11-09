package command.impl

import database.impl.Leaderboard
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import utils.Helper

class Leaderboard : ListenerAdapter() {

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        val embed = Helper.createEmbed("Leaderboard")
        embed.setDescription("Displaying the top ten repped users across all servers using RepCord!")
        val results = Leaderboard.getResults();
        var index = 1
        for (result in results) {
            embed.addField("(${index++} ${result.username})", result.rep.toString(), false)
        }
        Helper.queueEmbed(event, embed)
    }
}