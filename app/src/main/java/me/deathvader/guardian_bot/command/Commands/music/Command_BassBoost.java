//Developer: Mubashir Zulfiqar  Date: 3/13/2021  Time: 12:23 AM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.music;

import com.google.api.services.youtube.model.SearchListResponse;
import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import me.deathvader.guardian_bot.lavaplayer.GuildMusicManager;
import me.deathvader.guardian_bot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class Command_BassBoost implements Commands_Interface {

    private static final float[] BASS_BOOST = {0.2f, 0.15f, 0.1f, 0.05f, 0.0f, -0.05f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f,
            -0.1f, -0.1f, -0.1f, -0.1f};

    @Override
    public SearchListResponse handle(Commands_Context context) throws IOException {
        EqualizerFactory equalizerFactory = new EqualizerFactory();
        final Guild guild = context.getGuild();
        final List<String> args = context.getArgs();
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        audioPlayer.setFilterFactory(equalizerFactory);
        int count = 0;

        // https://github.com/natanbc/lavadsp
        // https://github.com/natanbc/andesite/tree/master
        // https://github.com/sedmelluq/lavaplayer
        // https://github.com/Devoxin/JukeBot/tree/ad7dd741d2b06f76529914dcf7b4e1faae937a14/src/main/java/jukebot

        if (args.get(0).equals("high")){
            for (float LVL :  BASS_BOOST){
                equalizerFactory.setGain(count, LVL + 2);
                count++;
            }
        }

        if (args.get(0).equals("low")){
            for (float LVL :  BASS_BOOST){
                equalizerFactory.setGain(count, LVL + 2);
                count++;
            }
        }

        return null;
    }

    @Override
    public String getName() {
        return "bassboost";
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
