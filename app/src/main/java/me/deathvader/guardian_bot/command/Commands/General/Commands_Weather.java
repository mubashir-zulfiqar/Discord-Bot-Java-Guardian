//Developer: Mubashir Zulfiqar  Date: 3/8/2021  Time: 9:33 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.General;

import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;

import java.io.IOException;

public class Commands_Weather implements Commands_Interface {

    @Override
    public SearchListResponse handle(Commands_Context context) throws IOException {

        // https://stackoverflow.com/questions/12461730/find-weather-using-java
        // https://app.climacell.co/development/keys , https://docs.climacell.co/reference/community-projects ,
        // https://www.climacell.co/weather-api/?utm_adgroup=weather_api&utm_source=google&utm_medium=cpc&utm_campaign=API_-_Generic_-_Head_-_T3&utm_term=weather%20api&utm_content=502064081458&hsa_acc=4679135646&hsa_kw=weather%20api&hsa_net=adwords&hsa_cam=12080208890&hsa_ad=502064081458&hsa_grp=113215620181&hsa_src=g&hsa_mt=e&hsa_tgt=kwd-40383213246&hsa_ver=3&gclid=Cj0KCQiAs5eCBhCBARIsAEhk4r7v4u2wmY1ULTEkoWMuD9INzeEe6eRCKvHGCxWoPno6LxZ9WP0-PU4aApVZEALw_wcB


        return null;
    }

    @Override
    public String getName() {
        return "weather";
    }

    @Override
    public String getCategory() {
        return GENERAL;
    }

    @Override
    public String getHelp(String prefix) {
        return "In process.";
    }
}
