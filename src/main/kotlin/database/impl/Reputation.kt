package database.impl

import database.Database
import model.Reputation
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import kotlin.math.abs


/*
 * @project RepCord-Bot
 * @author Patrity - https://github.com/Patrity
 * Created on - 11/7/2020
 */
class Reputation {

    fun rep (user: String, rep: Int, comment: String, giver: String, guild: String) {

        val con = Database.get()
        val st = con.prepareStatement("INSERT INTO `user_reputation` (date_given, user, rep, comment, giver, guild) VALUES(?,?,?,?,?,?)")
        st.setTimestamp(1, Timestamp(System.currentTimeMillis()))
        st.setString(2, user)
        st.setInt(3, rep)
        st.setString(4, comment)
        st.setString(5, giver)
        st.setString(6, guild)
        st.execute()
        con.commit()
    }

    fun checkIfRepped(user: String, target: String): Boolean {

        val con = Database.get()
        val st = con.prepareStatement("SELECT COUNT(date_given) FROM user_reputation where giver=? AND user=?")
        st.setString(1, user)
        st.setString(2, target)
        val rs = st.executeQuery()
        con.commit()
        while (rs.next()) {
            if (rs.getInt(1) != 0) {
                return true
            }
        }
        return false
    }

    fun getRep(user: String, target: String): Reputation? {
        var reputation: Reputation? = null
        val con = Database.get()
        val st = con.prepareStatement("SELECT id, date_given, user, rep, comment, giver FROM user_reputation WHERE user = ? && giver = ?")
        st.setString(1, target)
        st.setString(2, user)

        val rs = st.executeQuery()
        if (rs.first()) {
            reputation?.let {
                reputation.id = rs.getInt("id")
                reputation.comment = rs.getString("comment")
                reputation.date = rs.getTimestamp("date_given")
                reputation.user = rs.getString("user")
                reputation.giver = rs.getString("giver")
                reputation.rep = rs.getInt("rep")
            }
        }
        return reputation
    }

    fun modifyRep(user: String, target: String, positive: Boolean, comment: String) {
        var reputation: Reputation? = getRep(user, target)
        reputation?.let {
            val newPoints: Int = if (positive) abs(reputation.rep) else -abs(reputation.rep)
            val con = Database.get()
            val st = con.prepareStatement("UPDATE user_reputation SET rep = ? , comment = ? WHERE user = ? && giver = ?")
            st.setInt(1, newPoints)
            st.setString(2, comment)
            st.setString(3, target)
            st.setString(4, user)
            st.executeQuery()
            con.commit()
        }
    }

    fun repPower(id: String): Int {
        // TODO: proper vote/donator power getters
        var power = 5
        return power
    }

    fun getRank(id: String): Int {
        val con = Database.get()
        val st = con.prepareStatement("SELECT SUM(rep), user FROM user_reputation WHERE user = ? GROUP BY user ORDER BY SUM(rep) DESC")
        st.setString(1, id)
        val rs = st.executeQuery()
        con.commit()
        if (rs.first())
            return rs.getInt("score")
        return 0
    }

    fun getScore(id: String): Int {
        val con = Database.get()
        val st = con.prepareStatement("SELECT SUM(rep) AS score FROM user_reputation WHERE user= ?")
        st.setString(1, id)
        val rs = st.executeQuery()
        con.commit()
        if (rs.first())
            return rs.getInt("score")
        return 0
    }

    fun getGuildScore(id: String, guild: String): Int {
        val con = Database.get()
        val st = con.prepareStatement("SELECT SUM(rep) AS score FROM user_reputation WHERE user= ? AND guild = ?")
        st.setString(1, id)
        st.setString(2, guild)
        val rs = st.executeQuery()
        con.commit()
        if (rs.first())
            return rs.getInt("score")
        return 0
    }

    fun getTotalReceivedReputations(id: String): Int {
        val con = Database.get()
        val st = con.prepareStatement("SELECT COUNT(rep) AS total FROM user_reputation WHERE user= ?")
        st.setString(1, id)
        val rs: ResultSet = st.executeQuery()
        con.commit()
        if (rs.first())
            return rs.getInt("total")
        return 0
    }

    fun getTotalPositiveReputationsReceived(id: String): Int {
        val con = Database.get()
        val st = con.prepareStatement("SELECT COUNT(rep) AS rep FROM user_reputation WHERE user= ? AND rep>0")
        st.setString(1, id)
        val rs = st.executeQuery()
        con.commit()
        if (rs.first())
            return rs.getInt("rep")
        return 0
    }

    fun getTotalNegativeReputationsReceived(id: String): Int {
        val con = Database.get()
        val st = con.prepareStatement("SELECT COUNT(rep) AS rep FROM user_reputation WHERE user= ? AND rep<0")
        st.setString(1, id)
        val rs = st.executeQuery()
        con.commit()
        if (rs.first())
            return rs.getInt("rep")
        return 0
    }

    fun getTotalGivenReps(id: String): Int {
        val con = Database.get()
        val st = con.prepareStatement("SELECT COUNT(rep) AS total FROM user_reputation WHERE giver = ?")
        st.setString(1, id)
        val rs = st.executeQuery()
        con.commit()
        if (rs.first())
            return rs.getInt("total")
        return 0
    }

    fun getTotalReputations(id: String): Int {
        val con = Database.get()
        val st = con.prepareStatement("SELECT COUNT(*) as total FROM user_reputation;")
        st.setString(1, id)
        val rs = st.executeQuery()
        con.commit()
        if (rs.first())
            return rs.getInt("total")
        return 0
    }

    fun getReputationCooldown(id: String): Long {
        //TODO
        return 0
    }


}