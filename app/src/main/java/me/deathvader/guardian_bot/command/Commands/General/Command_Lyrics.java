//Developer: Mubashir Zulfiqar  Date: 2/27/2021  Time: 8:41 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.General;

import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import me.deathvader.guardian_bot.command.lyrics.LyricsInfo;
import me.deathvader.guardian_bot.command.lyrics.LyricsParser;
import me.deathvader.guardian_bot.command.lyrics.Lyrics_Search;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.awaitility.Awaitility;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Command_Lyrics implements Commands_Interface {

    @Override
    public SearchListResponse handle(Commands_Context context) throws IOException {
        final List<String> args = context.getArgs();
        final TextChannel channel = context.getChannel();
        final String PROPERTIES_FILENAME = "youtube.properties";

        HashMap<Long, String> map;
        LyricsParser lp = new LyricsParser();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        LyricsInfo lyricsInfo = null;

        Properties properties = new Properties();
        try {
            InputStream input = new FileInputStream("youtube.properties");
            properties.load(input);

        } catch (IOException | NullPointerException e) {
            System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
                    + " : " + e.getMessage());
            System.exit(1);
        }

        try {
            Lyrics_Search.search(String.join(" ", args));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Awaitility.await().atMost(20, TimeUnit.SECONDS).until(() -> (!properties.get("downloaded").equals("true")));

        File file = new File("C:\\Users\\mubas\\IdeaProjects\\Discord-Bot-Java-Guardian\\app\\src\\main\\java\\me\\deathvader\\guardian_bot\\webCrawler\\lyrics.lrc");
        final int[] colors = {0xfc0303, 0xfc3503, 0xfc7703, 0xfcb503, 0xfce703, 0xdbfc03,
                0xadfc03, 0x62fc03, 0x18fc03, 0x03fc20, 0x03fc45, 0x03fc7b, 0x03fcad, 0x03fce7, 0x03dbfc,
                0x0398fc, 0x036ffc, 0x036bfc, 0x0320fc, 0x3d03fc, 0x7703fc, 0xb103fc, 0xe703fc, 0xfc03ca, 0xfc0384,
                0xfc0345, 0xfc031c};
        Random random = new Random();

        try {
            lyricsInfo = lp.parser(file.getPath());
        } catch (Exception e) {
            System.out.println("parser error");
            e.printStackTrace();
        }

        assert lyricsInfo != null;
        map = lyricsInfo.getInfo();

        embedBuilder.setAuthor("Song name"); // url = "song icon"

        channel.sendMessage("Starting " + lyricsInfo.getTitle() + " lyrics").queue((msg) -> {
            long elapsedTime = 0;
            int count = random.nextInt(colors.length);
            for (long x : lp.time){
                try {
                    Thread.sleep(x - elapsedTime);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
                if (count >= colors.length)
                    count = 0;
                embedBuilder.setColor(new Color(colors[count]));
                embedBuilder.setDescription(map.get(x));
                msg.editMessage(embedBuilder.build()).queue();
                elapsedTime += x - elapsedTime;
                count++;
            }
            embedBuilder.setDescription("**The End.**");
            msg.editMessage(embedBuilder.build()).queue();
            msg.delete().queueAfter(2, TimeUnit.SECONDS);
            System.out.println(file.delete());
        });
        return null;
    }

    @Override
    public String getName() {
        return "lyrics";
    }

    @Override
    public String getCategory() {
        return "GENERAL";
    }

    @Override
    public String getHelp(String prefix) {
        return "search lyrics of the given song\n" +
                "Usage: `" + prefix + "lyrics`.";
    }
}
