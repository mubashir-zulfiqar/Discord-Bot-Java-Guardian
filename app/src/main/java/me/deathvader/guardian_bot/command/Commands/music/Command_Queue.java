//Developer: Mubashir Zulfiqar  Date: 3/5/2021  Time: 2:58 AM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.music;

import com.google.api.services.youtube.model.SearchListResponse;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import me.deathvader.guardian_bot.lavaplayer.GuildMusicManager;
import me.deathvader.guardian_bot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.io.IOException;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Command_Queue implements Commands_Interface {
    @Override
    public SearchListResponse handle(Commands_Context context) throws IOException {
        final TextChannel channel = context.getChannel();
        final User user = context.getMember().getUser();
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(context.getGuild());
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (queue.isEmpty()){
            channel.sendMessage("The queue is currently empty.").queue();
            return null;
        }

        final int trackCount = Math.min(queue.size(), 20);
        final List<AudioTrack> audioTracks = new ArrayList<>(queue);

        embedBuilder.setAuthor("Current Queued Tracks");
        embedBuilder.setColor(new Color(0x35A7C8));
        embedBuilder.setDescription("__There are " +
                audioTracks.size() +
                (audioTracks.size() > 1 ? " songs" : " song") +
                " queued:__\n");
        embedBuilder.setFooter("Requested by" + user.getName(), user.getAvatarUrl());
        embedBuilder.setTimestamp(new Date().toInstant().atOffset(ZoneOffset.UTC));

        for (int i = 0; i < trackCount; i++) {
            final AudioTrack track = audioTracks.get(i);
            final AudioTrackInfo trackInfo = track.getInfo();

            embedBuilder.appendDescription("**" + (i + 1) + ".** ")
                    .appendDescription(trackInfo.title)
                    .appendDescription(" by ")
                    .appendDescription('_' + trackInfo.author + '_')
                    .appendDescription(" `[")
                    .appendDescription(formatTime(track.getDuration()))
                    .appendDescription("]`\n");
        }

        if (audioTracks.size() > trackCount) {
            embedBuilder.appendDescription("And ")
                    .appendDescription(String.valueOf(audioTracks.size() - trackCount))
                    .appendDescription(" more...");
        }

        channel.sendMessage(embedBuilder.build()).queue();

        return null;
    }

    private String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getCategory() {
        return "MUSIC";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows the current queued up songs.";
    }
}
