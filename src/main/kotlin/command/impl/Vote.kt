package command.impl

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import utils.Helper

class Vote : ListenerAdapter() {
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        var activeVote = "No";
        if(database.impl.Vote.active(event.author.id)) activeVote = "Yes"
        val lastVoted : String = database.impl.Vote.lastVoted(event.author.id);

        val embed = Helper.createEmbed("Vote")
        embed.setDescription("By voting for us you gain an a rep power boost of +2 for 12 hours! Though remember to vote again once the 12 hours have passed to retain your boost.")
        embed.addField("Discord Bot List:", "https://top.gg/bot/621182362008551444/vote", false)
        embed.addField("Active", activeVote, true)
        embed.addField("Last Vote", lastVoted, true)
        Helper.queueEmbed(event, embed)
    }
}