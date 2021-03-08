//Developer: Mubashir Zulfiqar  Date: 2/12/2021  Time: 1:52 AM
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

import java.awt.*;
import java.util.List;

public class Command_Kick implements Commands_Interface {

    @Override
    public SearchListResponse handle(Commands_Context context) {
        final TextChannel channel = context.getChannel();
        final Message message = context.getMessage();
        final Member member = context.getMember();
        final List<String> args = context.getArgs();
        final EmbedBuilder embedBuilder = new EmbedBuilder();

        if (args.size() < 2 || message.getMentionedMembers().isEmpty()) {
            embedBuilder.setAuthor("Missing Arguments");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        final Member targetUser = message.getMentionedMembers().get(0);

        if (!member.canInteract(targetUser) || !member.hasPermission(Permission.KICK_MEMBERS)) {
            embedBuilder.setAuthor(member.getUser().getName() + " you don't have permission to kick this member");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        final Member selfMember = context.getSelfMember();

        if (!selfMember.canInteract(targetUser) || !selfMember.hasPermission(Permission.KICK_MEMBERS)) {
            embedBuilder.setAuthor("I don't have permission to kick that member.");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        final String reason = String.join("", args.subList(1, args.size()));

        context.getGuild().kick(targetUser, reason).reason(member.getUser().getName() + " kicked "+
                targetUser.getUser().getName() + ".\n**Reason:** " + reason).queue(
                (__) -> {
                    embedBuilder.setAuthor(targetUser.getUser().getName() + " has been kicked from the server successfully.");
                    embedBuilder.setColor(new Color(0x25db53));
                    channel.sendMessage(embedBuilder.build()).queue();
                },
                (error) -> {
                    embedBuilder.setAuthor("Could not kick %s", error.getMessage());
                    embedBuilder.setColor(new Color(0xf22b1d));
                    channel.sendMessage(embedBuilder.build()).queue();
                }
        );
        return null;
    }

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getCategory() {
        return "MOD";
    }

    @Override
    public String getHelp(String prefix) {
        return "Kick a member off the server.\n" +
                "Usage: `" + prefix +"kick [@user] [reason]`";
    }
}
