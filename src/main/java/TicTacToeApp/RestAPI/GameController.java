package TicTacToeApp.RestAPI;

import TicTacToeApp.Objects.Player;
import TicTacToeApp.Objects.Step;
import TicTacToeApp.RestAPI.Services.GameResultService;
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
    private final GameResultService gameResultService;

    @Autowired
    public GameController(PlayerService playerService, GameboardService gameboardService,
                          StepService stepService, GameResultService gameResultService) {
        this.playerService = playerService;
        this.gameboardService = gameboardService;
        this.stepService = stepService;
        this.gameResultService = gameResultService;
    }

    @PostMapping(value = "/gameplay/start")
    public ResponseEntity<String> startGame() {
        gameboardService.create();
        return new ResponseEntity<>("Игра запущена\nПередайте имя первого игрока", HttpStatus.CREATED);
    }

    @PostMapping(value = "/gameplay/restart")
    public ResponseEntity<String> restartGame() {
        gameboardService.delete();
        playerService.deleteAll();
        stepService.deleteAll();
        gameResultService.setFinishChecker(0);
        startGame();
        return new ResponseEntity<>("Игра перезапущена\nПередайте имя первого игрока", HttpStatus.CREATED);
    }

    @PostMapping(value = "/gameplay/player1/set/name")
    public ResponseEntity<String> updateFirstPlayerName(@RequestBody Player player) {
        if (toCheckIsGameInProcess() != null)
            return toCheckIsGameInProcess();

        playerService.create(1, player.getName(), "X");
        return player.getName() != null ?
                new ResponseEntity<>("Передайте имя второго игрока", HttpStatus.OK)
                : new ResponseEntity<>("Введено неверное значение", HttpStatus.NOT_FOUND);

    }

    @PostMapping(value = "/gameplay/player2/set/name")
    public ResponseEntity<String> updateSecondPlayerName(@RequestBody Player player) {
        if (toCheckIsGameInProcess() != null)
            return toCheckIsGameInProcess();

        playerService.create(2, player.getName(), "O");
        return player.getName() != null
                ? new ResponseEntity<>("Приступайте к игре \n" + gameboardService.read(), HttpStatus.OK)
                : new ResponseEntity<>("Введено неверное значение", HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/gameplay/player1/set/step")
    public ResponseEntity<String> makeStepByFirstPlayer(@RequestBody Step step) {
        if (toRunMakeStepChecks(1) != null)
            return toRunMakeStepChecks(1);

        if (toCheckIsCellModified(1, step) != null)
            return toCheckIsCellModified(1, step);

        stepService.create(new Step(stepService.readAll().size() + 1, 1, step.getCell()));
        gameResultService.setFinishChecker(TicTacToe.toCheckWin(gameboardService.getGameboard(),
                stepService.readAll().size()));

        ResponseEntity<String> entity = toCheckIsSomeoneWin(1);
        if (entity != null)
            return entity;

        return new ResponseEntity<>(gameboardService.read(), HttpStatus.OK);
    }

    @PutMapping(value = "/gameplay/player2/set/step")
    public ResponseEntity<String> makeStepBySecondPlayer(@RequestBody Step step) {
        if (toRunMakeStepChecks(2) != null)
            return toRunMakeStepChecks(2);

        if (toCheckIsCellModified(2, step) != null)
            return toCheckIsCellModified(2, step);

        stepService.create(new Step(stepService.readAll().size() + 1, 2, step.getCell()));
        gameResultService.setFinishChecker(TicTacToe.toCheckWin(gameboardService.getGameboard(),
                stepService.readAll().size()));

        ResponseEntity<String> entity = toCheckIsSomeoneWin(2);
        if (entity != null)
            return entity;

        return new ResponseEntity<>(gameboardService.read(), HttpStatus.OK);
    }

    @GetMapping(value = "/gameplay/result")
    public ResponseEntity<String> readResult() {

        return gameResultService.getGameResult() != null
                ? new ResponseEntity<>(gameResultService.getGameResult(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/gameplay/results")
    public ResponseEntity<List<String>> readResults() {
        final List<String> results = gameResultService.readAll();

        return results != null
                ? new ResponseEntity<>(results, HttpStatus.OK)
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
        if (stepService.readAll().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        final Step step = stepService.read(stepNum);

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
        gameboardService.getGameboard().setCellForSimulating(stepService.readAll().get(stepNum).getCell(),
                String.valueOf(stepService.readAll().get(stepNum).getCell()));
        final boolean deleted = stepService.delete(stepNum);

        return deleted
                ? new ResponseEntity<>(gameboardService.read() + "\nШаг был удален", HttpStatus.OK)
                : new ResponseEntity<>(gameboardService.read(), HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/gameplay/results/delete")
    public ResponseEntity<String> deleteResults() {
        gameResultService.deleteAll();

        return gameResultService.readAll().isEmpty()
                ? new ResponseEntity<>("Результаты были удалены", HttpStatus.OK)
                : new ResponseEntity<>(gameboardService.read(), HttpStatus.NOT_MODIFIED);
    }

    private ResponseEntity<String> toRunMakeStepChecks(int playerId) {
        if (gameboardService.getGameboard() == null) {
            return new ResponseEntity<>("Сначала запустите игру", HttpStatus.LOCKED);
        } else if ((playerService.read(1) == null)) {
            return new ResponseEntity<>("Задайте имена игрокам", HttpStatus.LOCKED);
        } else if (gameResultService.getFinishChecker() != 0) {
            return new ResponseEntity<>((gameboardService.read()
                    + "\nИгра окончена, вы можете перезапустить её"), HttpStatus.LOCKED);
        } else if (!(stepService.readAll().size() % 2 == playerId - 1)) {
            return new ResponseEntity<>(gameboardService.read()
                    + "\nОшибка, сейчас не Ваш ход", HttpStatus.LOCKED);
        }
        return null;
    }

    private ResponseEntity<String> toCheckIsSomeoneWin(int playerId) {
        if (gameResultService.getFinishChecker() == 2) {
            gameResultService.add("\n" + gameboardService.read() + "\nНичья\nИгра окончена");
            return new ResponseEntity<>(gameResultService.getGameResult(), HttpStatus.OK);
        } else if (gameResultService.getFinishChecker() == 1) {
            gameResultService.add("\n" + gameboardService.read() + "\n" +
                    playerService.read(playerId).getName() + " победил\nИгра окончена");
            return new ResponseEntity<>(gameResultService.getGameResult(), HttpStatus.OK);
        }
        return null;
    }

    private ResponseEntity<String> toCheckIsCellModified(int playerId, Step step) {
        if (!gameboardService.update(playerId, step.getCell())) {
            return new ResponseEntity<>(gameboardService.read() + "\nВведено неверное значение", HttpStatus.OK);
        }
        return null;
    }

    private ResponseEntity<String> toCheckIsGameInProcess() {
        if (gameboardService.getGameboard() == null) {
            return new ResponseEntity<>("Cначала запустите игру", HttpStatus.LOCKED);
        } else if (gameResultService.getFinishChecker() != 0) {
            return new ResponseEntity<>(("Игра окончена, вы можете перезапустить её"), HttpStatus.LOCKED);
        }
        return null;
    }

}