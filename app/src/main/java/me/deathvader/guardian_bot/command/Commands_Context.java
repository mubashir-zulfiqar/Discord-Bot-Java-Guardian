//Developer: Mubashir Zulfiqar  Date: 2/11/2021  Time: 1:22 AM
//Happy Coding...

package me.deathvader.guardian_bot.command;

import me.duncte123.botcommons.commands.ICommandContext;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class Commands_Context implements ICommandContext {
    private final GuildMessageReceivedEvent event;
    private final List<String> args;

    public Commands_Context(GuildMessageReceivedEvent event, List<String> args) {
        this.event = event;
        this.args = args;
    }

    @Override
    public Guild getGuild() {
        return this.getEvent().getGuild();
    }

    @Override
    public GuildMessageReceivedEvent getEvent() {
        return this.event;
    }

    public List<String> getArgs() {
        return this.args;
    }
}
