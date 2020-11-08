package database.impl

import database.Database
import java.sql.PreparedStatement

object Leaderboard {
    fun get(): MutableList<LeaderboardObject> {
        val con = Database.get()
        val st: PreparedStatement = con.prepareStatement("SELECT SUM(rep) AS rep, user_cache.username FROM user_reputation, user_cache WHERE USER = user_cache.userid GROUP by userid ORDER BY SUM(rep) DESC LIMIT 10")
        val rs = st.executeQuery()
        con.commit()
        val leaderboardResults = mutableListOf<LeaderboardObject>();
        while (rs.next())
        {
            leaderboardResults.add(LeaderboardObject(rs.getInt("rep"), rs.getString("username")))
        }
        con.close();
        return leaderboardResults;
    }
}

data class LeaderboardObject(
        val rep : Int,
        val username: String) {
}