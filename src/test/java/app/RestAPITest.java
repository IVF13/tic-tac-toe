package app;

import app.models.Player;
import app.models.Step;
import app.controller.GameController;
import app.repository.GameplayDataRepository;
import app.services.GameResultService;
import app.services.GameResultServiceImpl;
import app.services.GameboardService;
import app.services.GameboardServiceImpl;
import app.services.GameplayDataService;
import app.services.PlayerService;
import app.services.PlayerServiceImpl;
import app.services.StepService;
import app.services.StepServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RestAPITest {
    private final PlayerService playerService = new PlayerServiceImpl();
    private final GameboardService gameboardService = new GameboardServiceImpl();
    private final StepService stepService = new StepServiceImpl();
    private final GameResultService gameResultService = new GameResultServiceImpl();
   private final GameplayDataService gameplayDataService = new GameplayDataService();
    @Autowired
    private GameplayDataRepository gameplayDataRepository;

    GameController gameController = new GameController(playerService, gameboardService, stepService, gameResultService, gameplayDataService, gameplayDataRepository);

    public RestAPITest() {
    }

    @BeforeEach
    public void toStartAPI() {
        gameboardService.delete();
        playerService.deleteAll();
        stepService.deleteAll();
        gameResultService.deleteAll();
        gameResultService.setFinishChecker(0);
    }

    @Test
    public void startApiTest() {
        assertEquals(new ResponseEntity<>("Game started\nEnter the name of the first player", HttpStatus.CREATED),
                gameController.startGame());
    }

    @Test
    public void nameSetTest() {
        assertEquals(new ResponseEntity<>("Launch the game first", HttpStatus.LOCKED),
                gameController.updateFirstPlayerName(new Player("Roma")));

        assertEquals(new ResponseEntity<>("Launch the game first", HttpStatus.LOCKED),
                gameController.updateSecondPlayerName(new Player("Arseniy")));

        gameController.startGame();

        assertEquals(new ResponseEntity<>("Invalid value entered", HttpStatus.NOT_FOUND),
                gameController.updateFirstPlayerName(new Player(null)));

        assertEquals(new ResponseEntity<>("Invalid value entered", HttpStatus.NOT_FOUND),
                gameController.updateSecondPlayerName(new Player(null)));

        assertEquals(new ResponseEntity<>("Enter the name of the second player", HttpStatus.OK),
                gameController.updateFirstPlayerName(new Player("Roma")));

        assertEquals(new ResponseEntity<>("Get started \n" +
                        gameboardService.read(), HttpStatus.OK),
                gameController.updateSecondPlayerName(new Player("Arseniy")));

    }

    @Test
    void makeStepTest() {
        assertEquals(new ResponseEntity<>("Launch the game at first", HttpStatus.LOCKED),
                gameController.makeStepByFirstPlayer(new Step(1)));

        assertEquals(new ResponseEntity<>("Launch the game at first", HttpStatus.LOCKED),
                gameController.makeStepBySecondPlayer(new Step(1)));

        gameController.startGame();

        assertEquals(new ResponseEntity<>("Name the players", HttpStatus.LOCKED),
                gameController.makeStepByFirstPlayer(new Step(1)));

        assertEquals(new ResponseEntity<>("Name the players", HttpStatus.LOCKED),
                gameController.makeStepBySecondPlayer(new Step(1)));

        gameController.updateFirstPlayerName(new Player("Roma"));
        gameController.updateSecondPlayerName(new Player("Arseniy"));

        assertEquals(new ResponseEntity<>(gameboardService.read()
                        + "\nError, now is not your turn", HttpStatus.LOCKED),
                gameController.makeStepBySecondPlayer(new Step(1)));

        assertEquals(new ResponseEntity<>("""
                        Select cell(1-9):\s
                         |X|-|-|
                         |-|-|-|
                         |-|-|-|
                        """, HttpStatus.OK),
                gameController.makeStepByFirstPlayer(new Step(1)));

    }

    @Test
    void readPlayersInfoTest() {
        gameController.startGame();

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND), gameController.readPlayersInfo());

        gameController.updateFirstPlayerName(new Player("Roma"));
        gameController.updateSecondPlayerName(new Player("Arseniy"));

        assertEquals(new ResponseEntity<>(playerService.readAll(), HttpStatus.OK),
                gameController.readPlayersInfo());
    }

    @Test
    void readPlayerInfoTest() {
        gameController.startGame();

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND), gameController.readPlayerInfo(1));

        gameController.updateFirstPlayerName(new Player("Roma"));

        assertEquals(new ResponseEntity<>(playerService.read(1), HttpStatus.OK),
                gameController.readPlayerInfo(1));
    }

    @Test
    void readStepsInfoTest() {
        gameController.startGame();

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND), gameController.readStepsInfo());

        gameController.updateFirstPlayerName(new Player("Roma"));
        gameController.updateSecondPlayerName(new Player("Arseniy"));

        gameController.makeStepByFirstPlayer(new Step(1));
        gameController.makeStepBySecondPlayer(new Step(2));

        assertEquals(new ResponseEntity<>(stepService.readAll(), HttpStatus.OK),
                gameController.readStepsInfo());
    }

    @Test
    void readStepInfoTest() {
        gameController.startGame();

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND), gameController.readStepInfo(0));

        gameController.updateFirstPlayerName(new Player("Roma"));
        gameController.updateSecondPlayerName(new Player("Arseniy"));

        gameController.makeStepByFirstPlayer(new Step(1));

        assertEquals(new ResponseEntity<>(stepService.readAll().get(0), HttpStatus.OK),
                gameController.readStepInfo(1));
    }

    @Test
    void deletePlayerTest() {
        gameController.startGame();

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_MODIFIED),
                gameController.deletePlayer(1));

        gameController.updateFirstPlayerName(new Player("Roma"));
        gameController.updateSecondPlayerName(new Player("Arseniy"));

        assertEquals(new ResponseEntity<>("Player 1 was deleted, create the new one", HttpStatus.OK),
                gameController.deletePlayer(1));
    }


    @Test
    void deleteStepTest() {
        gameController.startGame();

        gameController.updateFirstPlayerName(new Player("Roma"));
        gameController.updateSecondPlayerName(new Player("Arseniy"));

        gameController.makeStepByFirstPlayer(new Step(1));
        gameController.makeStepBySecondPlayer(new Step(2));

        assertEquals(new ResponseEntity<>("""
                Select cell(1-9):\s
                 |-|O|-|
                 |-|-|-|
                 |-|-|-|

                Step was deleted""", HttpStatus.OK), gameController.deleteStep(0));
    }

    @Test
    void toPlayTicTacToeTestWin() {
        gameController.startGame();

        gameController.updateFirstPlayerName(new Player("Roma"));
        gameController.updateSecondPlayerName(new Player("Arseniy"));

        gameController.makeStepByFirstPlayer(new Step(1));
        gameController.makeStepBySecondPlayer(new Step(2));
        gameController.makeStepByFirstPlayer(new Step(3));
        gameController.makeStepBySecondPlayer(new Step(4));
        gameController.makeStepByFirstPlayer(new Step(5));
        gameController.makeStepBySecondPlayer(new Step(6));
        assertEquals(new ResponseEntity<>("\nSelect cell(1-9): \n" +
                " |X|O|X|\n" +
                " |O|X|O|\n" +
                " |X|-|-|\n" +
                "\n" +
                playerService.read(1).getName()
                + " won\n" +
                "Game over", HttpStatus.OK), gameController.makeStepByFirstPlayer(new Step(7)));

        assertEquals(new ResponseEntity<>("{playerId=1, name='Roma', symbol='X'}", HttpStatus.OK),
                gameController.readResult());
    }

    @Test
    void toPlayTicTacToeTestDraw() {
        gameController.startGame();

        gameController.updateFirstPlayerName(new Player("Roma"));
        gameController.updateSecondPlayerName(new Player("Arseniy"));

        gameController.makeStepByFirstPlayer(new Step(1));
        gameController.makeStepBySecondPlayer(new Step(2));
        gameController.makeStepByFirstPlayer(new Step(4));
        gameController.makeStepBySecondPlayer(new Step(5));
        gameController.makeStepByFirstPlayer(new Step(8));
        gameController.makeStepBySecondPlayer(new Step(7));
        gameController.makeStepByFirstPlayer(new Step(3));
        gameController.makeStepBySecondPlayer(new Step(9));
        gameController.makeStepByFirstPlayer(new Step(6));

        assertEquals(new ResponseEntity<>("Draw!", HttpStatus.OK), gameController.readResult());

        assertEquals(new ResponseEntity<>((gameboardService.read()
                        + "\nThe game is over, you can restart it"), HttpStatus.LOCKED),
                gameController.makeStepByFirstPlayer(new Step(1)));

        assertEquals(new ResponseEntity<>((gameboardService.read()
                        + "\nThe game is over, you can restart it"), HttpStatus.LOCKED),
                gameController.makeStepBySecondPlayer(new Step(1)));

    }

    @Test
    void toPlayTicTacToeNRestartTest() {
        gameController.startGame();

        gameController.updateFirstPlayerName(new Player("Roma"));
        gameController.updateSecondPlayerName(new Player("Arseniy"));

        gameController.makeStepByFirstPlayer(new Step(1));
        gameController.makeStepBySecondPlayer(new Step(2));
        gameController.makeStepByFirstPlayer(new Step(3));
        gameController.makeStepBySecondPlayer(new Step(4));
        gameController.makeStepByFirstPlayer(new Step(5));
        gameController.makeStepBySecondPlayer(new Step(6));
        gameController.makeStepByFirstPlayer(new Step(7));

        assertEquals(new ResponseEntity<>("[{playerId=1, name='Roma', symbol='X'}]", HttpStatus.OK).toString(),
                gameController.readResults().toString());

        assertEquals(new ResponseEntity<>((gameboardService.read()
                        + "\nThe game is over, you can restart it"), HttpStatus.LOCKED),
                gameController.makeStepByFirstPlayer(new Step(1)));

        assertEquals(new ResponseEntity<>("Game restarted\n" +
                        "Enter the name of the first player", HttpStatus.CREATED),
                gameController.restartGame());

        String[][] defalutField = {{"1", "2", "3"}, {"4", "5", "6"}, {"7", "8", "9"}};

        assertArrayEquals(defalutField, gameboardService.getGameboard().getField());
        assertTrue(playerService.readAll().isEmpty());
        assertTrue(stepService.readAll().isEmpty());

        assertEquals(new ResponseEntity<>("Enter the name of the second player", HttpStatus.OK),
                gameController.updateFirstPlayerName(new Player("Roma")));

        assertEquals(new ResponseEntity<>("Get started \n" +
                        gameboardService.read(), HttpStatus.OK),
                gameController.updateSecondPlayerName(new Player("Arseniy")));

    }

    @Test
    void toPlayTicTacToeNGetResultsTest() {
        gameController.startGame();

        gameController.updateFirstPlayerName(new Player("Roma"));
        gameController.updateSecondPlayerName(new Player("Arseniy"));

        gameController.makeStepByFirstPlayer(new Step(1));
        gameController.makeStepBySecondPlayer(new Step(2));
        gameController.makeStepByFirstPlayer(new Step(4));
        gameController.makeStepBySecondPlayer(new Step(5));
        gameController.makeStepByFirstPlayer(new Step(8));
        gameController.makeStepBySecondPlayer(new Step(7));
        gameController.makeStepByFirstPlayer(new Step(3));
        gameController.makeStepBySecondPlayer(new Step(9));
        gameController.makeStepByFirstPlayer(new Step(6));

        assertEquals(new ResponseEntity<>("Draw!", HttpStatus.OK), gameController.readResult());

        gameController.restartGame();

        gameController.updateFirstPlayerName(new Player("Roma"));
        gameController.updateSecondPlayerName(new Player("Arseniy"));

        gameController.makeStepByFirstPlayer(new Step(1));
        gameController.makeStepBySecondPlayer(new Step(2));
        gameController.makeStepByFirstPlayer(new Step(3));
        gameController.makeStepBySecondPlayer(new Step(4));
        gameController.makeStepByFirstPlayer(new Step(5));
        gameController.makeStepBySecondPlayer(new Step(6));
        gameController.makeStepByFirstPlayer(new Step(7));

        assertEquals(new ResponseEntity<>("[Draw!, {playerId=1, name='Roma', symbol='X'}]",
                HttpStatus.OK).toString(), gameController.readResults().toString());


        assertEquals(new ResponseEntity<>("Results were deleted", HttpStatus.OK),
                gameController.deleteResults());

        assertTrue(Objects.requireNonNull(gameController.readResults().getBody()).isEmpty());

    }

}
