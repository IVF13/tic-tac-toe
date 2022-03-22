package TicTacToeApp.Parsers;

import TicTacToeApp.Objects.Player;
import TicTacToeApp.Objects.Step;
import java.util.ArrayList;
import java.util.List;


public interface Parser {
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Step> stepsToRead = new ArrayList<>();
    StringBuilder gameResult = new StringBuilder();

    void toReadFile();

    void toWriteFile(int finishChecker, List<Step> stepsToWrite, List<Player> players);

}
