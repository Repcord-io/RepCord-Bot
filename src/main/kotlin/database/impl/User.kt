package database.impl

import utils.query
import java.sql.PreparedStatement

object User {
    fun getTitle(id: String): String {
        query({
            val st: PreparedStatement = it.prepareStatement("SELECT title FROM users WHERE provider_id = ?")
            st.setString(1, id)
            val rs = st.executeQuery()
            it.commit()

            if (rs.first()) {
                return rs.getString("title") ?: return ""
            }
        })
        return ""
    }

    fun getDescription(id: String): String {
        query({
            val st: PreparedStatement = it.prepareStatement("SELECT bio FROM users WHERE provider_id = ?")
            st.setString(1, id)
            val rs = st.executeQuery()
            it.commit()

            if (rs.first()) {
                return rs.getString("bio") ?: return ""
            }
        })
        return ""
    }
}