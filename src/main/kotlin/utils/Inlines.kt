package utils

import database.Database
import java.sql.Connection

/**
 *
 */
inline fun query(block: (connection: Connection) -> Unit, close: Boolean = true, commit: Boolean = true) : Connection {
    val db = Database.get()
    block(db)
    if (commit) db.commit()
    if (close) db.close()
    return db
}