package database.impl

import utils.query
import java.sql.PreparedStatement

object Donator {
    fun user(id: String): Boolean {
        query({
            val st: PreparedStatement = it.prepareStatement("SELECT id FROM subscriptions WHERE content_id = ? AND type = 'user' AND expires > CURRENT_TIMESTAMP;")
            st.setString(1, id)
            val rs = st.executeQuery()
            it.commit()
            return rs.first()
        })
        return false;
    }

    fun guild(id: String) : Boolean {
        query({
            val st: PreparedStatement = it.prepareStatement("SELECT id FROM subscriptions WHERE content_id = ? AND type = 'guild' AND expires > CURRENT_TIMESTAMP;")
            st.setString(1, id)
            val rs = st.executeQuery()
            it.commit()
            return rs.first()
        })
        return false;
    }
}