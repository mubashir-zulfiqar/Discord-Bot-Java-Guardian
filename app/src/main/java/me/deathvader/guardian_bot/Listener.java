package me.deathvader.guardian_bot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.deathvader.guardian_bot.database.DatabaseManager;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;

/**
 * @author Mubashir Zulfiqar
 * @Date: 2/10/2021
 * @Time: 10:57 PM
 * Happy Coding...*/

public class Listener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final Commands_Manager manager;
    private final EmbedBuilder embedBuilder = new EmbedBuilder();
    public static TextChannel lastMusicCmdChannel;

    public Listener(EventWaiter waiter) {
        manager = new Commands_Manager(waiter);
    }

    @SubscribeEvent
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }

    @SubscribeEvent
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        final List<TextChannel> dontDoThis = event.getGuild().getTextChannelsByName("bot-spam", true);

        if (dontDoThis.isEmpty()) {
            return;
        }

        final TextChannel pleaseDontDoThisAtAll = dontDoThis.get(0);

        final String useGuildSpecificSettingsInstead = String.format("Welcome %s to %s",
                event.getMember().getUser().getAsTag(), event.getGuild().getName());

        pleaseDontDoThisAtAll.sendMessage(useGuildSpecificSettingsInstead).queue();
    }

    @SubscribeEvent
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        final List<TextChannel> dontDoThis = event.getGuild().getTextChannelsByName("bot-spam", true);

        if (dontDoThis.isEmpty()) {
            return;
        }

        final TextChannel pleaseDontDoThisAtAll = dontDoThis.get(0);

        final String useGuildSpecificSettingsInstead = String.format("Goodbye %s",
                Objects.requireNonNull(event.getMember()).getUser().getAsTag());

        pleaseDontDoThisAtAll.sendMessage(useGuildSpecificSettingsInstead).queue();
    }



    @SubscribeEvent
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        //
    }

    @SubscribeEvent
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        //
    }

    @SubscribeEvent
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        //
    }

    @SubscribeEvent
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        final List<Member> memberList = event.getChannelLeft().getMembers();
        final Member bot = event.getGuild().getSelfMember();
        final AudioManager audioManager = event.getGuild().getAudioManager();
        final VoiceChannel connectedChannel = audioManager.getConnectedChannel();

        if (Objects.requireNonNull(bot.getVoiceState()).inVoiceChannel() && memberList.size() == 1) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    assert connectedChannel != null;
                    lastMusicCmdChannel.sendMessageFormat("I am idle for too long in <#%s>. So I am disconnecting from it.", connectedChannel.getId()).queue();
                    audioManager.closeAudioConnection();
                }
            }, 300000);
        }

    }

    @SubscribeEvent
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
        final Member bot = event.getGuild().getSelfMember();
        String botUrl = null;

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        final long guildId = event.getGuild().getIdLong();
        /*final ArrayList<String> guildBadWords = DatabaseManager.INSTANCE.getBadWords(guildId);
        if (!guildBadWords.isEmpty()) {
            dbData.BAD_WORDS.put(guildId, guildBadWords);
        }*/

        final Message message = event.getMessage();
        String prefix = dbData.PREFIXES.computeIfAbsent(guildId, DatabaseManager.INSTANCE::getPrefix);
        String raw_message = message.getContentRaw().toLowerCase();
        // ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(raw_message.split(" ")));

        /*dbData.BAD_WORDS.get(guildId).forEach(word -> {
            if (arrayList.contains(word)){
                message.delete().queue();
                event.getChannel().sendMessage( "<@" +
                        message.getAuthor().getId() +
                        "> you have used a swear word.\nYou have been warned 1/5 times").queue();
            }
        });*/

        if (!event.getAuthor().isBot() && raw_message.equals("<@!" + Bot.dotenv.get("BOT_ID") +">")) {
            final List<Guild> authorGuilds = user.getJDA().getGuilds();
            for (Guild guild : authorGuilds) {
                botUrl = !guild.getMembers().contains(bot) && Objects.requireNonNull(guild.getMember(user)).hasPermission(Permission.MANAGE_SERVER) ? null : "https://discordapp.com";
            }

            embedBuilder.setAuthor(bot.getUser().getName(), botUrl, bot.getUser().getAvatarUrl());
            embedBuilder.setTitle("Sup " + event.getAuthor().getName(), null);
            embedBuilder.setColor(new Color(0x6790c9));
            embedBuilder.setDescription("My Name is " + bot.getUser().getName() +
                    " and I am here to assist you. My Prefix is '" +
                    prefix + "'\nType " + prefix +
                    "botinfo to get more information about me.");

            embedBuilder.setFooter("Requested By " + user.getName(), event.getAuthor().getAvatarUrl());
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }

        if (raw_message.equalsIgnoreCase(prefix + "shutdown")
                && user.getId().equals(Bot.dotenv.get("DEVELOPER_ID"))) {
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("Temporarily Shutting Down For Maintenance\nHope to See you all Soon\nGood Bye").queue();
            LOGGER.info("Shutting Down");
            // event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());
            return;
        } else if (raw_message.equalsIgnoreCase(prefix + "shutdown")
                && !user.getId().equals(Bot.dotenv.get("DEVELOPER_ID"))){
            embedBuilder.setAuthor("Sorry, This Command can only be used by The **Developer**.");
            embedBuilder.setColor(new Color(0xf22b1d));
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }

        if (raw_message.startsWith(prefix)) {
            try {
                manager.handle(event, prefix);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
