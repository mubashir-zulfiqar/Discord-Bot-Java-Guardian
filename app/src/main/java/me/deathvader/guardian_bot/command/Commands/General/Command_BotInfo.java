//Developer: Mubashir Zulfiqar  Date: 3/5/2021  Time: 8:39 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.General;

import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.Commands_Manager;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import me.deathvader.guardian_bot.dbData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Command_BotInfo implements Commands_Interface {

    private final Commands_Manager manager;

    public Command_BotInfo(Commands_Manager manager) {
        this.manager = manager;
    }

    @Override
    public SearchListResponse handle(Commands_Context context) {
        final TextChannel channel = context.getChannel();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        final Member member = context.getMember();
        final Member bot = context.getSelfMember();
        final User user = member.getUser();
        final String inviteLink = "https://discord.com/api/oauth2/authorize?client_id=805974578249334855&permissions=8&scope=bot";

        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        long upTime = runtimeMXBean.getUptime();
        long upTimeInSeconds = upTime / 1000;
        long numberOfHours = upTimeInSeconds / (60 * 60);
        long numberOfMinutes = (upTimeInSeconds / 60) - (numberOfHours * 60);
        long numberOfSeconds = upTimeInSeconds % 60;
        long numberOfDays = upTimeInSeconds / 86400;

        if (!member.hasPermission(Permission.ADMINISTRATOR)){
            embedBuilder.setAuthor("This Command can only be used by Administrators.");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
        }

        embedBuilder.setAuthor("About " + bot.getUser().getName());
        embedBuilder.setDescription("Hello there, my name is **" + bot.getUser().getName() + "** and I’m currently being developed by <@542410968949456936>.");

        final List<Guild> authorGuilds = context.getMessage().getAuthor().getJDA().getGuilds();
        for (Guild guild : authorGuilds) {
            if (!guild.getMembers().contains(bot) && Objects.requireNonNull(guild.getMember(user)).hasPermission(Permission.MANAGE_SERVER)){
                embedBuilder.appendDescription("If you want to add me to your server you can do that by [*Clicking Here*](" + inviteLink + ").");
            }
        }

        embedBuilder.appendDescription("\nYou can join my official server [**The Order**](https://discord.gg/JsSQwYEYre) and share your **ideas** that you feel would benefit the me [*" + bot.getUser().getName() + "*] in any way.");
        embedBuilder.appendDescription("\nAnd If you have noticed any **bug**, please mention it too on the Server so that Developer can get rid of it as quickly as possible.");

        embedBuilder.addField("Language", "<:java:817355692767445012> Java `14`", true);
        embedBuilder.addField("Wrapper", "<:jda_2:818540930277572649> JDA `3.0.5`", true);
        embedBuilder.addField("Servers", String.valueOf(bot.getJDA().getGuilds().size()), true);
        // embedBuilder.addBlankField(false);
        embedBuilder.addField("Total Commands", String.valueOf(manager.getCommands().size() + 1), true);
        embedBuilder.addField("Up Time" ,
                "`" + numberOfDays + "`d, `" +
                        numberOfHours + "`h, `" +
                        numberOfMinutes + "`m, `" +
                        numberOfSeconds + "`s",
                true);
        embedBuilder.addField("Help", "Type `" + dbData.PREFIXES.get(member.getGuild().getIdLong()) + "help`\nFor my Command list.", true);

        embedBuilder.setColor(new Color(0x4B9DEC));
        embedBuilder.setThumbnail(bot.getUser().getAvatarUrl());
        embedBuilder.setTimestamp(new Date().toInstant().atOffset(ZoneOffset.UTC));
        embedBuilder.setFooter("Requested By " + member.getUser().getName(), member.getUser().getAvatarUrl());
        channel.sendMessage(embedBuilder.build()).queue();

        return null;
    }

    @Override
    public String getName() {
        return "botinfo";
    }

    @Override
    public String getHelp(String prefix) {
        return "Display the bot info.\n" +
                "Usage: `" + prefix + "botinfo`.";
    }

    @Override
    public String getCategory() {
        return GENERAL;
    }
}
