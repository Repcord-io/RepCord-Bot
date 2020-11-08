package listeners

import Bot
import database.impl.Info
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.text.DecimalFormat
import java.util.*


class ReadyReceived : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        val df = DecimalFormat("#,###")
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                when ((1..3).random()) {
                    1 -> {
                        applyActivity(event, "${Bot.config.default_prefix}help | ${event.jda.shardManager?.guilds?.size} servers")
                    }
                    2 -> {
                        applyActivity(event, "${Bot.config.default_prefix}help | ${df.format(Info.totalReps())} reps")
                    }
                    3 -> {
                        applyActivity(event, "${Bot.config.default_prefix}help | ${df.format(Info.totalActiveVotes())} active votes")
                    }
                }
            }
        }, 0, 30000)

    }

    private fun applyActivity(event: ReadyEvent, message: String) {
        event.jda.shardManager?.setActivity(Activity.watching(message))
    }
}