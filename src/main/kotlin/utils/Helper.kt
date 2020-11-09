package utils

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.awt.Color

object Helper {
    fun createEmbed(title: String): EmbedBuilder {
        val embed = EmbedBuilder()
        embed.setTitle(title)
        embed.setFooter("RepCord - https://repcord.io")
        embed.setColor(Color.decode("#5262a6"))
        return embed
    }

    fun errorResponse(event: GuildMessageReceivedEvent, reason: String) {
        val eb = createEmbed("Error")
        eb.setColor(Color.RED)
        eb.setDescription(reason)
        queueEmbed(event, eb)
    }

    fun queueEmbed(event: GuildMessageReceivedEvent, embedBuilder: EmbedBuilder) {
        event.channel.sendMessage(embedBuilder.build()).queue();
    }

    fun embed(
        event: GuildMessageReceivedEvent,
        title: String = "",
        send: Boolean = false,
        description: String = ""
    ): EmbedBuilder {
        val embed = EmbedBuilder()
        embed.setTitle(title)
        if (description.isNotBlank())
            embed.setDescription(description)
        embed.setFooter("RepCord - https://repcord.io")
        embed.setColor(Color.decode("#5262a6"))
        val messAction = event.channel.sendMessage(embed.build())
        if (send)
            messAction.queue()
        return embed
    }
}