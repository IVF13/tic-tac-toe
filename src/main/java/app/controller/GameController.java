package app.controller;

import app.exceptions.InvalidValueException;
import app.exceptions.NotFinishedException;
import app.exceptions.NotFoundException;
import app.exceptions.NotModifiedException;
import app.models.GameResult;
import app.models.Player;
import app.models.Step;
import app.services.GameResultService;
import app.services.GameboardService;
import app.services.PlayerService;
import app.services.StepService;
import app.utils.GameConstants;
import app.utils.GameSimulator;
import app.utils.Logger;
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
    static PlayerService playerService = null;
    static GameboardService gameboardService = null;
    static StepService stepService = null;
    static GameResultService gameResultService = null;

    @Autowired
    public GameController(PlayerService playerService, GameboardService gameboardService,
                          StepService stepService, GameResultService gameResultService) {
        GameController.playerService = playerService;
        GameController.gameboardService = gameboardService;
        GameController.stepService = stepService;
        GameController.gameResultService = gameResultService;
    }

    @PostMapping(value = "/gameplay/start")
    public ResponseEntity<String> startGame() {
        gameboardService.create();
        return new ResponseEntity<>(GameConstants.START, HttpStatus.CREATED);
    }

    @PostMapping(value = "/gameplay/restart")
    public ResponseEntity<String> restartGame() {
        gameboardService.delete();
        playerService.deleteAll();
        stepService.deleteAll();
        gameResultService.setFinishChecker(0);
        startGame();
        return new ResponseEntity<>(GameConstants.RESTART, HttpStatus.CREATED);
    }

    @PostMapping(value = "/gameplay/player1/set/name")
    public ResponseEntity<String> updateFirstPlayerName(@RequestBody Player player) {
        if (playerService.toCheckIsGameInProcess(gameboardService, gameResultService) != null)
            return playerService.toCheckIsGameInProcess(gameboardService, gameResultService);

        playerService.create(1, player.getName(), GameConstants.X);


        try {
            if (player.getName() != null) {
                return new ResponseEntity<>(GameConstants.SECOND_PLAYER_NAME, HttpStatus.OK);
            } else {
                throw new InvalidValueException();
            }
        } catch (InvalidValueException e) {
            e.printStackTrace();
            return new ResponseEntity<>(GameConstants.INVALID_VALUE, HttpStatus.LOCKED);
        }

    }

    @PostMapping(value = "/gameplay/player2/set/name")
    public ResponseEntity<String> updateSecondPlayerName(@RequestBody Player player) {
        if (playerService.toCheckIsGameInProcess(gameboardService, gameResultService) != null)
            return playerService.toCheckIsGameInProcess(gameboardService, gameResultService);

        playerService.create(2, player.getName(), GameConstants.O);

        try {
            if (player.getName() != null) {
                return new ResponseEntity<>(GameConstants.GET_STARTED + gameboardService.read(), HttpStatus.OK);
            } else {
                throw new InvalidValueException();
            }
        } catch (InvalidValueException e) {
            e.printStackTrace();
            return new ResponseEntity<>(GameConstants.INVALID_VALUE, HttpStatus.LOCKED);
        }

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
        try {
            if (gameResultService.getGameResult() != null) {
                return new ResponseEntity<>(gameResultService.getGameResult().toString(), HttpStatus.OK);
            } else {
                throw new NotFoundException();
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/gameplay/results")
    public ResponseEntity<List<GameResult>> readResults() {
        final List<GameResult> results = gameResultService.readAll();

        try {
            if (results != null) {
                return new ResponseEntity<>(results, HttpStatus.OK);
            } else {
                throw new NotFoundException();
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/gameplay/players/info")
    public ResponseEntity<List<Player>> readPlayersInfo() {
        final List<Player> players = playerService.readAll();

        try {
            if (players != null && !players.isEmpty()) {
                return new ResponseEntity<>(players, HttpStatus.OK);
            } else {
                throw new NotFoundException();
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/gameplay/steps/info")
    public ResponseEntity<List<Step>> readStepsInfo() {
        final List<Step> steps = stepService.readAll();

        try {
            if (steps != null && !steps.isEmpty()) {
                return new ResponseEntity<>(steps, HttpStatus.OK);
            } else {
                throw new NotFoundException();
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/gameplay/players/{id}")
    public ResponseEntity<Player> readPlayerInfo(@PathVariable(name = "id") int id) {
        final Player player = playerService.read(id);

        try {
            if (player != null) {
                return new ResponseEntity<>(player, HttpStatus.OK);
            } else {
                throw new NotFoundException();
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/gameplay/steps/{stepNum}")
    public ResponseEntity<Step> readStepInfo(@PathVariable(name = "stepNum") int stepNum) {
        try {
            if (stepService.readAll().isEmpty()) {
                throw new NotFoundException();
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        final Step step = stepService.read(stepNum);

        try {
            if (step != null) {
                return new ResponseEntity<>(step, HttpStatus.OK);
            } else {
                throw new NotFoundException();
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/gameplay/players/{id}")
    public ResponseEntity<String> deletePlayer(@PathVariable(name = "id") int id) {
        final boolean deleted = playerService.delete(id);

        try {
            if (deleted) {
                return new ResponseEntity<>(GameConstants.PLAYER_WAS_DELETED, HttpStatus.OK);
            } else {
                throw new NotModifiedException();
            }
        } catch (NotModifiedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @DeleteMapping(value = "/gameplay/steps/{stepNum}")
    public ResponseEntity<String> deleteStep(@PathVariable(name = "stepNum") int stepNum) {
        gameboardService.getGameboard().setCellForSimulating(stepService.readAll().get(stepNum).getCell(),
                String.valueOf(stepService.readAll().get(stepNum).getCell()));
        final boolean deleted = stepService.delete(stepNum);

        try {
            if (deleted) {
                return new ResponseEntity<>(gameboardService.read()
                        + GameConstants.STEP_WAS_DELETED, HttpStatus.OK);
            } else {
                throw new NotModifiedException();
            }
        } catch (NotModifiedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @DeleteMapping(value = "/gameplay/results/delete")
    public ResponseEntity<String> deleteResults() {
        gameResultService.deleteAll();

        try {
            if (gameResultService.readAll().isEmpty()) {
                return new ResponseEntity<>(GameConstants.RESULTS_WERE_DELETED, HttpStatus.OK);
            } else {
                throw new NotModifiedException();
            }
        } catch (NotModifiedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(gameboardService.read(), HttpStatus.NOT_MODIFIED);
        }
    }

    @GetMapping(value = "/gameplay/write/log/info")
    public ResponseEntity<String> toGetWriteLogInfo() {
        try {
            if (gameResultService.getFinishChecker() == 0) {
                throw new NotFinishedException();
            } else {
                return new ResponseEntity<>(GameConstants.SELECT_THE_LOG_TO_WRITE, HttpStatus.OK);
            }
        } catch (NotFinishedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(GameConstants.NOT_FINISHED, HttpStatus.LOCKED);
        }
    }

    @PostMapping(value = "/gameplay/write/log/{menuItemNum}")
    public ResponseEntity<String> toWriteLog(@PathVariable(name = "menuItemNum") int menuItemNum) {
        try {
            if (gameResultService.getFinishChecker() == 0) {
                throw new NotFinishedException();
            } else {
                String json = Logger.toWriteTheLog(playerService.readAll(), stepService.readAll(),
                        gameResultService.getFinishChecker(), menuItemNum);
                return new ResponseEntity<>(GameConstants.SUCCESSFULLY_WRITTEN + json, HttpStatus.OK);
            }
        } catch (NotFinishedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(GameConstants.NOT_FINISHED, HttpStatus.LOCKED);
        }
    }

    @GetMapping(value = "/gameplay/simulate/info")
    public ResponseEntity<String> toSimulateTheGameInfo() {

        return new ResponseEntity<>(GameConstants.SELECT_THE_LOG_TO_READ, HttpStatus.OK);
    }

    @GetMapping(value = "/gameplay/simulate/{menuItemNum}")
    public ResponseEntity<String> toSimulateTheGame(@PathVariable(name = "menuItemNum") int menuItemNum) {

        return new ResponseEntity<>(GameSimulator.toBuildGameSimulation(menuItemNum), HttpStatus.OK);
    }

}