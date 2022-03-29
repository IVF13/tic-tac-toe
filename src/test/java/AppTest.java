import TicTacToeApp.Objects.Player;
import TicTacToeApp.RestAPI.GameController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {
    static GameController gameController;

    @BeforeAll
    public static void toStartAPI() {
        gameController = new GameController();
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

}
