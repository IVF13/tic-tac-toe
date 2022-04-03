package TicTacToeApp.RestAPI.Services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameResultServiceImpl implements GameResultService {
    private static final List<String> RESULTS = new ArrayList<>();
    private int finishChecker = 0;

    @Override
    public String getGameResult() {
        return RESULTS.get(RESULTS.size() - 1);
    }

    @Override
    public void add(String gameResult) {
        RESULTS.add(gameResult);
    }

    @Override
    public List<String> readAll() {
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
    public ResponseEntity<String> toCheckIsSomeoneWin(int playerId, GameboardService gameboardService,
                                                      PlayerService playerService) {
        if (this.getFinishChecker() == 2) {
            this.add("\n" + gameboardService.read() + "\nНичья\nИгра окончена");
            return new ResponseEntity<>(this.getGameResult(), HttpStatus.OK);
        } else if (this.getFinishChecker() == 1) {
            this.add("\n" + gameboardService.read() + "\n" +
                    playerService.read(playerId).getName() + " победил\nИгра окончена");
            return new ResponseEntity<>(this.getGameResult(), HttpStatus.OK);
        }
        return null;
    }
}
