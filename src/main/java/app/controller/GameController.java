package app.controller;

import app.GameSimulator;
import app.Logger;
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

import java.io.File;
import java.util.Date;
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
        return new ResponseEntity<>("Game started\nEnter the name of the first player", HttpStatus.CREATED);
    }

    @PostMapping(value = "/gameplay/restart")
    public ResponseEntity<String> restartGame() {
        gameboardService.delete();
        playerService.deleteAll();
        stepService.deleteAll();
        gameResultService.setFinishChecker(0);
        startGame();
        return new ResponseEntity<>("Game restarted\nEnter the name of the first player", HttpStatus.CREATED);
    }

    @PostMapping(value = "/gameplay/player1/set/name")
    public ResponseEntity<String> updateFirstPlayerName(@RequestBody Player player) {
        if (playerService.toCheckIsGameInProcess(gameboardService, gameResultService) != null)
            return playerService.toCheckIsGameInProcess(gameboardService, gameResultService);

        playerService.create(1, player.getName(), "X");
        return player.getName() != null ?
                new ResponseEntity<>("Enter the name of the second player", HttpStatus.OK)
                : new ResponseEntity<>("Invalid value entered", HttpStatus.NOT_FOUND);

    }

    @PostMapping(value = "/gameplay/player2/set/name")
    public ResponseEntity<String> updateSecondPlayerName(@RequestBody Player player) {
        if (playerService.toCheckIsGameInProcess(gameboardService, gameResultService) != null)
            return playerService.toCheckIsGameInProcess(gameboardService, gameResultService);

        playerService.create(2, player.getName(), "O");
        return player.getName() != null
                ? new ResponseEntity<>("Get started \n" + gameboardService.read(), HttpStatus.OK)
                : new ResponseEntity<>("Invalid value entered", HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/gameplay/player{id}/set/step")
    public ResponseEntity<String> makeStep(@PathVariable(name = "id") int id, @RequestBody Step step) {
        if (stepService.toRunMakeNewStepChecks(id, gameboardService, playerService, gameResultService) != null)
            return stepService.toRunMakeNewStepChecks(id, gameboardService, playerService, gameResultService);

        if (gameboardService.toModifyCell(id, step) != null)
            return gameboardService.toModifyCell(id, step);

        stepService.toMakeNewStep(step, id, gameResultService, gameboardService);

        ResponseEntity<String> entity = gameResultService
                .toCheckIsSomeoneWon(id, gameboardService, playerService);
        if (entity != null) {
            toGetWriteLogInfo();
            return entity;
        }

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
                ? new ResponseEntity<>("Player " + id + " was deleted, create the new one", HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/gameplay/steps/{stepNum}")
    public ResponseEntity<String> deleteStep(@PathVariable(name = "stepNum") int stepNum) {
        gameboardService.getGameboard().setCellForSimulating(stepService.readAll().get(stepNum).getCell(),
                String.valueOf(stepService.readAll().get(stepNum).getCell()));
        final boolean deleted = stepService.delete(stepNum);

        return deleted
                ? new ResponseEntity<>(gameboardService.read() + "\nStep was deleted", HttpStatus.OK)
                : new ResponseEntity<>(gameboardService.read(), HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/gameplay/results/delete")
    public ResponseEntity<String> deleteResults() {
        gameResultService.deleteAll();

        return gameResultService.readAll().isEmpty()
                ? new ResponseEntity<>("Results were deleted", HttpStatus.OK)
                : new ResponseEntity<>(gameboardService.read(), HttpStatus.NOT_MODIFIED);
    }

    @GetMapping(value = "/gameplay/write/log/info")
    public ResponseEntity<String> toGetWriteLogInfo() {
        if (gameResultService.getFinishChecker() == 0)
            return new ResponseEntity<>("The game is not finished", HttpStatus.LOCKED);

        return new ResponseEntity<>("""
                Select log format:\s
                1 - XML File\s
                2 - JSON File\s
                3 - XML & JSON Files\s
                4 - JSON File & String\s
                5 - All formats\s
                default: TXT\s""", HttpStatus.OK);
    }

    @PostMapping(value = "/gameplay/write/log/{menuItemNum}")
    public ResponseEntity<String> toWriteLog(@PathVariable(name = "menuItemNum") int menuItemNum) {
        if (gameResultService.getFinishChecker() == 0)
            return new ResponseEntity<>("The game is not finished", HttpStatus.LOCKED);

        String json = Logger.toWriteTheLog(playerService.readAll(), stepService.readAll(),
                gameResultService.getFinishChecker(), menuItemNum);

        return new ResponseEntity<>("The log successfully written\n" + json, HttpStatus.OK);
    }

    @GetMapping(value = "/gameplay/simulate/info")
    public ResponseEntity<String> toSimulateTheGameInfo() {

        return new ResponseEntity<>("Select the log by which the game will be played: " +
                "1 - XML changed: " + new Date(new File("src/main/resources/gameplay.xml").lastModified())
                + "\n"
                + "2 - JSON changed: " + new Date(new File("src/main/resources/gameplay.json").lastModified())
                + "\n",
                HttpStatus.OK);
    }

    @GetMapping(value = "/gameplay/simulate/{menuItemNum}")
    public ResponseEntity<String> toSimulateTheGame(@PathVariable(name = "menuItemNum") int menuItemNum) {

        return new ResponseEntity<>(GameSimulator.toBuildGameSimulation(menuItemNum),
                HttpStatus.OK);
    }

    @PutMapping(value = "/gameplay/save/last/game/to/db")
    public ResponseEntity<String> saveResultsToDB() {
        if (gameResultService.readAll().isEmpty()) {
            new ResponseEntity<>("The game must be finished at first", HttpStatus.NOT_FOUND);
        }

        GameplayData gameplayData = new GameplayData();
        playerService.readAll().forEach(x -> x.setGameplayData(gameplayData));
        stepService.readAll().forEach(x -> x.setGameplayData(gameplayData));

        gameplayData.setPlayers(playerService.readAll());
        gameplayData.setStepsToWrite(stepService.readAll());
        gameplayData.setGameResult(List.of(gameResultService.getGameResult()));

        GameplayData savedGameplay = gameplayDataRepository.save(gameplayData);

        return savedGameplay.equals(gameplayData)
                ? new ResponseEntity<>("Results were saved to the database\n", HttpStatus.OK)
                : new ResponseEntity<>("An error occurred", HttpStatus.NOT_MODIFIED);
    }

    @GetMapping(value = "/gameplay/find/game/byId/in/db")
    public ResponseEntity<String> findByPlayerInDB(@RequestBody Long id) {
        GameplayData gameplayDataList = gameplayDataService.findById(id);

        return new ResponseEntity<>(gameplayDataList.toString(), HttpStatus.OK);
    }

    @GetMapping(value = "/gameplay/findAll/games/in/db")
    public ResponseEntity<String> findAllInDB() {
        List<GameplayData> gameplayDataList = gameplayDataService.findAll();

        return new ResponseEntity<>(gameplayDataList.toString(), HttpStatus.OK);
    }

    @GetMapping(value = "/gameplay/find/game/byPlayer/in/db")
    public ResponseEntity<String> findByPlayerInDB(@RequestBody Player player) {
        List<GameplayData> gameplayDataList = gameplayDataService.findByPlayer(player);

        return new ResponseEntity<>(gameplayDataList.toString(), HttpStatus.OK);
    }

    @GetMapping(value = "/gameplay/find/game/byGameResult/in/db")
    public ResponseEntity<String> findByGameResultInDB(@RequestBody GameResult gameResult) {
        List<GameplayData> gameplayDataList = gameplayDataService.findByGameResult(gameResult);

        return new ResponseEntity<>(gameplayDataList.toString(), HttpStatus.OK);

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