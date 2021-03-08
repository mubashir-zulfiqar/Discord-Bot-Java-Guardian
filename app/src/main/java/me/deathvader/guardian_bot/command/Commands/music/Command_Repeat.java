//Developer: Mubashir Zulfiqar  Date: 3/5/2021  Time: 3:34 AM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.music;

import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.Listener;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import me.deathvader.guardian_bot.lavaplayer.GuildMusicManager;
import me.deathvader.guardian_bot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Command_Repeat implements Commands_Interface {
    @Override
    public SearchListResponse handle(Commands_Context context) throws IOException {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        final TextChannel channel = context.getChannel();
        final Member bot = context.getSelfMember();
        final GuildVoiceState botVoiceState = bot.getVoiceState();

        final Member member = context.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        Listener.lastMusicCmdChannel = context.getChannel();

        assert memberVoiceState != null;
        if (!memberVoiceState.inVoiceChannel()) {
            embedBuilder.setAuthor("Please Join a voice Channel to use this command. And play a song and use this command");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }


        assert botVoiceState != null;
        if (!Objects.equals(memberVoiceState.getChannel(), botVoiceState.getChannel()) && botVoiceState.inVoiceChannel()){
            embedBuilder.setAuthor("You need to be in the same voice channel as me to use this command.");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(context.getGuild());
        final boolean newRepeating = !musicManager.scheduler.repeating;
        musicManager.scheduler.repeating = newRepeating;

        channel.sendMessageFormat("The has been set to **%s**.", newRepeating ? "repeating" : "not repeating").queue();

        return null;
    }

    @Override
    public String getName() {
        return "repeat";
    }

    @Override
    public String getCategory() {
        return "MUSIC";
    }

    @Override
    public String getHelp(String prefix) {
        return "Repeat the current song.";
    }
}
