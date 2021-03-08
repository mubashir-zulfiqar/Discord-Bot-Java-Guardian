//Developer: Mubashir Zulfiqar  Date: 3/5/2021  Time: 11:06 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.General;

import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.Commands_Manager;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import me.deathvader.guardian_bot.dbData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.io.IOException;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Command_help implements Commands_Interface {

    private final Commands_Manager manager;

    public Command_help(Commands_Manager manager) {
        this.manager = manager;
    }

    @Override
    public SearchListResponse handle(Commands_Context context) throws IOException {
        long guildID = context.getGuild().getIdLong();
        final List<String> args = context.getArgs();

        final User user = context.getMember().getUser();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        final TextChannel channel = context.getChannel();
        final List<Commands_Interface> commands = manager.getCommands();

        if (args.isEmpty()){
            // Commands Type's
            ArrayList<Commands_Interface> generalCommands = new ArrayList<>();
            ArrayList<Commands_Interface> moderationCommands = new ArrayList<>();
            ArrayList<Commands_Interface> musicCommands = new ArrayList<>();
            ArrayList<Commands_Interface> funCommands = new ArrayList<>();

            for (Commands_Interface command : commands) {
                switch (command.getCategory()) {
                    case "GENERAL" -> generalCommands.add(command);
                    case "MOD" -> moderationCommands.add(command);
                    case "MUSIC" -> musicCommands.add(command);
                    case "FUN" -> funCommands.add(command);
                }
            }

            embedBuilder.setAuthor("Bot Commands");
            embedBuilder.setDescription("For more information about a command type `" +
                    dbData.PREFIXES.get(guildID) + "help [command]`.");
            embedBuilder.setColor(new Color(0x4185B1));

            embedBuilder.addField("General Commands", helpPattern(generalCommands), true);
            embedBuilder.addField("Moderation Commands", helpPattern(moderationCommands), true);
            embedBuilder.addField("Music Commands", helpPattern(musicCommands), true);
            embedBuilder.addBlankField(false);
            embedBuilder.addField("Fun Commands", helpPattern(funCommands), true);

            embedBuilder.setFooter("Requested By " + user.getName() + ".", user.getAvatarUrl());
            embedBuilder.setTimestamp(new Date().toInstant().atOffset(ZoneOffset.UTC));

            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        String search = args.get(0);
        final Commands_Interface command = manager.getCommand(search);

        if (command == null) {
            channel.sendMessage("Nothing found for " + search).queue();
            return null;
        }

        channel.sendMessage(command.getHelp(dbData.PREFIXES.get(guildID))).queue();
        return null;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp(String prefix) {
        return "Display the Commands that can be used by the bot\n" +
                "Usage: `" + prefix + "help`.";
    }

    @Override
    public String getCategory() {
        return "GENERAL";
    }

    private String helpPattern(List<Commands_Interface> commands) {
        String separator = "";
        StringBuilder stringBuilder = new StringBuilder();
        for (Commands_Interface command : commands) {
            stringBuilder.append(separator).append('`').append(command.getName()).append('`');
            separator = " | ";
        }
        return stringBuilder.toString();
    }
}