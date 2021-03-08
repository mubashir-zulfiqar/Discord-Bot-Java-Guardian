//Developer: Mubashir Zulfiqar  Date: 2/27/2021  Time: 3:54 AM
//Happy Coding...

package me.deathvader.guardian_bot.command.lyrics;

import me.deathvader.guardian_bot.webCrawler.Python_Interpreter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Lyrics_Search {
    public static void search(String query) throws Exception {
        String url = "https://www.megalobiz.com/search/all?qry=" + URLEncoder.encode(query, StandardCharsets.UTF_8) + "&searchButton.x=0&searchButton.y=0";
        try {
            File file = new File("app/src/main/java/me/deathvader/guardian_bot/webCrawler" + "/link.txt");
            FileWriter writer = new FileWriter(file);
            writer.write(url);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Python_Interpreter.run();
    }
}
