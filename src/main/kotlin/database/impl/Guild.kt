package database.impl

import Bot
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import utils.Helper
import utils.query
import java.sql.PreparedStatement
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

    fun autoKickOrBan(event: GuildMessageReceivedEvent, user: User) {
        if (!Donator.guild(event.guild.id)) return
        val guildScore: Int = Reputation.getGuildScore(user.id, event.guild.id)

        // TODO: Make this into one database call for both values.
        val autoBanScore: Int = getAutoBanValue(event.guild.id)
        val autoKickScore: Int = getAutoKickValue(event.guild.id)

        if (autoBanScore != 0 && guildScore < autoBanScore) {
            if (!event.guild.selfMember.hasPermission(Permission.BAN_MEMBERS)) {
                Helper.errorResponse(event, "Missing ban permissions.")
                return
            }
            event.guild.ban(user.id, 0, "Reached predefined ban score.").queue(
                    { Helper.successResponse(event, "Banning user, due to auto ban threshold.") },
                    { Helper.errorResponse(event, "Don't have permission to ban this user.") }
            )
            return
        }

        if (autoKickScore != 0 && guildScore < autoKickScore) {
            if (!event.guild.selfMember.hasPermission(Permission.KICK_MEMBERS)) {
                Helper.errorResponse(event, "Missing kick permissions.")
                return
            }
            event.guild.kick(user.id, "Reached predefined kick score.").queue(
                    { Helper.successResponse(event, "Kicking user, due to auto kick threshold.") },
                    { Helper.errorResponse(event, "Don't have permission to kick this user.") }
            )
        }
    }

    private fun getAutoKickValue(id: String): Int {
        query({
            val st: PreparedStatement = it.prepareStatement("Select auto_kick FROM guilds WHERE id = ?")
            st.setString(1, id)
            val rs = st.executeQuery()
            it.commit()
            if(rs.first()) return rs.getInt(1)
        })
        return 0
    }

    private fun getAutoBanValue(id: String): Int {
        query({
            val st: PreparedStatement = it.prepareStatement("Select auto_ban FROM guilds WHERE id = ?")
            st.setString(1, id)
            val rs = st.executeQuery()
            it.commit()
            if(rs.first()) return rs.getInt(1)
        })
        return 0
    }
}