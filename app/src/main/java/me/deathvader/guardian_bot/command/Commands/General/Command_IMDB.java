//Developer: Mubashir Zulfiqar  Date: 2/24/2021  Time: 11:10 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.General;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.duncte123.botcommons.web.WebParserUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.Objects;

public class Command_IMDB {
    private static final OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) throws IOException {
        createRequest("game of thrones");
    }

    private static void createRequest(String text) throws IOException {
        Request request = new Request.Builder()
                .url("https://imdb8.p.rapidapi.com/auto-complete?q=" + text.replace(" ", "%20"))
                .get()
                .addHeader("x-rapidapi-key", "485e133508msh0efc58f6b489c1ap1a2a64jsn4bfb75bb3418")
                .addHeader("x-rapidapi-host", "imdb8.p.rapidapi.com")
                .build();


        /*WebUtils.ins.prepareRaw(request, (result) -> WebParserUtils.toJSONObject(result, new ObjectMapper())).async(
                (json) -> {
                    String key = json.get("key").asText();
                    callback.accept(HASTE_SERVER + key);
                },
                (error) -> callback.accept("Error: " + error.getMessage())
        );*/
        // Response response = client.newCall(request).execute();
        final JsonNode response = Objects.requireNonNull(WebParserUtils.toJSONObject(client.newCall(request).execute(), new ObjectMapper())).get("d");

        int count = 0;
        for (JsonNode singleResult : response) {
            System.out.println("\n" + (count++ + 1));
            System.out.println("Image Url: " + singleResult.get("i").get("imageUrl"));
            System.out.println(singleResult.get("i"));
            System.out.println(singleResult.get("id"));
            System.out.println(singleResult.get("l"));
            System.out.println(singleResult.get("q"));
            System.out.println(singleResult.get("rank"));
            System.out.println(singleResult.get("s"));
            System.out.println(singleResult.get("v"));
            System.out.println(singleResult.get("vt"));
            System.out.println(singleResult.get("y"));
            System.out.println(singleResult.get("yr"));
            /*for (JsonNode image : singleResult.get("i")){
                System.out.println(image.get("imageUrl"));
            }*/
        }
    }
}
