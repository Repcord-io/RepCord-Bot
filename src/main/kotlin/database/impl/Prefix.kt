package database.impl

import Bot
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.sql.PreparedStatement
import java.sql.SQLException


/*
 * @project RepCord-Bot
 * @author Patrity - https://github.com/Patrity
 * Created on - 11/6/2020
 */
object Prefix {

    @Throws(SQLException::class)
    fun getPrefix(guildID: String?): String? {
        var prefix = ""
        Bot.db.datasource.use { ds ->
            val con = ds?.connection!!
            con.setAutoCommit(false)
            val st: PreparedStatement = con.prepareStatement("SELECT prefix FROM guilds WHERE id=?")
            st.setString(1, guildID)
            val rs = st.executeQuery()
            con.commit()
            if (rs.first()) {
                prefix = rs.getString(1)
            }
        }
        if (prefix.isEmpty()) {
            prefix = Bot.config.default_prefix
            //TODO: Set guild's prefix to default
        }
        return prefix
    }


    @Throws(SQLException::class)
    fun setPrefix(e: GuildMessageReceivedEvent, prefix: String) {
        Bot.db.datasource.use{ ds ->
            val con = ds?.connection!!
            con.autoCommit = false
            val st: PreparedStatement = con.prepareStatement("UPDATE guilds SET prefix=? WHERE id=?")
            st.setString(1, prefix)
            st.setString(2, e.guild.id)
            st.executeUpdate()
            con.commit()
            val eb = EmbedBuilder()
                .setTitle("Prefix Updated!")
                .setDescription("New prefix: `$prefix`")
            e.channel.sendMessage(eb.build()).queue()
        }
    }
}