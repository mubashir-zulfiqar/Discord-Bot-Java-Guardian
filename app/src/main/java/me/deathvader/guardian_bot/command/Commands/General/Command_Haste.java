//Developer: Mubashir Zulfiqar  Date: 2/11/2021  Time: 5:25 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.General;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import me.duncte123.botcommons.web.WebParserUtils;
import me.duncte123.botcommons.web.WebUtils;
import me.duncte123.botcommons.web.ContentType;
import net.dv8tion.jda.api.entities.TextChannel;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.function.Consumer;

public class Command_Haste implements Commands_Interface {
    private static final String HASTE_SERVER = "https://api.paste.gg/v1";

    @Override
    public SearchListResponse handle(Commands_Context context) {
        final TextChannel channel = context.getChannel();

        if (context.getArgs().isEmpty()) {
            channel.sendMessage("**Missing Arguments**").queue();
            return null;
        }

        final String invoke = this.getName();
        final String contentRaw = context.getMessage().getContentRaw();
        final int index = contentRaw.indexOf(invoke) + invoke.length();
        final String body = contentRaw.substring(index).trim();

        this.createPaste(body, (text) -> channel.sendMessage(text).queue());
        return null;
    }

    private void createPaste(String text, Consumer<String> callback) {
        Request request = WebUtils.defaultRequest()
                .post(RequestBody.create(ContentType.JSON.toMediaType(), text.getBytes()))
                .url(HASTE_SERVER + "documents")
                .addHeader("Content-Type", ContentType.TEXT_HTML.getType())
                .build();

        System.out.println(request);

        WebUtils.ins.prepareRaw(request, (result) -> WebParserUtils.toJSONObject(result, new ObjectMapper())).async(
                (json) -> {
                    String key = json.get("key").asText();
                    callback.accept(HASTE_SERVER + key);
                },
                (error) -> callback.accept("Error: " + error.getMessage())
        );
    }

    @Override
    public String getName() {
        return "haste";
    }

    @Override
    public String getCategory() {
        return GENERAL;
    }

    @Override
    public String getHelp(String prefix) {
        return "• This Command is Under Process" +
                "\nPosts some text to Haste Bin\n" +
                "Usage: " + prefix + "`haste [text/code]`";
    }
}