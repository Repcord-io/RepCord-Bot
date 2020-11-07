package command.impl

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import utils.Helper

class Vote : ListenerAdapter() {
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        val embed = utils.Helper.createEmbed("Vote")
        embed.addField(
            "Perks",
            "By voting for us you gain an a rep power boost of +2 for 12 hours!\nThough remember to vote again once the 12 hours have passed to retain your boost.",
            false
        )
        embed.addField("Discord Bot List:", "https://top.gg/bot/621182362008551444/vote", false)
        Helper.queueEmbed(event, embed)
    }
}