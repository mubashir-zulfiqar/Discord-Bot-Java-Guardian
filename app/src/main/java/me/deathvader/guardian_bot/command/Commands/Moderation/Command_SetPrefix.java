//Developer: Mubashir Zulfiqar  Date: 2/12/2021  Time: 10:44 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.Moderation;

import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.Bot;
import me.deathvader.guardian_bot.dbData;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import me.deathvader.guardian_bot.database.DatabaseManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Command_SetPrefix implements Commands_Interface {

    @Override
    public SearchListResponse handle(Commands_Context context) {
        final TextChannel channel = context.getChannel();
        final List<String> args = context.getArgs();
        final Member member = context.getMember();
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

        final String newPrefix = String.join(" ", args);
        Bot.BOT_PREFIX = newPrefix;
        updatePrefix(context.getGuild().getIdLong(), newPrefix);
        embedBuilder.setAuthor("New prefix has been set to `" + newPrefix + "`");
        embedBuilder.setColor(new Color(0x25db53));
        channel.sendMessage(embedBuilder.build()).queue( (botMessage) -> botMessage.delete().queueAfter(2, TimeUnit.SECONDS));
        channel.deleteMessageById(context.getMessage().getId()).queueAfter(3, TimeUnit.SECONDS);
        return null;
    }

    @Override
    public String getName() {
        return "setprefix";
    }

    @Override
    public String getCategory() {
        return "MOD";
    }

    @Override
    public String getHelp(String prefix) {
        return "Sets the prefix for this server\n" +
                "Usage: `" + prefix + "setprefix <prefix>`";
    }

    private void updatePrefix(long guildID, String newPrefix) {
        dbData.PREFIXES.put(guildID, newPrefix);
        DatabaseManager.INSTANCE.setPrefix(guildID, newPrefix);
    }
}
