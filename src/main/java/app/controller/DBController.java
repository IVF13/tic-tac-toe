package app.controller;

import app.exceptions.NotFinishedException;
import app.exceptions.NotFoundException;
import app.models.GameResult;
import app.models.GameplayData;
import app.models.Player;
import app.repository.GameplayDataRepository;
import app.services.GameplayDataService;
import app.utils.GameConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static app.controller.GameplayController.gameResultService;
import static app.controller.GameplayController.playerService;
import static app.controller.GameplayController.stepService;

@RestController
public class DBController {
    private static GameplayDataService gameplayDataService = null;
    private static GameplayDataRepository gameplayDataRepository = null;

    @Autowired
    public DBController(GameplayDataService gameplayDataService, GameplayDataRepository gameplayDataRepository) {
        DBController.gameplayDataService = gameplayDataService;
        DBController.gameplayDataRepository = gameplayDataRepository;
    }

    @PutMapping(value = "/gameplay/save/last/game/to/db")
    public ResponseEntity<String> saveResultsToDB() {
        try {
            if (gameResultService.readAll().isEmpty()) {
                throw new NotFinishedException();
            } else {
                GameplayData gameplayData = new GameplayData();
                playerService.readAll().forEach(x -> x.setGameplayData(gameplayData));
                stepService.readAll().forEach(x -> x.setGameplayData(gameplayData));

                gameplayData.setPlayers(playerService.readAll());
                gameplayData.setStepsToWrite(stepService.readAll());
                gameplayData.setGameResult(List.of(gameResultService.getGameResult()));

                GameplayData savedGameplay = gameplayDataRepository.save(gameplayData);

                if (savedGameplay.equals(gameplayData)) {
                    return new ResponseEntity<>(GameConstants.RESULTS_WERE_SAVED, HttpStatus.OK);
                } else {
                    throw new Exception();
                }
            }
        } catch (NotFinishedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(GameConstants.MUST_BE_FINISHED_AT_FIRST, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(GameConstants.ERROR, HttpStatus.NOT_MODIFIED);
        }
    }

    @GetMapping(value = "/gameplay/find/game/byId/in/db")
    public ResponseEntity<String> findByPlayerInDB(@RequestBody Long id) {
        try {
            if (gameplayDataRepository.findById(id).isPresent()) {
                GameplayData gameplayData = gameplayDataService.findById(id);
                return new ResponseEntity<>(gameplayData.toString(), HttpStatus.OK);
            } else {
                throw new NotFoundException();
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(GameConstants.NOT_FOUND, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/gameplay/findAll/games/in/db")
    public ResponseEntity<String> findAllInDB() {
        try {
            if (!gameplayDataRepository.findAll().isEmpty()) {
                List<GameplayData> gameplayDataList = gameplayDataService.findAll();
                return new ResponseEntity<>(gameplayDataList.toString(), HttpStatus.OK);
            } else {
                throw new NotFoundException();
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(GameConstants.NOT_FOUND, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/gameplay/find/game/byPlayer/in/db")
    public ResponseEntity<String> findByPlayerInDB(@RequestBody Player player) {
        try {
            if (!gameplayDataRepository.findByPlayer(player).isEmpty()) {
                List<GameplayData> gameplayDataList = gameplayDataService.findByPlayer(player);
                return new ResponseEntity<>(gameplayDataList.toString(), HttpStatus.OK);
            } else {
                throw new NotFoundException();
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(GameConstants.NOT_FOUND, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/gameplay/find/game/byGameResult/in/db")
    public ResponseEntity<String> findByGameResultInDB(@RequestBody GameResult gameResult) {
        try {
            if (!gameplayDataRepository.findByGameResult(gameResult).isEmpty()) {
                List<GameplayData> gameplayDataList = gameplayDataService.findByGameResult(gameResult);
                return new ResponseEntity<>(gameplayDataList.toString(), HttpStatus.OK);
            } else {
                throw new NotFoundException();
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(GameConstants.NOT_FOUND, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/gameplay/delete/game/byId/from/db")
    public ResponseEntity<String> deleteByIdFromDB(@RequestBody Long id) {
        try {
            if (!gameplayDataRepository.findById(id).isEmpty()) {
                gameplayDataRepository.deleteById(id);
                return new ResponseEntity<>(GameConstants.GAMEPLAY_DATA_WAS_DELETED, HttpStatus.OK);
            } else {
                throw new NotFoundException();
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(GameConstants.NOT_FOUND, HttpStatus.OK);
        }
    }

}
