package utils

import net.dv8tion.jda.api.EmbedBuilder

object helper {
    fun createEmbed(title: String) : EmbedBuilder {
        val embed = EmbedBuilder()
        embed.setTitle(title)
        embed.setFooter("RepCord - https://repcord.io")
        return embed
    }
}