package de.christianbergau.warcraft3.replaymanager;

import java.io.File;

public class TestApplication {
    public static void main(String... args) {
        Parser parser = new Parser();
        parseFiles(parser, new File(args[0]).listFiles());
    }

    static void parseFiles(Parser parser, File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                //System.out.println("Directory: " + file.getName());
                parseFiles(parser, file.listFiles()); // Calls same method again.
            } else if (file.getName().endsWith(".w3g")){
                System.out.println("Replay File: " + file.getAbsolutePath());
                try {
                    parser.parse(file.getAbsolutePath());
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }
}
