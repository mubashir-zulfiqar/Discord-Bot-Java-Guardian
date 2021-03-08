//Developer: Mubashir Zulfiqar  Date: 3/8/2021  Time: 8:40 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.music;

import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import me.deathvader.guardian_bot.lavaplayer.GuildMusicManager;
import me.deathvader.guardian_bot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Command_Add implements Commands_Interface {
    @Override
    public SearchListResponse handle(Commands_Context context) throws IOException {
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(context.getGuild());
        final TextChannel channel = context.getChannel();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        String link = String.join(" ", context.getArgs());

        if (!isUrl(link)) {
            //noinspection SpellCheckingInspection
            link = "ytsearch:" + link;
        }

        if (musicManager.audioPlayer.getPlayingTrack() == null){
            PlayerManager.getInstance().loadAndPlay(channel, link);
            musicManager.audioPlayer.setPaused(true);
            embedBuilder.setAuthor("Song has been successfully added to the player because the queue is already empty.");
            embedBuilder.setColor(new Color(0x4BA2E3));
            return null;
        }

        final String songTitle = musicManager.audioPlayer.getPlayingTrack().getInfo().title;
        PlayerManager.getInstance().loadAndPlay(channel, link);
        embedBuilder.setAuthor(songTitle + " has been successfully added to the queue.");
        embedBuilder.setColor(new Color(0x4BA2E3));
        return null;
    }

    private boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException exception) {
            return false;
        }
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getCategory() {
        return MUSIC;
    }

    @Override
    public String getHelp(String prefix) {
        return "Add a song into the queue\n" +
                "Usage: `" + prefix + "add`.";
    }
}
