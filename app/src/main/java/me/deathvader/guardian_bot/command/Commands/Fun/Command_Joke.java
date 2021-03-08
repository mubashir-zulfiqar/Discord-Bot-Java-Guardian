//Developer: Mubashir Zulfiqar  Date: 2/12/2021  Time: 6:36 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.Fun;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class Command_Joke implements Commands_Interface {

    @Override
    public SearchListResponse handle(Commands_Context context) {
        final TextChannel channel = context.getChannel();
        WebUtils.ins.getJSONObject("https://apis.duncte123.me/joke").async(
                (json) -> {
                    if (!json.get("success").asBoolean()) {
                        channel.sendMessage("Something went wrong, try again later").queue();
                        System.out.println(json);
                        return;
                    }

                    final JsonNode data = json.get("data");
                    final String title = data.get("title").asText();
                    final String url = data.get("url").asText();
                    final String body = data.get("body").asText();
                    final EmbedBuilder embedBuilder = new EmbedBuilder();

                    embedBuilder.setTitle(title, url);
                    embedBuilder.setDescription("|| **" + body + "**||");
                    embedBuilder.setColor(new Color(0x2BD762));
                    embedBuilder.setFooter("Requested By " + context.getAuthor().getName(), context.getAuthor().getAvatarUrl());
                    channel.sendMessage(embedBuilder.build()).queue();
                }
        );
        return null;
    }

    @Override
    public String getName() {
        return "joke";
    }

    @Override
    public String getCategory() {
        return FUN;
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows a Random Joke.\n" +
                "Usage: `" + prefix + "joke`";
    }
}
