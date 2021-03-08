//Developer: Mubashir Zulfiqar  Date: 3/2/2021  Time: 10:06 PM
//Happy Coding...

package me.deathvader.guardian_bot.database;

import me.deathvader.guardian_bot.Bot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class cloudClusterDataSource implements DatabaseManager{
    private static final Logger LOGGER = LoggerFactory.getLogger(cloudClusterDataSource.class);
    String dbServer = "mysql-20127-0.cloudclusters.net";
    int dbPort = 20127;
    String userName = "deathvader";
    String password = "alpha9umeric";
    private Connection connection = null;
    String url = String.format("jdbc:mysql://%s:%d?user=%s&password=%s", dbServer, dbPort, userName, password);
    /*public static String user = "deathvader",
            pass = "alpha9umeric",
            dbClass = "com.mysql.cj.jdbc.Driver",
            dbDriver = "jdbc:mysql://mysql-20127-0.cloudclusters.net:20127/guardian_bot";*/

    public cloudClusterDataSource() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        try (final Statement statement = getConnection().createStatement()) {
            final String botPrefix = Bot.BOT_PREFIX;

            // Language = SQL Lite
            statement.execute("CREATE TABLE IF NOT EXISTS guild_settings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "guild_id VARCHAR(20) NOT NULL," +
                    "prefix VARCHAR(255) NOT NULL DEFAULT '" + botPrefix + "'" +
                    ");");

            LOGGER.info("Table Initialised.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        return connection;
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
