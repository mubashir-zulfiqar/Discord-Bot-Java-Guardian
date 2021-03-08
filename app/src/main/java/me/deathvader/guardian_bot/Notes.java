package me.deathvader.guardian_bot;//Developer: Mubashir Zulfiqar  Date: 2/25/2021  Time: 7:00 PM
//Happy Coding...

import me.deathvader.guardian_bot.command.Commands_Interface;
import org.python.antlr.ast.Str;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class Notes{
    public static void testingMain() {

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
        String separator = "";  // separator here is your ","

        final List<Commands_Interface> commands = new Commands_Manager().getCommands();
        String[] arrayListWords = {"First",
                "Second",
                "Third",
                "Fourth",
                "Fifth",
                "Sixth",
                "Seventh",
                "Eighth",
                "Ninth",
                "Tenth"};


        StringBuilder stringBuilder = new StringBuilder();

        for (String s : arrayListWords) {
            stringBuilder.append(separator).append('`').append(s).append('`');
            separator = " | ";
        }

        System.out.println(commands.size());
    }

    // Pattern for recognizing a URL, based off RFC 3986
    /*private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);*/
}
