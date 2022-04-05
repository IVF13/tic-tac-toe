package app.services;

import app.exceptions.AlreadyFinishedException;
import app.exceptions.NotLaucnhedException;
import app.exceptions.NotNamedPlayersException;
import app.exceptions.TurnsOrderException;
import app.game.TicTacToe;
import app.models.Step;
import app.utils.GameConstants;
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

        try {
            if (gameboardService.getGameboard() == null) {
                throw new NotLaucnhedException();
            } else if ((playerService.read(1) == null)) {
                throw new NotNamedPlayersException();
            } else if (gameResultService.getFinishChecker() != 0) {
                throw new AlreadyFinishedException();
            } else if (!(this.readAll().size() % 2 == playerId - 1)) {
                throw new TurnsOrderException();
            }
        } catch (NotLaucnhedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(GameConstants.LAUNCH_AT_FIRST, HttpStatus.LOCKED);
        } catch (NotNamedPlayersException e) {
            e.printStackTrace();
            return new ResponseEntity<>(GameConstants.NAME_PLAYERS, HttpStatus.LOCKED);
        } catch (AlreadyFinishedException e) {
            e.printStackTrace();
            return new ResponseEntity<>((gameboardService.read()
                    + "\n" + GameConstants.ALREADY_FINISHED), HttpStatus.LOCKED);
        } catch (TurnsOrderException e) {
            e.printStackTrace();
            return new ResponseEntity<>(gameboardService.read()
                    + "\n" + GameConstants.NOT_YOUR_TURN, HttpStatus.LOCKED);
        }

        return null;
    }

}
