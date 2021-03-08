//Developer: Mubashir Zulfiqar  Date: 2/15/2021  Time: 5:42 AM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.General;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import me.deathvader.guardian_bot.command.Commands_Context;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

public class Youtube_Search {
    private static final String PROPERTIES_FILENAME = "youtube.properties";
    private static final long NUMBER_OF_VIDEOS_RETURNED = 5;
    private static String QUERY_TERM = "";
    private static Commands_Context context;
    private static final EmbedBuilder embedBuilder = new EmbedBuilder();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final String CREDENTIALS_DIRECTORY = ".oauth-credentials";

    public Youtube_Search(String QUERY_TERM, Commands_Context context) {
        Youtube_Search.QUERY_TERM = QUERY_TERM;
        Youtube_Search.context = context;
    }

    public List<SearchResult> search() {
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
            YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, request -> {
            }).setApplicationName("youtube-cmdline-search-sample").build();
            
            YouTube.Search.List search = youtube.search().list("id,snippet");
            String apiKey = properties.getProperty("youtube.apikey");
            search.setKey(apiKey);
            search.setQ(QUERY_TERM);
            search.setType("video");
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {
                return prettyPrint(searchResultList, context);
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    private static List<SearchResult> prettyPrint(List<SearchResult> searchResultList, Commands_Context context) {

        final Iterator<SearchResult> iteratorSearchResults = searchResultList.iterator();

        if (!iteratorSearchResults.hasNext()) {
            embedBuilder.setAuthor("There aren't any results for your query.");
            embedBuilder.setColor(new Color(0xf22b1d));
            context.getChannel().sendMessage(embedBuilder.build()).queue();
            return null;
        }

        int sNo = 0;

        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();
            Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
            final String description = singleVideo.getSnippet().getDescription();
            final String title = singleVideo.getSnippet().getTitle();

            sNo++;

            if (rId.getKind().equals("youtube#video")) {
                embedBuilder.setAuthor("Search Result " + sNo, null ,null);
                embedBuilder.setColor(new Color(0xFF0000));
                embedBuilder.setTitle(title, "https://www.youtube.com/watch?v=" + singleVideo.getId().getVideoId());
                embedBuilder.setThumbnail("http://www.gstatic.com/youtube/img/branding/youtubelogo/1x/youtubelogo_30.png");
                embedBuilder.setImage(thumbnail.getUrl());
                embedBuilder.setDescription(description);
                context.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        }
        return searchResultList;
    }
}
