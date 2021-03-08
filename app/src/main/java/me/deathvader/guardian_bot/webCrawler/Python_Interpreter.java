//Developer: Mubashir Zulfiqar  Date: 2/27/2021  Time: 4:20 AM
//Happy Coding...

package me.deathvader.guardian_bot.webCrawler;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Python_Interpreter {
    public static void run() throws Exception {
        // TODO Exception in thread "main" IOError: (2, 'File not found - C:\\Users\\mubas\\IdeaProjects\\Discord-Bot-Java-Guardian\\crawler.py (The system cannot find the file specified)')
        //Using Jython
        /*File file = new File("C:/Users/mubas/IdeaProjects/Discord-Bot-Java-Guardian/app/src/main/java/me/deathvader/guardian_bot/webCrawler/crawler.py");
        String query = "DHARIA+-+Sugar+%26+Brownies+(by+Monoir)+[Official+Video]";
        // String[] arguments = {"crawler.py", query};
        PythonInterpreter interpreter = new PythonInterpreter();
        String fileUrlPath = file.getPath();
        String scriptName = file.getName();
        interpreter.execfile(file.getName());
        // interpreter.exec("from resources.Python38.Lib.site_packages import requests \n" + "from resources.Python38.Lib.site_packages.bs4 import BeautifulSoup \n" + "from me.deathvader.guardian_bot.webCrawler.crawler import *");
        String funcName = "spider";
        PyObject someFunc = interpreter.get(funcName);
        if (someFunc == null) {
            throw new Exception("Could not find Python function: " + funcName);
        }
        try {
            someFunc.__call__(new PyString(query));
        } catch (PyException e) {
            e.printStackTrace();
        }*/

        //Using CMD
        List<String> cmdAndArgs = Arrays.asList("cmd.exe", "/C", "Start", "run.bat");
        ProcessBuilder pb = new ProcessBuilder(cmdAndArgs);
        File dir = new File("app\\src\\main\\java\\me\\deathvader\\guardian_bot\\webCrawler");
        pb.directory(dir);
        pb.start();
    }
}