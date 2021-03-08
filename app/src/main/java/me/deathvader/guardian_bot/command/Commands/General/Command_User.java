//Developer: Mubashir Zulfiqar  Date: 2/14/2021  Time: 7:22 PM
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

public class Command_User implements Commands_Interface {
    EmbedBuilder embedBuilder = new EmbedBuilder();

    @Override
    public SearchListResponse handle(Commands_Context context) {
        final List<String> args = context.getArgs();
        final Message message = context.getMessage();
        final TextChannel channel = context.getChannel();
        final User author = context.getAuthor();

        if (args.size() < 1 && message.getMentionedMembers().isEmpty()) {
            channel.sendMessage(userInfo(author, context).build()).queue();
        } else if (args.size() == 1 && !message.getMentionedMembers().isEmpty()) {
            channel.sendMessage(userInfo(message.getMentionedMembers().get(0).getUser(), context).build()).queue();
        } else if (args.size() == 1 && message.getMentionedMembers().isEmpty()) {
            embedBuilder.setAuthor("User not Found");
            embedBuilder.setColor(new Color(0xF12F4B));
            channel.sendMessage(embedBuilder.build()).queue();
        } else if (args.size() > 1) {
            channel.sendMessage(getHelp(dbData.PREFIXES.get(context.getGuild().getIdLong()))).queue();
        }
        return null;
    }

    private EmbedBuilder userInfo(User user, Commands_Context context) {
        String day, dayOfWeek, month, year, dateJoined;
        day = String.valueOf(user.getTimeCreated().getDayOfMonth());
        dayOfWeek = String.valueOf(user.getTimeCreated().getDayOfWeek().toString());
        month = String.valueOf(user.getTimeCreated().getMonth().getValue());
        year = String.valueOf(user.getTimeCreated().getYear());
        dateJoined = dayOfWeek + ", " + day + "/" + month + "/" + year;

        if (user.isBot()) {
            embedBuilder.setAuthor("Bot Info:");
            embedBuilder.setColor(new Color(0x6790c9));
            embedBuilder.setThumbnail(user.getAvatarUrl());
            embedBuilder.setTitle("Name: " + user.getName() + " [BOT]");
            embedBuilder.addField("Discriminator:", "#" + user.getDiscriminator(), true);
            embedBuilder.addBlankField(true);
            embedBuilder.addField("User Id:", "||" + user.getId() + "||", true);
            embedBuilder.addField("Joined Discord on:", "`" + dateJoined + "`", false);
        } else {
            embedBuilder.setAuthor("User Info:");
            embedBuilder.setColor(new Color(0x6790c9));
            embedBuilder.setThumbnail(user.getAvatarUrl());
            embedBuilder.setTitle("Name: " + user.getName());
            embedBuilder.addField("Discriminator:", "#" + user.getDiscriminator(), true);
            embedBuilder.addBlankField(true);
            embedBuilder.addField("User Id:", "||" + user.getId() + "||", true);
            embedBuilder.addField("Joined Discord on:", "`" + dateJoined + "`", true);
            embedBuilder.addBlankField(true);
            embedBuilder.addField("Nitro", findRole(context, "Nitro") ? "You have Nitro" : "You don't have Nitro", true);
            embedBuilder.setFooter("Requested By " + context.getAuthor().getName(), context.getAuthor().getAvatarUrl());
        }
        return embedBuilder;
    }

    public static boolean findRole(Commands_Context context, String roleName) {
        for (int i = 0; i < context.getMember().getRoles().size(); i++) {
            if (roleName.equals(context.getMember().getRoles().get(i).getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "user";
    }

    @Override
    public String getCategory() {
        return "GENERAL";
    }

    @Override
    public String getHelp(String prefix) {
        return "Display a User info\n" +
                "Usage: `" + prefix + "user [@user]`.";
    }
}
