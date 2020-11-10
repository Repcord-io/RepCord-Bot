package utils

import database.Database
import java.sql.Connection

inline fun query(block: (connection: Connection) -> Unit, close: Boolean = true, commit: Boolean = false) {
    val connection = Database.get()
    try {
        return block(connection)
    } finally {
        if (commit) connection.commit()
        if (close) connection.close()
    }
}