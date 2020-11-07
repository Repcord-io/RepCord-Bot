package command.impl

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class Vote : ListenerAdapter() {
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        val embed = utils.helper.createEmbed("Vote")
        embed.addField(
            "Perks",
            "By voting for us you gain an a rep power boost of +2 for 12 hours!\nThough remember to vote again once the 12 hours have passed to retain your boost.",
            false
        )
        embed.addField("Discord Bot List:", "https://top.gg/bot/621182362008551444/vote", false)
        event.channel.sendMessage(embed.build()).queue()
    }
}