package app.services;

import app.models.GameResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameResultServiceImpl implements GameResultService {
    private static final List<GameResult> RESULTS = new ArrayList<>();
    private int finishChecker = 0;

    @Override
    public GameResult getGameResult() {
        return RESULTS.get(RESULTS.size() - 1);
    }

    @Override
    public void add(GameResult gameResult) {
        RESULTS.add(gameResult);
    }

    @Override
    public List<GameResult> readAll() {
        return RESULTS;
    }

    @Override
    public void deleteAll() {
        RESULTS.clear();
    }

    @Override
    public void setFinishChecker(int finishChecker) {
        this.finishChecker = finishChecker;
    }

    @Override
    public int getFinishChecker() {
        return finishChecker;
    }

    @Override
    public ResponseEntity<String> toCheckIsSomeoneWon(int playerId, GameboardService gameboardService,
                                                      PlayerService playerService) {
        if (this.getFinishChecker() == 2) {
            RESULTS.add(new GameResult("Draw!"));
            return new ResponseEntity<>("\n" + gameboardService.read() + "Draw!" + "\nИгра окончена", HttpStatus.OK);
        } else if (this.getFinishChecker() == 1) {
            RESULTS.add(new GameResult(playerService.read(playerId).toString()));
            return new ResponseEntity<>("\n" + gameboardService.read() + "\n" + playerService.read(playerId).getName() + " won\nИгра окончена", HttpStatus.OK);
        }
        return null;
    }
}
