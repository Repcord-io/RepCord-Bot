package model

import java.sql.Timestamp


/*
 * @project RepCord-Bot
 * @author Patrity - https://github.com/Patrity
 * Created on - 11/7/2020
 */

data class Reputation(var id: Int, var date: Timestamp, var user: String, var rep: Int, var comment: String, var giver: String)