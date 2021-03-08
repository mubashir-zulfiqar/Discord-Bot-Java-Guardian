//Developer: Mubashir Zulfiqar  Date: 3/3/2021  Time: 12:08 AM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.music;

import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.Listener;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;

@SuppressWarnings("ConstantConditions")
public class Command_Join implements Commands_Interface {

    @Override
    public SearchListResponse handle(Commands_Context context) {
        final TextChannel channel = context.getChannel();
        final Member bot = context.getSelfMember();
        final GuildVoiceState botVoiceState = bot.getVoiceState();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        Listener.lastMusicCmdChannel = context.getChannel();

        if (botVoiceState.inVoiceChannel()) {
            embedBuilder.setAuthor("I am already in a Voice Channel");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        final Member member = context.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            embedBuilder.setAuthor("Please Join a voice Channel to use this command.");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        final AudioManager audioManager = context.getGuild().getAudioManager();
        final VoiceChannel memberVoiceChannel = memberVoiceState.getChannel();

        if (!bot.hasPermission(Permission.VOICE_CONNECT)) {
            embedBuilder.setAuthor("I don't have permission to join this " + memberVoiceChannel.getName());
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        embedBuilder.setAuthor("Connecting to \uD83D\uDD17 " + memberVoiceChannel.getName());
        embedBuilder.setColor(new Color(0x4792E2));
        channel.sendMessage(embedBuilder.build()).queue((msg) -> {
            audioManager.openAudioConnection(memberVoiceChannel);
            embedBuilder.setAuthor("Connected to \uD83D\uDD0A " + memberVoiceChannel.getName());
            embedBuilder.setColor(new Color(0x4792E2));
            msg.editMessage(embedBuilder.build()).queue();
        });
        return null;
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getCategory() {
        return "MUSIC";
    }

    @Override
    public String getHelp(String prefix) {
        return "Make the bot join your Voice Channel.\n" +
                "Usage: `" + prefix + "`";
    }
}
