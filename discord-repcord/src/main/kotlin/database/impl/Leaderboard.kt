package database.impl

import utils.query
import java.sql.PreparedStatement

object Leaderboard {

    fun getResults(): MutableList<LeaderboardUser> {
        val leaderboardUsers = mutableListOf<LeaderboardUser>()
        query({ connection ->
            val st: PreparedStatement = connection.prepareStatement("SELECT SUM(rep) AS rep, user_cache.username FROM user_reputation, user_cache WHERE USER = user_cache.userid GROUP by userid ORDER BY SUM(rep) DESC LIMIT 10")
            val rs = st.executeQuery()
            connection.commit()
            while (rs.next()) {
                leaderboardUsers.add(LeaderboardUser(rs.getInt("rep"), rs.getString("username")))
            }
        })
        return leaderboardUsers
    }

    fun getGuildResults(id: String): MutableList<LeaderboardUser> {
        val leaderboardUsers = mutableListOf<LeaderboardUser>()
        query({
            val st: PreparedStatement = it.prepareStatement("SELECT SUM(rep) AS rep, user_cache.username FROM user_reputation, user_cache WHERE USER = user_cache.userid AND guild = ? GROUP by userid ORDER BY SUM(rep) DESC LIMIT 10")
            st.setString(1, id)
            val rs = st.executeQuery()
            it.commit()
            while (rs.next()) {
                leaderboardUsers.add(LeaderboardUser(rs.getInt("rep"), rs.getString("username")))
            }
        })
        return leaderboardUsers
    }



}

data class LeaderboardUser(val rep: Int, val username: String)