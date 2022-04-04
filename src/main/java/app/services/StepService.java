package app.services;

import app.models.Step;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StepService {

    List<Step> readAll();

    Step read(int stepNum);

    boolean delete(int stepNum);

    void deleteAll();

    void toMakeNewStep(Step step, int playerId,
                              GameResultService gameResultService, GameboardService gameboardService);

    ResponseEntity<String> toRunMakeNewStepChecks
            (int playerId, GameboardService gameboardService,
             PlayerService playerService, GameResultService gameResultService);

}
