//Developer: Mubashir Zulfiqar  Date: 2/15/2021  Time: 12:37 AM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.General;

import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import me.deathvader.guardian_bot.dbData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Command_ScreenShot implements Commands_Interface {

    @Override
    public SearchListResponse handle(Commands_Context context) {
        final List<String> args = context.getArgs();
        final Message message = context.getMessage();
        final TextChannel channel = context.getChannel();
        final EmbedBuilder embedBuilder = new EmbedBuilder();

        if (args.size() < 1 || args.size() > 2) {
            embedBuilder.setAuthor("Arguments Error.");
            embedBuilder.setDescription(getHelp(dbData.PREFIXES.get(context.getGuild().getIdLong())));
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
            return null;
        }

        // Example For Help: https://www.javacodegeeks.com/2012/09/simple-rest-client-in-java.html

        final String URL;

        if (args.get(0).startsWith("http"))
            URL = args.get(0);
        else
            URL = "https://" + args.get(0);


        final String SSL_API_KEY = "95b4783c2ac62652e0014c9b7c12e97d";
        final String SSL_SECRET_KEY = "guardian-jda-bot-key";
        final String viewPort = "2560x1440";
        final String width = "1920";
        final String SECRET_KEY = getMd5(URL + SSL_SECRET_KEY);
        final String SSL_API = "http://api.screenshotlayer.com/api/capture?access_key=" + SSL_API_KEY + "&url=" + URL + "&viewport=" + viewPort + "&width=" + width + "&delay=2" + "&secret_key=" + SECRET_KEY + "&format=JPG";

        message.delete().queue();
        channel.sendMessage(SSL_API).queue();
        return null;
    }

    public static String getMd5(String input)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            StringBuilder hashText = new StringBuilder(no.toString(16));
            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }
            return hashText.toString();
        }

        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return "screenshot";
    }

    @Override
    public String getCategory() {
        return "GENERAL";
    }

    @Override
    public String getHelp(String prefix) {
        return "Get a screenshot of a specific Web page\n" +
                "Usage: `" + prefix + "screenshot [url]`.";
    }

    @Override
    public List<String> getAliases() {
        return List.of("ss", "capture");
    }
}
