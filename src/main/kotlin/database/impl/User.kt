package database.impl

import net.dv8tion.jda.api.entities.User
import utils.query
import java.sql.PreparedStatement
import java.util.*

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

    fun cacheUser(user: User) {
        query({
            val st: PreparedStatement = it.prepareStatement("INSERT INTO user_cache (`userid`, `username`, `avatar`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE username = ?, avatar = ?;")
            st.setString(1, user.id)
            st.setString(2, user.asTag)
            st.setString(3, user.avatarUrl)
            st.setString(4, user.asTag)
            st.setString(5, if (user.avatarUrl != null) user.avatarUrl else "null")
            st.executeUpdate()
            it.commit()
        })
    }
}