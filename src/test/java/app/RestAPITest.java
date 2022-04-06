package app;

import app.controller.GameDataController;
import app.controller.GameplayController;
import app.models.Player;
import app.models.Step;
import app.parsers.Parser;
import app.services.GameResultService;
import app.services.GameResultServiceImpl;
import app.services.GameboardService;
import app.services.GameboardServiceImpl;
import app.services.PlayerService;
import app.services.PlayerServiceImpl;
import app.services.StepService;
import app.services.StepServiceImpl;
import app.utils.GameSimulator;
import app.utils.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.Date;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RestAPITest {
    private final PlayerService playerService = new PlayerServiceImpl();
    private final GameboardService gameboardService = new GameboardServiceImpl();
    private final StepService stepService = new StepServiceImpl();
    private final GameResultService gameResultService = new GameResultServiceImpl();

    GameplayController gameplayController = new GameplayController(playerService, gameboardService, stepService, gameResultService);
    GameDataController gameDataController = new GameDataController();

    public RestAPITest() {
    }

    @BeforeEach
    public void toPrepareForTests() {
        gameboardService.delete();
        playerService.deleteAll();
        stepService.deleteAll();
        gameResultService.deleteAll();
        gameResultService.setFinishChecker(0);
        Parser.toClearParserData();
    }

    @Test
    public void startApiTest() {
        assertEquals(new ResponseEntity<>("Game started\nEnter the name of the first player", HttpStatus.CREATED),
                gameplayController.startGame());
    }

    @Test
    public void nameSetTest() {
        assertEquals(new ResponseEntity<>("Launch the game at first", HttpStatus.LOCKED),
                gameplayController.updateFirstPlayerName(new Player("Roma")));

        assertEquals(new ResponseEntity<>("Launch the game at first", HttpStatus.LOCKED),
                gameplayController.updateSecondPlayerName(new Player("Arseniy")));

        gameplayController.startGame();

        assertEquals(new ResponseEntity<>("Invalid value entered", HttpStatus.LOCKED),
                gameplayController.updateFirstPlayerName(new Player(null)));

        assertEquals(new ResponseEntity<>("Invalid value entered", HttpStatus.LOCKED),
                gameplayController.updateSecondPlayerName(new Player(null)));

        assertEquals(new ResponseEntity<>("Enter the name of the second player", HttpStatus.OK),
                gameplayController.updateFirstPlayerName(new Player("Roma")));

        assertEquals(new ResponseEntity<>("Get started \n" +
                        gameboardService.read(), HttpStatus.OK),
                gameplayController.updateSecondPlayerName(new Player("Arseniy")));

    }

    @Test
    void makeStepTest() {
        assertEquals(new ResponseEntity<>("Launch the game at first", HttpStatus.LOCKED),
                gameplayController.makeStep(1, new Step(1)));

        assertEquals(new ResponseEntity<>("Launch the game at first", HttpStatus.LOCKED),
                gameplayController.makeStep(2, new Step(1)));

        gameplayController.startGame();

        assertEquals(new ResponseEntity<>("Name the players", HttpStatus.LOCKED),
                gameplayController.makeStep(1, new Step(1)));

        assertEquals(new ResponseEntity<>("Name the players", HttpStatus.LOCKED),
                gameplayController.makeStep(2, new Step(1)));

        gameplayController.updateFirstPlayerName(new Player("Roma"));
        gameplayController.updateSecondPlayerName(new Player("Arseniy"));

        assertEquals(new ResponseEntity<>(gameboardService.read()
                        + "\nError, now is not your turn", HttpStatus.LOCKED),
                gameplayController.makeStep(2, new Step(1)));

        assertEquals(new ResponseEntity<>("""
                        Select cell(1-9):\s
                         |X|-|-|
                         |-|-|-|
                         |-|-|-|
                        """, HttpStatus.OK),
                gameplayController.makeStep(1, new Step(1)));

    }

    @Test
    void readPlayersInfoTest() {
        gameplayController.startGame();

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND), gameDataController.readPlayersInfo());

        gameplayController.updateFirstPlayerName(new Player("Roma"));
        gameplayController.updateSecondPlayerName(new Player("Arseniy"));

        assertEquals(new ResponseEntity<>(playerService.readAll(), HttpStatus.OK),
                gameDataController.readPlayersInfo());
    }

    @Test
    void readPlayerInfoTest() {
        gameplayController.startGame();

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND), gameDataController.readPlayerInfo(1));

        gameplayController.updateFirstPlayerName(new Player("Roma"));

        assertEquals(new ResponseEntity<>(playerService.read(1), HttpStatus.OK),
                gameDataController.readPlayerInfo(1));
    }

    @Test
    void readStepsInfoTest() {
        gameplayController.startGame();

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND), gameDataController.readStepsInfo());

        gameplayController.updateFirstPlayerName(new Player("Roma"));
        gameplayController.updateSecondPlayerName(new Player("Arseniy"));

        gameplayController.makeStep(1, new Step(1));
        gameplayController.makeStep(2, new Step(2));

        assertEquals(new ResponseEntity<>(stepService.readAll(), HttpStatus.OK),
                gameDataController.readStepsInfo());
    }

    @Test
    void readStepInfoTest() {
        gameplayController.startGame();

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND), gameDataController.readStepInfo(0));

        gameplayController.updateFirstPlayerName(new Player("Roma"));
        gameplayController.updateSecondPlayerName(new Player("Arseniy"));

        gameplayController.makeStep(1, new Step(1));

        assertEquals(new ResponseEntity<>(stepService.readAll().get(0), HttpStatus.OK),
                gameDataController.readStepInfo(1));
    }

    @Test
    void deletePlayerTest() {
        gameplayController.startGame();

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_MODIFIED),
                gameDataController.deletePlayer(1));

        gameplayController.updateFirstPlayerName(new Player("Roma"));
        gameplayController.updateSecondPlayerName(new Player("Arseniy"));

        assertEquals(new ResponseEntity<>("Player was deleted, create the new one", HttpStatus.OK),
                gameDataController.deletePlayer(1));
    }


    @Test
    void deleteStepTest() {
        gameplayController.startGame();

        gameplayController.updateFirstPlayerName(new Player("Roma"));
        gameplayController.updateSecondPlayerName(new Player("Arseniy"));

        gameplayController.makeStep(1, new Step(1));
        gameplayController.makeStep(2, new Step(2));

        assertEquals(new ResponseEntity<>("""
                Select cell(1-9):\s
                 |-|O|-|
                 |-|-|-|
                 |-|-|-|

                Step was deleted""", HttpStatus.OK), gameDataController.deleteStep(0));
    }

    @Test
    void toPlayTicTacToeTestWin() {
        gameplayController.startGame();

        gameplayController.updateFirstPlayerName(new Player("Roma"));
        gameplayController.updateSecondPlayerName(new Player("Arseniy"));

        gameplayController.makeStep(1, new Step(1));
        gameplayController.makeStep(2, new Step(2));
        gameplayController.makeStep(1, new Step(3));
        gameplayController.makeStep(2, new Step(4));
        gameplayController.makeStep(1, new Step(5));
        gameplayController.makeStep(2, new Step(6));
        assertEquals(new ResponseEntity<>("\nSelect cell(1-9): \n" +
                " |X|O|X|\n" +
                " |O|X|O|\n" +
                " |X|-|-|\n" +
                "\n" +
                playerService.read(1).getName()
                + " won\n" +
                "Game over", HttpStatus.OK), gameplayController.makeStep(1, new Step(7)));

        assertEquals(new ResponseEntity<>("{playerId=1, name='Roma', symbol='X'}", HttpStatus.OK),
                gameDataController.readResult());
    }

    @Test
    void toPlayTicTacToeTestDraw() {
        gameplayController.startGame();

        gameplayController.updateFirstPlayerName(new Player("Roma"));
        gameplayController.updateSecondPlayerName(new Player("Arseniy"));

        gameplayController.makeStep(1, new Step(1));
        gameplayController.makeStep(2, new Step(2));
        gameplayController.makeStep(1, new Step(4));
        gameplayController.makeStep(2, new Step(5));
        gameplayController.makeStep(1, new Step(8));
        gameplayController.makeStep(2, new Step(7));
        gameplayController.makeStep(1, new Step(3));
        gameplayController.makeStep(2, new Step(9));
        gameplayController.makeStep(1, new Step(6));

        assertEquals(new ResponseEntity<>("Draw!", HttpStatus.OK), gameDataController.readResult());

        assertEquals(new ResponseEntity<>((gameboardService.read()
                        + "\nThe game is over, you can restart it"), HttpStatus.LOCKED),
                gameplayController.makeStep(1, new Step(1)));

        assertEquals(new ResponseEntity<>((gameboardService.read()
                        + "\nThe game is over, you can restart it"), HttpStatus.LOCKED),
                gameplayController.makeStep(2, new Step(1)));

    }

    @Test
    void toPlayTicTacToeNRestartTest() {
        gameplayController.startGame();

        gameplayController.updateFirstPlayerName(new Player("Roma"));
        gameplayController.updateSecondPlayerName(new Player("Arseniy"));

        gameplayController.makeStep(1, new Step(1));
        gameplayController.makeStep(2, new Step(2));
        gameplayController.makeStep(1, new Step(3));
        gameplayController.makeStep(2, new Step(4));
        gameplayController.makeStep(1, new Step(5));
        gameplayController.makeStep(2, new Step(6));
        gameplayController.makeStep(1, new Step(7));

        assertEquals(new ResponseEntity<>("[{playerId=1, name='Roma', symbol='X'}]", HttpStatus.OK).toString(),
                gameDataController.readResults().toString());

        assertEquals(new ResponseEntity<>((gameboardService.read()
                        + "\nThe game is over, you can restart it"), HttpStatus.LOCKED),
                gameplayController.makeStep(1, new Step(1)));

        assertEquals(new ResponseEntity<>("Game restarted\n" +
                        "Enter the name of the first player", HttpStatus.CREATED),
                gameplayController.restartGame());

        String[][] defalutField = {{"1", "2", "3"}, {"4", "5", "6"}, {"7", "8", "9"}};

        assertArrayEquals(defalutField, gameboardService.getGameboard().getField());
        assertTrue(playerService.readAll().isEmpty());
        assertTrue(stepService.readAll().isEmpty());

        assertEquals(new ResponseEntity<>("Enter the name of the second player", HttpStatus.OK),
                gameplayController.updateFirstPlayerName(new Player("Roma")));

        assertEquals(new ResponseEntity<>("Get started \n" +
                        gameboardService.read(), HttpStatus.OK),
                gameplayController.updateSecondPlayerName(new Player("Arseniy")));

    }

    @Test
    void toPlayTicTacToeNGetResultsTest() {
        gameplayController.startGame();

        gameplayController.updateFirstPlayerName(new Player("Roma"));
        gameplayController.updateSecondPlayerName(new Player("Arseniy"));

        gameplayController.makeStep(1, new Step(1));
        gameplayController.makeStep(2, new Step(2));
        gameplayController.makeStep(1, new Step(4));
        gameplayController.makeStep(2, new Step(5));
        gameplayController.makeStep(1, new Step(8));
        gameplayController.makeStep(2, new Step(7));
        gameplayController.makeStep(1, new Step(3));
        gameplayController.makeStep(2, new Step(9));
        gameplayController.makeStep(1, new Step(6));

        assertEquals(new ResponseEntity<>("Draw!", HttpStatus.OK), gameDataController.readResult());

        gameplayController.restartGame();

        gameplayController.updateFirstPlayerName(new Player("Roma"));
        gameplayController.updateSecondPlayerName(new Player("Arseniy"));

        gameplayController.makeStep(1, new Step(1));
        gameplayController.makeStep(2, new Step(2));
        gameplayController.makeStep(1, new Step(3));
        gameplayController.makeStep(2, new Step(4));
        gameplayController.makeStep(1, new Step(5));
        gameplayController.makeStep(2, new Step(6));
        gameplayController.makeStep(1, new Step(7));

        assertEquals(new ResponseEntity<>("[Draw!, {playerId=1, name='Roma', symbol='X'}]",
                HttpStatus.OK).toString(), gameDataController.readResults().toString());


        assertEquals(new ResponseEntity<>("Results were deleted", HttpStatus.OK),
                gameDataController.deleteResults());

        assertTrue(Objects.requireNonNull(gameDataController.readResults().getBody()).isEmpty());

    }

    @Test
    void toWriteLogNSimulateGameTest() {
        gameplayController.startGame();

        gameplayController.updateFirstPlayerName(new Player("Roma"));
        gameplayController.updateSecondPlayerName(new Player("Arseniy"));

        assertEquals(new ResponseEntity<>("The game is not finished", HttpStatus.LOCKED),
                gameDataController.toGetWriteLogInfo());

        assertEquals(new ResponseEntity<>("The game is not finished", HttpStatus.LOCKED),
                gameDataController.toWriteLog(3));

        gameplayController.makeStep(1, new Step(1));
        gameplayController.makeStep(2, new Step(2));
        gameplayController.makeStep(1, new Step(4));
        gameplayController.makeStep(2, new Step(5));
        gameplayController.makeStep(1, new Step(8));
        gameplayController.makeStep(2, new Step(7));
        gameplayController.makeStep(1, new Step(3));
        gameplayController.makeStep(2, new Step(9));
        gameplayController.makeStep(1, new Step(6));

        assertEquals(new ResponseEntity<>("""
                Select log format:\s
                1 - XML File\s
                2 - JSON File\s
                3 - XML & JSON Files\s
                4 - JSON File & String\s
                5 - All formats\s
                default: TXT\s""", HttpStatus.OK), gameDataController.toGetWriteLogInfo());

        String jsonExpected = Logger.toWriteTheLog(playerService.readAll(), stepService.readAll(),
                gameResultService.getFinishChecker(), 5);

        assertEquals(new ResponseEntity<>("The log successfully written\n", HttpStatus.OK),
                gameDataController.toWriteLog(2));

        assertEquals(new ResponseEntity<>("The log successfully written\n" + jsonExpected, HttpStatus.OK),
                gameDataController.toWriteLog(5));

        assertEquals(new ResponseEntity<>("Select the log by which the game will be played: \n" +
                "1 - XML changed: " + new Date(new File("src/main/resources/gameplay.xml").lastModified())
                + "\n"
                + "2 - JSON changed: " + new Date(new File("src/main/resources/gameplay.json").lastModified())
                + "\n",
                HttpStatus.OK), gameDataController.toSimulateTheGameInfo());

        assertEquals(new ResponseEntity<>(GameSimulator.toBuildGameSimulation(2),
                HttpStatus.OK), gameDataController.toSimulateTheGame(2));

    }

}
