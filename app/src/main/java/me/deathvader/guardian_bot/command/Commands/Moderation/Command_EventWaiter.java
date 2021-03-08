//Developer: Mubashir Zulfiqar  Date: 3/5/2021  Time: 4:13 AM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.Moderation;

import com.google.api.services.youtube.model.SearchListResponse;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Command_EventWaiter implements Commands_Interface {
    private static final String EMOTE = "👌";
    private final EventWaiter waiter;

    public Command_EventWaiter(EventWaiter eventWaiter) {
        this.waiter = eventWaiter;
    }

    @Override
    public SearchListResponse handle(Commands_Context context) throws IOException {
        final TextChannel channel = context.getChannel();

        channel.sendMessage("React with")
                .append(EMOTE)
                .queue((message -> {
                    message.addReaction(EMOTE).queue();
                    this.waiter.waitForEvent(
                            GuildMessageReactionAddEvent.class,
                            (e) -> e.getMessageIdLong() == message.getIdLong() && !e.getUser().isBot(),
                            (e) -> channel.sendMessageFormat("%#s was the first to react.", e.getUser()).queue(),
                            5L, TimeUnit.SECONDS, () -> channel.sendMessage("You waited too long.").queue()
                    );
                }));



        return null;
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public String getName() {
        return "eventwaiter";
    }

    @Override
    public String getCategory() {
        return TEST;
    }

    @Override
    public String getHelp(String prefix) {
        return "in process";
    }
}
