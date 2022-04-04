package app.services;


import app.models.GameResult;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GameResultService {

    GameResult getGameResult();

    void add(GameResult gameResult);

    List<GameResult> readAll();

    void deleteAll();

    int getFinishChecker();

    void setFinishChecker(int finishChecker);


    ResponseEntity<String> toCheckIsSomeoneWon(int playerId,
                                               GameboardService gameboardService, PlayerService playerService);
}
