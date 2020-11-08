package database.impl

import database.Database
import java.sql.PreparedStatement

object Info {
    fun totalReps() : Int {
        var total = 0;
        val con = Database.get()
        val st: PreparedStatement = con.prepareStatement("SELECT COUNT(*) as total FROM user_reputation;")
        val rs = st.executeQuery()
        con.commit()

        if (rs.first()) {
            total = rs.getInt("total")
        }
        con.close()
        return total;
    }

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
}