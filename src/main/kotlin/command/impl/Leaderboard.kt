package command.impl

import database.impl.Leaderboard
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import utils.Helper
import java.lang.String

class Leaderboard : ListenerAdapter() {

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        val embed = Helper.createEmbed("Leaderboard")
        embed.setDescription("Displaying the top ten repped users across all servers using RepCord!")
        val results = Leaderboard.get();
        var index = 1
        for (result in results) {
            embed.addField("(" + index++ + ") " + result.username, String.valueOf(result.rep), false)
        }
        Helper.queueEmbed(event, embed)
    }
}