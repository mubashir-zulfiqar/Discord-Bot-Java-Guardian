//Developer: Mubashir Zulfiqar  Date: 2/26/2021  Time: 11:07 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.lyrics;

import java.util.HashMap;

/**
 * Class for encapsulating lyric information
 * @author Mubashir Zulfiqar
*/


public class LyricsInfo {
    private String title; //song name
    private String singer; //singer name
    private String album; //album name

    private String length; //song length

    private HashMap<Long,String> info; //Save the lyrics information and the point-in-time map

    //The following is getter() setter()
    public String getTitle() {
        return title;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public HashMap<Long, String> getInfo() {
        return info;
    }

    public void setInfo(HashMap<Long, String> info) {
        this.info = info;
    }


}
