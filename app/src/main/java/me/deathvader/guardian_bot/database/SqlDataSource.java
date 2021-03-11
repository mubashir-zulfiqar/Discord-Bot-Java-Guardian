//Developer: Mubashir Zulfiqar  Date: 2/12/2021  Time: 11:25 PM
//Happy Coding...

package me.deathvader.guardian_bot.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.deathvader.guardian_bot.Bot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class SqlDataSource { // implements DatabaseManager
    private static final Logger LOGGER = LoggerFactory.getLogger(SqlDataSource.class);
    private final HikariDataSource ds;

    public SqlDataSource () {
        try {
            final File dbFile = new File("database.db");
            if (!dbFile.exists()) {
                if (dbFile.createNewFile()){
                    LOGGER.info("Database File Created.");
                }else {
                    LOGGER.error("Could not Create Database File.");
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:database.db");
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);

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

    private Connection getConnection () throws SQLException {
        return ds.getConnection();
    }

    // @Override
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

    // @Override
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
