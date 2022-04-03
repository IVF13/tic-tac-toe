package TicTacToeApp.Parsers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ParserTXT {
    public static void toWriteFile(String playerName, int finishChecker) throws IOException {
        File file = new File("src/main/resources/scores.txt");
        FileWriter writer = new FileWriter(file, true);

        if (finishChecker == 2) {
            writer.write("Ничья\n");
        } else {
            writer.write(playerName + " победил\n");
        }

        writer.flush();
        writer.close();

    }
}
