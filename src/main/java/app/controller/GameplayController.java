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
public class GameplayController {
    static PlayerService playerService = null;
    static GameboardService gameboardService = null;
    static StepService stepService = null;
    static GameResultService gameResultService = null;

    @Autowired
    public GameplayController(PlayerService playerService, GameboardService gameboardService,
                              StepService stepService, GameResultService gameResultService) {
        GameplayController.playerService = playerService;
        GameplayController.gameboardService = gameboardService;
        GameplayController.stepService = stepService;
        GameplayController.gameResultService = gameResultService;
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
            return entity;
        }

        return new ResponseEntity<>(gameboardService.read(), HttpStatus.OK);
    }

}