//Developer: Mubashir Zulfiqar  Date: 3/17/2021  Time: 8:39 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.music;

import com.google.api.services.youtube.model.SearchListResponse;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import me.deathvader.guardian_bot.dbData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.apache.commons.lang3.math.NumberUtils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"SpellCheckingInspection", "ConstantConditions"})
public class Command_MoveMe implements Commands_Interface {
    private final EventWaiter waiter;
    public Command_MoveMe(EventWaiter eventWaiter) {
        this.waiter = eventWaiter;
    }

    @Override
    public SearchListResponse handle(Commands_Context context) throws IOException {
        final List<String> args = context.getArgs();
        final Message message = context.getMessage();
        final Member member = message.getMember();
        final TextChannel channel = context.getChannel();
        final Guild guild = context.getGuild();
        final List<VoiceChannel> voiceChannels = guild.getVoiceChannels();
        final long guildId = guild.getIdLong();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (!member.getVoiceState().inVoiceChannel()) {
            channel.sendMessage("Please join a voice channel first.").queue();
            return null;
        }

        if (args.size() < 1){
            channel.sendMessage("Please Enter the name of channel to which you want to be moved.").queue();
            return null;
        }

        ArrayList<VoiceChannel> targetVoiceChannels = new ArrayList<>();
        for (VoiceChannel voiceChannel : voiceChannels) {
            if (voiceChannel.getName().equals(args.get(0))) {
                targetVoiceChannels.add(voiceChannel);
            }
        }

        if (targetVoiceChannels.isEmpty()){
            channel.sendMessage("There isn't any channel with this name in the server").queue();
            return null;
        }

        if (NumberUtils.isParsable(args.get(0))) {
            guild.moveVoiceMember(member, guild.getVoiceChannelById(args.get(0))).queue();
            return null;
        }

        if (targetVoiceChannels.size() > 1) {
            embedBuilder.setAuthor("Error in moving to another Voice Channel.");
            embedBuilder.setDescription("""
                    There are more than one voice channels with the same name in this Server.
                    So please enter the id of the channel please.
                    """);
            embedBuilder.appendDescription("\n**There are [" +
                    targetVoiceChannels.size()+
                    "] voice channels in this server with the name `" +
                    args.get(0) + "`.**\n");
            embedBuilder.setColor(new Color(0xD92C2C));
            targetVoiceChannels.forEach(targetVoiceChannel ->
                embedBuilder.appendDescription("**>** Name: " + targetVoiceChannel.getName() +
                        ", Id: " + targetVoiceChannel.getId() + ".\n"));
            embedBuilder.appendDescription("\nUsage: `" + dbData.PREFIXES.get(guildId) + "moveme Voice-Channel-Id`.\n\n" +
                    "If you don't see or you don't know how to get the id of a channel react with ❓ on this message for help.");
            channel.sendMessage(embedBuilder.build()).queue(msg -> {
                msg.addReaction("❓").queue(); // \u2753
                this.waiter.waitForEvent(
                        GuildMessageReactionAddEvent.class,
                        (e) -> e.getMessageIdLong() == msg.getIdLong() && !e.getUser().isBot() && e.getReactionEmote().getEmoji().equals("❓"),
                        (e) -> {
                            embedBuilder.clear();
                            embedBuilder.setAuthor("Step 1");
                            embedBuilder.setDescription("Right click on the channel you want to move in and select copy id.");
                            embedBuilder.setImage("https://i.imgur.com/LTYpxVO.jpg");
                            msg.editMessage(embedBuilder.build()).queue(msg2 -> {
                                msg2.clearReactions().queue();
                                msg2.addReaction("➡").queue();
                                this.waiter.waitForEvent(
                                        GuildMessageReactionAddEvent.class,
                                        (e2) -> e2.getMessageIdLong() == msg2.getIdLong() && !e2.getUser().isBot() && e2.getReactionEmote().getEmoji().equals("➡"),
                                        (e2) -> {
                                            embedBuilder.clear();
                                            embedBuilder.setAuthor("Step 2");
                                            embedBuilder.setDescription("""
                                                    If you don't see copy id in the menu.
                                                    You need to enabel it from the settings.
                                                    Goto user settings.""");
                                            embedBuilder.setImage("https://i.imgur.com/f3pPt4V.jpg");
                                            msg2.editMessage(embedBuilder.build()).queue(msg3 -> {
                                                msg3.clearReactions().queue();
                                                msg3.addReaction("➡").queue();
                                                this.waiter.waitForEvent(
                                                        GuildMessageReactionAddEvent.class,
                                                        (e3) -> e3.getMessageIdLong() == msg3.getIdLong() && !e3.getUser().isBot() && e3.getReactionEmote().getEmoji().equals("➡"),
                                                        (e3) -> {
                                                            embedBuilder.clear();
                                                            embedBuilder.setAuthor("Step 3");
                                                            embedBuilder.setDescription("In App Setting > Appearance > Enable Developer Mode");
                                                            embedBuilder.setImage("https://i.imgur.com/sg19DaQ.jpg");
                                                            msg3.clearReactions().queue();
                                                            msg3.editMessage(embedBuilder.build()).queue();
                                                        }
                                                );
                                            });
                                        },60L, TimeUnit.SECONDS, () -> msg2.reply("Time Out.").queue()
                                );
                            });
                        },60L, TimeUnit.SECONDS, () -> msg.reply("Time Out.").queue()
                );
            });
            return null;
        }

        guild.moveVoiceMember(member, targetVoiceChannels.get(0)).queue();
        return null;
    }

    @Override
    public String getName() {
        return "moveme";
    }

    @Override
    public String getCategory() {
        return MUSIC;
    }

    @Override
    public String getHelp(String prefix) {
        return "Move you to anothre Voice Channel.\n" +
                "Usage: `" + prefix + "moveme [Voice-Channle-Name / Voice-Channel-Id]`.";
    }
}
