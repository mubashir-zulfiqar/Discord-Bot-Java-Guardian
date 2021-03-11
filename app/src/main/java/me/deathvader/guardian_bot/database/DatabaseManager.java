//Developer: Mubashir Zulfiqar  Date: 3/2/2021  Time: 9:28 PM
//Happy Coding...

package me.deathvader.guardian_bot.database;

import java.util.ArrayList;

@SuppressWarnings("unused")
public interface DatabaseManager {
    DatabaseManager INSTANCE = new cloudClusterDataSource();

    /**
     *
     * @param guildID Guild id in which the member is in
     * @param memberID Member Id in the Guild
     * @param warningAmount The Amount of warning in 0/5 TODO Had to setup a method by which a user an set its warning limit and warning action[Mute(time), Ban, Kick]
     *
     */
    void setMemberWarnings(long guildID, long memberID, int warningAmount);

    int getMemberWarnings(long guildID, long memberID);

    /**
     * @param guildID Guild id in which the member is in
     * @return ArrayList of the bad words in the database
     */
    ArrayList<String> getBadWords(long guildID);

    /**
     * @param guildID Guild id in which the member is in
     * @param newPrefix New Prefix of the Guild to set in the database
     */
    void setBadWord(long guildID, String newPrefix);

    /**
     *
     * @param guildID Guild id in which the member is in
     * @return Prefix of the guild from the Database
     */
    String getPrefix(long guildID);

    /**
     *
     * @param guildID Guild id in which the member is in
     * @param prefix New prefix of the Guild to set in the database
     */
    void setPrefix(long guildID, String prefix);
}
