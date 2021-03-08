//Developer: Mubashir Zulfiqar  Date: 3/5/2021  Time: 9:38 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.General;

import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.io.IOException;
import java.time.ZoneOffset;
import java.util.Date;

public class Command_Invite implements Commands_Interface {
    @Override
    public SearchListResponse handle(Commands_Context context) throws IOException {
        final User user = context.getMember().getUser();
        final String inviteLink = "https://discord.com/api/oauth2/authorize?client_id=805974578249334855&permissions=8&scope=bot";
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final Message message = context.getMessage();

        embedBuilder.setAuthor("Invite Link" , null, "https://i.imgur.com/x0g3gzo.png");
        embedBuilder.setDescription("You can invite the Bot in your server by [clicking here](" + inviteLink + ").");
        embedBuilder.setFooter("Invite Created by you [" + user.getName() + "]", user.getAvatarUrl());
        embedBuilder.setTimestamp(new Date().toInstant().atOffset(ZoneOffset.UTC));
        embedBuilder.setColor(new Color(0x45A5D9));

        message.reply(user.getName() + " Check Your Dm.").queue();
        user.openPrivateChannel().queue((privateChannel) -> privateChannel.sendMessage(embedBuilder.build()).queue());

        return null;
    }

    @Override
    public String getName() {
        return "invitebot";
    }

    @Override
    public String getCategory() {
        return "GENERAL";
    }

    @Override
    public String getHelp(String prefix) {
        return "Send an invite link of the bot for your guild.\n" +
                "Usage: `" + prefix + "invite`.";
    }
}
