//Developer: Mubashir Zulfiqar  Date: 2/15/2021  Time: 8:17 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.General;

import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class Command_Youtube_Search implements Commands_Interface {
    private final EventWaiter waiter;

    public Command_Youtube_Search(EventWaiter eventWaiter) {
        this.waiter = eventWaiter;
    }

    @Override
    public SearchListResponse handle(Commands_Context context) {
        final List<String> args = context.getArgs();
        final TextChannel channel = context.getChannel();
        final String authorId = context.getMessage().getAuthor().getId();
        final Message message = context.getMessage();
        final StringBuilder stringBuilder = new StringBuilder();
        final EmbedBuilder embedBuilder = new EmbedBuilder();

        for (String arg : args)
            stringBuilder.append(arg).append(" ");

        final String SEARCH_QUERY = stringBuilder.toString();

        if (stringBuilder.length() < 1) {
            embedBuilder.setAuthor("Enter a valid Search Query.");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        if (message.getAuthor().isBot()) return null;

        final Youtube_Search youtube_search = new Youtube_Search(SEARCH_QUERY, context);
        final List<SearchResult> searchResult = youtube_search.search();
        context.getChannel().sendMessage("**Enter no form 1-5 to get the video.**").queue();
        waiter.waitForEvent(GuildMessageReceivedEvent.class,
                (e) -> e.getMessage().getAuthor().getId().equals(authorId) && e.getChannel().equals(channel),
                (e) -> {
                    SearchResult finalSearchResult = null;
                    try {
                        finalSearchResult = searchResult.get(Integer.parseInt(e.getMessage().getContentRaw()) - 1);
                    } catch (Exception exception) {
                        System.out.println();
                    }
                    assert finalSearchResult != null;
                    // TODO exception sahi karna hey
                    e.getChannel().sendMessage("https://www.youtube.com/watch?v=" + finalSearchResult.getId().getVideoId()).queue();
                });
        return null;
    }

    @Override
    public String getName() {
        return "youtube";
    }

    @Override
    public String getCategory() {
        return "GENERAL";
    }

    @Override
    public String getHelp(String prefix) {
        return "Search youtube\n" +
                "Usage: `" + prefix + "youtube [Search]`\nAliases: `" +
                getAliases() + "`";
    }

    @Override
    public List<String> getAliases() {
        return List.of("yt", "yTube", "tube");
    }
}