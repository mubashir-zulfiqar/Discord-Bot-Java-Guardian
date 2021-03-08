//Developer: Mubashir Zulfiqar  Date: 3/3/2021  Time: 3:04 AM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.music;

import com.google.api.services.youtube.model.SearchListResponse;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.deathvader.guardian_bot.Listener;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import me.deathvader.guardian_bot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings("ConstantConditions")
public class Command_Play implements Commands_Interface {

    @Override
    public SearchListResponse handle(Commands_Context context) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        final TextChannel channel = context.getChannel();
        final Member bot = context.getSelfMember();
        final GuildVoiceState botVoiceState = bot.getVoiceState();
        final AudioManager audioManager = context.getGuild().getAudioManager();
        final VoiceChannel memberVoiceChannel = context.getMember().getVoiceState().getChannel();
        final AudioPlayer audioPlayer = PlayerManager.getInstance().getMusicManager(context.getGuild()).audioPlayer;

        if (context.getArgs().isEmpty() && audioPlayer.getPlayingTrack() == null) {
            embedBuilder.setAuthor("Missing Arguments.");
            embedBuilder.setDescription("Type `$help play` for more information.");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        final Member member = context.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        Listener.lastMusicCmdChannel = context.getChannel();

        if (!memberVoiceState.inVoiceChannel()) {
            embedBuilder.setAuthor("Please Join a voice Channel to use this command.");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        if (!memberVoiceState.getChannel().equals(botVoiceState.getChannel()) && botVoiceState.inVoiceChannel()){
            embedBuilder.setAuthor("You need to be in the same voice channel as me to use this command.");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        if (audioPlayer.getPlayingTrack() != null && audioPlayer.isPaused()) {
            audioPlayer.setPaused(false);
        } else if(audioPlayer.getPlayingTrack() != null && !audioPlayer.isPaused()) { // TODO Checking Purpose.
            embedBuilder.setAuthor("Missing Arguments.");
            embedBuilder.setDescription("Type `$help play` for more information.");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }else {
            String link = String.join(" ", context.getArgs());

            if (!isUrl(link)) {
                //noinspection SpellCheckingInspection
                link = "ytsearch:" + link;
            }

            if (!botVoiceState.inVoiceChannel()){
                audioManager.openAudioConnection(memberVoiceChannel);
            }

            PlayerManager.getInstance().loadAndPlay(channel, link);
        }

        return null;
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getCategory() {
        return "MUSIC";
    }

    @Override
    public String getHelp(String prefix) {
        return "Plays a Song.\n" +
                "Usage `" + prefix + "play [youtube video URL/Title(plays a playlist.)]`.";
    }

    private boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException exception) {
            return false;
        }
    }
}
