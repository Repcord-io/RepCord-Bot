package database.impl

import Bot
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import utils.Helper
import java.sql.PreparedStatement
import java.sql.SQLException


/*
 * @project RepCord-Bot
 * @author Patrity - https://github.com/Patrity
 * Created on - 11/6/2020
 */
object Prefix {

    @Throws(SQLException::class)
    fun getPrefix(guildID: String): String? {
        var prefix = ""

        val con = Bot.db.datasource?.connection!!
        val st: PreparedStatement = con.prepareStatement("SELECT prefix FROM guilds WHERE id=?")
        st.setString(1, guildID)
        val rs = st.executeQuery()
        con.commit()
        if (rs.first()) {
            prefix = rs.getString(1)
        }

        if (prefix.isEmpty()) {
            setPrefix(guildID)
        }

        con.close()

        return prefix
    }


    @Throws(SQLException::class)
    fun setPrefix(guildID: String, prefix: String = Bot.config.default_prefix) {
        val con = Bot.db.get()
        val st: PreparedStatement = con.prepareStatement("UPDATE guilds SET prefix=? WHERE id=?")
        st.setString(1, prefix)
        st.setString(2, guildID)
        st.executeUpdate()
        con.commit()
    }
}