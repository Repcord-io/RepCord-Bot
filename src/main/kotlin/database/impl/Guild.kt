package database.impl

import Bot
import net.dv8tion.jda.api.entities.Guild
import utils.query
import java.sql.SQLException

object Guild {
    @Throws(SQLException::class)
    fun addGuild(guild: Guild) {
        query({
            val st = it.prepareStatement("INSERT INTO guilds (`id`, `name`, `owner`, `members`, `prefix`) VALUES(?,?,?,?,?) ON DUPLICATE KEY UPDATE name = ?, owner = ?, members = ?;")
            st.setString(1, guild.id)
            st.setString(2, guild.name)
            st.setString(3, guild.ownerId)
            st.setInt(4, guild.memberCount)
            st.setString(5, Bot.config.default_prefix)
            st.setString(6, guild.name)
            st.setString(7, guild.ownerId)
            st.setInt(8, guild.memberCount)
            st.executeUpdate()
        }, commit = true)
    }
}