package TicTacToeApp.Parsers;

import TicTacToeApp.Models.Player;
import TicTacToeApp.Models.Step;
import java.util.ArrayList;
import java.util.List;


public interface Parser {
    List<Player> players = new ArrayList<>();
    List<Step> stepsToRead = new ArrayList<>();
    StringBuilder gameResult = new StringBuilder();

    void toReadFile();

    void toWriteFile(List<Player> players, List<Step> stepsToWrite, int finishChecker);

}
