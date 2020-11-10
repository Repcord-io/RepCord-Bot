package database.impl

import command.Ranks
import database.Database
import model.Reputation
import utils.query
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import kotlin.math.abs
import kotlin.math.roundToInt


/*
 * @project RepCord-Bot
 * @author Patrity - https://github.com/Patrity
 * Created on - 11/7/2020
 */
object Reputation {

    fun rep(user: String, rep: Int, comment: String, giver: String, guild: String) {
        query({ con ->
            val st = con.prepareStatement("INSERT INTO `user_reputation` (date_given, user, rep, comment, giver, guild) VALUES(?,?,?,?,?,?)")
            st.setTimestamp(1, Timestamp(System.currentTimeMillis()))
            st.setString(2, user)
            st.setInt(3, rep)
            st.setString(4, comment)
            st.setString(5, giver)
            st.setString(6, guild)
            st.execute()
        }, commit = true)
    }

    fun checkIfRepped(user: String, target: String): Boolean {
        query({
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
        })
        return false
    }

    fun getRep(user: String, target: String): Reputation? {
        query({
            val st = it.prepareStatement("SELECT id, date_given, user, rep, comment, giver FROM user_reputation WHERE user = ? && giver = ?")
            st.setString(1, target)
            st.setString(2, user)
            val rs = st.executeQuery()
            if (rs.first()) {
                return Reputation(
                        rs.getInt("id"),
                        rs.getTimestamp("date_given"),
                        rs.getString("user"),
                        rs.getInt("rep"),
                        rs.getString("comment"),
                        rs.getString("giver")
                )
            }
        })
        return null
    }

    fun modifyRep(user: String, target: String, positive: Boolean, comment: String) {
        val reputation: Reputation? = getRep(user, target)
        reputation?.let {
            val newPoints: Int = if (positive) abs(reputation.rep) else -abs(reputation.rep)
            query({
                val st = it.prepareStatement("UPDATE user_reputation SET rep = ? , comment = ? WHERE user = ? && giver = ?")
                st.setInt(1, newPoints)
                st.setString(2, comment)
                st.setString(3, target)
                st.setString(4, user)
                st.executeUpdate()
            }, commit = true)
        }
    }

    fun repPower(id: String): Int {
        // TODO: proper vote/donator power getters
        val power = 5
        return power
    }

    fun getRank(id: String): Int {
        query({
            val st = it.prepareStatement("SELECT SUM(rep), user FROM user_reputation WHERE user = ? GROUP BY user ORDER BY SUM(rep) DESC")
            st.setString(1, id)
            val rs = st.executeQuery()
            it.commit()
            if (rs.first())
                return rs.getInt(1)
        })
        return 0
    }

    fun getScore(id: String): Int {
        query({
            val st = it.prepareStatement("SELECT SUM(rep) AS score FROM user_reputation WHERE user= ?")
            st.setString(1, id)
            val rs = st.executeQuery()
            it.commit()
            if (rs.first())
                return rs.getInt("score")
        })
        return 0
    }

    fun getGuildScore(id: String, guild: String): Int {
        query({
            val st = it.prepareStatement("SELECT SUM(rep) AS score FROM user_reputation WHERE user= ? AND guild = ?")
            st.setString(1, id)
            st.setString(2, guild)
            val rs = st.executeQuery()
            it.commit()
            if (rs.first())
                return rs.getInt("score")
        })
        return 0
    }

    fun getTotalReceivedReputations(id: String): Int {
        query({
            val st = it.prepareStatement("SELECT COUNT(rep) AS total FROM user_reputation WHERE user= ?")
            st.setString(1, id)
            val rs: ResultSet = st.executeQuery()
            it.commit()
            if (rs.first())
                return rs.getInt("total")
        })
        return 0
    }

    fun getTotalPositiveReputationsReceived(id: String): Int {
        query({
            val st = it.prepareStatement("SELECT COUNT(rep) AS rep FROM user_reputation WHERE user= ? AND rep>0")
            st.setString(1, id)
            val rs = st.executeQuery()
            it.commit()
            if (rs.first())
                return rs.getInt("rep")
        })
        return 0
    }

    fun getTotalNegativeReputationsReceived(id: String): Int {
        query({
            val st = it.prepareStatement("SELECT COUNT(rep) AS rep FROM user_reputation WHERE user= ? AND rep<0")
            st.setString(1, id)
            val rs = st.executeQuery()
            it.commit()
            if (rs.first())
                return rs.getInt("rep")
        })
        return 0
    }

    // Total given reps a user has given another.
    fun getTotalGivenReps(id: String): Int {
        query({
            val st = it.prepareStatement("SELECT COUNT(rep) AS total FROM user_reputation WHERE giver = ?")
            st.setString(1, id)
            val rs = st.executeQuery()
            it.commit()
            if (rs.first())
                return rs.getInt("total")
        })
        return 0
    }

    fun getTotalReputations(): Int {
        query({
            val st = it.prepareStatement("SELECT COUNT(*) as total FROM user_reputation;")
            val rs = st.executeQuery()
            it.commit()
            if (rs.first())
                return rs.getInt("total")
        })
        return 0
    }

    fun getReputationCooldown(id: String): Int {
        query({
            val st: PreparedStatement = it.prepareStatement("SELECT id, UNIX_TIMESTAMP(date_given) AS date from user_reputation where giver= ? ORDER BY UNIX_TIMESTAMP(date_given) DESC LIMIT 1")
            st.setString(1, id)
            val rs = st.executeQuery()
            it.commit()
            if (rs.first()) {
                val cooldown: Long?
                val lastRepTime : Long = rs.getInt("date").toLong()
                val systemTime: Long = System.currentTimeMillis() / 1000L
                val isDonator: Boolean = Donator.user(id)
                val remaining: Long = systemTime - lastRepTime

                // Check if user is a donor
                if(isDonator) {
                    cooldown = Ranks.DONATOR.cooldown - remaining
                    if(remaining > Ranks.DONATOR.cooldown) {
                        return 0;
                    }
                    return (cooldown / 60).toFloat().roundToInt()
                }

                cooldown = Ranks.DEFAULT.cooldown - remaining
                if(remaining > Ranks.DEFAULT.cooldown) {
                    return 0;
                }
                return (cooldown / 60).toFloat().roundToInt()
            }
        })
        return 0
    }
}