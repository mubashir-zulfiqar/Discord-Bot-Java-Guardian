//Developer: Mubashir Zulfiqar  Date: 2/14/2021  Time: 4:30 AM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.Moderation;

import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class Command_SetNick implements Commands_Interface {

    @Override
    public SearchListResponse handle(Commands_Context context) {
        final Member member = context.getMember();
        final TextChannel channel = context.getChannel();
        final List<String> args = context.getArgs();
        final Message message = context.getMessage();
        final EmbedBuilder embedBuilder = new EmbedBuilder();

        if (args.size() < 1) {
            embedBuilder.setAuthor("Missing Arguments");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        if (!member.hasPermission(Permission.NICKNAME_CHANGE)) {
            embedBuilder.setAuthor(member.getUser().getName() + " You must have the permission to use this command");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        final String mentionedUser = args.get(0);
        final User targetUser = Objects.requireNonNull(context.getGuild().getMemberById(args.get(0))).getUser();
        final String rawContent = context.getMessage().getContentRaw();


        if (message.getMentionedMembers().isEmpty()) {
            if (message.getContentRaw().length() > 32) {
                embedBuilder.setColor(new Color(0xf22b1d));
                embedBuilder.setAuthor("Nick must be less than or equal to 32 characters");
            } else {
                member.modifyNickname(message.getContentRaw()).queue();
                embedBuilder.setColor(new Color(0x25db53));
                embedBuilder.setAuthor("<@" + mentionedUser + "> nick has been changed to " + rawContent, targetUser.getAvatarUrl());
            }
        } else {
            final int index = rawContent.indexOf(mentionedUser) + mentionedUser.length();
            final String newNick = rawContent.substring(index).trim();

            if (newNick.length() > 32) {
                embedBuilder.setColor(new Color(0xf22b1d));
                embedBuilder.setAuthor("Nick must be less than or equal to 32 characters");
                return null;
            } else {
                message.getMentionedMembers().get(0).modifyNickname(newNick).queue();
                embedBuilder.setAuthor("<@" + mentionedUser + "> nick has been changed to " + newNick, Objects.requireNonNull(context.getGuild().getMemberById(mentionedUser)).getUser().getAvatarUrl());
            }
        }
        channel.sendMessage(embedBuilder.build()).queue();
        return null;
    }

    @Override
    public String getName() {
        return "setnick";
    }

    @Override
    public String getCategory() {
        return "MOD";
    }

    @Override
    public String getHelp(String prefix) {
        return "Set your nick";
    }
}
