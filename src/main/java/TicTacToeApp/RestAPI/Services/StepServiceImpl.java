package TicTacToeApp.RestAPI.Services;

import TicTacToeApp.Models.Step;
import TicTacToeApp.TicTacToe;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StepServiceImpl implements StepService {
    private static final List<Step> STEPS = new ArrayList<>();

    @Override
    public void toMakeNewStep(Step step, int playerId,
                              GameResultService gameResultService, GameboardService gameboardService) {
        STEPS.add(new Step(this.readAll().size() + 1, playerId, step.getCell()));
        gameResultService.setFinishChecker(TicTacToe.toCheckWin(gameboardService.getGameboard(),
                this.readAll().size()));
    }

    @Override
    public List<Step> readAll() {
        return new ArrayList<>(STEPS);
    }

    @Override
    public Step read(int stepNum) {
        return STEPS.get(stepNum - 1);
    }

    @Override
    public boolean delete(int stepNum) {
        return STEPS.remove(stepNum) != null;
    }

    @Override
    public void deleteAll() {
        STEPS.clear();
    }

    @Override
    public ResponseEntity<String> toRunMakeNewStepChecks
            (int playerId, GameboardService gameboardService,
             PlayerService playerService, GameResultService gameResultService) {
        if (gameboardService.getGameboard() == null) {
            return new ResponseEntity<>("Сначала запустите игру", HttpStatus.LOCKED);
        } else if ((playerService.read(1) == null)) {
            return new ResponseEntity<>("Задайте имена игрокам", HttpStatus.LOCKED);
        } else if (gameResultService.getFinishChecker() != 0) {
            return new ResponseEntity<>((gameboardService.read()
                    + "\nИгра окончена, вы можете перезапустить её"), HttpStatus.LOCKED);
        } else if (!(this.readAll().size() % 2 == playerId - 1)) {
            return new ResponseEntity<>(gameboardService.read()
                    + "\nОшибка, сейчас не Ваш ход", HttpStatus.LOCKED);
        }
        return null;
    }

}
