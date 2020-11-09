package database.impl

import Bot
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import utils.query
import java.sql.PreparedStatement
import java.sql.SQLException


/*
 * @project RepCord-Bot
 * @author Patrity - https://github.com/Patrity
 * Created on - 11/6/2020
 */
object Prefix {

    @Throws(SQLException::class)
    fun getPrefix(event: GuildMessageReceivedEvent): String? {
        var prefix = ""
        query({ connection ->
            val st: PreparedStatement = connection.prepareStatement("SELECT prefix FROM guilds WHERE id=?")
            st.setString(1, event.guild.id)
            val rs = st.executeQuery()
            connection.commit()
            if (rs.first()) {
                prefix = rs.getString(1)
            }
            if (prefix.isEmpty()) {
                // This condition is normally only met if the bot got added to a guild while it was offline/disconnected from the websocket.
                Guild.addGuild(event.guild)
            }
        })
        return prefix
    }


    @Throws(SQLException::class)
    fun setPrefix(guildID: String, prefix: String = Bot.config.default_prefix) {
        query({
            val st: PreparedStatement = it.prepareStatement("UPDATE guilds SET prefix=? WHERE id=?")
            st.setString(1, prefix)
            st.setString(2, guildID)
            st.executeUpdate()
        }, commit = true)
    }
}