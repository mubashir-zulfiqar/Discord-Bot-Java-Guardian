//Developer: Mubashir Zulfiqar  Date: 3/2/2021  Time: 10:58 PM
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
import java.io.IOException;
import java.util.List;

public class Command_SetWelcomeChannel implements Commands_Interface {

    @Override
    public SearchListResponse handle(Commands_Context context) throws IOException {
        final TextChannel channel = context.getChannel();
        final List<String> args = context.getArgs();
        final Message message = context.getMessage();
        final Member member = context.getMember();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (args.size() < 2 || message.getMentionedChannels().isEmpty()){
            embedBuilder.setAuthor("Missing Arguments");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        final TextChannel targetChannel = message.getMentionedChannels().get(0);

        if (!member.hasPermission(Permission.MANAGE_CHANNEL)) {
            embedBuilder.setAuthor(member.getUser().getName() + " you don't have permission to ban this member");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        final Member selfMember = context.getSelfMember();

        if (!selfMember.hasPermission(Permission.MANAGE_CHANNEL)) {
            embedBuilder.setAuthor("I don't have permission to ban that member.");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        embedBuilder.setAuthor("Sorry, This Command is under Process 🚧");
        embedBuilder.setColor(new Color(0xf22b1d));
        channel.sendMessage(embedBuilder.build()).queue();
        //TODO abhi manualy bana howa hey sahi sey yahaan banana hey. Or setWelcomeMessage ki Command bhi banani hey
        //TODO abhi Listner mein manually bana howa hey Database sey connect kar key banana hey

        return null;
    }

    @Override
    public String getName() {
        return "setWelcomeChannel";
    }

    @Override
    public String getCategory() {
        return "MOD";
    }

    @Override
    public String getHelp(String prefix) {
        return "Set a Channel for for welcoming the new members in your server\n" +
                "Usage: `" + prefix + "setwelcomechannel [#channel]";
    }

    @Override
    public List<String> getAliases() {
        return List.of("swc", "setwc");
    }
}
