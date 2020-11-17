package org.repcord.database

import utils.query
import java.sql.PreparedStatement
import java.sql.Timestamp

object Reputation {
    fun getUsersRep(id: String?): MutableList<ReputationModal> {
        // Get users rep verbose :ReputationModal
        val reputations = mutableListOf<ReputationModal>()
        query({
            val st: PreparedStatement = it.prepareStatement("SELECT date_given, user_cache.username, user_cache.avatar, rep, comment FROM user_reputation INNER JOIN user_cache ON user_reputation.giver = user_cache.userid WHERE user_reputation.user = ?")
            st.setString(1, id)
            val rs = st.executeQuery()
            it.commit()
            while (rs.next()) {
                reputations.add(
                    ReputationModal(
                        rs.getTimestamp(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getString(5)
                    )
                )
            }

            return reputations
        })
        return reputations
    }
}

data class ReputationModal(
    val created: Timestamp,
    val user: String, // User who gave the reputation.
    val avatar: String?,
    val score: Int, // Score/power added to rep.
    val comment: String
)