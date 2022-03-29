package TicTacToeApp.RestAPI.Services;

import TicTacToeApp.Objects.Step;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StepServiceImpl implements StepService {
    private static final List<Step> STEPS = new ArrayList<>();

    @Override
    public void create(Step step) {
        STEPS.add(step);
    }

    @Override
    public List<Step> readAll() {
        return new ArrayList<Step>(STEPS);
    }

    @Override
    public boolean delete(int stepNum) {
        return STEPS.remove(stepNum) != null;
    }

}
