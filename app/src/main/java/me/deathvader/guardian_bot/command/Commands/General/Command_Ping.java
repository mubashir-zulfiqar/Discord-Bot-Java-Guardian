//Developer: Mubashir Zulfiqar  Date: 2/11/2021  Time: 2:15 AM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.General;

import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Command_Ping implements Commands_Interface {

    @Override
    public SearchListResponse handle(Commands_Context context) {
        JDA jda = context.getJDA();
        final TextChannel channel = context.getChannel();

        jda.getRestPing().queue(
                (ping) -> {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setTitle("Server Ping Status");
                    embedBuilder.setColor(ping < 150 ? new Color(0x25db53) : ping > 150 && ping < 250 ? new Color(0xf2981b) : new Color(0xf22b1d));
                    embedBuilder.setThumbnail("https://cdn1.bbcode0.com/uploads/2021/2/12/8a3465fdcb370e2efc9bac944bff46a2-full.png");
                    embedBuilder.addField("Rest Ping", String.valueOf(ping), true);
                    embedBuilder.addBlankField(true);
                    embedBuilder.addField("Web Socket Ping", String.valueOf(jda.getGatewayPing()), true);
                    embedBuilder.setFooter("Requested By " + context.getAuthor().getName(), context.getAuthor().getAvatarUrl());
                    channel.sendMessage(embedBuilder.build()).queue( (botMessage) -> botMessage.delete().queueAfter(10, TimeUnit.SECONDS));
                    channel.deleteMessageById(context.getMessage().getId()).queueAfter(11, TimeUnit.SECONDS);
                }
        );
        return null;
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getCategory() {
        return "GENERAL";
    }

    @Override
    public String getHelp(String prefix) {
        return "See the current ping from\nthe Bot to the Server\n" +
                "Usage: `" + prefix + "ping`";
    }
}
