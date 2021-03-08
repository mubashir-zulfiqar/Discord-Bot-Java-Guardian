//Developer: Mubashir Zulfiqar  Date: 3/5/2021  Time: 4:45 AM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.General;

import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class Command_Uptime implements Commands_Interface {
    @Override
    public SearchListResponse handle(Commands_Context context) throws IOException {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        long upTime = runtimeMXBean.getUptime();
        long upTimeInSeconds = upTime / 1000;
        long numberOfHours = upTimeInSeconds / (60 * 60);
        long numberOfMinutes = (upTimeInSeconds / 60) - (numberOfHours * 60);
        long numberOfSeconds = upTimeInSeconds % 60;
        long numberOfDays = upTimeInSeconds / 86400;

        if (numberOfHours > 24) {
            context.getChannel().sendMessageFormat("My uptime is `%s` days, `%s` hours, `%s` minutes, `%s` seconds",
                    numberOfDays, numberOfHours, numberOfMinutes, numberOfSeconds).queue();
            return null;
        }

        context.getChannel().sendMessageFormat("My uptime is `%s` hours, `%s` minutes, `%s` seconds",
                numberOfHours, numberOfMinutes, numberOfSeconds).queue();

        return null;
    }

    @Override
    public String getName() {
        return "uptime";
    }

    @Override
    public String getCategory() {
        return "GENERAL";
    }

    @Override
    public String getHelp(String prefix) {
        return "Show the current uptime of the bot\n" +
                "Usage: `" + prefix + "uptime`.";
    }
}
