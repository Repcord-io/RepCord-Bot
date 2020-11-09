package database.impl

import utils.query
import java.sql.PreparedStatement
import java.sql.ResultSet

object Vote {

    fun active(id: String): Boolean {
        var isActive = false
        query({
            it.prepareStatement("SELECT UNIX_TIMESTAMP(updated_at) AS updated FROM votes WHERE user = ?")
            val st: PreparedStatement =
                it.prepareStatement("SELECT UNIX_TIMESTAMP(updated_at) AS updated FROM votes WHERE user = ?")
            st.setString(1, id)
            val rs: ResultSet = st.executeQuery()
            it.commit()
            if (rs.first()) {
                val voteTime: Int = rs.getInt(1)
                val systemTime: Long = System.currentTimeMillis() / 1000L
                isActive = (systemTime - voteTime < 43200)
            }
        })
        return isActive
    }

    fun lastVoted(id: String): String {
        var timestamp = "Never voted."
        query({
            val st: PreparedStatement = it.prepareStatement("SELECT updated_at AS updated FROM votes WHERE user = ?")
            st.setString(1, id)
            val rs = st.executeQuery()
            it.commit()
            if (rs.first()) {
                timestamp = rs.getString(1)
            }
        })
        return timestamp
    }

}