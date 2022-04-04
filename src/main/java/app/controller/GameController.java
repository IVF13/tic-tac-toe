package app.controller;

import app.models.GameResult;
import app.models.GameplayData;
import app.models.Player;
import app.models.Step;
import app.repository.GameplayDataRepository;
import app.services.GameResultService;
import app.services.GameboardService;
import app.services.GameplayDataService;
import app.services.PlayerService;
import app.services.StepService;
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
    private final GameplayDataService gameplayDataService;
    private final GameplayDataRepository gameplayDataRepository;

    @Autowired
    public GameController(PlayerService playerService, GameboardService gameboardService,
                          StepService stepService, GameResultService gameResultService, GameplayDataService gameplayDataService, GameplayDataRepository gameplayDataRepository) {
        this.playerService = playerService;
        this.gameboardService = gameboardService;
        this.stepService = stepService;
        this.gameResultService = gameResultService;
        this.gameplayDataService = gameplayDataService;
        this.gameplayDataRepository = gameplayDataRepository;
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
        if (playerService.toCheckIsGameInProcess(gameboardService, gameResultService) != null)
            return playerService.toCheckIsGameInProcess(gameboardService, gameResultService);

        playerService.create(1, player.getName(), "X");
        return player.getName() != null ?
                new ResponseEntity<>("Передайте имя второго игрока", HttpStatus.OK)
                : new ResponseEntity<>("Введено неверное значение", HttpStatus.NOT_FOUND);

    }

    @PostMapping(value = "/gameplay/player2/set/name")
    public ResponseEntity<String> updateSecondPlayerName(@RequestBody Player player) {
        if (playerService.toCheckIsGameInProcess(gameboardService, gameResultService) != null)
            return playerService.toCheckIsGameInProcess(gameboardService, gameResultService);

        playerService.create(2, player.getName(), "O");
        return player.getName() != null
                ? new ResponseEntity<>("Приступайте к игре \n" + gameboardService.read(), HttpStatus.OK)
                : new ResponseEntity<>("Введено неверное значение", HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/gameplay/player1/set/step")
    public ResponseEntity<String> makeStepByFirstPlayer(@RequestBody Step step) {
        if (stepService.toRunMakeNewStepChecks(1, gameboardService, playerService, gameResultService) != null)
            return stepService.toRunMakeNewStepChecks(1, gameboardService, playerService, gameResultService);

        if (gameboardService.toModifyCell(1, step) != null)
            return gameboardService.toModifyCell(1, step);

        stepService.toMakeNewStep(step, 1, gameResultService, gameboardService);

        ResponseEntity<String> entity = gameResultService
                .toCheckIsSomeoneWon(1, gameboardService, playerService);
        if (entity != null)
            return entity;

        return new ResponseEntity<>(gameboardService.read(), HttpStatus.OK);
    }

    @PutMapping(value = "/gameplay/player2/set/step")
    public ResponseEntity<String> makeStepBySecondPlayer(@RequestBody Step step) {
        if (stepService.toRunMakeNewStepChecks(2, gameboardService, playerService, gameResultService) != null)
            return stepService.toRunMakeNewStepChecks(2, gameboardService, playerService, gameResultService);

        if (gameboardService.toModifyCell(2, step) != null)
            return gameboardService.toModifyCell(2, step);

        stepService.toMakeNewStep(step, 2, gameResultService, gameboardService);

        ResponseEntity<String> entity = gameResultService
                .toCheckIsSomeoneWon(2, gameboardService, playerService);
        if (entity != null)
            return entity;

        return new ResponseEntity<>(gameboardService.read(), HttpStatus.OK);
    }

    @GetMapping(value = "/gameplay/result")
    public ResponseEntity<String> readResult() {

        return gameResultService.getGameResult() != null
                ? new ResponseEntity<>(gameResultService.getGameResult().toString(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/gameplay/results")
    public ResponseEntity<List<GameResult>> readResults() {
        final List<GameResult> results = gameResultService.readAll();

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

    @DeleteMapping(value = "/gameplay/players/{id}")
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

    @PutMapping(value = "/gameplay/save/last/game/to/db")
    public ResponseEntity<String> saveResultsToDB() {
        if (gameResultService.readAll().isEmpty()) {
            new ResponseEntity<>("Игра должна быть закончена перед сохранением", HttpStatus.NOT_MODIFIED);
        }

        GameplayData gameplayData = new GameplayData();
        playerService.readAll().forEach(x -> x.setGameplayData(gameplayData));
        stepService.readAll().forEach(x -> x.setGameplayData(gameplayData));

        gameplayData.setPlayers(playerService.readAll());
        gameplayData.setStepsToWrite(stepService.readAll());
        gameplayData.setGameResult(List.of(gameResultService.getGameResult()));

        GameplayData savedGameplay = gameplayDataRepository.save(gameplayData);

        return savedGameplay.equals(gameplayData)
                ? new ResponseEntity<>("Результаты были сохранены в базу данных\n" + savedGameplay, HttpStatus.OK)
                : new ResponseEntity<>("Произошла ошибка", HttpStatus.NOT_MODIFIED);
    }

    @GetMapping(value = "/gameplay/find/game/byId/in/db")
    public ResponseEntity<String> findByPlayerInDB(@RequestBody Long id) {
        GameplayData gameplayDataList = gameplayDataService.findById(id);

        return gameplayDataList != null
                ? new ResponseEntity<>(gameplayDataList.toString(), HttpStatus.OK)
                : new ResponseEntity<>("Не найдено", HttpStatus.NOT_MODIFIED);
    }

    @GetMapping(value = "/gameplay/findAll/games/in/db")
    public ResponseEntity<String> findAllInDB() {
        List<GameplayData> gameplayDataList = gameplayDataService.findAll();

        return gameplayDataList != null
                ? new ResponseEntity<>(gameplayDataList.toString(), HttpStatus.OK)
                : new ResponseEntity<>("База данных пуста", HttpStatus.NOT_MODIFIED);
    }

    @GetMapping(value = "/gameplay/find/game/byPlayer/in/db")
    public ResponseEntity<String> findByPlayerInDB(@RequestBody Player player) {
        List<GameplayData> gameplayDataList = gameplayDataService.findByPlayer(player);

        return new ResponseEntity<>(gameplayDataList.toString(), HttpStatus.OK);
    }

    @GetMapping(value = "/gameplay/find/game/byGameResult/in/db")
    public ResponseEntity<String> findByGameResultInDB(@RequestBody GameResult gameResult) {
        List<GameplayData> gameplayDataList = gameplayDataService.findByGameResult(gameResult);

        return gameplayDataList != null
                ? new ResponseEntity<>(gameplayDataList.toString(), HttpStatus.OK)
                : new ResponseEntity<>("Не найдено", HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/gameplay/delete/game/byId/from/db")
    public ResponseEntity<String> deleteByIdFromDB(@RequestBody Long id) {
        gameplayDataRepository.deleteById(id);

        return new ResponseEntity<>("Запись игры удалена", HttpStatus.OK);
    }

    @DeleteMapping(value = "/gameplay/deleteAll/games/from/db")
    public ResponseEntity<String> deleteAllFromDB(@RequestBody List<GameplayData> gameplayDataList) {
        gameplayDataRepository.deleteAll(gameplayDataList);

        return new ResponseEntity<>("Записи игры удалена", HttpStatus.OK);
    }

}