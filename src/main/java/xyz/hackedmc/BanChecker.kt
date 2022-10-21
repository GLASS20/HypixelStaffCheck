package xyz.hackedmc

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import xyz.hackedmc.utils.ColorUtils.ANSI_CYAN
import xyz.hackedmc.utils.ColorUtils.ANSI_GREEN
import xyz.hackedmc.utils.ColorUtils.ANSI_WHITE
import xyz.hackedmc.utils.ColorUtils.ANSI_YELLOW
import xyz.hackedmc.utils.HttpUtils
import xyz.hackedmc.utils.MSTimer

class BanChecker {
    fun BanChecker(){
        val API_PUNISHMENT = "https://api.plancke.io/hypixel/v1/punishmentStats"

        var WATCHDOG_BAN_LAST_MIN = 0
        var LAST_TOTAL_STAFF = -1
        var STAFF_BAN_LAST_MIN = 0

        val checkTimer = MSTimer()

        println("${ANSI_CYAN}Hypixel BanCheck Time: ${System.currentTimeMillis()} By GLASS20${ANSI_WHITE}")
        while (true) {
            if (checkTimer.hasTimePassed(60000L)) {
                try {
                    val apiContent: String = HttpUtils.get(API_PUNISHMENT)
                    val jsonObject: JsonObject = JsonParser().parse(apiContent).asJsonObject
                    if (jsonObject.get("success").asBoolean && jsonObject.has("record")) {
                        val objectAPI: JsonObject = jsonObject.get("record").asJsonObject
                        WATCHDOG_BAN_LAST_MIN = objectAPI.get("watchdog_lastMinute").asInt
                        var staffBanTotal: Int = objectAPI.get("staff_total").asInt
                        if (staffBanTotal < LAST_TOTAL_STAFF) staffBanTotal = LAST_TOTAL_STAFF
                        if (LAST_TOTAL_STAFF == -1) LAST_TOTAL_STAFF = staffBanTotal else {
                            STAFF_BAN_LAST_MIN = staffBanTotal - LAST_TOTAL_STAFF
                            LAST_TOTAL_STAFF = staffBanTotal
                        }
                        if (STAFF_BAN_LAST_MIN > 0) {
                            println("${ANSI_YELLOW}Staffs banned $STAFF_BAN_LAST_MIN players in the last minute!${ANSI_WHITE}")
                        }
                        else {
                            println("${ANSI_GREEN}Staffs didn't ban any player in the last minute.${ANSI_WHITE}")
                        }
                        // watchdog ban doesnt matter, open an issue if you want to add it.
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    println("An error has occurred.")
                }
                checkTimer.reset()
            }
        }
    }
}
