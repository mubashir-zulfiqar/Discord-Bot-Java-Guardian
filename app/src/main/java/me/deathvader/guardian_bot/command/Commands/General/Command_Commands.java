//Developer: Mubashir Zulfiqar  Date: 2/11/2021  Time: 3:52 AM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.General;

import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.Commands_Manager;
import me.deathvader.guardian_bot.dbData;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.List;

public class Command_Commands implements Commands_Interface {

    private final Commands_Manager manager;

    public Command_Commands(Commands_Manager manager) {
        this.manager = manager;
    }

    @Override
    public SearchListResponse handle(Commands_Context context) {
        List<String> args = context.getArgs();
        TextChannel channel = context.getChannel();

        if (args.isEmpty()) {
            // StringBuilder builder = new StringBuilder();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Bot Commands");
            embedBuilder.setColor(new Color(0x4287f5));
            embedBuilder.setThumbnail("https://aux2.iconspalace.com/uploads/utilities-terminal-icon-256.png");
            embedBuilder.setDescription("");

            manager.getCommands().stream().map(Commands_Interface::getName).forEach(
                    (it) -> embedBuilder.appendDescription("**❯ " + dbData.PREFIXES.get(context.getGuild().getIdLong()))
                            .appendDescription(it)
                            .appendDescription("**\n\n")
                    /*(it) -> builder.append("**")
                            .append(Bot.BOT_PREFIX)
                            .append(it)
                            .append("**\n")*/
            );

            embedBuilder.appendDescription("To get information about a specific command.\nType `" + dbData.PREFIXES.get(context.getGuild().getIdLong()) + "help [command]`");
            embedBuilder.setFooter("Requested By " + context.getAuthor().getName(), context.getAuthor().getAvatarUrl());
            channel.sendMessage(embedBuilder.build()).queue();
            // channel.sendMessage(builder.toString()).queue();
            return null;
        }

        String search = args.get(0);
        Commands_Interface command = manager.getCommand(search);

        if (command == null) {
            channel.sendMessage("Nothing found for " + search).queue();
            return null;
        }

        channel.sendMessage(command.getHelp(dbData.PREFIXES.get(context.getGuild().getIdLong()))).queue();

        return null;
    }

    @Override
    public String getName() {
        return "commands";
    }

    @Override
    public String getCategory() {
        return GENERAL;
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows the List of Bot Commands\n" +
                "Usage `" + prefix + "commands [command]`";
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public List<String> getAliases() {
        return List.of("botcommands", "cmds", "commandlist", "botcomms", "comms", "coms");
    }
}
