//Developer: Mubashir Zulfiqar  Date: 2/27/2021  Time: 9:44 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.Commands.Testing;

import com.google.api.services.youtube.model.SearchListResponse;
import me.deathvader.guardian_bot.command.Commands_Context;
import me.deathvader.guardian_bot.command.Commands_Interface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.List;

public class Command_Testing implements Commands_Interface {

    @Override
    public SearchListResponse handle(Commands_Context context) {
        final TextChannel channel = context.getChannel();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        final Member member = context.getMember();

        if (!member.hasPermission(Permission.ADMINISTRATOR)){
            embedBuilder.setAuthor("This Command can only be used by Administrators.");
            embedBuilder.setColor(new Color(0xf22b1d));
            channel.sendMessage(embedBuilder.build()).queue();
        } // Test if user is admin or not

        channel.sendMessage("Emote List").queue();

        final List<Emote> emoteList = context.getGuild().getEmotes();

        // <:jda:817360288293716009>
        // emoteList.stream().map(Emote::getId).
        String emotes = null;
        for (Emote emote : emoteList) {
            emotes = String.join(" **|** ", emote.isAnimated() ? "<a:" : "<:" + emote.getName() + ":" + emote.getId() + ">\n");
        }
        assert emotes != null;
        channel.sendMessage(emotes).queue();

        // channel.sendMessage("<:" + emote.getName() + ":" + emote.getId() + ">").queue()
       channel.sendMessage("<:KurumiThonk:806334418233851955>").queue();


        /*
         Loading Giff's
         https://i.pinimg.com/originals/7b/73/6a/7b736a33be802fc2e737e3df56b4ef0e.gif
         https://i.pinimg.com/originals/49/c4/75/49c47592c39596189d33ffca3544313d.gif
         https://i.pinimg.com/originals/f9/41/ae/f941ae9d16fd7d2957eea6e5b1100d1e.gif
         https://cdn.dribbble.com/users/1803770/screenshots/4277046/my-first-gif.gif
         https://linustechtips.com/uploads/monthly_2016_06/krizzghfqegk0fv0g3ey.gif.39cedd1a0b5944180324665a30d30ced.thumb.gif.f6bf6ded1a94ac488bc6cda2646b247a.gif
         https://i.pinimg.com/originals/37/cf/a9/37cfa953bb7a0394881fa123361fa5e7.gif Mute Icon
         https://media.giphy.com/media/qEv4UXqSP5jXy/giphy.gif
        */

        return null;
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getHelp(String prefix) {
        return "testing purpose";
    }

    @Override
    public String getCategory() {
        return TEST;
    }

}
