package app.parsers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ParserTXT {
    public static void toWriteFile(String playerName, int finishChecker) throws IOException {
        File file = new File("src/main/resources/scores.txt");
        FileWriter writer = new FileWriter(file, true);

        if (finishChecker == 2) {
            writer.write("Draw!\n");
        } else {
            writer.write(playerName + " won\n");
        }

        writer.flush();
        writer.close();

    }
}