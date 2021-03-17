//Developer: Mubashir Zulfiqar  Date: 3/2/2021  Time: 10:06 PM
//Happy Coding...

package me.deathvader.guardian_bot.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.deathvader.guardian_bot.Bot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;

public class cloudClusterDataSource implements DatabaseManager{
    private static final Logger LOGGER = LoggerFactory.getLogger(cloudClusterDataSource.class);
    String configFile = "db.properties";
    HikariConfig cfg = new HikariConfig(configFile);
    HikariDataSource ds = new HikariDataSource(cfg);

    public cloudClusterDataSource() {
        try {
            if (ds.getConnection() != null) {
                LOGGER.info("Successfully Connected to Database.");
            } else {
                LOGGER.error("Can not Connect to Database.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try (final Statement statement = getConnection().createStatement()) {
            final String botPrefix = Bot.BOT_PREFIX;

            try (final PreparedStatement preparedStatement = getConnection().prepareStatement("SHOW TABLES")){
                try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()){
                        // System.out.println(resultSet.getString("Tables_in_guardian_bot"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // For Guild Prefix
            statement.addBatch("CREATE TABLE IF NOT EXISTS guild_settings (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "guild_id VARCHAR(20) NOT NULL," +
                    "prefix VARCHAR(255) NOT NULL DEFAULT '" + botPrefix + "'" +
                    ");");

            // For Guild Bad Words
            statement.addBatch("CREATE TABLE IF NOT EXISTS bad_words (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "guild_id VARCHAR(20) NOT NULL," +
                    "word VARCHAR(255) NOT NULL" +
                    ");");

            // For Guild Members and there Warnings
            statement.addBatch("CREATE TABLE IF NOT EXISTS guild_members (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "guild_id VARCHAR(20) NOT NULL," +
                    "member_id VARCHAR(255) NOT NULL," +
                    "member_warnings INTEGER(5) NULL" +
                    ");");

            statement.executeBatch();
            LOGGER.info("Tables are Initialised.");
        } catch (SQLException e) {
            LOGGER.warn("Error in setting up Database.", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    // TODO set karna hey warnings ko

    @Override
    public void setMemberWarnings(long guildID, long memberID, int warningAmount) {
        String query = String.format("INSERT INTO guild_members (id, guild_id, member_id, member_warnings) VALUES (NULL, '%d', '%d', %d)", guildID, memberID, warningAmount);
        try (final PreparedStatement preparedStatement = getConnection().prepareStatement(query)){
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getMemberWarnings(long guildID, long memberID) {
        String query = String.format("SELECT member_id FROM guild_members WHERE guild_id = %d AND member_id = %d", guildID, memberID);
        try (final PreparedStatement preparedStatement = getConnection().prepareStatement(query)){
            try (final ResultSet resultSet = preparedStatement.executeQuery()){
                return resultSet.getInt("member_warnings");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public ArrayList<String> getBadWords(long guildID) {
        try (final PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT word FROM bad_words WHERE guild_id = " + guildID)){
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                final ArrayList<String> words = new ArrayList<>();
                while (resultSet.next()){
                    words.add(resultSet.getString("word"));
                }
                return words;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setBadWord(long guildID, String word) {
        String query = String.format("INSERT INTO bad_words (id, guild_id, word) VALUES (NULL, '%d', '%s')", guildID, word);
        try (final PreparedStatement preparedStatement = getConnection().prepareStatement(query)){
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPrefix(long guildID) {
        try (final PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT prefix FROM guild_settings WHERE guild_id = ?")) {
            preparedStatement.setString(1, String.valueOf(guildID));
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()){
                    return resultSet.getString("prefix");
                }
            }

            try (final PreparedStatement insertStatement = getConnection().prepareStatement("INSERT INTO guild_settings(guild_id) VALUES(?)")) {
                insertStatement.setString(1, String.valueOf(guildID));
                insertStatement.execute();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return Bot.BOT_PREFIX;
    }

    @Override
    public void setPrefix(long guildID, String newPrefix) {
        try (final PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE guild_settings SET prefix = ? WHERE guild_id = ?")) {
            preparedStatement.setString(1,newPrefix);
            preparedStatement.setString(2,String.valueOf(guildID));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
