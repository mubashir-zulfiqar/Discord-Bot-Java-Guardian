//Developer: Mubashir Zulfiqar  Date: 2/11/2021  Time: 1:19 AM
//Happy Coding...

package me.deathvader.guardian_bot.command;

import com.google.api.services.youtube.model.SearchListResponse;

import java.io.IOException;
import java.util.List;

public interface Commands_Interface {

    String GENERAL = "GENERAL";
    String FUN = "FUN";
    String MODERATION = "MOD";
    String MUSIC = "MUSIC";
    String TEST = "TESTING";

    SearchListResponse handle(Commands_Context context) throws IOException;

    String getName();

    String getCategory();

    String getHelp(String prefix);

    default List<String> getAliases() {
        return List.of();
    }
}
