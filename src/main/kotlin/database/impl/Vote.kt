package database.impl

import Bot
import java.sql.PreparedStatement
import java.sql.ResultSet

object Vote {
    fun active(id: String) : Boolean {
        var isActive = false
        val con = Bot.db.get()
        con.prepareStatement("SELECT UNIX_TIMESTAMP(updated_at) AS updated FROM votes WHERE user = ?")
        val st: PreparedStatement = con.prepareStatement("SELECT UNIX_TIMESTAMP(updated_at) AS updated FROM votes WHERE user = ?")
        st.setString(1, id)
        val rs: ResultSet = st.executeQuery()
        con.commit()
        if (rs.first()) {
            val voteTime: Int  = rs.getInt(1)
            val systemTime: Long = System.currentTimeMillis() / 1000L
            isActive = (systemTime - voteTime < 43200)
        }
        con.close()
        return isActive
    }

    fun lastVoted(id: String) : String {
        var timestamp = "Never voted."
        val con = Bot.db.get();
        val st: PreparedStatement = con.prepareStatement("SELECT updated_at AS updated FROM votes WHERE user = ?")
        st.setString(1, id)
        val rs = st.executeQuery()
        con.commit()
        if (rs.first()) {
            timestamp = rs.getString(1)
        }
        con.close()
        return timestamp
    }
}