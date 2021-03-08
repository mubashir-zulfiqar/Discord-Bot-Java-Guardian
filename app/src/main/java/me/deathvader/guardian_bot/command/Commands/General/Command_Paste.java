//Developer: Mubashir Zulfiqar  Date: 2/11/2021  Time: 9:06 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.General;

import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.menudocs.paste.PasteClient;
import org.menudocs.paste.PasteClientBuilder;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Command_Paste implements Commands_Interface {
    private final PasteClient client = new PasteClientBuilder()
            .setUserAgent("Guardian Bot")
            .setDefaultExpiry("10m")
            .build();

    @Override
    public SearchListResponse handle(Commands_Context context) {
        final List<String> args = context.getArgs();
        final TextChannel channel = context.getChannel();

        if (args.size() < 2) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("Missing Arguments");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue( (botMessage) -> botMessage.delete().queueAfter(2, TimeUnit.SECONDS));
            channel.deleteMessageById(context.getMessage().getId()).queueAfter(3, TimeUnit.SECONDS);
            return null;
        }

        final String language = args.get(0);
        final String rawContent = context.getMessage().getContentRaw();
        final int index = rawContent.indexOf(language) + language.length();
        final String body = rawContent.substring(index).trim();

        client.createPaste(language, body).async(
                /*(id) -> channel.sendMessageFormat("https://paste.menudocs.org/paste/%s", id).queue()*/
                (id) -> client.getPaste(id).async((paste) -> {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setTitle("Your Paste is Here", paste.getPasteUrl())
                            .setDescription("```")
                            .appendDescription(paste.getLanguage().getId())
                            .appendDescription("\n")
                            .appendDescription(paste.getBody())
                            .appendDescription("```");

                    channel.sendMessage(builder.build()).queue();
                })
        );
        return null;
    }

    @Override
    public String getName() {
        return "paste";
    }

    @Override
    public String getCategory() {
        return "GENERAL";
    }

    @Override
    public String getHelp(String prefix) {
        return "Creates a paste on the Menu-Docs paste\n" +
                "Usage: `"+ prefix + "paste [language] [text]`";
    }
}
