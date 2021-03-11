//Developer: Mubashir Zulfiqar  Date: 3/9/2021  Time: 11:06 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.Moderation;

import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import me.deathvader.guardian_bot.database.DatabaseManager;
import me.deathvader.guardian_bot.dbData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@SuppressWarnings("SpellCheckingInspection")
public class Command_SetBadWord implements Commands_Interface {

    @Override
    public SearchListResponse handle(Commands_Context context) throws IOException {
        final TextChannel channel = context.getChannel();
        final List<String> args = context.getArgs();
        final Member member = context.getMember();
        final long guildID = context.getGuild().getIdLong();
        final EmbedBuilder embedBuilder = new EmbedBuilder();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            embedBuilder.setAuthor(member.getUser().getName() + " You must have the Manage Server permission to use his command");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        if (args.isEmpty()) {
            embedBuilder.setAuthor("Missing Arguments");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        final String word = String.join(" ", args);
        DatabaseManager.INSTANCE.setBadWord(guildID, word.toLowerCase());
        embedBuilder.setAuthor("**" + word + "** has beed added into the swear word list.");
        embedBuilder.setColor(new Color(0x25db53));
        channel.sendMessage(embedBuilder.build()).queue( (botMessage) -> botMessage.delete().queueAfter(2, TimeUnit.SECONDS));
        channel.deleteMessageById(context.getMessage().getId()).queueAfter(3, TimeUnit.SECONDS);
        return null;
    }

    @Override
    public String getName() {
        return "setbadword";
    }

    @Override
    public String getCategory() {
        return MODERATION;
    }

    @Override
    public String getHelp(String prefix) {
        return "set a swear word for you guild and warns a member if he typed that word.\n" +
                "Usage: `" + prefix + "setbadword [word]`.";
    }

    @Override
    public List<String> getAliases() {
        return List.of(
                "sbw",
                "set-bad-word",
                "add-bad-word",
                "abw",
                "addbadword",
                "insertbadword",
                "ibw",
                "insert-bad-word"
        );
    }
}
