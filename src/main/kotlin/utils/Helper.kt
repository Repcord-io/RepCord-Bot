package utils

import net.dv8tion.jda.api.EmbedBuilder
import java.awt.Color

object Helper {
    fun createEmbed(title: String) : EmbedBuilder {
        val embed = EmbedBuilder()
        embed.setTitle(title)
        embed.setFooter("RepCord - https://repcord.io")
        embed.setColor(Color.decode("#5262a6"))
        return embed
    }
}