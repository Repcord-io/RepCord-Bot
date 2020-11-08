import listeners.MessageReceived
import com.google.gson.Gson
import database.Database
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.utils.MemberCachePolicy
import java.io.File
import java.io.InputStream

/*
 * @project RepCord-Bot
 * @author Patrity - https://github.com/Patrity
 * Created on - 11/6/2020
 */
object Bot {

    val inputStream: InputStream = File("data/bot_config.json").inputStream()
    val inputString = inputStream.bufferedReader().use { it.readText() }
    val gson = Gson()
    val config = gson.fromJson(inputString, Config::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        run {
            Database.connect()
        }.also {
            val jda = DefaultShardManagerBuilder.createLight(config.token, GatewayIntent.GUILD_MESSAGES)
                .setMemberCachePolicy(MemberCachePolicy.NONE)
                .addEventListeners(MessageReceived())
                .build()
        }


    }

    /*
     * Object to read bot_config.json
     */
    data class Config(
        val token: String = "",
        val default_prefix: String = "",
        val sql_username: String = "",
        val sql_password: String = "",
        val sql_host: String = ""
    )

}