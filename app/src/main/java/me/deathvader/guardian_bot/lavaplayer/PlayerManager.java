//Developer: Mubashir Zulfiqar  Date: 3/3/2021  Time: 2:39 AM
//Happy Coding...

package me.deathvader.guardian_bot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildID) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    /*public void pause(TextChannel channel, boolean pause) {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        musicManager.scheduler.setPause(pause);
    }*/

    public boolean playerStatus(TextChannel channel) {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        return musicManager.scheduler.isPlaying();
    }

    public void loadAndPlay(TextChannel channel, String trackUrl) {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);
                channel.sendMessage("Adding to queue: `")
                        .append(track.getInfo().title)
                        .append(" by ")
                        .append(track.getInfo().author)
                        .append(" Length: ").append(String.valueOf(track.getInfo().length))
                        .append("`")
                        .queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                final List<AudioTrack> tracks = playlist.getTracks();

                channel.sendMessage("Adding to queue: `")
                        .append(String.valueOf(tracks.size()))
                        .append(" tracks from playlist ")
                        .append(playlist.getName())
                        .append("`")
                        .queue();

                for (final AudioTrack track : tracks)
                    musicManager.scheduler.queue(track);
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Cannot find a file to play in this URL.").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Cannot load from this URL.").queue();
            }
        });
    }

    private void skipTrack(TextChannel channel) {
        GuildMusicManager musicManager = getMusicManager(channel.getGuild());
        musicManager.scheduler.nextTrack();

        channel.sendMessage("Skipped to next track.").queue();
    }

    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}
