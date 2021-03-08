//Developer: Mubashir Zulfiqar  Date: 2/14/2021  Time: 2:12 AM
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
import java.util.concurrent.TimeUnit;

public class Command_Clear implements Commands_Interface {

    @Override
    public SearchListResponse handle(Commands_Context context) {
        final List<String> args = context.getArgs();
        final TextChannel channel = context.getChannel();
        final Member member = context.getMember();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (args.isEmpty()){
            embedBuilder.setAuthor("Missing Arguments");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        if (!member.hasPermission(Permission.MESSAGE_HISTORY)) {
            embedBuilder.setAuthor(member.getUser().getName() + " you don't have permission to Delete Messages.");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        if (Integer.parseInt(args.get(0)) > 100 || Integer.parseInt(args.get(0)) < 2){
            embedBuilder.setAuthor("Must provide at least 2 or at most 100 messages to be deleted.");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        List<Message> messages = channel.getHistory().retrievePast(Integer.parseInt(args.get(0))).complete();
        channel.deleteMessages(messages).queue();
        embedBuilder.setAuthor(args.get(0) + " messages has been deleted from the channel.");
        embedBuilder.setColor(new Color(0x25db53));
        channel.sendMessage(embedBuilder.build()).queue( (m) -> m.delete().queueAfter(5, TimeUnit.SECONDS));
        return null;
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getCategory() {
        return "MOD";
    }

    @Override
    public String getHelp(String prefix) {
        return "Clear chat messages (max 100) (min 2)" +
                "Usage: `" + prefix + "clear`";
    }
}
