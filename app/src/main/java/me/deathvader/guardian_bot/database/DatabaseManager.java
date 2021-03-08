//Developer: Mubashir Zulfiqar  Date: 3/2/2021  Time: 9:28 PM
//Happy Coding...

package me.deathvader.guardian_bot.database;

public interface DatabaseManager {
    DatabaseManager INSTANCE = new SqlDataSource();
    String getPrefix(long guildID);
    void setPrefix(long guildID, String newPrefix);
}
