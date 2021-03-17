//Developer: Mubashir Zulfiqar  Date: 3/12/2021  Time: 11:07 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.music;

import com.google.api.services.youtube.model.SearchListResponse;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import me.deathvader.guardian_bot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class Command_Volume implements Commands_Interface {

    @Override
    public SearchListResponse handle(Commands_Context context) throws IOException {
        final TextChannel channel = context.getChannel();
        final List<String> args = context.getArgs();
        final AudioPlayer audioPlayer = PlayerManager.getInstance().getMusicManager(context.getGuild()).audioPlayer;

        if (audioPlayer.getPlayingTrack() == null) {
            channel.sendMessage("There is nothing playing right now.").queue();
            return null;
        }

        if (args.isEmpty()) {
            channel.sendMessageFormat("Current Player volume is [%d]", audioPlayer.getVolume()).queue();
        }

        if (args.size() >= 1) {
            try {
                final int newVolume = Integer.parseInt(args.get(0));
                if (newVolume > 100 || newVolume < 1){
                    channel.sendMessage("Volume can be from 1-100.").queue();
                    return null;
                }
                audioPlayer.setVolume(newVolume);
            } catch (NumberFormatException e){
                channel.sendMessage("Please enter a number b/w [1-100]").queue();
            }
        }

        return null;
    }

    @Override
    public String getName() {
        return "volume";
    }

    @Override
    public String getCategory() {
        return MUSIC;
    }

    @Override
    public String getHelp(String prefix) {
        return "Enable Bass Boost in the Player.\n" +
                "Usage: `" + prefix + "bassbost [amount/off]";
    }
}
