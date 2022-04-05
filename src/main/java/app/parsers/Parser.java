package app.parsers;

import app.models.Player;
import app.models.Step;

import java.util.ArrayList;
import java.util.List;


public interface Parser {
    List<Player> players = new ArrayList<>();
    List<Step> stepsToRead = new ArrayList<>();
    StringBuilder gameResult = new StringBuilder();

    void toReadFile();

    void toWriteFile(List<Player> players, List<Step> stepsToWrite, int finishChecker);

    static void toClearParserData() {
        if (!players.isEmpty()) {
            players.clear();
        }
        if (!stepsToRead.isEmpty()) {
            stepsToRead.clear();
        }
        if (!gameResult.isEmpty()) {
            gameResult.setLength(0);
        }
    }

}
