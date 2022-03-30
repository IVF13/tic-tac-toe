import TicTacToeApp.Objects.Player;
import TicTacToeApp.Objects.Step;
import TicTacToeApp.RestAPI.GameController;
import TicTacToeApp.RestAPI.Services.GameResultService;
import TicTacToeApp.RestAPI.Services.GameResultServiceImpl;
import TicTacToeApp.RestAPI.Services.GameboardService;
import TicTacToeApp.RestAPI.Services.GameboardServiceImpl;
import TicTacToeApp.RestAPI.Services.PlayerService;
import TicTacToeApp.RestAPI.Services.PlayerServiceImpl;
import TicTacToeApp.RestAPI.Services.StepService;
import TicTacToeApp.RestAPI.Services.StepServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {
    private final PlayerService playerService = new PlayerServiceImpl();
    private final GameboardService gameboardService = new GameboardServiceImpl();
    private final StepService stepService = new StepServiceImpl();
    private final GameResultService gameResultService = new GameResultServiceImpl();
    GameController gameController = new GameController(playerService, gameboardService, stepService, gameResultService);

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
        assertEquals(new ResponseEntity<>("Игра запущена\nПередайте имя первого игрока", HttpStatus.CREATED),
                gameController.startGame());
    }

    @Test
    public void nameSetTest() {
        assertEquals(new ResponseEntity<>("Cначала запустите игру", HttpStatus.LOCKED),
                gameController.updateFirstPlayerName(new Player("Roma")));

        assertEquals(new ResponseEntity<>("Cначала запустите игру", HttpStatus.LOCKED),
                gameController.updateSecondPlayerName(new Player("Arseniy")));

        gameController.startGame();

        assertEquals(new ResponseEntity<>("Введено неверное значение", HttpStatus.NOT_FOUND),
                gameController.updateFirstPlayerName(new Player(null)));

        assertEquals(new ResponseEntity<>("Введено неверное значение", HttpStatus.NOT_FOUND),
                gameController.updateSecondPlayerName(new Player(null)));

        assertEquals(new ResponseEntity<>("Передайте имя второго игрока", HttpStatus.OK),
                gameController.updateFirstPlayerName(new Player("Roma")));

        assertEquals(new ResponseEntity<>("Приступайте к игре \n" +
                        gameboardService.read(), HttpStatus.OK),
                gameController.updateSecondPlayerName(new Player("Arseniy")));

    }

    @Test
    void makeStepTest() {
        assertEquals(new ResponseEntity<>("Сначала запустите игру", HttpStatus.LOCKED),
                gameController.makeStepByFirstPlayer(new Step(1)));

        assertEquals(new ResponseEntity<>("Сначала запустите игру", HttpStatus.LOCKED),
                gameController.makeStepBySecondPlayer(new Step(1)));

        gameController.startGame();

        assertEquals(new ResponseEntity<>("Задайте имена игрокам", HttpStatus.LOCKED),
                gameController.makeStepByFirstPlayer(new Step(1)));

        assertEquals(new ResponseEntity<>("Задайте имена игрокам", HttpStatus.LOCKED),
                gameController.makeStepBySecondPlayer(new Step(1)));

        gameController.updateFirstPlayerName(new Player("Roma"));
        gameController.updateSecondPlayerName(new Player("Arseniy"));

        assertEquals(new ResponseEntity<>(gameboardService.read()
                        + "\nОшибка, сейчас не Ваш ход", HttpStatus.LOCKED),
                gameController.makeStepBySecondPlayer(new Step(1)));

        assertEquals(new ResponseEntity<>("""
                        Выберите ячейку(1-9):\s
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

        assertEquals(new ResponseEntity<>("Игрок " + 1 + " был удален, создайте нового", HttpStatus.OK),
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
                Выберите ячейку(1-9):\s
                 |-|O|-|
                 |-|-|-|
                 |-|-|-|

                Шаг был удален""", HttpStatus.OK), gameController.deleteStep(0));
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
        assertEquals(new ResponseEntity<>("\nВыберите ячейку(1-9): \n" +
                " |X|O|X|\n" +
                " |O|X|O|\n" +
                " |X|-|-|\n" +
                "\n" +
                playerService.read(1).getName()
                + " победил\nИгра окончена", HttpStatus.OK), gameController.makeStepByFirstPlayer(new Step(7)));

        assertEquals(new ResponseEntity<>("\nВыберите ячейку(1-9): \n" +
                " |X|O|X|\n" +
                " |O|X|O|\n" +
                " |X|-|-|\n" +
                "\n" +
                playerService.read(1).getName()
                + " победил\nИгра окончена", HttpStatus.OK), gameController.readResult());
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

        assertEquals(new ResponseEntity<>("""
                \nВыберите ячейку(1-9):\s
                 |X|O|X|
                 |X|O|X|
                 |O|X|O|

                Ничья
                Игра окончена""", HttpStatus.OK), gameController.readResult());

        assertEquals(new ResponseEntity<>((gameboardService.read()
                        + "\nИгра окончена, вы можете перезапустить её"), HttpStatus.LOCKED),
                gameController.makeStepByFirstPlayer(new Step(1)));

        assertEquals(new ResponseEntity<>((gameboardService.read()
                        + "\nИгра окончена, вы можете перезапустить её"), HttpStatus.LOCKED),
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

        assertEquals(new ResponseEntity<>("""
                [
                Выберите ячейку(1-9):\s
                 |X|O|X|
                 |O|X|O|
                 |X|-|-|

                Roma победил
                Игра окончена]""", HttpStatus.OK).toString(), gameController.readResults().toString());

        assertEquals(new ResponseEntity<>((gameboardService.read()
                        + "\nИгра окончена, вы можете перезапустить её"), HttpStatus.LOCKED),
                gameController.makeStepByFirstPlayer(new Step(1)));

        assertEquals(new ResponseEntity<>("Игра перезапущена\nПередайте имя первого игрока", HttpStatus.CREATED),
                gameController.restartGame());

        String[][] defalutField = {{"1", "2", "3"}, {"4", "5", "6"}, {"7", "8", "9"}};

        assertArrayEquals(defalutField, gameboardService.getGameboard().getField());
        assertTrue(playerService.readAll().isEmpty());
        assertTrue(stepService.readAll().isEmpty());

        assertEquals(new ResponseEntity<>("Передайте имя второго игрока", HttpStatus.OK),
                gameController.updateFirstPlayerName(new Player("Roma")));

        assertEquals(new ResponseEntity<>("Приступайте к игре \n" +
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

        assertEquals(new ResponseEntity<>("""
                [
                Выберите ячейку(1-9):\s
                 |X|O|X|
                 |X|O|X|
                 |O|X|O|

                Ничья
                Игра окончена,\s
                Выберите ячейку(1-9):\s
                 |X|O|X|
                 |O|X|O|
                 |X|-|-|

                Roma победил
                Игра окончена]""", HttpStatus.OK).toString(), gameController.readResults().toString());


        assertEquals(new ResponseEntity<>("Результаты были удалены", HttpStatus.OK),
                gameController.deleteResults());

        assertTrue(Objects.requireNonNull(gameController.readResults().getBody()).isEmpty());

    }
}
