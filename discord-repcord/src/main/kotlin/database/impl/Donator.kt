package database.impl

import utils.query
import java.sql.PreparedStatement

object Donator {
    // Checks if user is a overall donator by checking if they have a active sub
    fun isOverall(id: String): Boolean {
        if (user(id)) return true
        if (userIsAGuildDonator(id)) return true
        return false
    }

    fun user(id: String): Boolean {
        query({
            val st: PreparedStatement =
                it.prepareStatement("SELECT id FROM subscriptions WHERE content_id = ? AND type = 'user' AND expires > CURRENT_TIMESTAMP;")
            st.setString(1, id)
            val rs = st.executeQuery()
            it.commit()
            return rs.next()
        })
        return false;
    }

    fun guild(id: String): Boolean {
        query({
            val st: PreparedStatement =
                it.prepareStatement("SELECT id FROM subscriptions WHERE content_id = ? AND type = 'guild' AND expires > CURRENT_TIMESTAMP;")
            st.setString(1, id)
            val rs = st.executeQuery()
            it.commit()
            return rs.next()
        })
        return false;
    }

    fun userIsAGuildDonator(id: String): Boolean {
        query({
            val st: PreparedStatement =
                it.prepareStatement("SELECT id FROM subscriptions WHERE user = (SELECT ID FROM users WHERE provider_id = ?) AND expires > CURRENT_TIMESTAMP;")
            st.setString(1, id)
            val rs = st.executeQuery()
            it.commit()
            return rs.next()
        })
        return false;
    }
}