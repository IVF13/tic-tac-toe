package TicTacToeApp.RestAPI.Services;

import TicTacToeApp.Objects.Step;

import java.util.List;

public interface StepService {
    void create(Step step);

    List<Step> readAll();

    Step read(int stepNum);

    boolean delete(int stepNum);

    void deleteAll();
}
