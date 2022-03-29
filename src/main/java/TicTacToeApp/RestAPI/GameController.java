package TicTacToeApp.RestAPI;

import TicTacToeApp.Objects.Player;
import TicTacToeApp.Objects.Step;
import TicTacToeApp.RestAPI.Services.GameboardService;
import TicTacToeApp.RestAPI.Services.PlayerService;
import TicTacToeApp.RestAPI.Services.StepService;
import TicTacToeApp.TicTacToe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GameController {
    private final PlayerService playerService;
    private final GameboardService gameboardService;
    private final StepService stepService;
    private String gameResult;
    private int finishChecker = 0;


    @Autowired
    public GameController(PlayerService playerService,
                          GameboardService gameboardService, StepService stepService) {
        this.playerService = playerService;
        this.gameboardService = gameboardService;
        this.stepService = stepService;
    }

    @PostMapping(value = "/gameplay/start")
    public ResponseEntity<String> startGame() {
        gameboardService.create();
        return new ResponseEntity<>("Передайте имя первого игрока", HttpStatus.CREATED);
    }

    @PostMapping(value = "/gameplay/stop")
    public ResponseEntity<String> restartGame() {
        System.exit(0);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/gameplay/player1/set/name")
    public ResponseEntity<String> updateFirstPlayerName(@RequestBody Player player) {
        if (gameboardService.getGAMEBOARD() == null)
            return new ResponseEntity<>("Cначала запустите игру", HttpStatus.LOCKED);

        if (finishChecker != 0) {
            return new ResponseEntity<>(("Игра окончена"), HttpStatus.OK);
        }

        playerService.create(1, player.getName(), "X");
        return player.getName() != null ?
                new ResponseEntity<>("Передайте имя второго игрока", HttpStatus.OK)
                : new ResponseEntity<>("Введено неверное значение", HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/gameplay/player2/set/name")
    public ResponseEntity<String> updateSecondPlayerName(@RequestBody Player player) {
        if (gameboardService.getGAMEBOARD() == null)
            return new ResponseEntity<>("Cначала запустите игру", HttpStatus.LOCKED);

        if (finishChecker != 0) {
            return new ResponseEntity<>(("Игра окончена"), HttpStatus.OK);
        }

        playerService.create(2, player.getName(), "O");
        return player.getName() != null
                ? new ResponseEntity<String>("Приступайте к игре \n" + gameboardService.read(), HttpStatus.OK)
                : new ResponseEntity<>("Введено неверное значение", HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/gameplay/player1/set/step")
    public ResponseEntity<String> makeStepByFirstPlayer(@RequestBody Step step) {
        if (gameboardService.getGAMEBOARD() == null) {
            return new ResponseEntity<>("Сначала запустите игру", HttpStatus.LOCKED);
        } else if ((playerService.read(1) == null)) {
            return new ResponseEntity<>("Задайте имена игрокам", HttpStatus.LOCKED);
        }

        if (finishChecker != 0) {
            return new ResponseEntity<>(("Игра окончена"), HttpStatus.OK);
        }

        if (stepService.readAll().size() % 2 == 0) {

            if (!gameboardService.update(1, step.getCell())) {
                return new ResponseEntity<>(gameboardService.read() + "\nВведено неверное значение", HttpStatus.OK);
            }

            stepService.create(new Step(stepService.readAll().size() + 1, 1, step.getCell()));

            finishChecker = TicTacToe.toCheckWin(gameboardService.getGAMEBOARD(), stepService.readAll().size());

            if (finishChecker == 2) {
                return new ResponseEntity<>((gameboardService.read() + "\nНичья\nИгра окончена"), HttpStatus.OK);
            } else if (finishChecker == 1) {
                gameResult = gameboardService.read() + "\n" +
                        playerService.read(1).getName() + " победил\nИгра окончена";
                return new ResponseEntity<>(gameResult, HttpStatus.OK);
            }

            return new ResponseEntity<>(gameboardService.read(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(gameboardService.read() + "\nОшибка, сейчас не Ваш ход", HttpStatus.OK);
        }
    }

    @PutMapping(value = "/gameplay/player2/set/step")
    public ResponseEntity<String> makeStepBySecondPlayer(@RequestBody Step step) {
        if (gameboardService.getGAMEBOARD() == null) {
            return new ResponseEntity<>("Сначала запустите игру", HttpStatus.LOCKED);
        } else if ((playerService.read(2) == null)) {
            return new ResponseEntity<>("Задайте имена игрокам", HttpStatus.LOCKED);
        }

        if (finishChecker != 0) {
            return new ResponseEntity<>((gameboardService.read() + "\nИгра окончена"), HttpStatus.OK);
        }

        if (stepService.readAll().size() % 2 == 1) {

            if (!gameboardService.update(2, step.getCell())) {
                return new ResponseEntity<>(gameboardService.read() + "\nВведено неверное значение", HttpStatus.OK);
            }

            stepService.create(new Step(stepService.readAll().size() + 1, 2, step.getCell()));

            finishChecker = TicTacToe.toCheckWin(gameboardService.getGAMEBOARD(), stepService.readAll().size());

            if (finishChecker == 2) {
                return new ResponseEntity<>((gameboardService.read() + "\nНичья\nИгра окончена"), HttpStatus.OK);
            } else if (finishChecker == 1) {
                gameResult = gameboardService.read() + "\n" + playerService.read(2).getName()
                        + " победил\nИгра окончена";
                return new ResponseEntity<>(gameResult, HttpStatus.OK);
            }

            return new ResponseEntity<>(gameboardService.read(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(gameboardService.read() + "\nОшибка, сейчас не Ваш ход", HttpStatus.OK);
        }
    }

    @GetMapping(value = "/gameplay/result")
    public ResponseEntity<String> readPlayerInfo() {

        return gameResult != null
                ? new ResponseEntity<>(gameResult, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/gameplay/players/info")
    public ResponseEntity<List<Player>> readPlayersInfo() {
        final List<Player> players = playerService.readAll();

        return players != null && !players.isEmpty()
                ? new ResponseEntity<>(players, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/gameplay/steps/info")
    public ResponseEntity<List<Step>> readStepsInfo() {
        final List<Step> steps = stepService.readAll();

        return steps != null && !steps.isEmpty()
                ? new ResponseEntity<>(steps, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/gameplay/players/{id}")
    public ResponseEntity<Player> readPlayerInfo(@PathVariable(name = "id") int id) {
        final Player player = playerService.read(id);

        return player != null
                ? new ResponseEntity<>(player, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/gameplay/steps/{stepNum}")
    public ResponseEntity<Step> readStepInfo(@PathVariable(name = "stepNum") int stepNum) {
        final Step step = stepService.readAll().get(stepNum);

        return step != null
                ? new ResponseEntity<>(step, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/gameplay/{id}")
    public ResponseEntity<String> deletePlayer(@PathVariable(name = "id") int id) {
        final boolean deleted = playerService.delete(id);

        return deleted
                ? new ResponseEntity<>("Игрок " + id + " был удален, создайте нового", HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/gameplay/steps/{stepNum}")
    public ResponseEntity<String> deleteStep(@PathVariable(name = "stepNum") int stepNum) {
        gameboardService.getGAMEBOARD().setCellForSimulating(stepService.readAll().get(stepNum).getCell(),
                String.valueOf(stepService.readAll().get(stepNum).getCell()));
        final boolean deleted = stepService.delete(stepNum);

        return deleted
                ? new ResponseEntity<>(gameboardService.read() + "\nШаг был удален", HttpStatus.OK)
                : new ResponseEntity<>(gameboardService.read(), HttpStatus.NOT_MODIFIED);
    }

}