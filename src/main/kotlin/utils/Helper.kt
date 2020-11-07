package utils

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.awt.Color

object Helper {
    fun createEmbed(title: String) : EmbedBuilder {
        val embed = EmbedBuilder()
        embed.setTitle(title)
        embed.setFooter("RepCord - https://repcord.io")
        embed.setColor(Color.decode("#5262a6"))
        return embed
    }

    fun errorResponse(event: GuildMessageReceivedEvent, reason: String) {
        var eb = createEmbed("Error")
        eb.setColor(Color.RED)
        eb.setDescription(reason)
        queueEmbed(event, eb)
    }

    fun queueEmbed(event: GuildMessageReceivedEvent, embedBuilder: EmbedBuilder) {
        event.channel.sendMessage(embedBuilder.build()).queue();
    }
}