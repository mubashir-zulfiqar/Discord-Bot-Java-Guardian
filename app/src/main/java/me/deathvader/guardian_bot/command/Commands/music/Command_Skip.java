//Developer: Mubashir Zulfiqar  Date: 3/5/2021  Time: 2:01 AM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.music;

import com.google.api.services.youtube.model.SearchListResponse;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
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

public class Command_Skip implements Commands_Interface {
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
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null) {
            embedBuilder.setAuthor("There is no track playing currently.");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        if (musicManager.scheduler.queue.size() > 1) {
            musicManager.scheduler.nextTrack();
            embedBuilder.setAuthor("Skipped the current track.");
            embedBuilder.setColor(new Color(0x1DF264));
        } else {
            embedBuilder.setAuthor("There are no tracks in the queue to skip");
            embedBuilder.setColor(new Color(0xf22b1d));
        }
        channel.sendMessage(embedBuilder.build()).queue();
        return null;
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getCategory() {
        return "MUSIC";
    }

    @Override
    public String getHelp(String prefix) {
        return "skip the current track in the playlist.";
    }
}
