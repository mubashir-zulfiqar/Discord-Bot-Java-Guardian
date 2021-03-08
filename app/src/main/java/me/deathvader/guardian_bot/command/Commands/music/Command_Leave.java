//Developer: Mubashir Zulfiqar  Date: 3/3/2021  Time: 5:41 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.music;

import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.Listener;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import me.deathvader.guardian_bot.lavaplayer.GuildMusicManager;
import me.deathvader.guardian_bot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Objects;

public class Command_Leave implements Commands_Interface {
    @Override
    public SearchListResponse handle(Commands_Context context) {
        final boolean inVoiceChannel = Objects.requireNonNull(context.getGuild().getSelfMember().getVoiceState()).inVoiceChannel();
        final AudioManager audioManager = context.getGuild().getAudioManager();
        Listener.lastMusicCmdChannel = context.getChannel();
        final Message message = context.getMessage();

        if (!inVoiceChannel){
            message.reply("I am not connected to a voice channel!").queue();
            return null;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(context.getGuild());

        musicManager.scheduler.repeating = false;
        musicManager.scheduler.queue.clear();
        musicManager.scheduler.player.stopTrack();
        audioManager.closeAudioConnection();

        final VoiceChannel botVoiceChannel = context.getGuild().getSelfMember().getVoiceState().getChannel();

        assert botVoiceChannel != null;
        context.getChannel().sendMessageFormat("I have left the voice Channel <#%s>.", botVoiceChannel.getId()).queue();
        return null;
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getCategory() {
        return "MUSIC";
    }

    @Override
    public String getHelp(String prefix) {
        return "Makes the bot to leave the voice Channel.\n" +
                "Usage: `" + prefix + "leave`.";
    }
}
