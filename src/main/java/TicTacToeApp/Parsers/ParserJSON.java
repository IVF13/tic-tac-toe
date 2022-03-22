package TicTacToeApp.Parsers;

import TicTacToeApp.Objects.Player;
import TicTacToeApp.Objects.Step;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ParserJSON implements Parser {

    @Override
    public void toReadFile() {
        DataObject dataObject = new DataObject();
        ObjectMapper mapper = new ObjectMapper();

        try {
            dataObject = mapper.readValue(new File("src/main/resources/gameplay.json"), DataObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        players.addAll(dataObject.getPlayers());
        stepsToRead.addAll(dataObject.getStepsToWrite());
        gameResult.append(dataObject.getGameResult().toString());

    }

    @Override
    public void toWriteFile(List<Player> players, List<Step> stepsToWrite, int finishChecker) {
        DataObject dataObject;

        if (finishChecker == 2) {
            dataObject = new DataObject(players, stepsToWrite);
        } else {
            int indexOfWinner = (stepsToWrite.size() + 1) % 2;
            dataObject = new DataObject(players, stepsToWrite, List.of(players.get(indexOfWinner)));
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("src/main/resources/gameplay.json"), dataObject);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String toWriteJSONString(List<Player> players, List<Step> stepsToWrite, int finishChecker) {
        DataObject dataObject;

        if (finishChecker == 2) {
            dataObject = new DataObject(players, stepsToWrite);
        } else {
            int indexOfWinner = (stepsToWrite.size() + 1) % 2;
            dataObject = new DataObject(players, stepsToWrite, List.of(players.get(indexOfWinner)));
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(dataObject);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}

