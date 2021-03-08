//Developer: Mubashir Zulfiqar  Date: 2/11/2021  Time: 1:29 AM
//Happy Coding...

package me.deathvader.guardian_bot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.deathvader.guardian_bot.command.Commands.General.*;
import me.deathvader.guardian_bot.command.Commands.Moderation.*;
import me.deathvader.guardian_bot.command.Commands.Fun.Command_Joke;
import me.deathvader.guardian_bot.command.Commands.Fun.Command_Meme;
import me.deathvader.guardian_bot.command.Commands.Testing.Command_Testing;
import me.deathvader.guardian_bot.command.Commands.music.*;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Commands_Manager {
    private final List<Commands_Interface> commands = new ArrayList<>();
    Commands_Context context;
    Commands_Interface command;

    public Commands_Manager() {

    }

    public Commands_Manager(EventWaiter waiter) {
        // GENERAL COMMANDS
        addCommand(new Command_Ping());
        addCommand(new Command_Commands(this));
        addCommand(new Command_Paste());
        addCommand(new Command_Meme());
        addCommand(new Command_Joke());
        addCommand(new Command_User());
        addCommand(new Command_Avatar());
        addCommand(new Command_ScreenShot());
        addCommand(new Command_Youtube_Search(waiter));
        addCommand(new Command_Uptime());
        addCommand(new Command_BotInfo(this));
        addCommand(new Command_Invite());
        addCommand(new Command_help(this));

        // MODERATION COMMANDS
        addCommand(new Command_Kick());
        addCommand(new Command_Ban());
        addCommand(new Command_Webhook());
        addCommand(new Command_SetPrefix());
        addCommand(new Command_Clear());
        addCommand(new Command_Embed(waiter));

        // MUSIC COMMANDS
        addCommand(new Command_Join());
        addCommand(new Command_Play());
        addCommand(new Command_Leave());
        addCommand(new Command_Pause());
        addCommand(new Command_Stop());
        addCommand(new Command_Skip());
        addCommand(new Command_NowPlaying());
        addCommand(new Command_Queue());
        addCommand(new Command_Repeat());
        addCommand(new Command_Add());

        /* UNDER PROCESS
         addCommand(new Command_Haste());
         addCommand(new Command_Instagram());
         addCommand(new Command_SetNick());
         addCommand(new Command_Lyrics());
         addCommand(new Commands_Weather());*/

        // FOR TESTING
        addCommand(new Command_Testing());
        addCommand(new Command_EventWaiter(waiter));
    }

    private void addCommand(Commands_Interface command) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(command.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }

        commands.add(command);
    }

    public List<Commands_Interface> getCommands() {
        return commands;
    }

    @Nullable
    public Commands_Interface getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (Commands_Interface command : this.commands) {
            if (command.getName().equals(searchLower) || command.getAliases().contains(searchLower)) {
                return command;
            }
        }
        return null;
    }

    void handle(GuildMessageReceivedEvent event, String prefix) throws IOException {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        command = this.getCommand(invoke);

        event.getChannel().sendTyping().queue();
        if (command != null) {
            List<String> args = Arrays.asList(split).subList(1, split.length);
            context = new Commands_Context(event, args);
            command.handle(context);
        } else {
            event.getChannel().sendMessage("**Command Not Found**").queue();
        }
    }
}
