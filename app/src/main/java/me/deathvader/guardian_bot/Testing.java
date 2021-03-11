package me.deathvader.guardian_bot;//Developer: Mubashir Zulfiqar  Date: 2/25/2021  Time: 7:00 PM
//Happy Coding...

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.deathvader.guardian_bot.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@SuppressWarnings("ALL")
public class Testing {
    public static void notes() {
        /*Pattern pattern = Pattern.compile("w3schools", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher("Visit W3Schools!");
        boolean matchFound = matcher.find();
        if(matchFound) {
            System.out.println("Match found");
        } else {
            System.out.println("Match not found");
        }*/ // Simple Example of RegEx

        // TODO make a command to get the emoji unicode
        /*String actualEmojiString = "😀😂";
        actualEmojiString.codePoints().mapToObj(Integer::toHexString).forEach(System.out::println);*/

        // TODO use it any where to get the link in a string
        /*String string = "testing url in brackets (https://www.google.com) like this";
        Matcher matcher = urlPattern.matcher(string);
        while (matcher.find()) {
            int matchStart = matcher.start(1);
            int matchEnd = matcher.end();
            System.out.println(string.substring(matchStart, matchEnd));
        }*/

    }

    public static void main(String[] args) {
        String configFile = "db.properties";
        HikariConfig cfg = new HikariConfig(configFile);
        HikariDataSource ds = new HikariDataSource(cfg);

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = ds.getConnection();
            pst = con.prepareStatement("SELECT * FROM bad_words");
            rs = pst.executeQuery();

            System.out.println("ID \t Guild_Id \t Word");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " \t " + rs.getString(2) + " \t " + rs.getString(3));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (pst != null) {
                    pst.close();
                }

                if (con != null) {
                    con.close();
                }

                ds.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}

// Pattern for recognizing a URL, based off RFC 3986
    /*private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);*/