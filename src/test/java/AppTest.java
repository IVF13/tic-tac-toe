import TicTacToeApp.Objects.Player;
import TicTacToeApp.Objects.Step;
import TicTacToeApp.RestAPI.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {
    GameController gameController;

    @BeforeEach
    public void toStartAPI() {
        gameController = new GameController();
        gameController.getGameboardService().delete();
        gameController.getPlayerService().delete(1);
        gameController.getPlayerService().delete(2);
        gameController.getStepService().deleteAll();
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
                        gameController.getGameboardService().read(), HttpStatus.OK),
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

        assertEquals(new ResponseEntity<>(gameController.getGameboardService().read()
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

        assertEquals(new ResponseEntity<>(gameController.getPlayerService().readAll(), HttpStatus.OK),
                gameController.readPlayersInfo());
    }

    @Test
    void readPlayerInfoTest() {
        gameController.startGame();

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND), gameController.readPlayerInfo(1));

        gameController.updateFirstPlayerName(new Player("Roma"));

        assertEquals(new ResponseEntity<>(gameController.getPlayerService().read(1), HttpStatus.OK),
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

        assertEquals(new ResponseEntity<>(gameController.getStepService().readAll(), HttpStatus.OK),
                gameController.readStepsInfo());
    }

    @Test
    void readStepInfoTest() {
        gameController.startGame();

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND), gameController.readStepInfo(0));

        gameController.updateFirstPlayerName(new Player("Roma"));
        gameController.updateSecondPlayerName(new Player("Arseniy"));

        gameController.makeStepByFirstPlayer(new Step(1));

        assertEquals(new ResponseEntity<>(gameController.getStepService().readAll().get(0), HttpStatus.OK),
                gameController.readStepInfo(0));
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
    void toPlayTicTacToeWthRESTAPITestWin() {
        gameController.startGame();

        gameController.updateFirstPlayerName(new Player("Roma"));
        gameController.updateSecondPlayerName(new Player("Arseniy"));

        gameController.makeStepByFirstPlayer(new Step(1));
        gameController.makeStepBySecondPlayer(new Step(2));
        gameController.makeStepByFirstPlayer(new Step(3));
        gameController.makeStepBySecondPlayer(new Step(4));
        gameController.makeStepByFirstPlayer(new Step(5));
        gameController.makeStepBySecondPlayer(new Step(6));
        assertEquals(new ResponseEntity<>("Выберите ячейку(1-9): \n" +
                " |X|O|X|\n" +
                " |O|X|O|\n" +
                " |X|-|-|\n" +
                "\n" +
                gameController.getPlayerService().read(1).getName()
                + " победил\nИгра окончена", HttpStatus.OK), gameController.makeStepByFirstPlayer(new Step(7)));

        assertEquals("Выберите ячейку(1-9): \n" +
                " |X|O|X|\n" +
                " |O|X|O|\n" +
                " |X|-|-|\n" +
                "\n" +
                gameController.getPlayerService().read(1).getName()
                + " победил\nИгра окончена", gameController.getGameResult());
    }

    @Test
    void toPlayTicTacToeWthRESTAPITestDraw() {
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

        assertEquals("""
                Выберите ячейку(1-9):\s
                 |X|O|X|
                 |X|O|X|
                 |O|X|O|

                Ничья
                Игра окончена""", gameController.getGameResult());

        assertEquals(new ResponseEntity<>((gameController.getGameboardService().read() + "\nИгра окончена"),
                HttpStatus.LOCKED), gameController.makeStepByFirstPlayer(new Step(1)));

        assertEquals(new ResponseEntity<>((gameController.getGameboardService().read() + "\nИгра окончена"),
                HttpStatus.LOCKED), gameController.makeStepBySecondPlayer(new Step(1)));
    }

}
