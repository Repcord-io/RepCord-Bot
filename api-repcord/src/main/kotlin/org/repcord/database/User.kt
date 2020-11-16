package org.repcord.database
import utils.query
import java.sql.PreparedStatement

object User {
    fun doesExist(id: String?): UserModal? {
        query({
            val st: PreparedStatement = it.prepareStatement("SELECT username, avatar, id, userid FROM user_cache WHERE userid = ?")
            st.setString(1, id)
            val rs = st.executeQuery()
            it.commit()
            if (rs.next()) {
               return UserModal(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4))
            }
        })
        // User doesn't exist if null
        return null;
    }
}

data class UserModal(
    val username: String,
    val avatar: String,
    val id: Int,
    val userid: String,
    var positiveTotal: Int? = 0,
    var negativeTotal: Int? = 0,
    var totalReps: Int? = 0,
    var donator: Boolean? = false,
    var description: String? = ""
    ){
}