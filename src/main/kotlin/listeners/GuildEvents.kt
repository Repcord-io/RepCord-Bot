package listeners

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import utils.Helper

class GuildEvents : ListenerAdapter() {
    override fun onGuildJoin(event: GuildJoinEvent) {
        val guild: Guild = event.guild

        database.impl.Guild.addGuild(guild)
        run {
            val embed = Helper.createEmbed(guild.name)
            embed.addField("Owner", guild.retrieveOwner().complete().user.asTag, false)
            embed.setThumbnail(guild.iconUrl)
            event.jda.getGuildById(Bot.config.repcord_guild)?.getTextChannelById(Bot.config.repcord_guild_new_servers)?.sendMessage(embed.build())?.queue()
        }
    }
}