//Developer: Mubashir Zulfiqar  Date: 2/14/2021  Time: 11:26 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.General;

import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import me.deathvader.guardian_bot.dbData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.List;

public class Command_Avatar implements Commands_Interface {

    @Override
    public SearchListResponse handle(Commands_Context context) {

        final List<String> args = context.getArgs();
        final TextChannel channel = context.getChannel();
        final User author = context.getAuthor();
        final Message message = context.getMessage();
        final EmbedBuilder embedBuilder = new EmbedBuilder();

        if (args.size() < 1 && message.getMentionedMembers().isEmpty()) {
            embedBuilder.setImage(author.getAvatarUrl() + "?size=1024");
            embedBuilder.setFooter("Requested By " + author.getName(), author.getAvatarUrl());
            channel.sendMessage(embedBuilder.build()).queue();
        } else if (args.size() == 1 && !message.getMentionedMembers().isEmpty()) {
            embedBuilder.setImage(message.getMentionedMembers().get(0).getUser().getAvatarUrl() + "?size=1024");
            embedBuilder.setFooter("Requested By " + author.getName(), author.getAvatarUrl());
            channel.sendMessage(embedBuilder.build()).queue();
        } else if (args.size() == 1 && message.getMentionedMembers().isEmpty()) {
            embedBuilder.setAuthor("User not Found");
            embedBuilder.setColor(new Color(0xF12F4B));
            channel.sendMessage(embedBuilder.build()).queue();
        } else if (args.size() > 1) {
            channel.sendMessage(getHelp(dbData.PREFIXES.get(context.getGuild().getIdLong()))).queue();
        }
        return null;
    }

    @Override
    public String getName() {
        return "avatar";
    }

    @Override
    public String getCategory() {
        return GENERAL;
    }

    @Override
    public String getHelp(String prefix) {
        return "Get a user avatar\n" +
                "Usage: `" + prefix + "avatar [@user]`";
    }
}
