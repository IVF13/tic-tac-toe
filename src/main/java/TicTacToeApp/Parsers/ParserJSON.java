package TicTacToeApp.Parsers;

import TicTacToeApp.Models.GameResult;
import TicTacToeApp.Models.GameplayData;
import TicTacToeApp.Models.Player;
import TicTacToeApp.Models.Step;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ParserJSON implements Parser {

    @Override
    public void toReadFile() {
        GameplayData dataObject = new GameplayData();
        ObjectMapper mapper = new ObjectMapper();

        try {
            dataObject = mapper.readValue(new File("src/main/resources/gameplay.json"), GameplayData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        players.addAll(dataObject.getPlayers());
        stepsToRead.addAll(dataObject.getStepsToWrite());
        gameResult.append(dataObject.getGameResult().toString());

    }

    @Override
    public void toWriteFile(List<Player> players, List<Step> stepsToWrite, int finishChecker) {
        GameplayData dataObject = toCreateGameplayDataObject(players, stepsToWrite, finishChecker);

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("src/main/resources/gameplay.json"), dataObject);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String toWriteJSONString(List<Player> players, List<Step> stepsToWrite, int finishChecker) {
        GameplayData dataObject = toCreateGameplayDataObject(players, stepsToWrite, finishChecker);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(dataObject);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";

    }

    private GameplayData toCreateGameplayDataObject(List<Player> players, List<Step> stepsToWrite, int finishChecker) {
        GameplayData gameplayDataObject;

        if (finishChecker == 2) {
            gameplayDataObject = new GameplayData(players, stepsToWrite);
        } else {
            int indexOfWinner = (stepsToWrite.size() + 1) % 2;
            gameplayDataObject = new GameplayData(players, stepsToWrite,
                    List.of(new GameResult(players.get(indexOfWinner).toString())));
        }

        return gameplayDataObject;

    }

}

