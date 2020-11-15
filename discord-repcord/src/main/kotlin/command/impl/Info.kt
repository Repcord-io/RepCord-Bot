package command.impl

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import utils.Helper
import java.text.DecimalFormat

class Info : ListenerAdapter() {
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        val df = DecimalFormat("#,###")

        val totalServers = event.jda.shardManager?.guilds?.size
        val totalReps = database.impl.Reputation.getTotalReputations()

        val runtime = Runtime.getRuntime()
        val memory = runtime.totalMemory() - runtime.freeMemory()
        val memoryUsed = memory / 1024 / 1024

        val embed = Helper.createEmbed("RepCord Information")
        embed.setDescription("https://repcord.io/invite")
        embed.addField("Creation date", "September 11, 2019", false)
        embed.addField("Servers", df.format(totalServers), true)
        embed.addField("Total Reps", df.format(totalReps), true)
        embed.addField("Current ping", "${event.jda.gatewayPing} ms", true)
        embed.addField("Registered Users", df.format(database.impl.Info.totalRegisteredUsers()), true)
        embed.addField("Language", "Kotlin", true)
        embed.addField("Library", "JDA", true)
        embed.addField("Ram usage", "$memoryUsed mb", true)
        embed.setThumbnail("https://repcord.io/logo.png")
        embed.addField(
            "Useful links",
            "[Register at our website!](https://repcord.io)\n[Vote for us!](https://discordbots.org/bot/621182362008551444/vote)",
            false
        )
        Helper.queueEmbed(event, embed)
    }
}