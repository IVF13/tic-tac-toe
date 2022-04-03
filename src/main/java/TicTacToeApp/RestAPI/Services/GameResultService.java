package TicTacToeApp.RestAPI.Services;


import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GameResultService {

    String getGameResult();

    void add(String gameResult);

    List<String> readAll();

    void deleteAll();

    int getFinishChecker();

    void setFinishChecker(int finishChecker);


    ResponseEntity<String> toCheckIsSomeoneWin(int playerId,
                                               GameboardService gameboardService, PlayerService playerService);
}
