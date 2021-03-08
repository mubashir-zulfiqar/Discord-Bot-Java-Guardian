//Developer: Mubashir Zulfiqar  Date: 2/25/2021  Time: 1:42 AM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.Moderation;

import com.google.api.services.youtube.model.SearchListResponse;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.deathvader.guardian_bot.Bot;
import me.deathvader.guardian_bot.Listener;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class Command_Embed implements Commands_Interface {
    private final EventWaiter waiter;

    public Command_Embed(EventWaiter eventWaiter) {
        this.waiter = eventWaiter;
    }

    @Override
    public SearchListResponse handle(Commands_Context context) {
        final TextChannel channel = context.getChannel();
        final Message message = context.getMessage();
        final User author = message.getAuthor();
        final Member member = context.getMember();
        final List<String> args = context.getArgs();
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final EmbedBuilder userEmbedBuilder = new EmbedBuilder();

        if (args.size() < 1 || message.getMentionedChannels().isEmpty()) {
            embedBuilder.setAuthor("Missing Arguments");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        final TextChannel targetChannel = message.getMentionedChannels().get(0);

        if (!member.hasPermission(Permission.MANAGE_CHANNEL)) {
            embedBuilder.setAuthor(member.getUser().getName() + " you don't have permission to use this command.");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        // Event Start
        embedBuilder.setAuthor("Embed Builder Wizard.");
        embedBuilder.setTitle("Embed Builder Wizard");
        embedBuilder.setDescription("This wizard will guide you to make a rich custom message and send it to a specific channel\n" +
                "The wizard will ask you to provide the information tap the start reaction to get started...");
        embedBuilder.setThumbnail("https://freepngimg.com/thumb/bulb/34138-7-idea-bulb-transparent.png");
        channel.sendMessage(embedBuilder.build()).queue((msg) -> {
            msg.addReaction("▶").queue();

            // Sending Options
            waiter.waitForEvent(MessageReactionAddEvent.class,
                    (e) -> Objects.requireNonNull(e.getMember()).getId().equals(author.getId()) && e.getChannel().equals(channel) && e.getReactionEmote().getEmoji().equals("▶"),
                    (e) -> {
                        menuEmbed(msg, embedBuilder);

                            // Receiving Answers
                            waiter.waitForEvent(MessageReactionAddEvent.class, (event) -> Objects.requireNonNull(event.getMember()).getId().equals(author.getId()) && event.getChannel().equals(channel), (event) -> {
                                if (event.getReactionEmote().getEmoji().equals("1️⃣")) {
                                    titleEmbed(msg, embedBuilder);

                                }
                            });
                        });
                    });

        return null;
    }

    private static void titleEmbed(Message message, EmbedBuilder embedBuilder){
        message.clearReactions().queue();
        embedBuilder.setTitle("Title", null);
        embedBuilder.setDescription("Title has two attributes: \n" +
                "1. Title\n" +
                "2. Url (to redirect on a specific url)");
        message.editMessage(embedBuilder.build()).queue();
        message.addReaction("1️⃣").queue();
        message.addReaction("2️⃣").queue();
    }

    private static void menuEmbed(Message message, EmbedBuilder embedBuilder) {
        message.clearReactions().queue();
        embedBuilder.setTitle("Embed options");
        embedBuilder.setDescription("You can set the following options in embed builder wizard:\n" +
                "1. Title\n" +
                "2. Author\n" +
                "3. Description\n" +
                "4. Thumbnail\n" +
                "5. Image\n" +
                "6. Color\n" +
                "7. Footer\n" +
                "_React with the number of option you want to add in your embed_");
        message.editMessage(embedBuilder.build()).queue((m) -> {
            message.addReaction("1️⃣").queue();
            message.addReaction("2️⃣").queue();
            message.addReaction("3️⃣").queue();
            message.addReaction("4️⃣").queue();
            message.addReaction("5️⃣").queue();
            message.addReaction("6️⃣").queue();
            message.addReaction("7️⃣").queue();
        });
    }

    @Override
    public String getName() {
        return "embed";
    }

    @Override
    public String getCategory() {
        return "MOD";
    }

    @Override
    public String getHelp(String prefix) {
        return "Embed a message in a channel\n" +
                "Usage: `" + prefix +"embed #channel-name`";
    }
}
