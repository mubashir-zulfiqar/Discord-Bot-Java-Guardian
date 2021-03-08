//Developer: Mubashir Zulfiqar  Date: 2/26/2021  Time: 11:24 PM
//Happy Coding...

package me.deathvader.guardian_bot.command.lyrics;

import com.google.api.client.repackaged.com.google.common.base.Strings;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @implNote  This class is used to parse the LRC file. Put the parsed complete LRC file into a LyricsInfo object and return the LyricsInfo objects.
 * @author Mubashir Zulfiqar
*/

public class LyricsParser {
    private LyricsInfo lyricsInfo = new LyricsInfo();
    public ArrayList<Long> time = new ArrayList<>();
    private long currentTime = 0; // store temporary time
    private String currentContent = null;//Save temporary lyrics
    private final HashMap<Long, String> maps = new HashMap<>();//Map where the user saves the mapping between all lyrics and time point information

    /**
     * According to the file path, read the file and return an input stream
     *
     * @param path
     * Path
     * @return input stream
     * @throws FileNotFoundException if the lrc file is not found
     */

    private InputStream readLrcFile(String path) throws FileNotFoundException {
        File f = new File(path);
        return new FileInputStream(f);
    }

    public LyricsInfo parser(String path) throws Exception {
        InputStream in = readLrcFile(path);
        lyricsInfo = parser(in);
        return lyricsInfo;
    }

    /**
     * Parse the information in the input stream and return a LyricsInfo object
     *
     * @param inputStream
     * Input stream
     * @return parsed LyricsInfo object
     * @throws IOException throws input/output exception
     */

    public LyricsInfo parser(InputStream inputStream) throws IOException {
        // three-layer packaging
        InputStreamReader inr = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inr);
        // read line by line, read one line each time, parse one line
        String line;
        while ((line = reader.readLine()) != null) {
            parserLine(line);
        }
        // After all parsing, set info
        lyricsInfo.setInfo(maps);
        return lyricsInfo;
    }

    /**
     * Use regular expressions to parse each line of specific statements
     * and after parsing the statement, set the parsed information in the LyricsInfo object
     *
     * @param str parsing each line of the lrc file by regular expression
     */

    private void parserLine(String str) {
        // Get song name information
        if (str.startsWith("[ti:")) {
            String title = str.substring(4, str.length() - 1);
            System.out.println("title--->" + title);
            lyricsInfo.setTitle(title);

        }
        else if (str.startsWith("[length:")) {
            String length = str.substring(8, str.length() - 1);
            System.out.println("length--->" + length);
            lyricsInfo.setLength(length);
        }// Get singer information
        else if (str.startsWith("[ar:")) {
            String singer = str.substring(4, str.length() - 1);
            System.out.println("singer--->" + singer);
            lyricsInfo.setSinger(singer);

        }// Get album information
        else if (str.startsWith("[al:")) {
            String album = str.substring(4, str.length() - 1);
            System.out.println("album--->" + album);
            lyricsInfo.setAlbum(album);

        }// Get the lyrics information for each sentence by regularity
        else {
			 // Set the regular rules
            String reg = "\\[(\\d{2}:\\d{2}\\.\\d{2})\\]";
            // compile
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(str);

            // If there is a match, do the following
            while (matcher.find()) {
                // get all the matches
                String msg = matcher.group();
                // get the index of the start of this match
                int start = matcher.start();
                // get the index of the end of this match
                int end = matcher.end();

                // Get the number of groups in this match
                int groupCount = matcher.groupCount();
                // Get the content in each group
                for (int i = 0; i <= groupCount; i++) {
                    String timeStr = matcher.group(i);
                    if (i == 1) {
                        // Set the content in the second group to the current point in time
                        currentTime = strToLong(timeStr);
                    }
                }

                // Get the content after the time
                String[] content = pattern.split(str);
				 // Output array content
                for (int i = 0; i < content.length; i++) {
                    if (i == content.length - 1) {
                        // set the content to the current content
                        currentContent = content[i];
                    }
                }
				 // Set the mapping of time points and content
                maps.put(currentTime, currentContent);
                time.add(currentTime);
                /*System.out.println("put---currentTime--->" + currentTime
                        + "----currentContent---->" + currentContent);*/

            }
        }
    }

    /**
     * Convert the parsed character representing the time to Long
     *
     * @param timeStr
     * Time point in character form
     * @return Long form time
     */

    private long strToLong(String timeStr) {
        // Because the time format of the string given is XX:XX.XX, the returned long requirement is in milliseconds.
        // 1: Use: Split 2: Use. Split
        String[] s = timeStr.split(":");
        int min = Integer.parseInt(s[0]);
        String[] ss = s[1].split("\\.");
        int sec = Integer.parseInt(ss[0]);
        int mill = Integer.parseInt(ss[1]);
        return min * 60 * 1000 + sec * 1000 + mill * 10;
    }
}
