//Developer: Mubashir Zulfiqar  Date: 3/3/2021  Time: 7:43 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.music;

import com.google.api.services.youtube.model.SearchListResponse;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import me.deathvader.guardian_bot.lavaplayer.GuildMusicManager;
import me.deathvader.guardian_bot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Objects;


@SuppressWarnings("CommentedOutCode")
public class Command_Pause implements Commands_Interface {

    @Override
    public SearchListResponse handle(Commands_Context context) {
        final TextChannel channel = context.getChannel();
        final boolean isBotInVc = Objects.requireNonNull(context.getGuild().getSelfMember().getVoiceState()).inVoiceChannel();
        // final AudioManager audioManager = context.getGuild().getAudioManager();
        // final PlayerManager playerManager = PlayerManager.getInstance();

        if (!isBotInVc) {
            channel.sendMessage("I am not in any channel").queue();
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(context.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.isPaused()){
            channel.sendMessage("Song is already paused.").queue();
        }

        audioPlayer.setPaused(true);
        channel.sendMessage("Song is Paused.").queue();
        return null;
    }

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String getCategory() {
        return "MUSIC";
    }

    @Override
    public String getHelp(String prefix) {
        return "Pause the current Song.\n" +
                "Usage: `" + prefix + "pause`.";
    }
}
