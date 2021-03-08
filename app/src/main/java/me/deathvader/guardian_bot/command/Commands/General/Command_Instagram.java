//Developer: Mubashir Zulfiqar  Date: 2/12/2021  Time: 9:26 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.General;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Collections;
import java.util.List;

public class Command_Instagram implements Commands_Interface {

    @Override
    public SearchListResponse handle(Commands_Context context) {
        final List<String> args = context.getArgs();
        final TextChannel channel = context.getChannel();

        if (args.isEmpty()) {
            channel.sendMessage("You must provide a username to look up").queue();
            return null;
        }

        final String usn = args.get(0);

        WebUtils.ins.getJSONObject("https://apis.duncte123.me/insta/" + usn).async((json) -> {
            if (!json.get("success").asBoolean()) {
                channel.sendMessage(json.get("error").get("message").asText()).queue();
                return;
            }

            final JsonNode user = json.get("user");
            final String username = user.get("username").asText();
            final String pfp = user.get("profile_pic_url").asText();
            final String biography = user.get("biography").asText();
            final boolean isPrivate = user.get("is_private").asBoolean();
            final int following = user.get("following").get("count").asInt();
            final int followers = user.get("followers").get("count").asInt();
            final int uploads = user.get("uploads").get("count").asInt();

            final EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Instagram info of " + username, "https://www.instagram.com/" + username);
            embedBuilder.setThumbnail(pfp);
            embedBuilder.setDescription(String.format(
                    "**Private account:** %s\n**Bio:** %s\n**Following:** %s\n**Followers:** %s\n**Uploads:** %s",
                    toEmote(isPrivate),
                    biography,
                    following,
                    followers,
                    uploads
            ));
            embedBuilder.setImage(getLatestImage(json.get("images")));
            channel.sendMessage(embedBuilder.build()).queue();
        });
        return null;
    }

    private String getLatestImage(JsonNode json) {
        if (!json.isArray()) {
            return null;
        }

        if (json.size() == 0) {
            return null;
        }

        return json.get(0).get("url").asText();
    }

    private String toEmote(boolean bool) {
        return bool ? "<:sliderRight:582718257598038017>" : "<:sliderLeft:582718257866473472>";
    }

    @Override
    public String getName() {
        return "instagram";
    }

    @Override
    public String getCategory() {
        return "GENERAL";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows instagram statistics of a user with the latest image\n" +
                "Usage: `" + prefix + "instagram [username]`";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("insta");
    }
}
