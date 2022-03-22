package TicTacToeApp.Parsers;

import TicTacToeApp.Objects.Player;
import TicTacToeApp.Objects.Step;

import java.util.List;

public class DataObject {
    private List<Player> players;
    private List<Step> stepsToWrite;
    private List gameResult;

    public DataObject() {

    }

    public DataObject(List<Player> players, List<Step> stepsToWrite) {
        this.players = players;
        this.stepsToWrite = stepsToWrite;
        this.gameResult = List.of("Draw!");
    }

    public DataObject(List<Player> players, List<Step> stepsToWrite, List<Player> gameResult) {
        this.players = players;
        this.stepsToWrite = stepsToWrite;
        this.gameResult = gameResult;
    }

    @Override
    public String toString() {
        return "DataObject{" +
                "players=" + players +
                ", stepsToWrite=" + stepsToWrite +
                ", gameResult=" + gameResult +
                '}';
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Step> getStepsToWrite() {
        return stepsToWrite;
    }

    public List getGameResult() {
        return gameResult;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setStepsToWrite(List<Step> stepsToWrite) {
        this.stepsToWrite = stepsToWrite;
    }

    public void setGameResult(List gameResult) {
        this.gameResult = gameResult;
    }

}
