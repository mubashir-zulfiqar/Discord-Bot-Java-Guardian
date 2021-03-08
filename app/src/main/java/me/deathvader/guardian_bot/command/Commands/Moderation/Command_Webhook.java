//Developer: Mubashir Zulfiqar  Date: 2/12/2021  Time: 5:14 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.Moderation;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.Bot;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Command_Webhook implements Commands_Interface {
    private final WebhookClient client;
    private final EmbedBuilder embedBuilder = new EmbedBuilder();


    public Command_Webhook() {
        WebhookClientBuilder webhookBuilder = new WebhookClientBuilder(Bot.dotenv.get("TEMP-CHAT_WEBHOOK_URL"));
        webhookBuilder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Webhook-Thread");
            thread.setDaemon(true);
            return thread;
        });

        this.client = webhookBuilder.build();
    }

    @Override
    public SearchListResponse handle(Commands_Context context) {
        final List<String> args = context.getArgs();
        final TextChannel channel = context.getChannel();
        final User user = context.getAuthor();

        if (!context.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("You Don't have PERMISSION to use this command.");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        if (args.isEmpty()) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("Missing Arguments");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        context.getGuild().getChannels().forEach(guildChannel ->
                channel.retrieveWebhooks().queue(webhooks ->
                        webhooks.forEach(webhook -> {
                            if (webhook.getUrl().length() > 60){
                                System.out.println("Channel Name: " +
                                        guildChannel.getName() +
                                        " URL: " + webhook.getUrl());
                            }
                        })));

        /*final RestAction<List<Webhook>> listRestAction = context.getGuild().retrieveWebhooks();
        listRestAction.queue(element -> {
            element.forEach(e -> System.out.println("Webhook: " +e.getUrl()));
        });*/


        WebhookMessageBuilder webhookMessageBuilder = new WebhookMessageBuilder();
        webhookMessageBuilder.setUsername(user.getName());
        webhookMessageBuilder.setAvatarUrl(user.getEffectiveAvatarUrl().replaceFirst("gif", "png") + "?size=512");
        webhookMessageBuilder.setContent(String.join(" ", args));
        client.send(webhookMessageBuilder.build());
        embedBuilder.setAuthor(user.getName() + " your message has been sent to <#" + client.getClass().getName() + ">.", user.getAvatarUrl());
        channel.sendMessage(embedBuilder.build()).queue( (botMessage) -> botMessage.delete().queueAfter(5, TimeUnit.SECONDS));
        return null;
    }

    @Override
    public String getName() {
        return "webhook";
    }

    @Override
    public String getCategory() {
        return MODERATION;
    }

    @Override
    public String getHelp(String prefix) {
        return "Send a webhook message as your name\n" +
                "Usage: `"+ prefix + "webhook [message]`";
    }
}
