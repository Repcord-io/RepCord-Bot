package database.impl

import database.Database
import utils.query
import java.sql.PreparedStatement

object Info {
    fun totalActiveVotes() : Int {
        var total = 0
        val con = Database.get()
        val st: PreparedStatement = con.prepareStatement("SELECT COUNT(id) as active FROM votes WHERE updated_at > Date_sub(now(), INTERVAL 12 hour);")
        val rs = st.executeQuery()
        con.commit()

        if (rs.first()) {
            total = rs.getInt("active")
        }
        con.close()
        return total
    }

    fun totalRegisteredUsers() : Int {
        query({
            val st: PreparedStatement = it.prepareStatement("SELECT COUNT(*) as total FROM users;")
            val rs = st.executeQuery()
            it.commit()
            if (rs.first()) {
                return rs.getInt("total")
            }
        }, commit = false)
        return 0
    }
}